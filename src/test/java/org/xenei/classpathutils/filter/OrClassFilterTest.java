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
import java.util.List;

import org.junit.Test;
import org.xenei.classpathutils.ClassPathFilter;
import org.xenei.classpathutils.filter.OrClassFilter;
import org.xenei.classpathutils.filter.parser.Parser;

/**
 * Test OrClassFilter
 *
 */
public class OrClassFilterTest {

	private Class<?> cls = String.class;
	private String str = cls.getName();

	/**
	 * Test that accept(Class) works
	 */
	@Test
	public void testAcceptClass() {
		ClassPathFilter filter = new OrClassFilter(ClassPathFilter.FALSE, ClassPathFilter.FALSE);
		assertFalse(filter.accept(cls));

		filter = new OrClassFilter(ClassPathFilter.FALSE, ClassPathFilter.TRUE);
		assertTrue(filter.accept(cls));

		filter = new OrClassFilter(ClassPathFilter.TRUE, ClassPathFilter.FALSE);
		assertTrue(filter.accept(cls));

		filter = new OrClassFilter(ClassPathFilter.TRUE, ClassPathFilter.TRUE);
		assertTrue(filter.accept(cls));
	}

	/**
	 * Test that accept(String) works.
	 */
	@Test
	public void testAccceptString() {
		ClassPathFilter filter = new OrClassFilter(ClassPathFilter.FALSE, ClassPathFilter.FALSE);
		assertFalse(filter.accept(str));

		filter = new OrClassFilter(ClassPathFilter.FALSE, ClassPathFilter.TRUE);
		assertTrue(filter.accept(str));

		filter = new OrClassFilter(ClassPathFilter.TRUE, ClassPathFilter.FALSE);
		assertTrue(filter.accept(str));

		filter = new OrClassFilter(ClassPathFilter.TRUE, ClassPathFilter.TRUE);
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

		ClassPathFilter filter = new OrClassFilter(ClassPathFilter.FALSE, ClassPathFilter.FALSE);
		assertFalse(filter.accept(url));

		filter = new OrClassFilter(ClassPathFilter.FALSE, ClassPathFilter.TRUE);
		assertTrue(filter.accept(url));

		filter = new OrClassFilter(ClassPathFilter.TRUE, ClassPathFilter.FALSE);
		assertTrue(filter.accept(url));

		filter = new OrClassFilter(ClassPathFilter.TRUE, ClassPathFilter.TRUE);
		assertTrue(filter.accept(url));
	}

	/**
	 * Test that toString() works.
	 */
	@Test
	public void testToString() {
		ClassPathFilter filter = new OrClassFilter(ClassPathFilter.FALSE, ClassPathFilter.TRUE);
		assertEquals("Or( False(), True() )", filter.toString());
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

		ClassPathFilter filter = new OrClassFilter(ClassPathFilter.FALSE, ClassPathFilter.TRUE);

		ClassPathFilter cf = p.parse(filter.toString());
		assertTrue("wrong class type", cf instanceof OrClassFilter);
		String[] args = cf.args();
		assertEquals(ClassPathFilter.FALSE.toString(), args[0]);
		assertEquals(ClassPathFilter.TRUE.toString(), args[1]);

	}

	@Test
	public void testOptimize() throws Exception {
		NameClassFilter foo = new NameClassFilter("foo");
		OrClassFilter ncf = new OrClassFilter(foo, foo);
		ClassPathFilter filter = ncf.optimize();
		assertEquals(foo, filter);

		ncf = new OrClassFilter(TrueClassFilter.TRUE, foo, FalseClassFilter.FALSE);
		filter = ncf.optimize();
		assertEquals(ClassPathFilter.TRUE, filter);

		NameClassFilter bar = new NameClassFilter("bar");
		ncf = new OrClassFilter(foo, FalseClassFilter.FALSE, bar);
		filter = ncf.optimize();

		assertTrue(filter instanceof OrClassFilter);

		List<ClassPathFilter> fLst = ((OrClassFilter) filter).getFilters();
		assertEquals(2, fLst.size());

		assertTrue(fLst.contains(foo));
		assertTrue(fLst.contains(bar));

		HasAnnotationClassFilter anno = new HasAnnotationClassFilter(Test.class);
		ncf = new OrClassFilter(anno, bar);
		filter = ncf.optimize();

		assertTrue(filter instanceof OrClassFilter);
		fLst = ((OrClassFilter) filter).getFilters();
		assertEquals(2, fLst.size());
		assertEquals(bar, fLst.get(0));
		assertEquals(anno, fLst.get(1));

	}
}
