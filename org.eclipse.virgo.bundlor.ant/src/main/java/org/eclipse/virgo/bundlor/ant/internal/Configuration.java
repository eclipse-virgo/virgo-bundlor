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

package org.eclipse.virgo.bundlor.ant.internal;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.taskdefs.Property;
import org.apache.tools.ant.types.PropertySet;

public final class Configuration {

    private volatile String inputPath;

    private volatile String outputPath;

    private volatile String manifestTemplatePath;

    private volatile String manifestTemplate;

    private volatile String osgiProfilePath;

    private volatile String osgiProfile;

    private volatile String bundleSymbolicName;

    private volatile String bundleVersion;

    private volatile String propertiesPath;

    private final List<PropertySet> propertySets = new ArrayList<PropertySet>();

    private final List<Property> properties = new ArrayList<Property>();

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public void setManifestTemplatePath(String manifestTemplatePath) {
        this.manifestTemplatePath = manifestTemplatePath;
    }

    public void setManifestTemplate(String manifestTemplate) {
        this.manifestTemplate = manifestTemplate;
    }

    public void setOsgiProfilePath(String osgiProfilePath) {
        this.osgiProfilePath = osgiProfilePath;
    }

    public void setOsgiProfile(String osgiProfile) {
        this.osgiProfile = osgiProfile;
    }

    public void setBundleSymbolicName(String bundleSymbolicName) {
        this.bundleSymbolicName = bundleSymbolicName;
    }

    public void setBundleVersion(String bundleVersion) {
        this.bundleVersion = bundleVersion;
    }

    public void setPropertiesPath(String propertiesPath) {
        this.propertiesPath = propertiesPath;
    }

    public void addPropertySet(PropertySet propertySet) {
        this.propertySets.add(propertySet);
    }

    public void addProperty(Property property) {
        this.properties.add(property);
    }

    public String getInputPath() {
        return inputPath;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public String getManifestTemplatePath() {
        return manifestTemplatePath;
    }

    public String getManifestTemplate() {
        return manifestTemplate;
    }

    public String getOsgiProfilePath() {
        return osgiProfilePath;
    }

    public String getOsgiProfile() {
        return osgiProfile;
    }

    public String getBundleSymbolicName() {
        return bundleSymbolicName;
    }

    public String getBundleVersion() {
        return bundleVersion;
    }

    public String getPropertiesPath() {
        return propertiesPath;
    }

    public List<PropertySet> getPropertySets() {
        return propertySets;
    }

    public List<Property> getProperties() {
        return properties;
    }

}
