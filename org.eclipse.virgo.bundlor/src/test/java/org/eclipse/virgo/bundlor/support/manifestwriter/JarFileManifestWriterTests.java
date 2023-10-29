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

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.eclipse.virgo.bundlor.util.FileCopyUtils;

import org.eclipse.virgo.bundlor.util.SimpleManifestContents;

public class JarFileManifestWriterTests {

    @Test
    public void write() throws IOException {
        JarFileManifestWriter writer = new JarFileManifestWriter(new File("build/input.jar"), new File("build/output.jar"));
        FileCopyUtils.copy(new File("src/test/resources/input.jar"), new File("build/input.jar"));
        writer.write(new SimpleManifestContents());
        assertTrue(new File("build/output.jar").exists());
    }

    @Test
    public void writeSameLocation() throws IOException {
        JarFileManifestWriter writer = new JarFileManifestWriter(new File("build/input.jar"), new File("build/input.jar"));
        FileCopyUtils.copy(new File("src/test/resources/input.jar"), new File("build/input.jar"));
        writer.write(new SimpleManifestContents());
        assertTrue(new File("build/input.jar").exists());
    }
}
