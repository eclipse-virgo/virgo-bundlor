/*
 * This file is part of the Eclipse Virgo project.
 *
 * Copyright (c) 2016 copyright_holder
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    dmarthaler - initial contribution
 */

package deps.neg;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
public @interface NotARuntimeRetentionAnnotation {

}
