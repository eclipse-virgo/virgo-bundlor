Bundlor Samples
===============

Sample Prerequisites
--------------------
* JDK 1.5 or > must be installed and in your system path
* Ant 1.7 or > must be installed and in your system path


Command Line Sample
-------------------
This sample demonstrates how to use Bundlor from its command line interface.
There are both UNIX and Windows scripts for running the command line interface,
and the content of these scripts show proper usage.

	ru.[sh | bat]		Runs Bundlor against a JAR file and manifest template
						transforming a JAR into a bundle.
To use this you must first obtain the test JAR by running ant resolve in the
ant sample directory.


Apache ANT Sample
-----------------
This sample demonstrates how to use Bundlor with Apache ANT.  The important part
of this sample is located in the build.xml file.  In this file notice a
definition of the bundlor tasks using an antlib URI.  In the two main targets,
those bundlor tasks are used into to different ways:

	without-template:	Runs Bundlor against the JAR file without any manifest
						template.  This shows the diagnostic information that
						Bundlor displays when the manifest is incomplete.

	with-template:		Runs Bundlor against the JAR file providing a complete
						manifest template.  This will create a new OSGi bundle
						in the target/ directory.


Apache Maven Sample
-------------------
This sample demonstrates how to use Bundlor with Apache Maven.

Runs Bundlor against a JAR file and manifest template outputting a new OSGi
bundle in the target/ directory. This bundle is ready for use in an OSGi
runtime.
