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

package com.liferay.portal.servlet.filters.aggregate;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ServiceProxyFactory;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Cleydyr de Albuquerque
 */
@PrepareForTest({PortalUtil.class, ServiceProxyFactory.class})
@RunWith(PowerMockRunner.class)
public class AggregateFilterTest extends PowerMockito {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		setUpPortalUtil();

		setUpServiceProxyFactory();
	}

	@Test
	public void testAggregateCss() throws IOException {
		String cssFileName = "./my-styles.css";

		String validCss = "body {color: black;}";

		ServletPaths servletPaths = _createMockServletPaths(
			cssFileName, validCss);

		String httpURL = "https://example.com/";

		String[] contents = {
			_wrapInImport(cssFileName, StringPool.QUOTE),
			_wrapInImport(httpURL, StringPool.APOSTROPHE),
			_wrapInImport(httpURL, StringPool.QUOTE),
			_wrapInImport(httpURL, StringPool.BLANK)
		};

		String[] expected = {
			validCss, _wrapInImport(httpURL, StringPool.APOSTROPHE),
			_wrapInImport(httpURL, StringPool.QUOTE),
			_wrapInImport(httpURL, StringPool.BLANK)
		};

		for (int i = 0; i < contents.length; i++) {
			Assert.assertEquals(
				expected[i],
				AggregateFilter.aggregateCss(servletPaths, contents[i]));
		}
	}

	protected void setUpPortalUtil() {
		mockStatic(PortalUtil.class);

		PowerMockito.when(
			PortalUtil.getPathModule()
		).thenReturn(
			StringPool.BLANK
		);
	}

	protected void setUpServiceProxyFactory() {
		mockStatic(ServiceProxyFactory.class);

		when(
			ServiceProxyFactory.newServiceTrackedInstance(
				Matchers.any(), Matchers.any(), Matchers.anyString(),
				Matchers.anyBoolean())
		).thenReturn(
			null
		);
	}

	private ServletPaths _createMockServletPaths(
		String cssFileName, String validCss) {

		ServletPaths servletPaths = mock(ServletPaths.class);

		ServletPaths validCSSServletPath = mock(ServletPaths.class);

		when(
			validCSSServletPath.getContent()
		).thenReturn(
			validCss
		);

		when(
			validCSSServletPath.getResourcePath()
		).thenReturn(
			StringPool.BLANK
		);

		when(
			servletPaths.getResourcePath()
		).thenReturn(
			StringPool.BLANK
		);

		when(
			servletPaths.getContent()
		).thenReturn(
			null
		);

		when(
			servletPaths.down(Mockito.anyString())
		).thenReturn(
			servletPaths
		);

		when(
			servletPaths.down(StringPool.QUOTE + cssFileName + StringPool.QUOTE)
		).thenReturn(
			validCSSServletPath
		);

		return servletPaths;
	}

	private String _wrapInImport(String url, String delimiter) {
		return StringBundler.concat(
			"@import url(", delimiter, url, delimiter, ");");
	}

}