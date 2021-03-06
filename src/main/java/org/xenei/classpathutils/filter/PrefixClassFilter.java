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
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xenei.classpathutils.Case;
import org.xenei.classpathutils.ClassPathFilter;

/**
 * A filter that matches classes by prefix.
 */
public class PrefixClassFilter extends _AbstractStringFilter implements Serializable {

	private static final Log LOG = LogFactory.getLog(PrefixClassFilter.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -5764899732969345726L;

	/**
	 * Constructs a new Prefix class filter for a single prefix.
	 * 
	 * @param prefix
	 *            the prefix to allow, must not be null
	 * @throws IllegalArgumentException
	 *             if the prefix is null
	 */
	public PrefixClassFilter(String prefix) {
		super(prefix);
	}

	@Override
	protected Log getLog() {
		return LOG;
	}

	/**
	 * Constructs a new Prefix class filter for a single prefix specifying
	 * case-sensitivity.
	 * 
	 * @param prefix
	 *            the prefix to allow, must not be null
	 * @param caseSensitivity
	 *            how to handle case sensitivity, null means case-sensitive
	 */
	public PrefixClassFilter(Case caseSensitivity, String prefix) {
		super(caseSensitivity, prefix);
	}

	/**
	 * Constructs a new Prefix class filter for any of an array of prefixes.
	 * <p>
	 * The array is not cloned, so could be changed after constructing the
	 * instance. This would be inadvisable however.
	 * 
	 * @param prefixes
	 *            the prefixes to allow, must not be null
	 * @throws IllegalArgumentException
	 *             if the prefix array is null
	 */
	public PrefixClassFilter(String... prefixes) {
		super(prefixes);
	}

	/**
	 * Constructs a new Prefix class filter for any of an array of prefixes
	 * specifying case-sensitivity.
	 * <p>
	 * The array is cloned.
	 * 
	 * @param prefixes
	 *            the prefixes to allow, must not be null
	 * @param caseSensitivity
	 *            how to handle case sensitivity, null means case-sensitive
	 */
	public PrefixClassFilter(Case caseSensitivity, String... prefixes) {
		super(caseSensitivity, prefixes);
	}

	/**
	 * Constructs a new Prefix class filter for a list of prefixes.
	 * 
	 * @param prefixes
	 *            the prefixes to allow, must not be null
	 * @throws IllegalArgumentException
	 *             if the prefix list is null
	 * @throws ClassCastException
	 *             if the list does not contain Strings
	 */
	public PrefixClassFilter(List<String> prefixes) {
		super(prefixes);
	}

	/**
	 * Constructs a new Prefix class filter for a collection of prefixes
	 * specifying case-sensitivity.
	 * 
	 * @param prefixes
	 *            the prefixes to allow, must not be null
	 * @param caseSensitivity
	 *            how to handle case sensitivity, null means case-sensitive
	 * @throws IllegalArgumentException
	 *             if the prefix list is null
	 * @throws ClassCastException
	 *             if the list does not contain Strings
	 */
	public PrefixClassFilter(Case caseSensitivity, Collection<String> prefixes) {
		super(caseSensitivity, prefixes);
	}

	/**
	 * Checks to see if the filename starts with the prefix.
	 * 
	 * @param className
	 *            the class name to check.
	 * @return true if the filename starts with one of our prefixes
	 */
	@Override
	public boolean accept(String className) {
		for (String prefix : getStrings()) {
			if (caseSensitivity.checkStartsWith(className, prefix)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ClassPathFilter optimize() {
		// remove duplicates
		Set<String> set = new LinkedHashSet<String>(getStrings());
		if (set.size() == 0) {
			return FalseClassFilter.FALSE;
		}

		Iterator<String> iter = set.iterator();
		while (iter.hasNext()) {
			if (iter.next().length() == 0) {
				return TrueClassFilter.TRUE;
			}
		}
		if (set.size() < getStrings().size()) {
			return new PrefixClassFilter(caseSensitivity, set);
		}
		return this;

	}
}
