/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.xenei.classpathutils.filter;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.xenei.classpathutils.ClassPathFilter;

/**
 * Base implementation for ConditionalClassFilter implementations. Provides
 * implementations for basic add/delete filters and toString.
 *
 */
public abstract class _AbstractConditionalFilter implements ConditionalClassFilter {

	/** The list of file filters. */
	private final List<ClassPathFilter> classFilters = new ArrayList<ClassPathFilter>();

	/**
	 * Create the conditionals from list of filters.
	 * 
	 * @param classFilters
	 *            The filters to create the conditional from.
	 */
	protected _AbstractConditionalFilter(
			final Collection<ClassPathFilter> classFilters) {
		if (classFilters == null || classFilters.size() < 2) {
			throw new IllegalArgumentException(
					"Collection of filters may not be null or contain less than 2 filters");
		}
		addFilters(classFilters);
	}

	/**
	 * Create the conditionals from an array of filters.
	 * 
	 * @param classFilters
	 *            The filters to create the conditional from.
	 */
	protected _AbstractConditionalFilter(final ClassPathFilter... classFilters) {
		if (classFilters.length < 2) {
			throw new IllegalArgumentException(
					"Array of filters may not contain less than 2 filters");
		}
		addFilters(classFilters);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] args() {
		String[] retval = new String[classFilters.size()];
		for (int i = 0; i < classFilters.size(); i++) {
			retval[i] = classFilters.get(i).toString();
		}
		return retval;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<URL> filterURLs(Collection<URL> collection) {
		return ClassPathFilter.Util.filterURLs(collection, this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<String> filterNames(Collection<String> collection) {
		return ClassPathFilter.Util.filterNames(collection, this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Class<?>> filterClasses(Collection<Class<?>> collection) {
		return ClassPathFilter.Util.filterClasses(collection, this);
	}

	protected boolean isFilterListEmpty() {
		return classFilters.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void addFilter(ClassPathFilter classFilter) {
		if (classFilter == null) {
			throw new IllegalArgumentException("classFilter may not be null");
		}
		this.classFilters.add(classFilter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final List<ClassPathFilter> getFilters() {
		return Collections.unmodifiableList(this.classFilters);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean removeFilter(ClassPathFilter classFilter) {
		return this.classFilters.remove(classFilter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setFilters(Collection<ClassPathFilter> classFilters) {
		this.classFilters.clear();
		addFilters(classFilters);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void addFilters(Collection<ClassPathFilter> classFilters) {
		for (ClassPathFilter filter : classFilters) {
			addFilter(filter);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setFilters(ClassPathFilter... classFilters) {
		this.classFilters.clear();
		addFilters(classFilters);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void addFilters(ClassPathFilter... classFilters) {
		for (ClassPathFilter filter : classFilters) {
			addFilter(filter);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void removeFilters(Collection<ClassPathFilter> classFilters) {
		this.classFilters.removeAll(classFilters);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void removeFilters(ClassPathFilter... classFilters) {
		this.classFilters.removeAll(Arrays.asList(classFilters));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return ClassPathFilter.Util.toString(this);
	}
}
