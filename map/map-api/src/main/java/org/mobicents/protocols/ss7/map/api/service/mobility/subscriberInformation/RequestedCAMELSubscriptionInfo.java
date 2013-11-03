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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation;

/**
 *
 RequestedCAMEL-SubscriptionInfo ::= ENUMERATED { o-CSI (0), t-CSI (1), vt-CSI (2), tif-CSI (3), gprs-CSI (4), mo-sms-CSI (5),
 * ss-CSI (6), m-CSI (7), d-csi (8)}
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum RequestedCAMELSubscriptionInfo {
    oCSI(0), tCSI(1), vtCSI(2), tifCSI(3), gprsCSI(4), moSmsCSI(5), ssCSI(6), mCSI(7), dcsi(8);

    private int code;

    private RequestedCAMELSubscriptionInfo(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static RequestedCAMELSubscriptionInfo getInstance(int code) {
        switch (code) {
            case 0:
                return RequestedCAMELSubscriptionInfo.oCSI;
            case 1:
                return RequestedCAMELSubscriptionInfo.tCSI;
            case 2:
                return RequestedCAMELSubscriptionInfo.vtCSI;
            case 3:
                return RequestedCAMELSubscriptionInfo.tifCSI;
            case 4:
                return RequestedCAMELSubscriptionInfo.gprsCSI;
            case 5:
                return RequestedCAMELSubscriptionInfo.moSmsCSI;
            case 6:
                return RequestedCAMELSubscriptionInfo.ssCSI;
            case 7:
                return RequestedCAMELSubscriptionInfo.mCSI;
            case 8:
                return RequestedCAMELSubscriptionInfo.dcsi;
            default:
                return null;
        }
    }
}
