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
import java.io.FileNotFoundException;
import java.util.Set;

import org.junit.Test;

import org.eclipse.virgo.bundlor.support.partialmanifest.ReadablePartialManifest;
import org.eclipse.virgo.bundlor.support.partialmanifest.StandardReadablePartialManifest;

public class Log4JXmlArtifactAnalyzerTests {

    private final Log4JXmlArtifactAnalyzer analyzer = new Log4JXmlArtifactAnalyzer();

    @Test
    public void canAnalyze() {
        assertTrue(analyzer.canAnalyse("log4j.xml"));
        assertFalse(analyzer.canAnalyse("META-INF/log4j.xml"));
    }

    @Test
    public void example1() throws FileNotFoundException, Exception {
        ReadablePartialManifest partialManifest = new StandardReadablePartialManifest();
        FileInputStream artifact = new FileInputStream("src/test/resources/org/eclipse/virgo/bundlor/support/contributors/xml/log4j-1.xml");
		analyzer.analyse(artifact, null, partialManifest);

        Set<String> importedPackages = partialManifest.getImportedPackages();
        assertEquals(1, importedPackages.size());
        assertTrue(importedPackages.contains("org.apache.log4j"));
    }

    @Test
    public void example2() throws FileNotFoundException, Exception {
        ReadablePartialManifest partialManifest = new StandardReadablePartialManifest();
        analyzer.analyse(new FileInputStream("src/test/resources/org/eclipse/virgo/bundlor/support/contributors/xml/log4j-2.xml"), null, partialManifest);

        Set<String> importedPackages = partialManifest.getImportedPackages();
        assertEquals(1, importedPackages.size());
        assertTrue(importedPackages.contains("org.apache.log4j"));
    }

    @Test
    public void example3() throws FileNotFoundException, Exception {
        ReadablePartialManifest partialManifest = new StandardReadablePartialManifest();
        analyzer.analyse(new FileInputStream("src/test/resources/org/eclipse/virgo/bundlor/support/contributors/xml/log4j-3.xml"), null, partialManifest);

        Set<String> importedPackages = partialManifest.getImportedPackages();
        assertEquals(1, importedPackages.size());
        assertTrue(importedPackages.contains("org.apache.log4j"));
    }

    @Test
    public void example4() throws FileNotFoundException, Exception {
        ReadablePartialManifest partialManifest = new StandardReadablePartialManifest();
        analyzer.analyse(new FileInputStream("src/test/resources/org/eclipse/virgo/bundlor/support/contributors/xml/log4j-4.xml"), null, partialManifest);

        Set<String> importedPackages = partialManifest.getImportedPackages();
        assertEquals(2, importedPackages.size());
        assertTrue(importedPackages.contains("org.apache.log4j"));
        assertTrue(importedPackages.contains("org.apache.log4j.rolling"));
    }
}
