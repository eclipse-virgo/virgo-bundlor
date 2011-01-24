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

package org.eclipse.virgo.bundlor.maven.plugin;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;

public class BundlorTests {

    private final StubBundlor bundlor = new StubBundlor();

    @Test
    public void executeDisabled() throws MojoExecutionException {
        this.bundlor.setEnabled(false);
        this.bundlor.execute();
        assertFalse(bundlor.getStubBundlorExecutor().getCalled());
    }

    @Test
    public void executeDontFailOnWarnings() throws MojoExecutionException {
        this.bundlor.execute();
        assertTrue(bundlor.getStubBundlorExecutor().getCalled());
    }

    @Test(expected = MojoExecutionException.class)
    public void executeFailOnWarnings() throws MojoExecutionException {
        this.bundlor.setFailOnWarnings(true);
        this.bundlor.execute();
    }

    @Test(expected = MojoExecutionException.class)
    public void executeFailOnWarningsWithMaifestTemplatePath() throws MojoExecutionException {
        this.bundlor.setFailOnWarnings(true);
        this.bundlor.setManifestTemplatePath("test");
        this.bundlor.execute();
    }

    private static class StubBundlor extends BundlorMojo {

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
