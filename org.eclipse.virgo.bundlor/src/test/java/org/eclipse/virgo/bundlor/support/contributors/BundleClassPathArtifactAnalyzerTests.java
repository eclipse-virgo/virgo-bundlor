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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Set;

import org.junit.Test;
import org.osgi.framework.Constants;

import org.eclipse.virgo.bundlor.support.ArtifactAnalyzer;
import org.eclipse.virgo.bundlor.support.partialmanifest.StandardReadablePartialManifest;
import org.eclipse.virgo.bundlor.util.SimpleManifestContents;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

public class BundleClassPathArtifactAnalyzerTests {

    private final BundleClassPathArtifactAnalyzer analyzer;

    public BundleClassPathArtifactAnalyzerTests() {
        this.analyzer = new BundleClassPathArtifactAnalyzer(Arrays.<ArtifactAnalyzer> asList(new StaticResourceArtifactAnalyzer()));
    }

    @Test
    public void canAnalyze() {
        assertTrue(this.analyzer.canAnalyse("test.jar"));
        assertFalse(this.analyzer.canAnalyse("test.properties"));
    }

    @Test
    public void analyze() throws FileNotFoundException, Exception {
        StandardReadablePartialManifest partialManifest = new StandardReadablePartialManifest();
        this.analyzer.analyse(new FileInputStream("src/test/resources/org/eclipse/virgo/bundlor/support/contributors/test.jar"), "test.jar",
            partialManifest);
        partialManifest.condense();
        Set<String> exportedPackages = partialManifest.getExportedPackages();
        assertEquals(1, exportedPackages.size());
        assertTrue(exportedPackages.contains("com.springsource.test"));

        ManifestContents manifest = new SimpleManifestContents();
        this.analyzer.contribute(manifest);
        String entry = manifest.getMainAttributes().get(Constants.BUNDLE_CLASSPATH);
        assertNotNull(entry);
        assertEquals(".,test.jar", entry);
    }
    
    @Test
    public void analyzeOnWindows() throws FileNotFoundException, Exception {
        StandardReadablePartialManifest partialManifest = new StandardReadablePartialManifest();
        this.analyzer.analyse(new FileInputStream("src/test/resources/org/eclipse/virgo/bundlor/support/contributors/test.jar"), "lib\\test.jar",
            partialManifest);
        partialManifest.condense();
        Set<String> exportedPackages = partialManifest.getExportedPackages();
        assertEquals(1, exportedPackages.size());
        assertTrue(exportedPackages.contains("com.springsource.test"));

        ManifestContents manifest = new SimpleManifestContents();
        this.analyzer.contribute(manifest);
        String entry = manifest.getMainAttributes().get(Constants.BUNDLE_CLASSPATH);
        assertNotNull(entry);
        assertEquals(".,lib/test.jar", entry);
    }
}
