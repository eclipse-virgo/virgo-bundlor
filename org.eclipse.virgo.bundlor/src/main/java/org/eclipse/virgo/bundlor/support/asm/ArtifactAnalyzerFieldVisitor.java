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

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.EmptyVisitor;

import org.eclipse.virgo.bundlor.support.partialmanifest.PartialManifest;

/**
 * ASM {@link FieldVisitor} for scanning class files.
 * <p/>
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Not thread safe.
 * 
 * @author Glyn Normington
 */
final class ArtifactAnalyzerFieldVisitor extends EmptyVisitor implements FieldVisitor {

    /**
     * That <code>PartialManifest</code> being updated.
     */
    private final PartialManifest partialManifest;

    /**
     * The type that is being scanned.
     */
    private final Type type;

    /**
     * Creates a new <code>ArtefactAnalyserClassVisitor</code> to scan the supplied {@link PartialManifest}.
     * 
     * @param partialManifest the <code>PartialManifest</code> to scan.
     */
    ArtifactAnalyzerFieldVisitor(PartialManifest partialManifest, Type type) {
        this.partialManifest = partialManifest;
        this.type = type;
    }

    /**
     * {@inheritDoc}
     */
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        Type t = Type.getType(desc);
        VisitorUtils.recordReferencedTypes(partialManifest, t);
        VisitorUtils.recordUses(partialManifest, this.type, t);
        return null;
    }

}
