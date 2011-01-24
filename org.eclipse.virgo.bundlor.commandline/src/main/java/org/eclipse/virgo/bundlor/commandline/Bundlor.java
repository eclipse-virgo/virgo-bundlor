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

package org.eclipse.virgo.bundlor.commandline;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import org.eclipse.virgo.bundlor.commandline.internal.CommandLineBundlorExecutor;
import org.eclipse.virgo.bundlor.commandline.internal.Configuration;
import org.eclipse.virgo.bundlor.commandline.internal.OptionsFactory;
import org.eclipse.virgo.bundlor.commandline.internal.support.StandardConfigurationValidator;
import org.eclipse.virgo.bundlor.commandline.internal.support.StandardManifestTemplateFactory;
import org.eclipse.virgo.bundlor.commandline.internal.support.StandardOsgiProfileFactory;
import org.eclipse.virgo.bundlor.commandline.internal.support.StandardPropertiesSourceFactory;
import org.eclipse.virgo.bundlor.support.classpath.StandardClassPathFactory;
import org.eclipse.virgo.bundlor.support.manifestwriter.StandardManifestWriterFactory;

public class Bundlor {

    private static final Options OPTIONS = new OptionsFactory().create();

    public final static void main(String[] args) {
        new Bundlor().run(args);
    }

    final void run(String[] args) {
        CommandLine commandLine = null;
        Configuration configuration = null;
        try {
            commandLine = new PosixParser().parse(OPTIONS, args);
            configuration = new Configuration(commandLine);
        } catch (ParseException e) {
            new HelpFormatter().printHelp("bundlor.[sh | bat] [OPTION]...", OPTIONS);
            exit(-1);
        }

        List<String> warnings = getBundlorExecutor(configuration).execute();

        if (warnings.size() > 0) {
            System.out.println("Bundlor Warnings:");
            for (String warning : warnings) {
                System.out.println("    " + warning);
            }

            if (commandLine.hasOption("f")) {
                String message = String.format("Bundlor returned warnings.  Please fix manifest template at '%s' and try again.",
                    configuration.getManifestTemplatePath());
                System.err.println(message);
                exit(-1);
            }
        }
    }

    protected BundlorExecutor getBundlorExecutor(Configuration configuration) {
        return new CommandLineBundlorExecutor(configuration, new StandardConfigurationValidator(), new StandardClassPathFactory(),
            new StandardManifestWriterFactory(), new StandardManifestTemplateFactory(), new StandardPropertiesSourceFactory(),
            new StandardOsgiProfileFactory());
    }

    protected void exit(int code) {
        System.exit(code);
    }

}
