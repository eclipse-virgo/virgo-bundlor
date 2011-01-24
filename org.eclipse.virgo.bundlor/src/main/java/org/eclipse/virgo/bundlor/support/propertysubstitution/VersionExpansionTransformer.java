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

package org.eclipse.virgo.bundlor.support.propertysubstitution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.framework.Version;
import org.eclipse.virgo.bundlor.util.StringUtils;

import org.eclipse.virgo.bundlor.support.TemplateHeaderReader;
import org.eclipse.virgo.bundlor.util.SimpleParserLogger;
import org.eclipse.virgo.util.common.PropertyPlaceholderResolver.PlaceholderValueTransformer;
import org.eclipse.virgo.util.osgi.manifest.parse.HeaderDeclaration;
import org.eclipse.virgo.util.osgi.manifest.parse.HeaderParser;
import org.eclipse.virgo.util.osgi.manifest.parse.HeaderParserFactory;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

final class VersionExpansionTransformer implements PlaceholderValueTransformer, TemplateHeaderReader {

    private static final String VERSION_PATTERNS = "Version-Patterns";

    private static final String ATT_PATTERN = "pattern";

    private final Map<String, VersionExpander> expanders = new HashMap<String, VersionExpander>();

    private final Object expandersLock = new Object();

    public VersionExpansionTransformer() {
    }

    public VersionExpansionTransformer(ManifestContents manifestTemplate) {
        String value = manifestTemplate.getMainAttributes().get(VERSION_PATTERNS);
        List<HeaderDeclaration> headers = parseTemplate(value);
        for (HeaderDeclaration header : headers) {
            String name = header.getNames().get(0);
            String pattern = header.getAttributes().get(ATT_PATTERN);
            expanders.put(name, VersionExpansionParser.parseVersionExpander(pattern));
        }
    }

    /**
     * {@inheritDoc}
     */
    public String transform(String propertyName, String value, String pattern) {
        Version version = new Version(value);
        VersionExpander expander = getVersionExpander(pattern);
        return expander.expand(version.getMajor(), version.getMinor(), version.getMicro(), version.getQualifier());
    }

    VersionExpander getVersionExpander(String pattern) {
        synchronized (expandersLock) {
            if (!expanders.containsKey(pattern)) {
                expanders.put(pattern, VersionExpansionParser.parseVersionExpander(pattern));
            }
            return expanders.get(pattern);
        }
    }

    public List<String> getTemplateOnlyHeaderNames() {
        return Arrays.asList(VERSION_PATTERNS);
    }

    private List<HeaderDeclaration> parseTemplate(String template) {
        if (StringUtils.hasText(template)) {
            HeaderParser parser = HeaderParserFactory.newHeaderParser(new SimpleParserLogger());
            return parser.parseHeader(template);
        }
        return new ArrayList<HeaderDeclaration>(0);
    }

}
