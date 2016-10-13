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

import org.eclipse.virgo.bundlor.support.ArtifactAnalyzer;
import org.eclipse.virgo.bundlor.support.partialmanifest.PartialManifest;

/**
 * An analyzer for the <code>web.xml</code> file in a web application. Analyzers the list of package names that are
 * found in various places in the file.
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Threadsafe
 * 
 * @author Ben Hale
 */
public final class WebApplicationArtifactAnalyzer implements ArtifactAnalyzer {

    private static final String WEB_XML_LOCATION_REGEX = ".*WEB-INF(\\\\\\\\||/)web.xml$";
    
    private static final String EXPRESSION = //
    "//context-param/param-value | " //
        + "//filter/filter-class | " //
        + "//filter/init-param/param-value | " //
        + "//listener/listener-class | " //
        + "//servlet/servlet-class | " //
        + "//servlet/init-param/param-value | " //
        + "//error-page/exception-type | " //
        + "//env-entry/env-entry-type | " //
        + "//ejb-ref/home | " //
        + "//ejb-ref/remote | " //
        + "//ejb-local-ref/local-home | " //
        + "//ejb-local-ref/local | " //
        + "//service-ref/service-interface | " //
        + "//resource-ref/res-type | " //
        + "//resource-env-ref/resource-env-ref-type | " //
        + "//message-destination-ref/message-destination-type";

    public void analyse(InputStream artifact, String artifactName, PartialManifest partialManifest) throws Exception {
        new StandardXmlArtifactAnalyzer(artifact).analyzeValues(EXPRESSION, new AllClassesValueAnalyzer(partialManifest));
    }

    public boolean canAnalyse(String artefactName) {
        return artefactName.matches(WEB_XML_LOCATION_REGEX);
    }
}
