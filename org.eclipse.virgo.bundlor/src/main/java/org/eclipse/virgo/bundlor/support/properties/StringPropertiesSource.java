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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;

public class StringPropertiesSource implements PropertiesSource {

    private final Properties properties;

    public StringPropertiesSource(String propertiesString) {
        Properties p = new Properties();
        try {
            p.load(new ByteArrayInputStream(propertiesString.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.properties = p;
    }

    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    public Properties getProperties() {
        return this.properties;
    }

}
