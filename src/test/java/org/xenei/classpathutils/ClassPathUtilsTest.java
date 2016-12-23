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

package org.xenei.classpathutils;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.xenei.classpathutils.ClassPathUtils;
import org.xenei.classpathutils.filter.PrefixClassFilter;
import org.xenei.classpathutils.filter.WildcardClassFilter;

/**
 * Test the ClassPathUtils.
 *
 */
public class ClassPathUtilsTest {

	/**
	 * Verify that we can find the classes in a jar.
	 * 
	 * @throws IOException
	 *             on error
	 */
	@Test
	public void testFindClassesFromClassJar() throws IOException {
		URL url = ClassPathUtilsTest.class.getResource("classes.jar");
		Set<String> names = ClassPathUtils.findClasses(url.toString() + "!/",
				"org.xenei.classpathutils.testClasses");
		assertEquals(4, names.size());
		names = ClassPathUtils.findClasses(url.toString() + "!/",
				"org.xenei.classpathutils.testClasses.package-info");
		assertEquals(1, names.size());
		names = ClassPathUtils.findClasses(url.toString() + "!/", "com.xenei");
		assertEquals(0, names.size());
	}

	/**
	 * Test that we find no classes in javadoc jar.
	 * 
	 * @throws IOException
	 *             on error
	 */
	@Test
	public void testFindClassesFromJavadocJar() throws IOException {
		URL url = ClassPathUtilsTest.class.getResource("javadoc.jar");
		Set<String> names = ClassPathUtils.findClasses(url.toString() + "!/",
				"org.xenei.classpathutils.testClasses");
		assertEquals(0, names.size());
	}

	/**
	 * Test that we find no classes from source jar.
	 * 
	 * @throws IOException
	 *             on error.
	 */
	@Test
	public void testFindClassesFromSourceJar() throws IOException {
		URL url = ClassPathUtilsTest.class.getResource("sources.jar");
		Set<String> names = ClassPathUtils.findClasses(url.toString() + "!/",
				"org.xenei.classpathutils.testClasses");
		assertEquals(0, names.size());
	}

	/**
	 * Test that we can find classes with directory and package name.
	 * 
	 * @throws IOException
	 *             on exception.
	 */
	@Test
	public void testFindClasses_StringString() throws IOException {
		URL url = ClassPathUtilsTest.class.getResource("/");
		Set<String> names = ClassPathUtils.findClasses(url.toString(),
				"org.xenei.classpathutils.testClasses.sub1");
		assertEquals(2, names.size());
	}

	/**
	 * Test that we can find classes with directory, package name and filter.
	 * 
	 * @throws IOException
	 *             on error.
	 */
	@Test
	public void testFindClasses_StringStringFilter() throws IOException {
		URL url = ClassPathUtilsTest.class.getResource("/org/xenei/classpathutils");
		Set<String> names = ClassPathUtils
				.findClasses(url.toString(), "org.xenei.classpathutils.testClasses",
						new PrefixClassFilter("org.xenei.classpathutils.testClasses.sub1"));
		assertEquals(2, names.size());

		url = ClassPathUtilsTest.class.getResource("/");
		names = ClassPathUtils.findClasses(url.toString(), "org.xenei.classpathutils.testClasses",
				new PrefixClassFilter("org.xenei.classpathutils.testClasses.sub1"));
		assertEquals(2, names.size());
	}

	/**
	 * Test that we can find classes with package name only.
	 * 
	 * @throws IOException
	 *             on error.
	 */
	@Test
	public void testGetClasses_String() throws IOException {
		Collection<Class<?>> classes = ClassPathUtils
				.getClasses("org.xenei.classpathutils.testClasses.sub1");
		assertEquals(2, classes.size());
	}

	/**
	 * Test that we can find classes with package name and filter.
	 * 
	 * @throws IOException
	 *             on error.
	 */
	@Test
	public void testGetClasses_StringFilter() throws IOException {
		Collection<Class<?>> classes = ClassPathUtils.getClasses(
				"org.xenei.classpathutils.testClasses", new WildcardClassFilter("*s.su*"));
		assertEquals(2, classes.size());
	}
}
