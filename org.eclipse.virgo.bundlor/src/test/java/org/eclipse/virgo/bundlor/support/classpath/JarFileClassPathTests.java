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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Iterator;
import java.util.jar.JarFile;

import org.junit.Test;

import org.eclipse.virgo.bundlor.ClassPathEntry;

public class JarFileClassPathTests {

    private final JarFileClassPath classPath;

    public JarFileClassPathTests() throws IOException {
        classPath = new JarFileClassPath(new JarFile("src/test/resources/input.jar"));
    }

    @Test
    public void entry() {
        assertTrue(this.classPath.getEntry("META-INF/MANIFEST.MF") instanceof JarFileClassPathEntry);
        assertNull(this.classPath.getEntry("test.properties"));
    }

    @Test
    public void iterator() {
        Iterator<ClassPathEntry> i = this.classPath.iterator();
        assertTrue(i.hasNext());
        ClassPathEntry entry = i.next();
        assertNotNull(entry);
        assertTrue(entry instanceof JarFileClassPathEntry);
    }

    @Test
    public void classPathEntry() {
        ClassPathEntry entry = this.classPath.getEntry("META-INF/MANIFEST.MF");
        assertNotNull(entry.getInputStream());
        assertNotNull(entry.getReader());
        assertFalse(entry.isDirectory());
        assertEquals("META-INF/MANIFEST.MF", entry.getName());
        assertTrue(this.classPath.getEntry("META-INF/").isDirectory());
    }
}
