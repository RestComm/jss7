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
 * SMS-DELIVER pdu
 *
 * @author sergey vetyutnev
 *
 */
public interface SmsDeliverTpdu extends SmsTpdu {

    /**
     * Mandatory. Parameter indicating whether or not there are more messages to send.
     *
     * @return TP-MMS field
     */
    boolean getMoreMessagesToSend();

    /**
     * @return TP-LP field false: The message has not been forwarded and is not a spawned message (or the sending network entity
     *         (e.g. an SC) does not support the setting of this bit.) true: The message has either been forwarded or is a
     *         spawned message.
     */
    boolean getForwardedOrSpawned();

    /**
     * Mandatory. Parameter indicating that Reply-Path exists.
     *
     * @return TP-RP field
     */
    boolean getReplyPathExists();

    /**
     * Optional. Parameter indicating that the TP-UD field contains a header.
     *
     * @return TP-UDHI field
     */
    boolean getUserDataHeaderIndicator();

    /**
     * Optional. Parameter indicating whether the SME has requested a status report.
     *
     * @return TP-SRI field
     */
    boolean getStatusReportIndication();

    /**
     * Mandatory. Address of the originating SME.
     *
     * @return TP-OA field
     */
    AddressField getOriginatingAddress();

    /**
     * Mandatory. Parameter identifying the top layer protocol, if any.
     *
     * @return TP-PID field
     */
    ProtocolIdentifier getProtocolIdentifier();

    /**
     * Mandatory. Parameter identifying the coding scheme within the TP-User-Data.
     *
     * @return TP-DCS field
     */
    DataCodingScheme getDataCodingScheme();

    /**
     * Mandatory. Parameter identifying time when the SC received the message.
     *
     * @return TP-SCTS field
     */
    AbsoluteTimeStamp getServiceCentreTimeStamp();

    /**
     * Mandatory. Parameter indicating the length of the TP-User-Data field to fol- low.
     *
     * @return TP-UDL field
     */
    int getUserDataLength();

    /**
     * Optional. The text or the data of the SMS
     *
     * @return TP-UD field
     */
    UserData getUserData();

}