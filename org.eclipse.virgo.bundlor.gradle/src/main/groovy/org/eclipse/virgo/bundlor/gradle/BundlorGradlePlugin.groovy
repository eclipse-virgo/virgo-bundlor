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
package org.eclipse.virgo.bundlor.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Contribute a 'bundlor' task capable of creating an OSGi manifest. Task is tied
 * to the lifecycle by having the 'jar' task depend on 'bundlor'.
 *
 * @author Chris Beams
 * @author Luke Taylor
 * @author Daniel Marthaler
 */
public class BundlorGradlePlugin implements Plugin<Project> {

	public void apply(Project project) {
		project.extensions.create("bundlor", BundlorGradlePluginExtension)
		project.bundlor.with {
			inputPath = project.sourceSets.main.output.classesDir
			outputDir = new File("${project.buildDir}/bundlor")
			propertiesPath = project.rootProject.file('gradle.properties')
			bundleVersion = project.version
			bundleVendor = 'Eclipse Virgo Project'
		}

		project.task('bundlor', type: DefaultTask, dependsOn: 'compileJava') {
			group = 'Build'
			description = 'Generates an OSGi-compatible MANIFEST.MF file.'

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

			}
		}
	}
}
