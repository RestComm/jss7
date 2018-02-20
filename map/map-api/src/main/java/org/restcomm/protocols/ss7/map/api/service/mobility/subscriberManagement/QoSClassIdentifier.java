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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

/**
 *
 QoS-Class-Identifier ::= INTEGER (1..9) -- values are defined in 3GPP TS 29.212 1 Conversational N/A N/A Speech 2
 * Conversational N/A N/A Unknown 3 Conversational N/A N/A Unknown 4 Streaming N/A N/A Unknown 5 Interactive 1 Yes N/A 6
 * Interactive 1 No N/A 7 Interactive 2 No N/A 8 Interactive 3 No N/A 9 Background N/A N/A N/A
 *
 * @author sergey vetyutnev
 *
 */
public enum QoSClassIdentifier {
    QCI_1(1), QCI_2(2), QCI_3(3), QCI_4(4), QCI_5(5), QCI_6(6), QCI_7(7), QCI_8(8), QCI_9(9);

    private int code;

    private QoSClassIdentifier(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static QoSClassIdentifier getInstance(int code) {
        switch (code) {
            case 1:
                return QoSClassIdentifier.QCI_1;
            case 2:
                return QoSClassIdentifier.QCI_2;
            case 3:
                return QoSClassIdentifier.QCI_3;
            case 4:
                return QoSClassIdentifier.QCI_4;
            case 5:
                return QoSClassIdentifier.QCI_5;
            case 6:
                return QoSClassIdentifier.QCI_6;
            case 7:
                return QoSClassIdentifier.QCI_7;
            case 8:
                return QoSClassIdentifier.QCI_8;
            case 9:
                return QoSClassIdentifier.QCI_9;
            default:
                return null;
        }
    }
}
