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

import org.apache.tools.ant.types.PropertySet;

import org.eclipse.virgo.bundlor.support.properties.PropertiesSource;

/**
 * {@link PropertiesSource} implementation that wraps a list of {@link PropertySet}s and exposes a
 * {@link Properties} view to the {@link PropertySet}s name-value pairs.
 * 
 * @author Christian Dupuis
 */
final class PropertySetPropertiesSource implements PropertiesSource {
	
	/** Eagerly created {@link Properties} instance containing all resolved name-value pairs */
	private final Properties properties;
	
	/**
	 * Create a new {@link PropertySetPropertiesSource} with the given <code>propertySets</code>.
	 * 
	 * @param propertySets list of {@link PropertySet}s
	 */
	public PropertySetPropertiesSource(List<PropertySet> propertySets) {
		this.properties = new Properties();
		for (PropertySet propertySet : propertySets) {
			this.properties.putAll(propertySet.getProperties());
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
