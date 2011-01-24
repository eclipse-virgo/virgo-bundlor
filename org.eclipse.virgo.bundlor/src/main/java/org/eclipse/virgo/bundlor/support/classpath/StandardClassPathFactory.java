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

package org.eclipse.virgo.bundlor.support.classpath;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;

import org.eclipse.virgo.bundlor.ClassPath;

public final class StandardClassPathFactory implements ClassPathFactory {

    /** 
     * {@inheritDoc}
     */
    public ClassPath create(String inputPath) {
        File inputFile = new File(inputPath);
        if (inputFile.isDirectory()) {
            return new FileSystemClassPath(inputFile);
        }

        try {
            return new JarFileClassPath(new JarFile(inputFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
