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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.util.Set;

import org.junit.Test;

import org.eclipse.virgo.bundlor.support.partialmanifest.ReadablePartialManifest;
import org.eclipse.virgo.bundlor.support.partialmanifest.StandardReadablePartialManifest;

public class JspArtifactAnalyzerTests {

    private final JspArtifactAnalyzer analyzer = new JspArtifactAnalyzer();

    @Test
    public void isolated() throws Exception {
        ReadablePartialManifest partialManifest = new StandardReadablePartialManifest();
        ByteArrayInputStream in = new ByteArrayInputStream(
            "<%@ page import=\"com.springsource.test.foo.class, com.springsource.test.bar.*\" %>".getBytes());
        this.analyzer.analyse(in, "test.jsp", partialManifest);

        Set<String> importedPackages = partialManifest.getImportedPackages();
        assertTrue(importedPackages.contains("com.springsource.test"));
        assertTrue(importedPackages.contains("com.springsource.test.bar"));
    }

    @Test
    public void beginning() throws Exception {
        ReadablePartialManifest partialManifest = new StandardReadablePartialManifest();
        ByteArrayInputStream in = new ByteArrayInputStream(
            "<%@ page import=\"com.springsource.test.foo.class, com.springsource.test.bar.*\" buffer=\"5kb\" autoFlush=\"false\" %> ".getBytes());
        this.analyzer.analyse(in, "test.jsp", partialManifest);

        Set<String> importedPackages = partialManifest.getImportedPackages();
        assertTrue(importedPackages.contains("com.springsource.test"));
        assertTrue(importedPackages.contains("com.springsource.test.bar"));
    }

    @Test
    public void end() throws Exception {
        ReadablePartialManifest partialManifest = new StandardReadablePartialManifest();
        ByteArrayInputStream in = new ByteArrayInputStream(
            "<%@ page errorPage=\"error.jsp\" import=\"com.springsource.test.foo.class, com.springsource.test.bar.*\" %>".getBytes());
        this.analyzer.analyse(in, "test.jsp", partialManifest);

        Set<String> importedPackages = partialManifest.getImportedPackages();
        assertTrue(importedPackages.contains("com.springsource.test"));
        assertTrue(importedPackages.contains("com.springsource.test.bar"));
    }

    @Test
    public void canAnalyze() {
        assertTrue(this.analyzer.canAnalyse("WEB-INF/jsp/index.jsp"));
        assertFalse(this.analyzer.canAnalyse("WEB-INF/jsp/index.html"));
    }
}
