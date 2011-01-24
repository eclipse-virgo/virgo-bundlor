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

package org.eclipse.virgo.bundlor.ant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.junit.Test;

import org.eclipse.virgo.bundlor.ant.internal.Configuration;

public class BundlorTests {

    private final StubBundlor bundlor = new StubBundlor();

    @Test
    public void executeDisabled() {
        this.bundlor.setEnabled(false);
        this.bundlor.execute();
        assertFalse(bundlor.getStubBundlorExecutor().getCalled());
    }

    @Test
    public void executeDontFailOnWarnings() {
        this.bundlor.execute();
        assertTrue(bundlor.getStubBundlorExecutor().getCalled());
    }

    @Test(expected = BuildException.class)
    public void executeFailOnWarnings() {
        this.bundlor.setFailOnWarnings(true);
        this.bundlor.execute();
    }

    @Test(expected = BuildException.class)
    public void executeFailOnWarningsWithMaifestTemplatePath() {
        this.bundlor.setFailOnWarnings(true);
        this.bundlor.setManifestTemplatePath("test");
        this.bundlor.execute();
    }

    @Test
    public void configuration() {
        Configuration configuration = new Configuration();
        Bundlor bundlor = new Bundlor(configuration);

        bundlor.setInputPath("testInputPath");
        assertEquals("testInputPath", configuration.getInputPath());

        bundlor.setOutputPath("testOutputPath");
        assertEquals("testOutputPath", configuration.getOutputPath());

        bundlor.setOsgiProfilePath("testOsgiProfilePath");
        assertEquals("testOsgiProfilePath", configuration.getOsgiProfilePath());

        bundlor.setBundleSymbolicName("testBundleSymbolicName");
        assertEquals("testBundleSymbolicName", configuration.getBundleSymbolicName());

        bundlor.setBundleVersion("testBundleVersion");
        assertEquals("testBundleVersion", configuration.getBundleVersion());

        bundlor.setPropertiesPath("testPropertiesPath");
        assertEquals("testPropertiesPath", configuration.getPropertiesPath());
    }

    private static class StubBundlor extends Bundlor {

        private final StubBundlorExecutor executor = new StubBundlorExecutor();

        @Override
        protected BundlorExecutor getBundlorExecutor() {
            return this.executor;
        }

        public StubBundlorExecutor getStubBundlorExecutor() {
            return this.executor;
        }

    }

    private static class StubBundlorExecutor implements BundlorExecutor {

        private volatile boolean called = false;

        public List<String> execute() {
            this.called = true;
            return Arrays.asList("warning");
        }

        public boolean getCalled() {
            return this.called;
        }
    }
}
