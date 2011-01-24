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

package org.eclipse.virgo.bundlor.blint.support;

import java.util.Set;

import org.eclipse.virgo.util.parser.manifest.ManifestContents;

/**
 * Implementations of this interface have the opportunity to check a bundle's manifest and create warnings against it.
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Implementations should be threadsafe
 * 
 * @author Ben Hale
 */
public interface Validator {

    /**
     * Check for warnings against a bundle's manifest
     * 
     * @param manifest the manifest to validate
     * @return a collection of warnings
     */
    Set<String> validate(ManifestContents manifest);

}
