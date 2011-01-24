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

package org.eclipse.virgo.bundlor.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.virgo.util.osgi.manifest.parse.ParserLogger;

/**
 * A simple implementation of {@link ParserLogger} that outputs message to console
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Threadsafe
 * 
 * @author Ben Hale
 */
public class SimpleParserLogger implements ParserLogger {

    private volatile boolean used = false;

    private final List<String> messages = new ArrayList<String>();

    private final Object messagesMonitor = new Object();

    /**
     * {@inheritDoc}
     */
    public String[] errorReports() {
        if (this.used) {
            synchronized (messagesMonitor) {
                return messages.toArray(new String[messages.size()]);
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void outputErrorMsg(Exception re, String item) {
        this.used = true;
        synchronized (messagesMonitor) {
            messages.add(item);
        }
        System.err.println(item);
        if (re != null) {
            re.printStackTrace();
        }
    }

}
