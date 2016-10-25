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

package org.eclipse.virgo.bundlor.support.contributors;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.osgi.framework.Constants;

import org.eclipse.virgo.bundlor.ClassPath;
import org.eclipse.virgo.bundlor.ClassPathEntry;
import org.eclipse.virgo.bundlor.support.ArtifactAnalyzer;
import org.eclipse.virgo.bundlor.support.ManifestContributor;
import org.eclipse.virgo.bundlor.support.partialmanifest.PartialManifest;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

public class BundleClassPathArtifactAnalyzer implements ArtifactAnalyzer, ManifestContributor {

    private final List<String> bundleClassPaths = new ArrayList<String>();

    private final List<ArtifactAnalyzer> artifactAnalyzers;

    private final Object monitor = new Object();

    public BundleClassPathArtifactAnalyzer(List<ArtifactAnalyzer> artifactAnalyzers) {
        this.artifactAnalyzers = artifactAnalyzers;
    }

    public void analyse(InputStream artifact, String artefactName, PartialManifest partialManifest) throws Exception {
        synchronized (this.monitor) {
            this.bundleClassPaths.add(artefactName.replace('\\', '/'));

            JarInputStream in = null;
            try {
                in = new JarInputStream(artifact);
                analyzeEntries(new JarInputStreamClassPath(in), partialManifest);
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        }
    }

    public boolean canAnalyse(String artefactName) {
        return artefactName.endsWith(".jar");
    }

    public void contribute(ManifestContents manifest) {
        if (bundleClassPaths.size() > 0) {
            StringBuilder sb = new StringBuilder(".");
            for (String bundleClassPath : bundleClassPaths) {
                sb.append(String.format(",%s", bundleClassPath));
            }
            manifest.getMainAttributes().put(Constants.BUNDLE_CLASSPATH, sb.toString());
        }
    }

    private void analyzeEntries(ClassPath classPath, PartialManifest partialManifest) {
        for (ClassPathEntry classPathEntry : classPath) {
            if (!classPathEntry.isDirectory()) {
                analyzeEntry(classPathEntry, partialManifest);
            }
        }
    }

    private void analyzeEntry(ClassPathEntry classPathEntry, PartialManifest partialManifest) {
        for (ArtifactAnalyzer artifactAnalyzer : this.artifactAnalyzers) {
            if (artifactAnalyzer.canAnalyse(classPathEntry.getName())) {
                InputStream inputStream = classPathEntry.getInputStream();
                try {
                    artifactAnalyzer.analyse(inputStream, classPathEntry.getName(), partialManifest);
                } catch (Exception e) {
                    // Swallow exception to allow other analyzers to proceed
                    e.printStackTrace();
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            // Nothing to do
                        }
                    }
                }
            }
        }
    }

    private static class JarInputStreamClassPath implements ClassPath {

        private final JarInputStream jarInputStream;

        public JarInputStreamClassPath(JarInputStream jarInputStream) {
            this.jarInputStream = jarInputStream;
        }

        public Iterator<ClassPathEntry> iterator() {
            return new JarInputStreamClassPathIterator(this.jarInputStream);
        }

        public ClassPathEntry getEntry(String name) {
            throw new UnsupportedOperationException();
        }

        public void close() {
            try {
                this.jarInputStream.close();
            } catch (IOException e) {
                // Nothing to do here
            }
        }

        private static class JarInputStreamClassPathIterator implements Iterator<ClassPathEntry> {

            private final JarInputStream jarInputStream;

            private volatile ClassPathEntry nextEntry;

            private final Object monitor = new Object();

            public JarInputStreamClassPathIterator(JarInputStream jarInputStream) {
                this.jarInputStream = jarInputStream;
            }

            public boolean hasNext() {
                synchronized (this.monitor) {
                    try {
                        if (this.nextEntry == null) {
                            JarEntry entry = jarInputStream.getNextJarEntry();
                            if (entry != null) {
                                this.nextEntry = new JarInputStreamClassPathEntry(this.jarInputStream, entry);
                            }
                        }
                        return this.nextEntry != null;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            public ClassPathEntry next() {
                synchronized (this.monitor) {
                    ClassPathEntry entry = this.nextEntry;
                    this.nextEntry = null;
                    return entry;
                }
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }

        }
    }

    private static class JarInputStreamClassPathEntry implements ClassPathEntry {

        private static final String UTF_8 = "UTF-8";

        private final byte[] contents;

        private final JarEntry entry;

        public JarInputStreamClassPathEntry(JarInputStream jarInputStream, JarEntry entry) {
            this.contents = getEntryContents(jarInputStream);
            this.entry = entry;
        }

        public InputStream getInputStream() {
            return new ByteArrayInputStream(this.contents);
        }

        public String getName() {
            return this.entry.getName();
        }

        public Reader getReader() {
            return new InputStreamReader(getInputStream(), Charset.forName(UTF_8));
        }

        public boolean isDirectory() {
            return this.entry.isDirectory();
        }

        public String toString() {
            return getName();
        }

        private byte[] getEntryContents(JarInputStream jarInputStream) {
            ByteArrayOutputStream contents = new ByteArrayOutputStream();
            try {
                byte[] buffer = new byte[8192];
                int length = 0;
                while ((length = jarInputStream.read(buffer)) != -1) {
                    contents.write(buffer, 0, length);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return contents.toByteArray();
        }

    }
}
