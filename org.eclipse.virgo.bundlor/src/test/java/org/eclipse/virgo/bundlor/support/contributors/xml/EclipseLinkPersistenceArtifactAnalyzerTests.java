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

public class EclipseLinkPersistenceArtifactAnalyzerTests {

    private final EclipseLinkPersistenceArtifactAnalyzer analyzer = new EclipseLinkPersistenceArtifactAnalyzer();

    @Test
    public void canAnalyze() {
        assertTrue(analyzer.canAnalyse("META-INF/eclipselink-orm.xml"));
        assertFalse(analyzer.canAnalyse("xml/persistence.xml"));
    }

    @Test
    @Ignore
    // TODO - investigate test failure in GitHub action
    public void ormXml() throws Exception {
        ReadablePartialManifest partialManifest = new StandardReadablePartialManifest();
        InputStream artifact = new FileInputStream("src/test/resources/org/eclipse/virgo/bundlor/support/contributors/xml/eclipselink-orm.xml");

        analyzer.analyse(artifact, "META-INF/orm.xml", partialManifest);
        assertContains(partialManifest.getImportedPackages(), "alpaca", "bird", "cat", "dog", "emu", "frog", "giraffe", "hippopotamus", "iguana",
            "jaguar", "kangaroo", "lemur", "manatee", "newt", "ocelot", "porcupine", "quoll", "rhinoceros", "snail", "tapir", "urchin", "vervet",
            "wombat", "xerus", "yak", "zebra", "aardvark", "bison", "caracal", "dolphin", "eel", "fox", "gazelle", "hummingbird", "ibex", "jellyfish",
            "kiwi");
    }

    private void assertContains(Set<String> experimentalPackages, String... expectedPackages) {
        assertEquals(expectedPackages.length, experimentalPackages.size());
        for (String expectedPackage : expectedPackages) {
            assertTrue("Does not contain " + expectedPackage, experimentalPackages.contains(expectedPackage));
        }
    }

}
