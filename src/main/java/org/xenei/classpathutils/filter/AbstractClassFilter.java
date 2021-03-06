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

import java.lang.reflect.Modifier;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xenei.classpathutils.ClassPathFilter;
import org.xenei.classpathutils.filter.types.ClassFilterType;

/**
 * Filters classes that are abstract
 */
public class AbstractClassFilter extends _AbstractBaseFilter implements ClassFilterType {

	private static final Log LOG = LogFactory.getLog(AbstractClassFilter.class);
	
	/** Singleton instance of file filter */
	public static final ClassPathFilter ABSTRACT = new AbstractClassFilter();

	/**
	 * Restrictive constructor.
	 */
	private AbstractClassFilter() {
	}

	@Override
	protected Log getLog() {
		return LOG;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String funcName() {
		return "AbstractClass";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] args() {
		return NO_ARGS;
	}

	/**
	 * Checks to see if the class is abstract.
	 *
	 * @param clazz
	 *            the Class to check
	 * @return true if the class is abstract
	 */
	@Override
	public boolean accept(Class<?> clazz) {
		return Modifier.isAbstract(clazz.getModifiers());
	}

	/**
	 * Checks to see if the class is abstract.
	 *
	 * @param className
	 *            the Class name to check
	 * @return true if the class is abstract
	 */
	@Override
	public boolean accept(String className) {

		try {
			return accept(loadClass(className));
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	/**
	 * Always returns false for a URL.
	 */
	@Override
	public boolean accept(URL url) {
		return false;
	}

	@Override
	public ClassPathFilter optimize() {
		return this;
	}
}
