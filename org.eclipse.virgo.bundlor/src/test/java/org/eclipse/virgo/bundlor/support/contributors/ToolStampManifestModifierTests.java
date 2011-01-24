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

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import org.eclipse.virgo.bundlor.util.SimpleManifestContents;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

public class ToolStampManifestModifierTests {

    private final ToolStampManifestModifier modifier = new ToolStampManifestModifier();

    @Test
    public void modify() {
        ManifestContents manifestContents = new SimpleManifestContents();
        this.modifier.modify(manifestContents);
        assertNotNull(manifestContents.getMainAttributes().get("Tool"));
    }
}
