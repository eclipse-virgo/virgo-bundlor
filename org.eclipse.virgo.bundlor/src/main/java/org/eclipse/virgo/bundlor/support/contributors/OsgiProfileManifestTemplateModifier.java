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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.virgo.bundlor.support.ManifestTemplateModifier;
import org.eclipse.virgo.bundlor.util.SimpleParserLogger;
import org.eclipse.virgo.util.common.StringUtils;
import org.eclipse.virgo.util.osgi.manifest.parse.HeaderDeclaration;
import org.eclipse.virgo.util.osgi.manifest.parse.HeaderParserFactory;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

/**
 * A modifer that reads the standard OSGi properties for boot delegation and system package exports and adds automatic
 * manifest template headers for them.
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Threadsafe
 * 
 * @author Ben Hale
 */
public final class OsgiProfileManifestTemplateModifier implements ManifestTemplateModifier {

    private static final String SYSTEM_PACKAGE_IMPORT_VERSION = "0";

    private static final String VERSION = "version";

    private static final String EXCLUDED_IMPORTS = "Excluded-Imports";

    private static final String IMPORT_TEMPLATE = "Import-Template";

    private static final String SYSTEM_PACKAGES = "org.osgi.framework.system.packages";

    private static final String BOOTDELEGATION = "org.osgi.framework.bootdelegation";

    private final List<HeaderDeclaration> systemPackages;

    private final List<HeaderDeclaration> bootDelegation;

    public OsgiProfileManifestTemplateModifier(Properties properties) {
        systemPackages = parseTemplate(properties.getProperty(SYSTEM_PACKAGES));
        for (HeaderDeclaration headerDeclaration : systemPackages) {
            headerDeclaration.getAttributes().put(VERSION, SYSTEM_PACKAGE_IMPORT_VERSION);
        }
        bootDelegation = parseTemplate(properties.getProperty(BOOTDELEGATION));
    }

    /**
     * {@inheritDoc}
     */
    public void modify(ManifestContents manifestTemplate) {
        if (systemPackages.size() != 0) {
            List<HeaderDeclaration> existingHeaders = parseTemplate(manifestTemplate.getMainAttributes().get(IMPORT_TEMPLATE));
            String newValue = createValueString(existingHeaders, systemPackages);
            manifestTemplate.getMainAttributes().put(IMPORT_TEMPLATE, newValue);
        }

        if (bootDelegation.size() != 0) {
            List<HeaderDeclaration> existingHeaders = parseTemplate(manifestTemplate.getMainAttributes().get(EXCLUDED_IMPORTS));
            String newValue = createValueString(existingHeaders, bootDelegation);
            manifestTemplate.getMainAttributes().put(EXCLUDED_IMPORTS, newValue);
        }
    }

    private String createValueString(List<HeaderDeclaration> existingHeaders, List<HeaderDeclaration> additionalHeaders) {
        List<HeaderDeclaration> allHeaders = new ArrayList<HeaderDeclaration>(existingHeaders.size() + additionalHeaders.size());
        allHeaders.addAll(existingHeaders);
        allHeaders.addAll(additionalHeaders);

        StringBuilder sb = new StringBuilder();
        for (Iterator<HeaderDeclaration> i = allHeaders.iterator(); i.hasNext();) {
            writeHeader(i.next(), sb);
            if (i.hasNext()) {
                sb.append(',');
            }
        }

        return sb.toString();
    }

    private void writeHeader(HeaderDeclaration header, StringBuilder sb) {
        sb.append(header.getNames().get(0));
        writeMap(header.getAttributes(), sb, "=");
        writeMap(header.getDirectives(), sb, ":=");
    }

    private void writeMap(Map<String, String> map, StringBuilder sb, String delimiter) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(";").append(entry.getKey()).append(delimiter).append("\"").append(entry.getValue()).append("\"");
        }
    }

    private List<HeaderDeclaration> parseTemplate(String template) {
        if (StringUtils.hasText(template)) {
            return HeaderParserFactory.newHeaderParser(new SimpleParserLogger()).parseHeader(template);
        } else {
            return new ArrayList<HeaderDeclaration>(0);
        }
    }

}
