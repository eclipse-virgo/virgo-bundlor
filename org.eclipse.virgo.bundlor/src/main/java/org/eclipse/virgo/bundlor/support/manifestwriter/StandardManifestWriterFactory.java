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

import java.io.File;

import org.eclipse.virgo.bundlor.ManifestWriter;
import org.eclipse.virgo.util.common.StringUtils;

public final class StandardManifestWriterFactory implements ManifestWriterFactory {

    /**
     * {@inheritDoc}
     */
    public ManifestWriter create(String inputPath, String outputPath) {
        if (!StringUtils.hasText(outputPath)) {
            return new SystemOutManifestWriter();
        }

        File outputFile = new File(outputPath);
        if (outputFile.isDirectory()) {
            return new FileSystemManifestWriter(outputFile);
        }
        return new JarFileManifestWriter(new File(inputPath), new File(outputPath));
    }
}
