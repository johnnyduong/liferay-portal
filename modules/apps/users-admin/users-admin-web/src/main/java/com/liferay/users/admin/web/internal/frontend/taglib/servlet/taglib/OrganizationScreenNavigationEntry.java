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

package com.liferay.users.admin.web.internal.frontend.taglib.servlet.taglib;

import com.liferay.frontend.taglib.servlet.taglib.ScreenNavigationEntry;
import com.liferay.frontend.taglib.servlet.taglib.util.JSPRenderer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.OrganizationService;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.users.admin.constants.UserFormConstants;
import com.liferay.users.admin.web.internal.constants.UsersAdminWebKeys;
import com.liferay.users.admin.web.internal.display.context.OrganizationScreenNavigationDisplayContext;

import java.io.IOException;

import java.util.Locale;
import java.util.function.BiFunction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Drew Brokke
 */
public class OrganizationScreenNavigationEntry
	implements ScreenNavigationEntry<Organization> {

	public OrganizationScreenNavigationEntry(
		JSPRenderer jspRenderer, OrganizationService organizationService,
		String entryKey, String categoryKey, String jspPath,
		String mvcActionCommandName,
		BiFunction<User, Organization, Boolean> isVisibleBiFunction) {

		_jspRenderer = jspRenderer;
		_organizationService = organizationService;
		_entryKey = entryKey;
		_categoryKey = categoryKey;
		_jspPath = jspPath;
		_mvcActionCommandName = mvcActionCommandName;
		_isVisibleBiFunction = isVisibleBiFunction;
	}

	@Override
	public String getCategoryKey() {
		return _categoryKey;
	}

	@Override
	public String getEntryKey() {
		return _entryKey;
	}

	@Override
	public String getLabel(Locale locale) {
		return LanguageUtil.get(
			ResourceBundleUtil.getBundle(
				locale, OrganizationScreenNavigationEntry.class),
			_entryKey);
	}

	@Override
	public String getScreenNavigationKey() {
		return UserFormConstants.SCREEN_NAVIGATION_KEY_ORGANIZATIONS;
	}

	@Override
	public boolean isVisible(User user, Organization organization) {
		return _isVisibleBiFunction.apply(user, organization);
	}

	@Override
	public void render(HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		OrganizationScreenNavigationDisplayContext
			organizationScreenNavigationDisplayContext =
				new OrganizationScreenNavigationDisplayContext();

		organizationScreenNavigationDisplayContext.setActionCommandName(
			_mvcActionCommandName);

		String redirect = ParamUtil.getString(request, "redirect");

		String backURL = ParamUtil.getString(request, "backURL", redirect);

		organizationScreenNavigationDisplayContext.setBackURL(backURL);

		organizationScreenNavigationDisplayContext.setFormLabel(
			getLabel(request.getLocale()));
		organizationScreenNavigationDisplayContext.setJspPath(_jspPath);

		long organizationId = ParamUtil.getLong(request, "organizationId");

		Organization organization = null;

		try {
			organization = _organizationService.fetchOrganization(
				organizationId);
		}
		catch (PortalException pe) {
			if (_log.isDebugEnabled()) {
				_log.debug(pe, pe);
			}
		}

		organizationScreenNavigationDisplayContext.setOrganization(
			organization);
		organizationScreenNavigationDisplayContext.setOrganizationId(
			organizationId);
		organizationScreenNavigationDisplayContext.
			setScreenNavigationCategoryKey(_categoryKey);
		organizationScreenNavigationDisplayContext.setScreenNavigationEntryKey(
			_entryKey);

		request.setAttribute(
			UsersAdminWebKeys.ORGANIZATION_SCREEN_NAVIGATION_DISPLAY_CONTEXT,
			organizationScreenNavigationDisplayContext);

		_jspRenderer.renderJSP(
			request, response, "/edit_organization_navigation.jsp");
	}

	private static final Log _log = LogFactoryUtil.getLog(
		OrganizationScreenNavigationEntry.class);

	private final String _categoryKey;
	private final String _entryKey;
	private final BiFunction<User, Organization, Boolean> _isVisibleBiFunction;
	private final String _jspPath;
	private final JSPRenderer _jspRenderer;
	private final String _mvcActionCommandName;
	private final OrganizationService _organizationService;

}