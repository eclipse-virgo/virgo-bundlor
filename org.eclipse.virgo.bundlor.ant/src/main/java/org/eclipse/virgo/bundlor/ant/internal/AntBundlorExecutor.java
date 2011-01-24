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

import java.util.List;

import org.apache.tools.ant.BuildException;

import org.eclipse.virgo.bundlor.ClassPath;
import org.eclipse.virgo.bundlor.ManifestGenerator;
import org.eclipse.virgo.bundlor.ManifestWriter;
import org.eclipse.virgo.bundlor.ant.BundlorExecutor;
import org.eclipse.virgo.bundlor.blint.ManifestValidator;
import org.eclipse.virgo.bundlor.blint.support.DefaultManifestValidatorContributorsFactory;
import org.eclipse.virgo.bundlor.blint.support.StandardManifestValidator;
import org.eclipse.virgo.bundlor.support.DefaultManifestGeneratorContributorsFactory;
import org.eclipse.virgo.bundlor.support.ManifestGeneratorContributors;
import org.eclipse.virgo.bundlor.support.StandardManifestGenerator;
import org.eclipse.virgo.bundlor.support.classpath.ClassPathFactory;
import org.eclipse.virgo.bundlor.support.manifestwriter.ManifestWriterFactory;
import org.eclipse.virgo.bundlor.support.properties.PropertiesSource;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

public final class AntBundlorExecutor implements BundlorExecutor {

    private final ClassPath inputClassPath;

    private final ManifestWriter manifestWriter;

    private final ManifestContents manifestTemplate;

    private final ManifestGenerator manifestGenerator;

    private final ManifestValidator manifestValidator;

    public AntBundlorExecutor(Configuration configuration, ConfigurationValidator configurationValidator, ClassPathFactory classPathFactory,
        ManifestWriterFactory manifestWriterFactory, ManifestTemplateFactory manifestTemplateFactory,
        PropertiesSourceFactory propertiesSourceFactory, OsgiProfileFactory osgiProfileFactory) throws BuildException {

        configurationValidator.validate(configuration);

        this.inputClassPath = classPathFactory.create(configuration.getInputPath());
        this.manifestWriter = manifestWriterFactory.create(configuration.getInputPath(), configuration.getOutputPath());
        this.manifestTemplate = manifestTemplateFactory.create(configuration.getManifestTemplatePath(), configuration.getManifestTemplate(),
            configuration.getBundleSymbolicName(), configuration.getBundleVersion());

        List<PropertiesSource> properties = propertiesSourceFactory.create(configuration.getPropertiesPath(), configuration.getPropertySets(),
            configuration.getProperties());
        properties.add(osgiProfileFactory.create(configuration.getOsgiProfilePath(), configuration.getOsgiProfile()));
        this.manifestGenerator = new StandardManifestGenerator(createContributors(properties));

        this.manifestValidator = new StandardManifestValidator(DefaultManifestValidatorContributorsFactory.create());
    }

    /**
     * {@inheritDoc}
     */
    public List<String> execute() {
        ManifestContents manifest = this.manifestGenerator.generate(this.manifestTemplate, this.inputClassPath);

        try {
            this.manifestWriter.write(manifest);
        } finally {
            this.manifestWriter.close();
        }

        return this.manifestValidator.validate(manifest);
    }

    private ManifestGeneratorContributors createContributors(List<PropertiesSource> properties) {
        return DefaultManifestGeneratorContributorsFactory.create(properties.toArray(new PropertiesSource[properties.size()]));
    }
}
