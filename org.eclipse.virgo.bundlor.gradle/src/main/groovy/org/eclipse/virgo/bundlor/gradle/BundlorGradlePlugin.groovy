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
	
	static final String BUNDLOR = 'bundlor'
	static final String JAVA_PLUGIN = 'java'

	@Override
	def void apply(Project project) {
		applyJavaPluginIfRequired(project)
		defineBundlorTask(project)
	}

	def applyJavaPluginIfRequired(Project project) {
		if(!project.plugins.findPlugin(JAVA_PLUGIN)) {
			project.plugins.apply(JAVA_PLUGIN)
		}
	}

	def defineBundlorTask(Project project) {
		def bundlorTask = project.tasks.create(BUNDLOR, BundlorTask.class)
		bundlorTask.dependsOn project.compileJava
		project.tasks.jar {
			dependsOn bundlorTask
			inputs.files bundlorTask.manifest
			manifest.from bundlorTask.manifest
		}
	}
}
