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

package org.eclipse.virgo.bundlor.support.contributors;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.virgo.bundlor.support.ManifestModifier;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

/**
 * Stamps the manifest with the name of the tool and it's version
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Threadsafe
 * 
 * @author Ben Hale
 */
public final class ToolStampManifestModifier implements ManifestModifier {

    private static final String HEADER = "Tool";

	private static final String VALUE_FORMAT = "Bundlor %s";

    /**
     * {@inheritDoc}
     */
    public void modify(ManifestContents manifest) {
        String version = this.getClass().getPackage().getImplementationVersion();
        if (version == null) {
            version = new SimpleDateFormat("yyMMddHHmmss").format(new Date());
        }

        manifest.getMainAttributes().put(HEADER, String.format(VALUE_FORMAT, version));
    }
}
