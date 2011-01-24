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

 * The sum transformer perhaps a numeric computation (for example adding 3 or subtracting 4) on the input value.

 * 

 * @author Andy Clement

 */

final class SumTransformer implements Transformer {



    private int modifier;



    SumTransformer(int modifier) {

        this.modifier = modifier;

    }



    @SuppressWarnings("unchecked")

    public <T> T transform(T input) {

        Integer n = (Integer) input;

        return (T) Integer.valueOf(n.intValue() + modifier);

    }



    public String toString() {

        StringBuffer sb = new StringBuffer();

        sb.append(modifier > 0 ? "+" : "");

        sb.append(Integer.toString(modifier));

        return sb.toString();

    }



}
