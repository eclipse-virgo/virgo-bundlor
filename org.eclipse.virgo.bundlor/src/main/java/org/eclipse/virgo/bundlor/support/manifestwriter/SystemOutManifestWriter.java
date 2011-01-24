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

package org.eclipse.virgo.bundlor.support.manifestwriter;

import java.io.IOException;
import java.io.StringWriter;

import org.eclipse.virgo.bundlor.ManifestWriter;
import org.eclipse.virgo.bundlor.util.BundleManifestUtils;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

class SystemOutManifestWriter implements ManifestWriter {

    public void write(ManifestContents manifest) {
        try {
            StringWriter stringWriter = new StringWriter();
            BundleManifestUtils.createBundleManifest(manifest).write(stringWriter);
            System.out.println(stringWriter.toString());
        } catch (IOException e) {
            // Do nothing
        }
    }

    public void close() {
    }

}
