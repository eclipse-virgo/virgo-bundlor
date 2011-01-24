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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.virgo.bundlor.support.ArtifactAnalyzer;
import org.eclipse.virgo.bundlor.support.partialmanifest.PartialManifest;

public class JspArtifactAnalyzer implements ArtifactAnalyzer {

    private static final String PACKAGE_SUFFIX = ".*";

    private static final String TYPE_SUFFIX = ".class";

    private final Pattern pattern = Pattern.compile("<%@ page.*import=\"(.*?)\".*%>");

    public void analyse(InputStream artifact, String artifactName, PartialManifest partialManifest) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(artifact));
        for (String line = in.readLine(); line != null; line = in.readLine()) {
            for (Matcher matcher = pattern.matcher(line); matcher.find();) {
                processImports(matcher.group(1), partialManifest);
            }
        }
    }

    public boolean canAnalyse(String artifactName) {
        return artifactName.endsWith(".jsp");
    }

    private void processImports(String imports, PartialManifest partialManifest) {
        for (String importString : imports.split(",")) {
            String trimmedString = importString.trim();
            if (trimmedString.endsWith(TYPE_SUFFIX)) {
                processType(trimmedString, partialManifest);
            } else if (trimmedString.endsWith(PACKAGE_SUFFIX)) {
                processPackage(trimmedString, partialManifest);
            }
        }
    }

    private void processType(String importString, PartialManifest partialManifest) {
        String typeName = importString.substring(0, importString.length() - TYPE_SUFFIX.length());
        partialManifest.recordReferencedType(typeName);

    }

    private void processPackage(String importString, PartialManifest partialManifest) {
        String packageName = importString.substring(0, importString.length() - PACKAGE_SUFFIX.length());
        partialManifest.recordReferencedPackage(packageName);
    }

}
