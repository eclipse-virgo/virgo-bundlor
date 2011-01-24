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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FileSystemPropertiesSource implements PropertiesSource {

    private final Properties properties;

    public FileSystemPropertiesSource(File propertiesFile) {
        this.properties = readProperties(propertiesFile);
    }

    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    public Properties getProperties() {
        return this.properties;
    }

    private Properties readProperties(File propertiesFile) {
        Properties p = new Properties();
        InputStream in = null;
        try {
            in = new FileInputStream(propertiesFile);
            p.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                // Nothing to do here
            }
        }
        return p;
    }

}
