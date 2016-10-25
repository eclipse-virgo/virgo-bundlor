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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import org.eclipse.virgo.bundlor.ManifestWriter;
import org.eclipse.virgo.bundlor.util.BundleManifestUtils;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

final class FileSystemManifestWriter implements ManifestWriter {

    private static final String MANIFEST_PATH = "META-INF/MANIFEST.MF";

    private final File manifestFile;

    public FileSystemManifestWriter(File root) {
        manifestFile = new File(root, MANIFEST_PATH);
    }

    public void write(ManifestContents manifest) {
        Writer out = null;

        try {
            if (!manifestFile.getParentFile().exists() && !manifestFile.getParentFile().mkdirs()) {
                throw new RuntimeException(String.format("Could not create parent directories of '%s'", manifestFile.getAbsolutePath()));
            }
            out = new OutputStreamWriter(new FileOutputStream(manifestFile), Charset.forName("UTF-8"));
            BundleManifestUtils.createBundleManifest(manifest).write(out);
            System.out.printf("Manifest written to '%s'%n", manifestFile.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // Nothing to do
                }
            }
        }
    }

    public void close() {
        // Nothing to close
    }

}
