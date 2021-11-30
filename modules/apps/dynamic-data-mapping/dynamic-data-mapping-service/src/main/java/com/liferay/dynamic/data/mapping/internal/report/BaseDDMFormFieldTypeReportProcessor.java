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
import com.liferay.dynamic.data.mapping.model.DDMFormFieldOptions;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.dynamic.data.mapping.report.DDMFormFieldTypeReportProcessor;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;

import java.util.Map;
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

		fieldJSONObject.put(
			"chartComponentName", getChartComponentName()
		).put(
			"chartComponentProps",
			getChartComponentPropsJSONObject(fieldJSONObject, ddmFormFieldValue)
		).put(
			"icon", ddmFormFieldTypeProperties.get("ddm.form.field.type.icon")
		).put(
			"title", ddmFormFieldTypeProperties.get("ddm.form.field.type.label")
		);

		return fieldJSONObject;
	}

	protected abstract JSONObject doProcess(
			DDMFormFieldValue ddmFormFieldValue, JSONObject fieldJSONObject,
			long formInstanceRecordId, String ddmFormInstanceReportEvent)
		throws Exception;

	protected abstract String getChartComponentName();

	protected abstract JSONObject getChartComponentPropsJSONObject(
		JSONObject fieldJSONObject, DDMFormFieldValue ddmFormFieldValue);

	protected JSONObject getDDMFormFieldOptionLabelsJSONObject(
		DDMFormFieldOptions ddmFormFieldOptions) {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		int index = 0;

		for (String optionValue : ddmFormFieldOptions.getOptionsValues()) {
			jsonObject.put(
				optionValue,
				JSONUtil.put(
					"index", index++
				).put(
					"value",
					getValue(ddmFormFieldOptions.getOptionLabels(optionValue))
				));
		}

		return jsonObject;
	}

	protected String getValue(Value value) {
		return value.getString(value.getDefaultLocale());
	}

	protected JSONArray mapToValueProperty(JSONArray jsonArray) {
		JSONArray valuesJSONArray = JSONFactoryUtil.createJSONArray();

		if (jsonArray == null) {
			return valuesJSONArray;
		}

		for (Object object : jsonArray) {
			JSONObject valueJSONObject = (JSONObject)object;

			valuesJSONArray.put(valueJSONObject.get("value"));
		}

		return valuesJSONArray;
	}

	protected int sumTotalValues(JSONObject valuesJSONObject) {
		Stream<String> stream = valuesJSONObject.keySet(
		).stream();

		return stream.map(
			key -> valuesJSONObject.getInt(key)
		).reduce(
			Integer::sum
		).orElse(
			0
		);
	}

	protected JSONArray toDataArray(
		DDMFormFieldOptions ddmFormFieldOptions, JSONObject valuesJSONObject) {

		JSONArray dataJSONArray = JSONFactoryUtil.createJSONArray();

		if (ddmFormFieldOptions == null) {
			return dataJSONArray;
		}

		JSONObject optionsJSONObject = getDDMFormFieldOptionLabelsJSONObject(
			ddmFormFieldOptions);

		Stream<String> stream = valuesJSONObject.keySet(
		).stream();

		stream.map(
			name -> {
				int count = valuesJSONObject.getInt(name);

				String label = name;

				JSONObject optionDataJSONObject =
					optionsJSONObject.getJSONObject(name);

				if (optionDataJSONObject != null) {
					label = optionDataJSONObject.getString("value");
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
		).forEachOrdered(
			dataJSONArray::put
		);

		return dataJSONArray;
	}

	@Reference
	protected DDMFormFieldTypeServicesTracker ddmFormFieldTypeServicesTracker;

}