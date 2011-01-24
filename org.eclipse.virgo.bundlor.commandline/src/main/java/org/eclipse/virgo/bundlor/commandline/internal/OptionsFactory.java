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

import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

public class OptionsFactory {

    @SuppressWarnings("static-access")
    public Options create() {
        Options options = new Options();

        options.addOption(OptionBuilder //
        .withArgName("path") //
        .withDescription("The path to the input source") //
        .hasArg() //
        .isRequired() //
        .create("i"));

        options.addOption(OptionBuilder //
        .withArgName("path") //
        .withDescription("The path to the output location. If unspecified then, System.out.") //
        .hasArg() //
        .create("o"));

        options.addOption(OptionBuilder //
        .withArgName("path") //
        .withDescription("The path to the manifest template file") //
        .hasArg() //
        .create("m"));

        options.addOption(OptionBuilder //
        .withArgName("path") //
        .withDescription("The path to an OSGi profile in properties file form") //
        .hasArg() //
        .create("p"));

        options.addOption(OptionBuilder //
        .withArgName("path") //
        .withDescription("The path to a file of properties for substitution in properties file form") //
        .hasArg() //
        .create("r"));

        options.addOption("f", null, false, "Fail with a non-zero exit code if warnings are returned");

        options.addOption(OptionBuilder //
        .withArgName("property=value") //
        .hasArgs(2) //
        .withValueSeparator() //
        .withDescription("A property to use for substitution") //
        .create("D"));

        return options;
    }
}
