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

package org.eclipse.virgo.bundlor.support.classpath;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.eclipse.virgo.bundlor.ClassPath;

public class StandardClassPathFactoryTests {

    private final StandardClassPathFactory factory = new StandardClassPathFactory();

    @Test
    public void directory() {
        ClassPath classPath = this.factory.create("src/test/resources");
        assertTrue(classPath instanceof FileSystemClassPath);
    }

    @Test
    public void file() {
        ClassPath classPath = this.factory.create("src/test/resources/input.jar");
        assertTrue(classPath instanceof JarFileClassPath);
    }
}
