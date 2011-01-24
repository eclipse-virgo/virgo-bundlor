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

package org.eclipse.virgo.bundlor.support.propertysubstitution;

import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;

import org.eclipse.virgo.bundlor.support.ManifestModifier;
import org.eclipse.virgo.bundlor.support.ManifestTemplateModifier;
import org.eclipse.virgo.bundlor.support.TemplateHeaderReader;
import org.eclipse.virgo.util.common.PropertyPlaceholderResolver;
import org.eclipse.virgo.util.common.PropertyPlaceholderResolver.PlaceholderValueTransformer;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

/**
 * An implementation of {@link ManifestTemplateModifier} that substitutes property values in for place holder values
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Threadsafe
 * 
 * @author Ben Hale
 */
public final class PlaceholderManifestAndTemplateModifier implements ManifestModifier, ManifestTemplateModifier, TemplateHeaderReader {

    private final Properties properties;

    public PlaceholderManifestAndTemplateModifier(Properties properties) {
        this.properties = properties;
    }

    public void modify(ManifestContents manifest) {
        PlaceholderValueTransformer transformer = new VersionExpansionTransformer(manifest);
        PropertyPlaceholderResolver placeholderResolver = new PropertyPlaceholderResolver();

        for (Entry<String, String> entry : manifest.getMainAttributes().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            manifest.getMainAttributes().put(key, placeholderResolver.resolve(value, this.properties, transformer));
        }
        
        for(String name : manifest.getSectionNames()) {
            for(Entry<String, String> entry : manifest.getAttributesForSection(name).entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                manifest.getAttributesForSection(name).put(key, placeholderResolver.resolve(value, this.properties, transformer));
            }
        }
    }

    public List<String> getTemplateOnlyHeaderNames() {
        return new VersionExpansionTransformer().getTemplateOnlyHeaderNames();
    }

}
