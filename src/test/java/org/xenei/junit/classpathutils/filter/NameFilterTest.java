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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;
import org.xenei.classpathutils.Case;
import org.xenei.classpathutils.ClassPathFilter;
import org.xenei.classpathutils.filter.NameFilter;
import org.xenei.classpathutils.filter.parser.Parser;

/**
 * Test NameClassFilter
 *
 */
public class NameFilterTest {

	private final ClassPathFilter filter_sens;
	private final ClassPathFilter filter_insens;

	private Class<?> t = ClassPathFilter.class;
	private Class<?> f = String.class;

	/**
	 * Constructor.
	 */
	public NameFilterTest() {
		filter_sens = new NameFilter(Case.SENSITIVE, t.getName());
		filter_insens = new NameFilter(Case.INSENSITIVE, t.getName());
	}

	/**
	 * Test that accept(Class) works
	 */
	@Test
	public void testAcceptClass() {
		assertTrue(filter_sens.accept(t));
		assertTrue(filter_insens.accept(t));

		assertFalse(filter_sens.accept(f));
		assertFalse(filter_insens.accept(f));
	}

	/**
	 * Test that accept(String) works.
	 */
	@Test
	public void testAccceptString() {

		assertTrue(filter_sens.accept(t.getName()));
		assertTrue(filter_insens.accept(t.getName()));

		assertFalse(filter_sens.accept(t.getName().toLowerCase()));
		assertTrue(filter_insens.accept(t.getName().toLowerCase()));

		assertFalse(filter_sens.accept(f.getName()));
		assertFalse(filter_insens.accept(f.getName()));
	}

	/**
	 * Test that accept(String) works.
	 * 
	 * @throws MalformedURLException
	 */
	@Test
	public void testAccceptURL() throws MalformedURLException {
		URL url = new URL("http://example.com");
		URL url2 = new URL("http://Example.com");
		URL url3 = new URL("ftp://example.com");

		NameFilter sens = new NameFilter(Case.SENSITIVE, "http://example.com");
		NameFilter insens = new NameFilter(Case.INSENSITIVE,
				"http://example.com");

		assertTrue(sens.accept(url));
		assertTrue(insens.accept(url));

		assertFalse(sens.accept(url2));
		assertTrue(insens.accept(url2));

		assertFalse(sens.accept(url3));
		assertFalse(insens.accept(url3));
	}

	/**
	 * Test that toString() works.
	 */
	@Test
	public void testToString() {
		assertEquals("Name( Sensitive, " + t.getName() + " )",
				filter_sens.toString());
		assertEquals("Name( Insensitive, " + t.getName() + " )",
				filter_insens.toString());
	}

	/**
	 * Test that the parser parses string representation correctly.
	 * 
	 * @throws Exception
	 *             on any Exception.
	 */
	@Test
	public void testParse() throws Exception {
		Parser p = new Parser();

		ClassPathFilter cf = p.parse(filter_sens.toString());
		assertTrue("Wrong class", cf instanceof NameFilter);
		String[] args = cf.args();
		assertEquals(Case.SENSITIVE.toString(), args[0]);
		assertEquals(t.getName(), args[1]);

		cf = p.parse(filter_insens.toString());
		assertTrue("Wrong class", cf instanceof NameFilter);
		args = cf.args();
		assertEquals(Case.INSENSITIVE.toString(), args[0]);
		assertEquals(t.getName(), args[1]);

	}
}
