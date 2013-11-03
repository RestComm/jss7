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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

/**
 *
 MM-Code ::= OCTET STRING (SIZE (1)) -- This type is used to indicate a Mobility Management event. -- Actions for the
 * following MM-Code values are defined in CAMEL Phase 4: -- -- CS domain MM events: -- Location-update-in-same-VLR MM-Code ::=
 * '00000000'B -- Location-update-to-other-VLR MM-Code ::= '00000001'B -- IMSI-Attach MM-Code ::= '00000010'B --
 * MS-initiated-IMSI-Detach MM-Code ::= '00000011'B -- Network-initiated-IMSI-Detach MM-Code ::= '00000100'B -- -- PS domain MM
 * events: -- Routeing-Area-update-in-same-SGSN MM-Code ::= '10000000'B --
 * Routeing-Area-update-to-other-SGSN-update-from-new-SGSN -- MM-Code ::= '10000001'B --
 * Routeing-Area-update-to-other-SGSN-disconnect-by-detach -- MM-Code ::= '10000010'B -- GPRS-Attach MM-Code ::= '10000011'B --
 * MS-initiated-GPRS-Detach MM-Code ::= '10000100'B -- Network-initiated-GPRS-Detach MM-Code ::= '10000101'B --
 * Network-initiated-transfer-to-MS-not-reachable-for-paging -- MM-Code ::= '10000110'B -- -- If the MSC receives any other
 * MM-code than the ones listed above for the -- CS domain, then the MSC shall ignore that MM-code. -- If the SGSN receives any
 * other MM-code than the ones listed above for the -- PS domain, then the SGSN shall ignore that MM-code.
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum MMCodeValue {
    // -- CS domain MM events:
    LocationUpdateInSameVLR(0), // '00000000'B
    LocationUpdateToOtherVLR(1), // '00000001'B
    IMSIAttach(2), // '00000010'B
    MSInitiatedIMSIDetach(3), // '00000011'B
    NetworkInitiatedIMSIDetach(4), // '00000100'B

    // -- PS domain MM events:
    RouteingAreaUpdateInSameSGSN(128 + 0), // '10000000'B
    RouteingAreaUpdateToOtherSGSNUpdateFromNewSGSN(128 + 1), // '10000001'B
    RouteingAreaUpdateToOtherSGSNDisconnectByDetach(128 + 2), // '10000010'B
    GPRSAttach(128 + 3), // '10000011'B
    MSInitiatedGPRSDetach(128 + 4), // '10000100'B
    NetworkInitiatedGPRSDetach(128 + 5), // '10000101'B
    NetworkInitiatedTransferToMSNotReachableForPaging(128 + 6); // '10000110'B

    private int code;

    private MMCodeValue(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static MMCodeValue getInstance(int code) {
        switch (code) {
            case 0:
                return MMCodeValue.LocationUpdateInSameVLR;
            case 1:
                return MMCodeValue.LocationUpdateToOtherVLR;
            case 2:
                return MMCodeValue.IMSIAttach;
            case 3:
                return MMCodeValue.MSInitiatedIMSIDetach;
            case 4:
                return MMCodeValue.NetworkInitiatedIMSIDetach;

            case 128 + 0:
                return MMCodeValue.RouteingAreaUpdateInSameSGSN;
            case 128 + 1:
                return MMCodeValue.RouteingAreaUpdateToOtherSGSNUpdateFromNewSGSN;
            case 128 + 2:
                return MMCodeValue.RouteingAreaUpdateToOtherSGSNDisconnectByDetach;
            case 128 + 3:
                return MMCodeValue.GPRSAttach;
            case 128 + 4:
                return MMCodeValue.MSInitiatedGPRSDetach;
            case 128 + 5:
                return MMCodeValue.NetworkInitiatedGPRSDetach;
            case 128 + 6:
                return MMCodeValue.NetworkInitiatedTransferToMSNotReachableForPaging;

            default:
                return null;
        }
    }
}
