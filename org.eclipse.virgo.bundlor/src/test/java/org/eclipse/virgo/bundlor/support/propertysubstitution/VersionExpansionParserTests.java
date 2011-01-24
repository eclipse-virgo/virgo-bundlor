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



import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertNotNull;

import static org.junit.Assert.fail;



import org.junit.Test;



public class VersionExpansionParserTests {



    @Test

    public void testUsage() {

        VersionExpander ve = VersionExpansionParser.parseVersionExpander("[+1.=,+2)");

        assertEquals("[4.4,5)", ve.expand(3, 4, 5, "dribble"));

    }



    @Test

    public void testBasics() {

        try {

            checkExpander("[=   , = ]", "[=,=]");

            checkExpander("[=.=.=.=,=.=.=.=]", "[=.=.=.=,=.=.=.=]");

            checkExpander("[=.=.=.=,+1]", "[=.=.=.=,+1]");

            checkExpander("[=.=.=.=,-1]", "[=.=.=.=,-1]");

            checkExpander("[=.=.=.=,=.+1]", "[=.=.=.=,=.+1]");

            checkExpander("[=.=.=.=,=.-1]", "[=.=.=.=,=.-1]");

            checkExpander("[=.=.=.=,=.=.+1]", "[=.=.=.=,=.=.+1]");

            checkExpander("[=.=.=.=,=.=.-1]", "[=.=.=.=,=.=.-1]");

            checkExpander("[=.=.=.=,=.=.=.wibble]", "[=.=.=.=,=.=.=.wibble]");

            checkExpander("[=.=.=.=,=.=.3]", "[=.=.=.=,=.=.3]");



            checkExpander("[=.=.=.=,=]", "[=.=.=.=,=]");

            checkExpander("[+1.=.=.=,=]", "[+1.=.=.=,=]");

            checkExpander("[-1.=.=,=]", "[-1.=.=,=]");

            checkExpander("[-1 . =. =,  = ]", "[-1.=.=,=]");

            checkExpander("[+1.=,=]", "[+1.=,=]");

            checkExpander("[-1.=,=]", "[-1.=,=]");

            checkExpander("[+1.=.-2.=,=]", "[+1.=.-2.=,=]");

            checkExpander("[=.=.=.foo,=]", "[=.=.=.foo,=]");

            checkExpander("[=.=.=.34,=]", "[=.=.=.34,=]");



            checkExpander("(=,=]", "(=,=]");

            checkExpander("(=,=)", "(=,=)");

            checkExpander("[=,=)", "[=,=)");

        } catch (VersionExpansionFormatException vefe) {

            System.out.println(vefe.getMessage());

            throw vefe;

        }

    }



    // maj: =.0.0

    // min: =.=.0

    // mic: =.=.=

    // qual: =.=.=.=

    @Test

    public void testMacros() {

        checkExpander("[maj,=]", "[=.0.0,=]");

        checkExpander("[min,=]", "[=.=.0,=]");

        checkExpander("[mic,=]", "[=.=.=,=]");

        checkExpander("[qual,=]", "[=.=.=.=,=]");

    }



    // maj[+/-]n: [+/-]n.0.0

    // min[+/-]n: =.[+/-]n.0

    // mic[+/-]n: =.=.[+/-]n

    @Test

    public void testMacroModification() {

        checkExpander("[maj+3,=]", "[+3.0.0,=]");

        checkExpander("[maj-23,=]", "[-23.0.0,=]");

        checkExpander("[min+37,=]", "[=.+37.0,=]");

        checkExpander("[min-1,=]", "[=.-1.0,=]");

        checkExpander("[mic+3,=]", "[=.=.+3,=]");

        checkExpander("[mic-333,=]", "[=.=.-333,=]");

    }



    @Test

    public void testErrors() {

        checkForError("[1.2,3", "run out of tokens");

        checkForError("[maj3,=]", "numeric modifier for macro");

        checkForError("[", "missing comma");

        checkForError("[1", "missing comma");

        checkForError("[1.", "missing comma");

        checkForError("[1.,", "expected one of");

        checkForError("[1.3,", "run out of tokens");

        checkForError("[1.3.4.qual,", "run out of tokens");

        checkForError("[1,qual", "run out of tokens");

        checkForError("wibble", "missing comma");

        checkForError("wibble,wobble", "expected a version start character");

        checkForError("[=.=.=.=,=,+1]", "expected a version end character");

        checkForError("[=.=.=.=,=,-1]", "expected a version end character");

        checkForError("[=.=.=.=,=.=.=.+1]", "cannot specify a numerical ");

        checkForError("[=.=.=.=,=.=.=.-1]", "cannot specify a numerical ");

        checkForError("[=,+36,+2)", "expected a version end character");

        checkForError("[32abc,32]", "expected a comma");

        checkForError("[1.2.3.4.5,=]", "too many version components specified");

        checkForError("[=,1.2.3.3.4]", "too many version components specified");

        checkForError("[1.abc,=]", "expected one of");

    }



    @Test

    public void testMoreErrors() {

        checkForError("[maj+-3,=]", "numeric modifier for macro should ");

        checkForError("[majBANANAS,=]", "numeric modifier for macro should");

        checkForError("[maj+,=]", "unable to determine the numeric modifier");

        checkForError("[maj+37-4,=]", "unable to determine the numeric ");

        checkForError("[++2,=]", "cannot parse numerical value ");

        checkForError("[3...,=]", "expected one of: ");

        checkForError("[3.3.1..,=]", "expected one of: '=' '+nnn' '-nnn' 'nnn' or 'xxx' but foun");

    }



    @Test

    public void testExpander() {

        VersionExpander ve = null;

        try {

            ve = VersionExpansionParser.parseVersionExpander("[+1,+2)");

            assertEquals("[4,5)", ve.expand(3, 4, 5, "dribble"));



            ve = VersionExpansionParser.parseVersionExpander("[=.+36,+2)");

            assertEquals("[3.40,5)", ve.expand(3, 4, 5, "dribble"));



            ve = VersionExpansionParser.parseVersionExpander("[=.=.=.wibble,+2.=.=.=)");

            assertEquals("[3.4.5.wibble,5.4.5.dribble)", ve.expand(3, 4, 5, "dribble"));



            ve = VersionExpansionParser.parseVersionExpander("[maj,min-2)");

            assertEquals("[3.0.0,3.2.0)", ve.expand(3, 4, 5, "dribble"));



            ve = VersionExpansionParser.parseVersionExpander("(=.+36,+2.+1]");

            assertEquals("(3.40,5.5]", ve.expand(3, 4, 5, "dribble"));

        } catch (VersionExpansionFormatException vefe) {

            System.out.println(vefe.getMessage());

            throw vefe;

        }

    }



    private void checkExpander(String input, String expectedToString) {

        VersionExpander ve = VersionExpansionParser.parseVersionExpander(input);

        assertNotNull(ve);

        assertEquals(expectedToString, ve.toString());

    }



    private void checkForError(String expansionString, String expectedErrorSubstring) {

        try {

            VersionExpansionParser.parseVersionExpander(expansionString);

            fail("Should have failed to parse " + expansionString);

        } catch (VersionExpansionFormatException vefe) {

            if (vefe.getMessage().indexOf(expectedErrorSubstring) == -1) {

                System.out.println(vefe.getMessage());

                fail("Did not get expected text in the exception string.\nExpected: '" + expectedErrorSubstring + "'\n  Actual:'" + vefe.getMessage()

                    + "'");

            }

            assertEquals(expansionString, vefe.getExpansionString());

        }

    }

}

