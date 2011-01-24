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

import org.eclipse.virgo.bundlor.commandline.internal.Configuration;
import org.eclipse.virgo.bundlor.commandline.internal.ConfigurationValidator;

public final class StandardConfigurationValidator implements ConfigurationValidator {

    public void validate(Configuration configuration) {
        inputPath(configuration.getInputPath());
        outputPath(configuration.getOutputPath());
        manifestTemplatePath(configuration.getManifestTemplatePath());
        osgiProfilePath(configuration.getOsgiProfilePath());
        propertiesPath(configuration.getPropertiesPath());
    }

    void inputPath(String inputPath) {
        if (!StringUtils.hasText(inputPath)) {
            throw new RuntimeException("Input path is required");
        }

        File inputFile = new File(inputPath);
        if (!inputFile.exists()) {
            throw new RuntimeException(String.format("Input path '%s' does not exist", inputPath));
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
        throw new RuntimeException(String.format("Input path '%s' is not JAR, WAR, or directory", inputPath));
    }

    void outputPath(String outputPath) {
        if (StringUtils.hasText(outputPath)) {
            File outputFile = new File(outputPath);
            if (outputFile.isDirectory()) {
                return;
            } else if (!outputFile.getParentFile().exists()) {
                throw new RuntimeException(String.format("Output path's parent directory '%s' does not exist", outputFile.getParent()));
            }
            if (outputFile.getAbsolutePath().endsWith("jar")) {
                return;
            }
            if (outputFile.getAbsolutePath().endsWith("war")) {
                return;
            }
            throw new RuntimeException(String.format("Output path '%s' is not JAR, WAR, or directory", outputFile));
        }
    }

    void manifestTemplatePath(String manifestTemplatePath) {
        if (StringUtils.hasText(manifestTemplatePath)) {
            File manifestTemplateFile = new File(manifestTemplatePath);
            if (!manifestTemplateFile.exists()) {
                throw new RuntimeException(String.format("Manifest template path '%s' does not exist", manifestTemplatePath));
            }
            if (!manifestTemplateFile.isFile()) {
                throw new RuntimeException(String.format("Manifest template path '%s' must be a file", manifestTemplatePath));
            }
        }
    }

    void osgiProfilePath(String osgiProfilePath) {
        if (StringUtils.hasText(osgiProfilePath)) {
            File osgiProfileFile = new File(osgiProfilePath);
            if (!osgiProfileFile.exists()) {
                throw new RuntimeException(String.format("OSGi profile path '%s' does not exist", osgiProfilePath));
            }
            if (!osgiProfileFile.isFile()) {
                throw new RuntimeException(String.format("OSGi profile path '%s' must be a file", osgiProfilePath));
            }
        }
    }

    void propertiesPath(String propertiesPath) {
        if (StringUtils.hasText(propertiesPath)) {
            File propertiesFile = new File(propertiesPath);
            if (!propertiesFile.exists()) {
                throw new RuntimeException(String.format("Properties path '%s' does not exist", propertiesPath));
            }
            if (!propertiesFile.isFile()) {
                throw new RuntimeException(String.format("Properties path '%s' must be a file", propertiesPath));
            }
        }
    }

}
