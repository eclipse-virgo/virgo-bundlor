How to build Bundlor locally
==========================

Since 1.2.0 Bundlor is built with Gradle.

During `./gradlew build` we
* build the `org.eclipse.virgo.bundlor` sources
* build the `org.eclipse.virgo.bundlor.ant` sources
* build the `org.eclipse.virgo.bundlor.blint` sources
* build the `org.eclipse.virgo.bundlor.commandline` sources

The resulting jar files can be found in the `build/libs` folder of each project.

Prerequisites
=============

Java 8 must be installed and `JAVA_HOME` must be set.

Import Bundlor projects into Eclipse
==========================

Use the Buildship Eclipse plugin from Gradle to import Bundlor projects into your workspace.

You can generate the Eclipse IDE project metadata by issuing `./gradlew eclipse` command on the command line before import. 

