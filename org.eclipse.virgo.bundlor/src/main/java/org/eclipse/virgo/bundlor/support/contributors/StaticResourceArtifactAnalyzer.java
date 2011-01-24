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

import java.io.InputStream;

import org.eclipse.virgo.bundlor.support.ArtifactAnalyzer;
import org.eclipse.virgo.bundlor.support.partialmanifest.PartialManifest;

/**
 * An analyzer that detects the packages of static resources
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Threadsafe
 * 
 * @author Ben Hale
 */
public final class StaticResourceArtifactAnalyzer implements ArtifactAnalyzer {

    /**
     * {@inheritDoc}
     */
    public void analyse(InputStream artefact, String artefactName, PartialManifest partialManifest) throws Exception {
        int index = artefactName.lastIndexOf('/');
        if (index > -1) {
            String packageName = artefactName.substring(0, index).replace('/', '.');
            if (packageName.matches("([a-zA-Z$_][a-zA-Z0-9$_]+)+(\\.[a-zA-Z$_][a-zA-Z0-9$_]+)*")) {
                partialManifest.recordExportPackage(packageName.trim());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean canAnalyse(String artifactName) {
        return !artifactName.startsWith("META-INF") && !artifactName.startsWith("WEB-INF") && !artifactName.endsWith(".class")
            && !artifactName.startsWith(".");
    }
}
