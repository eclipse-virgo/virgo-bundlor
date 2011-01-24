/*******************************************************************************
 * Copyright (c) 2008, 2010 VMware Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   VMware Inc. - initial contribution
 *******************************************************************************/

package org.eclipse.virgo.bundlor.maven.plugin.internal.support;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.eclipse.virgo.bundlor.support.properties.EmptyPropertiesSource;
import org.eclipse.virgo.bundlor.support.properties.FileSystemPropertiesSource;
import org.eclipse.virgo.bundlor.support.properties.PropertiesSource;
import org.eclipse.virgo.bundlor.support.properties.StringPropertiesSource;

public class StandardOsgiProfileFactoryTests {

    private final StandardOsgiProfileFactory factory = new StandardOsgiProfileFactory();

    @Test
    public void profileString() {
        PropertiesSource propertiesSource = this.factory.create(null, "test.property=test.value");
        assertTrue(propertiesSource instanceof StringPropertiesSource);
    }

    @Test
    public void profilePath() {
        PropertiesSource propertiesSource = this.factory.create("src/test/resources/test.properties", null);
        assertTrue(propertiesSource instanceof FileSystemPropertiesSource);
    }

    @Test
    public void profileNull() {
        PropertiesSource propertiesSource = this.factory.create(null, null);
        assertTrue(propertiesSource instanceof EmptyPropertiesSource);
    }
}
