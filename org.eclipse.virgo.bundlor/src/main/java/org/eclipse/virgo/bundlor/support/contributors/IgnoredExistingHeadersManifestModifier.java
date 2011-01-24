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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.eclipse.virgo.bundlor.util.StringUtils;

import org.eclipse.virgo.bundlor.support.ManifestModifier;
import org.eclipse.virgo.bundlor.support.ManifestReader;
import org.eclipse.virgo.bundlor.support.TemplateHeaderReader;
import org.eclipse.virgo.bundlor.util.SimpleParserLogger;
import org.eclipse.virgo.util.osgi.manifest.parse.HeaderDeclaration;
import org.eclipse.virgo.util.osgi.manifest.parse.HeaderParser;
import org.eclipse.virgo.util.osgi.manifest.parse.HeaderParserFactory;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

/**
 * An analyzer that removes existing headers from the input manifest before it is used to build the bundle manifest.
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Threadsafe
 * 
 * @author Ben Hale
 */
public final class IgnoredExistingHeadersManifestModifier implements ManifestModifier, ManifestReader, TemplateHeaderReader {

    private static final String ATTR_IGNORED_EXISTING_HEADERS = "Ignored-Existing-Headers";

    private final List<String> ignoredExistingHeaders = new ArrayList<String>();

    private final Object ignoredExistingHeadersMonitor = new Object();

    /**
     * {@inheritDoc}
     */
    public void readJarManifest(ManifestContents manifest) {
        // Nothing to do
    }

    /**
     * {@inheritDoc}
     */
    public void readManifestTemplate(ManifestContents manifestTemplate) {
        synchronized (ignoredExistingHeadersMonitor) {
            String value = manifestTemplate.getMainAttributes().get(ATTR_IGNORED_EXISTING_HEADERS);
            List<HeaderDeclaration> headers = parseTemplate(value);
            for (HeaderDeclaration header : headers) {
                ignoredExistingHeaders.add(header.getNames().get(0));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void modify(ManifestContents manifest) {
        for (String ignoredExistingHeader : ignoredExistingHeaders) {
            Pattern pattern = Pattern.compile(ignoredExistingHeader.replace("*", ".*"));

            Map<String, String> attributes = manifest.getMainAttributes();
            Iterator<String> i = attributes.keySet().iterator();
            while(i.hasNext()) {
                String header = i.next();
                if (pattern.matcher(header).matches()) {
                    i.remove();
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getTemplateOnlyHeaderNames() {
        return Arrays.asList(ATTR_IGNORED_EXISTING_HEADERS);
    }

    private List<HeaderDeclaration> parseTemplate(String template) {
        if (StringUtils.hasText(template)) {
            HeaderParser parser = HeaderParserFactory.newHeaderParser(new SimpleParserLogger());
            return parser.parseHeader(template);
        }
        return new ArrayList<HeaderDeclaration>(0);
    }

}
