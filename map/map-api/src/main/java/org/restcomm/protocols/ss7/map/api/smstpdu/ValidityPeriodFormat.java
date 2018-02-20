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

package org.restcomm.protocols.ss7.map.api.smstpdu;

/**
 * The TP-Validity-Period-Format is a 2-bit field, located within bit no 3 and 4 of the first octet of SMS-SUBMIT, and to be
 * given the following values: bit4 bit3 0 0 TP-VP field not present 1 0 TP-VP field present - relative format 0 1 TP-VP field
 * present - enhanced format 1 1 TP-VP field present - absolute format
 *
 * @author sergey vetyutnev
 *
 */
public enum ValidityPeriodFormat {

    fieldNotPresent(0), fieldPresentEnhancedFormat(1), fieldPresentRelativeFormat(2), fieldPresentAbsoluteFormat(3);

    private int code;

    private ValidityPeriodFormat(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static ValidityPeriodFormat getInstance(int code) {
        switch (code) {
            case 0:
                return fieldNotPresent;
            case 1:
                return fieldPresentEnhancedFormat;
            case 2:
                return fieldPresentRelativeFormat;
            default:
                return fieldPresentAbsoluteFormat;
        }
    }
}
