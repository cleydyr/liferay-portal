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

package com.liferay.template.info.item.provider;

import com.liferay.info.field.InfoFieldSet;
import com.liferay.info.field.InfoFieldValue;
import com.liferay.template.model.TemplateEntry;

/**
 * @author Lourdes Fernández Besada
 */
public interface TemplateInfoItemFieldSetProvider {

	public default InfoFieldSet getInfoFieldSet(String className) {
		return getInfoFieldSet(className, 0);
	}

	public InfoFieldSet getInfoFieldSet(String className, long classPK);

	public InfoFieldValue<Object> getInfoFieldValue(
		TemplateEntry templateEntry, Object itemObject);

}