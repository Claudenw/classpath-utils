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
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.xenei.classpathutils.ClassPathFilter;
import org.xenei.classpathutils.ClassPathUtils;

/**
 * Base class with simple toString implementation.
 *
 */
public abstract class _AbstractBaseFilter implements ClassPathFilter {


	protected static final String[] NO_ARGS = new String[0];

	protected abstract Log getLog();

	/* package static so it can be used in tests. */
	/* package static */static String removeDotClass(String className) {
		return className.endsWith(".class") ? className.substring(0, className.length() - ".class".length())
				: className;
	}

	protected Class<?> loadClass(String className) throws ClassNotFoundException {
		String classNameStr = removeDotClass(className);
		try {
			return Class.forName(classNameStr, false, ClassPathUtils.getClassLoader());
		} catch (ClassNotFoundException e) {
			String err = String.format("Can not load %s: %s", className, e.toString());
			getLog().error(err);
			ClassPathUtils.doLog(err);
			throw e;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return ClassPathFilter.Util.toString(this);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof ClassPathFilter) {
			return ClassPathFilter.Util.equals(this, (ClassPathFilter) o);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return ClassPathFilter.Util.hashCode(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String funcName() {
		String func = getClass().getSimpleName();
		if (func.endsWith("ResourceFilter")) {
			return func.substring(0, func.length() - "ResourceFilter".length());
		}
		if (func.endsWith("ClassFilter")) {
			return func.substring(0, func.length() - "ClassFilter".length());
		}
		return func;
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
}
