/*******************************************************************************
 * Copyright (c) 2012 VMware Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   VMware Inc. - initial contribution
 *******************************************************************************/
package org.eclipse.virgo.bundlor.gradle

/**
 * @author Chris Beams
 * @author Luke Taylor
 * @author Daniel Marthaler
 */
class BundlorGradlePluginExtension {

    String bundlorVersion

    boolean enabled = true
    boolean failOnWarnings = true
    File propertiesPath

    String bundleManifestVersion = '2'

    String bundleName
    String bundleVersion
    String bundleSymbolicName
    String bundleVendor

    String manifestTemplatePath
    String manifestTemplate

    List importTemplate = []
    List exportTemplate = []
    List excludedImports = []
    List excludedExports = []

    File inputPath
    File outputDir
}
