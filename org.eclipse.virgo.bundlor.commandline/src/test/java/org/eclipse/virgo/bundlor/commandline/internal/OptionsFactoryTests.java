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

package org.eclipse.virgo.bundlor.commandline.internal;

import static org.junit.Assert.assertEquals;

import org.apache.commons.cli.Options;
import org.junit.Test;

public class OptionsFactoryTests {

    private final OptionsFactory factory = new OptionsFactory();

    @Test
    public void options() {
        Options options = this.factory.create();
        assertEquals(7, options.getOptions().size());
    }
}
