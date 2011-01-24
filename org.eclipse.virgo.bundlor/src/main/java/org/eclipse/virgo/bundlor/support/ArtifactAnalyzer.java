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

package org.eclipse.virgo.bundlor.support;

import java.io.InputStream;

import org.eclipse.virgo.bundlor.support.partialmanifest.PartialManifest;

/**
 * Strategy interface for scanning artefacts in a JAR file and adding to the {@link PartialManifest}.
 * <p/>
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Implementations must be threadsafe.
 * 
 * @author Rob Harrop
 */
public interface ArtifactAnalyzer {

    /**
     * Analyse the supplied artefact and update the supplied {@link PartialManifest} as needed.
     * 
     * @param artefact the artefact to scan.
     * @param artefactName the name of the artefact.
     * @param partialManifest the <code>PartialManifest</code> to update.
     * @throws Exception if an error occurs during scanning.
     */
    void analyse(InputStream artifact, String artifactName, PartialManifest partialManifest) throws Exception;

    /**
     * Can this {@link ArtifactAnalyzer} analyse the artefact with the supplied name.
     * 
     * @param artefactName the name of the artefact.
     * @return <code>true</code> if the artefact can be analysed; otherwise <code>false</code>.
     */
    boolean canAnalyse(String artifactName);
}
