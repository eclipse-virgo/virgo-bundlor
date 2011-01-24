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
import java.util.Properties;

import org.apache.tools.ant.types.PropertySet;
import org.junit.Test;

public class PropertySetPropertiesSourceTests {

    @Test
    public void test() {
        PropertySetPropertiesSource propertiesSource = new PropertySetPropertiesSource(Arrays.asList((PropertySet) new StubPropertySet()));
        assertEquals(Integer.MAX_VALUE, propertiesSource.getPriority());
        assertTrue(propertiesSource.getProperties().size() > 0);
    }

    private static class StubPropertySet extends PropertySet {

        @Override
        public Properties getProperties() {
            Properties p = new Properties();
            p.put("test", "test");
            return p;
        }
    }
}
