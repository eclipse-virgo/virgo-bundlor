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

package org.eclipse.virgo.bundlor.maven.plugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.eclipse.virgo.bundlor.util.StringUtils;

import org.eclipse.virgo.bundlor.maven.plugin.internal.Configuration;
import org.eclipse.virgo.bundlor.maven.plugin.internal.MavenBundlorExecutor;
import org.eclipse.virgo.bundlor.maven.plugin.internal.MavenVersionNumberConverter;
import org.eclipse.virgo.bundlor.maven.plugin.internal.support.StandardConfigurationValidator;
import org.eclipse.virgo.bundlor.maven.plugin.internal.support.StandardManifestTemplateFactory;
import org.eclipse.virgo.bundlor.maven.plugin.internal.support.StandardOsgiProfileFactory;
import org.eclipse.virgo.bundlor.maven.plugin.internal.support.StandardPropertiesSourceFactory;
import org.eclipse.virgo.bundlor.support.classpath.StandardClassPathFactory;
import org.eclipse.virgo.bundlor.support.manifestwriter.StandardManifestWriterFactory;

/**
 * Goal that runs Bundlor against a Maven project
 * 
 * @goal bundlor
 * 
 * @phase compile
 */
public class BundlorMojo extends AbstractMojo {

    private final Configuration configuration = new Configuration();

    /**
     * @parameter
     */
    private volatile boolean failOnWarnings = false;

    public final void setFailOnWarnings(boolean failOnWarnings) {
        this.failOnWarnings = failOnWarnings;
    }

    /**
     * @parameter
     */
    private volatile boolean enabled = true;

    public final void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    private static final Set<String> IGNORED_PACKAGINGS = new HashSet<String>(Arrays.asList(new String[]{"pom"}));

    /**
     * @parameter expression="${project.packaging}"
     */
    private volatile String packaging;

    public final void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    /**
     * @parameter default-value="${project.build.outputDirectory}"
     */
    @SuppressWarnings("unused")
    private volatile String inputPath;

    public final void setInputPath(String inputPath) {
        this.configuration.setInputPath(inputPath);
    }

    /**
     * @parameter default-value="${project.build.outputDirectory}"
     */
    @SuppressWarnings("unused")
    private volatile String outputPath;

    public final void setOutputPath(String outputPath) {
        this.configuration.setOutputPath(outputPath);
    }

    /**
     * @parameter default-value="${basedir}/template.mf"
     */
    @SuppressWarnings("unused")
    private volatile String manifestTemplatePath;

    public final void setManifestTemplatePath(String manifestTemplatePath) {
        this.configuration.setManifestTemplatePath(manifestTemplatePath);
    }

    /**
     * @parameter
     */
    @SuppressWarnings("unused")
    private volatile String manifestTemplate;

    public final void setManifestTemplate(String manifestTemplate) {
        this.configuration.setManifestTemplate(manifestTemplate);
    }

    /**
     * @parameter
     */
    @SuppressWarnings("unused")
    private volatile String osgiProfilePath;

    public final void setOsgiProfilePath(String osgiProfilePath) {
        this.configuration.setOsgiProfilePath(osgiProfilePath);
    }

    /**
     * @parameter
     */
    @SuppressWarnings("unused")
    private volatile String osgiProfile;

    public final void setOsgiProfile(String osgiProfile) {
        this.configuration.setOsgiProfile(osgiProfile);
    }

    /**
     * @parameter
     */
    @SuppressWarnings("unused")
    private volatile String bundleSymbolicName;

    public final void setBundleSymbolicName(String bundleSymbolicName) {
        this.configuration.setBundleSymbolicName(bundleSymbolicName);
    }
    
    /**
     * @parameter default-value="${project.artifactId}"
     */
    @SuppressWarnings("unused")
    private volatile String defaultBundleSymbolicName;

    public final void setDefaultBundleSymbolicName(String bundleSymbolicName) {
        this.configuration.setDefaultBundleSymbolicName(bundleSymbolicName);
    }

    /**
     * @parameter
     */
    @SuppressWarnings("unused")
    private volatile String bundleVersion;

    public final void setBundleVersion(String bundleVersion) {
        this.configuration.setBundleVersion(new MavenVersionNumberConverter().convertToOsgi(bundleVersion));
    }
    
    /**
     * @parameter default-value="${project.version}"
     */
    @SuppressWarnings("unused")
    private volatile String defaultBundleVersion;

    public final void setDefaultBundleVersion(String bundleVersion) {
        this.configuration.setDefaultBundleVersion(bundleVersion);
    }

    /**
     * @parameter
     */
    @SuppressWarnings("unused")
    private volatile String propertiesPath;

    public final void setPropertiesPath(String propertiesPath) {
        this.configuration.setPropertiesPath(propertiesPath);
    }

    /**
     * @parameter default-value="${project.properties}"
     */
    @SuppressWarnings("unused")
    private volatile Properties properties;

    public final void setProperties(Properties properties) {
        this.configuration.addProperties(properties);
    }

    /**
     * @parameter expression="${project.groupId}"
     */
    private volatile String groupId;

    /**
     * @parameter expression="${project.artifactId}"
     */
    private volatile String artifactId;

    /**
     * @parameter expression="${project.version}"
     */
    private volatile String version;

    public final void execute() throws MojoExecutionException {
        if (!this.enabled) {
            getLog().info("Ignored project with enabled = false");
            return;
        }

        if (IGNORED_PACKAGINGS.contains(this.packaging)) {
            getLog().info(String.format("Ignored project with type = '%s'", this.packaging));
            return;
        }

        this.configuration.addProperties(createCommonProperties());

        List<String> warnings = getBundlorExecutor().execute();

        if (warnings.size() > 0) {
            getLog().warn("Bundlor Warnings:");
            for (String warning : warnings) {
                getLog().warn("    " + warning);
            }

            if (this.failOnWarnings) {
                String message;
                if (StringUtils.hasText(this.configuration.getManifestTemplatePath())) {
                    message = String.format("Bundlor returned warnings.  Please fix manifest template at '%s' and try again.",
                        this.configuration.getManifestTemplatePath());
                } else {
                    message = "Bundlor returned warnings.  Please fix inline manifest template and try again.";
                }

                throw new MojoExecutionException(message);
            }
        }

    }

    protected BundlorExecutor getBundlorExecutor() throws MojoExecutionException {
        return new MavenBundlorExecutor(this.configuration, new StandardConfigurationValidator(), new StandardClassPathFactory(),
            new StandardManifestWriterFactory(), new StandardManifestTemplateFactory(), new StandardPropertiesSourceFactory(),
            new StandardOsgiProfileFactory());
    }

    private Properties createCommonProperties() {
        Properties p = new Properties();
        if (groupId != null) {
            p.put("pom.groupId", groupId);
            p.put("project.groupId", groupId);
        }

        if (artifactId != null) {
            p.put("pom.artifactId", artifactId);
            p.put("project.artifactId", artifactId);
            p.put("project.name", artifactId);
        }

        if (version != null) {
            p.put("pom.version", version);
            p.put("project.version", version);
            p.put("version", version);
        }

        return p;
    }
}
