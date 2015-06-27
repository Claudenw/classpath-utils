classpath-utils: A suite of utilities to search the classpath 
=============================================================

Maven Repository Info 
---------------------

Release version

     Group Id: org.xenei 
     Artifact Id: classpath-utils 

Snapshot versions are hosted at:

     https://oss.sonatype.org/content/repositories/snapshots/

The ClassPathUtils class contains a number of static methods to locate classes and resources on the class path.  Objects are selected by name and may filtered with a Filter.

Filters
=======

Filters are patterned after the Java FileFilter class and are used to filter resources.  The default classes are defined in <code>org.xenei.classpathutils.filter</code> package.

The filters are implemented as functions and can be parsed from strings.  The case of the function name does not matter.  Methods that  take string matching arguments (e.g. prefix) have arguments that enable or disable case matching.


 
AbstractClass
-------------

The object being tested must be a class class and must be abstract.  All URLs will return false.

**arguments:** none.

**example:** AbstractClass()

And
---

Performs a logical and on two or more filter functions.

**arguments:** two or more filters.

**example:** And( Abstract(), Wildcard( *test* ) )

AnnotationClass
---------------

The object being tested must be a class class and must be an annotation.

**arguments:** none

**example:** AnnotationClass()

False
-----

Always evaluates to false.
		
**arguments:** none

**example:** False()
		
HasAnnotation
-------------

The object being tested must be a class class and must have the specified annotation.
		
**arguments:** the class name of an annotation (e.g. org.junit.Test).  Class must be found in the class loader of the current thread. 

**example:** HasAnnotation( org.junit.Test )


InterfaceClass
--------------

The object being tested must be a class class and must be an interface
		
**arguments:** none

**example:** InterfaceClass()


Name
----

The object string representation must match.
		
**arguments:** An optional sensitive flag followed by one or more fully qualified class names.  if the sensitive flag is not specified _Sensitive_ is assumed.

**example:** Name( Sensitive, java.io.File, java.net.URL )
**example:** Name( java.io.File, java.net.URL )
**example:** Name( Insensitive, java.io.File, java.net.URL )

Not
---

Negates another filter.

**arguments:** another class filter.

**example:** Not( AbstractClass() )


Or
--

Performs a logical _or_ on two or more filters.

**arguments:** two or more other class filters.

**example:** Or( AbstractClass(), Wildcard( *test* ) )


Prefix
------

The object string representation must match the prefix.  Useful for stripping out inner classes, or for selecting entire package trees.

**arguments:** An optional sensitive flag followed by one or more class name prefixes.  if the sensitive flag is not specified _Sensitive_ is assumed.

**example:** prefix( Sensitive, java.io. )
**example:** prefix( java.io. )
**example:** prefix( Insensitive, java.io. )


Regex
-----

The object string representation must match the regular expression.

**arguments:** An optional sensitive flag followed by one regular expression.  if the sensitive flag is not specified _Sensitive_ is assumed.

**example:** Regex( Sensitive, ^.*Test.*$ )
**example:** Regex( ^.*Test.*$  )
**example:** Regex( Insensitive, ^.*Test.*$ )

Suffix
------

The object string representation must match the suffix.  

**arguments:** An optional sensitive flag followed by one or more class name prefixes.  if the sensitive flag is not specified _Sensitive_ is assumed.

**example:** suffix( Sensitive, io.File )
**example:** suffix( io.File )
**example:** suffix( Insensitive, io.File )

True
----

Always evaluates to true.
		
**arguments:** none

**example:** True()


Wildcard
--------

Matches the object string representation using the standard asterisk (*) to match multiple characters and the question mark (?) to match single characters.  Not as powerful as the Regex filter but easier to use.

**arguments:** An optional sensitive flag followed by one regular expression.  if the sensitive flag is not specified _Sensitive_ is assumed.

**example:** Wildcard( Sensitive, *Test? )
**example:** Wildcard( *Test?  )
**example:** Wildcard( Insensitive, *Test? )



