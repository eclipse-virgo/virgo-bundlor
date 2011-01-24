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

package org.eclipse.virgo.bundlor.ant.internal.support;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import org.eclipse.virgo.util.parser.manifest.ManifestContents;

public class StandardManifestTemplateFactoryTests {

    private static final String SYMBOLIC_NAME = "test.symbolic.name";

    private static final String VERSION = "0.0.0";

    private final StandardManifestTemplateFactory factory = new StandardManifestTemplateFactory();

    @Test
    public void manifestTemplatePath() {
        ManifestContents template = this.factory.create("src/test/resources/test-template.mf", null, SYMBOLIC_NAME, VERSION);
        assertNotNull(template.getMainAttributes().get("Bundle-SymbolicName"));
        assertNotNull(template.getMainAttributes().get("Bundle-Version"));
    }

    @Test
    public void manifestTemplate() {
        ManifestContents template = this.factory.create(null, "Manifest-Version: 1.0", SYMBOLIC_NAME, VERSION);
        assertNotNull(template.getMainAttributes().get("Bundle-SymbolicName"));
        assertNotNull(template.getMainAttributes().get("Bundle-Version"));
    }

    @Test
    public void manifestTemplateNull() {
        ManifestContents template = this.factory.create(null, null, SYMBOLIC_NAME, VERSION);
        assertNotNull(template.getMainAttributes().get("Bundle-SymbolicName"));
        assertNotNull(template.getMainAttributes().get("Bundle-Version"));
    }
}
