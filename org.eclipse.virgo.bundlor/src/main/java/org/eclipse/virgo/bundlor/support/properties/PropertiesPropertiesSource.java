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

import java.util.Properties;

public final class PropertiesPropertiesSource implements PropertiesSource {

    private final Properties properties;

    public PropertiesPropertiesSource(Properties properties) {
        this.properties = properties;
    }

    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    public Properties getProperties() {
        return this.properties;
    }

}
