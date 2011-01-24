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

import org.junit.Test;

import org.eclipse.virgo.bundlor.ManifestWriter;

public class StandardManifestWriterFactoryTests {

    private final StandardManifestWriterFactory factory = new StandardManifestWriterFactory();

    @Test
    public void outputPathNull() {
        ManifestWriter writer = this.factory.create(null, null);
        assertTrue(writer instanceof SystemOutManifestWriter);
    }

    @Test
    public void outputPathDirectory() {
        ManifestWriter writer = this.factory.create(null, "src/test/resources");
        assertTrue(writer instanceof FileSystemManifestWriter);
    }

    @Test
    public void outputPathFile() {
        ManifestWriter writer = this.factory.create("src/test/resources/test.jar", "src/test/resources/test.jar");
        assertTrue(writer instanceof JarFileManifestWriter);
    }

}
