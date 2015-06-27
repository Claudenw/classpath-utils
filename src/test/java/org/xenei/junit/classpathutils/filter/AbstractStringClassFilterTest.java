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

package org.xenei.junit.classpathutils.filter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xenei.classpathutils.Case;
import org.xenei.classpathutils.ClassPathFilter;
import org.xenei.classpathutils.filter._AbstractStringFilter;

/**
 * Class to test AbstractStringClassFilter.
 *
 */
public class AbstractStringClassFilterTest {

	private _AbstractStringFilter filter;

	private _AbstractStringFilter filter_sens;

	private _AbstractStringFilter filter_insens;

	private static Logger LOG = LoggerFactory
			.getLogger(AbstractStringClassFilterTest.class);

	/**
	 * Recreate the filters.
	 */
	@Before
	public void setupAbstractStringClassFilterTest() {
		filter = new _AbstractStringFilter("foo") {

			@Override
			public boolean accept(String className) {
				return true;
			}

			@Override
			protected Logger getLog() {
				return LOG;
			}
		};
		filter_sens = new _AbstractStringFilter(Case.SENSITIVE, "foo") {

			@Override
			public boolean accept(String className) {
				return true;
			}

			@Override
			protected Logger getLog() {
				return LOG;
			}
		};
		filter_insens = new _AbstractStringFilter(Case.INSENSITIVE, "foo") {

			@Override
			public boolean accept(String className) {
				return true;
			}

			@Override
			protected Logger getLog() {
				return LOG;
			}
		};
	}

	/**
	 * Test that toString() works.
	 */
	@Test
	public void testToString() {
		assertEquals("( Sensitive, foo )", filter.toString());
		assertEquals("( Sensitive, foo )", filter_sens.toString());
		assertEquals("( Insensitive, foo )", filter_insens.toString());

		filter.addString(ClassPathFilter.class.getName());
		filter_sens.addString(ClassPathFilter.class.getName());
		filter_insens.addString(ClassPathFilter.class.getName());
		assertEquals("( Sensitive, foo, " + ClassPathFilter.class.getName()
				+ " )", filter.toString());
		assertEquals("( Sensitive, foo, " + ClassPathFilter.class.getName()
				+ " )", filter_sens.toString());
		assertEquals("( Insensitive, foo, " + ClassPathFilter.class.getName()
				+ " )", filter_insens.toString());

	}

	/**
	 * Test that addStrings( String... ) works.
	 */
	@Test
	public void testAddStrings() {
		filter.addStrings("fu", "bar", "baz");
		assertEquals(4, filter.getStrings().size());
		assertTrue("missing foo (from constructor)", filter.getStrings()
				.contains("foo"));
		assertTrue("missing fu", filter.getStrings().contains("fu"));
		assertTrue("missing bar", filter.getStrings().contains("bar"));
		assertTrue("missing baz", filter.getStrings().contains("baz"));
	}

	/**
	 * Test that addStrings( List&lt;String&gt; ) works.
	 */
	@Test
	public void testAddStrings_list() {
		List<String> lst = new ArrayList<String>();
		lst.add("fu");
		lst.add("bar");
		lst.add("baz");
		filter_sens.addStrings(lst);
		assertEquals(4, filter_sens.getStrings().size());
		assertTrue("missing foo (from constructor)", filter_sens.getStrings()
				.contains("foo"));
		assertTrue("missing fu", filter_sens.getStrings().contains("fu"));
		assertTrue("missing bar", filter_sens.getStrings().contains("bar"));
		assertTrue("missing baz", filter_sens.getStrings().contains("baz"));
	}

}
