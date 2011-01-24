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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.taskdefs.Property;
import org.apache.tools.ant.types.PropertySet;
import org.eclipse.virgo.bundlor.util.StringUtils;

import org.eclipse.virgo.bundlor.ant.internal.PropertiesSourceFactory;
import org.eclipse.virgo.bundlor.support.properties.FileSystemPropertiesSource;
import org.eclipse.virgo.bundlor.support.properties.PropertiesSource;
import org.eclipse.virgo.bundlor.support.properties.SystemPropertiesSource;

public class StandardPropertiesSourceFactory implements PropertiesSourceFactory {

    public List<PropertiesSource> create(String propertiesPath, List<PropertySet> propertySets, List<Property> properties) {
        List<PropertiesSource> propertySources = new ArrayList<PropertiesSource>();

        propertySources.add(new SystemPropertiesSource());
        propertySources.add(new PropertySetPropertiesSource(propertySets));
        propertySources.add(new PropertyPropertiesSource(properties));

        if (StringUtils.hasText(propertiesPath)) {
            propertySources.add(new FileSystemPropertiesSource(new File(propertiesPath)));
        }

        return propertySources;
    }

}
