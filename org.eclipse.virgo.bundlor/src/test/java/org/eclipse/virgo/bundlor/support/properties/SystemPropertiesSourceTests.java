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

import java.util.Properties;

import org.junit.Test;

public class SystemPropertiesSourceTests {

    private SystemPropertiesSource propertiesSource = new SystemPropertiesSource();

    @Test
    public void properties() {
        Properties props = propertiesSource.getProperties();
        for (Object key : System.getProperties().keySet()) {
            assertEquals(System.getProperty((String) key), props.getProperty((String) key));
        }
    }

    @Test
    public void priority() {
        assertEquals(Integer.MIN_VALUE, propertiesSource.getPriority());
    }

}
