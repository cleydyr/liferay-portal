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

package com.liferay.gradle.plugins.workspace.tasks;

import com.liferay.gradle.plugins.workspace.internal.util.GradleUtil;
import com.liferay.gradle.util.FileUtil;
import com.liferay.portal.tools.bundle.support.constants.BundleSupportConstants;

import java.io.File;

import java.net.URI;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.JavaExec;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.OutputDirectory;

/**
 * @author David Truong
 */
public class InitBundleTask extends JavaExec {

	public InitBundleTask() {
		setMain("com.liferay.portal.tools.bundle.support.BundleSupport");
	}

	@Override
	public void exec() {
		setArgs(_getCompleteArgs());

		super.exec();
	}

	@Input
	public String getConfigEnvironment() {
		return GradleUtil.toString(_configEnvironment);
	}

	@Optional
	public File getConfigsDir() {
		return GradleUtil.toFile(getProject(), _configsDir);
	}

	public File getDestinationDir() {
		return GradleUtil.toFile(getProject(), _homeDir);
	}

	@InputFile
	public File getFile() {
		return GradleUtil.toFile(getProject(), _file);
	}

	@OutputDirectory
	public File getHomeDir() {
		return GradleUtil.toFile(getProject(), _homeDir);
	}

	@Input
	@Optional
	public int getStripComponents() {
		return GradleUtil.toInteger(_stripComponents);
	}

	@Input
	@Optional
	public FileCollection getProvidedModules() {
		return _providedModules;
	}

	public void setConfigEnvironment(Object configEnvironment) {
		_configEnvironment = configEnvironment;
	}

	public void setConfigsDir(Object configsDir) {
		_configsDir = configsDir;
	}

	public void setFile(Object file) {
		_file = file;
	}

	public void setHomeDir(Object homeDir) {
		_homeDir = homeDir;
	}

	public void setProvidedModules(FileCollection providedModules) {
		_providedModules = providedModules;
	}

	public void setStripComponents(Object stripComponents) {
		_stripComponents = stripComponents;
	}

	private List<String> _getCompleteArgs() {
		List<String> args = new ArrayList<>(getArgs());

		args.add("initBundle");

		File configsDir = getConfigsDir();

		if (configsDir != null) {
			args.add("--configs");
			args.add(FileUtil.getAbsolutePath(configsDir));
		}

		args.add("--environment");
		args.add(getConfigEnvironment());
		args.add("--liferay");
		args.add(FileUtil.getAbsolutePath(getHomeDir()));

		FileCollection providedModules = getProvidedModules();

		System.out.println(providedModules.getAsPath());

		args.add("--provided-modules");
		args.add(providedModules.getAsPath());

		args.add("--strip-components");
		args.add(String.valueOf(getStripComponents()));

		try {
			File file = getFile();

			URI uri = file.toURI();

			URL url = uri.toURL();

			args.add("--url");
			args.add(url.toString());
		}
		catch (Exception e) {
		}

		return args;
	}

	private Object _configEnvironment =
		BundleSupportConstants.DEFAULT_ENVIRONMENT;
	private Object _configsDir;
	private Object _file;
	private FileCollection _providedModules;
	private Object _homeDir;
	private Object _stripComponents =
		BundleSupportConstants.DEFAULT_STRIP_COMPONENTS;

}