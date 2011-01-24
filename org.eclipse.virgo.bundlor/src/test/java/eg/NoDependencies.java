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

package eg;

public class NoDependencies {

    public void m() {
        // Use primitive types to make sure the don't cause a problem, however unlikely that may seem.
        byte b = (byte) 0;
        short h = (short) b;
        long l = (long) h;
        double d = (double) l;
        float f = (float) d;
        String s = (String) Float.toString(f);
        char c = (char) s.charAt(0);
        boolean o = (boolean) s.equals(this);
        System.out.println(c + "" + o);
    }
}
