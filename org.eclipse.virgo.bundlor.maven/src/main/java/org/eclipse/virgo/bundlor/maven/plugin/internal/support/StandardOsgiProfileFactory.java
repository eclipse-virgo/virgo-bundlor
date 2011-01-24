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

package org.eclipse.virgo.bundlor.maven.plugin.internal.support;

import java.io.File;

import org.eclipse.virgo.bundlor.util.StringUtils;

import org.eclipse.virgo.bundlor.maven.plugin.internal.OsgiProfileFactory;
import org.eclipse.virgo.bundlor.support.properties.EmptyPropertiesSource;
import org.eclipse.virgo.bundlor.support.properties.FileSystemPropertiesSource;
import org.eclipse.virgo.bundlor.support.properties.PropertiesSource;
import org.eclipse.virgo.bundlor.support.properties.StringPropertiesSource;

public class StandardOsgiProfileFactory implements OsgiProfileFactory {

    public PropertiesSource create(String osgiProfilePath, String osgiProfile) {
        if (StringUtils.hasText(osgiProfile)) {
            return new StringPropertiesSource(osgiProfile);
        }

        if (StringUtils.hasText(osgiProfilePath)) {
            return new FileSystemPropertiesSource(new File(osgiProfilePath));
        }

        return new EmptyPropertiesSource();
    }

}
