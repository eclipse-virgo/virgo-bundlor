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

package org.eclipse.virgo.bundlor.support.manifestwriter;

import org.junit.Test;

import org.eclipse.virgo.bundlor.util.SimpleManifestContents;

public class SystemOutManifestWriterTests {

    @Test
    public void test() {
        SystemOutManifestWriter writer = new SystemOutManifestWriter();
        writer.write(new SimpleManifestContents());
        // Ensuring that no exceptions are thrown
    }
}
