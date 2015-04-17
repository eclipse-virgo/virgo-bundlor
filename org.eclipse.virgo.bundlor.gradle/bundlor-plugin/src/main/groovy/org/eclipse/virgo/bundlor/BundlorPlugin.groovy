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
import org.gradle.api.DefaultTask

//import org.gradle.api.plugins.JavaPlugin
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
        project.extensions.create("bundlor", BundlorPluginExtension)
        project.bundlor.with {
            inputPath = project.sourceSets.main.output.classesDir
            outputDir = new File("${project.buildDir}/bundlor")
            propertiesPath = project.rootProject.file('gradle.properties')
            bundleVersion = project.version
            bundleVendor = 'Eclipse Virgo Project'
            bundlorVersion = '1.1.2.RELEASE'
        }

        project.configurations { bundlorconf }

        project.repositories.maven( {name = 'bundlor'; url = 'http://build.eclipse.org/rt/virgo/maven/bundles/release'})
        project.repositories.maven( {name = 'springsource'; url = 'http://repository.springsource.com/maven/bundles/external'})

        project.dependencies {
            bundlorconf group: 'org.eclipse.virgo.bundlor', name: 'org.eclipse.virgo.bundlor', version: project.bundlor.bundlorVersion, configuration: 'runtime'
            bundlorconf group: 'org.eclipse.virgo.bundlor', name: 'org.eclipse.virgo.bundlor.ant', version: project.bundlor.bundlorVersion, configuration: 'runtime'
            bundlorconf group: 'org.eclipse.virgo.bundlor', name: 'org.eclipse.virgo.bundlor.blint', version: project.bundlor.bundlorVersion, configuration: 'runtime'
        }

        project.task('bundlor', type: DefaultTask, dependsOn: 'compileJava') {
            group = 'Build'
            description = 'Generates an OSGi-compatibile MANIFEST.MF file.'

            def manifest = new File("${project.bundlor.outputDir}/META-INF/MANIFEST.MF")

            // inform gradle what directory this task writes so that
            // it can be removed when issuing `gradle cleanBundlor`
            outputs.dir project.bundlor.outputDir

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
                if (project.bundlor.bundleName == null)
                    project.bundlor.bundleName = project.description

                project.ant.taskdef(
                        resource: 'org/eclipse/virgo/bundlor/ant/antlib.xml',
                        classpath: project.configurations.bundlorconf.asPath)

                // the bundlor ant task writes directly to standard out
                // redirect it to INFO level logging, which gradle will
                // deal with gracefully
                logging.captureStandardOutput(LogLevel.INFO)

                // the ant task will throw unless this dir exists
                if (!project.bundlor.outputDir.isDirectory())
                    project.bundlor.outputDir.mkdir()

                // execute the ant task, and write out the manifest file
                project.ant.bundlor(
                        enabled: project.bundlor.enabled,
                        inputPath: project.bundlor.inputPath,
                        propertiesPath: project.bundlor.propertiesPath,
                        outputPath: project.bundlor.outputDir,
                        bundleVersion: project.bundlor.bundleVersion,
                        failOnWarnings: project.bundlor.failOnWarnings) {
                            if (project.bundlor.manifestTemplatePath != null) {
                                logger.info('Using explicit bundlor manifest template:')
                                project.bundlor.manifestTemplate = project.file(project.bundlor.manifestTemplatePath).text
                            } else if (project.bundlor.manifestTemplate == null) {
                                assert project.bundlor.bundleSymbolicName != null
                                assert project.bundlor.bundleVendor != null
                                assert project.bundlor.bundleName != null
                                project.bundlor.manifestTemplate = """\
                                            Bundle-Vendor: ${project.bundlor.bundleVendor}
                                            Bundle-Version: ${project.bundlor.bundleVersion}
                                            Bundle-Name: ${project.bundlor.bundleName}
                                            Bundle-ManifestVersion: ${project.bundlor.bundleManifestVersion}
                                            Bundle-SymbolicName: ${project.bundlor.bundleSymbolicName}
                                        """.stripIndent()
                                project.bundlor.manifestTemplate += generateTemplateDirective(project.bundlor.importTemplate,  "Import-Template")
                                project.bundlor.manifestTemplate += generateTemplateDirective(project.bundlor.exportTemplate,  "Export-Template")
                                project.bundlor.manifestTemplate += generateTemplateDirective(project.bundlor.excludedExports, "Excluded-Exports")
                                project.bundlor.manifestTemplate += generateTemplateDirective(project.bundlor.excludedImports, "Excluded-Imports")

                                logger.info('Using generated bundlor manifest template:')
                            }
                            manifestTemplate(project.bundlor.manifestTemplate)
                            logger.info('-------------------------------------------------')
                            logger.info(project.bundlor.manifestTemplate)
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
