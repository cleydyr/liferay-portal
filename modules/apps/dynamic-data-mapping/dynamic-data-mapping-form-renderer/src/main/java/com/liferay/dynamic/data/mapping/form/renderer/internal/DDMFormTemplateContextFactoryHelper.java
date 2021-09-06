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

package com.liferay.dynamic.data.mapping.form.renderer.internal;

import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldValidation;
import com.liferay.dynamic.data.mapping.model.DDMFormLayout;
import com.liferay.dynamic.data.mapping.model.DDMFormRule;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Leonardo Barros
 * @author Rafael Praxedes
 */
public class DDMFormTemplateContextFactoryHelper {

	public Set<String> getEvaluableDDMFormFieldNames(
		DDMForm ddmForm, DDMFormLayout ddmFormLayout) {

		Set<String> expressionParameters = new HashSet<>();

		Map<String, DDMFormField> ddmFormFieldsMap =
			ddmForm.getDDMFormFieldsMap(true);

		Set<String> ddmFormFieldNames = ddmFormFieldsMap.keySet();

		List<DDMFormRule> ddmFormRules = ddmFormLayout.getDDMFormRules();

		if (ListUtil.isEmpty(ddmFormRules)) {
			ddmFormRules = ddmForm.getDDMFormRules();
		}

		expressionParameters.addAll(
			getParametersByDDMFormRules(ddmFormRules));

		for (DDMFormField ddmFormField : ddmFormFieldsMap.values()) {
			if (isDDMFormFieldEvaluable(ddmFormField)) {
				expressionParameters.add(ddmFormField.getName());
			}

			expressionParameters.addAll(
				getParametersByExpression(
					ddmFormField.getVisibilityExpression()));
		}

		ddmFormFieldNames.retainAll(expressionParameters);

		return ddmFormFieldNames;
	}

	protected Set<String> getParametersByDDMFormRules(
		List<DDMFormRule> ddmFormRules) {

		Set<String> parameters = new HashSet<>();

		for (DDMFormRule ddmFormRule : ddmFormRules) {
			parameters.addAll(
				getParametersByExpression(
					ddmFormRule.getCondition()));

			for (String action : ddmFormRule.getActions()) {
				parameters.addAll(
					getParametersByExpression(action));
			}
		}

		return parameters;
	}

	protected Set<String> getParametersByExpression(
		String expression) {

		if (Validator.isNull(expression)) {
			return Collections.emptySet();
		}

		Set<String> parameters = new HashSet<>();

		Matcher matcher = _functionParametersPattern.matcher(expression);

		while (matcher.find()) {
			parameters.add(matcher.group(1));
		}

		return parameters;
	}

	protected boolean isDDMFormFieldEvaluable(DDMFormField ddmFormField) {
		if (GetterUtil.getBoolean(ddmFormField.getProperty("inputMask")) ||
			GetterUtil.getBoolean(
				ddmFormField.getProperty("requireConfirmation")) ||
			GetterUtil.getBoolean(ddmFormField.getProperty("required"))) {

			return true;
		}

		DDMFormFieldValidation ddmFormFieldValidation =
			ddmFormField.getDDMFormFieldValidation();

		if ((ddmFormFieldValidation != null) &&
			(ddmFormFieldValidation.getDDMFormFieldValidationExpression() !=
				null)) {

			return true;
		}

		return false;
	}

	private static final Pattern _functionParametersPattern = Pattern.compile(
		"'?([\\w]+)'?\\s*[,\\)]\\s*");

}