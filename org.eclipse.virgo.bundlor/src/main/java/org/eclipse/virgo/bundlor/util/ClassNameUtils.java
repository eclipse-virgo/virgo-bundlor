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

import java.util.regex.Pattern;

/**
 * A utility class for dealing with Java class names
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Threadsafe
 * 
 * @author Ben Hale
 */
public class ClassNameUtils {

    private static final Pattern FQN_CLASS_PATTERN = Pattern.compile("[a-zA-Z](([\\w_$])+)+(\\.([\\w_$])+)*");

    /**
     * Determines if a string is valid Java identifier.
     * 
     * @param candidate The candidate string
     * @return <code>true</code> if candidate is a valid Java identifier, <code>false</code> otherwise
     */
    public static boolean isValidFqn(String candidate) {
        return FQN_CLASS_PATTERN.matcher(candidate).matches();
    }

}
