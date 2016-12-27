/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.xenei.classpathutils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xenei.classpathutils.filter.PrefixClassFilter;
import org.xenei.classpathutils.filter.RegexClassFilter;
import org.xenei.classpathutils.filter.AndClassFilter;
import org.xenei.classpathutils.filter.NotClassFilter;
import org.xenei.classpathutils.filter.SuffixClassFilter;

/**
 * Package of class path searching utilities
 *
 */
public class ClassPathUtils {

	private static final Log LOG = LogFactory.getLog(ClassPathUtils.class);

	private static PrintStream os;

	static {
		String s = System.getProperty("ClassPathUtils_DEBUG");
		if (s != null) {
			os = System.out;
		}
	}

	/**
	 * Write to log if log is enabled.
	 * 
	 * @param args
	 */
	public static void doLog(Object... args) {
		if (os != null) {
			if (args.length == 1) {
				os.println(args[0].toString());
			} else {
				os.println(String.format(args[0].toString(), Arrays.copyOfRange(args, 1, args.length)));
			}
		}
	}

	/**
	 * Get the classloader.
	 * 
	 * @return The classloader to use for finding classes and resources.
	 */
	public static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	/**
	 * Recursive method used to find all classes in a given directory and
	 * subdirs. Adapted from http://snippets.dzone.com/posts/show/4831 and
	 * extended to support use of JAR files
	 *
	 * @param directory
	 *            The base directory
	 * @param packageName
	 *            The package name for classes found inside the base directory
	 * @return The classes
	 * @throws IOException
	 *             on error.
	 */
	public static Set<String> findClasses(final String directory, final String packageName) throws IOException {
		return findClasses(directory, packageName, new PrefixClassFilter(packageName));
	}

	/**
	 * Find the classes in a directory and sub directory.
	 * 
	 * @param directory
	 *            The directory or jar file to search.
	 * @param packageName
	 *            The list of packages to look for.
	 * @param filter
	 *            The filter to apply to results.
	 * @return list of class names that match the filter.
	 * @throws IOException
	 *             on error.
	 */
	public static Set<String> findClasses(final String directory, String packageName, final ClassPathFilter filter)
			throws IOException {

		final Set<String> classes = new HashSet<String>();
		ClassPathFilter myFilter = new AndClassFilter(new SuffixClassFilter(".class"),
				new NotClassFilter(new PrefixClassFilter("META")),
				new NotClassFilter(new RegexClassFilter(".+\\$[0-9]+[\\.\\$].*")), 
				new PrefixClassFilter( packageName.replace("/", ".")),
				filter).optimize();

		if (LOG.isDebugEnabled() || os != null) {
			String s = String.format("finding classes pkg: %s filter: %s ", packageName, myFilter);
			LOG.debug(s);
			doLog(s);
		}
		if (directory.contains("!") || directory.endsWith(".jar")) {
			handleJar(classes, directory, myFilter);
		} else {
			String dirStr = directory.startsWith("file:") ? directory.substring("file:".length()) : directory;
			scanDir(classes, packageName, new File(dirStr), myFilter);
		}

		Set<String> retval = new HashSet<String>();
		for (String s : classes) {
			retval.add(s.substring(0, s.length() - ".class".length()));
		}
		return retval;

	}

	/**
	 * Find all classes accessible from the context class loader which belong to
	 * the given package and sub packages.
	 *
	 * An empty or null packageName = all packages.
	 *
	 * @param packageName
	 *            The base package or class name.
	 * @return A collection of Class objects.
	 */
	public static Collection<Class<?>> getClasses(final String packageName) {
		return getClasses(packageName, new PrefixClassFilter(packageName));
	}

	/**
	 * Get a collection of classes in the package name that meet the filter.
	 * Classes will be loaded from the current thread class loader.
	 * 
	 * @param packageName
	 *            The package name to locate.
	 * @param filter
	 *            the ClassFilter to filter the results with.
	 * @return A collection of Class objects.
	 */
	public static Collection<Class<?>> getClasses(final String packageName, ClassPathFilter filter) {
		final ClassLoader classLoader = getClassLoader();
		if (classLoader == null) {
			LOG.error("Class loader may not be null.  No class loader for current thread");
			return Collections.emptyList();
		}
		return getClasses(classLoader, packageName, filter);
	}

	/**
	 * Find all classes accessible from the class loader which belong to the
	 * given package and sub packages.
	 *
	 * Adapted from http://snippets.dzone.com/posts/show/4831 and extended to
	 * support use of JAR files
	 *
	 * @param classLoader
	 *            The class loader to load the classes from.
	 * @param packageName
	 *            The base package or class name
	 * @return A collection of Class objects
	 */
	public static Collection<Class<?>> getClasses(final ClassLoader classLoader, final String packageName) {
		return getClasses(classLoader, packageName, new PrefixClassFilter(packageName));
	}

	/**
	 * Find all classes accessible from the class loader which belong to the
	 * given package and sub packages.
	 *
	 * Adapted from http://snippets.dzone.com/posts/show/4831 and extended to
	 * support use of JAR files
	 *
	 * @param classLoader
	 *            The class loader to load the classes from.
	 * @param packageName
	 *            The package name to locate the classes in.
	 * @param filter
	 *            The filter for the classes.
	 * @return A collection of Class objects
	 */
	public static Collection<Class<?>> getClasses(final ClassLoader classLoader, final String packageName,
			final ClassPathFilter filter) {
		if (classLoader == null) {
			LOG.error("Class loader may not be null.");
			return Collections.emptyList();
		}
		if (packageName == null) {
			LOG.error("Package name may not be null.");
			return Collections.emptyList();
		}

		Set<URL> resources = getAllResources(classLoader);

		final Set<Class<?>> classes = new HashSet<Class<?>>();
		final Set<String> directories = new HashSet<String>();

		for (URL resource : resources) {
			if (LOG.isInfoEnabled() || os != null) {

				String s = String.format("getting classes processing: %s ", resource);
				LOG.debug(s);
				doLog(s);
			}

			String dir = resource.getPath();
			if (LOG.isDebugEnabled()) {
				LOG.debug(String.format("Processing dir %s", dir));
			}

			if (!directories.contains(dir)) {
				directories.add(dir);

				try {
					for (final String clazz : findClasses(dir, packageName, filter)) {
						try {
							if (LOG.isDebugEnabled()) {
								LOG.debug(String.format("Adding class %s", clazz));
							}
							classes.add(Class.forName(clazz, false, classLoader));
						} catch (final ClassNotFoundException e) {
							String err = String.format("Unable to get class %s due to %s", clazz, e.toString());
							doLog(err);
							LOG.warn(err);
						} catch (NoClassDefFoundError e) {
							String err = String.format("Unable to get class %s due to %s", clazz, e.toString());
							doLog(err);
							LOG.warn(err);
						}
					}
				} catch (final IOException e) {
					doLog(e.toString());
					LOG.warn(e.toString());
				}
			}
		}

		return classes;
	}

	/**
	 * Return the set of classes from the collection that pass the filter.
	 * 
	 * @param classes
	 *            The collection of classes to filter.
	 * @param filter
	 *            The filter to use.
	 * @return the set of Class objects that pass the filter.
	 */
	public static Set<Class<?>> filterClasses(Collection<Class<?>> classes, ClassPathFilter filter) {
		Set<Class<?>> retval = new HashSet<Class<?>>();
		for (Class<?> clazz : classes) {
			if (filter.accept(clazz)) {
				retval.add(clazz);
			}
		}
		return retval;
	}

	/**
	 * Return the set of classes from the collection that pass the filter.
	 * 
	 * @param classNames
	 *            the collection of class names.
	 * @param filter
	 *            The filter to apply.
	 * @return the set of class names that pass the filter.
	 */
	public static Set<String> filterClassNames(Collection<String> classNames, ClassPathFilter filter) {
		Set<String> retval = new HashSet<String>();
		for (String className : classNames) {
			if (filter.accept(className)) {
				retval.add(className);
			}
		}
		return retval;
	}

	/**
	 * Get the array of class path elements.
	 *
	 * These are strings separated by java.class.path property
	 *
	 * @return Array of class path elements
	 */
	public static String[] getClassPathElements() {
		final String splitter = String.format("\\%s", System.getProperty("path.separator"));
		final String[] classPath = System.getProperty("java.class.path").split(splitter);
		return classPath;
	}

	/**
	 * Get all the interfaces for the class.
	 *
	 * @param clazz
	 *            The class to find interfaces for.
	 * @return set of interfaces implemented by clazz.
	 */
	public static Set<Class<?>> getAllInterfaces(final Class<?> clazz) {
		// set of implementation classes
		final Set<Class<?>> implClasses = new LinkedHashSet<Class<?>>();
		// populate the set of implementation classes
		ClassPathUtils.getAllInterfaces(implClasses, clazz);
		return implClasses;
	}

	/**
	 * Get all the interfaces for the class that meet the filter.
	 *
	 * @param clazz
	 *            The class to find interfaces for.
	 * @param filter
	 *            The filter to apply.
	 * @return set of interfaces implemented by clazz.
	 */
	public static Set<Class<?>> getAllInterfaces(final Class<?> clazz, ClassPathFilter filter) {
		return filterClasses(getAllInterfaces(clazz), filter);
	}

	/**
	 * Get all the interfaces that the class implements. Adds the interfaces to
	 * the set of classes.
	 *
	 * This method calls recursively to find all parent interfaces.
	 *
	 * @param set
	 *            The set off classes to add the interface classes to.
	 * @param c
	 *            The class to check.
	 */
	public static void getAllInterfaces(final Set<Class<?>> set, final Class<?> c) {
		if ((c == null) || (c == Object.class)) {
			return;
		}
		for (final Class<?> i : c.getClasses()) {
			if (i.isInterface()) {
				if (!set.contains(i)) {
					set.add(i);
					getAllInterfaces(set, i);
				}
			}
		}
		for (final Class<?> i : c.getInterfaces()) {
			if (!set.contains(i)) {
				set.add(i);
				getAllInterfaces(set, i);
			}
		}
		getAllInterfaces(set, c.getSuperclass());
	}

	/**
	 * Recursive method used to find all classes in a given directory and
	 * subdirs. Adapted from http://snippets.dzone.com/posts/show/4831 and
	 * extended to support use of JAR files
	 *
	 * @param directory
	 *            The base directory
	 * @param packageName
	 *            The package name for classes found inside the base directory
	 * @return The classes
	 * @throws IOException
	 *             on error.
	 */
	public static Set<String> findResources(final String directory, final String packageName) throws IOException {
		return findResources(directory, packageName, new PrefixClassFilter(packageName));
	}

	/**
	 * Find the classes in a directory and sub directory.
	 * 
	 * @param directory
	 *            The directory or jar file to search.
	 * @param packageName
	 *            The list of packages to look for.
	 * @param filter
	 *            The filter to apply to results.
	 * @return list of class names that match the filter.
	 * @throws IOException
	 *             on error.
	 */
	public static Set<String> findResources(final String directory, String packageName, final ClassPathFilter filter)
			throws IOException {
		ClassPathFilter myFilter = filter.optimize();

		if (LOG.isInfoEnabled() || os != null) {

			String s = String.format("finding resources pkg: %s filter: %s ", packageName, myFilter);
			LOG.info(s);
			doLog(s);

		}

		final Set<String> classes = new HashSet<String>();

		if (directory.contains("!") || directory.endsWith(".jar")) {
			handleJar(classes, directory, myFilter);

		} else {
			String dirStr = directory.startsWith("file:") ? directory.substring("file:".length()) : directory;
			scanDir(classes, packageName, new File(dirStr), myFilter);
		}
		return classes;
	}

	/**
	 * Find all classes accessible from the context class loader which belong to
	 * the given package and sub packages.
	 *
	 * An empty or null packageName = all packages.
	 *
	 * @param packageName
	 *            The base package or class name.
	 * @return A collection of Class objects.
	 */
	public static Collection<URL> getResources(final String packageName) {
		return getResources(packageName, new PrefixClassFilter(packageName));
	}

	/**
	 * Get a collection of classes in the package name that meet the filter.
	 * Classes will be loaded from the current thread class loader.
	 * 
	 * @param packageName
	 *            The package name to locate.
	 * @param filter
	 *            the ClassFilter to filter the results with.
	 * @return A collection of Class objects.
	 */
	public static Collection<URL> getResources(final String packageName, ClassPathFilter filter) {
		final ClassLoader classLoader = getClassLoader();
		if (classLoader == null) {
			LOG.error("Class loader may not be null.  No class loader for current thread");
			return Collections.emptyList();
		}

		return getResources(classLoader, packageName, filter);
	}

	/**
	 * Find all classes accessible from the class loader which belong to the
	 * given package and sub packages.
	 *
	 * Adapted from http://snippets.dzone.com/posts/show/4831 and extended to
	 * support use of JAR files
	 *
	 * @param classLoader
	 *            The class loader to load the classes from.
	 * @param packageName
	 *            The base package or class name
	 * @return A collection of Class objects
	 */
	public static Collection<URL> getResources(final ClassLoader classLoader, final String packageName) {
		return getResources(classLoader, packageName, new PrefixClassFilter(packageName));
	}

	private static Set<URL> getAllResources(final ClassLoader classLoader) {

		Set<URL> lst = new LinkedHashSet<URL>();
		try {
			Enumeration<URL> e = classLoader.getResources("");
			while (e.hasMoreElements()) {
				lst.add(e.nextElement());
			}
		} catch (final IOException e1) {
			LOG.error(e1.toString());
		}

		/*
		 * if we return here it would be a short search and only return from the
		 * current class loader. this may not inlcude all the jars from the
		 * claspath.
		 */
		if (classLoader instanceof URLClassLoader) {
			lst.addAll(Arrays.asList(((URLClassLoader) classLoader).getURLs()));
		}
		ClassLoader parent = classLoader.getParent();
		if (parent != null && classLoader != parent) {
			lst.addAll(getAllResources(parent));
		}
		doLog("Found resources: %s", lst);

		return lst;
	}

	/**
	 * Find all classes accessible from the class loader which belong to the
	 * given package and sub packages.
	 *
	 * Adapted from http://snippets.dzone.com/posts/show/4831 and extended to
	 * support use of JAR files
	 *
	 * @param classLoader
	 *            The class loader to load the classes from.
	 * @param packageName
	 *            The package name to locate the classes in.
	 * @param filter
	 *            The filter for the classes.
	 * @return A collection of URL objects
	 */
	public static Collection<URL> getResources(final ClassLoader classLoader, final String packageName,
			final ClassPathFilter filter) {
		if (classLoader == null) {
			LOG.error("Class loader may not be null.");
			return Collections.emptyList();
		}
		if (packageName == null) {
			LOG.error("Package name may not be null.");
			return Collections.emptyList();
		}

		if (LOG.isInfoEnabled() || os != null) {

			String s = String.format("getting resources pkg: %s filter: %s ", packageName, filter);
			LOG.info(s);
			if (os != null) {
				os.println(s);
			}
		}

		Set<URL> resources = getAllResources(classLoader);

		final Set<URL> classes = new HashSet<URL>();
		final Set<String> directories = new HashSet<String>();
		for (URL resource : resources) {
			String dir = resource.getPath();
			if (!directories.contains(dir)) {
				directories.add(dir);

				try {
					for (final String clazz : findResources(dir, packageName, filter)) {
						if (LOG.isDebugEnabled()) {
							LOG.debug(String.format("Adding class %s", clazz));
						}
						URL url = classLoader.getResource(clazz);
						if (url != null) {
							classes.add(url);
						} else {
							LOG.warn(String.format("Unable to locate: %s", clazz));
						}
					}
				} catch (final IOException e) {
					LOG.warn(e.toString());
				}
			}
		}

		return classes;
	}

	/**
	 * Handle the files in a given package. The directory is already known to be
	 * at or under the directory specified by the package name. So we just have
	 * to find matches.
	 * 
	 * @param classes
	 * @param packageName
	 * @param dir
	 * @param cFilter
	 */
	private static void handleDir(Set<String> classes, String packageName, File dir, ClassPathFilter cFilter) {
		if (!dir.exists()) {
			return;
		}
		if (dir.isDirectory()) {
			// handle all the classes in the directory
			for (File file : dir.listFiles((FileFilter) new NotFileFilter(DirectoryFileFilter.DIRECTORY))) {
				handleDir(classes, packageName, file, cFilter);
			}
			// handle all the sub-directories
			for (File file : dir.listFiles((FileFilter) new AndFileFilter(DirectoryFileFilter.DIRECTORY,
					new NotFileFilter(new PrefixFileFilter("."))))) {
				final String newPkgName = String.format("%s%s%s", packageName, (packageName.length() > 0 ? "." : ""),
						file.getName());
				handleDir(classes, newPkgName, file, cFilter);
			}
		} else {
			String className = String.format("%s%s%s", packageName, (packageName.length() > 0 ? "." : ""),
					modifyFileName(dir.getName()));
			if (cFilter.accept(className)) {
				classes.add(className);
			}
		}
	}

	/**
	 * Scan a directory for packages that match. This method is used prior to
	 * finding a matching directory. Once the package names is matched
	 * handleDir() is used.
	 * 
	 * @param classes
	 *            The classes that have been found.
	 * @param packageName
	 *            The package name for classes to find.
	 * @param dir
	 *            The directory to scan.
	 * @param cFilter
	 *            The class acceptance filter.
	 */
	private static void scanDir(Set<String> classes, String packageName, File dir, ClassPathFilter cFilter) {
		if (!dir.exists()) {
			return;
		}
		if (dir.isDirectory()) {
			if (dir.getPath().endsWith(packageName.replace('.', '/'))) {
				// we have a match
				handleDir(classes, packageName, dir, cFilter);
			} else {
				// no match check next level
				for (File file : dir.listFiles((FileFilter) new AndFileFilter(DirectoryFileFilter.DIRECTORY,
						new NotFileFilter(new PrefixFileFilter("."))))) {
					scanDir(classes, packageName, file, cFilter);
				}
			}
		}
		// if it is not a directory we don't process it here as we are looking
		// for directories that start with the packageName.
	}

	/*
	 * convert a jar filename into a class name
	 */
	private static String modifyFileName(String fileName) {
		return fileName.replace('/', '.');
	}

	/**
	 * handle finding classes in a jar.
	 * 
	 * @param classes
	 *            the classes that have been found.
	 * @param directory
	 *            The directory path to a file in a jar or the jar itself.
	 * @param filter
	 *            The classes to accept.
	 * @throws IOException
	 */
	private static void handleJar(Set<String> classes, String directory, ClassPathFilter filter) throws IOException {
		final String[] split = directory.split("!");
		URL jar = null;
		try {
			jar = new URL(split[0]);
		} catch (MalformedURLException e) {
			// expected in some cases
			jar = new File(split[0]).toURI().toURL();
		}
		final String prefix = (split.length > 1 && split[1].length() > 0) ? split[1].substring(1) : "";
		final ZipInputStream zip = new ZipInputStream(jar.openStream());
		ClassPathFilter myFilter = new AndClassFilter(new PrefixClassFilter(prefix), filter).optimize();
		ZipEntry entry = null;
		while ((entry = zip.getNextEntry()) != null) {
			final String className = modifyFileName(entry.getName());
			if (myFilter.accept(className)) {
				classes.add(className);
			}
		}
	}

}
