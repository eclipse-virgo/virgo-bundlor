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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.virgo.bundlor.ClassPath;
import org.eclipse.virgo.bundlor.ClassPathEntry;
import org.eclipse.virgo.bundlor.EntryScannerListener;
import org.eclipse.virgo.bundlor.ManifestGenerator;
import org.eclipse.virgo.bundlor.support.partialmanifest.PartialManifest;
import org.eclipse.virgo.bundlor.support.partialmanifest.ReadablePartialManifest;
import org.eclipse.virgo.bundlor.util.BundleManifestUtils;
import org.eclipse.virgo.bundlor.util.SimpleManifestContents;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

public class StandardManifestGenerator implements ManifestGenerator {

    private final ManifestGeneratorContributors contributors;

    private final ManifestMerger manifestMerger;

    public StandardManifestGenerator(ManifestGeneratorContributors contributors) {
        this(contributors, new StandardManifestMerger(contributors.getPartialManifestResolver()));
    }

    StandardManifestGenerator(ManifestGeneratorContributors contributors, ManifestMerger manifestMerger) {
        this.contributors = contributors;
        this.manifestMerger = manifestMerger;
    }

    public ManifestContents generate(ManifestContents manifestTemplate, ClassPath... classPaths) {
        ReadablePartialManifest partialManifest = this.contributors.getReadablePartialManifest();
        ManifestContents existingManifest = getExistingManifest(classPaths);

        for (ManifestTemplateModifier manifestTemplateModifier : this.contributors.getManifestTemplateModifiers()) {
            manifestTemplateModifier.modify(manifestTemplate);
        }

        for (ManifestReader manifestReader : this.contributors.getManifestReaders()) {
            manifestReader.readManifestTemplate(manifestTemplate);
        }

        for (ManifestModifier manifestModifier : this.contributors.getManifestModifiers()) {
            manifestModifier.modify(existingManifest);
        }

        for (ManifestReader manifestReader : this.contributors.getManifestReaders()) {
            manifestReader.readJarManifest(existingManifest);
        }

        analyzeEntries(classPaths, partialManifest);

        for (PartialManifestModifier partialManifestModifier : this.contributors.getPartialManifestModifiers()) {
            partialManifestModifier.modify(partialManifest);
        }

        ManifestContents contributedManifest = new SimpleManifestContents();
        for (ManifestContributor manifestContributor : this.contributors.getManifestContributors()) {
            manifestContributor.contribute(contributedManifest);
        }

        List<String> templateOnlyHeaderNames = new ArrayList<String>();
        for (TemplateHeaderReader templateHeaderReader : this.contributors.getTemplateHeaderReaders()) {
            templateOnlyHeaderNames.addAll(templateHeaderReader.getTemplateOnlyHeaderNames());
        }

        return this.manifestMerger.merge(existingManifest, manifestTemplate, contributedManifest, partialManifest, templateOnlyHeaderNames);
    }

    private ManifestContents getExistingManifest(ClassPath... classPaths) {
        for (ClassPath classPath : classPaths) {
            ClassPathEntry classPathEntry = classPath.getEntry("META-INF/MANIFEST.MF");
            if (classPathEntry != null) {
                return BundleManifestUtils.getManifest(classPathEntry.getReader());
            }
        }
        return new SimpleManifestContents();
    }

    private void analyzeEntries(ClassPath[] classPaths, PartialManifest partialManifest) {
        for (ClassPath classPath : classPaths) {
            try {
                for (ClassPathEntry classPathEntry : classPath) {
                    if (!classPathEntry.isDirectory()) {
                        beginEntry(classPathEntry);
                        analyzeEntry(classPathEntry, partialManifest);
                        endEntry();
                    }
                }
            } finally {
                classPath.close();
            }
        }
    }

    private void analyzeEntry(ClassPathEntry classPathEntry, PartialManifest partialManifest) {
        for (ArtifactAnalyzer artifactAnalyzer : this.contributors.getArtifactAnalyzers()) {
            if (artifactAnalyzer.canAnalyse(classPathEntry.getName())) {
                InputStream inputStream = classPathEntry.getInputStream();
                try {
                    artifactAnalyzer.analyse(inputStream, classPathEntry.getName(), partialManifest);
                } catch (Exception e) {
                    // Swallow exception to allow other analyzers to proceed
                    e.printStackTrace();
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            // Nothing to do
                        }
                    }
                }
            }
        }
    }

    private void beginEntry(ClassPathEntry classPathEntry) {
        for (EntryScannerListener entryScannerListener : this.contributors.getEntryScannerListeners()) {
            entryScannerListener.onBeginEntry(classPathEntry.getName());
        }
    }

    private void endEntry() {
        for (EntryScannerListener entryScannerListener : this.contributors.getEntryScannerListeners()) {
            entryScannerListener.onEndEntry();
        }
    }

}
