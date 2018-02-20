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
 * Short message transfer protocol data unit informing the receiving MS of the status of a mobile originated short message
 * previously submitted by the MS, i.e. whether the SC was able to forward the message or not, or whether the message was stored
 * in the SC for later delivery.
 *
 * SMS-STATUS-REPORT pdu
 *
 * @author sergey vetyutnev
 *
 */
public interface SmsStatusReportTpdu extends SmsTpdu {

    /**
     * Parameter indicating that the TP–UD field contains a header.
     *
     * @return TP-UDHI field
     */
    boolean getUserDataHeaderIndicator();

    /**
     * Parameter indicating whether or not there are more messages to send.
     *
     * @return TP-MMS field
     */
    boolean getMoreMessagesToSend();

    /**
     * @return TP-LP field
     */
    boolean getForwardedOrSpawned();

    /**
     * Parameter indicating whether the previously submitted TPDU was an SMS_SUBMIT or an SMS_COMMAND
     *
     * @return TP-SRQ field
     */
    StatusReportQualifier getStatusReportQualifier();

    /**
     * Parameter identifying the previously submitted SMS_SUBMIT or SMS_COMMAND
     *
     * @return TP-MR field
     */
    int getMessageReference();

    /**
     * Address of the recipient of the previously submitted mobile originated short message
     *
     * @return TP-RA field
     */
    AddressField getRecipientAddress();

    /**
     * Parameter identifying time when the SC received the previously sent SMS_SUBMIT.
     *
     * @return TP-SCTS field
     */
    AbsoluteTimeStamp getServiceCentreTimeStamp();

    /**
     * <p>
     * Parameter identifying the time associated with a particular TP–ST outcome
     * </p>
     *
     * <p>
     * The TP-Discharge-Time field indicates the time at which a previously submitted SMS-SUBMIT was successfully delivered to
     * or attempted to deliver to the recipient SME or disposed of by the SC.
     * </p>
     *
     * <p>
     * In the case of "transaction completed" the time shall be the time of the completion of the transaction. In the case of
     * "SC still trying to transfer SM" the time shall be the time of the last transfer attempt. In the case of
     * "permanent or temporary error - SC not making any more transfer attempts" the time shall be the time of either the last
     * transfer attempt or the time at which the SC disposed of the SM according to the Status outcome in TP-ST.
     * </p>
     *
     * @return TP-DT field
     */
    AbsoluteTimeStamp getDischargeTime();

    /**
     * Parameter identifying the status of the previously sent mobile originated short message.
     *
     * @return TP-ST field
     */
    Status getStatus();

    /**
     * Parameter indicating the presence of any of the optional parameters that follow.
     *
     * @return TP-PI field
     */
    ParameterIndicator getParameterIndicator();

    /**
     * TP–PID of original SMS_SUBMIT.
     *
     * @return TP-PID field
     */
    ProtocolIdentifier getProtocolIdentifier();

    /**
     * Parameter identifying the coding scheme within the TP–UD.
     *
     * @return TP-DCS field
     */
    DataCodingScheme getDataCodingScheme();

    /**
     * Parameter identifying the length of the TP–UD field.
     *
     * @return TP-UDL field
     */
    int getUserDataLength();

    /**
     * Variable user data.
     *
     * @return TP-UD field
     */
    UserData getUserData();

}