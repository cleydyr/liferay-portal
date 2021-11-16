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
import com.liferay.dynamic.data.mapping.report.DDMFormFieldTypeReportProcessor;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Reference;

/**
 * @author Cleydyr de Albuquerque
 */
public abstract class BaseDDMFormFieldTypeReportProcessor
	implements DDMFormFieldTypeReportProcessor {

	public JSONObject process(
			DDMFormFieldValue ddmFormFieldValue, JSONObject fieldJSONObject,
			long formInstanceRecordId, String ddmFormInstanceReportEvent)
		throws Exception {

		doProcess(
			ddmFormFieldValue, fieldJSONObject, formInstanceRecordId,
			ddmFormInstanceReportEvent);

		Map<String, Object> ddmFormFieldTypeProperties =
			ddmFormFieldTypeServicesTracker.getDDMFormFieldTypeProperties(
				ddmFormFieldValue.getType());

		setIconAndLabelProperties(fieldJSONObject, ddmFormFieldTypeProperties);

		fieldJSONObject.put(
			"chartComponentName", getChartComponentName()
		).put(
			"chartComponentProps",
			getChartComponentPropsJSONObject(
				fieldJSONObject, ddmFormFieldTypeProperties)
		);

		return fieldJSONObject;
	}

	protected abstract JSONObject doProcess(
			DDMFormFieldValue ddmFormFieldValue, JSONObject fieldJSONObject,
			long formInstanceRecordId, String ddmFormInstanceReportEvent)
		throws Exception;

	protected abstract String getChartComponentName();

	protected abstract JSONObject getChartComponentPropsJSONObject(
		JSONObject fieldJSONObject,
		Map<String, Object> ddmFormFieldTypeProperties);

	protected JSONArray mapToValueProperty(JSONArray jsonArray) {
		JSONArray valuesJSONArray = JSONFactoryUtil.createJSONArray();

		for (Object object : jsonArray) {
			JSONObject valueJSONObject = (JSONObject)object;

			valuesJSONArray.put(valueJSONObject.get("value"));
		}

		return valuesJSONArray;
	}

	protected void setIconAndLabelProperties(
		JSONObject fieldJSONObject,
		Map<String, Object> ddmFormFieldTypeProperties) {

		fieldJSONObject.put(
			"icon", ddmFormFieldTypeProperties.get("ddm.form.field.type.icon")
		).put(
			"title", ddmFormFieldTypeProperties.get("ddm.form.field.type.label")
		);
	}

	protected JSONArray toDataArray(
		JSONObject optionsJSONObject, JSONObject valuesJSONObject) {

		Stream<String> stream = valuesJSONObject.keySet(
		).stream();

		return JSONFactoryUtil.createJSONArray(
			stream.map(
				name -> {
					int count = valuesJSONObject.getInt("count");

					String label = optionsJSONObject.getString(name);

					if (label == null) {
						label = name;
					}

					return JSONUtil.put(
						"count", count
					).put(
						"label", label
					);
				}
			).sorted(
				(jsonObject1, jsonObject2) ->
					jsonObject2.getInt("count") - jsonObject1.getInt("count")
			).collect(
				Collectors.toList()
			));
	}

	@Reference
	protected DDMFormFieldTypeServicesTracker ddmFormFieldTypeServicesTracker;

}