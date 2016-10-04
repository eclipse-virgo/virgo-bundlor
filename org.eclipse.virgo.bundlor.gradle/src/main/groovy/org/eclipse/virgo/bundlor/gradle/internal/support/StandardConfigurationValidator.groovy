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
package org.eclipse.virgo.bundlor.gradle.internal.support

import org.eclipse.virgo.bundlor.gradle.internal.Configuration
import org.eclipse.virgo.bundlor.gradle.internal.ConfigurationValidator
import org.eclipse.virgo.bundlor.util.StringUtils
import org.gradle.api.GradleException

class StandardConfigurationValidator implements ConfigurationValidator {

	@Override
	public void validate(Configuration configuration) {
		if(!configuration){
			throw new GradleException("configuration is required")
		}
		inputPath(configuration.getInputPath())
		outputPath(configuration.getOutputPath())
		manifestTemplate(configuration.getManifestTemplatePath(), configuration.getManifestTemplate())
		osgiProfile(configuration.getOsgiProfilePath())
		properties(configuration.getPropertiesPath())
	}
	
	void inputPath(String inputPath) {
		if(!StringUtils.hasText(inputPath)) {
			throw new GradleException("inputPath is required")
		}
		
		File inputFile = new File(inputPath);
		if (!inputFile.exists()) {
			throw new GradleException(String.format("Input path '%s' does not exist", inputPath))
		}
		if (inputFile.isDirectory()) {
			return
		}
		if (inputFile.getAbsolutePath().endsWith("jar")) {
			return
		}
		if (inputFile.getAbsolutePath().endsWith("war")) {
			return
		}
		throw new GradleException(String.format("Input path '%s' is not JAR, WAR, or directory", inputPath))
	}
	
	void outputPath(String outputPath) {
		if (StringUtils.hasText(outputPath)) {
			File outputFile = new File(outputPath);
			if (outputFile.isDirectory()) {
				return;
			} else if (!outputFile.getParentFile().exists()) {
				throw new GradleException(String.format("Output path's parent directory '%s' does not exist", 
					outputFile.getParent()));
			}
			if (outputFile.getAbsolutePath().endsWith("jar")) {
				return;
			}
			if (outputFile.getAbsolutePath().endsWith("war")) {
				return;
			}
			throw new GradleException(String.format("Output path '%s' is not JAR, WAR, or directory", outputFile));
		}
	}
	
	void manifestTemplate(String manifestTemplatePath, String manifestTemplate) {
		if (!StringUtils.hasText(manifestTemplate) && StringUtils.hasText(manifestTemplatePath)) {
			File manifestTemplateFile = new File(manifestTemplatePath);
			if (!manifestTemplateFile.exists()) {
				throw new GradleException(String.format("Manifest template path '%s' does not exist", 
					manifestTemplatePath));
			}
			if (!manifestTemplateFile.isFile()) {
				throw new GradleException(String.format("Manifest template path '%s' must be a file", 
					manifestTemplatePath));
			}
		}
	}
	
	void osgiProfile(String osgiProfilePath) {
		if (StringUtils.hasText(osgiProfilePath)) {
			File osgiProfileFile = new File(osgiProfilePath);
			if (!osgiProfileFile.exists()) {
				throw new GradleException(String.format("OSGi profile path '%s' does not exist", osgiProfilePath));
			}
			if (!osgiProfileFile.isFile()) {
				throw new GradleException(String.format("OSGi profile path '%s' must be a file", osgiProfilePath));
			}
		}
	}

	void properties(String propertiesPath) {
		if (StringUtils.hasText(propertiesPath)) {
			File propertiesFile = new File(propertiesPath);
			if (!propertiesFile.exists()) {
				throw new GradleException(String.format("Properties path '%s' does not exist", propertiesPath));
			}
			if (!propertiesFile.isFile()) {
				throw new GradleException(String.format("Properties path '%s' must be a file", propertiesPath));
			}
		}
	}
}
