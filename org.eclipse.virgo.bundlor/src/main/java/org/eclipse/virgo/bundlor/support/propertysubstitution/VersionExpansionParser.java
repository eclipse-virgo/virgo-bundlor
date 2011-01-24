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



import java.util.ArrayList;

import java.util.List;



import org.eclipse.virgo.bundlor.support.propertysubstitution.Token.TokenKind;



/**

 * Parser for the version expansion format, which looks like "[=.=,+1.+2]". Once parsed the resultant VersionExpander

 * object can be used to produce the expansion of some maj.min.mic.qual version number. See

 * {@link VersionExpansionParserTests} for examples of the format and usage.

 * 

 * @author Andy Clement

 */

final class VersionExpansionParser {



    private final String expansionString;



    private char[] data;



    private int dataLength;



    private int pos;



    private List<Token> tokens = new ArrayList<Token>();



    private int currentTokenPointer;



    private int tokenCount;



    private boolean startInclusive; // false = startExclusive



    private boolean endInclusive; // false = endExclusive



    private int currentVersionComponent = 0;



    // keep track of whether the data on the left (lower) or right (upper) of

    // the comma is being processed

    private boolean devouringLowerLimit;



    private List<Transformer> lowerVersionTransformers = new ArrayList<Transformer>();



    private List<Transformer> upperVersionTransformers = new ArrayList<Transformer>();



    // example input: "(=.=.=.=, +1.0.0]"

    private VersionExpansionParser(String expansion) {

        this.expansionString = expansion;

    }



    /**

     * Entrypoint to the VersionExpansionParser that attempts to parse the expansion string supplied. It will either

     * return a reusable VersionExpander object that captures the transformation described in the string or throw

     * VersionExpansionFormatException if there is a problem with parsing.

     * 

     * @param expansion the expansion formatted string to parse

     * @return a reusable VersionExpander

     * @throws VersionExpansionFormatException

     */

    public static VersionExpander parseVersionExpander(String expansion) throws VersionExpansionFormatException {

        return new VersionExpansionParser(expansion).parseExpansion();

    }



    private VersionExpander parseExpansion() throws VersionExpansionFormatException {

        if (!expansionString.contains(",")) {

            throw new VersionExpansionFormatException(expansionString, "missing comma in input data '" + expansionString + "'");

        }

        data = (expansionString + "\0").toCharArray(); // TODO could use sbuffer

        dataLength = data.length;

        pos = 0;

        lex();

        parse();

        return new VersionExpander(startInclusive, this.lowerVersionTransformers, this.upperVersionTransformers, endInclusive);

    }



    // useful for debugging:

    // private void printTokens() {

    // for (Token t : tokens) {

    // System.out.print(t.kind + " "

    // + expansionString.substring(t.start, t.end + 1) + " ("

    // + t.start + "," + t.end + ") ");

    // }

    // System.out.println();

    // }



    private void parse() {

        tokenCount = tokens.size();

        currentTokenPointer = 0;

        eatRangeStart();

        devouringLowerLimit = true;

        currentVersionComponent = 0;

        do {

            eatModifier();

        } while (maybeEatDot());

        eatComma();

        devouringLowerLimit = false;

        currentVersionComponent = 0;

        do {

            eatModifier();

        } while (maybeEatDot());

        eatRangeEnd();

    }



    // Expects either '[' or '('

    private void eatRangeStart() {

        Token token = tokens.get(currentTokenPointer++);

        if (token.kind == TokenKind.STARTINCLUSIVE) {

            startInclusive = true;

        } else if (token.kind == TokenKind.STARTEXCLUSIVE) {

            startInclusive = false;

        } else {

            raiseParseProblem("expected a version start character '[' or '(' but found '" + string(token) + "' at position " + token.start,

                token.start);

        }

    }



    // extract piece of expansion string for the specified token

    private String string(Token t) {

        return expansionString.substring(t.start, t.end);

    }



    // Expects ')' or ']'

    private void eatRangeEnd() {

        Token token = tokens.get(currentTokenPointer++);

        if (token.kind == TokenKind.ENDINCLUSIVE) {

            endInclusive = true;

        } else if (token.kind == TokenKind.ENDEXCLUSIVE) {

            endInclusive = false;

        } else {

            raiseParseProblem("expected a version end character ']' or ')' but found '" + string(token) + "' at position " + token.start, token.start);

        }

    }



    // add a transformer to either the lower or upper version list

    private void pushIt(Transformer transformer) {

        if (devouringLowerLimit) {

            lowerVersionTransformers.add(transformer);

        } else {

            upperVersionTransformers.add(transformer);

        }

        currentVersionComponent++;

    }



    // processing '='

    private void processEquals() {

        pushIt(IdentityTransformer.instance);

    }



    // processing a modifier like '+3' or '-2'

    private void processNumericModifier() {

        Token token = tokens.get(currentTokenPointer);

        if (currentVersionComponent == 3) {

            raiseParseProblem("cannot specify a numerical +/- value for the qualifier, found '" + string(token) + "' at position " + token.start,

                token.start);

        }

        String tokenString = string(token);

        try {

            Integer value = null;

            if (token.kind == TokenKind.PLUSNUMBER) {

                value = Integer.parseInt(tokenString.substring(1));

            } else {

                value = Integer.parseInt(tokenString);

            }

            pushIt(new SumTransformer(value));

        } catch (NumberFormatException nfe) {

            raiseParseProblem("cannot parse numerical value '" + tokenString + "' at position " + token.start, token.start);

        }

    }



    // process any number that might be following a macro (eg. maj+3)

    private void processPossibleNumber(String data, int position) {

        Token t = tokens.get(currentTokenPointer);

        try {

            char possiblePlus = data.charAt(position);

            Integer value = null;

            if (possiblePlus == '+') {

                String number = data.substring(position + 1);

                if (number.length() > 0 && number.charAt(0) == '-') {

                    raiseParseProblem(

                        "numeric modifier for macro should be +nnn or -nnn.  This '" + data + " is not allowed, at position " + t.start, t.start);

                }

                value = Integer.parseInt(data.substring(position + 1));

            } else {

                if (possiblePlus != '-') {

                    raiseParseProblem(

                        "numeric modifier for macro should be +nnn or -nnn.  This '" + data + " is not allowed, at position " + t.start, t.start);

                }

                value = Integer.parseInt(data.substring(position));

            }

            pushIt(new SumTransformer(value));

        } catch (NumberFormatException nfe) {

            raiseParseProblem("unable to determine the numeric modifier for macro.  Macro was '" + data + "' at position " + t.start, t.start);

        }

    }



    private void processWord() {

        Token token = tokens.get(currentTokenPointer);

        String tokenString = string(token);

        // Recognize the macros:

        // * maj: =.0.0

        // * min: =.=.0

        // * mic: =.=.=

        // * qual: =.=.=.=

        if (tokenString.startsWith("maj")) {

            if (tokenString.length() == 3) {

                pushIt(IdentityTransformer.instance);

            } else {

                processPossibleNumber(tokenString, 3);

            }

            pushIt(new ReplacementTransformer("0"));

            pushIt(new ReplacementTransformer("0"));

        } else if (tokenString.startsWith("min")) {

            pushIt(IdentityTransformer.instance);

            if (tokenString.length() == 3) {

                pushIt(IdentityTransformer.instance);

            } else {

                processPossibleNumber(tokenString, 3);

            }

            pushIt(new ReplacementTransformer("0"));

        } else if (tokenString.startsWith("mic")) {

            pushIt(IdentityTransformer.instance);

            pushIt(IdentityTransformer.instance);

            if (tokenString.length() == 3) {

                pushIt(IdentityTransformer.instance);

            } else {

                processPossibleNumber(tokenString, 3);

            }

        } else if (tokenString.equals("qual")) {

            pushIt(IdentityTransformer.instance);

            pushIt(IdentityTransformer.instance);

            pushIt(IdentityTransformer.instance);

            pushIt(IdentityTransformer.instance);

        } else {

            if (currentVersionComponent < 3) {

                raiseParseProblem("expected one of: '=' '+nnn' '-nnn' or 'nnn' but found '" + string(token) + "' at position " + token.start,

                    token.start);

            }

            pushIt(new ReplacementTransformer(tokenString));

        }

    }



    // process a numeric replacement for a value, eg. '4'

    private void processNumeric() {

        Token token = tokens.get(currentTokenPointer);

        String tokenString = string(token);

        pushIt(new ReplacementTransformer(tokenString));

    }



    // expect one of: EQUALS, WORD, NUMBER, PLUSNUMBER, NEGATIVENUMBER

    private void eatModifier() {

        if (currentTokenPointer >= tokenCount) {

            raiseParseProblem("run out of tokens to process", expansionString.length());

        }

        Token token = tokens.get(currentTokenPointer);

        TokenKind k = token.kind;

        if (currentVersionComponent > 3) {

            raiseParseProblem("too many version components specified, only major.minor.micro.qualifier is allowed.  Found '" + string(token)

                + "' at position " + token.start, token.start);

        }

        if (k == TokenKind.EQUALS) {

            processEquals();

        } else if (k == TokenKind.WORD) {

            processWord();

        } else if (k == TokenKind.NUMBER) {

            processNumeric();

        } else if (k == TokenKind.PLUSNUMBER || k == TokenKind.NEGATIVENUMBER) {

            processNumericModifier();

        } else {

            if (currentVersionComponent < 3) {

                raiseParseProblem("expected one of: '=' '+nnn' '-nnn' or 'nnn' but found '" + string(token) + "' at position " + token.start,

                    token.start);

            } else {

                raiseParseProblem("expected one of: '=' '+nnn' '-nnn' 'nnn' or 'xxx' but found '" + string(token) + "' at position " + token.start,

                    token.start);

            }

        }

        currentTokenPointer++;

    }



    private void eatComma() {

        Token token = tokens.get(currentTokenPointer);

        if (token.kind == TokenKind.COMMA) {

            currentTokenPointer++;

        } else {

            raiseParseProblem("expected a comma but found " + string(token) + " at position " + token.start, token.start);

        }

    }



    private boolean maybeEatDot() {

        if (currentTokenPointer >= tokenCount) {

            raiseParseProblem("run out of tokens to process whilst expecting '.'", expansionString.length());

        }

        Token token = tokens.get(currentTokenPointer);

        if (token.kind == TokenKind.DOT) {

            currentTokenPointer++;

            return true;

        } else {

            return false;

        }

    }



    private void lex() {

        while (pos < dataLength) {

            char ch = data[pos];

            if (isDigit(ch)) {

                lexNumber();

            } else if (isComma(ch)) {

                lexComma();

            } else if (isRangeDelimiter(ch)) {

                lexRangeDelimiter();

            } else if (isDot(ch)) {

                lexDot();

            } else if (isEquals(ch)) {

                lexEquals();

            } else if (isPlus(ch)) {

                lexPositiveNumber();

            } else if (isMinus(ch)) {

                lexNegativeNumber();

            } else if (isSpace(ch)) {

                pos++;

            } else if (ch == 0) {

                break;

            } else {

                lexWord();

            }

        }

    }



    private boolean isRangeDelimiter(char ch) {

        return "[]()".indexOf(ch) != -1;

    }



    private void lexRangeDelimiter() {

        switch (data[pos]) {

            case '[':

                tokens.add(new Token(TokenKind.STARTINCLUSIVE, pos, pos));

                break;

            case ']':

                tokens.add(new Token(TokenKind.ENDINCLUSIVE, pos, pos));

                break;

            case '(':

                tokens.add(new Token(TokenKind.STARTEXCLUSIVE, pos, pos));

                break;

            case ')':

                tokens.add(new Token(TokenKind.ENDEXCLUSIVE, pos, pos));

                break;

        }

        pos++;

    }



    private void lexWord() {

        int qualifierStart = pos;

        char ch = 0;

        do {

            ch = data[++pos];

            if (isSpace(ch) || isDot(ch) || isComma(ch) || isRangeDelimiter(ch) || ch == 0) {

                break;

            }

            // can never hit pos==data.length as 0 terminal added prior to lex

            // and that triggers the break condition above

        } while (pos < data.length);

        tokens.add(new Token(TokenKind.WORD, qualifierStart, pos));

    }



    private void lexNumber() {

        int numberStart = pos;

        readDigits();

        tokens.add(new Token(TokenKind.NUMBER, numberStart, pos));

    }



    private void lexPositiveNumber() {

        int numberStart = pos;

        readDigits();

        tokens.add(new Token(TokenKind.PLUSNUMBER, numberStart, pos));

    }



    private void lexNegativeNumber() {

        int numberStart = pos;

        readDigits();

        tokens.add(new Token(TokenKind.NEGATIVENUMBER, numberStart, pos));

    }



    private void readDigits() {

        while (isDigit(data[++pos]))

            ;

    }



    private void lexDot() {

        tokens.add(new Token(TokenKind.DOT, pos, ++pos));

    }



    private void lexEquals() {

        tokens.add(new Token(TokenKind.EQUALS, pos, ++pos));

    }



    private void lexComma() {

        tokens.add(new Token(TokenKind.COMMA, pos, ++pos));

    }



    private boolean isSpace(char ch) {

        return ch == ' ';

    }



    private boolean isEquals(char ch) {

        return ch == '=';

    }



    private boolean isPlus(char ch) {

        return ch == '+';

    }



    private boolean isMinus(char ch) {

        return ch == '-';

    }



    private boolean isDigit(char ch) {

        return ch >= '0' && ch <= '9';

    }



    private boolean isDot(char ch) {

        return ch == '.';

    }



    private boolean isComma(char ch) {

        return ch == ',';

    }



    private void raiseParseProblem(String message, int position) {

        // Augment message with context

        StringBuffer sb = new StringBuffer();

        sb.append(expansionString).append("\n");

        for (int i = 0; i < position; i++) {

            sb.append(" ");

        }

        sb.append("^\n");

        sb.append(message);

        throw new VersionExpansionFormatException(expansionString, sb.toString());

    }



}

