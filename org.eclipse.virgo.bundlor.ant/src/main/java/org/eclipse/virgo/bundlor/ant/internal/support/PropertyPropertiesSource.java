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

package org.eclipse.virgo.bundlor.ant.internal.support;

import java.util.List;
import java.util.Properties;

import org.apache.tools.ant.taskdefs.Property;
import org.apache.tools.ant.types.PropertySet;

import org.eclipse.virgo.bundlor.support.properties.PropertiesPropertiesSource;
import org.eclipse.virgo.bundlor.support.properties.PropertiesSource;

public class PropertyPropertiesSource implements PropertiesSource {

    private final Properties properties;

    /**
     * Create a new {@link PropertiesPropertiesSource} with the given {@link Property}s.
     * 
     * @param propertySets list of {@link PropertySet}s
     */
    public PropertyPropertiesSource(List<Property> properties) {
        this.properties = new Properties();
        for (Property property : properties) {
            this.properties.put(property.getName(), property.getValue());
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    /**
     * {@inheritDoc}
     */
    public Properties getProperties() {
        return properties;
    }
}
