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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.smstpdu.AbsoluteTimeStamp;
import org.mobicents.protocols.ss7.map.api.smstpdu.AddressField;
import org.mobicents.protocols.ss7.map.api.smstpdu.DataCodingScheme;
import org.mobicents.protocols.ss7.map.api.smstpdu.ProtocolIdentifier;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsDeliverTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsTpduType;
import org.mobicents.protocols.ss7.map.api.smstpdu.UserData;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SmsDeliverTpduImpl extends SmsTpduImpl implements SmsDeliverTpdu {

    private boolean moreMessagesToSend;
    private boolean forwardedOrSpawned;
    private boolean replyPathExists;
    private boolean userDataHeaderIndicator;
    private boolean statusReportIndication;
    private AddressField originatingAddress;
    private ProtocolIdentifier protocolIdentifier;
    private DataCodingScheme dataCodingScheme;
    private AbsoluteTimeStamp serviceCentreTimeStamp;
    private int userDataLength;
    private UserData userData;

    private SmsDeliverTpduImpl() {
        this.tpduType = SmsTpduType.SMS_DELIVER;
        this.mobileOriginatedMessage = false;
    }

    public SmsDeliverTpduImpl(boolean moreMessagesToSend, boolean forwardedOrSpawned, boolean replyPathExists,
            boolean statusReportIndication, AddressField originatingAddress, ProtocolIdentifier protocolIdentifier,
            AbsoluteTimeStamp serviceCentreTimeStamp, UserData userData) {
        this();

        this.moreMessagesToSend = moreMessagesToSend;
        this.forwardedOrSpawned = forwardedOrSpawned;
        this.replyPathExists = replyPathExists;
        this.statusReportIndication = statusReportIndication;
        this.originatingAddress = originatingAddress;
        this.protocolIdentifier = protocolIdentifier;
        this.serviceCentreTimeStamp = serviceCentreTimeStamp;
        this.userData = userData;
    }

    public SmsDeliverTpduImpl(byte[] data, Charset gsm8Charset) throws MAPException {
        this();

        if (data == null)
            throw new MAPException("Error creating a new SmsDeliverTpduImpl instance: data is empty");
        if (data.length < 1)
            throw new MAPException("Error creating a new SmsDeliverTpduImpl instance: data length is equal zero");

        ByteArrayInputStream stm = new ByteArrayInputStream(data);

        int bt = stm.read();
        if ((bt & _MASK_TP_MMS) == 0)
            this.moreMessagesToSend = true;
        if ((bt & _MASK_TP_LP) != 0)
            this.forwardedOrSpawned = true;
        if ((bt & _MASK_TP_RP) != 0)
            this.replyPathExists = true;
        if ((bt & _MASK_TP_UDHI) != 0)
            this.userDataHeaderIndicator = true;
        if ((bt & _MASK_TP_SRI) != 0)
            this.statusReportIndication = true;

        this.originatingAddress = AddressFieldImpl.createMessage(stm);

        bt = stm.read();
        if (bt == -1)
            throw new MAPException(
                    "Error creating a new SmsDeliverTpduImpl instance: protocolIdentifier field has not been found");
        this.protocolIdentifier = new ProtocolIdentifierImpl(bt);

        bt = stm.read();
        if (bt == -1)
            throw new MAPException(
                    "Error creating a new SmsDeliverTpduImpl instance: dataCodingScheme field has not been found");
        this.dataCodingScheme = new DataCodingSchemeImpl(bt);

        this.serviceCentreTimeStamp = AbsoluteTimeStampImpl.createMessage(stm);

        this.userDataLength = stm.read();
        if (this.userDataLength == -1)
            throw new MAPException("Error creating a new SmsDeliverTpduImpl instance: userDataLength field has not been found");

        int avail = stm.available();
        byte[] buf = new byte[avail];
        try {
            stm.read(buf);
        } catch (IOException e) {
            throw new MAPException("IOException while creating a new SmsDeliverTpduImpl instance: " + e.getMessage(), e);
        }
        userData = new UserDataImpl(buf, dataCodingScheme, userDataLength, userDataHeaderIndicator, gsm8Charset);
    }

    public boolean getMoreMessagesToSend() {
        return this.moreMessagesToSend;
    }

    public boolean getForwardedOrSpawned() {
        return this.forwardedOrSpawned;
    }

    public boolean getReplyPathExists() {
        return this.replyPathExists;
    }

    public boolean getUserDataHeaderIndicator() {
        return this.userDataHeaderIndicator;
    }

    public boolean getStatusReportIndication() {
        return this.statusReportIndication;
    }

    public AddressField getOriginatingAddress() {
        return originatingAddress;
    }

    public ProtocolIdentifier getProtocolIdentifier() {
        return protocolIdentifier;
    }

    public DataCodingScheme getDataCodingScheme() {
        return dataCodingScheme;
    }

    public AbsoluteTimeStamp getServiceCentreTimeStamp() {
        return serviceCentreTimeStamp;
    }

    public int getUserDataLength() {
        return userDataLength;
    }

    public UserData getUserData() {
        return userData;
    }

    public byte[] encodeData() throws MAPException {

        if (this.originatingAddress == null || this.protocolIdentifier == null || this.serviceCentreTimeStamp == null
                || this.userData == null)
            throw new MAPException(
                    "Parameters originatingAddress, protocolIdentifier, serviceCentreTimeStamp and userData must not be null");

        this.userData.encode();
        this.userDataHeaderIndicator = this.userData.getEncodedUserDataHeaderIndicator();
        this.userDataLength = this.userData.getEncodedUserDataLength();
        this.dataCodingScheme = this.userData.getDataCodingScheme();

        if (this.userData.getEncodedData().length > _UserDataLimit)
            throw new MAPException("User data field length may not increase " + _UserDataLimit);

        ByteArrayOutputStream res = new ByteArrayOutputStream();

        // byte 0
        res.write(SmsTpduType.SMS_DELIVER.getEncodedValue() | (!this.moreMessagesToSend ? _MASK_TP_MMS : 0)
                | (this.forwardedOrSpawned ? _MASK_TP_LP : 0) | (this.replyPathExists ? _MASK_TP_RP : 0)
                | (this.userDataHeaderIndicator ? _MASK_TP_UDHI : 0) | (this.statusReportIndication ? _MASK_TP_SRI : 0));

        this.originatingAddress.encodeData(res);
        res.write(this.protocolIdentifier.getCode());
        res.write(this.dataCodingScheme.getCode());
        this.serviceCentreTimeStamp.encodeData(res);
        res.write(this.userDataLength);
        try {
            res.write(this.userData.getEncodedData());
        } catch (IOException e) {
            // This can not occur
        }

        return res.toByteArray();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("SMS-DELIVER tpdu [");

        boolean started = false;
        if (this.moreMessagesToSend) {
            sb.append("moreMessagesToSend");
            started = true;
        }
        if (this.forwardedOrSpawned) {
            if (started)
                sb.append(", ");
            sb.append("LoopPrevention");
            started = true;
        }
        if (this.replyPathExists) {
            if (started)
                sb.append(", ");
            sb.append("replyPathExists");
            started = true;
        }
        if (this.userDataHeaderIndicator) {
            if (started)
                sb.append(", ");
            sb.append("userDataHeaderIndicator");
            started = true;
        }
        if (this.statusReportIndication) {
            if (started)
                sb.append(", ");
            sb.append("statusReportIndication");
            started = true;
        }
        if (this.originatingAddress != null) {
            if (started)
                sb.append(", ");
            sb.append("originatingAddress [");
            sb.append(this.originatingAddress.toString());
            sb.append("]");
        }
        if (this.protocolIdentifier != null) {
            sb.append(", ");
            sb.append(this.protocolIdentifier.toString());
        }
        if (this.serviceCentreTimeStamp != null) {
            sb.append(", serviceCentreTimeStamp [");
            sb.append(this.serviceCentreTimeStamp.toString());
            sb.append("]");
        }
        if (this.userData != null) {
            sb.append("\nMSG [");
            try {
                this.userData.decode();
            } catch (MAPException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            sb.append(this.userData.toString());
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
}
