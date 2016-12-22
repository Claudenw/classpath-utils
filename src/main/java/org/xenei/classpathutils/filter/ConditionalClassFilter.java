/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xenei.classpathutils.filter;

import java.util.Collection;
import java.util.List;

import org.xenei.classpathutils.ClassPathFilter;

/**
 * Defines operations for conditional resource filters.
 *
 */
public interface ConditionalClassFilter extends ClassPathFilter {

	/**
	 * Adds the specified file filter to the list of file filters at the end of
	 * the list.
	 *
	 * @param resourceFilter
	 *            the filter to be added
	 * @return this for chaining
	 */
	ConditionalClassFilter addFilter(ClassPathFilter resourceFilter);

	/**
	 * Returns this conditional file filter's list of file filters.
	 *
	 * @return the file filter list
	 */
	List<ClassPathFilter> getFilters();

	/**
	 * Removes the specified file filter.
	 *
	 * @param resourceFilter
	 *            filter to be removed
	 * @return {@code true} if the filter was found in the list, {@code false}
	 *         otherwise
	 */
	boolean removeFilter(ClassPathFilter resourceFilter);

	/**
	 * Sets the list of file filters, replacing any previously configured file
	 * filters on this filter.
	 *
	 * @param resourceFilters
	 *            the collection of filters
	 */
	void setFilters(Collection<ClassPathFilter> resourceFilters);

	/**
	 * Sets the list of file filters, replacing any previously configured file
	 * filters on this filter.
	 *
	 * @param resourceFilters
	 *            the array of filters
	 */
	void setFilters(ClassPathFilter... resourceFilters);

	/**
	 * Removes resource filters from this filter.
	 *
	 * @param resourceFilters
	 *            the collection of filters to remove.
	 */
	void removeFilters(Collection<ClassPathFilter> resourceFilters);

	/**
	 * Removes class filters from this filter.
	 *
	 * @param resourceFilters
	 *            the array of filters to remove.
	 */
	void removeFilters(ClassPathFilter... resourceFilters);

	/**
	 * Adds class filters to this filter.
	 *
	 * @param classFilters
	 *            the collection of filters to add.
	 */
	void addFilters(Collection<ClassPathFilter> classFilters);

	/**
	 * Adds class filters to this filter.
	 *
	 * @param resourceFilters
	 *            the array of filters to add.
	 */
	void addFilters(ClassPathFilter... resourceFilters);

}
