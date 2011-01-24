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

import java.util.Collections;
import java.util.Set;

import org.eclipse.virgo.bundlor.blint.support.Validator;
import org.eclipse.virgo.bundlor.util.BundleManifestUtils;
import org.eclipse.virgo.util.math.Sets;
import org.eclipse.virgo.util.osgi.manifest.BundleManifest;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

/**
 * Validates that the <code>BundleManifest-Version</code> value is always 2
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Threadsafe
 * 
 * @author Ben Hale
 */
public final class BundleManifestVersionValidator implements Validator {

    private static final String MESSAGE = "The manifest specifies an incompatible bundle manifest version of %s. This value must be 2.";

    /**
     * {@inheritDoc}
     */
    public Set<String> validate(ManifestContents manifest) {
        BundleManifest bundleManifest = BundleManifestUtils.createBundleManifest(manifest);
        if (bundleManifest.getBundleManifestVersion() != 2) {
            return Sets.asSet(String.format(MESSAGE, bundleManifest.getBundleManifestVersion()));
        }

        return Collections.emptySet();
    }
}
