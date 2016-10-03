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

import static org.gradle.testkit.runner.TaskOutcome.*
import static org.junit.Assert.*

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Before
import org.junit.Test

class BundlorGradlePluginTests {
	
	public File testProjectDir
	public List pluginClasspath

	@Before
	public void before() {
		pluginClasspath = getClass().classLoader.findResource('plugin-classpath.txt').readLines().collect { 
			println it
			new File(it) 
		}
	}
	
	@Test
	public void test() {
//		testProjectDir = new File('src/test/resources/simpleproject')
//		
//		BuildResult result = GradleRunner.create()
//			.withProjectDir(testProjectDir)
//			.withArguments("tasks", "--stacktrace")
//			.withPluginClasspath(pluginClasspath)
//			.build()
//
//		assertEquals(result.task(":tasks").getOutcome(), SUCCESS)
	}
}
