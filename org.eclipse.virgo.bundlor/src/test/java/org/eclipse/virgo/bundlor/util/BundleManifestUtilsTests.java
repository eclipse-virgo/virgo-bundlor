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

package org.eclipse.virgo.bundlor.util;

import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Test;

public class BundleManifestUtilsTests {

    @Test
    public void getManifest() {
        assertNotNull(BundleManifestUtils.getManifest(new File("src/test/resources/input.mf")));
    }

    @Test(expected = RuntimeException.class)
    public void getBadManifest() {
        BundleManifestUtils.getManifest(new File("src/test/resources/bad-input.mf"));
    }

    @Test
    public void getManifestNoFile() {
        assertNotNull(BundleManifestUtils.getManifest((File) null));
    }
}
