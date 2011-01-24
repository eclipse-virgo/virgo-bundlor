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

package org.eclipse.virgo.bundlor.blint.support.contributors;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.virgo.bundlor.blint.support.Validator;
import org.eclipse.virgo.bundlor.util.BundleManifestUtils;
import org.eclipse.virgo.util.osgi.manifest.BundleManifest;
import org.eclipse.virgo.util.osgi.manifest.ExportedPackage;
import org.eclipse.virgo.util.osgi.manifest.ImportedPackage;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

/**
 * Validates that packages that are exported are not also imported
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Threadsafe
 * 
 * @author Ben Hale
 */
public final class ExportedNotImportedValidator implements Validator {

    private static final String MESSAGE = "The manifest both imports and exports the package %s";

    /**
     * {@inheritDoc}
     */
    public Set<String> validate(ManifestContents manifest) {
        BundleManifest bundleManifest = BundleManifestUtils.createBundleManifest(manifest);
        Set<String> warnings = new HashSet<String>();

        for (ExportedPackage exportedPackage : bundleManifest.getExportPackage().getExportedPackages()) {
            String exportedName = exportedPackage.getPackageName();
            for (ImportedPackage importedPackage : bundleManifest.getImportPackage().getImportedPackages()) {
                if (importedPackage.getPackageName().equals(exportedName)) {
                    warnings.add(String.format(MESSAGE, exportedName));
                }
            }
        }

        return warnings;
    }

}
