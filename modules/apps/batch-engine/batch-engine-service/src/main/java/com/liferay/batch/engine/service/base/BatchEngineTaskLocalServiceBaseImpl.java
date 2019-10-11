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

package com.liferay.batch.engine.service.base;

import com.liferay.batch.engine.model.BatchEngineTask;
import com.liferay.batch.engine.model.BatchEngineTaskContentBlobModel;
import com.liferay.batch.engine.service.BatchEngineTaskLocalService;
import com.liferay.batch.engine.service.persistence.BatchEngineTaskPersistence;
import com.liferay.exportimport.kernel.lar.ExportImportHelperUtil;
import com.liferay.exportimport.kernel.lar.ManifestSummary;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DefaultActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.BaseLocalServiceImpl;
import com.liferay.portal.kernel.service.PersistedModelLocalService;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PortalUtil;

import java.io.Serializable;

import java.util.List;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Reference;

/**
 * Provides the base implementation for the batch engine task local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.batch.engine.service.impl.BatchEngineTaskLocalServiceImpl}.
 * </p>
 *
 * @author Shuyang Zhou
 * @see com.liferay.batch.engine.service.impl.BatchEngineTaskLocalServiceImpl
 * @generated
 */
public abstract class BatchEngineTaskLocalServiceBaseImpl
	extends BaseLocalServiceImpl
	implements AopService, BatchEngineTaskLocalService,
			   IdentifiableOSGiService {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>BatchEngineTaskLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>com.liferay.batch.engine.service.BatchEngineTaskLocalServiceUtil</code>.
	 */

	/**
	 * Adds the batch engine task to the database. Also notifies the appropriate model listeners.
	 *
	 * @param batchEngineTask the batch engine task
	 * @return the batch engine task that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public BatchEngineTask addBatchEngineTask(BatchEngineTask batchEngineTask) {
		batchEngineTask.setNew(true);

		return batchEngineTaskPersistence.update(batchEngineTask);
	}

	/**
	 * Creates a new batch engine task with the primary key. Does not add the batch engine task to the database.
	 *
	 * @param batchEngineTaskId the primary key for the new batch engine task
	 * @return the new batch engine task
	 */
	@Override
	@Transactional(enabled = false)
	public BatchEngineTask createBatchEngineTask(long batchEngineTaskId) {
		return batchEngineTaskPersistence.create(batchEngineTaskId);
	}

	/**
	 * Deletes the batch engine task with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param batchEngineTaskId the primary key of the batch engine task
	 * @return the batch engine task that was removed
	 * @throws PortalException if a batch engine task with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public BatchEngineTask deleteBatchEngineTask(long batchEngineTaskId)
		throws PortalException {

		return batchEngineTaskPersistence.remove(batchEngineTaskId);
	}

	/**
	 * Deletes the batch engine task from the database. Also notifies the appropriate model listeners.
	 *
	 * @param batchEngineTask the batch engine task
	 * @return the batch engine task that was removed
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public BatchEngineTask deleteBatchEngineTask(
		BatchEngineTask batchEngineTask) {

		return batchEngineTaskPersistence.remove(batchEngineTask);
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(
			BatchEngineTask.class, clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return batchEngineTaskPersistence.findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.batch.engine.model.impl.BatchEngineTaskModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return batchEngineTaskPersistence.findWithDynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.batch.engine.model.impl.BatchEngineTaskModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<T> orderByComparator) {

		return batchEngineTaskPersistence.findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery) {
		return batchEngineTaskPersistence.countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		DynamicQuery dynamicQuery, Projection projection) {

		return batchEngineTaskPersistence.countWithDynamicQuery(
			dynamicQuery, projection);
	}

	@Override
	public BatchEngineTask fetchBatchEngineTask(long batchEngineTaskId) {
		return batchEngineTaskPersistence.fetchByPrimaryKey(batchEngineTaskId);
	}

	/**
	 * Returns the batch engine task with the matching UUID and company.
	 *
	 * @param uuid the batch engine task's UUID
	 * @param companyId the primary key of the company
	 * @return the matching batch engine task, or <code>null</code> if a matching batch engine task could not be found
	 */
	@Override
	public BatchEngineTask fetchBatchEngineTaskByUuidAndCompanyId(
		String uuid, long companyId) {

		return batchEngineTaskPersistence.fetchByUuid_C_First(
			uuid, companyId, null);
	}

	/**
	 * Returns the batch engine task with the primary key.
	 *
	 * @param batchEngineTaskId the primary key of the batch engine task
	 * @return the batch engine task
	 * @throws PortalException if a batch engine task with the primary key could not be found
	 */
	@Override
	public BatchEngineTask getBatchEngineTask(long batchEngineTaskId)
		throws PortalException {

		return batchEngineTaskPersistence.findByPrimaryKey(batchEngineTaskId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery =
			new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(batchEngineTaskLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(BatchEngineTask.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("batchEngineTaskId");

		return actionableDynamicQuery;
	}

	@Override
	public IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			new IndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setBaseLocalService(
			batchEngineTaskLocalService);
		indexableActionableDynamicQuery.setClassLoader(getClassLoader());
		indexableActionableDynamicQuery.setModelClass(BatchEngineTask.class);

		indexableActionableDynamicQuery.setPrimaryKeyPropertyName(
			"batchEngineTaskId");

		return indexableActionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {

		actionableDynamicQuery.setBaseLocalService(batchEngineTaskLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(BatchEngineTask.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("batchEngineTaskId");
	}

	@Override
	public ExportActionableDynamicQuery getExportActionableDynamicQuery(
		final PortletDataContext portletDataContext) {

		final ExportActionableDynamicQuery exportActionableDynamicQuery =
			new ExportActionableDynamicQuery() {

				@Override
				public long performCount() throws PortalException {
					ManifestSummary manifestSummary =
						portletDataContext.getManifestSummary();

					StagedModelType stagedModelType = getStagedModelType();

					long modelAdditionCount = super.performCount();

					manifestSummary.addModelAdditionCount(
						stagedModelType, modelAdditionCount);

					long modelDeletionCount =
						ExportImportHelperUtil.getModelDeletionCount(
							portletDataContext, stagedModelType);

					manifestSummary.addModelDeletionCount(
						stagedModelType, modelDeletionCount);

					return modelAdditionCount;
				}

			};

		initActionableDynamicQuery(exportActionableDynamicQuery);

		exportActionableDynamicQuery.setAddCriteriaMethod(
			new ActionableDynamicQuery.AddCriteriaMethod() {

				@Override
				public void addCriteria(DynamicQuery dynamicQuery) {
					portletDataContext.addDateRangeCriteria(
						dynamicQuery, "modifiedDate");
				}

			});

		exportActionableDynamicQuery.setCompanyId(
			portletDataContext.getCompanyId());

		exportActionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod<BatchEngineTask>() {

				@Override
				public void performAction(BatchEngineTask batchEngineTask)
					throws PortalException {

					StagedModelDataHandlerUtil.exportStagedModel(
						portletDataContext, batchEngineTask);
				}

			});
		exportActionableDynamicQuery.setStagedModelType(
			new StagedModelType(
				PortalUtil.getClassNameId(BatchEngineTask.class.getName())));

		return exportActionableDynamicQuery;
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {

		return batchEngineTaskLocalService.deleteBatchEngineTask(
			(BatchEngineTask)persistedModel);
	}

	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return batchEngineTaskPersistence.findByPrimaryKey(primaryKeyObj);
	}

	/**
	 * Returns the batch engine task with the matching UUID and company.
	 *
	 * @param uuid the batch engine task's UUID
	 * @param companyId the primary key of the company
	 * @return the matching batch engine task
	 * @throws PortalException if a matching batch engine task could not be found
	 */
	@Override
	public BatchEngineTask getBatchEngineTaskByUuidAndCompanyId(
			String uuid, long companyId)
		throws PortalException {

		return batchEngineTaskPersistence.findByUuid_C_First(
			uuid, companyId, null);
	}

	/**
	 * Returns a range of all the batch engine tasks.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.batch.engine.model.impl.BatchEngineTaskModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of batch engine tasks
	 * @param end the upper bound of the range of batch engine tasks (not inclusive)
	 * @return the range of batch engine tasks
	 */
	@Override
	public List<BatchEngineTask> getBatchEngineTasks(int start, int end) {
		return batchEngineTaskPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of batch engine tasks.
	 *
	 * @return the number of batch engine tasks
	 */
	@Override
	public int getBatchEngineTasksCount() {
		return batchEngineTaskPersistence.countAll();
	}

	/**
	 * Updates the batch engine task in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param batchEngineTask the batch engine task
	 * @return the batch engine task that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public BatchEngineTask updateBatchEngineTask(
		BatchEngineTask batchEngineTask) {

		return batchEngineTaskPersistence.update(batchEngineTask);
	}

	@Override
	public BatchEngineTaskContentBlobModel getContentBlobModel(
		Serializable primaryKey) {

		Session session = null;

		try {
			session = batchEngineTaskPersistence.openSession();

			return (BatchEngineTaskContentBlobModel)session.get(
				BatchEngineTaskContentBlobModel.class, primaryKey);
		}
		catch (Exception e) {
			throw batchEngineTaskPersistence.processException(e);
		}
		finally {
			batchEngineTaskPersistence.closeSession(session);
		}
	}

	@Override
	public Class<?>[] getAopInterfaces() {
		return new Class<?>[] {
			BatchEngineTaskLocalService.class, IdentifiableOSGiService.class,
			PersistedModelLocalService.class
		};
	}

	@Override
	public void setAopProxy(Object aopProxy) {
		batchEngineTaskLocalService = (BatchEngineTaskLocalService)aopProxy;
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return BatchEngineTaskLocalService.class.getName();
	}

	protected Class<?> getModelClass() {
		return BatchEngineTask.class;
	}

	protected String getModelClassName() {
		return BatchEngineTask.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = batchEngineTaskPersistence.getDataSource();

			DB db = DBManagerUtil.getDB();

			sql = db.buildSQL(sql);
			sql = PortalUtil.transformSQL(sql);

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(
				dataSource, sql);

			sqlUpdate.update();
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	protected BatchEngineTaskLocalService batchEngineTaskLocalService;

	@Reference
	protected BatchEngineTaskPersistence batchEngineTaskPersistence;

	@Reference
	protected com.liferay.counter.kernel.service.CounterLocalService
		counterLocalService;

}