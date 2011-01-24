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

import static junit.framework.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;

import org.eclipse.virgo.bundlor.util.SimpleManifestContents;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

public class VersionedImportsValidatorTests {

    private final VersionedImportsValidator validator = new VersionedImportsValidator();

    @Test
    public void valid() {
        ManifestContents manifest = new SimpleManifestContents();
        manifest.getMainAttributes().put("Import-Package", "test.package;version=\"[1.0.0, 2.0.0)\"");
        Set<String> warnings = this.validator.validate(manifest);
        assertEquals(0, warnings.size());
    }

    @Test
    public void invalid() {
        ManifestContents manifest = new SimpleManifestContents();
        manifest.getMainAttributes().put("Import-Package", "test.package");
        Set<String> warnings = this.validator.validate(manifest);
        assertEquals(1, warnings.size());
    }

}
