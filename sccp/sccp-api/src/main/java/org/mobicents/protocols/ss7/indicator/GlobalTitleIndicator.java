/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.indicator;

/**
 *
 * @author kulikov
 */
public enum GlobalTitleIndicator {
    NO_GLOBAL_TITLE_INCLUDED(0), GLOBAL_TITLE_INCLUDES_NATURE_OF_ADDRESS_INDICATOR_ONLY(1), GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY(
            2), GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME(3), GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS(
            4);

    private int value;

    private GlobalTitleIndicator(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static GlobalTitleIndicator valueOf(int v) {
        switch (v) {
            case 0:
                return NO_GLOBAL_TITLE_INCLUDED;
            case 1:
                return GLOBAL_TITLE_INCLUDES_NATURE_OF_ADDRESS_INDICATOR_ONLY;
            case 2:
                return GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY;
            case 3:
                return GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME;
            case 4:
                return GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS;
            default:
                return null;
        }
    }
}
