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
import java.util.List;
import java.util.Properties;

import org.apache.maven.plugin.MojoExecutionException;
import org.eclipse.virgo.bundlor.util.StringUtils;

import org.eclipse.virgo.bundlor.maven.plugin.internal.Configuration;
import org.eclipse.virgo.bundlor.maven.plugin.internal.ConfigurationValidator;

public final class StandardConfigurationValidator implements ConfigurationValidator {

    public void validate(Configuration configuration) throws MojoExecutionException {
        inputPath(configuration.getInputPath());
        outputPath(configuration.getOutputPath());
        manifestTemplate(configuration.getManifestTemplatePath(), configuration.getManifestTemplate(), configuration.getBundleSymbolicName(),
            configuration.getBundleVersion());
        osgiProfile(configuration.getOsgiProfilePath(), configuration.getOsgiProfile());
        properties(configuration.getPropertiesPath(), configuration.getProperties());
    }

    void inputPath(String inputPath) throws MojoExecutionException {
        if (!StringUtils.hasText(inputPath)) {
            throw new MojoExecutionException("Input path is required");
        }

        File inputFile = new File(inputPath);
        if (!inputFile.exists()) {
            throw new MojoExecutionException(String.format("Input path '%s' does not exist", inputPath));
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
        throw new MojoExecutionException(String.format("Input path '%s' is not JAR, WAR, or directory", inputPath));
    }

    void outputPath(String outputPath) throws MojoExecutionException {
        if (StringUtils.hasText(outputPath)) {
            File outputFile = new File(outputPath);
            if (outputFile.isDirectory()) {
                return;
            } else if (!outputFile.getParentFile().exists()) {
                throw new MojoExecutionException(String.format("Output path's parent directory '%s' does not exist", outputFile.getParent()));
            }
            if (outputFile.getAbsolutePath().endsWith("jar")) {
                return;
            }
            if (outputFile.getAbsolutePath().endsWith("war")) {
                return;
            }
            throw new MojoExecutionException(String.format("Output path '%s' is not JAR, WAR, or directory", outputFile));
        }
    }

    void manifestTemplate(String manifestTemplatePath, String manifestTemplate, String bundleSymbolicName, String bundleVersion)
        throws MojoExecutionException {
        if (StringUtils.hasText(manifestTemplatePath)) {
            File manifestTemplateFile = new File(manifestTemplatePath);
            if (!manifestTemplateFile.exists()) {
                throw new MojoExecutionException(String.format("Manifest template path '%s' does not exist", manifestTemplatePath));
            }
            if (!manifestTemplateFile.isFile()) {
                throw new MojoExecutionException(String.format("Manifest template path '%s' must be a file", manifestTemplatePath));
            }
        }
    }

    void osgiProfile(String osgiProfilePath, String osgiProfile) throws MojoExecutionException {
        if (StringUtils.hasText(osgiProfilePath)) {
            File osgiProfileFile = new File(osgiProfilePath);
            if (!osgiProfileFile.exists()) {
                throw new MojoExecutionException(String.format("OSGi profile path '%s' does not exist", osgiProfilePath));
            }
            if (!osgiProfileFile.isFile()) {
                throw new MojoExecutionException(String.format("OSGi profile path '%s' must be a file", osgiProfilePath));
            }
        }
    }

    void properties(String propertiesPath, List<Properties> properties) throws MojoExecutionException {
        if (StringUtils.hasText(propertiesPath)) {
            File propertiesFile = new File(propertiesPath);
            if (!propertiesFile.exists()) {
                throw new MojoExecutionException(String.format("Properties path '%s' does not exist", propertiesPath));
            }
            if (!propertiesFile.isFile()) {
                throw new MojoExecutionException(String.format("Properties path '%s' must be a file", propertiesPath));
            }
        }
    }
}
