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

package org.eclipse.virgo.bundlor.diff;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.eclipse.virgo.util.osgi.manifest.BundleManifest;
import org.eclipse.virgo.util.osgi.manifest.ExportedPackage;
import org.eclipse.virgo.util.osgi.manifest.ImportedPackage;
import org.eclipse.virgo.util.osgi.manifest.internal.StandardBundleManifest;
import org.eclipse.virgo.util.osgi.manifest.parse.ParserLogger;

public class BundleDiffer {

    private static void diff(File bundle1, File bundle2) throws IOException {
        BundleManifest bundleManifest1 = createManifest(bundle1);
        BundleManifest bundleManifest2 = createManifest(bundle2);

        List<ImportedPackage> importPackages1 = bundleManifest1.getImportPackage().getImportedPackages();
        List<ImportedPackage> importPackages2 = bundleManifest2.getImportPackage().getImportedPackages();

        List<ExportedPackage> exportPackages1 = bundleManifest1.getExportPackage().getExportedPackages();
        List<ExportedPackage> exportPackages2 = bundleManifest2.getExportPackage().getExportedPackages();

        List<String> differences = new ArrayList<String>();

        compareImports(bundle1, importPackages1, bundle2, importPackages2, differences);
        compareExports(bundle1, exportPackages1, bundle2, exportPackages2, differences);

        if (!differences.isEmpty()) {
            System.out.println(bundle1 + " " + bundle2);
            for (String difference : differences) {
                System.out.println("    " + difference);
            }
        }
    }

    private static BundleManifest createManifest(File bundle) throws IOException {
        JarFile jarFile = new JarFile(bundle);
        ZipEntry manifest = jarFile.getEntry(JarFile.MANIFEST_NAME);

        Reader in = new InputStreamReader(jarFile.getInputStream(manifest));
        try {
            return new StandardBundleManifest(new SimpleParserLogger(), in);
        } finally {
            in.close();
        }
    }

    private static void process(File fileToProcess, String dir1Path, String dir2Path) throws IOException {
        if (fileToProcess.isDirectory()) {

            File[] files = fileToProcess.listFiles();

            if (files != null) {
                for (File file : files) {
                    process(file, dir1Path, dir2Path);
                }
            }
        } else if (fileToProcess.getName().endsWith(".jar") && !fileToProcess.getName().contains("-sources-")) {
            File otherFile = new File(fileToProcess.getAbsolutePath().replace(dir1Path, dir2Path));
            if (otherFile.exists()) {
                diff(fileToProcess, otherFile);
            } else {
                System.out.println("File '" + otherFile + "' corresponding to file '" + fileToProcess + "' was not found.");
            }
        }
    }

    private static void compareImports(File file1, List<ImportedPackage> importsOne, File file2, List<ImportedPackage> importsTwo,
        List<String> differences) {

        for (ImportedPackage packageImport : importsOne) {
            if (!findMatchingImport(packageImport.getPackageName(), importsTwo)) {
                differences.add(file2 + " does not import " + packageImport.getPackageName());
            }
        }

        for (ImportedPackage packageImport : importsTwo) {
            if (!findMatchingImport(packageImport.getPackageName(), importsOne)) {
                differences.add(file1 + " does not import " + packageImport.getPackageName());
            }
        }
    }

    private static void compareExports(File file1, List<ExportedPackage> exportsOne, File file2, List<ExportedPackage> exportsTwo,
        List<String> differences) {

        for (ExportedPackage packageExport : exportsOne) {
            if (!findMatchingExport(packageExport.getPackageName(), exportsTwo)) {
                differences.add(file2 + " does not export " + packageExport.getPackageName());
            }
        }

        for (ExportedPackage packageExport : exportsTwo) {
            if (!findMatchingExport(packageExport.getPackageName(), exportsOne)) {
                differences.add(file1 + " does not export " + packageExport.getPackageName());
            }
        }
    }

    private static boolean findMatchingImport(String packageName, List<ImportedPackage> imports) {
        for (ImportedPackage packageImport : imports) {
            if (packageImport.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    private static boolean findMatchingExport(String packageName, List<ExportedPackage> exports) {
        for (ExportedPackage packageExport : exports) {
            if (packageExport.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    private static class SimpleParserLogger implements ParserLogger {

        private volatile boolean used = false;

        private final List<String> messages = new ArrayList<String>();

        private final Object messagesMonitor = new Object();

        /**
         * {@inheritDoc}
         */
        public String[] errorReports() {
            if (this.used) {
                synchronized (messagesMonitor) {
                    return messages.toArray(new String[messages.size()]);
                }
            }
            return null;
        }

        /**
         * {@inheritDoc}
         */
        public void outputErrorMsg(Exception re, String item) {
            this.used = true;
            synchronized (messagesMonitor) {
                messages.add(item);
            }
            System.err.println(item);
            re.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException {
        String dir1Path = args[0];
        String dir2Path = args[1];

        File dir1 = new File(dir1Path);

        process(dir1, dir1Path, dir2Path);
    }
}
