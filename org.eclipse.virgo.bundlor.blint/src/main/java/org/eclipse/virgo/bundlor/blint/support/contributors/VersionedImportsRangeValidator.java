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
import java.util.List;
import java.util.Set;

import org.eclipse.virgo.bundlor.blint.support.Validator;
import org.eclipse.virgo.bundlor.util.BundleManifestUtils;
import org.eclipse.virgo.util.osgi.manifest.BundleManifest;
import org.eclipse.virgo.util.osgi.manifest.ImportedPackage;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

public final class VersionedImportsRangeValidator implements Validator {

    private static final String MESSAGE = "The import of package %s results in an empty range";

    public Set<String> validate(ManifestContents manifest) {
        Set<String> warnings = new HashSet<String>();

        BundleManifest bundleManifest = BundleManifestUtils.createBundleManifest(manifest);
        List<ImportedPackage> importedPackages = bundleManifest.getImportPackage().getImportedPackages();
        for (ImportedPackage importedPackage : importedPackages) {
            try {
                if (importedPackage.getVersion().isEmpty()) {
                    warnings.add(String.format(MESSAGE, importedPackage.getPackageName()));
                }
            } catch (IllegalArgumentException e) {
                warnings.add(String.format(MESSAGE, importedPackage.getPackageName()));
            }
        }

        return warnings;
    }

}
