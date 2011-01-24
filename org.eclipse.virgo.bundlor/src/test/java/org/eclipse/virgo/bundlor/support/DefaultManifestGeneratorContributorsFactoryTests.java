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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Properties;

import org.junit.Test;

import org.eclipse.virgo.bundlor.support.asm.AsmTypeArtifactAnalyzer;
import org.eclipse.virgo.bundlor.support.contributors.BundleClassPathArtifactAnalyzer;
import org.eclipse.virgo.bundlor.support.contributors.ExcludedImportAndExportPartialManifestModifier;
import org.eclipse.virgo.bundlor.support.contributors.IgnoredExistingHeadersManifestModifier;
import org.eclipse.virgo.bundlor.support.contributors.JspArtifactAnalyzer;
import org.eclipse.virgo.bundlor.support.contributors.ManifestTemplateDirectiveMigrator;
import org.eclipse.virgo.bundlor.support.contributors.OsgiProfileManifestTemplateModifier;
import org.eclipse.virgo.bundlor.support.contributors.StaticResourceArtifactAnalyzer;
import org.eclipse.virgo.bundlor.support.contributors.ToolStampManifestModifier;
import org.eclipse.virgo.bundlor.support.contributors.xml.BlueprintArtifactAnalyzer;
import org.eclipse.virgo.bundlor.support.contributors.xml.EclipseLinkPersistenceArtifactAnalyzer;
import org.eclipse.virgo.bundlor.support.contributors.xml.HibernateMappingArtifactAnalyzer;
import org.eclipse.virgo.bundlor.support.contributors.xml.JpaPersistenceArtifactAnalyzer;
import org.eclipse.virgo.bundlor.support.contributors.xml.Log4JXmlArtifactAnalyzer;
import org.eclipse.virgo.bundlor.support.contributors.xml.SpringApplicationContextArtifactAnalyzer;
import org.eclipse.virgo.bundlor.support.contributors.xml.WebApplicationArtifactAnalyzer;
import org.eclipse.virgo.bundlor.support.partialmanifest.StandardPartialManifestResolver;
import org.eclipse.virgo.bundlor.support.partialmanifest.StandardReadablePartialManifest;
import org.eclipse.virgo.bundlor.support.properties.PropertiesSource;
import org.eclipse.virgo.bundlor.support.propertysubstitution.PlaceholderManifestAndTemplateModifier;

public class DefaultManifestGeneratorContributorsFactoryTests {

    private final ManifestGeneratorContributors contributors = DefaultManifestGeneratorContributorsFactory.create(new StubPropertiesSource(
        Integer.MAX_VALUE), new StubPropertiesSource(Integer.MIN_VALUE));

    @Test
    public void artifactAnalyzers() {
        assertContainsInstanceOf(contributors.getArtifactAnalyzers(), //
            AsmTypeArtifactAnalyzer.class, //
            StaticResourceArtifactAnalyzer.class, //
            HibernateMappingArtifactAnalyzer.class, //
            JpaPersistenceArtifactAnalyzer.class, //
            Log4JXmlArtifactAnalyzer.class, //
            SpringApplicationContextArtifactAnalyzer.class, //
            BlueprintArtifactAnalyzer.class, //
            WebApplicationArtifactAnalyzer.class, //
            BundleClassPathArtifactAnalyzer.class, //
            JspArtifactAnalyzer.class,
            EclipseLinkPersistenceArtifactAnalyzer.class);
    }

    @Test
    public void manifestReaders() {
        assertContainsInstanceOf(contributors.getManifestReaders(), //
            ExcludedImportAndExportPartialManifestModifier.class, //
            IgnoredExistingHeadersManifestModifier.class, //
            BlueprintArtifactAnalyzer.class);
    }

    @Test
    public void manifestModifiers() {
        assertContainsInstanceOf(contributors.getManifestModifiers(), //
            PlaceholderManifestAndTemplateModifier.class, //
            IgnoredExistingHeadersManifestModifier.class, //
            ToolStampManifestModifier.class);
    }

    @Test
    public void manifestTemplateModifiers() {
        assertContainsInstanceOf(contributors.getManifestTemplateModifiers(), //
            ManifestTemplateDirectiveMigrator.class, //
            PlaceholderManifestAndTemplateModifier.class, //
            OsgiProfileManifestTemplateModifier.class);
    }

    @Test
    public void partialManifestModifiers() {
        assertContainsInstanceOf(contributors.getPartialManifestModifiers(), //
            ManifestTemplateDirectiveMigrator.class, //
            ExcludedImportAndExportPartialManifestModifier.class);
    }

    @Test
    public void manifestContributors() {
        assertContainsInstanceOf(contributors.getManifestContributors(), //
            BundleClassPathArtifactAnalyzer.class);
    }

    @Test
    public void templateHeaderReaders() {
        assertContainsInstanceOf(contributors.getTemplateHeaderReaders(), //
            ExcludedImportAndExportPartialManifestModifier.class, //
            IgnoredExistingHeadersManifestModifier.class, // 
            PlaceholderManifestAndTemplateModifier.class, //
            StandardPartialManifestResolver.class);
    }

    @Test
    public void entryScannerListeners() {
        assertContainsInstanceOf(contributors.getEntryScannerListeners() //
        );
    }

    @Test
    public void readablePartialManifest() {
        assertTrue(contributors.getReadablePartialManifest() instanceof StandardReadablePartialManifest);
    }

    @Test
    public void partialManifestResolver() {
        assertTrue(contributors.getPartialManifestResolver() instanceof StandardPartialManifestResolver);
    }

    private final <T> void assertContainsInstanceOf(List<T> candidates, Class<?>... clazzes) {
        assertEquals(candidates.size(), clazzes.length);
        for (Class<?> clazz : clazzes) {
            for (T candidate : candidates) {
                if (candidate.getClass().equals(clazz)) {
                    return;
                }
            }
            fail(String.format("Default contributors does not contain an instance of '%s'", clazz.getName()));
        }
    }

    private static class StubPropertiesSource implements PropertiesSource {

        private final int priority;

        public StubPropertiesSource(int priority) {
            this.priority = priority;
        }

        public int getPriority() {
            return this.priority;
        }

        public Properties getProperties() {
            return new Properties();
        }

    }
}
