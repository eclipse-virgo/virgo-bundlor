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

package org.eclipse.virgo.bundlor.ant;

import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Property;
import org.apache.tools.ant.types.PropertySet;
import org.eclipse.virgo.bundlor.util.StringUtils;

import org.eclipse.virgo.bundlor.ant.internal.AntBundlorExecutor;
import org.eclipse.virgo.bundlor.ant.internal.Configuration;
import org.eclipse.virgo.bundlor.ant.internal.support.StandardConfigurationValidator;
import org.eclipse.virgo.bundlor.ant.internal.support.StandardManifestTemplateFactory;
import org.eclipse.virgo.bundlor.ant.internal.support.StandardOsgiProfileFactory;
import org.eclipse.virgo.bundlor.ant.internal.support.StandardPropertiesSourceFactory;
import org.eclipse.virgo.bundlor.support.classpath.StandardClassPathFactory;
import org.eclipse.virgo.bundlor.support.manifestwriter.StandardManifestWriterFactory;

/**
 * An ANT task for dealing with the Bundlor tool.
 * 
 * @author Ben Hale
 * @author Christian Dupuis
 */
public class Bundlor {

    private final Configuration configuration;

    private volatile boolean failOnWarnings = false;

    private volatile boolean enabled = true;

    public Bundlor() {
        this(new Configuration());
    }

    Bundlor(Configuration configuration) {
        this.configuration = configuration;
    }

    public final void setFailOnWarnings(boolean failOnWarnings) {
        this.failOnWarnings = failOnWarnings;
    }

    public final void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public final void setInputPath(String inputPath) {
        this.configuration.setInputPath(inputPath);
    }

    public final void setOutputPath(String outputPath) {
        this.configuration.setOutputPath(outputPath);
    }

    public final void setManifestTemplatePath(String manifestTemplatePath) {
        this.configuration.setManifestTemplatePath(manifestTemplatePath);
    }

    public final void addConfiguredManifestTemplate(ManifestTemplate manifestTemplate) {
        this.configuration.setManifestTemplate(manifestTemplate.getTemplate());
    }

    public final void setOsgiProfilePath(String osgiProfilePath) {
        this.configuration.setOsgiProfilePath(osgiProfilePath);
    }

    public final void addConfiguredOsgiProfile(OsgiProfile osgiProfile) {
        this.configuration.setOsgiProfile(osgiProfile.getProfile());
    }

    public final void setBundleSymbolicName(String bundleSymbolicName) {
        this.configuration.setBundleSymbolicName(bundleSymbolicName);
    }

    public final void setBundleVersion(String bundleVersion) {
        this.configuration.setBundleVersion(bundleVersion);
    }

    public final void setPropertiesPath(String propertiesPath) {
        this.configuration.setPropertiesPath(propertiesPath);
    }

    public final void addPropertySet(PropertySet propertySet) {
        this.configuration.addPropertySet(propertySet);
    }

    public final void addProperty(Property property) {
        this.configuration.addProperty(property);
    }

    public final void execute() {
        if (!this.enabled) {
            return;
        }

        List<String> warnings = getBundlorExecutor().execute();

        if (warnings.size() > 0) {
            System.out.println("Bundlor Warnings:");
            for (String warning : warnings) {
                System.out.println("    " + warning);
            }

            if (this.failOnWarnings) {
                String message;
                if (StringUtils.hasText(this.configuration.getManifestTemplatePath())) {
                    message = String.format("Bundlor returned warnings.  Please fix manifest template at '%s' and try again.",
                        this.configuration.getManifestTemplatePath());
                } else {
                    message = "Bundlor returned warnings.  Please fix inline manifest template and try again.";
                }

                throw new BuildException(message);
            }
        }
    }

    protected BundlorExecutor getBundlorExecutor() {
        return new AntBundlorExecutor(this.configuration, new StandardConfigurationValidator(), new StandardClassPathFactory(),
            new StandardManifestWriterFactory(), new StandardManifestTemplateFactory(), new StandardPropertiesSourceFactory(),
            new StandardOsgiProfileFactory());
    }

}
