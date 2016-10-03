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

import org.eclipse.virgo.bundlor.gradle.internal.ManifestTemplateFactory
import org.junit.After
import org.junit.Before
import org.junit.Test

class StandardManifestTemplateFactoryTests {
	
	private static final String BUNDLE_SYMBOLIC_NAME = "Bundle-SymbolicName";
	private static final String BUNDLE_VERSION = "Bundle-Version";
	private static final String NAME = "test.symbolic.name";
	private static final String VERSION = "0.0.0";
	
	private ManifestTemplateFactory manifestTemplateFactory;
	
	@Before
	public void before() {
		manifestTemplateFactory = new StandardManifestTemplateFactory();
	}
	
	@After
	public void after() {
		manifestTemplateFactory = null;
	}
	
	@Test
	public void testNullCreate() {
		def template = manifestTemplateFactory.create(null, null, null, null, null, null)
		assertNotNull(template)
		assertEquals('1.0', template.getVersion())
		template.getMainAttributes().each { k,v ->
			assertNull(v)
		}
	}
	
	@Test
	public void manifestTemplatePath() {
		def template = manifestTemplateFactory.create("src/test/resources/test-template.mf", null, NAME, null, VERSION, null);
		assertNotNull(template.getMainAttributes().get(BUNDLE_SYMBOLIC_NAME));
		assertNotNull(template.getMainAttributes().get(BUNDLE_VERSION));
	}

	@Test
	public void manifestTemplate() {
		def template = manifestTemplateFactory.create(null, "Manifest-Version: 1.0", NAME, null, VERSION, null);
		assertNotNull(template.getMainAttributes().get(BUNDLE_SYMBOLIC_NAME));
		assertNotNull(template.getMainAttributes().get(BUNDLE_VERSION));
	}
	
	@Test
	public void manifestTemplateDefaults() {
		def template = manifestTemplateFactory.create(null, "Manifest-Version: 1.0", null, NAME, null, VERSION);
		assertNotNull(template.getMainAttributes().get(BUNDLE_SYMBOLIC_NAME));
		assertNotNull(template.getMainAttributes().get(BUNDLE_VERSION));
	}

	@Test
	public void manifestTemplateNull() {
		def template = manifestTemplateFactory.create(null, null, NAME, null, VERSION, null);
		assertNotNull(template.getMainAttributes().get(BUNDLE_SYMBOLIC_NAME));
		assertNotNull(template.getMainAttributes().get(BUNDLE_VERSION));
	}
}
