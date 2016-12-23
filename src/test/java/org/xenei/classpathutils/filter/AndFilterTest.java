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

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;
import org.xenei.classpathutils.ClassPathFilter;
import org.xenei.classpathutils.filter.AndClassFilter;
import org.xenei.classpathutils.filter.parser.Parser;

/**
 * Test the AndClassFilter.
 *
 */
public class AndFilterTest {

	private Class<?> cls = String.class;
	private String str = cls.getName();

	/**
	 * Test that accept(Class) works
	 */
	@Test
	public void testAcceptClass() {
		ClassPathFilter filter = new AndClassFilter(ClassPathFilter.FALSE,
				ClassPathFilter.FALSE);
		assertFalse(filter.accept(cls));

		filter = new AndClassFilter(ClassPathFilter.FALSE, ClassPathFilter.TRUE);
		assertFalse(filter.accept(cls));

		filter = new AndClassFilter(ClassPathFilter.TRUE, ClassPathFilter.FALSE);
		assertFalse(filter.accept(cls));

		filter = new AndClassFilter(ClassPathFilter.TRUE, ClassPathFilter.TRUE);
		assertTrue(filter.accept(cls));
	}

	/**
	 * Test that accept(String) works.
	 */
	@Test
	public void testAccceptString() {
		ClassPathFilter filter = new AndClassFilter(ClassPathFilter.FALSE,
				ClassPathFilter.FALSE);
		assertFalse(filter.accept(str));

		filter = new AndClassFilter(ClassPathFilter.FALSE, ClassPathFilter.TRUE);
		assertFalse(filter.accept(str));

		filter = new AndClassFilter(ClassPathFilter.TRUE, ClassPathFilter.FALSE);
		assertFalse(filter.accept(str));

		filter = new AndClassFilter(ClassPathFilter.TRUE, ClassPathFilter.TRUE);
		assertTrue(filter.accept(str));
	}

	/**
	 * Test that accept(String) works.
	 * 
	 * @throws MalformedURLException
	 */
	@Test
	public void testAccceptURL() throws MalformedURLException {
		URL url = new URL("http://example.com");
		ClassPathFilter filter = new AndClassFilter(ClassPathFilter.FALSE,
				ClassPathFilter.FALSE);
		assertFalse(filter.accept(url));

		filter = new AndClassFilter(ClassPathFilter.FALSE, ClassPathFilter.TRUE);
		assertFalse(filter.accept(url));

		filter = new AndClassFilter(ClassPathFilter.TRUE, ClassPathFilter.FALSE);
		assertFalse(filter.accept(url));

		filter = new AndClassFilter(ClassPathFilter.TRUE, ClassPathFilter.TRUE);
		assertTrue(filter.accept(url));
	}

	/**
	 * Test that toString() works.
	 */
	@Test
	public void testToString() {
		ClassPathFilter filter = new AndClassFilter(ClassPathFilter.FALSE,
				ClassPathFilter.TRUE);
		assertEquals("And( False(), True() )", filter.toString());
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

		ClassPathFilter filter = new AndClassFilter(ClassPathFilter.FALSE,
				ClassPathFilter.TRUE);

		ClassPathFilter cf = p.parse(filter.toString());
		assertTrue("wrong class type", cf instanceof AndClassFilter);
		String[] args = cf.args();
		assertEquals(ClassPathFilter.FALSE.toString(), args[0]);
		assertEquals(ClassPathFilter.TRUE.toString(), args[1]);
	}

}
