/*
 * This file is part of the Eclipse Virgo project.
 *
 * Copyright (c) 2016 ISPIN AG
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Daniel Marthaler - initial contribution
 */
package org.eclipse.virgo.bundlor.gradle.internal.support

import org.eclipse.virgo.bundlor.gradle.internal.ManifestTemplateFactory
import org.eclipse.virgo.bundlor.util.BundleManifestUtils
import org.eclipse.virgo.bundlor.util.SimpleManifestContents
import org.eclipse.virgo.bundlor.util.StringUtils
import org.eclipse.virgo.util.parser.manifest.ManifestContents

class StandardManifestTemplateFactory implements ManifestTemplateFactory {

	private static final String BUNDLE_SYMBOLIC_NAME = "Bundle-SymbolicName";
    private static final String BUNDLE_VERSION = "Bundle-Version";

	@Override
    public ManifestContents create(String manifestTemplatePath, String manifestTemplate, String bundleSymbolicName, 
		String defaultBundleSymbolicName, String bundleVersion, String defaultBundleVersion) {
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
        } else if (!StringUtils.hasText(attributes.get(BUNDLE_SYMBOLIC_NAME))) {
            attributes.put(BUNDLE_SYMBOLIC_NAME, defaultBundleSymbolicName);
        }
                
        if (StringUtils.hasText(bundleVersion)) {
            attributes.put(BUNDLE_VERSION, bundleVersion);
        } else if (!StringUtils.hasText(attributes.get(BUNDLE_VERSION))) {
            attributes.put(BUNDLE_VERSION, defaultBundleVersion);
        }

        return template;
    }

}
