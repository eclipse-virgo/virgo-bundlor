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

package org.eclipse.virgo.bundlor.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.eclipse.virgo.bundlor.util.MatchUtils;

public class MatchUtilsTests {

    @Test
    public void matchWildcard() {
        assertTrue(MatchUtils.matches("com.foo.bar", "com.foo.bar.*"));
        assertTrue(MatchUtils.matches("com.foo.bar", "com.foo.bar"));
        assertTrue(MatchUtils.matches("com.foo.bar", "com.foo.*"));
        assertTrue(MatchUtils.matches("com.foo.bar", "*"));
        assertFalse(MatchUtils.matches("com.foo.bar", "com.foo"));
        assertFalse(MatchUtils.matches("javax.foo", "java.*"));
    }

    @Test
    public void matchMultipleWildcards() {
        // multiple wildcards matches
        assertTrue(MatchUtils.matches("com.foo.internal.bar", "*.internal.*"));
        assertTrue(MatchUtils.matches("com.foo.internal.bar", "*internal*"));
        assertTrue(MatchUtils.matches("com.foo.internal.bar", "com.foo.*internal.*"));
        assertFalse(MatchUtils.matches("com.foo.internal.bar", "com.foo.*internal"));
        assertTrue(MatchUtils.matches("com.foo.internal.bar", "com.*.*internal.*"));
        assertFalse(MatchUtils.matches("com.foo.internal.bar", "com.*.*.internal.*"));
        assertTrue(MatchUtils.matches("com.foo.internal.bar", "*.*.internal.*"));
    }

    @Test
    public void matchWithUnderscoreInPackageName() {
        assertTrue(MatchUtils.matches("com_cenqua_clover", "com_cenqua_clover"));
        assertTrue(MatchUtils.matches("com_cenqua_clover", "com_cenqua_clover.*"));
        assertTrue(MatchUtils.matches("com_cenqua_clover", "com_cenqua_clover*"));
        assertTrue(MatchUtils.matches("com_cenqua_clover", "com_cenqua*"));
        assertTrue(MatchUtils.matches("com_cenqua_clover", "com*"));
    }

    @Test
    public void testRankedMatch() {
        assertEquals(MatchUtils.NO_MATCH, MatchUtils.rankedMatch("", null));
        assertEquals(MatchUtils.NO_MATCH, MatchUtils.rankedMatch("", ""));
        assertEquals(MatchUtils.NO_MATCH, MatchUtils.rankedMatch("com.foo.bar", "foo.*"));
        assertEquals(0, MatchUtils.rankedMatch("", "*"));

        int rank1 = MatchUtils.rankedMatch("com.foo.bar.Fudge", "com.foo.*");
        int rank2 = MatchUtils.rankedMatch("com.foo.bar.Fudge", "com.foo.bar.*");
        assertTrue(rank1 < rank2);
    }

}
