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

package org.eclipse.virgo.bundlor.support;

import java.util.List;

/**
 * An interface that allows Bundlor to determine which headers exist to support operation and should not exist at
 * runtime. These headers will be removed from the final manifest.
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Implementations should be threadsafe.
 * 
 * @author Ben Hale
 */
public interface TemplateHeaderReader {

    /**
     * Gets the list of header names required by this reader.
     * @return The list of header names.
     */
    List<String> getTemplateOnlyHeaderNames();
}
