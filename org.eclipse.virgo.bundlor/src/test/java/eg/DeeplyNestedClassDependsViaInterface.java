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

import deps.SomeInterface;

public class DeeplyNestedClassDependsViaInterface {

    public class Nested {

        public Object m() {

            return new Object() {

                @Override
                protected Object clone() throws CloneNotSupportedException {
                    return new SomeInterface() {
                    };
                }

            };

        }
    }

}
