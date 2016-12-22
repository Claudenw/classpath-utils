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

import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xenei.classpathutils.Case;

/**
 * filters classes by name.
 * 
 */
public class NameClassFilter extends _AbstractStringFilter implements Serializable {

	private static final Log LOG = LogFactory
			.getLog(NameClassFilter.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 2314511406134237664L;

	/**
	 * Constructs a new case-sensitive name class filter for a single name.
	 * 
	 * @param name
	 *            the name to allow, must not be null
	 * @throws IllegalArgumentException
	 *             if the name is null
	 */
	public NameClassFilter(String name) {
		super(name);
	}

	@Override
	protected Log getLog() {
		return LOG;
	}

	/**
	 * Construct a new name class filter specifying case-sensitivity.
	 *
	 * @param caseSensitivity
	 *            how to handle case sensitivity, null means case-sensitive
	 * @param name
	 *            the name to allow, must not be null
	 * @throws IllegalArgumentException
	 *             if the name is null
	 */
	public NameClassFilter(Case caseSensitivity, String name) {
		super(caseSensitivity, name);
	}

	/**
	 * Constructs a new case-sensitive name class filter for an array of names.
	 * <p>
	 * The array is not cloned, so could be changed after constructing the
	 * instance. This would be inadvisable however.
	 * </p>
	 * 
	 * @param names
	 *            the names to allow, must not be null
	 * @throws IllegalArgumentException
	 *             if the names array is null
	 */
	public NameClassFilter(String... names) {
		super(names);
	}

	/**
	 * Constructs a new name class filter for an array of names specifying
	 * case-sensitivity.
	 * <p>
	 * The array is not cloned, so could be changed after constructing the
	 * instance. This would be inadvisable however.
	 * 
	 * @param caseSensitivity
	 *            how to handle case sensitivity, null means case-sensitive
	 * @param names
	 *            the names to allow, must not be null
	 * @throws IllegalArgumentException
	 *             if the names array is null
	 */
	public NameClassFilter(Case caseSensitivity, String... names) {
		super(caseSensitivity, names);
	}

	/**
	 * Constructs a new case-sensitive name class filter for a collection of
	 * names.
	 * 
	 * @param names
	 *            the names to allow, must not be null
	 * @throws IllegalArgumentException
	 *             if the name list is null
	 * @throws ClassCastException
	 *             if the list does not contain Strings
	 */
	public NameClassFilter(Collection<String> names) {
		super(names);
	}

	/**
	 * Constructs a new name class filter for a collection of names specifying
	 * case-sensitivity.
	 * 
	 * @param caseSensitivity
	 *            how to handle case sensitivity, null means case-sensitive
	 * @param names
	 *            the names to allow, must not be null
	 * @throws IllegalArgumentException
	 *             if the name list is null
	 * @throws ClassCastException
	 *             if the list does not contain Strings
	 */
	public NameClassFilter(Case caseSensitivity, Collection<String> names) {
		super(caseSensitivity, names);
	}

	/**
	 * Checks to see if the name matches.
	 * 
	 * @param className
	 *            the class name to check
	 * @return true if the filename matches
	 */
	@Override
	public boolean accept(String className) {
		for (String name2 : getStrings()) {
			if (caseSensitivity.checkEquals(className, name2)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Add a string to the filter.
	 * 
	 * @param str
	 *            the string to add.
	 * @return this for chaining.            
	 */
	public NameClassFilter addClass(Class<?> clazz) {
		if (clazz == null) {
			throw new IllegalArgumentException("The class must not be null");
		}
		super.addString(clazz.getName());
		return this;
	}

	/**
	 * Add a collection of strings to the filter. Strings will be added in the
	 * order the collection iterator returns them.
	 * 
	 * @param strings
	 *            the collection of strings to be added.
	 */
	public final NameClassFilter addClasses(Collection<Class<?>> classes) {
		if (classes == null) {
			throw new IllegalArgumentException("The classes parameter must not be null");
		}
		for (Class<?> c : classes) {
			addClass(c);
		}
		return this;
	}

	/**
	 * Add an array of strings to the filter.
	 * 
	 * @param strings
	 *            The strings to add.
	 * @return this for chaining            
	 */
	public final NameClassFilter addClasses(Class<?>... classes) {
		if (classes == null) {
			throw new IllegalArgumentException("The classes parameter must not be null");
		}
		for (Class<?> c : classes) {
			addClass(c);
		}
		return this;
	}

}
