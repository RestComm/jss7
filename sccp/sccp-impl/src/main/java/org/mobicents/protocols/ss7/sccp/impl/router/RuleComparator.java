/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.mobicents.protocols.ss7.sccp.impl.router;

import org.mobicents.protocols.ss7.sccp.OriginationType;

import java.util.Comparator;

/**
 * <p>
 * A comparison function which imposes ordering on collection of {@link RuleImpl} based on the GT digits defined while creating
 * rule. The sorting algo is as defined bellow
 * </p>
 * <p>
 * <ol>
 * <li>
 * GT digits having no wildcard (* or ?) is always on top of the list. Between two GT digits, both having no wildcards, one with
 * shortest length is on top of list. For example Digit1 "123456" will be above Digit2 "1234567890" will be above Digit3 "999/*"
 * </li>
 * <li>
 * GT digits having wildcard ? is always above digits having wildcard *. For example Digit1 "800/????/9" will be above Digit2
 * "999/*"</li>
 * <li>
 * Between two GT digits both having wildcard ?, one with least number of wildcard ? is on top of list. For example Digit1
 * "800/????/9" is above Digit2 "800/?????/9"</li>
 * <li>
 * Between two GT digits both having equal number of wildcard ?, the digit who's first appearance of ? is after other, is on top
 * of list. For example between Digit1 "80/??/0/???/9" and Digit 2 "800/?????/9", Digit2 is above Digit1
 * <li>
 * <li>
 * Between two GT digits both having wildcard *, the digit who's first appearance of * is after other. For example between Digit1
 * 80* and Digit 2 800*, Digit 2 is above Digit 1
 * </li>
 * </ol>
 * </p>
 *
 *
 * @author amit bhayani
 *
 */
public class RuleComparator implements Comparator<RuleImpl> {

    private static final String SECTION_SEPARTOR = "/";
    private static final char WILDCARD_ANY = '*';
    private static final String WILDCARD_ANY_STRING = "*";
    private static final char WILDCARD_SINGLE = '?';

    /*
     * (non-Javadoc)
     *
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(RuleImpl o1, RuleImpl o2) {

        String digits1 = o1.getPattern().getGlobalTitle().getDigits();
        String digits2 = o2.getPattern().getGlobalTitle().getDigits();

        // Normalize rule. Remove all separator
        digits1 = digits1.replaceAll(SECTION_SEPARTOR, "");
        digits2 = digits2.replaceAll(SECTION_SEPARTOR, "");

        // If a rule 1 is not OriginationType.All and rule 2 is OriginationType.All we put rule 1 first
        if (o1.getOriginationType() != OriginationType.ALL && o2.getOriginationType() == OriginationType.ALL)
            return -1;
        if (o1.getOriginationType() == OriginationType.ALL && o2.getOriginationType() != OriginationType.ALL)
            return 1;

        // Check if digits are exactly same. In that case we sort based on the callingDigits
        if ( digits1.equals( digits2 ) && (o1.getPatternCallingAddress() != null || o2.getPatternCallingAddress() != null )) {
                if ( o1.getPatternCallingAddress() != null &&
                        o2.getPatternCallingAddress() == null  ) {
                    return -1;
                } else if ( o1.getPatternCallingAddress() == null &&
                        o2.getPatternCallingAddress() != null ) {
                    return 1;
                }
                // both have calling party addresses. lets compare these 2
                digits1 = o1.getPatternCallingAddress().getGlobalTitle().getDigits();
                digits2 = o2.getPatternCallingAddress().getGlobalTitle().getDigits();

                // Normalize rule. Remove all separator
                digits1 = digits1.replaceAll(SECTION_SEPARTOR, "");
                digits2 = digits2.replaceAll(SECTION_SEPARTOR, "");

                return compareDigits( digits1, digits2 );
        }
        return compareDigits( digits1, digits2 );
    }

    private int compareDigits( String digits1, String digits2 ) {

        // If any digit is just wildcard "*" return 1 indicating it should be
        // below the other
        if (digits1.equals(WILDCARD_ANY_STRING)) {
            return 1;
        } else if (digits2.equals(WILDCARD_ANY_STRING)) {
            return -1;
        }

        // Truly speaking any digits will have just one asterisk max.
        int asterisksFound1 = this.numberOfAsterisks(digits1);
        int asterisksFound2 = this.numberOfAsterisks(digits2);

        if (asterisksFound1 == asterisksFound2) {
            // If both digits have "*", lets compare if there are wildcard "?"
            return compareQuestionMarks(digits1, digits2);
        } else if (asterisksFound1 < asterisksFound2) {
            // if digit 1 doesn't have *, return -1 indicating it should be
            // above other
            return -1;
        }

        return digits1.compareTo( digits2 );
//        return 1;
    }

    /**
     * Compare number of wildcards "?" in passed digits
     *
     * @param digits1
     * @param digits2
     * @return
     */
    private int compareQuestionMarks(String digits1, String digits2) {
        int questionMarksFound1 = this.numberOfQuestionMarks(digits1);
        int questionMarksFound2 = this.numberOfQuestionMarks(digits2);

        if (questionMarksFound1 == questionMarksFound2) {
            // If both the digits have equal number of "?", compare the index of
            // first occurrence of "?" in both digits. Digits for which "?"
            // occurred after other is on top
            return compareQuestionMarksIndex(digits1, 0, digits2, 0);
        } else if (questionMarksFound1 < questionMarksFound2) {
            return -1;
        }
        return 1;
    }

    /**
     *
     * @param digits1
     * @param fromIndex1
     * @param digits2
     * @param fromIndex2
     * @return
     */
    private int compareQuestionMarksIndex(String digits1, int fromIndex1, String digits2, int fromIndex2) {
        int index1 = digits1.indexOf(WILDCARD_SINGLE, fromIndex1);
        int index2 = digits2.indexOf(WILDCARD_SINGLE, fromIndex2);

        if (index1 == -1 && index2 == -1) {
            // If both digits have exactly same occurrence of "?", compare
            // lengths. Shortest length digit is above other
            return comapreLength(digits1, digits2);
        } else if (index1 == index2) {
            // Keep comparing occurrence of "?" till difference is reached
            compareQuestionMarksIndex(digits1, index1 + 1, digits2, index2 + 1);
        } else if (index1 < index2) {
            return 1;
        }
        return -1;
    }

    /**
     * Compare length of digits. Shorter length is above longer one
     *
     * @param digits1
     * @param digits2
     * @return
     */
    private int comapreLength(String digits1, String digits2) {

        if (digits1.length() == digits2.length()) {
            return 0;
        } else if (digits1.length() < digits2.length()) {
            return 1;
        } else if (digits1.length() > digits2.length()) {
            return -1;
        }

        return digits1.compareTo(digits2);
    }

    /**
     * Returns number of '*' occured in passed digits
     *
     * @param digits
     * @return
     */
    private int numberOfAsterisks(String digits) {
        int asterisksFound = 0;
        char[] chars = digits.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == WILDCARD_ANY) {
                asterisksFound++;
            }
        }
        return asterisksFound;
    }

    /**
     * Returns number of '?' occured in passed digits
     *
     * @param digits
     * @return
     */
    private int numberOfQuestionMarks(String digits) {
        int questionMarksFound = 0;
        char[] chars = digits.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == WILDCARD_SINGLE) {
                questionMarksFound++;
            }
        }
        return questionMarksFound;
    }

}
