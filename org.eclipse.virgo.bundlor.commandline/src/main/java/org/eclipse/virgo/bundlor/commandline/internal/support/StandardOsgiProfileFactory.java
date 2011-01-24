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

import org.eclipse.virgo.bundlor.util.StringUtils;

import org.eclipse.virgo.bundlor.commandline.internal.OsgiProfileFactory;
import org.eclipse.virgo.bundlor.support.properties.EmptyPropertiesSource;
import org.eclipse.virgo.bundlor.support.properties.FileSystemPropertiesSource;
import org.eclipse.virgo.bundlor.support.properties.PropertiesSource;

public class StandardOsgiProfileFactory implements OsgiProfileFactory {

    public PropertiesSource create(String osgiProfilePath) {
        if (StringUtils.hasText(osgiProfilePath)) {
            return new FileSystemPropertiesSource(new File(osgiProfilePath));
        }

        return new EmptyPropertiesSource();
    }

}
