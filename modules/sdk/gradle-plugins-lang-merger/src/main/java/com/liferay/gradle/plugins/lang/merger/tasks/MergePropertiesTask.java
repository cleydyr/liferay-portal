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

package com.liferay.gradle.plugins.lang.merger.tasks;

import com.liferay.gradle.util.FileUtil;

import java.io.File;
import java.io.IOException;

import java.util.Properties;
import java.util.Set;

import org.gradle.util.GUtil;

/**
 * @author Andrea Di Giorgi
 */
public class MergePropertiesTask extends BaseMergeTask {

	@Override
	public String getPattern() {
		return _PATTERN;
	}

	@Override
	protected void merge(Set<File> sourceFiles, File destinationFile)
		throws IOException {

		Properties mergedProperties = new Properties();

		for (File sourceFile : sourceFiles) {
			Properties sourceProperties = FileUtil.readProperties(sourceFile);

			mergedProperties.putAll(sourceProperties);
		}

		GUtil.saveProperties(mergedProperties, destinationFile);
	}

	private static final String _PATTERN = "*.properties";

}