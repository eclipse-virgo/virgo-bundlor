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



import java.util.List;



/**

 * A VersionExpander is the result of parsing a valid expansion string with the VersionExpansionParser. A version

 * expander is then a reusable entity that can be fed a four part bundle version and will return the expansion of that.

 * For example, if the expansion is "[+1.=,+2,=]" and the expander is called with expand(2,3,4,someQualifier) then the

 * output will be "[3.3,4.3]"

 * 

 * @author Andy Clement

 */

final class VersionExpander {



    private final boolean startInclusive;



    private final List<Transformer> lower;



    private final List<Transformer> upper;



    private final boolean endInclusive;



    public VersionExpander(boolean startInclusive, List<Transformer> lower, List<Transformer> upper, boolean endInclusive) {

        this.startInclusive = startInclusive;

        this.lower = lower;

        this.upper = upper;

        this.endInclusive = endInclusive;

    }



    public String toString() {

        StringBuffer sb = new StringBuffer();

        sb.append(startInclusive ? "[" : "(");

        int pos = 0;

        for (Transformer t : lower) {

            if (pos > 0)

                sb.append(".");

            sb.append(t);

            pos++;

        }

        sb.append(",");

        pos = 0;

        for (Transformer t : upper) {

            if (pos > 0)

                sb.append(".");

            sb.append(t);

            pos++;

        }

        sb.append(endInclusive ? "]" : ")");

        return sb.toString();

    }



    public String expand(int maj, int min, int mic, String qualifier) {

        StringBuffer expansion = new StringBuffer();

        expansion.append(startInclusive ? "[" : "(");

        expansion.append(lower.get(0).transform(maj));

        if (lower.size() > 1) {

            expansion.append(".");

            expansion.append(lower.get(1).transform(min));

            if (lower.size() > 2) {

                expansion.append(".");

                expansion.append(lower.get(2).transform(mic));

                if (lower.size() > 3) {

                    expansion.append(".");

                    expansion.append(lower.get(3).transform(qualifier));

                }

            }

        }

        expansion.append(",");

        expansion.append(upper.get(0).transform(maj));

        if (upper.size() > 1) {

            expansion.append(".");

            expansion.append(upper.get(1).transform(min));

            if (upper.size() > 2) {

                expansion.append(".");

                expansion.append(upper.get(2).transform(mic));

                if (upper.size() > 3) {

                    expansion.append(".");

                    expansion.append(upper.get(3).transform(qualifier));

                }

            }

        }

        expansion.append(endInclusive ? "]" : ")");

        return expansion.toString();

    }

}
