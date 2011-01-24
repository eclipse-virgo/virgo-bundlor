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

 * Represents a token lex'd from a version expansion string

 * 

 * @author Andy Clement

 */

final class Token {



    TokenKind kind;



    int start;



    int end;



    public Token(TokenKind kind, int start, int end) {

        this.kind = kind;

        this.start = start;

        this.end = end;

    }



    // The kinds of token that can be lex'd from an version expansion string

    static enum TokenKind {

        DOT, EQUALS, COMMA, WORD, NUMBER, PLUSNUMBER, NEGATIVENUMBER, STARTINCLUSIVE, ENDINCLUSIVE, STARTEXCLUSIVE, ENDEXCLUSIVE;

    }

}
