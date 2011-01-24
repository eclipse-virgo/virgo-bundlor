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

package org.eclipse.virgo.bundlor.support.contributors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.virgo.bundlor.util.StringUtils;

import org.eclipse.virgo.bundlor.support.ManifestReader;
import org.eclipse.virgo.bundlor.support.PartialManifestModifier;
import org.eclipse.virgo.bundlor.support.TemplateHeaderReader;
import org.eclipse.virgo.bundlor.support.partialmanifest.ReadablePartialManifest;
import org.eclipse.virgo.bundlor.util.MatchUtils;
import org.eclipse.virgo.bundlor.util.SimpleParserLogger;
import org.eclipse.virgo.util.osgi.manifest.parse.HeaderDeclaration;
import org.eclipse.virgo.util.osgi.manifest.parse.HeaderParser;
import org.eclipse.virgo.util.osgi.manifest.parse.HeaderParserFactory;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

/**
 * An implementation of {@link PartialManifestModifier} that removes excluded imports and exports from the partial
 * manifest
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Threadsafe
 * 
 * @author Ben Hale
 */
public final class ExcludedImportAndExportPartialManifestModifier implements ManifestReader, PartialManifestModifier, TemplateHeaderReader {

    private static final String ATTR_EXCLUDED_EXPORTS = "Excluded-Exports";

    private static final String ATTR_EXCLUDED_IMPORTS = "Excluded-Imports";

    private final List<String> excludedImports = new ArrayList<String>();

    private final Object excludedImportsMonitor = new Object();

    private final List<String> excludedExports = new ArrayList<String>();

    private final Object excludedExportsMonitor = new Object();

    public void readJarManifest(ManifestContents manifest) {
        // Nothing to do
    }

    public void readManifestTemplate(ManifestContents manifestTemplate) {
        synchronized (excludedImportsMonitor) {
            String value = manifestTemplate.getMainAttributes().get(ATTR_EXCLUDED_IMPORTS);
            List<HeaderDeclaration> headers = parseTemplate(value);
            for (HeaderDeclaration header : headers) {
                excludedImports.add(header.getNames().get(0));
            }
        }
        synchronized (excludedExportsMonitor) {
            String value = manifestTemplate.getMainAttributes().get(ATTR_EXCLUDED_EXPORTS);
            List<HeaderDeclaration> headers = parseTemplate(value);
            for (HeaderDeclaration header : headers) {
                excludedExports.add(header.getNames().get(0));
            }
        }
    }

    private List<HeaderDeclaration> parseTemplate(String template) {
        if (StringUtils.hasText(template)) {
            HeaderParser parser = HeaderParserFactory.newHeaderParser(new SimpleParserLogger());
            return parser.parseHeader(template);
        }
        return new ArrayList<HeaderDeclaration>(0);
    }

    public void modify(ReadablePartialManifest partialManifest) {
        synchronized (excludedImportsMonitor) {
            removeExcludedPackages(partialManifest.getImportedPackages(), excludedImports);
        }
        synchronized (excludedExportsMonitor) {
            removeExcludedPackages(partialManifest.getExportedPackages(), excludedExports);
        }
    }

    private void removeExcludedPackages(Set<String> packageNames, List<String> exclusions) {
        for (String packageName : new LinkedHashSet<String>(packageNames)) {
            if (isExcluded(packageName, exclusions)) {
                packageNames.remove(packageName);
            }
        }
    }

    private boolean isExcluded(String exportedPackage, List<String> exclusions) {
        for (String exclusion : exclusions) {
            if (MatchUtils.matches(exportedPackage, exclusion)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getTemplateOnlyHeaderNames() {
        return Arrays.asList(ATTR_EXCLUDED_EXPORTS, ATTR_EXCLUDED_IMPORTS);
    }
}
