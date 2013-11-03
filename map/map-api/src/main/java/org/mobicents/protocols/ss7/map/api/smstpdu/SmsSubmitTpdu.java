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
 * SMS-SUBMIT pdu
 *
 * @author sergey vetyutnev
 *
 */
public interface SmsSubmitTpdu extends SmsTpdu {

    /**
     * Mandatory. Parameter indicating whether or not the SC shall accept an SMS-SUBMIT for an SM still held in the SC, which
     * has the same TP-MR and the same TP-DA as a previously submitted SM from the same OA.
     *
     * @return TP-RD field
     */
    boolean getRejectDuplicates();

    /**
     * Mandatory. Parameter indicating whether or not the TP-VP field is present.
     *
     * @return TP-VPF field
     */
    ValidityPeriodFormat getValidityPeriodFormat();

    /**
     * Mandatory. Parameter indicating the request for a Reply-Path.
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
     * Optional. Parameter indicating if the MS is requesting a status report.
     *
     * @return TP-SRR field
     */
    boolean getStatusReportRequest();

    /**
     * Mandatory. Parameter identifying the SMS-SUBMIT
     *
     * @return TP-MR field
     */
    int getMessageReference();

    /**
     * Mandatory. Address of the destination SME
     *
     * @return TP-DA field
     */
    AddressField getDestinationAddress();

    /**
     * Mandatory. Parameter identifying the top layer protocol, if any.
     *
     * @return TP-PID field
     */
    ProtocolIdentifier getProtocolIdentifier();

    /**
     * Mandatory. Parameter identifying the coding scheme within the TP–User–Data.
     *
     * @return TP-DCS field
     */
    DataCodingScheme getDataCodingScheme();

    /**
     * Optional. Parameter identifying the time from where the message is no longer valid.
     *
     * @return TP-VP field
     */
    ValidityPeriod getValidityPeriod();

    /**
     * Mandatory. Parameter indicating the length of the TP-User-Data field to follow.
     *
     * @return TP-UDL field
     */
    int getUserDataLength();

    /**
     * Optional. Actual Data
     *
     * @return TP-UD field
     */
    UserData getUserData();

}