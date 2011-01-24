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

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.virgo.bundlor.ClassPath;
import org.eclipse.virgo.bundlor.ClassPathEntry;

final class FileSystemClassPath implements ClassPath {

    private final File classPathRoot;

    public FileSystemClassPath(File classPathRoot) {
        this.classPathRoot = classPathRoot;
    }

    public ClassPathEntry getEntry(String name) {
        File file = new File(classPathRoot, name);
        if (file.exists()) {
            return new FileSystemClassPathEntry(this.classPathRoot, file);
        }
        return null;
    }

    public Iterator<ClassPathEntry> iterator() {
        return new FileSystemClassPathIterator(this.classPathRoot);
    }

    public void close() {
        // Nothing to close
    }

    public String toString() {
        return classPathRoot.getAbsolutePath();
    }

    private static class FileSystemClassPathIterator implements Iterator<ClassPathEntry> {

        private final Iterator<ClassPathEntry> fileSystemIterator;

        public FileSystemClassPathIterator(File root) {
            List<ClassPathEntry> entries = new ArrayList<ClassPathEntry>();
            enumerateEntries(root, root, entries);
            this.fileSystemIterator = entries.iterator();
        }

        public boolean hasNext() {
            return fileSystemIterator.hasNext();
        }

        public ClassPathEntry next() {
            return fileSystemIterator.next();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        private void enumerateEntries(File root, File orignalRootPath, List<ClassPathEntry> entries) {
            for (File file : root.listFiles()) {
                if (file.isDirectory()) {
                    enumerateEntries(file, orignalRootPath, entries);
                } else {
                    String name = file.getAbsolutePath().substring(orignalRootPath.getAbsolutePath().length());
                    name = normalizeWindowsPaths(name);
                    name = normalizeRootPaths(name);
                    entries.add(new FileSystemClassPathEntry(orignalRootPath, file));
                }
            }
        }

        private String normalizeWindowsPaths(String name) {
            return name.replace('\\', '/');
        }

        private String normalizeRootPaths(String name) {
            if (name.startsWith("/")) {
                return name.substring(1);
            }
            return name;
        }
    }
}
