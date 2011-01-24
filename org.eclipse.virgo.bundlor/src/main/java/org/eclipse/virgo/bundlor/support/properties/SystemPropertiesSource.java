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
 * Simple {@link PropertiesSource} implementation exposing the {@link Properties}s instance of
 * {@link System#getProperties()}.
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Not threadsafe.
 * 
 * @author Christian Dupuis
 */
public final class SystemPropertiesSource implements PropertiesSource {

    /**
     * Internal copy of {@link System#getProperties()} as of creation time of this instance
     */
    private final Properties properties;

    /**
     * Create a new {@link SystemPropertiesSource}
     */
    public SystemPropertiesSource() {
        this.properties = new Properties();
        this.properties.putAll(System.getProperties());
    }

    /**
     * {@inheritDoc}
     */
    public int getPriority() {
        return Integer.MIN_VALUE;
    }

    /**
     * {@inheritDoc}
     */
    public Properties getProperties() {
        return properties;
    }

}
