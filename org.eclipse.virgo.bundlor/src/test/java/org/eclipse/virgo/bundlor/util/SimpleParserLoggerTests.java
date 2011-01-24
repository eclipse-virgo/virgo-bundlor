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

package org.eclipse.virgo.bundlor.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SimpleParserLoggerTests {

    private final SimpleParserLogger logger = new SimpleParserLogger();

    @Test
    public void unused() {
        assertNull(logger.errorReports());
    }

    @Test
    public void used() {
        logger.outputErrorMsg(new Exception(), "testItem");

        String[] errorReports = logger.errorReports();
        assertNotNull(errorReports);
        assertEquals(1, errorReports.length);
        assertEquals("testItem", errorReports[0]);
    }
}
