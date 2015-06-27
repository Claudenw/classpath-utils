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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xenei.classpathutils.ClassPathFilter;

/**
 * A class filter that always returns true.
 *
 */
public class TrueFilter extends _AbstractBaseFilter implements Serializable {

	private static final Logger LOG = LoggerFactory.getLogger(TrueFilter.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 6422553815074269475L;
	/**
	 * Singleton instance of true filter.
	 */
	public static final ClassPathFilter TRUE = new TrueFilter();

	/**
	 * Restrictive constructor.
	 */
	private TrueFilter() {
	}

	@Override
	protected Logger getLog() {
		return LOG;
	}

	/**
	 * Returns true.
	 *
	 * @param url
	 *            the url to check (ignored)
	 * @return true
	 */
	@Override
	public boolean accept(URL url) {
		return true;
	}

	/**
	 * Returns true.
	 *
	 * @param clazz
	 *            the class to check (ignored)
	 * @return true
	 */
	@Override
	public boolean accept(Class<?> clazz) {
		return true;
	}

	/**
	 * Returns true.
	 *
	 * 
	 * @param string
	 *            the string name (ignored)
	 * @return true
	 */
	@Override
	public boolean accept(String string) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] args() {
		return NO_ARGS;
	}

}
