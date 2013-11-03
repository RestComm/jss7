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
 *
 Bit 1 Bit 0 Indication Type: 0 0 Voicemail MessageWaiting 0 1 Fax MessageWaiting 1 0 Electronic Mail MessageWaiting 1 1 Other
 * MessageWaiting* Mobile manufacturers may implement the "Other MessageWaiting" indication as an additional indication without
 * specifying the meaning.
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum DataCodingSchemaIndicationType {
    VoicemailMessageWaiting(0), FaxMessageWaiting(1), ElectronicMailMessageWaiting(2), OtherMessageWaiting(3);

    private int code;

    private DataCodingSchemaIndicationType(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static DataCodingSchemaIndicationType getInstance(int code) {
        switch (code) {
            case 0:
                return VoicemailMessageWaiting;
            case 1:
                return FaxMessageWaiting;
            case 2:
                return ElectronicMailMessageWaiting;
            default:
                return OtherMessageWaiting;
        }
    }
}