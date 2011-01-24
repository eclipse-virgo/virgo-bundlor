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

package org.eclipse.virgo.bundlor.support.contributors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.junit.Test;

import org.eclipse.virgo.bundlor.util.SimpleManifestContents;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

public class OsgiProfileManifestTemplateModifierTests {

    private static final String BOOTDELEGATION = "org.osgi.framework.bootdelegation";

    private static final String SYSTEM_PACKAGES = "org.osgi.framework.system.packages";

    private static final String EXCLUDED_IMPORTS = "Excluded-Imports";

    private static final String IMPORT_TEMPLATE = "Import-Template";

    @Test
    public void testNoInputProperties() {
        OsgiProfileManifestTemplateModifier modifier = new OsgiProfileManifestTemplateModifier(new Properties());

        ManifestContents manifestTemplate = new SimpleManifestContents();
        modifier.modify(manifestTemplate);

        assertNull(manifestTemplate.getMainAttributes().get(IMPORT_TEMPLATE));
        assertNull(manifestTemplate.getMainAttributes().get(EXCLUDED_IMPORTS));
    }

    @Test
    public void testNoExistingHeaders() {
        Properties properties = new Properties();
        properties.put(SYSTEM_PACKAGES, "java.lang");
        properties.put(BOOTDELEGATION, "com.sun.*");
        OsgiProfileManifestTemplateModifier modifier = new OsgiProfileManifestTemplateModifier(properties);

        ManifestContents manifestTemplate = new SimpleManifestContents();
        modifier.modify(manifestTemplate);

        assertNotNull(manifestTemplate.getMainAttributes().get(IMPORT_TEMPLATE));
        assertNotNull(manifestTemplate.getMainAttributes().get(EXCLUDED_IMPORTS));
    }

    @Test
    public void testExistingHeaders() {
        Properties properties = new Properties();
        properties.put(SYSTEM_PACKAGES, "java.lang");
        properties.put(BOOTDELEGATION, "com.sun.*");
        OsgiProfileManifestTemplateModifier modifier = new OsgiProfileManifestTemplateModifier(properties);

        ManifestContents manifestTemplate = new SimpleManifestContents();
        manifestTemplate.getMainAttributes().put(IMPORT_TEMPLATE, "org.springframework.*;resolution:=optional");
        manifestTemplate.getMainAttributes().put(EXCLUDED_IMPORTS, "org.hibernate.*");
        modifier.modify(manifestTemplate);

        assertContains((String) manifestTemplate.getMainAttributes().get(IMPORT_TEMPLATE), "java.lang",
            "org.springframework.*;resolution:=\"optional\"");
        assertContains((String) manifestTemplate.getMainAttributes().get(EXCLUDED_IMPORTS), "com.sun.*", "org.hibernate.*");
        assertNotNull(manifestTemplate.getMainAttributes().get(EXCLUDED_IMPORTS));
    }

    @Test
    public void testImportAtZero() {
        Properties properties = new Properties();
        properties.put(SYSTEM_PACKAGES, "java.lang");
        OsgiProfileManifestTemplateModifier modifier = new OsgiProfileManifestTemplateModifier(properties);

        ManifestContents manifestTemplate = new SimpleManifestContents();
        modifier.modify(manifestTemplate);

        assertContains((String) manifestTemplate.getMainAttributes().get(IMPORT_TEMPLATE), "java.lang;version=\"0\"");
    }

    private void assertContains(String s, String... candidates) {
        for (String candidate : candidates) {
            assertTrue("Could not find '" + candidate + "' in '" + s + "'", s.contains(candidate));
        }
    }
}
