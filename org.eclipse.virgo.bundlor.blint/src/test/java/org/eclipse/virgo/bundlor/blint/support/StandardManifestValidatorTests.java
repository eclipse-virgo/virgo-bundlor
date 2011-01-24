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

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Set;

import org.junit.Test;

import org.eclipse.virgo.bundlor.util.SimpleManifestContents;
import org.eclipse.virgo.util.math.Sets;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

public class StandardManifestValidatorTests {

    private final StandardManifestValidator validator = new StandardManifestValidator(
        new ManifestValidatorContributors().addValidator(new StubValidator()));

    @Test(expected = UnsupportedOperationException.class)
    public void warningsListUnmodifiable() {
        List<String> warnings = this.validator.validate(new SimpleManifestContents());
        assertNotNull(warnings);
        warnings.remove(0);
    }

    private static class StubValidator implements Validator {

        public Set<String> validate(ManifestContents manifest) {
            return Sets.asSet("warning");
        }

    }
}
