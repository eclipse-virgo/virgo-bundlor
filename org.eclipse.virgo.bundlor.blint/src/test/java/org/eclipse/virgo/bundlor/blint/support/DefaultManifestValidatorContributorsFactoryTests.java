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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import org.eclipse.virgo.bundlor.blint.support.contributors.BundleManifestVersionValidator;
import org.eclipse.virgo.bundlor.blint.support.contributors.BundleSymbolicNameValidator;
import org.eclipse.virgo.bundlor.blint.support.contributors.ExportedNotImportedValidator;
import org.eclipse.virgo.bundlor.blint.support.contributors.SignedJarFileValidator;
import org.eclipse.virgo.bundlor.blint.support.contributors.VersionedExportsValidator;
import org.eclipse.virgo.bundlor.blint.support.contributors.VersionedImportsRangeValidator;
import org.eclipse.virgo.bundlor.blint.support.contributors.VersionedImportsValidator;

public class DefaultManifestValidatorContributorsFactoryTests {

    @Test
    public void contents() {
        ManifestValidatorContributors contributors = DefaultManifestValidatorContributorsFactory.create();
        assertContainsInstanceOf(contributors.getValidators(), BundleManifestVersionValidator.class, BundleSymbolicNameValidator.class,
            VersionedImportsValidator.class, VersionedExportsValidator.class, ExportedNotImportedValidator.class, SignedJarFileValidator.class,
            VersionedImportsRangeValidator.class);
    }

    private final void assertContainsInstanceOf(List<Validator> validators, Class<?>... clazzes) {
        assertEquals(validators.size(), clazzes.length);
        for (Class<?> clazz : clazzes) {
            for (Validator validator : validators) {
                if (validator.getClass().equals(clazz)) {
                    return;
                }
            }
            fail(String.format("Default contributors does not contain an instance of '%s'", clazz.getName()));
        }
    }
}
