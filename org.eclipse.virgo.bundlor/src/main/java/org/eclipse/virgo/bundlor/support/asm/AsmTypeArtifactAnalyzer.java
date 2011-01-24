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

package org.eclipse.virgo.bundlor.support.asm;

import java.io.InputStream;

import org.objectweb.asm.ClassReader;

import org.eclipse.virgo.bundlor.support.ArtifactAnalyzer;
import org.eclipse.virgo.bundlor.support.partialmanifest.PartialManifest;

/**
 * {@link ArtifactAnalyzer} implementation that uses ASM to scan <code>.class</code> files for dependencies and exports.
 * <p/>
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Threadsafe.
 * 
 * @author Rob Harrop
 */
public final class AsmTypeArtifactAnalyzer implements ArtifactAnalyzer {

    /**
     * @inheritDoc
     */
    public void analyse(InputStream artefact, String artefactName, PartialManifest model) throws Exception {
        ClassReader reader = new ClassReader(artefact);
        reader.accept(new ArtifactAnalyzerClassVisitor(model), 0);
    }

    /**
     * @inheritDoc
     */
    public boolean canAnalyse(String artefactName) {
        return artefactName.endsWith(".class");
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        return "ASM Type Scanner";
    }

}
