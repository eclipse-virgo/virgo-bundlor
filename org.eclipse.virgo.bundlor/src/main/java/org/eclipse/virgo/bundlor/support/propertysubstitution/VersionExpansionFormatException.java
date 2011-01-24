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

package org.eclipse.virgo.bundlor.support.propertysubstitution;



/**

 * This exception occurs if there is a problem with the format of the expansion string.

 * 

 * @author Andy Clement

 */

public final class VersionExpansionFormatException extends RuntimeException {



    private static final long serialVersionUID = 3114387175457934241L;



    private String expansionString;



    public VersionExpansionFormatException(String expansion, String message) {

        super(message);

        this.expansionString = expansion;

    }



    public String getExpansionString() {

        return expansionString;

    }



}
