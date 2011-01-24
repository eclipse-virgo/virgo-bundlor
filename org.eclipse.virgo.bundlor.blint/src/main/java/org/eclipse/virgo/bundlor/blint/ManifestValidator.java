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

package org.eclipse.virgo.bundlor.blint;

import java.util.List;

import org.eclipse.virgo.util.parser.manifest.ManifestContents;

/**
 * Main entry point for manifest validation. Clients provide an already transformed manifest or bundle and are returned
 * a list of warnings against the contained contained within.
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Implementations must be threadsafe.
 * 
 * @author Ben Hale
 */
public interface ManifestValidator {

    /**
     * Check for warnings in a manifest directly
     * 
     * @param manifest The manifest to check
     * @return a list of warnings for this manifest
     */
    List<String> validate(ManifestContents manifest);

}
