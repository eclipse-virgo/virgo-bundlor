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

import java.util.Arrays;
import java.util.Comparator;
import java.util.Properties;

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

public final class DefaultManifestGeneratorContributorsFactory {

    public static ManifestGeneratorContributors create(PropertiesSource... propertiesSources) {
        ManifestGeneratorContributors contributors = new ManifestGeneratorContributors();

        Properties properties = combineProperties(propertiesSources);

        BlueprintArtifactAnalyzer blueprintArtifactAnalyzer = new BlueprintArtifactAnalyzer();
        IgnoredExistingHeadersManifestModifier ignoredExistingHeadersManifestModifier = new IgnoredExistingHeadersManifestModifier();
        ExcludedImportAndExportPartialManifestModifier excludedImportAndExportPartialManifestModifier = new ExcludedImportAndExportPartialManifestModifier();
        PlaceholderManifestAndTemplateModifier placeholderManifestAndTemplateModifier = new PlaceholderManifestAndTemplateModifier(properties);
        ManifestTemplateDirectiveMigrator manifestTemplateDirectiveMigrator = new ManifestTemplateDirectiveMigrator();
        StandardPartialManifestResolver partialManifestResolver = new StandardPartialManifestResolver();
        BundleClassPathArtifactAnalyzer bundleClassPathArtifactAnalyzer = new BundleClassPathArtifactAnalyzer(contributors.getArtifactAnalyzers());

        contributors //
        .addArtifactAnalyzer(new AsmTypeArtifactAnalyzer()) //
        .addArtifactAnalyzer(new StaticResourceArtifactAnalyzer()) //
        .addArtifactAnalyzer(new HibernateMappingArtifactAnalyzer()) //
        .addArtifactAnalyzer(new JpaPersistenceArtifactAnalyzer()) //
        .addArtifactAnalyzer(new Log4JXmlArtifactAnalyzer()) //
        .addArtifactAnalyzer(new SpringApplicationContextArtifactAnalyzer()) //
        .addArtifactAnalyzer(blueprintArtifactAnalyzer) //
        .addArtifactAnalyzer(new WebApplicationArtifactAnalyzer()) //
        .addArtifactAnalyzer(bundleClassPathArtifactAnalyzer) //
        .addArtifactAnalyzer(new JspArtifactAnalyzer()) //
        .addArtifactAnalyzer(new EclipseLinkPersistenceArtifactAnalyzer());

        contributors //
        .addManifestReader(excludedImportAndExportPartialManifestModifier) //
        .addManifestReader(ignoredExistingHeadersManifestModifier) //
        .addManifestReader(blueprintArtifactAnalyzer);

        contributors //
        .addManifestModifier(placeholderManifestAndTemplateModifier) //
        .addManifestModifier(ignoredExistingHeadersManifestModifier) //
        .addManifestModifier(new ToolStampManifestModifier());

        contributors //
        .addManifestTemplateModifier(manifestTemplateDirectiveMigrator) //
        .addManifestTemplateModifier(placeholderManifestAndTemplateModifier) //
        .addManifestTemplateModifier(new OsgiProfileManifestTemplateModifier(properties));

        contributors //
        .addManifestContributor(bundleClassPathArtifactAnalyzer);

        contributors //
        .addPartialManifestModifier(manifestTemplateDirectiveMigrator) //
        .addPartialManifestModifier(excludedImportAndExportPartialManifestModifier);

        contributors //
        .addTemplateHeaderReader(excludedImportAndExportPartialManifestModifier) //
        .addTemplateHeaderReader(ignoredExistingHeadersManifestModifier) //
        .addTemplateHeaderReader(placeholderManifestAndTemplateModifier) //
        .addTemplateHeaderReader(partialManifestResolver);

        contributors //
        .setReadablePartialManifest(new StandardReadablePartialManifest());

        contributors //
        .setPartialManifestResolver(partialManifestResolver);

        return contributors;
    }

    private static Properties combineProperties(PropertiesSource... propertiesSources) {
        PropertiesSource[] sortedPropertiesSources = new PropertiesSource[propertiesSources.length];
        System.arraycopy(propertiesSources, 0, sortedPropertiesSources, 0, propertiesSources.length);
        // Sort by priority so that sources with lower priority are added first into the final
        // Properties instance to allow for overriding by later instances
        Arrays.sort(sortedPropertiesSources, new Comparator<PropertiesSource>() {

            public int compare(PropertiesSource o1, PropertiesSource o2) {
                if (o1.getPriority() == o2.getPriority()) {
                    return 0;
                } else if (o1.getPriority() > o2.getPriority()) {
                    return 1;
                }
                return -1;
            }
        });

        Properties properties = new Properties();
        for (PropertiesSource source : propertiesSources) {
            properties.putAll(source.getProperties());
        }
        return properties;
    }
}
