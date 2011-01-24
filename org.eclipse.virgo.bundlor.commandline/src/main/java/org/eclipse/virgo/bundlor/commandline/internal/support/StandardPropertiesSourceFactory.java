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

package org.eclipse.virgo.bundlor.commandline.internal.support;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.virgo.bundlor.util.StringUtils;

import org.eclipse.virgo.bundlor.commandline.internal.PropertiesSourceFactory;
import org.eclipse.virgo.bundlor.support.properties.FileSystemPropertiesSource;
import org.eclipse.virgo.bundlor.support.properties.PropertiesPropertiesSource;
import org.eclipse.virgo.bundlor.support.properties.PropertiesSource;
import org.eclipse.virgo.bundlor.support.properties.SystemPropertiesSource;

public class StandardPropertiesSourceFactory implements PropertiesSourceFactory {

    public List<PropertiesSource> create(String propertiesPath, Properties properties) {
        List<PropertiesSource> propertySources = new ArrayList<PropertiesSource>();

        propertySources.add(new SystemPropertiesSource());

        if (StringUtils.hasText(propertiesPath)) {
            propertySources.add(new FileSystemPropertiesSource(new File(propertiesPath)));
        }

        propertySources.add(new PropertiesPropertiesSource(properties));

        return propertySources;
    }

}
