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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.virgo.bundlor.blint.ManifestValidator;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

/**
 * Standard Implementation of {@link ManifestValidator}.
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Threadsafe
 * 
 * @author Ben Hale
 */
public final class StandardManifestValidator implements ManifestValidator {

    private final ManifestValidatorContributors contributors;

    public StandardManifestValidator(ManifestValidatorContributors contributors) {
        this.contributors = contributors;
    }

    /**
     * {@inheritDoc}
     */
    public List<String> validate(ManifestContents manifest) {
        Set<String> warnings = new HashSet<String>();
        for (Validator validator : this.contributors.getValidators()) {
            try {
                warnings.addAll(validator.validate(manifest));
            } catch (Exception e) {
                // Swallow to allow others to proceed
            }
        }

        List<String> warningsList = new ArrayList<String>(warnings);
        Collections.sort(warningsList);
        return Collections.unmodifiableList(warningsList);
    }

}
