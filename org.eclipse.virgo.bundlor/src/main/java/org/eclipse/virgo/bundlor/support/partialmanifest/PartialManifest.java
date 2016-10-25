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

package org.eclipse.virgo.bundlor.support.partialmanifest;

/**
 * Describes a partially-constructed, dynamically-generated manifest. {@link ArtifactAnalyzer ArtefactAnalysers}
 * contribute manifest elements (imports and exports) to a <code>PartialManifest</code> during analysis.
 * <p/>
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Implementations needn't be threadsafe.
 * 
 * @author Rob Harrop
 * @author Ben Hale
 */
public interface PartialManifest {

    /**
     * Records a <code>uses</code> directive member for a given package export.
     * 
     * @param usingPackage the package that is using the package.
     * @param usedPackage the package being used.
     */
    void recordUsesPackage(String usingPackage, String usedPackage);

    /**
     * Records that the supplied type is referenced by the code being analysed.
     * 
     * @param fullyQualifiedTypeName the fully-qualified name of the referenced type
     */
    void recordReferencedType(String fullyQualifiedTypeName);

    /**
     * Records the existence of a type
     * 
     * @param fullyQualifiedTypeName The fully qualified name of the type
     */
    void recordType(String fullyQualifiedTypeName);

    /**
     * Records that the supplied package is referenced by the artefact being analysed.
     * 
     * @param fullyQualifiedPackageName The fully-qualified name of the package
     */
    void recordReferencedPackage(String fullyQualifiedPackageName);

    /**
     * Records that the supplied package should be exported.
     * 
     * @param fullyQualifiedPackageName The fully-qualified name of the package
     */
    void recordExportPackage(String fullyQualifiedPackageName);

}
