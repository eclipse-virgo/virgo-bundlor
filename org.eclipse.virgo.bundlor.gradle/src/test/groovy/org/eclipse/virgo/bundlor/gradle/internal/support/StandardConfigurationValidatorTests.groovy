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
package org.eclipse.virgo.bundlor.gradle.internal.support

import static org.junit.Assert.*

import org.eclipse.virgo.bundlor.gradle.internal.Configuration
import org.eclipse.virgo.bundlor.gradle.internal.ConfigurationValidator
import org.gradle.api.GradleException
import org.junit.After
import org.junit.Before
import org.junit.Test

class StandardConfigurationValidatorTests {

	private static final String EMPTY_PATH = '';
	private static final String INEXISTENT_PATH = '/inexistent/path';
	private static final String DIR_PATH = System.getProperty('user.home');
	private static final String JAR_PATH = 'testpath/input.jar'
	private static final String WAR_PATH = 'testpath/input.war'
	private static final String TXT_PATH = 'testpath/input.txt'
	private static final String TEMPLATE_PATH = 'testpath/template.mf'
	private static final String PROFILE_PATH = 'testpath/osgi.profile'
	private static final String PROPERTIES_PATH = 'testpath/bundlor.properties'
	
	private ConfigurationValidator configurationValidator;
	
	@Before
	public void before() {
		configurationValidator = new StandardConfigurationValidator();
	}
	
	@After
	public void after() {
		configurationValidator = null;
	}
		
	@Test(expected=GradleException.class)
	public void testValidateWithNullConfiguration() {
		configurationValidator.validate(null)
	}
	
	@Test(expected=GradleException.class)
	public void testEmptyInputPath() {
		configurationValidator.inputPath(EMPTY_PATH)
	}
	
	@Test(expected=GradleException.class)
	public void testInvalidInputPath() {
		configurationValidator.inputPath(INEXISTENT_PATH)
	}
	
	@Test
	public void testDirectoryInputPath() {
		configurationValidator.inputPath(DIR_PATH)
	}
	
	@Test
	public void testJarInputPath() {
		configurationValidator.inputPath(getPath(JAR_PATH))
	}
	
	@Test
	public void testWarInputPath() {
		configurationValidator.inputPath(getPath(WAR_PATH))
	}
	
	@Test(expected=GradleException.class)
	public void testUnsupportedInputPath() {
		configurationValidator.inputPath(getPath(TXT_PATH))
	}
	
	@Test
	public void testEmptyOutputPath() {
		configurationValidator.outputPath(EMPTY_PATH)
	}
	
	@Test(expected=GradleException.class)
	public void testInvalidOutputPath() {
		configurationValidator.outputPath(INEXISTENT_PATH)
	}
	
	@Test
	public void testDirectoryOutputPath() {
		configurationValidator.outputPath(DIR_PATH)
	}
	
	@Test
	public void testJarOutputPath() {
		configurationValidator.outputPath(getPath(JAR_PATH))
	}
	
	@Test
	public void testWarOutputPath() {
		configurationValidator.outputPath(getPath(WAR_PATH))
	}
	
	@Test(expected=GradleException.class)
	public void testUnsupportedOutputPath() {
		configurationValidator.outputPath(getPath(TXT_PATH))
	}
	
	@Test
	public void testNullManifestTemplate() {
		configurationValidator.manifestTemplate(null, null)
	}
	
	@Test(expected=GradleException.class)
	public void testInexistentManifestTemplate() {
		configurationValidator.manifestTemplate(INEXISTENT_PATH, null)
	}
	
	@Test(expected=GradleException.class)
	public void testNoFileManifestTemplate() {
		configurationValidator.manifestTemplate(DIR_PATH, null)
	}
	
	@Test
	public void testManifestTemplate() {
		configurationValidator.manifestTemplate(getPath(TEMPLATE_PATH), null)
	}
	
	@Test
	public void testNullOsgiProfile() {
		configurationValidator.osgiProfile(null)
	}
	
	@Test(expected=GradleException.class)
	public void testInexistentOsgiProfile() {
		configurationValidator.osgiProfile(INEXISTENT_PATH)
	}
	
	@Test(expected=GradleException.class)
	public void testNoFileOsgiProfile() {
		configurationValidator.osgiProfile(DIR_PATH)
	}
	
	@Test
	public void testOsgiProfile() {
		configurationValidator.osgiProfile(getPath(PROFILE_PATH))
	}
	
	@Test
	public void testNullProperties() {
		configurationValidator.properties(null)
	}
	
	@Test(expected=GradleException.class)
	public void testInexistentProperties() {
		configurationValidator.properties(INEXISTENT_PATH)
	}
	
	@Test(expected=GradleException.class)
	public void testNoFileProperties() {
		configurationValidator.properties(DIR_PATH)
	}
	
	@Test
	public void testProperties() {
		configurationValidator.properties(getPath(PROPERTIES_PATH))
	}
	
	private String getPath(String resource) {
		return getClass().getClassLoader().getResource(resource).path
	}
}
