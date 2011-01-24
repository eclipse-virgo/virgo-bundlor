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

/**
 * Implementations of this interface have the opportunity to modify the partial manifest before it is used to create the
 * bundle manifest
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Implementations of this interface must be threadsafe
 * 
 * @author Ben Hale
 */
public interface PartialManifestModifier {

    /**
     * Modify the partial manifest before it is used to create the bundle manifest
     * 
     * @param partialManifest the partial manifest to modify
     */
    void modify(ReadablePartialManifest partialManifest);
}
