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
import java.net.URL;
import java.util.Collection;
import java.util.regex.Pattern;

import org.xenei.classpathutils.Case;
import org.xenei.classpathutils.ClassPathFilter;
import org.xenei.classpathutils.filter.types.StringFilterType;

/**
 * Match classes with a regular expression.
 */
public class RegexClassFilter implements ClassPathFilter, Serializable, StringFilterType {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3282334808113162667L;
	/** The regular expression pattern that will be used to match filenames */
	private final Pattern pattern;

	/**
	 * Construct a new regular expression filter.
	 *
	 * @param pattern
	 *            regular string expression to match
	 * @throws IllegalArgumentException
	 *             if the pattern is null
	 */
	public RegexClassFilter(String pattern) {
		this(Case.SENSITIVE, pattern);
	}

	/**
	 * Construct a new regular expression filter with the specified flags case
	 * sensitivity.
	 *
	 * @param pattern
	 *            regular string expression to match
	 * @param caseSensitivity
	 *            how to handle case sensitivity, null means case-sensitive
	 * @throws IllegalArgumentException
	 *             if the pattern is null
	 */
	public RegexClassFilter(Case caseSensitivity, String pattern) {
		if (pattern == null) {
			throw new IllegalArgumentException("Pattern is missing");
		}
		int flags = 0;
		if (caseSensitivity != null && !caseSensitivity.isCaseSensitive()) {
			flags = Pattern.CASE_INSENSITIVE;
		}
		this.pattern = Pattern.compile(pattern, flags);
	}

	/**
	 * Construct a new regular expression filter with the specified flags.
	 *
	 * @param pattern
	 *            regular string expression to match
	 * @param flags
	 *            pattern flags - e.g. {@link Pattern#CASE_INSENSITIVE}
	 * @throws IllegalArgumentException
	 *             if the pattern is null
	 */
	public RegexClassFilter(String pattern, int flags) {
		if (pattern == null) {
			throw new IllegalArgumentException("Pattern is missing");
		}
		this.pattern = Pattern.compile(pattern, flags);
	}

	/**
	 * Construct a new regular expression filter for a compiled regular
	 * expression
	 *
	 * @param pattern
	 *            regular expression to match
	 * @throws IllegalArgumentException
	 *             if the pattern is null
	 */
	public RegexClassFilter(Pattern pattern) {
		if (pattern == null) {
			throw new IllegalArgumentException("Pattern is missing");
		}

		this.pattern = pattern;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String funcName() {
		return "Regex";
	}

	/**
	 * Checks to see if the class name matches the regular expression.
	 *
	 * @param className
	 *            the class name to match
	 * @return true if the filename matches one of the regular expressions
	 */
	@Override
	public boolean accept(String className) {
		return pattern.matcher(className).matches();
	}

	/**
	 * Checks to see if the URL name matches the regular expression.
	 *
	 * @param url
	 *            the URL to match
	 * @return true if the filename matches one of the regular expressions
	 */
	@Override
	public boolean accept(URL url) {
		return accept(url.toExternalForm());
	}

	/**
	 * Converts class to name and calls accept( className )
	 * 
	 * @return true if the class name passes the filter.
	 */
	@Override
	public boolean accept(Class<?> clazz) {
		return accept(clazz.getName());
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
	public String[] args() {
		Case c = (pattern.flags() & Pattern.CASE_INSENSITIVE) != 0 ? Case.INSENSITIVE : Case.SENSITIVE;
		return new String[] { c.getName(), pattern.pattern() };
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

	@Override
	public ClassPathFilter optimize() {
		return this;
	}

}
