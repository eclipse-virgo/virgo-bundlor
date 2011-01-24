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
import java.util.List;

import org.osgi.framework.Constants;
import org.eclipse.virgo.bundlor.util.StringUtils;

import org.eclipse.virgo.bundlor.support.ManifestTemplateModifier;
import org.eclipse.virgo.bundlor.support.PartialManifestModifier;
import org.eclipse.virgo.bundlor.support.partialmanifest.ReadablePartialManifest;
import org.eclipse.virgo.bundlor.util.SimpleParserLogger;
import org.eclipse.virgo.util.osgi.manifest.parse.HeaderDeclaration;
import org.eclipse.virgo.util.osgi.manifest.parse.HeaderParser;
import org.eclipse.virgo.util.osgi.manifest.parse.HeaderParserFactory;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

public class ManifestTemplateDirectiveMigrator implements ManifestTemplateModifier, PartialManifestModifier {

    private final List<HeaderDeclaration> importPackage = new ArrayList<HeaderDeclaration>();

    private final List<HeaderDeclaration> exportPackage = new ArrayList<HeaderDeclaration>();

    private final Object monitor = new Object();

    public void modify(ManifestContents manifestTemplate) {
        synchronized (this.monitor) {
            String importPackageString = manifestTemplate.getMainAttributes().get(Constants.IMPORT_PACKAGE);
            this.importPackage.addAll(parseTemplate(importPackageString));

            if (importPackageString != null) {
                String importTemplateString = manifestTemplate.getMainAttributes().get("Import-Template");
                if (importTemplateString == null) {
                    manifestTemplate.getMainAttributes().put("Import-Template", importPackageString);
                } else {
                    manifestTemplate.getMainAttributes().put("Import-Template", String.format("%s,%s", importTemplateString, importPackageString));
                }
            }

            String exportPackageString = manifestTemplate.getMainAttributes().get(Constants.EXPORT_PACKAGE);
            this.exportPackage.addAll(parseTemplate(exportPackageString));

            if (exportPackageString != null) {
                String exportTemplateString = manifestTemplate.getMainAttributes().get("Export-Template");
                if (exportTemplateString == null) {
                    manifestTemplate.getMainAttributes().put("Export-Template", exportPackageString);
                } else {
                    manifestTemplate.getMainAttributes().put("Export-Template", String.format("%s,%s", exportTemplateString, exportPackageString));
                }
            }
        }
    }

    public void modify(ReadablePartialManifest partialManifest) {
        synchronized (this.monitor) {
            for (HeaderDeclaration header : importPackage) {
                partialManifest.recordReferencedPackage(header.getNames().get(0));
            }
            for (HeaderDeclaration header : exportPackage) {
                partialManifest.recordExportPackage(header.getNames().get(0));
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
}
