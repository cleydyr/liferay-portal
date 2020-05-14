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

package com.liferay.layout.page.template.service.impl;

import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.model.LayoutPageTemplateStructure;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalService;
import com.liferay.layout.page.template.service.base.LayoutPageTemplateStructureServiceBaseImpl;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.BaseModelPermissionCheckerUtil;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.util.Portal;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pavel Savinov
 */
@Component(
	property = {
		"json.web.service.context.name=layout",
		"json.web.service.context.path=LayoutPageTemplateStructure"
	},
	service = AopService.class
)
public class LayoutPageTemplateStructureServiceImpl
	extends LayoutPageTemplateStructureServiceBaseImpl {

	@Override
	public LayoutPageTemplateStructure updateLayoutPageTemplateStructure(
			long groupId, long classNameId, long classPK,
			long segmentsExperienceId, String data)
		throws PortalException {

		Layout layout = _layoutLocalService.getLayout(classPK);

		layout = _getPublishedLayout(layout);

		String className = _portal.getClassName(classNameId);
		long classPK1 = layout.getPlid();

		if (layout.isTypeAssetDisplay()) {
			LayoutPageTemplateEntry layoutPageTemplateEntry =
				_getLayoutPageTemplateEntry(layout);

			className = LayoutPageTemplateEntry.class.getName();
			classPK1 = layoutPageTemplateEntry.getLayoutPageTemplateEntryId();
		}

		Boolean containsPermission =
			BaseModelPermissionCheckerUtil.containsBaseModelPermission(
				getPermissionChecker(), groupId, className, classPK1,
				ActionKeys.UPDATE);

		if (!containsPermission) {
			throw new PrincipalException.MustHavePermission(
				getUserId(), className, classPK1, ActionKeys.UPDATE);
		}

		return layoutPageTemplateStructureLocalService.
			updateLayoutPageTemplateStructure(
				groupId, classNameId, classPK, segmentsExperienceId, data);
	}

	private LayoutPageTemplateEntry _getLayoutPageTemplateEntry(Layout layout) {
		return _layoutPageTemplateEntryLocalService.
			fetchLayoutPageTemplateEntryByPlid(layout.getPlid());
	}

	private Layout _getPublishedLayout(Layout layout) {
		long classPK = layout.getClassPK();

		if (classPK != 0) {
			return _layoutLocalService.fetchLayout(classPK);
		}

		return layout;
	}

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private LayoutPageTemplateEntryLocalService
		_layoutPageTemplateEntryLocalService;

	@Reference
	private Portal _portal;

}