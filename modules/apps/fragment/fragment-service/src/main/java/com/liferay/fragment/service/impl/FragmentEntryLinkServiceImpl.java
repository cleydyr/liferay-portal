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

package com.liferay.fragment.service.impl;

import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.service.base.FragmentEntryLinkServiceBaseImpl;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalService;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.BaseModelPermissionCheckerUtil;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.permission.LayoutPermissionUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.segments.constants.SegmentsExperienceConstants;

import java.util.Map;
import java.util.Objects;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pavel Savinov
 */
@Component(
	property = {
		"json.web.service.context.name=fragment",
		"json.web.service.context.path=FragmentEntryLink"
	},
	service = AopService.class
)
public class FragmentEntryLinkServiceImpl
	extends FragmentEntryLinkServiceBaseImpl {

	@Override
	public FragmentEntryLink addFragmentEntryLink(
			long groupId, long originalFragmentEntryLinkId,
			long fragmentEntryId, long segmentsExperienceId, long classNameId,
			long classPK, String css, String html, String js,
			String configuration, String editableValues, String namespace,
			int position, String rendererKey, ServiceContext serviceContext)
		throws PortalException {

		_checkPermission(
			groupId, _portal.getClassName(classNameId), classPK, false);

		return fragmentEntryLinkLocalService.addFragmentEntryLink(
			getUserId(), groupId, originalFragmentEntryLinkId, fragmentEntryId,
			segmentsExperienceId, classNameId, classPK, css, html, js,
			configuration, editableValues, namespace, position, rendererKey,
			serviceContext);
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #addFragmentEntryLink(long, long, long, long, long, long,
	 *             String, String, String, String, String, String, int, String,
	 *             ServiceContext)}
	 */
	@Deprecated
	@Override
	public FragmentEntryLink addFragmentEntryLink(
			long groupId, long originalFragmentEntryLinkId,
			long fragmentEntryId, long classNameId, long classPK, String css,
			String html, String js, String configuration, String editableValues,
			String namespace, int position, String rendererKey,
			ServiceContext serviceContext)
		throws PortalException {

		return addFragmentEntryLink(
			groupId, originalFragmentEntryLinkId, fragmentEntryId,
			SegmentsExperienceConstants.ID_DEFAULT, classNameId, classPK, css,
			html, js, configuration, editableValues, namespace, position,
			rendererKey, serviceContext);
	}

	@Override
	public FragmentEntryLink deleteFragmentEntryLink(long fragmentEntryLinkId)
		throws PortalException {

		FragmentEntryLink fragmentEntryLink =
			fragmentEntryLinkPersistence.findByPrimaryKey(fragmentEntryLinkId);

		_checkPermission(
			fragmentEntryLink.getGroupId(), fragmentEntryLink.getClassName(),
			fragmentEntryLink.getClassPK(), false);

		return fragmentEntryLinkLocalService.deleteFragmentEntryLink(
			fragmentEntryLinkId);
	}

	@Override
	public FragmentEntryLink updateFragmentEntryLink(
			long fragmentEntryLinkId, String editableValues)
		throws PortalException {

		return updateFragmentEntryLink(
			fragmentEntryLinkId, editableValues, true);
	}

	@Override
	public FragmentEntryLink updateFragmentEntryLink(
			long fragmentEntryLinkId, String editableValues,
			boolean updateClassedModel)
		throws PortalException {

		FragmentEntryLink fragmentEntryLink =
			fragmentEntryLinkPersistence.findByPrimaryKey(fragmentEntryLinkId);

		_checkPermission(
			fragmentEntryLink.getGroupId(), fragmentEntryLink.getClassName(),
			fragmentEntryLink.getClassPK(), true);

		return fragmentEntryLinkLocalService.updateFragmentEntryLink(
			fragmentEntryLinkId, editableValues, updateClassedModel);
	}

	@Override
	public void updateFragmentEntryLinks(
			long groupId, long classNameId, long classPK,
			long[] fragmentEntryIds, String editableValues,
			ServiceContext serviceContext)
		throws PortalException {

		_checkPermission(
			groupId, _portal.getClassName(classNameId), classPK, true);

		fragmentEntryLinkLocalService.updateFragmentEntryLinks(
			getUserId(), groupId, classNameId, classPK, fragmentEntryIds,
			editableValues, serviceContext);
	}

	@Override
	public void updateFragmentEntryLinks(
			Map<Long, String> fragmentEntryLinksEditableValuesMap)
		throws PortalException {

		for (Map.Entry<Long, String> entry :
				fragmentEntryLinksEditableValuesMap.entrySet()) {

			FragmentEntryLink fragmentEntryLink =
				fragmentEntryLinkPersistence.findByPrimaryKey(entry.getKey());

			_checkPermission(
				fragmentEntryLink.getGroupId(),
				fragmentEntryLink.getClassName(),
				fragmentEntryLink.getClassPK(), true);
		}

		fragmentEntryLinkLocalService.updateFragmentEntryLinks(
			fragmentEntryLinksEditableValuesMap);
	}

	private void _checkPermission(
			long groupId, String className, long classPK,
			boolean checkUpdateLayoutContentPermission)
		throws PortalException {

		Layout layout = _layoutLocalService.getLayout(classPK);

		layout = _getPublishedLayout(layout);

		String className1 = className;
		long classPK1 = layout.getPlid();

		if (layout.isTypeAssetDisplay()) {
			LayoutPageTemplateEntry layoutPageTemplateEntry =
				_getLayoutPageTemplateEntry(layout);

			className1 = LayoutPageTemplateEntry.class.getName();
			classPK1 = layoutPageTemplateEntry.getLayoutPageTemplateEntryId();
		}

		Boolean containsPermission = Boolean.valueOf(
			BaseModelPermissionCheckerUtil.containsBaseModelPermission(
				getPermissionChecker(), groupId, className1, classPK1,
				ActionKeys.UPDATE));

		if (checkUpdateLayoutContentPermission &&
			Objects.equals(className1, Layout.class.getName())) {

			containsPermission =
				containsPermission ||
				LayoutPermissionUtil.contains(
					getPermissionChecker(), classPK1,
					ActionKeys.UPDATE_LAYOUT_CONTENT);
		}

		if ((containsPermission == null) || !containsPermission) {
			throw new PrincipalException.MustHavePermission(
				getUserId(), className1, classPK1, ActionKeys.UPDATE);
		}
	}

	private LayoutPageTemplateEntry _getLayoutPageTemplateEntry(Layout layout) {
		return _layoutPageTemplateEntryLocalService.
			fetchLayoutPageTemplateEntryByPlid(layout.getPlid());
	}

	private Layout _getPublishedLayout(Layout layout) {
		long classPK = layout.getClassPK();

		if (classPK != 0) {
			return _layoutLocalService.fetchLayout(classPK);
		}

		return layout;
	}

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private LayoutPageTemplateEntryLocalService
		_layoutPageTemplateEntryLocalService;

	@Reference
	private Portal _portal;

}