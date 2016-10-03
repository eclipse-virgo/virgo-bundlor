/*
 * This file is part of the Eclipse Virgo project.
 *
 * Copyright (c) 2016 ISPIN AG
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Daniel Marthaler - initial contribution
 */
package org.eclipse.virgo.bundlor.gradle.internal.support

import org.eclipse.virgo.bundlor.gradle.internal.PropertiesSourceFactory
import org.eclipse.virgo.bundlor.support.properties.FileSystemPropertiesSource
import org.eclipse.virgo.bundlor.support.properties.PropertiesPropertiesSource
import org.eclipse.virgo.bundlor.support.properties.PropertiesSource
import org.eclipse.virgo.bundlor.support.properties.SystemPropertiesSource
import org.eclipse.virgo.bundlor.util.StringUtils

class StandardPropertiesSourceFactory implements PropertiesSourceFactory {

	@Override
	public List<PropertiesSource> create(String propertiesPath, List<Properties> properties) {
		List<PropertiesSource> propertySources = new ArrayList<PropertiesSource>()
        propertySources.add(new SystemPropertiesSource())
        for (Properties p : properties) {
            propertySources.add(new PropertiesPropertiesSource(p))
        }
        if (StringUtils.hasText(propertiesPath)) {
            propertySources.add(new FileSystemPropertiesSource(new File(propertiesPath)))
        }
        return propertySources
	}
}
