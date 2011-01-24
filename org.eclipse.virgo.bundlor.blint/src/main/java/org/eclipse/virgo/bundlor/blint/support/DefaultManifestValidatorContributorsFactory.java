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

package org.eclipse.virgo.bundlor.blint.support;

import org.eclipse.virgo.bundlor.blint.support.contributors.BundleManifestVersionValidator;
import org.eclipse.virgo.bundlor.blint.support.contributors.BundleSymbolicNameValidator;
import org.eclipse.virgo.bundlor.blint.support.contributors.ExportedNotImportedValidator;
import org.eclipse.virgo.bundlor.blint.support.contributors.SignedJarFileValidator;
import org.eclipse.virgo.bundlor.blint.support.contributors.VersionedExportsValidator;
import org.eclipse.virgo.bundlor.blint.support.contributors.VersionedImportsRangeValidator;
import org.eclipse.virgo.bundlor.blint.support.contributors.VersionedImportsValidator;

public final class DefaultManifestValidatorContributorsFactory {

    public static ManifestValidatorContributors create() {
        ManifestValidatorContributors contributors = new ManifestValidatorContributors();

        contributors //
        .addValidator(new BundleManifestVersionValidator()) //
        .addValidator(new BundleSymbolicNameValidator()) //
        .addValidator(new VersionedImportsValidator()) //
        .addValidator(new VersionedExportsValidator()) //
        .addValidator(new ExportedNotImportedValidator()) //
        .addValidator(new SignedJarFileValidator()) //
        .addValidator(new VersionedImportsRangeValidator());

        return contributors;
    }
}
