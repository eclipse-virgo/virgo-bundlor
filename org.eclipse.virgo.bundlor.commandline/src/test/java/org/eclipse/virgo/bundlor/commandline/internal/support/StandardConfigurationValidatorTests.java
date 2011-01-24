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

package org.eclipse.virgo.bundlor.commandline.internal.support;

import org.junit.Test;

public class StandardConfigurationValidatorTests {

    private final StandardConfigurationValidator validator = new StandardConfigurationValidator();

    // Input

    @Test(expected = RuntimeException.class)
    public void inputPathNull() {
        this.validator.inputPath(null);
    }

    @Test(expected = RuntimeException.class)
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

    @Test(expected = RuntimeException.class)
    public void inputPathOther() {
        this.validator.inputPath("src/test/resources/input.txt");
    }

    // Output

    @Test
    public void outputPathNull() {
        this.validator.outputPath(null);
    }

    @Test(expected = RuntimeException.class)
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

    @Test(expected = RuntimeException.class)
    public void outputPathOther() {
        this.validator.outputPath("src/test/resources/input.txt");
    }

    // Manifest Template

    @Test
    public void pathNull() {
        this.validator.manifestTemplatePath(null);
    }

    @Test(expected = RuntimeException.class)
    public void pathDoesntExist() {
        this.validator.manifestTemplatePath("src/test/resources/does-not-exist.mf");
    }

    @Test(expected = RuntimeException.class)
    public void pathDirectory() {
        this.validator.manifestTemplatePath("src/test/resources");
    }

    @Test
    public void path() {
        this.validator.manifestTemplatePath("src/test/resources/test-template.mf");
    }

    // OSGi Profile

    @Test
    public void profileNull() {
        this.validator.osgiProfilePath(null);
    }

    @Test(expected = RuntimeException.class)
    public void profileDoesNotExist() {
        this.validator.osgiProfilePath("does-not-exist.properties");
    }

    @Test(expected = RuntimeException.class)
    public void profileNotFile() {
        this.validator.osgiProfilePath("src/test/resources");
    }

    @Test
    public void profile() {
        this.validator.osgiProfilePath("src/test/resources/test.properties");
    }

    // Properties

    @Test
    public void propertiesNull() {
        this.validator.propertiesPath(null);
    }

    @Test(expected = RuntimeException.class)
    public void propertiesDoesNotExist() {
        this.validator.propertiesPath("does-not-exist.properties");
    }

    @Test(expected = RuntimeException.class)
    public void propertiesNotFile() {
        this.validator.propertiesPath("src/test/resources");
    }

    @Test
    public void properties() {
        this.validator.propertiesPath("src/test/resources/test.properties");
    }

}
