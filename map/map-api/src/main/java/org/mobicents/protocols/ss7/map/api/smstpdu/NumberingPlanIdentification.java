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

package org.mobicents.protocols.ss7.map.api.smstpdu;

/**
 * Bits 3 2 1 0 0 0 0 0 Unknown 0 0 0 1 ISDN/telephone numbering plan (E.164 [17]/E.163[18]) 0 0 1 1 Data numbering plan (X.121)
 * 0 1 0 0 Telex numbering plan 0 1 0 1 Service Centre Specific plan 1) 0 1 1 0 Service Centre Specific plan 1) 1 0 0 0 National
 * numbering plan 1 0 0 1 Private numbering plan 1 0 1 0 ERMES numbering plan (ETSI DE/PS 3 01-3) 1 1 1 1 Reserved for extension
 * All other values are reserved.
 *
 * @author sergey vetyutnev
 *
 */
public enum NumberingPlanIdentification {

    Unknown(0), ISDNTelephoneNumberingPlan(1), DataNumberingPlan(3), TelexNumberingPlan(4), ServiceCentreSpecificPlan1(5), ServiceCentreSpecificPlan2(
            6), NationalNumberingPlan(8), PrivateNumberingPlan(9), ERMESNumberingPlan(10), Reserved(15);

    private int code;

    private NumberingPlanIdentification(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static NumberingPlanIdentification getInstance(int code) {
        switch (code) {
            case 0:
                return Unknown;
            case 1:
                return ISDNTelephoneNumberingPlan;
            case 3:
                return DataNumberingPlan;
            case 4:
                return TelexNumberingPlan;
            case 5:
                return ServiceCentreSpecificPlan1;
            case 6:
                return ServiceCentreSpecificPlan2;
            case 8:
                return NationalNumberingPlan;
            case 9:
                return PrivateNumberingPlan;
            case 10:
                return ERMESNumberingPlan;
            default:
                return Reserved;
        }
    }
}