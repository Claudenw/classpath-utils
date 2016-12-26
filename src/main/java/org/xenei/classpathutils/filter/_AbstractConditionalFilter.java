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
import java.util.Comparator;
import java.util.List;

import org.xenei.classpathutils.ClassPathFilter;
import org.xenei.classpathutils.filter.types.CollectionFilterType;
import org.xenei.classpathutils.filter.types.LogicFilterType;
import org.xenei.classpathutils.filter.types.StringFilterType;

/**
 * Base implementation for ConditionalClassFilter implementations. Provides
 * implementations for basic add/delete filters and toString.
 *
 */
public abstract class _AbstractConditionalFilter implements CollectionFilterType {
	
	protected static final Comparator<ClassPathFilter> EXECUTION_ORDER = new Comparator<ClassPathFilter>(){

		private ClassPathFilter getFilter( ClassPathFilter f )
		{
			if (f instanceof CollectionFilterType)
			{
				List<ClassPathFilter> l = ((CollectionFilterType)f).getFilters();
				if (l.size()!=1)
				{
					return f;
				}
				return l.get(0);
			}
			return f;
		}
		
		private int getFilterValue( ClassPathFilter f )
		{
			if (f instanceof StringFilterType)
			{
				return 1;
			}
			if (f instanceof LogicFilterType)
			{
				return 0;
			}
			if (f instanceof CollectionFilterType )
				{
				return 10;
				}
			return 100;
		}
		
		@Override
		public int compare(ClassPathFilter arg0, ClassPathFilter arg1) {
			return Integer.compare( getFilterValue(getFilter( arg0 )),
					getFilterValue( getFilter( arg1 )));			
		}};

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
	 * Adds the specified file filter to the list of file filters at the end of
	 * the list.
	 *
	 * @param resourceFilter
	 *            the filter to be added
	 * @return this for chaining
	 */
	public final _AbstractConditionalFilter addFilter(ClassPathFilter classFilter) {
		if (classFilter == null) {
			throw new IllegalArgumentException("classFilter may not be null");
		}
		this.classFilters.add(classFilter);
		return this;
	}

	/**
	 * Returns this conditional file filter's list of file filters.
	 *
	 * @return the file filter list
	 */
	@Override
	public final List<ClassPathFilter> getFilters() {
		return Collections.unmodifiableList(this.classFilters);
	}

	/**
	 * Removes the specified file filter.
	 *
	 * @param resourceFilter
	 *            filter to be removed
	 * @return {@code true} if the filter was found in the list, {@code false}
	 *         otherwise
	 */
	public final boolean removeFilter(ClassPathFilter classFilter) {
		return this.classFilters.remove(classFilter);
	}

	/**
	 * Sets the list of file filters, replacing any previously configured file
	 * filters on this filter.
	 *
	 * @param resourceFilters
	 *            the collection of filters
	 */
	public final void setFilters(Collection<ClassPathFilter> classFilters) {
		this.classFilters.clear();
		addFilters(classFilters);
	}

	/**
	 * Adds class filters to this filter.
	 *
	 * @param classFilters
	 *            the collection of filters to add.
	 */
	public final void addFilters(Collection<ClassPathFilter> classFilters) {
		for (ClassPathFilter filter : classFilters) {
			addFilter(filter);
		}

	}

	/**
	 * Sets the list of file filters, replacing any previously configured file
	 * filters on this filter.
	 *
	 * @param resourceFilters
	 *            the array of filters
	 */
	public final void setFilters(ClassPathFilter... classFilters) {
		this.classFilters.clear();
		addFilters(classFilters);
	}

	/**
	 * Adds class filters to this filter.
	 *
	 * @param resourceFilters
	 *            the array of filters to add.
	 */
	public final void addFilters(ClassPathFilter... classFilters) {
		for (ClassPathFilter filter : classFilters) {
			addFilter(filter);
		}
	}


	/**
	 * Removes resource filters from this filter.
	 *
	 * @param resourceFilters
	 *            the collection of filters to remove.
	 */
	public final void removeFilters(Collection<ClassPathFilter> classFilters) {
		this.classFilters.removeAll(classFilters);
	}

	/**
	 * Removes class filters from this filter.
	 *
	 * @param resourceFilters
	 *            the array of filters to remove.
	 */
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
	
	@Override
	public boolean equals( Object o )
	{
		if (o instanceof ClassPathFilter)
		{
			return ClassPathFilter.Util.equals(this, (ClassPathFilter)o);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return ClassPathFilter.Util.hashCode(this);
	}


}
