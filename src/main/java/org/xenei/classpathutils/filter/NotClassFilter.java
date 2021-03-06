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

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xenei.classpathutils.ClassPathFilter;
import org.xenei.classpathutils.filter.types.CollectionFilterType;

/**
 * This filter produces a logical NOT of the specified filter
 *
 */
public class NotClassFilter extends _AbstractBaseFilter implements CollectionFilterType {

	private static final Log LOG = LogFactory.getLog(NotClassFilter.class);

	/** The filter */
	private final ClassPathFilter filter;

	/**
	 * Constructs a new file filter that NOTs the result of another filter.
	 * 
	 * @param filter
	 *            the enclosed filter, must not be null
	 * @throws IllegalArgumentException
	 *             if the filter is null
	 */
	public NotClassFilter(ClassPathFilter filter) {
		if (filter == null) {
			throw new IllegalArgumentException("The filter must not be null");
		}
		this.filter = filter;
	}

	@Override
	protected Log getLog() {
		return LOG;
	}

	/**
	 * Returns the logical NOT of the underlying filter's return value for the
	 * same URL.
	 * 
	 * @param url
	 *            the URL to check
	 * @return true if the enclosed filter returns false
	 */
	@Override
	public boolean accept(URL url) {
		return !filter.accept(url);
	}

	/**
	 * Returns the logical NOT of the underlying filter's return value for the
	 * same Clazz.
	 * 
	 * @param clazz
	 *            the Class to check
	 * @return true if the enclosed filter returns false
	 */
	@Override
	public boolean accept(Class<?> clazz) {
		return !filter.accept(clazz);
	}

	/**
	 * Returns the logical NOT of the underlying filter's return value for the
	 * same string.
	 * 
	 * @param className
	 *            the class name to check.
	 * @return true if the enclosed filter returns false
	 */
	@Override
	public boolean accept(String className) {
		return !filter.accept(className);
	}

	/**
	 * Provide a String representation of this c;ass filter.
	 *
	 * @return a String representation
	 */
	@Override
	public String toString() {
		return ClassPathFilter.Util.toString(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] args() {
		return new String[] { filter.toString() };
	}

	@Override
	public ClassPathFilter optimize() {
		ClassPathFilter f = filter.optimize();
		if (f == FalseClassFilter.FALSE) {
			return TrueClassFilter.TRUE;
		}
		if (f == TrueClassFilter.TRUE) {
			return FalseClassFilter.FALSE;
		}
		if (f == filter) {
			return this;
		}
		return new NotClassFilter(f);

	}

	@Override
	public List<ClassPathFilter> getFilters() {
		return Arrays.asList(filter);
	}

}
