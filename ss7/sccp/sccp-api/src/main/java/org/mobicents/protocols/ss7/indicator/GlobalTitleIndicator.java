/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.protocols.ss7.indicator;

/**
 *
 * @author kulikov
 */
public enum GlobalTitleIndicator {
    NO_GLOBAL_TITLE_INCLUDED(0), 
    GLOBAL_TITLE_INCLUDES_NATURE_OF_ADDRESS_INDICATOR_ONLY(1),
    GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY(2),
    GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME(3),
    GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS(4);
    
    private int value;
    
    private GlobalTitleIndicator(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    public static GlobalTitleIndicator valueOf(int v) {
        switch (v) {
            case 0 : return NO_GLOBAL_TITLE_INCLUDED;
            case 1 : return GLOBAL_TITLE_INCLUDES_NATURE_OF_ADDRESS_INDICATOR_ONLY;
            case 2 : return GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY;
            case 3 : return GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME;
            case 4 : return GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS;
            default : return null;
        }
    }
}
