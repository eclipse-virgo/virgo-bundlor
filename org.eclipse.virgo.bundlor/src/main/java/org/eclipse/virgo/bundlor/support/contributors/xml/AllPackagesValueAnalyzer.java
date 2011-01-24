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

package org.eclipse.virgo.bundlor.support.contributors.xml;

import org.eclipse.virgo.bundlor.support.partialmanifest.PartialManifest;
import org.eclipse.virgo.bundlor.util.ClassNameUtils;

final class AllPackagesValueAnalyzer implements ValueAnalyzer {

    private final PartialManifest partialManifest;

    public AllPackagesValueAnalyzer(PartialManifest partialManifest) {
        this.partialManifest = partialManifest;
    }

    public void analyse(String value) {
        if (ClassNameUtils.isValidFqn(value)) {
            partialManifest.recordReferencedPackage(value);
        }
    }

}
