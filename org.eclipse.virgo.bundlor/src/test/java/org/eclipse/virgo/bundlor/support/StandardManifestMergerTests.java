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

package org.eclipse.virgo.bundlor.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Test;

import org.eclipse.virgo.bundlor.support.partialmanifest.ReadablePartialManifest;
import org.eclipse.virgo.bundlor.support.partialmanifest.StandardReadablePartialManifest;
import org.eclipse.virgo.bundlor.util.SimpleManifestContents;
import org.eclipse.virgo.util.osgi.manifest.BundleManifest;
import org.eclipse.virgo.util.osgi.manifest.BundleManifestFactory;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

public class StandardManifestMergerTests {

    private final StandardManifestMerger merger = new StandardManifestMerger(new StubPartialManifestResolver());

    @Test
    public void merge() {
        ManifestContents existingManifest = new SimpleManifestContents();
        existingManifest.getMainAttributes().put("existing-key", "existing-value");
        existingManifest.getMainAttributes().put("template-key", "existing-value");
        existingManifest.getMainAttributes().put("resolved-key", "existing-value");
        existingManifest.getMainAttributes().put("removed-key", "existing-value");

        ManifestContents templateManifest = new SimpleManifestContents();
        templateManifest.getMainAttributes().put("template-key", "template-value");
        templateManifest.getMainAttributes().put("resolved-key", "template-value");

        ManifestContents manifest = this.merger.merge(existingManifest, templateManifest, new SimpleManifestContents(),
            new StandardReadablePartialManifest(), Arrays.asList("removed-key"));

        assertEquals("existing-value", manifest.getMainAttributes().get("existing-key"));
        assertEquals("template-value", manifest.getMainAttributes().get("template-key"));
        assertEquals("resolved-value", manifest.getMainAttributes().get("resolved-key"));
        assertNull(manifest.getMainAttributes().get("removed-key"));
    }

    private static class StubPartialManifestResolver implements PartialManifestResolver {

        public BundleManifest resolve(ManifestContents templateManifest, ReadablePartialManifest partial) {
            BundleManifest manifest = BundleManifestFactory.createBundleManifest();
            manifest.setHeader("resolved-key", "resolved-value");
            return manifest;
        }

    }
}
