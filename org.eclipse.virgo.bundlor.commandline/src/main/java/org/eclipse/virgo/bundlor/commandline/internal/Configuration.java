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

package org.eclipse.virgo.bundlor.commandline.internal;

import java.util.Properties;

import org.apache.commons.cli.CommandLine;

public final class Configuration {

    private final String inputPath;

    private final String outputPath;

    private final String manifestTemplatePath;

    private final String osgiProfilePath;

    private final String propertiesPath;

    private final Properties properties;

    Configuration() {
        this.inputPath = "";
        this.outputPath = "";
        this.manifestTemplatePath = "";
        this.osgiProfilePath = "";
        this.propertiesPath = "";
        this.properties = new Properties();
    }

    public Configuration(CommandLine commandLine) {
        this.inputPath = commandLine.getOptionValue("i");
        this.outputPath = commandLine.getOptionValue("o");
        this.manifestTemplatePath = commandLine.getOptionValue("m");
        this.osgiProfilePath = commandLine.getOptionValue("p");
        this.propertiesPath = commandLine.getOptionValue("r");
        this.properties = commandLine.getOptionProperties("D");
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

    public String getOsgiProfilePath() {
        return osgiProfilePath;
    }

    public String getPropertiesPath() {
        return propertiesPath;
    }

    public Properties getProperties() {
        return properties;
    }
}
