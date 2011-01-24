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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.virgo.bundlor.EntryScannerListener;
import org.eclipse.virgo.bundlor.support.partialmanifest.ReadablePartialManifest;

public final class ManifestGeneratorContributors {

    private final List<ArtifactAnalyzer> artifactAnalyzers = new ArrayList<ArtifactAnalyzer>();

    private final List<ManifestReader> manifestReaders = new ArrayList<ManifestReader>();

    private final List<ManifestModifier> manifestModifiers = new ArrayList<ManifestModifier>();

    private final List<ManifestTemplateModifier> manifestTemplateModifiers = new ArrayList<ManifestTemplateModifier>();

    private final List<PartialManifestModifier> partialManifestModifiers = new ArrayList<PartialManifestModifier>();

    private final List<ManifestContributor> manifestContributors = new ArrayList<ManifestContributor>();

    private final List<TemplateHeaderReader> templateHeaderReaders = new ArrayList<TemplateHeaderReader>();

    private final List<EntryScannerListener> entryScannerListeners = new ArrayList<EntryScannerListener>();

    private volatile ReadablePartialManifest readablePartialManifest = null;

    private volatile PartialManifestResolver partialManifestResolver = null;

    List<ArtifactAnalyzer> getArtifactAnalyzers() {
        return this.artifactAnalyzers;
    }

    List<ManifestReader> getManifestReaders() {
        return this.manifestReaders;
    }

    List<ManifestModifier> getManifestModifiers() {
        return this.manifestModifiers;
    }

    List<ManifestTemplateModifier> getManifestTemplateModifiers() {
        return this.manifestTemplateModifiers;
    }

    List<PartialManifestModifier> getPartialManifestModifiers() {
        return this.partialManifestModifiers;
    }

    public List<ManifestContributor> getManifestContributors() {
        return manifestContributors;
    }

    List<TemplateHeaderReader> getTemplateHeaderReaders() {
        return this.templateHeaderReaders;
    }

    List<EntryScannerListener> getEntryScannerListeners() {
        return this.entryScannerListeners;
    }

    ReadablePartialManifest getReadablePartialManifest() {
        return readablePartialManifest;
    }

    PartialManifestResolver getPartialManifestResolver() {
        return partialManifestResolver;
    }

    public ManifestGeneratorContributors addArtifactAnalyzer(ArtifactAnalyzer artifactAnalyzer) {
        this.artifactAnalyzers.add(artifactAnalyzer);
        return this;
    }

    public ManifestGeneratorContributors addManifestReader(ManifestReader manifestReader) {
        this.manifestReaders.add(manifestReader);
        return this;
    }

    public ManifestGeneratorContributors addManifestModifier(ManifestModifier manifestModifier) {
        this.manifestModifiers.add(manifestModifier);
        return this;
    }

    public ManifestGeneratorContributors addManifestTemplateModifier(ManifestTemplateModifier manifestTemplateModifier) {
        this.manifestTemplateModifiers.add(manifestTemplateModifier);
        return this;
    }

    public ManifestGeneratorContributors addPartialManifestModifier(PartialManifestModifier partialManifestModifier) {
        this.partialManifestModifiers.add(partialManifestModifier);
        return this;
    }

    public ManifestGeneratorContributors addManifestContributor(ManifestContributor manifestContributor) {
        this.manifestContributors.add(manifestContributor);
        return this;
    }

    public ManifestGeneratorContributors addTemplateHeaderReader(TemplateHeaderReader templateHeaderReader) {
        this.templateHeaderReaders.add(templateHeaderReader);
        return this;
    }

    public ManifestGeneratorContributors addEntryScannerListener(EntryScannerListener entryScannerListener) {
        this.entryScannerListeners.add(entryScannerListener);
        return this;
    }

    public ManifestGeneratorContributors setReadablePartialManifest(ReadablePartialManifest readablePartialManifest) {
        this.readablePartialManifest = readablePartialManifest;
        return this;
    }

    public ManifestGeneratorContributors setPartialManifestResolver(PartialManifestResolver partialManifestResolver) {
        this.partialManifestResolver = partialManifestResolver;
        return this;
    }
}
