/*
 * This file is part of the Eclipse Virgo project.
 *
 * Copyright (c) 2016 ISPIN AG
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Daniel Marthaler - initial contribution
 */
package org.eclipse.virgo.bundlor.gradle.internal

import static org.junit.Assert.*;

import org.eclipse.virgo.bundlor.ClassPath
import org.eclipse.virgo.bundlor.ClassPathEntry
import org.eclipse.virgo.bundlor.ManifestWriter
import org.eclipse.virgo.bundlor.gradle.BundlorExecutor
import org.eclipse.virgo.bundlor.support.classpath.ClassPathFactory
import org.eclipse.virgo.bundlor.support.manifestwriter.ManifestWriterFactory
import org.eclipse.virgo.bundlor.support.properties.EmptyPropertiesSource
import org.eclipse.virgo.bundlor.support.properties.PropertiesSource
import org.eclipse.virgo.bundlor.util.SimpleManifestContents
import org.eclipse.virgo.util.parser.manifest.ManifestContents
import org.junit.After
import org.junit.Before
import org.junit.Test

class GradleBundlorExecutorTests {

	private BundlorExecutor bundlorExecutor

	@Before
	public void before() {
		bundlorExecutor = new GradleBundlorExecutor(new Configuration(), new StubConfigurationValidator(),
				new StubClassPathFactory(), new StubManifestWriterFactory(), new StubManifestTemplateFactory(),
				new StubPropertiesSourceFactory(), new StubOsgiProfileFactory())
	}

	@After
	public void after() {
		bundlorExecutor = null
	}

	@Test
	public void testExecute() {
		def warnings = bundlorExecutor.execute()
		assertTrue(warnings.size() == 2)
	}

	private static class StubConfigurationValidator implements ConfigurationValidator {
		public void validate(Configuration configuration) {
		}
	}

	private static class StubClassPathFactory implements ClassPathFactory {
		public ClassPath create(String inputPath) {
			return new StubClassPath()
		}
	}

	private static class StubClassPath implements ClassPath {
		public ClassPathEntry getEntry(String name) {
			return null
		}
		public Iterator<ClassPathEntry> iterator() {
			return new Iterator<ClassPathEntry>() {

						public boolean hasNext() {
							return false
						}

						public ClassPathEntry next() {
							return null
						}

						public void remove() {
						}
					}
		}
		public void close() {
		}
	}

	private static class StubManifestWriterFactory implements ManifestWriterFactory {
		public ManifestWriter create(String inputPath, String outputPath) {
			return new StubManifestWriter()
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
			return new SimpleManifestContents()
		}
	}

	private static class StubPropertiesSourceFactory implements PropertiesSourceFactory {
		public List<PropertiesSource> create(String propertiesPath, List<Properties> propertySets) {
			return new ArrayList<PropertiesSource>()
		}
	}

	private static class StubOsgiProfileFactory implements OsgiProfileFactory {
		public PropertiesSource create(String osgiProfilePath, String osgiProfile) {
			return new EmptyPropertiesSource()
		}
	}
}
