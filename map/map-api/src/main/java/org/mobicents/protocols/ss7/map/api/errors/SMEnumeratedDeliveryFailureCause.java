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

package org.mobicents.protocols.ss7.map.api.errors;

/**
 *
 * SM-DeliveryFailureCause ::= SEQUENCE { sm-EnumeratedDeliveryFailureCause SM-EnumeratedDeliveryFailureCause, diagnosticInfo
 * SignalInfo OPTIONAL, extensionContainer ExtensionContainer OPTIONAL, ...}
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum SMEnumeratedDeliveryFailureCause {
    memoryCapacityExceeded(0), equipmentProtocolError(1), equipmentNotSMEquipped(2), unknownServiceCentre(3), scCongestion(4), invalidSMEAddress(
            5), subscriberNotSCSubscriber(6);

    private int code;

    private SMEnumeratedDeliveryFailureCause(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static SMEnumeratedDeliveryFailureCause getInstance(int code) {
        switch (code) {
            case 0:
                return memoryCapacityExceeded;
            case 1:
                return equipmentProtocolError;
            case 2:
                return equipmentNotSMEquipped;
            case 3:
                return unknownServiceCentre;
            case 4:
                return scCongestion;
            case 5:
                return invalidSMEAddress;
            case 6:
                return subscriberNotSCSubscriber;
            default:
                return null;
        }
    }
}
