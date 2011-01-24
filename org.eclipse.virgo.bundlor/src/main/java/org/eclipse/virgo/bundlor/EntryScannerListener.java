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

package org.eclipse.virgo.bundlor;

/**
 * Lifecycle interface to be implemented by interested parties to get notified during scanning of JARs or class folders
 * with start and end events on a per entry/file basis.
 * 
 * @author Christian Dupuis
 */
public interface EntryScannerListener {

    /**
     * Start scanning of entry identified by given <code>name</code>.
     */
    void onBeginEntry(String name);

    /**
     * End scanning of latest started entry.
     */
    void onEndEntry();
}
