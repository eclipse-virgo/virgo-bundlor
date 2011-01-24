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

 * Identity transformer is a no-op transformer.

 * 

 * @author Andy Clement

 */

final class IdentityTransformer implements Transformer {



    static IdentityTransformer instance = new IdentityTransformer();



    // static shared instance should be used

    private IdentityTransformer() {



    }



    public <T> T transform(T input) {

        return input;

    }



    public String toString() {

        return "=";

    }

}
