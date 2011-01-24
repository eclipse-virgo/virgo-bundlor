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

import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.eclipse.virgo.bundlor.support.ArtifactAnalyzer;
import org.eclipse.virgo.bundlor.support.partialmanifest.PartialManifest;

/**
 * An analyzer for a Log4J<code>log4j.xml</code> file. Analyzes the list of class names that are found in the
 * <code>appender</code> and <code>layout</code> tags.
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Threadsafe.
 * 
 * @author Ben Hale
 */
public final class Log4JXmlArtifactAnalyzer implements ArtifactAnalyzer {

    private static final String EXPRESSION = "//appender/@class | //layout/@class";

    public void analyse(InputStream artifact, String artifactName, PartialManifest partialManifest) throws Exception {
        new StandardXmlArtifactAnalyzer(artifact, new Log4JEntityResolver()).analyzeValues(EXPRESSION, new AllClassesValueAnalyzer(partialManifest));
    }

    public boolean canAnalyse(String artefactName) {
        return "log4j.xml".equals(artefactName);
    }

    private static class Log4JEntityResolver implements EntityResolver {

        private static final String LOG4J_DTD = "org/eclipse/virgo/bundlor/support/contributors/log4j.dtd";

        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            if (systemId.endsWith("log4j.dtd")) {
                ClassLoader classLoader = this.getClass().getClassLoader();
                return new InputSource(classLoader.getResourceAsStream(LOG4J_DTD));
            }
            return null;
        }

    }

}
