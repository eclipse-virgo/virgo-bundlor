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

import java.io.File;
import java.util.Properties;

import org.junit.Test;

public class FileSystemPropertiesSourceTests {

    private static final File BUNDLE1_PROPERTIES = new File(
        "src/test/resources/org/eclipse/virgo/bundlor/support/propertysubstitution/bundle.properties");

    private final FileSystemPropertiesSource propertiesSource = new FileSystemPropertiesSource(BUNDLE1_PROPERTIES);

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
