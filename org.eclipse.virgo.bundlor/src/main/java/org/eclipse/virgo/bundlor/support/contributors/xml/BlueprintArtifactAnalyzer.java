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

package org.eclipse.virgo.bundlor.support.contributors.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.virgo.bundlor.util.StringUtils;

import org.eclipse.virgo.bundlor.support.ArtifactAnalyzer;
import org.eclipse.virgo.bundlor.support.ManifestReader;
import org.eclipse.virgo.bundlor.support.partialmanifest.PartialManifest;
import org.eclipse.virgo.bundlor.util.AntPathMatcher;
import org.eclipse.virgo.bundlor.util.SimpleParserLogger;
import org.eclipse.virgo.util.osgi.manifest.parse.HeaderDeclaration;
import org.eclipse.virgo.util.osgi.manifest.parse.HeaderParser;
import org.eclipse.virgo.util.osgi.manifest.parse.HeaderParserFactory;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

/**
 * An analyzer for an OSGi Blueprint located in a JAR file. Analyzes the list of package names that are found in the
 * context.
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Not threadsafe.
 * 
 * @author Ben Hale
 */
public final class BlueprintArtifactAnalyzer implements ManifestReader, ArtifactAnalyzer {

    private static final Map<String, String> NAMESPACE_MAPPING;

    static {
        NAMESPACE_MAPPING = new HashMap<String, String>();
        NAMESPACE_MAPPING.put("bp", "http://www.osgi.org/xmlns/blueprint/v1.0.0");
    }

    private static final String DEFAULT_CONTEXT_LOCATION = "OSGI-INF/blueprint/*.xml";

    private static final String CONTEXT_PATH_HEADER = "Bundle-Blueprint";

    private static final String EXPRESSION = //
    "//bp:bean/bp:argument/@type | " + //
        "//bp:bean/@class | " + //
        "//bp:service/@interface | " + //
        "//bp:reference/@interface | " + //
        "//bp:reference-list/@interface | " + //
        "//bp:map/@key-type | " + //
        "//bp:map/@value-type | " + //
        "//bp:list/@value-type | " + //
        "//bp:set/@value-type | " + //
        "//bp:array/@value-type | " + //
        "//bp:interfaces/bp:value";

    private final List<String> contextPaths = new ArrayList<String>();

    public final void readJarManifest(ManifestContents manifest) {
        readContextPaths(manifest);
    }

    public final void readManifestTemplate(ManifestContents manifestTemplate) {
        readContextPaths(manifestTemplate);
    }

    private final void readContextPaths(ManifestContents manifest) {
        String value = manifest.getMainAttributes().get(CONTEXT_PATH_HEADER);
        List<HeaderDeclaration> headers = parseTemplate(value);
        if (headers.size() == 0) {
            contextPaths.add(DEFAULT_CONTEXT_LOCATION);
        } else {
            for (HeaderDeclaration header : headers) {
                contextPaths.add(header.getNames().get(0));
            }
        }
    }

    private final List<HeaderDeclaration> parseTemplate(String template) {
        if (StringUtils.hasText(template)) {
            HeaderParser parser = HeaderParserFactory.newHeaderParser(new SimpleParserLogger());
            return parser.parseHeader(template);
        }
        return new ArrayList<HeaderDeclaration>(0);
    }

    public void analyse(InputStream artifact, String artifactName, PartialManifest partialManifest) throws Exception {
        new StandardXmlArtifactAnalyzer(artifact, NAMESPACE_MAPPING).analyzeValues(EXPRESSION, new AllClassesValueAnalyzer(partialManifest));
    }

    public final boolean canAnalyse(String artefactName) {
        AntPathMatcher matcher = new AntPathMatcher();
        for (String contextPath : this.contextPaths) {
            if (matcher.match(contextPath, artefactName)) {
                return true;
            }
        }
        return false;
    }

}
