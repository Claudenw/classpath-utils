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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;
import org.xenei.classpathutils.Case;
import org.xenei.classpathutils.ClassPathFilter;
import org.xenei.classpathutils.filter.RegexClassFilter;
import org.xenei.classpathutils.filter.parser.Parser;

/**
 * Test RegexClassFilter
 *
 */
public class RegexClassFilterTest {

	private final ClassPathFilter filter_sens;
	private final ClassPathFilter filter_insens;

	private Class<?> t = ClassPathFilter.class;
	private Class<?> f = String.class;

	/**
	 * Constructor
	 */
	public RegexClassFilterTest() {
		filter_sens = new RegexClassFilter(Case.SENSITIVE, "^.+xenei.+$");
		filter_insens = new RegexClassFilter(Case.INSENSITIVE, "^.+Xenei.+$");
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

		assertFalse(filter_sens.accept(t.getName().toUpperCase()));
		assertTrue(filter_insens.accept(t.getName().toUpperCase()));

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
		RegexClassFilter sens = new RegexClassFilter(Case.SENSITIVE, "^.+example.c.+$");
		RegexClassFilter insens = new RegexClassFilter(Case.INSENSITIVE, "^.+Example.c.+$");

		assertTrue(sens.accept(url));
		assertTrue(insens.accept(url));

		url = new URL("http://Example.com");
		assertFalse(sens.accept(url));
		assertTrue(insens.accept(url));

		url = new URL("http://example.net");
		assertFalse(sens.accept(url));
		assertFalse(insens.accept(url));
	}

	/**
	 * Test that toString() works.
	 */
	@Test
	public void testToString() {
		assertEquals("Regex( Sensitive, ^.+xenei.+$ )", filter_sens.toString());
		assertEquals("Regex( Insensitive, ^.+Xenei.+$ )", filter_insens.toString());
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
		assertTrue("Wrong class", cf instanceof RegexClassFilter);
		String[] args = cf.args();
		assertEquals(Case.SENSITIVE.toString(), args[0]);
		assertEquals("^.+xenei.+$", args[1]);

		cf = p.parse(filter_insens.toString());
		assertTrue("Wrong class", cf instanceof RegexClassFilter);
		args = cf.args();
		assertEquals(Case.INSENSITIVE.toString(), args[0]);
		assertEquals("^.+Xenei.+$", args[1]);

	}
}
