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

package org.eclipse.virgo.bundlor.support;

import org.eclipse.virgo.bundlor.support.partialmanifest.ReadablePartialManifest;
import org.eclipse.virgo.util.osgi.manifest.BundleManifest;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

public interface PartialManifestResolver {

    /**
     * Resolves the supplied {@link StandardReadablePartialManifest} against the supplied manifest template.
     * 
     * @param templateManifest the template.
     * @param partial the partial manifest.
     * @return the resolved {@link BundleManifest}.
     */
    BundleManifest resolve(ManifestContents templateManifest, ReadablePartialManifest partial);

}
