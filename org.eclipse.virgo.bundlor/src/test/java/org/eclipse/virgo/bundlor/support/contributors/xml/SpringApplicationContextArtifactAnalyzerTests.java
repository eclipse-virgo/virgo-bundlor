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

package org.eclipse.virgo.bundlor.support.contributors.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Set;

import org.junit.Test;

import org.eclipse.virgo.bundlor.support.partialmanifest.StandardReadablePartialManifest;

public class SpringApplicationContextArtifactAnalyzerTests {

    private final SpringApplicationContextArtifactAnalyzer analyzer = new SpringApplicationContextArtifactAnalyzer();

    @Test
    public void analyze() throws Exception {
        InputStream in = null;
        try {
            in = new FileInputStream("src/test/resources/org/eclipse/virgo/bundlor/support/contributors/xml/spring-1.xml");
            StandardReadablePartialManifest manifest = new StandardReadablePartialManifest();
            this.analyzer.analyse(in, "test-artifact", manifest);

            assertContains(manifest.getImportedPackages(), "org.springframework.somebean", "org.springframework.somebean2",
                "org.springframework.somebean4", "xml.org.springframework.aop.implementinterface", "xml.org.springframework.aop.defaultimpl",
                "xml.org.springframework.context.basepackage", "xml.org.springframework.context.namegenerator",
                "xml.org.springframework.context.scoperesolver", "xml.org.springframework.context.loadtimeweaver",
                "xml.org.springframework.jee.jndilookup.expectedtype", "xml.org.springframework.jee.jndilookup.proxyinteface",
                "xml.org.springframework.jee.localslsb.businessinterface", "xml.org.springframework.jee.remoteslsb.businessinterface",
                "xml.org.springframework.jee.remoteslsb.homeinterface", "xml.org.springframework.jms.listenercontainer.containerclass",
                "xml.org.springframework.jruby.scripinterfaces.a", "xml.org.springframework.jruby.scripinterfaces.b",
                "xml.org.springframework.bsh.scripinterfaces.a", "xml.org.springframework.bsh.scripinterfaces.b",
                "xml.org.springframework.osgi.reference.interface", "xml.org.springframework.osgi.reference.interface.b",
                "xml.org.springframework.osgi.reference.interface.c", "xml.org.springframework.osgi.service.interface",
                "xml.org.springframework.osgi.service.interface.b", "xml.org.springframework.osgi.service.interface.c",
                "xml.org.springframework.util.list.listclass", "xml.org.springframework.util.map.mapclass",
                "xml.org.springframework.util.set.setclass");
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    @Test
    public void canAnalyze() {
        assertTrue(this.analyzer.canAnalyse("test.xml"));
        assertFalse(this.analyzer.canAnalyse("test.properties"));
    }

    private void assertContains(Set<String> experimentalPackages, String... expectedPackages) {
        assertEquals(expectedPackages.length, experimentalPackages.size());
        for (String expectedPackage : expectedPackages) {
            assertTrue("Does not contain " + expectedPackage, experimentalPackages.contains(expectedPackage));
        }
    }
}
