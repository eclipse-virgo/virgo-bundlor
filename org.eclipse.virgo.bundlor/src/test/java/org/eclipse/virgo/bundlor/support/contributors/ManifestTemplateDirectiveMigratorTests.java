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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.osgi.framework.Constants;

import org.eclipse.virgo.bundlor.support.partialmanifest.StandardReadablePartialManifest;
import org.eclipse.virgo.bundlor.util.SimpleManifestContents;

public class ManifestTemplateDirectiveMigratorTests {

    private final ManifestTemplateDirectiveMigrator contributor = new ManifestTemplateDirectiveMigrator();

    @Test
    public void importPackage() {
        SimpleManifestContents template = new SimpleManifestContents();
        template.getMainAttributes().put(Constants.IMPORT_PACKAGE,
            "com.springsource.test;version=0,com.springsource.test2;version=1;resolution:=optional");
        this.contributor.modify(template);

        StandardReadablePartialManifest partialManifest = new StandardReadablePartialManifest();
        this.contributor.modify(partialManifest);

        Set<String> importedPackages = partialManifest.getImportedPackages();
        assertEquals(2, importedPackages.size());
        assertTrue(importedPackages.contains("com.springsource.test"));
        assertTrue(importedPackages.contains("com.springsource.test2"));
    }

    @Test
    public void importPackageNull() {
        StandardReadablePartialManifest partialManifest = new StandardReadablePartialManifest();
        this.contributor.modify(partialManifest);

        Set<String> importedPackages = partialManifest.getImportedPackages();
        assertEquals(0, importedPackages.size());
    }

    @Test
    public void exportPackage() {
        SimpleManifestContents template = new SimpleManifestContents();
        template.getMainAttributes().put(Constants.EXPORT_PACKAGE, "com.springsource.test;version=0,com.springsource.test2;version=1");
        this.contributor.modify(template);

        StandardReadablePartialManifest partialManifest = new StandardReadablePartialManifest();
        this.contributor.modify(partialManifest);

        Set<String> exportedPackages = partialManifest.getExportedPackages();
        assertEquals(2, exportedPackages.size());
        assertTrue(exportedPackages.contains("com.springsource.test"));
        assertTrue(exportedPackages.contains("com.springsource.test2"));
    }

    @Test
    public void exportPackageNull() {
        StandardReadablePartialManifest partialManifest = new StandardReadablePartialManifest();
        this.contributor.modify(partialManifest);

        Set<String> exportedPackages = partialManifest.getExportedPackages();
        assertEquals(0, exportedPackages.size());
    }

    @Test
    public void importTemplate() {
        SimpleManifestContents template = new SimpleManifestContents();
        template.getMainAttributes().put(Constants.IMPORT_PACKAGE,
            "com.springsource.test;version=0,com.springsource.test2;version=1;resolution:=optional");
        this.contributor.modify(template);

        String importTemplateString = template.getMainAttributes().get("Import-Template");
        assertNotNull(importTemplateString);
        assertEquals("com.springsource.test;version=0,com.springsource.test2;version=1;resolution:=optional", importTemplateString);
    }

    @Test
    public void importTemplateAdditional() {
        SimpleManifestContents template = new SimpleManifestContents();
        template.getMainAttributes().put("Import-Template", "com.springsource.test3;version=0");
        template.getMainAttributes().put(Constants.IMPORT_PACKAGE,
            "com.springsource.test;version=0,com.springsource.test2;version=1;resolution:=optional");
        this.contributor.modify(template);

        String importTemplateString = template.getMainAttributes().get("Import-Template");
        assertNotNull(importTemplateString);
        assertEquals("com.springsource.test3;version=0,com.springsource.test;version=0,com.springsource.test2;version=1;resolution:=optional",
            importTemplateString);
    }

    @Test
    public void exportTemplate() {
        SimpleManifestContents template = new SimpleManifestContents();
        template.getMainAttributes().put(Constants.EXPORT_PACKAGE, "com.springsource.test;version=0,com.springsource.test2;version=1");
        this.contributor.modify(template);

        String exportTemplateString = template.getMainAttributes().get("Export-Template");
        assertNotNull(exportTemplateString);
        assertEquals("com.springsource.test;version=0,com.springsource.test2;version=1", exportTemplateString);
    }

    @Test
    public void exportTemplateAdditional() {
        SimpleManifestContents template = new SimpleManifestContents();
        template.getMainAttributes().put("Export-Template", "com.springsource.test3;version=0");
        template.getMainAttributes().put(Constants.EXPORT_PACKAGE, "com.springsource.test;version=0,com.springsource.test2;version=1");
        this.contributor.modify(template);

        String exportTemplateString = template.getMainAttributes().get("Export-Template");
        assertNotNull(exportTemplateString);
        assertEquals("com.springsource.test3;version=0,com.springsource.test;version=0,com.springsource.test2;version=1", exportTemplateString);
    }
}
