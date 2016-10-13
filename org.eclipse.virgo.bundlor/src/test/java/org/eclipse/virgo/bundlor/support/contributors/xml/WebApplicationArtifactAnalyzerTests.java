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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Set;

import org.junit.Test;

import org.eclipse.virgo.bundlor.support.contributors.xml.WebApplicationArtifactAnalyzer;
import org.eclipse.virgo.bundlor.support.partialmanifest.ReadablePartialManifest;
import org.eclipse.virgo.bundlor.support.partialmanifest.StandardReadablePartialManifest;

public class WebApplicationArtifactAnalyzerTests {

    private WebApplicationArtifactAnalyzer analyzer = new WebApplicationArtifactAnalyzer();

    @Test
    public void validForAnalyzation() {
        assertTrue("Invalid for standard web.xml file on nix system", analyzer.canAnalyse("WEB-INF/web.xml"));
        assertTrue("Invalid for standard web.xml file on win system", analyzer.canAnalyse("WEB-INF\\\\web.xml"));
    }

    @Test
    public void invalidForAnalyzation() {
        assertFalse("Invalid for web.xml in META-INF", analyzer.canAnalyse("META-INF/web.xml"));
        assertFalse("Invalid for web.xml in META-INF", analyzer.canAnalyse("META-INF\\\\web.xml"));
        assertFalse("Invalid for web.xml in root", analyzer.canAnalyse("web.xml"));
    }

    @Test
    public void contentsOnNixSystem() throws FileNotFoundException, Exception {
        ReadablePartialManifest partialManifest = new StandardReadablePartialManifest();
        analyzer.analyse(new FileInputStream("src/test/resources/org/eclipse/virgo/bundlor/support/contributors/xml/web.xml"), "WEB-INF/web.xml",
            partialManifest);

        Set<String> importedPackages = partialManifest.getImportedPackages();
        for (String expectedImportedPackage : new String[] { "alpha", "bravo", "charlie", "delta", "echo", "foxtrot", "golf", "hotel", "indigo",
            "juliet", "lima", "mike", "november", "oscar", "poppa" }) {
            assertTrue("Missing package '" + expectedImportedPackage + "'", importedPackages.contains(expectedImportedPackage));
        }
    }
    
    @Test
    public void contentsOnWinSystem() throws FileNotFoundException, Exception {
        ReadablePartialManifest partialManifest = new StandardReadablePartialManifest();
        analyzer.analyse(new FileInputStream("src/test/resources/org/eclipse/virgo/bundlor/support/contributors/xml/web.xml"), "WEB-INF\\\\web.xml",
            partialManifest);

        Set<String> importedPackages = partialManifest.getImportedPackages();
        for (String expectedImportedPackage : new String[] { "alpha", "bravo", "charlie", "delta", "echo", "foxtrot", "golf", "hotel", "indigo",
            "juliet", "lima", "mike", "november", "oscar", "poppa" }) {
            assertTrue("Missing package '" + expectedImportedPackage + "'", importedPackages.contains(expectedImportedPackage));
        }
    }
}
