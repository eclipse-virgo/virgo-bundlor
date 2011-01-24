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

package org.eclipse.virgo.bundlor.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.virgo.util.parser.manifest.ManifestContents;

/**
 * A simple implementation of the {@link ManifestContents} interface backed by maps.
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Not threadsafe
 * 
 * @author Ben Hale
 */
public class SimpleManifestContents implements ManifestContents {

    private final String version;

    private final Map<String, String> mainAttributes = new HashMap<String, String>();

    private final Map<String, Map<String, String>> otherSectionAttributes = new HashMap<String, Map<String, String>>();

    public SimpleManifestContents() {
        this("1.0");
    }

    public SimpleManifestContents(String version) {
        this.version = version;
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, String> getAttributesForSection(String sectionName) {
        if (!this.otherSectionAttributes.containsKey(sectionName)) {
            this.otherSectionAttributes.put(sectionName, new HashMap<String, String>());
        }
        return this.otherSectionAttributes.get(sectionName);
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, String> getMainAttributes() {
        return this.mainAttributes;
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getSectionNames() {
        return new ArrayList<String>(this.otherSectionAttributes.keySet());
    }

    /**
     * {@inheritDoc}
     */
    public String getVersion() {
        return this.version;
    }

}
