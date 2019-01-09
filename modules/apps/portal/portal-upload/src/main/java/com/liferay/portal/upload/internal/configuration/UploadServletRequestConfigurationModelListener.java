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

package com.liferay.portal.upload.internal.configuration;

import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListener;
import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListenerException;
import com.liferay.portal.kernel.util.LocaleThreadLocal;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.upload.UploadServletRequestImpl;

import java.io.File;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.Dictionary;
import java.util.ResourceBundle;

import org.osgi.service.component.annotations.Component;

/**
 * @author Pei-Jung Lan
 */
@Component(
	immediate = true,
	property = "model.class.name=com.liferay.portal.upload.internal.configuration.UploadServletRequestConfiguration",
	service = ConfigurationModelListener.class
)
public class UploadServletRequestConfigurationModelListener
	implements ConfigurationModelListener {

	@Override
	public void onAfterSave(String pid, Dictionary<String, Object> properties)
		throws ConfigurationModelListenerException {

		String tempDir = (String)properties.get("tempDir");

		if (Validator.isNotNull(tempDir)) {
			UploadServletRequestImpl.setTempDir(new File(tempDir));
		}
	}

	@Override
	public void onBeforeSave(String pid, Dictionary<String, Object> properties)
		throws ConfigurationModelListenerException {

		String tempDir = (String)properties.get("tempDir");

		try {
			if (Validator.isNotNull(tempDir)) {
				_validateFolder(tempDir);
			}
		}
		catch (Exception e) {
			throw new ConfigurationModelListenerException(
				e.getMessage(), UploadServletRequestConfiguration.class,
				getClass(), properties);
		}
	}

	private String _getLocalizedResourceBundleMessage(String key) {
		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			"content.Language", LocaleThreadLocal.getThemeDisplayLocale(),
			getClass());

		String message = ResourceBundleUtil.getString(resourceBundle, key);

		return message;
	}

	private void _validateFolder(String tempDir) throws Exception {
		File file = new File(tempDir);

		String message = null;

		if (!file.exists()) {
			message = _getLocalizedResourceBundleMessage(
				"temporary-directory-must-exist");
		}
		else if (!file.isDirectory()) {
			message = _getLocalizedResourceBundleMessage(
				"path-must-be-a-directory");
		}
		else if (!Files.isWritable(Paths.get(tempDir))) {
			message = _getLocalizedResourceBundleMessage(
				"path-must-be-writable");
		}

		if (message != null) {
			throw new Exception(message);
		}
	}

}