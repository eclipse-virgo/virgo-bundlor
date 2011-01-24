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

public class PropertiesPropertiesSourceTests {

    private final PropertiesPropertiesSource propertiesSource;

    public PropertiesPropertiesSourceTests() {
        Properties p = new Properties();
        p.put("bundle.version", "1.1.0");
        p.put("bundle.name", "SpringSource Test Bundle");
        this.propertiesSource = new PropertiesPropertiesSource(p);
    }

    @Test
    public void properties() {
        Properties props = propertiesSource.getProperties();
        assertEquals(props.getProperty("bundle.version"), "1.1.0");
        assertEquals(props.getProperty("bundle.name"), "SpringSource Test Bundle");
    }

    @Test
    public void priority() {
        assertEquals(Integer.MAX_VALUE, propertiesSource.getPriority());
    }
}
