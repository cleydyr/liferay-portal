/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portlet.journalcontent.action;

import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletRequestModel;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.struts.ActionConstants;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.documentlibrary.service.DLAppServiceUtil;
import com.liferay.portlet.documentlibrary.util.DLUtil;
import com.liferay.portlet.documentlibrary.util.DocumentConversionUtil;
import com.liferay.portlet.journal.model.JournalArticleDisplay;
import com.liferay.portlet.journalcontent.util.JournalContentUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author Bruno Farache
 */
public class ExportArticleAction extends PortletAction {

	@Override
	public void processAction(
			ActionMapping actionMapping, ActionForm actionForm,
			PortletConfig portletConfig, ActionRequest actionRequest,
			ActionResponse actionResponse)
		throws Exception {

		try {
			long groupId = ParamUtil.getLong(actionRequest, "groupId");
			String articleId = ParamUtil.getString(actionRequest, "articleId");

			String targetExtension = ParamUtil.getString(
				actionRequest, "targetExtension");

			PortletPreferences portletPreferences =
				actionRequest.getPreferences();

			String[] allowedExtensions = portletPreferences.getValues(
				"extensions", null);

			String languageId = LanguageUtil.getLanguageId(actionRequest);
			PortletRequestModel portletRequestModel = new PortletRequestModel(
				actionRequest, actionResponse);
			ThemeDisplay themeDisplay =
				(ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
			HttpServletRequest request = PortalUtil.getHttpServletRequest(
				actionRequest);
			HttpServletResponse response = PortalUtil.getHttpServletResponse(
				actionResponse);

			getFile(
				groupId, articleId, targetExtension, allowedExtensions,
				languageId, portletRequestModel, themeDisplay, request,
				response);

			setForward(actionRequest, ActionConstants.COMMON_NULL);
		}
		catch (Exception e) {
			PortalUtil.sendError(e, actionRequest, actionResponse);
		}
	}

	protected void getFile(
			long groupId, String articleId, String targetExtension,
			String[] allowedExtensions, String languageId,
			PortletRequestModel portletRequestModel, ThemeDisplay themeDisplay,
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		try {
			JournalArticleDisplay articleDisplay =
				JournalContentUtil.getDisplay(
					groupId, articleId, null, "export", languageId, 1,
					portletRequestModel, themeDisplay);

			int pages = articleDisplay.getNumberOfPages();

			StringBundler sb = new StringBundler(pages + 12);

			sb.append("<html>");

			sb.append("<head>");
			sb.append("<meta content=\"");
			sb.append(ContentTypes.TEXT_HTML_UTF8);
			sb.append("\" http-equiv=\"content-type\" />");
			sb.append("<base href=\"");
			sb.append(themeDisplay.getPortalURL());
			sb.append("\" />");
			sb.append("</head>");

			sb.append("<body>");

			sb.append(articleDisplay.getContent());

			for (int i = 2; i <= pages; i++) {
				articleDisplay = JournalContentUtil.getDisplay(
					groupId, articleId, "export", languageId, i, themeDisplay);

				sb.append(articleDisplay.getContent());
			}

			sb.append("</body>");
			sb.append("</html>");

			String html = sb.toString();

			if (PropsValues.JOURNAL_ARTICLE_EXPORT_USE_DATA_URI_SCHEMA) {
				html = _insertDataURIs(sb.toString());
			}

			InputStream is = new UnsyncByteArrayInputStream(
				html.getBytes(StringPool.UTF8));

			String title = articleDisplay.getTitle();
			String sourceExtension = "html";

			String fileName = title.concat(StringPool.PERIOD).concat(
				sourceExtension);

			if (Validator.isNotNull(targetExtension) &&
				ArrayUtil.contains(allowedExtensions, targetExtension)) {

				String id = DLUtil.getTempFileId(
					articleDisplay.getId(),
					String.valueOf(articleDisplay.getVersion()), languageId);

				File convertedFile = DocumentConversionUtil.convert(
					id, is, sourceExtension, targetExtension);

				if (convertedFile != null) {
					fileName = title.concat(StringPool.PERIOD).concat(
						targetExtension);

					is = new FileInputStream(convertedFile);
				}
			}

			String contentType = MimeTypesUtil.getContentType(fileName);

			ServletResponseUtil.sendFile(
				request, response, fileName, is, contentType);
		}
		catch (Exception e) {
			_log.error(e, e);
		}
	}

	@Override
	protected boolean isCheckMethodOnProcessAction() {
		return _CHECK_METHOD_ON_PROCESS_ACTION;
	}

	private static void _appendRemainingImgTagAttributes(StringBundler sb,
			Attributes attributes) {

		for (Attribute attribute : attributes) {
			String attributeName = attribute.getName();

			if (!attributeName.equals("src")) {
				sb.append(attributeName);
				sb.append("=\"");
				sb.append(attribute.getValue());
				sb.append("\" ");
			}
		}
	}

	private static String _insertDataURIs(String html) {
		Source source = new Source(html);

		OutputDocument outputDocument = new OutputDocument(source);

		try {
			List<StartTag> imgTags = source.getAllStartTags(HTMLElementName.IMG);

			for (StartTag imgTag : imgTags) {
				Attributes attributes = imgTag.getAttributes();

				Attribute src = attributes.get("src");

				String srcAttribute = src.getValue();

				String path = HttpUtil.getPath(srcAttribute);

				if (_isDLPath(path)) {
					String[] pathArray = srcAttribute
											.split(StringPool.SLASH);
					long pathGroupId = GetterUtil.getLong(pathArray[_INDEX_OF_GROUP_ID]);
					String pathUuid = pathArray[_INDEX_OF_UUID];

					FileEntry fileEntry = DLAppServiceUtil.getFileEntryByUuidAndGroupId(pathUuid, pathGroupId);

					byte[] imageData = FileUtil.getBytes(fileEntry.getContentStream());

					String base64EncodedImageData = Base64.encode(imageData);

					StringBundler sb = new StringBundler(6 + 4*(attributes.size() - 1));

					sb.append("<img src=\"data:");
					sb.append(fileEntry.getMimeType());
					sb.append(";base64,");
					sb.append(base64EncodedImageData);
					sb.append("\" ");

					_appendRemainingImgTagAttributes(sb, attributes);

					sb.append("/>");

					outputDocument.replace(imgTag, sb.toString());
				}
			}

			outputDocument.toString();
		} catch (PrincipalException e) {
			_log.warn("User does not have permissions to view some of the web content files.");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return outputDocument.toString();
	}

	private static boolean _isDLPath(String path) {
		return path.startsWith("/documents");
	}

	private static final boolean _CHECK_METHOD_ON_PROCESS_ACTION = false;

	private static final int _INDEX_OF_GROUP_ID = 2;

	private static final int _INDEX_OF_UUID = 5;

	private static Log _log = LogFactoryUtil.getLog(ExportArticleAction.class);

}