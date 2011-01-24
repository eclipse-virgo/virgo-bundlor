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

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.eclipse.virgo.bundlor.support.partialmanifest.ReadablePartialManifest;
import org.eclipse.virgo.bundlor.util.SimpleManifestContents;
import org.eclipse.virgo.util.osgi.manifest.BundleManifest;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

final class StandardManifestMerger implements ManifestMerger {

    private final PartialManifestResolver partialManifestResolver;

    public StandardManifestMerger(PartialManifestResolver partialManifestResolver) {
        this.partialManifestResolver = partialManifestResolver;
    }

    public ManifestContents merge(ManifestContents existingManifest, ManifestContents manifestTemplate, ManifestContents contributedManifest,
        ReadablePartialManifest partialManifest, List<String> templateOnlyHeaderNames) {

        ManifestContents manifest = new SimpleManifestContents();
        mergeManifests(manifest, existingManifest);
        mergeManifests(manifest, manifestTemplate);
        mergeManifests(manifest, contributedManifest);

        BundleManifest resolved = this.partialManifestResolver.resolve(manifestTemplate, partialManifest);
        mergeManifests(manifest, toManifestContents(resolved));

        removeTemplateOnlyHeaders(manifest, templateOnlyHeaderNames);

        return manifest;
    }

    private void mergeManifests(ManifestContents base, ManifestContents add) {
        base.getMainAttributes().putAll(add.getMainAttributes());
        for (String sectionName : add.getSectionNames()) {
            base.getAttributesForSection(sectionName).putAll(add.getAttributesForSection(sectionName));
        }
    }

    private ManifestContents toManifestContents(BundleManifest bundleManifest) {
        Dictionary<String, String> headers = bundleManifest.toDictionary();
        ManifestContents manifest = new SimpleManifestContents(headers.get("Manifest-Version"));
        Map<String, String> attributes = manifest.getMainAttributes();

        Enumeration<String> headerNames = headers.keys();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            attributes.put(headerName, headers.get(headerName));
        }

        return manifest;
    }

    private void removeTemplateOnlyHeaders(ManifestContents manifest, List<String> names) {
        for (String name : names) {
            manifest.getMainAttributes().remove(name);
        }
    }

}
