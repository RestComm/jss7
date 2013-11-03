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

package org.mobicents.protocols.ss7.map.smstpdu;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.smstpdu.AbsoluteTimeStamp;
import org.mobicents.protocols.ss7.map.api.smstpdu.AddressField;
import org.mobicents.protocols.ss7.map.api.smstpdu.DataCodingScheme;
import org.mobicents.protocols.ss7.map.api.smstpdu.ParameterIndicator;
import org.mobicents.protocols.ss7.map.api.smstpdu.ProtocolIdentifier;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsStatusReportTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsTpduType;
import org.mobicents.protocols.ss7.map.api.smstpdu.Status;
import org.mobicents.protocols.ss7.map.api.smstpdu.StatusReportQualifier;
import org.mobicents.protocols.ss7.map.api.smstpdu.UserData;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SmsStatusReportTpduImpl extends SmsTpduImpl implements SmsStatusReportTpdu {

    private boolean userDataHeaderIndicator;
    private boolean moreMessagesToSend;
    private boolean forwardedOrSpawned;
    private StatusReportQualifier statusReportQualifier;
    private int messageReference;
    private AddressField recipientAddress;
    private AbsoluteTimeStamp serviceCentreTimeStamp;
    private AbsoluteTimeStamp dischargeTime;
    private Status status;
    private ParameterIndicator parameterIndicator;
    private ProtocolIdentifier protocolIdentifier;
    private DataCodingScheme dataCodingScheme;
    private int userDataLength;
    private UserData userData;

    private SmsStatusReportTpduImpl() {
        this.tpduType = SmsTpduType.SMS_STATUS_REPORT;
        this.mobileOriginatedMessage = false;
    }

    public SmsStatusReportTpduImpl(boolean moreMessagesToSend, boolean forwardedOrSpawned,
            StatusReportQualifier statusReportQualifier, int messageReference, AddressField recipientAddress,
            AbsoluteTimeStamp serviceCentreTimeStamp, AbsoluteTimeStamp dischargeTime, Status status,
            ProtocolIdentifier protocolIdentifier, UserData userData) {
        this();

        this.moreMessagesToSend = moreMessagesToSend;
        this.forwardedOrSpawned = forwardedOrSpawned;
        this.statusReportQualifier = statusReportQualifier;
        this.messageReference = messageReference;
        this.recipientAddress = recipientAddress;
        this.serviceCentreTimeStamp = serviceCentreTimeStamp;
        this.dischargeTime = dischargeTime;
        this.status = status;
        this.protocolIdentifier = protocolIdentifier;
        this.userData = userData;
    }

    public SmsStatusReportTpduImpl(byte[] data, Charset gsm8Charset) throws MAPException {
        this();

        if (data == null)
            throw new MAPException("Error creating a new SmsStatusReport instance: data is empty");
        if (data.length < 1)
            throw new MAPException("Error creating a new SmsStatusReport instance: data length is equal zero");

        ByteArrayInputStream stm = new ByteArrayInputStream(data);

        int bt = stm.read();
        if ((bt & _MASK_TP_UDHI) != 0)
            this.userDataHeaderIndicator = true;
        if ((bt & _MASK_TP_MMS) == 0)
            this.moreMessagesToSend = true;
        if ((bt & _MASK_TP_LP) != 0)
            this.forwardedOrSpawned = true;
        int code = (bt & _MASK_TP_SRQ) >> 5;
        this.statusReportQualifier = StatusReportQualifier.getInstance(code);

        this.messageReference = stm.read();
        if (this.messageReference == -1)
            throw new MAPException("Error creating a new SmsStatusReport instance: messageReference field has not been found");

        this.recipientAddress = AddressFieldImpl.createMessage(stm);
        this.serviceCentreTimeStamp = AbsoluteTimeStampImpl.createMessage(stm);
        this.dischargeTime = AbsoluteTimeStampImpl.createMessage(stm);

        bt = stm.read();
        if (bt == -1)
            throw new MAPException("Error creating a new SmsStatusReport instance: Status field has not been found");
        this.status = new StatusImpl(bt);

        bt = stm.read();
        if (bt == -1)
            this.parameterIndicator = new ParameterIndicatorImpl(0);
        else
            this.parameterIndicator = new ParameterIndicatorImpl(bt);

        if (this.parameterIndicator.getTP_PIDPresence()) {
            bt = stm.read();
            if (bt == -1)
                throw new MAPException(
                        "Error creating a new SmsStatusReport instance: protocolIdentifier field has not been found");
            this.protocolIdentifier = new ProtocolIdentifierImpl(bt);
        }

        if (this.parameterIndicator.getTP_DCSPresence()) {
            bt = stm.read();
            if (bt == -1)
                throw new MAPException(
                        "Error creating a new SmsStatusReport instance: dataCodingScheme field has not been found");
            this.dataCodingScheme = new DataCodingSchemeImpl(bt);
        }

        if (this.parameterIndicator.getTP_UDLPresence()) {
            this.userDataLength = stm.read();
            if (this.userDataLength == -1)
                throw new MAPException("Error creating a new SmsStatusReport instance: userDataLength field has not been found");

            int avail = stm.available();
            byte[] buf = new byte[avail];
            try {
                stm.read(buf);
            } catch (IOException e) {
                throw new MAPException("IOException while creating a new SmsStatusReport instance: " + e.getMessage(), e);
            }
            userData = new UserDataImpl(buf, dataCodingScheme, userDataLength, userDataHeaderIndicator, gsm8Charset);
        }
    }

    public boolean getUserDataHeaderIndicator() {
        return this.userDataHeaderIndicator;
    }

    public boolean getMoreMessagesToSend() {
        return this.moreMessagesToSend;
    }

    public boolean getForwardedOrSpawned() {
        return this.forwardedOrSpawned;
    }

    public StatusReportQualifier getStatusReportQualifier() {
        return this.statusReportQualifier;
    }

    public int getMessageReference() {
        return messageReference;
    }

    public AddressField getRecipientAddress() {
        return recipientAddress;
    }

    public AbsoluteTimeStamp getServiceCentreTimeStamp() {
        return serviceCentreTimeStamp;
    }

    public AbsoluteTimeStamp getDischargeTime() {
        return dischargeTime;
    }

    public Status getStatus() {
        return status;
    }

    public ParameterIndicator getParameterIndicator() {
        return parameterIndicator;
    }

    public ProtocolIdentifier getProtocolIdentifier() {
        return protocolIdentifier;
    }

    public DataCodingScheme getDataCodingScheme() {
        return dataCodingScheme;
    }

    public int getUserDataLength() {
        return userDataLength;
    }

    public UserData getUserData() {
        return userData;
    }

    public byte[] encodeData() throws MAPException {

        if (statusReportQualifier == null || this.recipientAddress == null || this.serviceCentreTimeStamp == null
                || this.dischargeTime == null || this.status == null)
            throw new MAPException(
                    "Error encoding a SmsStatusReportTpdu: statusReportQualifier, recipientAddress, serviceCentreTimeStamp, dischargeTime and status must no be null");

        if (this.userData != null) {
            this.userData.encode();
            this.userDataHeaderIndicator = this.userData.getEncodedUserDataHeaderIndicator();
            this.userDataLength = this.userData.getEncodedUserDataLength();
            this.dataCodingScheme = this.userData.getDataCodingScheme();
            if (this.userData.getEncodedData().length > _UserDataStatusReportLimit)
                throw new MAPException("User data field length may not increase " + _UserDataStatusReportLimit);
        }

        AsnOutputStream res = new AsnOutputStream();

        // byte 0
        res.write(SmsTpduType.SMS_COMMAND.getEncodedValue() | (this.userDataHeaderIndicator ? _MASK_TP_UDHI : 0)
                | (!this.moreMessagesToSend ? _MASK_TP_MMS : 0) | (this.forwardedOrSpawned ? _MASK_TP_LP : 0)
                | (this.statusReportQualifier.getCode() << 5));

        res.write(this.messageReference);
        this.recipientAddress.encodeData(res);
        this.serviceCentreTimeStamp.encodeData(res);
        this.dischargeTime.encodeData(res);
        res.write(this.status.getCode());

        this.parameterIndicator = new ParameterIndicatorImpl(this.userData != null, this.dataCodingScheme != null,
                this.protocolIdentifier != null);

        if (this.parameterIndicator.getCode() != 0) {
            res.write(this.parameterIndicator.getCode());
        }
        if (this.protocolIdentifier != null) {
            res.write(this.protocolIdentifier.getCode());
        }
        if (this.dataCodingScheme != null) {
            res.write(this.dataCodingScheme.getCode());
        }

        if (this.userData != null) {
            res.write(this.userDataLength);
            res.write(this.userData.getEncodedData());
        }

        return res.toByteArray();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("SMS-STATUS-REPORT tpdu [");

        boolean started = false;
        if (this.userDataHeaderIndicator) {
            sb.append("userDataHeaderIndicator");
            started = true;
        }
        if (this.moreMessagesToSend) {
            if (started)
                sb.append(", ");
            sb.append("moreMessagesToSend");
            started = true;
        }
        if (this.forwardedOrSpawned) {
            if (started)
                sb.append(", ");
            sb.append("forwardedOrSpawned");
            started = true;
        }
        if (this.statusReportQualifier != null) {
            if (started)
                sb.append(", ");
            sb.append("statusReportQualifier=");
            sb.append(this.statusReportQualifier);
        }

        sb.append(", messageReference=");
        sb.append(this.messageReference);
        if (this.recipientAddress != null) {
            sb.append(", recipientAddress [");
            sb.append(this.recipientAddress.toString());
            sb.append("]");
        }
        if (this.serviceCentreTimeStamp != null) {
            sb.append(", serviceCentreTimeStamp [");
            sb.append(this.serviceCentreTimeStamp.toString());
            sb.append("]");
        }
        if (this.dischargeTime != null) {
            sb.append(", dischargeTime [");
            sb.append(this.dischargeTime.toString());
            sb.append("]");
        }
        if (this.status != null) {
            sb.append(", ");
            sb.append(this.status.toString());
        }
        if (this.parameterIndicator != null) {
            sb.append(", ");
            sb.append(this.parameterIndicator.toString());
        }
        if (this.protocolIdentifier != null) {
            sb.append(", ");
            sb.append(this.protocolIdentifier.toString());
        }
        if (this.userData != null) {
            sb.append("\nMSG [");
            sb.append(this.userData.toString());
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
}
