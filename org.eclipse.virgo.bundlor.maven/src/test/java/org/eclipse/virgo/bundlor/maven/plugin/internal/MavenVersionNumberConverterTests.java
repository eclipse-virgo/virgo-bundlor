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

package org.eclipse.virgo.bundlor.maven.plugin.internal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MavenVersionNumberConverterTests {

    private final MavenVersionNumberConverter converter = new MavenVersionNumberConverter();

    @Test
    public void major() {
        assertEquals("1.0.0.SNAPSHOT", converter.convertToOsgi("1-SNAPSHOT"));
    }

    @Test
    public void minor() {
        assertEquals("1.0.0.SNAPSHOT", converter.convertToOsgi("1.0-SNAPSHOT"));
    }

    @Test
    public void micro() {
        assertEquals("1.0.0.SNAPSHOT", converter.convertToOsgi("1.0.0-SNAPSHOT"));
    }

    @Test
    public void qualifier() {
        assertEquals("1.0.0.SNAPSHOT", converter.convertToOsgi("1.0.0.SNAPSHOT"));
    }

    @Test
    public void extendedQualifier() {
        assertEquals("1.0.0.BUILD-SNAPSHOT", converter.convertToOsgi("1.0.0.BUILD-SNAPSHOT"));
    }

    @Test
    public void illegalQualifier() {
        assertEquals("1.0.0.BUILD_SNAPSHOT", converter.convertToOsgi("1.0.0.BUILD.SNAPSHOT"));
    }
    
    @Test
    public void noQualifier() {
        assertEquals("1.0.0", converter.convertToOsgi("1.0.0"));
    }

}
