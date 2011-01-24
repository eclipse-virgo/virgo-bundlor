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

package org.eclipse.virgo.bundlor.support.partialmanifest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import org.eclipse.virgo.bundlor.support.PartialManifestResolver;
import org.eclipse.virgo.bundlor.util.SimpleManifestContents;
import org.eclipse.virgo.util.osgi.manifest.BundleManifest;
import org.eclipse.virgo.util.osgi.manifest.ExportedPackage;
import org.eclipse.virgo.util.osgi.manifest.ImportedPackage;
import org.eclipse.virgo.util.osgi.manifest.Resolution;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;
import org.eclipse.virgo.util.parser.manifest.ManifestParser;
import org.eclipse.virgo.util.parser.manifest.ManifestProblem;
import org.eclipse.virgo.util.parser.manifest.RecoveringManifestParser;

public class StandardPartialManifestResolverTests {

    private ManifestContents template;

    @Before
    public void before() throws Exception {
        this.template = loadManifest("TEMPLATE.MF");
    }

    @Test
    public void importsResolved() {
        ReadablePartialManifest partial = new StandardReadablePartialManifest();
        partial.recordReferencedType("com.springsource.server.kernel.SomeImpl");
        partial.recordReferencedType("com.springsource.server.kernel.core.CoreClass");
        partial.recordReferencedType("com.springsource.server.kernel.core.foo.FooClass");
        PartialManifestResolver merger = new StandardPartialManifestResolver();
        BundleManifest result = merger.resolve(this.template, partial);
        assertNotNull(result);
        assertImports("com.springsource.server.kernel", "1.1", result);
        assertImports("com.springsource.server.kernel.core", "1.1", result);
        assertImports("com.springsource.server.kernel.core.foo", "1.2", result, true);

    }

    @Test
    public void exportsResolved() {
        ReadablePartialManifest partial = new StandardReadablePartialManifest();
        partial.recordExportPackage("com.springsource.server.concurrent");
        partial.recordExportPackage("com.springsource.server.concurrent.internal");
        partial.recordExportPackage("com.springsource.server.concurrent.internal.something");
        partial.recordExportPackage("com.springsource.server.config");

        PartialManifestResolver merger = new StandardPartialManifestResolver();
        BundleManifest result = merger.resolve(this.template, partial);
        assertNotNull(result);
        assertExports("com.springsource.server.concurrent", "1.2.3", result);
        assertExports("com.springsource.server.config", "1.9", result);

    }

    @Test
    public void testWithNoImportTemplate() throws Exception {
        ManifestContents mf = loadManifest("NOIMPORTTEMPLATE.MF");
        ReadablePartialManifest partial = new StandardReadablePartialManifest();
        partial.recordReferencedType("foo.FooClass");
        PartialManifestResolver merger = new StandardPartialManifestResolver();
        BundleManifest result = merger.resolve(mf, partial);
        assertNotNull(result);
        assertImports("foo", null, result);
    }

    @Test
    public void testWithNoExportTemplate() throws Exception {
        ManifestContents mf = loadManifest("NOEXPORTTEMPLATE.MF");
        ReadablePartialManifest partial = new StandardReadablePartialManifest();
        partial.recordExportPackage("foo");
        PartialManifestResolver merger = new StandardPartialManifestResolver();
        BundleManifest result = merger.resolve(mf, partial);
        assertNotNull(result);
        assertExports("foo", null, result);
    }

    @Test
    public void testQuotingOfUses() throws Exception {
        ManifestContents template = new SimpleManifestContents();
        template.getMainAttributes().put("Export-Template", "com.foo.bar;uses:=\"use.a,use.b\"");
        ReadablePartialManifest partial = new StandardReadablePartialManifest();
        partial.recordExportPackage("com.foo.bar");
        partial.recordUsesPackage("com.foo.bar", "use.c");
        partial.recordUsesPackage("com.foo.bar", "use.d");
        PartialManifestResolver resolver = new StandardPartialManifestResolver();
        BundleManifest result = resolver.resolve(template, partial);
        List<ExportedPackage> packageExports = result.getExportPackage().getExportedPackages();
        assertTrue(packageExports.size() == 1);
        ExportedPackage export = packageExports.get(0);
        String packageName = export.getPackageName();
        assertTrue(packageName.equals("com.foo.bar"));
        List<String> uses = export.getUses();
        assertTrue(uses.size() == 4);
        assertTrue(uses.toString(), uses.contains("use.a"));
        assertTrue(uses.toString(), uses.contains("use.b"));
        assertTrue(uses.toString(), uses.contains("use.c"));
        assertTrue(uses.toString(), uses.contains("use.d"));
    }

    @Test
    public void testTemplateWithVersionRangeInImportTemplate() throws Exception {
        ManifestContents template = new SimpleManifestContents();
        template.getMainAttributes().put("Import-Template", "com.foo;version=\"[1.0.0,2.0.0)\"");
        ReadablePartialManifest partial = new StandardReadablePartialManifest();
        partial.recordReferencedType("com.foo.FooClass");
        PartialManifestResolver resolver = new StandardPartialManifestResolver();
        BundleManifest result = resolver.resolve(template, partial);
        assertImports("com.foo", "[1.0.0,2.0.0)", result);
    }

    private ManifestContents loadManifest(String name) throws IOException {
        ManifestParser parser = new RecoveringManifestParser();
        ManifestContents manifest = parser.parse(new FileReader("src/test/resources/org/eclipse/virgo/bundlor/support/partialmanifest/" + name));
        if (parser.foundProblems()) {
            for (ManifestProblem problem : parser.getProblems()) {
                System.err.println(problem.toStringWithContext());
                System.err.println();
            }
            throw new RuntimeException("There was a problem with the manifest");
        }
        return manifest;
    }

    private void assertImports(String packageName, String version, BundleManifest result) {
        assertImports(packageName, version, result, false);
    }

    private void assertImports(String packageName, String version, BundleManifest result, boolean optional) {
        List<ImportedPackage> packageImports = result.getImportPackage().getImportedPackages();
        boolean found = false;
        for (ImportedPackage packageImport : packageImports) {
            if (packageImport.getPackageName().equals(packageName)
                && (version == null || version.equals(packageImport.getAttributes().get("version")))
                && optional == packageImport.getResolution().equals(Resolution.OPTIONAL)) {
                found = true;
                break;
            }
        }
        if (optional) {
            assertTrue("Expected optional Import-Package of '" + packageName + " at version '" + version + "' in manifest " + result, found);
        } else {
            assertTrue("Expected Import-Package of '" + packageName + " at version '" + version + "' in manifest " + result, found);
        }
    }

    protected void assertExports(String packageName, String version, BundleManifest result) {
        boolean found = manifestExports(packageName, version, result);
        assertTrue("Expected Export-Package of '" + packageName + " at version '" + version + "'", found);
    }

    private boolean manifestExports(String packageName, String version, BundleManifest result) {
        List<ExportedPackage> packageExports = result.getExportPackage().getExportedPackages();
        boolean found = false;
        for (ExportedPackage packageExport : packageExports) {
            if (packageExport.getPackageName().equals(packageName)
                && (version == null || version.equals(packageExport.getAttributes().get("version")))) {
                found = true;
                break;
            }
        }
        return found;
    }
}
