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

import java.util.Set;

public interface ReadablePartialManifest extends PartialManifest {

    /**
     * Gets the exported packages.
     * 
     * @return the exported packages.
     */
    Set<String> getExportedPackages();

    /**
     * Gets the imported packages.
     * 
     * @return the imported packages.
     */
    Set<String> getImportedPackages();

    /**
     * Gets the uses for the supplied exporting package.
     * 
     * @param exportingPackage the exporting package.
     * @return the uses.
     */
    Set<String> getUses(String exportingPackage);

    /**
     * Indicate whether a package is recordable
     * 
     * @param packageName The name of the package to record
     */
    boolean isRecordablePackage(String packageName);

    /**
     * Gets the set of unsatisfied types for a given package
     * @param packageName The name of the package
     * @return The set of unsatisfied types
     */
    Set<String> getUnsatisfiedTypes(String packageName);

}
