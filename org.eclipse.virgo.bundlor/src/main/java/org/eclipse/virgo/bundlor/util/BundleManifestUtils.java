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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.eclipse.virgo.util.osgi.manifest.BundleManifest;
import org.eclipse.virgo.util.osgi.manifest.BundleManifestFactory;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;
import org.eclipse.virgo.util.parser.manifest.ManifestParser;
import org.eclipse.virgo.util.parser.manifest.ManifestProblem;
import org.eclipse.virgo.util.parser.manifest.RecoveringManifestParser;

/**
 * Utilities for working with {@link BundleManifest BundleManifests}.
 * <p/>
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Threadsafe.
 * 
 * @author Rob Harrop
 */
public final class BundleManifestUtils {

    private static final String UTF_8 = "UTF-8";

    /**
     * Creates a {@link BundleManifest} from the supplied {@link ManifestContents}.
     * 
     * @param mf the <code>Manifest</code>.
     * @return the created <code>BundleManifest</code>.
     */
    public static BundleManifest createBundleManifest(ManifestContents manifest) {
        return BundleManifestFactory.createBundleManifest(manifest, new SimpleParserLogger());
    }

    public static ManifestContents getManifest(File manifestFile) {
        if (manifestFile == null || !manifestFile.exists()) {
            return new SimpleManifestContents();
        }

        try {
            return getManifest(new InputStreamReader(new FileInputStream(manifestFile), Charset.forName(UTF_8)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static ManifestContents getManifest(Reader reader) {
        ManifestParser parser = new RecoveringManifestParser();
        try {
            ManifestContents manifest = parser.parse(reader);
            if (parser.foundProblems()) {
                for (ManifestProblem problem : parser.getProblems()) {
                    System.err.println(problem.toStringWithContext());
                    System.err.println();
                }
                throw new RuntimeException("There was a problem with the manifest");
            }
            return manifest;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // Nothing to do
                }
            }
        }
    }

}
