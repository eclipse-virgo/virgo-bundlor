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

package org.eclipse.virgo.bundlor.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import org.eclipse.virgo.bundlor.commandline.internal.Configuration;

public class BundlorTests {

    private final StubBundlor bundlor = new StubBundlor();

    @Test
    public void executeDontFailOnWarnings() {
        this.bundlor.run(new String[] { "-i", "test" });
        assertTrue(this.bundlor.getStubBundlorExecutor().getCalled());
    }

    @Test
    public void executeFailOnWarnings() {
        this.bundlor.run(new String[] { "-i", "test", "-f" });
        assertTrue(this.bundlor.getStubBundlorExecutor().getCalled());
        assertEquals(-1, this.bundlor.getCode());
    }

    private static class StubBundlor extends Bundlor {

        private static final StubBundlorExecutor executor = new StubBundlorExecutor();

        private volatile int code;

        @Override
        protected BundlorExecutor getBundlorExecutor(Configuration configuration) {
            return executor;
        }

        @Override
        protected void exit(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public StubBundlorExecutor getStubBundlorExecutor() {
            return executor;
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
