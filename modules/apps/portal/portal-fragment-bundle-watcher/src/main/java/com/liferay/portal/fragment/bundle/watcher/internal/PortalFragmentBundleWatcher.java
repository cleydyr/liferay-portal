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

package com.liferay.portal.fragment.bundle.watcher.internal;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.Constants;
import org.osgi.framework.wiring.BundleRevision;
import org.osgi.framework.wiring.FrameworkWiring;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.tracker.BundleTracker;

/**
 * @author Shuyang Zhou
 */
@Component(service = {})
public class PortalFragmentBundleWatcher {

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		_installedFragmentBundleTracker = new BundleTracker<String>(
			_bundleContext, Bundle.INSTALLED, null) {

			@Override
			public String addingBundle(Bundle bundle, BundleEvent event) {
				if (!_isFragment(bundle)) {
					return null;
				}

				return _getFragmentHost(bundle);
			}

		};

		_installedFragmentBundleTracker.open();

		Bundle systemBundle = _bundleContext.getBundle(0);

		FrameworkWiring frameworkWiring = systemBundle.adapt(
			FrameworkWiring.class);

		_resolvedBundleListener = bundleEvent -> {
			Bundle bundleEventBundle = bundleEvent.getBundle();

			if (((bundleEvent.getType() == BundleEvent.INSTALLED) &&
				 (bundleEventBundle.getState() != Bundle.UNINSTALLED) &&
				 _isFragment(bundleEventBundle)) ||
				((bundleEvent.getType() == BundleEvent.RESOLVED) &&
				 !_isFragment(bundleEventBundle))) {

				Map<Bundle, String> installedFragmentBundles =
					_installedFragmentBundleTracker.getTracked();

				if (installedFragmentBundles.isEmpty()) {
					return;
				}

				Map<String, List<Bundle>> fragmentBundlesMap = new HashMap<>();

				for (Map.Entry<Bundle, String> entry :
						installedFragmentBundles.entrySet()) {

					List<Bundle> fragmentBundles =
						fragmentBundlesMap.computeIfAbsent(
							entry.getValue(), key -> new ArrayList<>());

					fragmentBundles.add(entry.getKey());
				}

				Bundle originBundle = bundleEvent.getOrigin();

				long originBundleId = originBundle.getBundleId();

				List<Bundle> hostBundles = new ArrayList<>();

				for (Bundle bundle : bundleContext.getBundles()) {
					List<Bundle> fragmantBundles = fragmentBundlesMap.remove(
						bundle.getSymbolicName());

					if (fragmantBundles == null) {
						continue;
					}

					if (originBundleId != bundle.getBundleId()) {
						boolean needRefresh = false;

						for (Bundle fragmentBundle : fragmantBundles) {
							if (fragmentBundle.getState() == Bundle.INSTALLED) {
								needRefresh = true;

								break;
							}
						}

						if (needRefresh) {
							hostBundles.add(bundle);
						}
					}

					if (fragmentBundlesMap.isEmpty()) {
						break;
					}
				}

				if (!hostBundles.isEmpty()) {
					frameworkWiring.refreshBundles(hostBundles);
				}
			}
		};

		_bundleContext.addBundleListener(_resolvedBundleListener);
	}

	@Deactivate
	protected void deactivate() {
		_bundleContext.removeBundleListener(_resolvedBundleListener);

		_installedFragmentBundleTracker.close();
	}

	private String _getFragmentHost(Bundle bundle) {
		Dictionary<String, String> dictionary = bundle.getHeaders(
			StringPool.BLANK);

		String fragmentHost = dictionary.get(Constants.FRAGMENT_HOST);

		if (fragmentHost == null) {
			return null;
		}

		int index = fragmentHost.indexOf(CharPool.SEMICOLON);

		if (index != -1) {
			fragmentHost = fragmentHost.substring(0, index);
		}

		return fragmentHost;
	}

	/**
	 * @see com.liferay.portal.file.install.internal.DirectoryWatcher#_isFragment
	 */
	private boolean _isFragment(Bundle bundle) {
		BundleRevision bundleRevision = bundle.adapt(BundleRevision.class);

		if ((bundleRevision.getTypes() & BundleRevision.TYPE_FRAGMENT) != 0) {
			return true;
		}

		return false;
	}

	private BundleContext _bundleContext;
	private BundleTracker<String> _installedFragmentBundleTracker;

	@Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED)
	private ModuleServiceLifecycle _moduleServiceLifecycle;

	private BundleListener _resolvedBundleListener;

}