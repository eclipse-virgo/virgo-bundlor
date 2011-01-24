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

package org.eclipse.virgo.bundlor.blint.support.contributors;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;

import org.eclipse.virgo.bundlor.util.SimpleManifestContents;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

public class VersionedImportsRangeValidatorTests {

    private final VersionedImportsRangeValidator validator = new VersionedImportsRangeValidator();

    @Test
    public void valid() {
        ManifestContents manifest = new SimpleManifestContents();
        manifest.getMainAttributes().put("Import-Package", "foo;version=\"[1,2)\",bar;version=\"3\",baz;version=\"[4,5]\"");
        Set<String> warnings = this.validator.validate(manifest);
        assertEquals(0, warnings.size());
    }

    @Test
    public void reversed() {
        ManifestContents manifest = new SimpleManifestContents();
        manifest.getMainAttributes().put("Import-Package", "foo;version=\"[2,1)\",bar;version=\"[2,1]\",baz;version=\"(2,1)\"");
        Set<String> warnings = this.validator.validate(manifest);
        assertEquals(3, warnings.size());
    }

    @Test
    public void exclusive() {
        ManifestContents manifest = new SimpleManifestContents();
        manifest.getMainAttributes().put("Import-Package", "foo;version=\"(1,2)\",bar;version=\"[1,1]\",baz;version=\"(1,1)\"");
        Set<String> warnings = this.validator.validate(manifest);
        assertEquals(1, warnings.size());
    }
    
    @Test
    public void missingComma() {
        ManifestContents manifest = new SimpleManifestContents();
        manifest.getMainAttributes().put("Import-Package", "foo;version=\"(1,2)\",bar;version=\"[1 1]\",baz;version=\"(1,2)\"");
        Set<String> warnings = this.validator.validate(manifest);
        assertEquals(1, warnings.size());
    }

}
