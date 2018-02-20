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