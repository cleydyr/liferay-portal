/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.dynamicdatamapping.model;

/**
 * <p>
 * This class is a wrapper for {@link DDMTemplate}.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       DDMTemplate
 * @generated
 */
public class DDMTemplateWrapper implements DDMTemplate {
	public DDMTemplateWrapper(DDMTemplate ddmTemplate) {
		_ddmTemplate = ddmTemplate;
	}

	public Class<?> getModelClass() {
		return DDMTemplate.class;
	}

	public String getModelClassName() {
		return DDMTemplate.class.getName();
	}

	/**
	* Gets the primary key of this d d m template.
	*
	* @return the primary key of this d d m template
	*/
	public long getPrimaryKey() {
		return _ddmTemplate.getPrimaryKey();
	}

	/**
	* Sets the primary key of this d d m template
	*
	* @param pk the primary key of this d d m template
	*/
	public void setPrimaryKey(long pk) {
		_ddmTemplate.setPrimaryKey(pk);
	}

	/**
	* Gets the uuid of this d d m template.
	*
	* @return the uuid of this d d m template
	*/
	public java.lang.String getUuid() {
		return _ddmTemplate.getUuid();
	}

	/**
	* Sets the uuid of this d d m template.
	*
	* @param uuid the uuid of this d d m template
	*/
	public void setUuid(java.lang.String uuid) {
		_ddmTemplate.setUuid(uuid);
	}

	/**
	* Gets the template ID of this d d m template.
	*
	* @return the template ID of this d d m template
	*/
	public long getTemplateId() {
		return _ddmTemplate.getTemplateId();
	}

	/**
	* Sets the template ID of this d d m template.
	*
	* @param templateId the template ID of this d d m template
	*/
	public void setTemplateId(long templateId) {
		_ddmTemplate.setTemplateId(templateId);
	}

	/**
	* Gets the group ID of this d d m template.
	*
	* @return the group ID of this d d m template
	*/
	public long getGroupId() {
		return _ddmTemplate.getGroupId();
	}

	/**
	* Sets the group ID of this d d m template.
	*
	* @param groupId the group ID of this d d m template
	*/
	public void setGroupId(long groupId) {
		_ddmTemplate.setGroupId(groupId);
	}

	/**
	* Gets the company ID of this d d m template.
	*
	* @return the company ID of this d d m template
	*/
	public long getCompanyId() {
		return _ddmTemplate.getCompanyId();
	}

	/**
	* Sets the company ID of this d d m template.
	*
	* @param companyId the company ID of this d d m template
	*/
	public void setCompanyId(long companyId) {
		_ddmTemplate.setCompanyId(companyId);
	}

	/**
	* Gets the user ID of this d d m template.
	*
	* @return the user ID of this d d m template
	*/
	public long getUserId() {
		return _ddmTemplate.getUserId();
	}

	/**
	* Sets the user ID of this d d m template.
	*
	* @param userId the user ID of this d d m template
	*/
	public void setUserId(long userId) {
		_ddmTemplate.setUserId(userId);
	}

	/**
	* Gets the user uuid of this d d m template.
	*
	* @return the user uuid of this d d m template
	* @throws SystemException if a system exception occurred
	*/
	public java.lang.String getUserUuid()
		throws com.liferay.portal.kernel.exception.SystemException {
		return _ddmTemplate.getUserUuid();
	}

	/**
	* Sets the user uuid of this d d m template.
	*
	* @param userUuid the user uuid of this d d m template
	*/
	public void setUserUuid(java.lang.String userUuid) {
		_ddmTemplate.setUserUuid(userUuid);
	}

	/**
	* Gets the user name of this d d m template.
	*
	* @return the user name of this d d m template
	*/
	public java.lang.String getUserName() {
		return _ddmTemplate.getUserName();
	}

	/**
	* Sets the user name of this d d m template.
	*
	* @param userName the user name of this d d m template
	*/
	public void setUserName(java.lang.String userName) {
		_ddmTemplate.setUserName(userName);
	}

	/**
	* Gets the create date of this d d m template.
	*
	* @return the create date of this d d m template
	*/
	public java.util.Date getCreateDate() {
		return _ddmTemplate.getCreateDate();
	}

	/**
	* Sets the create date of this d d m template.
	*
	* @param createDate the create date of this d d m template
	*/
	public void setCreateDate(java.util.Date createDate) {
		_ddmTemplate.setCreateDate(createDate);
	}

	/**
	* Gets the modified date of this d d m template.
	*
	* @return the modified date of this d d m template
	*/
	public java.util.Date getModifiedDate() {
		return _ddmTemplate.getModifiedDate();
	}

	/**
	* Sets the modified date of this d d m template.
	*
	* @param modifiedDate the modified date of this d d m template
	*/
	public void setModifiedDate(java.util.Date modifiedDate) {
		_ddmTemplate.setModifiedDate(modifiedDate);
	}

	/**
	* Gets the structure ID of this d d m template.
	*
	* @return the structure ID of this d d m template
	*/
	public long getStructureId() {
		return _ddmTemplate.getStructureId();
	}

	/**
	* Sets the structure ID of this d d m template.
	*
	* @param structureId the structure ID of this d d m template
	*/
	public void setStructureId(long structureId) {
		_ddmTemplate.setStructureId(structureId);
	}

	/**
	* Gets the name of this d d m template.
	*
	* @return the name of this d d m template
	*/
	public java.lang.String getName() {
		return _ddmTemplate.getName();
	}

	/**
	* Sets the name of this d d m template.
	*
	* @param name the name of this d d m template
	*/
	public void setName(java.lang.String name) {
		_ddmTemplate.setName(name);
	}

	/**
	* Gets the description of this d d m template.
	*
	* @return the description of this d d m template
	*/
	public java.lang.String getDescription() {
		return _ddmTemplate.getDescription();
	}

	/**
	* Sets the description of this d d m template.
	*
	* @param description the description of this d d m template
	*/
	public void setDescription(java.lang.String description) {
		_ddmTemplate.setDescription(description);
	}

	/**
	* Gets the type of this d d m template.
	*
	* @return the type of this d d m template
	*/
	public java.lang.String getType() {
		return _ddmTemplate.getType();
	}

	/**
	* Sets the type of this d d m template.
	*
	* @param type the type of this d d m template
	*/
	public void setType(java.lang.String type) {
		_ddmTemplate.setType(type);
	}

	/**
	* Gets the language of this d d m template.
	*
	* @return the language of this d d m template
	*/
	public java.lang.String getLanguage() {
		return _ddmTemplate.getLanguage();
	}

	/**
	* Sets the language of this d d m template.
	*
	* @param language the language of this d d m template
	*/
	public void setLanguage(java.lang.String language) {
		_ddmTemplate.setLanguage(language);
	}

	/**
	* Gets the script of this d d m template.
	*
	* @return the script of this d d m template
	*/
	public java.lang.String getScript() {
		return _ddmTemplate.getScript();
	}

	/**
	* Sets the script of this d d m template.
	*
	* @param script the script of this d d m template
	*/
	public void setScript(java.lang.String script) {
		_ddmTemplate.setScript(script);
	}

	public boolean isNew() {
		return _ddmTemplate.isNew();
	}

	public void setNew(boolean n) {
		_ddmTemplate.setNew(n);
	}

	public boolean isCachedModel() {
		return _ddmTemplate.isCachedModel();
	}

	public void setCachedModel(boolean cachedModel) {
		_ddmTemplate.setCachedModel(cachedModel);
	}

	public boolean isEscapedModel() {
		return _ddmTemplate.isEscapedModel();
	}

	public void setEscapedModel(boolean escapedModel) {
		_ddmTemplate.setEscapedModel(escapedModel);
	}

	public java.io.Serializable getPrimaryKeyObj() {
		return _ddmTemplate.getPrimaryKeyObj();
	}

	public com.liferay.portlet.expando.model.ExpandoBridge getExpandoBridge() {
		return _ddmTemplate.getExpandoBridge();
	}

	public void setExpandoBridgeAttributes(
		com.liferay.portal.service.ServiceContext serviceContext) {
		_ddmTemplate.setExpandoBridgeAttributes(serviceContext);
	}

	public java.lang.Object clone() {
		return new DDMTemplateWrapper((DDMTemplate)_ddmTemplate.clone());
	}

	public int compareTo(
		com.liferay.portlet.dynamicdatamapping.model.DDMTemplate ddmTemplate) {
		return _ddmTemplate.compareTo(ddmTemplate);
	}

	public int hashCode() {
		return _ddmTemplate.hashCode();
	}

	public com.liferay.portlet.dynamicdatamapping.model.DDMTemplate toEscapedModel() {
		return new DDMTemplateWrapper(_ddmTemplate.toEscapedModel());
	}

	public java.lang.String toString() {
		return _ddmTemplate.toString();
	}

	public java.lang.String toXmlString() {
		return _ddmTemplate.toXmlString();
	}

	public com.liferay.portlet.dynamicdatamapping.model.DDMStructure getStructure()
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _ddmTemplate.getStructure();
	}

	public DDMTemplate getWrappedDDMTemplate() {
		return _ddmTemplate;
	}

	public void resetOriginalValues() {
		_ddmTemplate.resetOriginalValues();
	}

	private DDMTemplate _ddmTemplate;
}