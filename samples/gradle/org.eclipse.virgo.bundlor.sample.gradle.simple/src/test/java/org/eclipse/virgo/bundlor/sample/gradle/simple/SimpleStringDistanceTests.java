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

package org.eclipse.virgo.bundlor.sample.gradle.simple;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.eclipse.virgo.bundlor.sample.gradle.simple.SimpleStringDistance.DistanceAlgorithm;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnitTests for {@link SimpleStringDistance} implementation.
 */
public class SimpleStringDistanceTests {

    private static final DistanceAlgorithm FUZZY = DistanceAlgorithm.FUZZY;

    private static final Locale LOCALE = Locale.US;

    private static final String VIRGO = "Virgo";

    private static final String GRADLE = "Gradle";

    private SimpleStringDistance simpleStringDistance;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        simpleStringDistance = new SimpleStringDistance(FUZZY, LOCALE, VIRGO, GRADLE);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        simpleStringDistance = null;
    }

    /**
     * Test method for {@link org.eclipse.virgo.bundlor.sample.gradle.simple.SimpleStringDistance#getDistance()}.
     */
    @Test
    public void testGetDistance() throws Exception {
        int distance = simpleStringDistance.getDistance();
        assertThat(distance, is(6));
    }

    /**
     * Test method for {@link org.eclipse.virgo.bundlor.sample.gradle.simple.SimpleStringDistance#toString()}.
     */
    @Test
    public void testToString() throws Exception {
        String value = simpleStringDistance.toString();
        assertThat(value, is(not(nullValue())));
        assertThat(value.isEmpty(), is(false));
    }
}
