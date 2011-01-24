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

import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import org.eclipse.virgo.bundlor.EntryScannerListener;
import org.eclipse.virgo.bundlor.support.classpath.StandardClassPathFactory;
import org.eclipse.virgo.bundlor.support.partialmanifest.PartialManifest;
import org.eclipse.virgo.bundlor.support.partialmanifest.ReadablePartialManifest;
import org.eclipse.virgo.bundlor.util.SimpleManifestContents;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

public class StandardManifestGeneratorTests {

    @Test
    public void generate() {
        StubManifestMerger manifestMerger = new StubManifestMerger();
        ManifestGeneratorContributors contributors = new ManifestGeneratorContributors();

        StubReadablePartialManifest readablePartialManifest = new StubReadablePartialManifest();
        contributors.setReadablePartialManifest(readablePartialManifest);

        StubManifestReader manifestReader = new StubManifestReader();
        contributors.addManifestReader(manifestReader);

        StubManifestModifier manifestModifier = new StubManifestModifier();
        contributors.addManifestModifier(manifestModifier);

        StubManifestTemplateModifier manifestTemplateModifier = new StubManifestTemplateModifier();
        contributors.addManifestTemplateModifier(manifestTemplateModifier);

        StubArtifactAnalyzer artifactAnalyzer = new StubArtifactAnalyzer();
        contributors.addArtifactAnalyzer(artifactAnalyzer);

        StubPartialManifestModifier partialManifestModifier = new StubPartialManifestModifier();
        contributors.addPartialManifestModifier(partialManifestModifier);

        StubTemplateHeaderReader templateHeaderReader = new StubTemplateHeaderReader();
        contributors.addTemplateHeaderReader(templateHeaderReader);

        StubEntryScannerListener entryScannerListener = new StubEntryScannerListener();
        contributors.addEntryScannerListener(entryScannerListener);

        StandardManifestGenerator generator = new StandardManifestGenerator(contributors, manifestMerger);
        generator.generate(new SimpleManifestContents(), new StandardClassPathFactory().create("src/main/java"));
        assertTrue(manifestReader.getJarCalled());
        assertTrue(manifestReader.getTemplateCalled());
        assertTrue(manifestModifier.getCalled());
        assertTrue(manifestTemplateModifier.getCalled());
        assertTrue(artifactAnalyzer.getAnalyzeCalled());
        assertTrue(artifactAnalyzer.getCanAnalyzeCalled());
        assertTrue(partialManifestModifier.getCalled());
        assertTrue(templateHeaderReader.getCalled());
        assertTrue(entryScannerListener.getBeginCalled());
        assertTrue(entryScannerListener.getEndCalled());
        assertTrue(manifestMerger.getCalled());
    }

    private static class StubManifestMerger implements ManifestMerger {

        private volatile boolean called = false;

        public ManifestContents merge(ManifestContents existingManifest, ManifestContents manifestTemplate, ManifestContents contributedManifest,
            ReadablePartialManifest partialManifest, List<String> templateOnlyHeaderNames) {
            this.called = true;
            return null;
        }

        public boolean getCalled() {
            return called;
        }
    }

    private static class StubReadablePartialManifest implements ReadablePartialManifest {

        public Set<String> getExportedPackages() {
            return null;
        }

        public Set<String> getImportedPackages() {
            return null;
        }

        public Set<String> getUnsatisfiedTypes(String packageName) {
            return null;
        }

        public Set<String> getUses(String exportingPackage) {
            return null;
        }

        public boolean isRecordablePackage(String packageName) {
            return false;
        }

        public void recordExportPackage(String fullyQualifiedPackageName) {
        }

        public void recordReferencedPackage(String fullyQualifiedPackageName) {
        }

        public void recordReferencedType(String fullyQualifiedTypeName) {
        }

        public void recordType(String fullyQualifiedTypeName) {
        }

        public void recordUsesPackage(String usingPackage, String usedPackage) {
        }

    }

    private static class StubManifestReader implements ManifestReader {

        private volatile boolean jarCalled = false;

        private volatile boolean templateCalled = false;

        public void readJarManifest(ManifestContents manifest) {
            this.jarCalled = true;
        }

        public void readManifestTemplate(ManifestContents manifestTemplate) {
            this.templateCalled = true;
        }

        public boolean getJarCalled() {
            return jarCalled;
        }

        public boolean getTemplateCalled() {
            return templateCalled;
        }
    }

    private static class StubManifestModifier implements ManifestModifier {

        private volatile boolean called = false;

        public void modify(ManifestContents manifest) {
            this.called = true;
        }

        public boolean getCalled() {
            return called;
        }
    }

    private static class StubManifestTemplateModifier implements ManifestTemplateModifier {

        private volatile boolean called = false;

        public void modify(ManifestContents manifestTemplate) {
            this.called = true;
        }

        public boolean getCalled() {
            return called;
        }
    }

    private static class StubArtifactAnalyzer implements ArtifactAnalyzer {

        private volatile boolean analyzeCalled = false;

        private volatile boolean canAnalyzeCalled = false;

        public void analyse(InputStream artefact, String artefactName, PartialManifest partialManifest) throws Exception {
            this.analyzeCalled = true;
        }

        public boolean canAnalyse(String artefactName) {
            this.canAnalyzeCalled = true;
            return true;
        }

        public boolean getAnalyzeCalled() {
            return analyzeCalled;
        }

        public boolean getCanAnalyzeCalled() {
            return canAnalyzeCalled;
        }
    }

    private static class StubPartialManifestModifier implements PartialManifestModifier {

        private volatile boolean called = false;

        public void modify(ReadablePartialManifest partialManifest) {
            this.called = true;
        }

        public boolean getCalled() {
            return called;
        }
    }

    private static class StubTemplateHeaderReader implements TemplateHeaderReader {

        private volatile boolean called = false;

        public List<String> getTemplateOnlyHeaderNames() {
            this.called = true;
            return Collections.emptyList();
        }

        public boolean getCalled() {
            return called;
        }
    }

    private static class StubEntryScannerListener implements EntryScannerListener {

        private volatile boolean beginCalled = false;

        private volatile boolean endCalled = false;

        public void onBeginEntry(String name) {
            this.beginCalled = true;
        }

        public void onEndEntry() {
            this.endCalled = true;
        }

        public boolean getBeginCalled() {
            return beginCalled;
        }

        public boolean getEndCalled() {
            return endCalled;
        }

    }
}
