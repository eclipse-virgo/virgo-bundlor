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

import static org.junit.Assert.assertEquals

import org.eclipse.virgo.bundlor.support.properties.PropertiesSource
import org.junit.Test

class StandardPropertiesSourceFactoryTests {

	private final StandardPropertiesSourceFactory factory = new StandardPropertiesSourceFactory()

	@Test
	public void create() {
		List<PropertiesSource> properties = this.factory.create("src/test/resources/testpath/bundlor.properties",
				Collections.<Properties> emptyList())
		assertEquals(2, properties.size())
	}

	@Test
	public void propertiesPathNull() {
		List<PropertiesSource> properties = this.factory.create(null, Collections.<Properties> emptyList())
		assertEquals(1, properties.size())
	}
}
