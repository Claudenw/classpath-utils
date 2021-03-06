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

import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;
import org.xenei.classpathutils.ClassPathFilter;
import org.xenei.classpathutils.filter.HasAnnotationClassFilter;
import org.xenei.classpathutils.filter.parser.Parser;

/**
 * Test HasAnnotationClassFilter
 *
 */
public class HasAnnotationClassFilterTest {
	private ClassPathFilter filter = new HasAnnotationClassFilter(Deprecated.class);
	private Class<?> t = X.class;
	private Class<?> f = File.class;

	/**
	 * Test that accept(Class) works
	 */
	@Test
	public void testAcceptClass() {
		assertTrue(filter.accept(t));
		assertFalse(filter.accept(f));
	}

	/**
	 * Test that accept(String) works.
	 */
	@Test
	public void testAccceptString() {
		assertTrue(filter.accept(_AbstractBaseFilter.removeDotClass(t.getName())));
		assertFalse(filter.accept(_AbstractBaseFilter.removeDotClass(f.getName())));

		assertTrue(filter.accept(t.getName()));
		assertFalse(filter.accept(f.getName()));
	}

	/**
	 * Test that accept(URL) works always false
	 * 
	 * @throws MalformedURLException
	 */
	@Test
	public void testAcceptURL() throws MalformedURLException {
		assertFalse(filter.accept(new URL("http://" + t.getName())));
		assertFalse(filter.accept(new URL("http://" + f.getName())));
	}

	/**
	 * Test that toString() works.
	 */
	@Test
	public void testToString() {
		assertEquals("HasAnnotation( " + Deprecated.class.getName() + " )", filter.toString());
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

		ClassPathFilter cf = p.parse(filter.toString());
		assertTrue("Wrong type", cf instanceof HasAnnotationClassFilter);
		String[] args = cf.args();
		assertEquals(1, args.length);
		assertEquals(Deprecated.class.getName(), args[0]);
	}

	@Deprecated
	private class X {

	}
}
