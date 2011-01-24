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

package org.eclipse.virgo.bundlor.support.properties;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StringPropertiesSourceTests {

    private final StringPropertiesSource propertiesSource = new StringPropertiesSource("key=value");

    @Test
    public void properties() {
        assertEquals(1, propertiesSource.getProperties().size());
    }

    @Test
    public void priority() {
        assertEquals(Integer.MAX_VALUE, propertiesSource.getPriority());
    }
}
