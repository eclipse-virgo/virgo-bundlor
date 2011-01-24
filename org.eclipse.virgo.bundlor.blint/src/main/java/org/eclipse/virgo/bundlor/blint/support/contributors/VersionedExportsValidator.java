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
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

/**
 * Validates that all imported packages have a version specified.
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Threadsafe
 * 
 * @author Ben Hale
 */
public final class VersionedExportsValidator implements Validator {

    private static final String MESSAGE = "The export of package %s does not specify a version";

    /**
     * {@inheritDoc}
     */
    public Set<String> validate(ManifestContents manifest) {
        BundleManifest bundleManifest = BundleManifestUtils.createBundleManifest(manifest);
        Set<String> warnings = new HashSet<String>();

        for (ExportedPackage packageExport : bundleManifest.getExportPackage().getExportedPackages()) {
            if (packageExport.getAttributes().get("version") == null) {
                warnings.add(String.format(MESSAGE, packageExport.getPackageName()));
            }
        }

        return warnings;
    }

}
