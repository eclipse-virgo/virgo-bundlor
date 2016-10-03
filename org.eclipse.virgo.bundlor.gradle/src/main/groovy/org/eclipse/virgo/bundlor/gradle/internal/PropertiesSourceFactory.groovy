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
package org.eclipse.virgo.bundlor.gradle.internal

import java.util.List;
import java.util.Properties;

import org.eclipse.virgo.bundlor.support.properties.PropertiesSource;

interface PropertiesSourceFactory {

	List<PropertiesSource> create(String propertiesPath, List<Properties> propertySets)
}
