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

import org.eclipse.virgo.util.parser.manifest.ManifestContents;

/**
 * Implementations of this interface have the opportunity to read the bundle's manifest and the manifest template before
 * they are used to create the bundle manifest
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Implementations of this interface must be threadsafe
 * 
 * @author Ben Hale
 */
public interface ManifestReader {

    /**
     * Read the source JAR's manifest before it is used to create the bundle manifest
     * 
     * @param manifest the JAR manifest
     */
    void readJarManifest(ManifestContents manifest);

    /**
     * Read the manifest template before it is used to create the bundle manifest
     * 
     * @param manifestTemplate the manifest template
     */
    void readManifestTemplate(ManifestContents manifestTemplate);
}
