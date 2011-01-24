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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import org.eclipse.virgo.bundlor.support.partialmanifest.ReadablePartialManifest;
import org.eclipse.virgo.bundlor.support.partialmanifest.StandardReadablePartialManifest;
import org.eclipse.virgo.bundlor.util.SimpleManifestContents;
import org.eclipse.virgo.util.parser.manifest.ManifestContents;

public class ExcludedImportAndExportPartialManifestModifierTests {

    private ExcludedImportAndExportPartialManifestModifier modifier = new ExcludedImportAndExportPartialManifestModifier();

    @Test
    public void testExcludedImportExportPackage() {
        ManifestContents manifestTemplate = new SimpleManifestContents();
        manifestTemplate.getMainAttributes().put("Excluded-Imports", "antlr*");
        manifestTemplate.getMainAttributes().put("Excluded-Exports", "groovy*");
        this.modifier.readManifestTemplate(manifestTemplate);

        ReadablePartialManifest partialManifest = new StandardReadablePartialManifest();
        partialManifest.recordReferencedPackage("antlr.one");
        partialManifest.recordReferencedPackage("antlr.two");
        partialManifest.recordReferencedPackage("springsource.one");
        partialManifest.recordExportPackage("groovy.one");
        partialManifest.recordExportPackage("groovy.two");
        partialManifest.recordExportPackage("springsource.two");

        this.modifier.modify(partialManifest);

        for (String importedPackage : partialManifest.getImportedPackages()) {
            assertFalse(importedPackage.startsWith("antlr"));
        }
        for (String exportedPackage : partialManifest.getExportedPackages()) {
            assertFalse(exportedPackage.startsWith("groovy"));
        }
    }

    @Test
    public void excludedImports() throws IOException {
        ManifestContents manifestTemplate = new SimpleManifestContents();
        manifestTemplate.getMainAttributes().put("Excluded-Imports", "com.import.foo.excluded,com.import.bar.excluded.*");
        this.modifier.readManifestTemplate(manifestTemplate);

        ReadablePartialManifest partialManifest = new StandardReadablePartialManifest();
        partialManifest.recordReferencedType("com.import.foo.excluded.TypeA");
        partialManifest.recordReferencedType("com.import.foo.excluded.a.SomeClass");
        partialManifest.recordReferencedType("com.import.bar.excluded.AnotherClass");
        partialManifest.recordReferencedType("com.import.bar.excluded.a.MyImpl");

        this.modifier.modify(partialManifest);

        for (String packageName : new String[] { "com.import.foo.excluded", "com.import.bar.excluded", "com.import.bar.excluded.a" }) {
            assertNotImported(partialManifest, packageName);
        }
        assertImported(partialManifest, "com.import.foo.excluded.a");
    }

    @Test
    public void testExcludedExport() {
        ManifestContents manifestTemplate = new SimpleManifestContents();
        manifestTemplate.getMainAttributes().put("Export-Template", "com.foo;version=\"[1.0.0,2.0.0)\"");
        manifestTemplate.getMainAttributes().put("Excluded-Exports", "com.foo, com.bar");
        this.modifier.readManifestTemplate(manifestTemplate);

        ReadablePartialManifest partialManifest = new StandardReadablePartialManifest();
        partialManifest.recordType("com.foo.FooClass");

        this.modifier.modify(partialManifest);

        assertNotExported(partialManifest, "com.foo");
    }

    @Test
    public void testExcludedExportExtraSpace() {
        ManifestContents manifestTemplate = new SimpleManifestContents();
        manifestTemplate.getMainAttributes().put("Export-Template", "com.foo;version=\"[1.0.0,2.0.0)\"");
        manifestTemplate.getMainAttributes().put("Excluded-Exports", "com.foo, com.bar ");
        this.modifier.readManifestTemplate(manifestTemplate);

        ReadablePartialManifest partialManifest = new StandardReadablePartialManifest();
        partialManifest.recordType("com.foo.FooClass");

        this.modifier.modify(partialManifest);

        assertNotExported(partialManifest, "com.foo");
    }

    @Test
    public void names() {
        List<String> names = this.modifier.getTemplateOnlyHeaderNames();
        assertEquals(2, names.size());
        assertTrue(names.contains("Excluded-Imports"));
        assertTrue(names.contains("Excluded-Exports"));
    }

    private void assertImported(ReadablePartialManifest partialManifest, String packageName) {
        boolean found = false;
        for (String importedPackage : partialManifest.getImportedPackages()) {
            if (importedPackage.equals(packageName)) {
                found = true;
                break;
            }
        }

        assertTrue("Did not find Import-Template entry for " + packageName, found);
    }

    private void assertNotImported(ReadablePartialManifest partialManifest, String packageName) {
        boolean found = false;
        for (String importedPackage : partialManifest.getImportedPackages()) {
            if (importedPackage.equals(packageName)) {
                found = true;
                break;
            }
        }

        assertFalse("Found Import-Template entry for " + packageName, found);
    }

    private void assertNotExported(ReadablePartialManifest partialManifest, String packageName) {
        boolean found = false;
        for (String exportedPackage : partialManifest.getExportedPackages()) {
            if (exportedPackage.equals(packageName)) {
                found = true;
                break;
            }
        }

        assertFalse("Found Import-Template entry for " + packageName, found);
    }

}
