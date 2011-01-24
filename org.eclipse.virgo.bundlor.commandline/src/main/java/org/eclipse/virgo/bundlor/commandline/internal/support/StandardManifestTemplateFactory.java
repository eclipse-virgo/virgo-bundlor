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

package org.eclipse.virgo.bundlor.commandline.internal.support;

import java.io.File;

import org.eclipse.virgo.bundlor.util.StringUtils;

import org.eclipse.virgo.bundlor.commandline.internal.ManifestTemplateFactory;
import org.eclipse.virgo.bundlor.util.BundleManifestUtils;
import org.eclipse.virgo.bundlor.util.SimpleManifestContents;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

public class StandardManifestTemplateFactory implements ManifestTemplateFactory {

    public ManifestContents create(String manifestTemplatePath) {
        ManifestContents template;
        if (StringUtils.hasText(manifestTemplatePath)) {
            template = BundleManifestUtils.getManifest(new File(manifestTemplatePath));
        } else {
            template = new SimpleManifestContents();
        }

        return template;
    }

}
