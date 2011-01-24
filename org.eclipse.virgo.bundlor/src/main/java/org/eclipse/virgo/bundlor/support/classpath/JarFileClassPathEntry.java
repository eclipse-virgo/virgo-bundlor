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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.virgo.bundlor.ClassPathEntry;

final class JarFileClassPathEntry implements ClassPathEntry {

    private final JarFile jarFile;

    private final JarEntry entry;

    public JarFileClassPathEntry(JarFile jarFile, JarEntry entry) {
        this.jarFile = jarFile;
        this.entry = entry;
    }

    public InputStream getInputStream() {
        try {
            return this.jarFile.getInputStream(this.entry);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Reader getReader() {
        return new InputStreamReader(getInputStream());
    }

    public boolean isDirectory() {
        return this.entry.isDirectory();
    }

    public String getName() {
        return this.entry.getName();
    }

    public String toString() {
        return getName();
    }

}
