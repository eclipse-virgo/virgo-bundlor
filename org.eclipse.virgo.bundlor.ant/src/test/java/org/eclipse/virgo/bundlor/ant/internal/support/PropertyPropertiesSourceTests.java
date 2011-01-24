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

package org.eclipse.virgo.bundlor.ant.internal.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.tools.ant.taskdefs.Property;
import org.junit.Test;

public class PropertyPropertiesSourceTests {

    @Test
    public void test() {
        PropertyPropertiesSource propertiesSource = new PropertyPropertiesSource(Arrays.asList((Property) new StubProperty()));
        assertEquals(Integer.MAX_VALUE, propertiesSource.getPriority());
        assertTrue(propertiesSource.getProperties().size() > 0);
    }

    private static class StubProperty extends Property {

        @Override
        public String getName() {
            return "test-name";
        }

        @Override
        public String getValue() {
            return "test-value";
        }

    }
}
