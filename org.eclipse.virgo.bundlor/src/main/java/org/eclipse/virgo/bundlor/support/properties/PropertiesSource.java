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

package org.eclipse.virgo.bundlor.support.properties;

import java.util.Properties;

/**
 * Describes a source for manifest property values. Those values will be replaced in the template manifest prior to
 * generating the final manifest.
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Implementations need to be threadsafe.
 * 
 * @author Christian Dupuis
 */
public interface PropertiesSource {

    /**
     * Returns the priority of this {@link PropertiesSource} instance. The priority describes the order in which all
     * {@link PropertiesSource}s will be merged into the final {@link Properties} instance.
     * <p>
     * A lower priority means that the {@link PropertiesSource}'s {@link Properties} instance will be added to the
     * merged {@link Properties} instance prior to {@link PropertiesSource}s with higher priority. For example the
     * properties returned by {@link System#getProperties()} are likely to have the lowest priority to be able to
     * override those by user specified values.
     * 
     * @return the priority
     */
    int getPriority();

    /**
     * Returns the full-constructed {@link Properties} instance.
     * 
     * @return the {@link Properties} instance created by this {@link PropertiesSource}
     */
    Properties getProperties();

}
