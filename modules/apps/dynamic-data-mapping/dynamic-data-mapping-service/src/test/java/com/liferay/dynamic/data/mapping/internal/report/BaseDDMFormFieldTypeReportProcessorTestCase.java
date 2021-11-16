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

import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTypeServicesTracker;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.portal.kernel.util.HashMapBuilder;

import org.junit.Before;

import org.powermock.api.mockito.PowerMockito;

/**
 * @author Cleydyr de Albuquerque
 */
public abstract class BaseDDMFormFieldTypeReportProcessorTestCase
	extends PowerMockito {

	@Before
	public void setUp() throws Exception {
		DDMFormFieldTypeServicesTracker ddmFormFieldTypeServicesTracker = mock(
			DDMFormFieldTypeServicesTracker.class);

		when(
			ddmFormFieldTypeServicesTracker.getDDMFormFieldTypeProperties(
				getFieldTypeName())
		).thenReturn(
			HashMapBuilder.<String, Object>put(
				"ddm.form.field.type.icon", getFieldTypeIcon()
			).put(
				"ddm.form.field.type.label", getFieldTypeLabel()
			).put(
				"ddm.form.field.type.name", getFieldTypeName()
			).build()
		);

		BaseDDMFormFieldTypeReportProcessor
			baseDDMFormFieldTypeReportProcessor =
				getBaseDDMFormFieldTypeReportProcessor();

		baseDDMFormFieldTypeReportProcessor.ddmFormFieldTypeServicesTracker =
			ddmFormFieldTypeServicesTracker;

		doSetUp();
	}

	protected abstract void doSetUp() throws Exception;

	protected abstract BaseDDMFormFieldTypeReportProcessor
		getBaseDDMFormFieldTypeReportProcessor();

	protected abstract String getFieldTypeIcon();

	protected abstract String getFieldTypeLabel();

	protected abstract String getFieldTypeName();

	protected void mockDDMFormField(DDMFormFieldValue ddmFormFieldValue) {
		DDMFormField ddmFormField = mock(DDMFormField.class);

		when(
			ddmFormField.getDDMFormFieldOptions()
		).thenReturn(
			null
		);

		when(
			ddmFormFieldValue.getDDMFormField()
		).thenReturn(
			ddmFormField
		);
	}

}