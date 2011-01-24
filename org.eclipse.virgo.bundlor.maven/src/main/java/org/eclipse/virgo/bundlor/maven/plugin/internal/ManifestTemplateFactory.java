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

package org.eclipse.virgo.bundlor.maven.plugin.internal;

import org.eclipse.virgo.util.parser.manifest.ManifestContents;

public interface ManifestTemplateFactory {

    ManifestContents create(String manifestTemplatePath, String manifestTemplate, String bundleSymbolicName, String defaultBundleSymbolicName,
        String bundleVersion, String defaultBundleVersion);
}
