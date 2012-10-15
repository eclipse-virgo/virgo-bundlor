/*******************************************************************************
 * Copyright (c) 2012 VMware Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   VMware Inc. - initial contribution
 *******************************************************************************/
package org.eclipse.virgo.bundlor

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.logging.LogLevel

/**
 * Contribute a 'bundlor' task capable of creating an OSGi manifest. Task is tied
 * to the lifecycle by having the 'jar' task depend on 'bundlor'.
 *
 * @author Chris Beams
 * @author Luke Taylor
 * @see http://www.springsource.org/bundlor
 * @see http://static.springsource.org/s2-bundlor/1.0.x/user-guide/html/ch04s02.html
 */
public class BundlorPlugin implements Plugin<Project> {

    public void apply(Project project) {

        project.configurations { bundlorconf }

	    project.repositories.maven( {name = 'bundlor'; url = 'http://build.eclipse.org/rt/virgo/maven/bundles/release'})
        project.repositories.maven( {name = 'springsource'; url = 'http://repository.springsource.com/maven/bundles/external'})
        project.dependencies {
             bundlorconf 'org.eclipse.virgo.bundlor:org.eclipse.virgo.bundlor.ant:1.1.1.RELEASE',
                         'org.eclipse.virgo.bundlor:org.eclipse.virgo.bundlor:1.1.1.RELEASE',
                         'org.eclipse.virgo.bundlor:org.eclipse.virgo.bundlor.blint:1.1.1.RELEASE'
        }

        project.tasks.add("bundlor") {
			
            ext {
                dependsOn project.compileJava
                group = 'Build'
                description = 'Generates an OSGi-compatibile MANIFEST.MF file.'

                enabled = true
                failOnWarnings = true
                bundleName = null
                bundleVersion = project.version
                bundleVendor = 'SpringSource'
                bundleSymbolicName = null
                bundleManifestVersion = '2'
                importTemplate = []
		        exportTemplate = []
		        excludedImports = []
		        excludedExports = []
                manifestTemplate = null
		        manifestTemplatePath = null

                outputDir = new File("${project.buildDir}/bundlor")
		        propertiesFile = new File("${project.projectDir}/gradle.properties")
            }
			
            def manifest = new File("${outputDir}/META-INF/MANIFEST.MF")

            // inform gradle what directory this task writes so that
            // it can be removed when issuing `gradle cleanBundlor`
            outputs.dir outputDir

            // incremental build configuration
            //   if the manifest output file already exists, the bundlor
            //   task will be skipped *unless* any of the following are true
            //   * manifestTemplate or other task properties have been changed
            //   * main classpath dependencies have been changed
            //   * main java sources for this project have been modified
            outputs.files manifest
            /* TODO ask gradle team how to do this such that inputs are lazy
            inputs.property 'bundleName', bundleName
            inputs.property 'bundleVersion', bundleVersion
            inputs.property 'bundleVendor', bundleVendor
            inputs.property 'bundleSymbolicName', bundleSymbolicName
            inputs.property 'bundleManifestVersion', bundleManifestVersion
            inputs.property 'manifestTemplate', manifestTemplate
            inputs.property 'importTemplate', importTemplate
            */
            inputs.files project.sourceSets.main.runtimeClasspath

            // the bundlor manifest should be evaluated as part of the jar task's
            // incremental build
            project.jar {
                dependsOn 'bundlor'
                inputs.files manifest
            }

            project.jar.manifest.from manifest

            doFirst {
                if (bundleName == null)
                    bundleName = project.description

                project.ant.taskdef(
                    resource: 'org/eclipse/virgo/bundlor/ant/antlib.xml',
                    classpath: project.configurations.bundlorconf.asPath)

                // the bundlor ant task writes directly to standard out
                // redirect it to INFO level logging, which gradle will
                // deal with gracefully
                logging.captureStandardOutput(LogLevel.INFO)

                // the ant task will throw unless this dir exists
                if (!outputDir.isDirectory())
                    outputDir.mkdir()


                // execute the ant task, and write out the manifest file
                project.ant.bundlor(
                        enabled: enabled,
                        inputPath: project.sourceSets.main.output.classesDir,
                        outputPath: outputDir,
                        bundleVersion: bundleVersion, 
                        failOnWarnings: failOnWarnings) {
		            if (manifestTemplatePath != null) {
                        logger.info('Using explicit bundlor manifest template:')
			            manifestTemplate = project.file(manifestTemplatePath).text
                    } else if (manifestTemplate == null) {
                        assert bundleSymbolicName != null
                        assert bundleVendor != null
                        assert bundleName != null
                        manifestTemplate = """\
                            Bundle-Vendor: ${bundleVendor}
                            Bundle-Version: ${bundleVersion}
                            Bundle-Name: ${bundleName}
                            Bundle-ManifestVersion: ${bundleManifestVersion}
                            Bundle-SymbolicName: ${bundleSymbolicName}
                        """.stripIndent()
                        manifestTemplate += generateTemplateDirective(importTemplate,  "Import-Template")
                        manifestTemplate += generateTemplateDirective(exportTemplate,  "Export-Template")
                        manifestTemplate += generateTemplateDirective(excludedExports, "Excluded-Exports")
                        manifestTemplate += generateTemplateDirective(excludedImports, "Excluded-Imports")
                        
                        logger.info('Using generated bundlor manifest template:')
                    }
		            manifestTemplate(manifestTemplate)
                    logger.info('-------------------------------------------------')
	                logger.info(manifestTemplate)
        	        logger.info('-------------------------------------------------')
                }
            }
        }
    }

    def generateTemplateDirective(list, name) {
        def template = ""
        if (!list.isEmpty()) {
            template += "${name}: "
            list.each { entry ->
                template += "\n " + entry
                if (entry != list.last()) {
                    template += ','
                }
            }
            template += "\n"
        }
        return template
    }
}
