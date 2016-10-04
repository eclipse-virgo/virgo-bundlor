/*
 * This file is part of the Eclipse Virgo project.
 *
 * Copyright (c) 2016 ISPIN AG
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Daniel Marthaler - initial contribution
 */
package org.eclipse.virgo.bundlor.gradle.internal

import static org.junit.Assert.*
import static org.junit.Assume.*

import org.eclipse.virgo.bundlor.gradle.BundlorGradlePlugin
import org.eclipse.virgo.bundlor.gradle.BundlorTask
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class BundlorTaskTests {
	
	private static final String NAME = 'BundlorTaskTestsProject'
	private static final String BUNDLOR = 'bundlor'
	
	private Project project
	
	@Rule
	public TemporaryFolder projectDir = new TemporaryFolder()
	
	@Before
	public void before() {
		project = ProjectBuilder.builder().withName(NAME).withProjectDir(projectDir.newFolder(NAME)).build()
		project.version = '9.9.9'
		project.plugins.apply(BundlorGradlePlugin.class)
		new File(project.buildDir.absolutePath + '/classes/main').mkdirs()
	}
	
	@After
	public void after() {
		project = null
	}
	
	@Test
	public void testBundlorTaskBasics() {
		def bundlorTask = project.tasks.bundlor
		assumeTrue(bundlorTask instanceof BundlorTask)				
		
		assertNotNull(bundlorTask.group)
		assertNotNull(bundlorTask.description)
	}
	
	@Test
	public void testDisabledBundlorTaskExecution() {		
		def bundlorTask = project.tasks.bundlor
		assumeTrue(bundlorTask instanceof BundlorTask)				
		
		project.bundlor {
			enabled = false
		}
		
		bundlorTask.execute()
	}
	
	@Test
	public void testDoNotFailOnWarningsBundlorTaskExecution() {
		def bundlorTask = project.tasks.bundlor
		assumeTrue(bundlorTask instanceof BundlorTask)
		
		project.bundlor {
			failOnWarnings = false
		}
		
		bundlorTask.execute()
		
		assertTrue(bundlorTask.didWork)
	}
	
	@Test
	public void testBundlorTaskExecution() {
		def bundlorTask = project.tasks.bundlor
		assumeTrue(bundlorTask instanceof BundlorTask)
		
		bundlorTask.execute()
		
		assertTrue(bundlorTask.didWork)
	}
}
