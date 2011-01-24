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
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import org.eclipse.virgo.bundlor.support.partialmanifest.ReadablePartialManifest;
import org.eclipse.virgo.bundlor.support.partialmanifest.StandardReadablePartialManifest;

public class StaticResourceArtifactAnalyzerTests {

    private final StaticResourceArtifactAnalyzer analyzer = new StaticResourceArtifactAnalyzer();

    @Test
    public void analyze() throws Exception {
        ReadablePartialManifest partialManifest = new StandardReadablePartialManifest();

        this.analyzer.analyse(null, "com/test.properties", partialManifest);
        this.analyzer.analyse(null, "com/springsource/test.properties", partialManifest);
        this.analyzer.analyse(null, "com/springsource/test/test.properties", partialManifest);
        
        Set<String> exportedPackages = partialManifest.getExportedPackages();
        assertEquals(3, exportedPackages.size());
        assertTrue(exportedPackages.contains("com"));
        assertTrue(exportedPackages.contains("com.springsource"));
        assertTrue(exportedPackages.contains("com.springsource.test"));
    }

    @Test
    public void canAnalyse() {
        assertTrue(this.analyzer.canAnalyse("test.properties"));
        assertFalse(this.analyzer.canAnalyse("META-INF/MANIFEST.MF"));
        assertFalse(this.analyzer.canAnalyse("WEB-INF/index.html"));
        assertFalse(this.analyzer.canAnalyse("test.class"));
        assertFalse(this.analyzer.canAnalyse(".DS_Store"));
    }
}
