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

package org.eclipse.virgo.bundlor.ant.internal.support;

import java.io.File;
import java.io.StringReader;
import java.util.Map;

import org.eclipse.virgo.bundlor.util.StringUtils;

import org.eclipse.virgo.bundlor.ant.internal.ManifestTemplateFactory;
import org.eclipse.virgo.bundlor.util.BundleManifestUtils;
import org.eclipse.virgo.bundlor.util.SimpleManifestContents;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

public final class StandardManifestTemplateFactory implements ManifestTemplateFactory {

    private static final String BUNDLE_SYMBOLIC_NAME = "Bundle-SymbolicName";

    private static final String BUNDLE_VERSION = "Bundle-Version";

    public ManifestContents create(String manifestTemplatePath, String manifestTemplate, String bundleSymbolicName, String bundleVersion) {
        ManifestContents template;
        if (StringUtils.hasText(manifestTemplate)) {
            template = BundleManifestUtils.getManifest(new StringReader(manifestTemplate));
        } else if (StringUtils.hasText(manifestTemplatePath)) {
            template = BundleManifestUtils.getManifest(new File(manifestTemplatePath));
        } else {
            template = new SimpleManifestContents();
        }

        Map<String, String> attributes = template.getMainAttributes();
        if (StringUtils.hasText(bundleSymbolicName)) {
            attributes.put(BUNDLE_SYMBOLIC_NAME, bundleSymbolicName);
        }
        if (StringUtils.hasText(bundleVersion)) {
            attributes.put(BUNDLE_VERSION, bundleVersion);
        }

        return template;
    }

}
