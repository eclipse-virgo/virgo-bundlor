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

package org.eclipse.virgo.bundlor.ant.internal.support;

import org.apache.tools.ant.BuildException;
import org.junit.Test;

public class StandardConfigurationValidatorTests {

    private final StandardConfigurationValidator validator = new StandardConfigurationValidator();

    // Input

    @Test(expected = BuildException.class)
    public void inputPathNull() {
        this.validator.inputPath(null);
    }

    @Test(expected = BuildException.class)
    public void inputPathDoesntExist() {
        this.validator.inputPath("src/test/resources/does-not-exist");
    }

    @Test
    public void inputPathDirectory() {
        this.validator.inputPath("src/test/resources");
    }

    @Test
    public void inputPathJar() {
        this.validator.inputPath("src/test/resources/input.jar");
    }

    @Test
    public void inputPathWar() {
        this.validator.inputPath("src/test/resources/input.war");
    }

    @Test(expected = BuildException.class)
    public void inputPathOther() {
        this.validator.inputPath("src/test/resources/input.txt");
    }

    // Output

    @Test
    public void outputPathNull() {
        this.validator.outputPath(null);
    }

    @Test(expected = BuildException.class)
    public void outputPathParentDoesntExist() {
        this.validator.outputPath("src/test/resources/does-not-exist/output.jar");
    }

    @Test
    public void outputPathDirectory() {
        this.validator.outputPath("src/test/resources");
    }

    @Test
    public void outputPathJar() {
        this.validator.outputPath("src/test/resources/input.jar");
    }

    @Test
    public void outputPathWar() {
        this.validator.outputPath("src/test/resources/input.war");
    }

    @Test(expected = BuildException.class)
    public void outputPathOther() {
        this.validator.outputPath("src/test/resources/input.txt");
    }

    // Manifest Template

    @Test
    public void inline() {
        this.validator.manifestTemplate(null, "Manifest-Version:1.0", "test.symbolic.name", "test.version");
    }

    @Test(expected = BuildException.class)
    public void pathDoesntExist() {
        this.validator.manifestTemplate("src/test/resources/does-not-exist.mf", null, "test.symbolic.name", "test.version");
    }

    @Test(expected = BuildException.class)
    public void pathDirectory() {
        this.validator.manifestTemplate("src/test/resources", null, "test.symbolic.name", "test.version");
    }

    @Test
    public void path() {
        this.validator.manifestTemplate("src/test/resources/test-template.mf", null, "test.symbolic.name", "test.version");
    }

    // OSGi Profile

    @Test
    public void profileNull() {
        this.validator.osgiProfile(null, null);
    }

    @Test
    public void profileInline() {
        this.validator.osgiProfile(null, "test.property=test.value");
    }

    @Test(expected = BuildException.class)
    public void profileDoesNotExist() {
        this.validator.osgiProfile("does-not-exist.properties", null);
    }

    @Test(expected = BuildException.class)
    public void profileNotFile() {
        this.validator.osgiProfile("src/test/resources", null);
    }

    @Test
    public void profile() {
        this.validator.osgiProfile("src/test/resources/test.properties", null);
    }

    // Properties

    @Test
    public void propertiesNull() {
        this.validator.properties(null, null, null);
    }

    @Test(expected = BuildException.class)
    public void propertiesDoesNotExist() {
        this.validator.properties("does-not-exist.properties", null, null);
    }

    @Test(expected = BuildException.class)
    public void propertiesNotFile() {
        this.validator.properties("src/test/resources", null, null);
    }

    @Test
    public void properties() {
        this.validator.properties("src/test/resources/test.properties", null, null);
    }

}
