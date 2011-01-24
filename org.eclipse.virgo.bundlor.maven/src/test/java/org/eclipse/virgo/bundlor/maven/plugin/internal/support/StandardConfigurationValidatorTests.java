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

package org.eclipse.virgo.bundlor.maven.plugin.internal.support;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;

public class StandardConfigurationValidatorTests {

    private final StandardConfigurationValidator validator = new StandardConfigurationValidator();

    // Input

    @Test(expected = MojoExecutionException.class)
    public void inputPathNull() throws MojoExecutionException {
        this.validator.inputPath(null);
    }

    @Test(expected = MojoExecutionException.class)
    public void inputPathDoesntExist() throws MojoExecutionException {
        this.validator.inputPath("src/test/resources/does-not-exist");
    }

    @Test
    public void inputPathDirectory() throws MojoExecutionException {
        this.validator.inputPath("src/test/resources");
    }

    @Test
    public void inputPathJar() throws MojoExecutionException {
        this.validator.inputPath("src/test/resources/input.jar");
    }

    @Test
    public void inputPathWar() throws MojoExecutionException {
        this.validator.inputPath("src/test/resources/input.war");
    }

    @Test(expected = MojoExecutionException.class)
    public void inputPathOther() throws MojoExecutionException {
        this.validator.inputPath("src/test/resources/input.txt");
    }

    // Output

    @Test
    public void outputPathNull() throws MojoExecutionException {
        this.validator.outputPath(null);
    }

    @Test(expected = MojoExecutionException.class)
    public void outputPathParentDoesntExist() throws MojoExecutionException {
        this.validator.outputPath("src/test/resources/does-not-exist/output.jar");
    }

    @Test
    public void outputPathDirectory() throws MojoExecutionException {
        this.validator.outputPath("src/test/resources");
    }

    @Test
    public void outputPathJar() throws MojoExecutionException {
        this.validator.outputPath("src/test/resources/input.jar");
    }

    @Test
    public void outputPathWar() throws MojoExecutionException {
        this.validator.outputPath("src/test/resources/input.war");
    }

    @Test(expected = MojoExecutionException.class)
    public void outputPathOther() throws MojoExecutionException {
        this.validator.outputPath("src/test/resources/input.txt");
    }

    // Manifest Template

    @Test
    public void inline() throws MojoExecutionException {
        this.validator.manifestTemplate(null, "Manifest-Version:1.0", "test.symbolic.name", "test.version");
    }

    @Test(expected = MojoExecutionException.class)
    public void pathDoesntExist() throws MojoExecutionException {
        this.validator.manifestTemplate("src/test/resources/does-not-exist.mf", null, "test.symbolic.name", "test.version");
    }

    @Test(expected = MojoExecutionException.class)
    public void pathDirectory() throws MojoExecutionException {
        this.validator.manifestTemplate("src/test/resources", null, "test.symbolic.name", "test.version");
    }

    @Test
    public void path() throws MojoExecutionException {
        this.validator.manifestTemplate("src/test/resources/test-template.mf", null, "test.symbolic.name", "test.version");
    }

    // OSGi Profile

    @Test
    public void profileNull() throws MojoExecutionException {
        this.validator.osgiProfile(null, null);
    }

    @Test
    public void profileInline() throws MojoExecutionException {
        this.validator.osgiProfile(null, "test.property=test.value");
    }

    @Test(expected = MojoExecutionException.class)
    public void profileDoesNotExist() throws MojoExecutionException {
        this.validator.osgiProfile("does-not-exist.properties", null);
    }

    @Test(expected = MojoExecutionException.class)
    public void profileNotFile() throws MojoExecutionException {
        this.validator.osgiProfile("src/test/resources", null);
    }

    @Test
    public void profile() throws MojoExecutionException {
        this.validator.osgiProfile("src/test/resources/test.properties", null);
    }

    // Properties

    @Test
    public void propertiesNull() throws MojoExecutionException {
        this.validator.properties(null, null);
    }

    @Test(expected = MojoExecutionException.class)
    public void propertiesDoesNotExist() throws MojoExecutionException {
        this.validator.properties("does-not-exist.properties", null);
    }

    @Test(expected = MojoExecutionException.class)
    public void propertiesNotFile() throws MojoExecutionException {
        this.validator.properties("src/test/resources", null);
    }

    @Test
    public void properties() throws MojoExecutionException {
        this.validator.properties("src/test/resources/test.properties", null);
    }

}
