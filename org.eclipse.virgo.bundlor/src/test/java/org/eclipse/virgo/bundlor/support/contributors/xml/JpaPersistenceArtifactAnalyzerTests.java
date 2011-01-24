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
import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Set;

import org.junit.Test;

import org.eclipse.virgo.bundlor.support.partialmanifest.ReadablePartialManifest;
import org.eclipse.virgo.bundlor.support.partialmanifest.StandardReadablePartialManifest;

public class JpaPersistenceArtifactAnalyzerTests {

    private final JpaPersistenceArtifactAnalyzer analyzer = new JpaPersistenceArtifactAnalyzer();

    @Test
    public void canAnalyze() {
        assertTrue(analyzer.canAnalyse("META-INF/orm.xml"));
        assertTrue(analyzer.canAnalyse("META-INF/persistence.xml"));
        assertFalse(analyzer.canAnalyse("xml/persistence.xml"));
    }

    @Test
    public void persistenceXml() throws Exception {
        ReadablePartialManifest partialManifest = new StandardReadablePartialManifest();
        InputStream artifact = new FileInputStream("src/test/resources/org/eclipse/virgo/bundlor/support/contributors/xml/persistence.xml");

        analyzer.analyse(artifact, "META-INF/persistence.xml", partialManifest);
        Set<String> imports = partialManifest.getImportedPackages();

        assertTrue(imports.contains("com.springsource.provider"));
        assertTrue(imports.contains("com.springsource.class1"));
        assertTrue(imports.contains("com.springsource.class2"));
    }

    @Test
    public void ormXml() throws Exception {
        ReadablePartialManifest partialManifest = new StandardReadablePartialManifest();
        InputStream artifact = new FileInputStream("src/test/resources/org/eclipse/virgo/bundlor/support/contributors/xml/orm.xml");

        analyzer.analyse(artifact, "META-INF/orm.xml", partialManifest);
        assertContains(partialManifest.getImportedPackages(), "alpaca", "bird", "cat", "dog", "emu", "frog", "giraffe", "hippopotamus", "iguana",
            "jaguar", "kangaroo", "lemur", "manatee", "newt");
    }

    private void assertContains(Set<String> experimentalPackages, String... expectedPackages) {
        assertEquals(expectedPackages.length, experimentalPackages.size());
        for (String expectedPackage : expectedPackages) {
            assertTrue("Does not contain " + expectedPackage, experimentalPackages.contains(expectedPackage));
        }
    }

}
