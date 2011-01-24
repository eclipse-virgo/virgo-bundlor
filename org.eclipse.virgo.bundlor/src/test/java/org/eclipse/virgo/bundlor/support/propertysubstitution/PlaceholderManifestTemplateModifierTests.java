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

package org.eclipse.virgo.bundlor.support.propertysubstitution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.eclipse.virgo.bundlor.util.StringUtils;

import org.eclipse.virgo.bundlor.util.BundleManifestUtils;
import org.eclipse.virgo.bundlor.util.SimpleManifestContents;
import org.eclipse.virgo.bundlor.util.SimpleParserLogger;
import org.eclipse.virgo.util.osgi.manifest.BundleManifest;
import org.eclipse.virgo.util.osgi.manifest.parse.HeaderDeclaration;
import org.eclipse.virgo.util.osgi.manifest.parse.HeaderParser;
import org.eclipse.virgo.util.osgi.manifest.parse.HeaderParserFactory;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

public class PlaceholderManifestTemplateModifierTests {

    private PlaceholderManifestAndTemplateModifier modifier;

    @Before
    public void createModifier() {
        Properties properties = new Properties();
        properties.put("com.springsource.version", "1.2.3.qualifer");
        modifier = new PlaceholderManifestAndTemplateModifier(properties);
    }

    @Test
    public void basicVersionExpansion() {
        ManifestContents manifestTemplate = new SimpleManifestContents();
        manifestTemplate.getMainAttributes().put("Import-Template", "com.foo;version=\"${com.springsource.version:[=.=,+1)}\"");

        modifier.modify(manifestTemplate);
        assertImportTemplate("com.foo", "[1.2,2)", manifestTemplate);
    }

    @Test
    public void patternVersionExpansion() {
        ManifestContents manifestTemplate = new SimpleManifestContents();
        manifestTemplate.getMainAttributes().put("Import-Template", "com.foo;version=\"${com.springsource.version:sample}\"");
        manifestTemplate.getMainAttributes().put("Version-Patterns", "sample;pattern=\"[=.=,+1)\"");

        modifier.modify(manifestTemplate);
        assertImportTemplate("com.foo", "[1.2,2)", manifestTemplate);
    }

    @Test
    public void testPlaceholderReplacement() {
        System.getProperties().put("domain.name", "org.springframework");
        PlaceholderManifestAndTemplateModifier modifier = new PlaceholderManifestAndTemplateModifier(System.getProperties());

        ManifestContents manifestTemplate = new SimpleManifestContents();
        manifestTemplate.getMainAttributes().put("Bundle-SymbolicName", "${domain.name}.core.${user.name}");
        manifestTemplate.getMainAttributes().put("Bundle-Version", "2.5.1");

        modifier.modify(manifestTemplate);
        BundleManifest manifest = BundleManifestUtils.createBundleManifest(manifestTemplate);
        assertEquals("org.springframework.core." + System.getProperty("user.name"), manifest.getBundleSymbolicName().getSymbolicName());
    }

    private void assertImportTemplate(String packageName, String version, ManifestContents manifestTemplate) {
        BundleManifest manifest = BundleManifestUtils.createBundleManifest(manifestTemplate);
        List<HeaderDeclaration> headers = parseTemplate(manifest.getHeader("Import-Template"));
        boolean found = false;
        for (HeaderDeclaration header : headers) {
            if (header.getNames().get(0).equals(packageName) && header.getAttributes().get("version").equals(version)) {
                found = true;
                break;
            }
        }

        assertTrue("Did not find Import-Template entry for " + packageName + " - " + version, found);
    }

    private List<HeaderDeclaration> parseTemplate(String template) {
        if (StringUtils.hasText(template)) {
            HeaderParser parser = HeaderParserFactory.newHeaderParser(new SimpleParserLogger());
            return parser.parseHeader(template);
        }
        return new ArrayList<HeaderDeclaration>(0);
    }
}
