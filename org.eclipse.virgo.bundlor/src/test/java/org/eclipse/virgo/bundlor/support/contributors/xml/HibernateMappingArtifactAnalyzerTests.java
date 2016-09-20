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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import org.eclipse.virgo.bundlor.support.partialmanifest.ReadablePartialManifest;
import org.eclipse.virgo.bundlor.support.partialmanifest.StandardReadablePartialManifest;

public class HibernateMappingArtifactAnalyzerTests {

    private static final String artifactName = "src/test/resources/org/eclipse/virgo/bundlor/support/contributors/xml/hibernate.hbm";

    private final HibernateMappingArtifactAnalyzer analyzer = new HibernateMappingArtifactAnalyzer();

    @Test
    public void testCanAnalyze() {
        assertTrue(analyzer.canAnalyse(artifactName));
        assertFalse(analyzer.canAnalyse(artifactName + "1"));
    }

    @Ignore("Ignoring because XSD resolution runs into proxy issue on hudson build")
    @Test
    public void testAnalyze() throws Exception {
        ReadablePartialManifest partialManifest = new StandardReadablePartialManifest();
        InputStream artifact = new FileInputStream(artifactName);

        analyzer.analyse(artifact, artifactName, partialManifest);
        assertContains(partialManifest.getImportedPackages(), "alpaca", "bird", "cat", "dog", "emu", "frog", "giraffe", "hippopotamus", "iguana",
            "jaguar", "kangaroo", "lemur", "manatee", "newt", "ocelot", "porcupine", "quoll", "rhinoceros");
    }

    private void assertContains(Set<String> experimentalPackages, String... expectedPackages) {
        assertEquals(expectedPackages.length, experimentalPackages.size());
        for (String expectedPackage : expectedPackages) {
            assertTrue("Does not contain " + expectedPackage, experimentalPackages.contains(expectedPackage));
        }
    }
}
