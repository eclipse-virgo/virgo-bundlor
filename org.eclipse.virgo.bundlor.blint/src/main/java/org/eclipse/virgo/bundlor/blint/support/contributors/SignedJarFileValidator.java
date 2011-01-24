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
import org.eclipse.virgo.util.math.Sets;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

/**
 * Validates that Bundlor has not been run on a signed JAR file as it would break the signing
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Threadsafe
 * 
 * @author Ben Hale
 */
public final class SignedJarFileValidator implements Validator {

    private static final String MESSAGE = "The manifest is from a signed bundle. Bundlor should not be used on signed bundles.";

    public Set<String> validate(ManifestContents manifest) {
        for (String sectionName : manifest.getSectionNames()) {
            for (String key : manifest.getAttributesForSection(sectionName).keySet()) {
                if (key.contains("Digest")) {
                    return Sets.asSet(MESSAGE);
                }
            }
        }

        return Collections.emptySet();
    }

}
