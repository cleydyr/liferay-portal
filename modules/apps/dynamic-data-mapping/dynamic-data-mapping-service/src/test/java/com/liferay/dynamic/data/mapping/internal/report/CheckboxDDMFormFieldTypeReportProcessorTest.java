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

package com.liferay.dynamic.data.mapping.internal.report;

import com.liferay.dynamic.data.mapping.constants.DDMFormInstanceReportConstants;
import com.liferay.dynamic.data.mapping.form.field.type.constants.DDMFormFieldTypeConstants;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.LocaleUtil;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Rodrigo Paulino
 */
@PrepareForTest(LanguageUtil.class)
@RunWith(PowerMockRunner.class)
public class CheckboxDDMFormFieldTypeReportProcessorTest
	extends BaseDDMFormFieldTypeReportProcessorTestCase {

	@Override
	public void doSetUp() throws Exception {
		_setUpJSONFactoryUtil();
		_setUpLanguageUtil();
	}

	private void _setUpLanguageUtil() {
		when(_language.get(Mockito.any(Locale.class), Mockito.eq(StringPool.FALSE))).thenReturn("False");
		when(_language.get(Mockito.any(Locale.class), Mockito.eq(StringPool.TRUE))).thenReturn("True");
	
		LanguageUtil languageUtil = new LanguageUtil();

		languageUtil.setLanguage(_language);
	}

	@Test
	public void testProcessDDMFormInstanceReportOnDeleteEvent()
		throws Exception {

		JSONObject processedJSONObject =
			_checkboxDDMFormFieldTypeReportProcessor.process(
				_mockDDMFormFieldValue("true"),
				JSONUtil.put("values", JSONUtil.put("true", 1)), 0,
				DDMFormInstanceReportConstants.EVENT_DELETE_RECORD_VERSION);

		JSONObject valuesJSONObject = processedJSONObject.getJSONObject(
			"values");

		Assert.assertEquals(0, valuesJSONObject.getLong("true"));
	}

	@Test
	public void testProcessDDMFormInstanceReportWithExistingData()
		throws Exception {

		JSONObject processedJSONObject =
			_checkboxDDMFormFieldTypeReportProcessor.process(
				_mockDDMFormFieldValue("true"),
				JSONUtil.put("values", JSONUtil.put("true", 1)), 0,
				DDMFormInstanceReportConstants.EVENT_ADD_RECORD_VERSION);

		JSONObject valuesJSONObject = processedJSONObject.getJSONObject(
			"values");

		Assert.assertEquals(2, valuesJSONObject.getLong("true"));
	}

	@Test
	public void testProcessDDMFormInstanceReportWithFalseValue()
		throws Exception {

		JSONObject processedJSONObject =
			_checkboxDDMFormFieldTypeReportProcessor.process(
				_mockDDMFormFieldValue("something other than true"),
				JSONUtil.put(
					"type", DDMFormFieldTypeConstants.CHECKBOX
				).put(
					"values", JSONFactoryUtil.createJSONObject()
				),
				0, DDMFormInstanceReportConstants.EVENT_ADD_RECORD_VERSION);

		JSONObject valuesJSONObject = processedJSONObject.getJSONObject(
			"values");

		Assert.assertEquals(1, valuesJSONObject.getLong("false"));
	}

	@Test
	public void testProcessDDMFormInstanceReportWithoutPreviousData()
		throws Exception {

		JSONObject processedJSONObject =
			_checkboxDDMFormFieldTypeReportProcessor.process(
				_mockDDMFormFieldValue("true"),
				JSONUtil.put("values", JSONFactoryUtil.createJSONObject()), 0,
				DDMFormInstanceReportConstants.EVENT_ADD_RECORD_VERSION);

		JSONObject valuesJSONObject = processedJSONObject.getJSONObject(
			"values");

		Assert.assertEquals(1, valuesJSONObject.getLong("true"));
	}

	@Override
	protected BaseDDMFormFieldTypeReportProcessor
		getBaseDDMFormFieldTypeReportProcessor() {

		return _checkboxDDMFormFieldTypeReportProcessor;
	}

	@Override
	protected String getFieldTypeIcon() {
		return "check";
	}

	@Override
	protected String getFieldTypeLabel() {
		return "boolean";
	}

	@Override
	protected String getFieldTypeName() {
		return DDMFormFieldTypeConstants.CHECKBOX;
	}

	private DDMFormFieldValue _mockDDMFormFieldValue(String value) {
		DDMFormFieldValue ddmFormFieldValue = mock(DDMFormFieldValue.class);

		when(
			ddmFormFieldValue.getName()
		).thenReturn(
			"boolean"
		);

		when(
			ddmFormFieldValue.getType()
		).thenReturn(
			DDMFormFieldTypeConstants.CHECKBOX
		);

		LocalizedValue localizedValue = new LocalizedValue();

		localizedValue.addString(LocaleUtil.US, value);
		localizedValue.setDefaultLocale(LocaleUtil.US);

		when(
			ddmFormFieldValue.getValue()
		).thenReturn(
			localizedValue
		);

		mockDDMFormField(ddmFormFieldValue);

		return ddmFormFieldValue;
	}

	private void _setUpJSONFactoryUtil() {
		JSONFactoryUtil jsonFactoryUtil = new JSONFactoryUtil();

		jsonFactoryUtil.setJSONFactory(new JSONFactoryImpl());
	}

	private final CheckboxDDMFormFieldTypeReportProcessor
		_checkboxDDMFormFieldTypeReportProcessor =
			new CheckboxDDMFormFieldTypeReportProcessor();

	@Mock
	private Language _language;
}