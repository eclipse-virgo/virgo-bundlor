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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

final class Configuration {

	String inputPath;
	String outputPath;
	String manifestTemplatePath;
	String manifestTemplate;
	String osgiProfilePath;
	String osgiProfile;
	String bundleSymbolicName;
	String defaultBundleSymbolicName;
	String bundleVersion;
	String defaultBundleVersion;
	String propertiesPath;
	
	final List<Properties> properties = new ArrayList<Properties>();
	
	void addProperties(Properties properties) {
		this.properties.add(properties)
	}
}
