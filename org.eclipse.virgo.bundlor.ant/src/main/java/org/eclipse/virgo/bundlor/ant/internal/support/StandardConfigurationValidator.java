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

import java.io.File;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Property;
import org.apache.tools.ant.types.PropertySet;
import org.eclipse.virgo.bundlor.util.StringUtils;

import org.eclipse.virgo.bundlor.ant.internal.Configuration;
import org.eclipse.virgo.bundlor.ant.internal.ConfigurationValidator;

public final class StandardConfigurationValidator implements ConfigurationValidator {

    public void validate(Configuration configuration) throws BuildException {
        inputPath(configuration.getInputPath());
        outputPath(configuration.getOutputPath());
        manifestTemplate(configuration.getManifestTemplatePath(), configuration.getManifestTemplate(), configuration.getBundleSymbolicName(),
            configuration.getBundleVersion());
        osgiProfile(configuration.getOsgiProfilePath(), configuration.getOsgiProfile());
        properties(configuration.getPropertiesPath(), configuration.getPropertySets(), configuration.getProperties());
    }

    void inputPath(String inputPath) throws BuildException {
        if (!StringUtils.hasText(inputPath)) {
            throw new BuildException("Input path is required");
        }

        File inputFile = new File(inputPath);
        if (!inputFile.exists()) {
            throw new BuildException(String.format("Input path '%s' does not exist", inputPath));
        }
        if (inputFile.isDirectory()) {
            return;
        }
        if (inputFile.getAbsolutePath().endsWith("jar")) {
            return;
        }
        if (inputFile.getAbsolutePath().endsWith("war")) {
            return;
        }
        throw new BuildException(String.format("Input path '%s' is not JAR, WAR, or directory", inputPath));
    }

    void outputPath(String outputPath) throws BuildException {
        if (StringUtils.hasText(outputPath)) {
            File outputFile = new File(outputPath);
            if (outputFile.isDirectory()) {
                return;
            } else if (!outputFile.getParentFile().exists()) {
                throw new BuildException(String.format("Output path's parent directory '%s' does not exist", outputFile.getParent()));
            }
            if (outputFile.getAbsolutePath().endsWith("jar")) {
                return;
            }
            if (outputFile.getAbsolutePath().endsWith("war")) {
                return;
            }
            throw new BuildException(String.format("Output path '%s' is not JAR, WAR, or directory", outputFile));
        }
    }

    void manifestTemplate(String manifestTemplatePath, String manifestTemplate, String bundleSymbolicName, String bundleVersion)
        throws BuildException {
        if (StringUtils.hasText(manifestTemplatePath)) {
            File manifestTemplateFile = new File(manifestTemplatePath);
            if (!manifestTemplateFile.exists()) {
                throw new BuildException(String.format("Manifest template path '%s' does not exist", manifestTemplatePath));
            }
            if (!manifestTemplateFile.isFile()) {
                throw new BuildException(String.format("Manifest template path '%s' must be a file", manifestTemplatePath));
            }
        }
    }

    void osgiProfile(String osgiProfilePath, String osgiProfile) throws BuildException {
        if (StringUtils.hasText(osgiProfilePath)) {
            File osgiProfileFile = new File(osgiProfilePath);
            if (!osgiProfileFile.exists()) {
                throw new BuildException(String.format("OSGi profile path '%s' does not exist", osgiProfilePath));
            }
            if (!osgiProfileFile.isFile()) {
                throw new BuildException(String.format("OSGi profile path '%s' must be a file", osgiProfilePath));
            }
        }
    }

    void properties(String propertiesPath, List<PropertySet> propertySets, List<Property> properties) throws BuildException {
        if (StringUtils.hasText(propertiesPath)) {
            File propertiesFile = new File(propertiesPath);
            if (!propertiesFile.exists()) {
                throw new BuildException(String.format("Properties path '%s' does not exist", propertiesPath));
            }
            if (!propertiesFile.isFile()) {
                throw new BuildException(String.format("Properties path '%s' must be a file", propertiesPath));
            }
        }
    }
}
