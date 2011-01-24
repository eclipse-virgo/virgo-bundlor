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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import org.eclipse.virgo.bundlor.util.SimpleManifestContents;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

public class IgnoredExistingHeadersManifestModifierTests {

    private IgnoredExistingHeadersManifestModifier modifier = new IgnoredExistingHeadersManifestModifier();

    @Test
    public void excludedExistingHeaders() {
        ManifestContents manifestTemplate = new SimpleManifestContents();
        manifestTemplate.getMainAttributes().put("Ignored-Existing-Headers", "Import-Package,Export-Package");
        modifier.readManifestTemplate(manifestTemplate);

        ManifestContents artefact = new SimpleManifestContents();
        artefact.getMainAttributes().put("Import-Package", "com.ridiculous.import");
        artefact.getMainAttributes().put("Export-Package", "com.ridiculous.export");

        modifier.modify(artefact);
        assertNull(artefact.getMainAttributes().get("Import-Package"));
        assertNull(artefact.getMainAttributes().get("Export-Package"));
    }

    @Test
    public void names() {
        List<String> names = this.modifier.getTemplateOnlyHeaderNames();
        assertEquals(1, names.size());
        assertTrue(names.contains("Ignored-Existing-Headers"));
    }

    @Test
    public void wildcardExcludedExistingHeaders() {
        ManifestContents manifestTemplate = new SimpleManifestContents();
        manifestTemplate.getMainAttributes().put("Ignored-Existing-Headers", "Alpha-*,*-Bravo,Charlie-*-Delta,*-Echo-*");
        modifier.readManifestTemplate(manifestTemplate);

        ManifestContents artefact = new SimpleManifestContents();
        artefact.getMainAttributes().put("Alpha-Test", "com.ridiculous.import");
        artefact.getMainAttributes().put("Test-Bravo", "com.ridiculous.export");
        artefact.getMainAttributes().put("Charlie-Test-Delta", "com.ridiculous.export");
        artefact.getMainAttributes().put("Test-Echo-Test", "com.ridiculous.export");
        artefact.getMainAttributes().put("Import-Package", "com.ridiculous.export");

        modifier.modify(artefact);
        assertNull(artefact.getMainAttributes().get("Alpha-Test"));
        assertNull(artefact.getMainAttributes().get("Test-Bravo"));
        assertNull(artefact.getMainAttributes().get("Charlie-Test-Delta"));
        assertNull(artefact.getMainAttributes().get("Test-Echo-Test"));
        assertNotNull(artefact.getMainAttributes().get("Import-Package"));        
    }
}
