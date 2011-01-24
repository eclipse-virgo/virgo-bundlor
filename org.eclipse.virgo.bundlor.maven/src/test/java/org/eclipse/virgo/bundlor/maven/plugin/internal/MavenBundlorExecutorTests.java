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

package org.eclipse.virgo.bundlor.maven.plugin.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;

import org.eclipse.virgo.bundlor.ClassPath;
import org.eclipse.virgo.bundlor.ClassPathEntry;
import org.eclipse.virgo.bundlor.ManifestWriter;
import org.eclipse.virgo.bundlor.support.classpath.ClassPathFactory;
import org.eclipse.virgo.bundlor.support.manifestwriter.ManifestWriterFactory;
import org.eclipse.virgo.bundlor.support.properties.EmptyPropertiesSource;
import org.eclipse.virgo.bundlor.support.properties.PropertiesSource;
import org.eclipse.virgo.bundlor.util.SimpleManifestContents;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

public class MavenBundlorExecutorTests {

    @Test
    public void execute() throws MojoExecutionException {
        new MavenBundlorExecutor(new Configuration(), new StubConfigurationValidator(), new StubClassPathFactory(), new StubManifestWriterFactory(),
            new StubManifestTemplateFactory(), new StubPropertiesSourceFactory(), new StubOsgiProfileFactory()).execute();
    }

    private static class StubConfigurationValidator implements ConfigurationValidator {

        public void validate(Configuration configuration) throws MojoExecutionException {
        }

    }

    private static class StubClassPathFactory implements ClassPathFactory {

        public ClassPath create(String inputPath) {
            return new StubClassPath();
        }

    }

    private static class StubClassPath implements ClassPath {

        public ClassPathEntry getEntry(String name) {
            return null;
        }

        public Iterator<ClassPathEntry> iterator() {
            return new Iterator<ClassPathEntry>() {

                public boolean hasNext() {
                    return false;
                }

                public ClassPathEntry next() {
                    return null;
                }

                public void remove() {
                }

            };
        }

        public void close() {
        }
    }

    private static class StubManifestWriterFactory implements ManifestWriterFactory {

        public ManifestWriter create(String inputPath, String outputPath) {
            return new StubManifestWriter();
        }

    }

    private static class StubManifestWriter implements ManifestWriter {

        public void write(ManifestContents manifest) {
        }

        public void close() {
        }

    }

    private static class StubManifestTemplateFactory implements ManifestTemplateFactory {

        public ManifestContents create(String manifestTemplatePath, String manifestTemplate, String bundleSymbolicName,
            String defaultBundleSymbolicName, String bundleVersion, String defaultBundleVersion) {
            return new SimpleManifestContents();
        }

    }

    private static class StubPropertiesSourceFactory implements PropertiesSourceFactory {

        public List<PropertiesSource> create(String propertiesPath, List<Properties> propertySets) {
            return new ArrayList<PropertiesSource>();
        }

    }

    private static class StubOsgiProfileFactory implements OsgiProfileFactory {

        public PropertiesSource create(String osgiProfilePath, String osgiProfile) {
            return new EmptyPropertiesSource();
        }

    }

}
