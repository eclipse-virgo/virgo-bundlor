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
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.virgo.bundlor.ClassPath;
import org.eclipse.virgo.bundlor.ClassPathEntry;

final class JarFileClassPath implements ClassPath {

    private final JarFile jarFile;

    public JarFileClassPath(JarFile jarFile) {
        this.jarFile = jarFile;
    }

    public Iterator<ClassPathEntry> iterator() {
        return new JarFileClassPathIterator(this.jarFile);
    }

    public ClassPathEntry getEntry(String name) {
        JarEntry entry = this.jarFile.getJarEntry(name);
        if (entry != null) {
            return new JarFileClassPathEntry(this.jarFile, entry);
        }
        return null;
    }

    public void close() {
        try {
            this.jarFile.close();
        } catch (IOException e) {
            // Nothing to do here
        }
    }

    public String toString() {
        return jarFile.getName();
    }

    private static class JarFileClassPathIterator implements Iterator<ClassPathEntry> {

        private final JarFile jarFile;

        private final Enumeration<JarEntry> jarEntryEnumeration;

        public JarFileClassPathIterator(JarFile jarFile) {
            this.jarFile = jarFile;
            this.jarEntryEnumeration = jarFile.entries();
        }

        public boolean hasNext() {
            return this.jarEntryEnumeration.hasMoreElements();
        }

        public ClassPathEntry next() {
            return new JarFileClassPathEntry(this.jarFile, this.jarEntryEnumeration.nextElement());
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }
}
