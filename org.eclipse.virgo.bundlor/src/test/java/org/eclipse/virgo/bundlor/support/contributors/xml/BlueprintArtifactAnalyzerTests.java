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

package org.eclipse.virgo.bundlor.support.contributors.xml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Set;

import org.junit.Test;

import org.eclipse.virgo.bundlor.support.partialmanifest.StandardReadablePartialManifest;
import org.eclipse.virgo.bundlor.util.SimpleManifestContents;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

public class BlueprintArtifactAnalyzerTests {

    private final BlueprintArtifactAnalyzer analyzer = new BlueprintArtifactAnalyzer();

    @Test
    public void analyze() throws Exception {
        InputStream in = null;
        try {
            in = new FileInputStream("src/test/resources/org/eclipse/virgo/bundlor/support/contributors/xml/blueprint-1.xml");
            StandardReadablePartialManifest manifest = new StandardReadablePartialManifest();
            this.analyzer.analyse(in, "test-artifact", manifest);

            assertContains("beans1", manifest.getImportedPackages());
            assertContains("beans2", manifest.getImportedPackages());
            assertContains("service1", manifest.getImportedPackages());
            assertContains("service2", manifest.getImportedPackages());
            assertContains("reference1", manifest.getImportedPackages());
            assertContains("reference2", manifest.getImportedPackages());
            assertContains("referenceList1", manifest.getImportedPackages());
            assertContains("referenceList2", manifest.getImportedPackages());
            assertContains("interfaces1", manifest.getImportedPackages());
            assertContains("interfaces2", manifest.getImportedPackages());
        } finally {
            if (in != null) {
                in.close();
            }
        }

    }

    @Test
    public void defaultContextLocation() {
        this.analyzer.readJarManifest(new SimpleManifestContents());
        assertTrue(this.analyzer.canAnalyse("OSGI-INF/blueprint/test.xml"));
        assertFalse(this.analyzer.canAnalyse("META-INF/test.xml"));
    }

    @Test
    public void customContextLocation() {
        ManifestContents manifest = new SimpleManifestContents();
        manifest.getMainAttributes().put("Bundle-Blueprint", "META-INF/test.xml");
        this.analyzer.readJarManifest(manifest);

        assertTrue(this.analyzer.canAnalyse("META-INF/test.xml"));
        assertFalse(this.analyzer.canAnalyse("OSGI-INF/blueprint/test.xml"));
    }

    private void assertContains(String expected, Set<String> importedPackages) {
        for (String importedPackage : importedPackages) {
            if (expected.equals(importedPackage)) {
                return;
            }
        }
        fail("Expected imported packages to contain " + expected);
    }

}
