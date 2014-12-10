package org.eclipse.virgo.bundlor

class BundlorPluginExtension {

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

    File outputDir
}
