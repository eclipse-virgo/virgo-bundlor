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

 * Replacement transformer is configured with a new value upon construction and returns that value instead of any value

 * that it is passed.

 * 

 * @author Andy Clement

 */

final class ReplacementTransformer implements Transformer {



    private String replacement;



    ReplacementTransformer(String replacement) {

        this.replacement = replacement;

    }



    @SuppressWarnings("unchecked")

    public <T> T transform(T input) {

        return (T) replacement;

    }



    public String toString() {

        return replacement;

    }



}

