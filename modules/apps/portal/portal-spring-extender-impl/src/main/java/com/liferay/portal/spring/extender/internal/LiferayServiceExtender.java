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

package com.liferay.portal.spring.extender.internal;

import com.liferay.osgi.felix.util.AbstractExtender;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.dao.orm.hibernate.SessionFactoryImpl;
import com.liferay.portal.dao.orm.hibernate.VerifySessionFactoryWrapper;
import com.liferay.portal.kernel.dao.orm.SessionFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.AggregateClassLoader;
import com.liferay.portal.kernel.util.InfrastructureUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.spring.extender.internal.jdbc.DataSourceUtil;
import com.liferay.portal.spring.hibernate.PortletHibernateConfiguration;
import com.liferay.portal.spring.hibernate.PortletTransactionManager;
import com.liferay.portal.spring.transaction.TransactionExecutor;
import com.liferay.portal.spring.transaction.TransactionExecutorFactory;
import com.liferay.portal.spring.transaction.TransactionManagerFactory;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import javax.sql.DataSource;

import org.apache.felix.utils.extender.Extension;

import org.hibernate.engine.SessionFactoryImplementor;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author Preston Crary
 */
@Component(immediate = true, service = {})
public class LiferayServiceExtender extends AbstractExtender {

	@Activate
	protected void activate(BundleContext bundleContext) throws Exception {
		start(bundleContext);
	}

	@Deactivate
	protected void deactivate(BundleContext bundleContext) throws Exception {
		stop(bundleContext);
	}

	@Override
	protected void debug(Bundle bundle, String s) {
		if (_log.isDebugEnabled()) {
			_log.debug(s);
		}
	}

	@Override
	protected Extension doCreateExtension(Bundle bundle) {
		Dictionary<String, String> headers = bundle.getHeaders(
			StringPool.BLANK);

		if ((headers.get("Liferay-Service") == null) ||
			(headers.get("Liferay-Spring-Context") != null)) {

			return null;
		}

		return new LiferayServiceExtension(bundle);
	}

	@Override
	protected void error(String s, Throwable throwable) {
		_log.error(s, throwable);
	}

	@Override
	protected void warn(Bundle bundle, String s, Throwable throwable) {
		if (_log.isWarnEnabled()) {
			_log.warn(s, throwable);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LiferayServiceExtender.class);

	private class LiferayServiceExtension implements Extension {

		@Override
		public void destroy() {
			for (ServiceRegistration<?> serviceRegistration :
					_serviceRegistrations) {

				serviceRegistration.unregister();
			}

			_sessionFactoryImplementor.close();
		}

		@Override
		public void start() throws Exception {
			BundleWiring extendeeBundleWiring = _extendeeBundle.adapt(
				BundleWiring.class);

			ClassLoader extendeeClassLoader =
				extendeeBundleWiring.getClassLoader();

			DataSource dataSource = DataSourceUtil.getDataSource(
				extendeeClassLoader);

			BundleContext extendeeBundleContext =
				_extendeeBundle.getBundleContext();

			_serviceRegistrations.add(
				extendeeBundleContext.registerService(
					DataSource.class, dataSource,
					MapUtil.singletonDictionary(
						"origin.bundle.symbolic.name",
						_extendeeBundle.getSymbolicName())));

			BundleContext extenderBundleContext =
				LiferayServiceExtender.this.getBundleContext();

			Bundle extenderBundle = extenderBundleContext.getBundle();

			BundleWiring extenderBundleWiring = extenderBundle.adapt(
				BundleWiring.class);

			ClassLoader classLoader =
				AggregateClassLoader.getAggregateClassLoader(
					extendeeClassLoader, extenderBundleWiring.getClassLoader());

			PortletHibernateConfiguration portletHibernateConfiguration =
				new PortletHibernateConfiguration(classLoader, dataSource);

			_sessionFactoryImplementor =
				(SessionFactoryImplementor)
					portletHibernateConfiguration.buildSessionFactory();

			SessionFactoryImpl sessionFactoryImpl = new SessionFactoryImpl();

			sessionFactoryImpl.setSessionFactoryClassLoader(classLoader);
			sessionFactoryImpl.setSessionFactoryImplementor(
				_sessionFactoryImplementor);

			SessionFactory sessionFactory =
				VerifySessionFactoryWrapper.createVerifySessionFactoryWrapper(
					sessionFactoryImpl);

			_serviceRegistrations.add(
				extendeeBundleContext.registerService(
					SessionFactory.class, sessionFactory,
					MapUtil.singletonDictionary(
						"origin.bundle.symbolic.name",
						_extendeeBundle.getSymbolicName())));

			TransactionExecutor transactionExecutor = _getTransactionExecutor(
				dataSource, _sessionFactoryImplementor);

			_serviceRegistrations.add(
				extendeeBundleContext.registerService(
					TransactionExecutor.class, transactionExecutor,
					MapUtil.singletonDictionary(
						"origin.bundle.symbolic.name",
						_extendeeBundle.getSymbolicName())));
		}

		private LiferayServiceExtension(Bundle bundle) {
			_extendeeBundle = bundle;
		}

		private TransactionExecutor _getTransactionExecutor(
			DataSource liferayDataSource,
			SessionFactoryImplementor sessionFactoryImplementor) {

			PlatformTransactionManager platformTransactionManager = null;

			if (InfrastructureUtil.getDataSource() == liferayDataSource) {
				platformTransactionManager = new PortletTransactionManager(
					(HibernateTransactionManager)
						InfrastructureUtil.getTransactionManager(),
					sessionFactoryImplementor);
			}
			else {
				platformTransactionManager =
					TransactionManagerFactory.createTransactionManager(
						liferayDataSource, sessionFactoryImplementor);
			}

			return TransactionExecutorFactory.createTransactionExecutor(
				platformTransactionManager, false);
		}

		private final Bundle _extendeeBundle;
		private final List<ServiceRegistration<?>> _serviceRegistrations =
			new ArrayList<>();
		private SessionFactoryImplementor _sessionFactoryImplementor;

	}

}