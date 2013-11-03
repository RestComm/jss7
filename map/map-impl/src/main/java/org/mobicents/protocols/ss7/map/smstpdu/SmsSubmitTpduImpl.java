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
import org.mobicents.protocols.ss7.map.api.smstpdu.ProtocolIdentifier;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsSubmitTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsTpduType;
import org.mobicents.protocols.ss7.map.api.smstpdu.UserData;
import org.mobicents.protocols.ss7.map.api.smstpdu.ValidityEnhancedFormatData;
import org.mobicents.protocols.ss7.map.api.smstpdu.ValidityPeriod;
import org.mobicents.protocols.ss7.map.api.smstpdu.ValidityPeriodFormat;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SmsSubmitTpduImpl extends SmsTpduImpl implements SmsSubmitTpdu {

    private boolean rejectDuplicates;
    private ValidityPeriodFormat validityPeriodFormat;
    private boolean replyPathExists;
    private boolean userDataHeaderIndicator;
    private boolean statusReportRequest;
    private int messageReference;
    private AddressField destinationAddress;
    private ProtocolIdentifier protocolIdentifier;
    private DataCodingScheme dataCodingScheme;
    private ValidityPeriod validityPeriod;
    private int userDataLength;
    private UserData userData;

    private SmsSubmitTpduImpl() {
        this.tpduType = SmsTpduType.SMS_SUBMIT;
        this.mobileOriginatedMessage = true;
    }

    public SmsSubmitTpduImpl(boolean rejectDuplicates, boolean replyPathExists, boolean statusReportRequest,
            int messageReference, AddressField destinationAddress, ProtocolIdentifier protocolIdentifier,
            ValidityPeriod validityPeriod, UserData userData) {
        this();

        this.rejectDuplicates = rejectDuplicates;
        this.replyPathExists = replyPathExists;
        this.statusReportRequest = statusReportRequest;
        this.messageReference = messageReference;
        this.destinationAddress = destinationAddress;
        this.protocolIdentifier = protocolIdentifier;
        this.validityPeriod = validityPeriod;
        this.userData = userData;
    }

    public SmsSubmitTpduImpl(byte[] data, Charset gsm8Charset) throws MAPException {
        this();

        if (data == null)
            throw new MAPException("Error creating a new SmsSubmitTpdu instance: data is empty");
        if (data.length < 1)
            throw new MAPException("Error creating a new SmsSubmitTpdu instance: data length is equal zero");

        ByteArrayInputStream stm = new ByteArrayInputStream(data);

        int bt = stm.read();
        if ((bt & _MASK_TP_RD) != 0)
            this.rejectDuplicates = true;
        int code = (bt & _MASK_TP_VPF) >> 3;
        this.validityPeriodFormat = ValidityPeriodFormat.getInstance(code);
        if ((bt & _MASK_TP_RP) != 0)
            this.replyPathExists = true;
        if ((bt & _MASK_TP_UDHI) != 0)
            this.userDataHeaderIndicator = true;
        if ((bt & _MASK_TP_SRR) != 0)
            this.statusReportRequest = true;

        this.messageReference = stm.read();
        if (this.messageReference == -1)
            throw new MAPException("Error creating a new SmsSubmitTpdu instance: messageReference field has not been found");

        this.destinationAddress = AddressFieldImpl.createMessage(stm);

        bt = stm.read();
        if (bt == -1)
            throw new MAPException("Error creating a new SmsSubmitTpdu instance: protocolIdentifier field has not been found");
        this.protocolIdentifier = new ProtocolIdentifierImpl(bt);

        bt = stm.read();
        if (bt == -1)
            throw new MAPException("Error creating a new SmsSubmitTpdu instance: dataCodingScheme field has not been found");
        this.dataCodingScheme = new DataCodingSchemeImpl(bt);

        switch (this.validityPeriodFormat) {
            case fieldPresentRelativeFormat:
                bt = stm.read();
                if (bt == -1)
                    throw new MAPException(
                            "Error creating a new SmsSubmitTpdu instance: validityPeriodFormat-fieldPresentEnhancedFormat field has not been found");
                this.validityPeriod = new ValidityPeriodImpl(bt);
                break;
            case fieldPresentAbsoluteFormat:
                AbsoluteTimeStamp ats = AbsoluteTimeStampImpl.createMessage(stm);
                this.validityPeriod = new ValidityPeriodImpl(ats);
                break;
            case fieldPresentEnhancedFormat:
                byte[] buf = new byte[7];
                try {
                    stm.read(buf);
                } catch (IOException e) {
                    throw new MAPException(
                            "IOException while creating a new SmsSubmitTpdu instance - decoding validityPeriodFormat-fieldPresentAbsoluteFormat: "
                                    + e.getMessage(), e);
                }
                ValidityEnhancedFormatData vef = new ValidityEnhancedFormatDataImpl(buf);
                this.validityPeriod = new ValidityPeriodImpl(vef);
                break;
        }

        this.userDataLength = stm.read();
        if (this.userDataLength == -1)
            throw new MAPException("Error creating a new SmsDeliverTpduImpl instance: userDataLength field has not been found");

        int avail = stm.available();
        byte[] buf = new byte[avail];
        try {
            stm.read(buf);
        } catch (IOException e) {
            throw new MAPException("IOException while creating a new SmsDeliverTpdu instance: " + e.getMessage(), e);
        }
        userData = new UserDataImpl(buf, dataCodingScheme, userDataLength, userDataHeaderIndicator, gsm8Charset);
    }

    public boolean getRejectDuplicates() {
        return this.rejectDuplicates;
    }

    public ValidityPeriodFormat getValidityPeriodFormat() {
        return this.validityPeriodFormat;
    }

    public boolean getReplyPathExists() {
        return this.replyPathExists;
    }

    public boolean getUserDataHeaderIndicator() {
        return this.userDataHeaderIndicator;
    }

    public boolean getStatusReportRequest() {
        return this.statusReportRequest;
    }

    public int getMessageReference() {
        return messageReference;
    }

    public AddressField getDestinationAddress() {
        return destinationAddress;
    }

    public ProtocolIdentifier getProtocolIdentifier() {
        return protocolIdentifier;
    }

    public DataCodingScheme getDataCodingScheme() {
        return dataCodingScheme;
    }

    public ValidityPeriod getValidityPeriod() {
        return validityPeriod;
    }

    public int getUserDataLength() {
        return userDataLength;
    }

    public UserData getUserData() {
        return userData;
    }

    public byte[] encodeData() throws MAPException {

        if (this.destinationAddress == null || this.protocolIdentifier == null || this.userData == null)
            throw new MAPException(
                    "Error encoding a SmsSubmitTpdu: destinationAddress, protocolIdentifier and userData must not be null");

        if (this.validityPeriod == null) {
            this.validityPeriodFormat = ValidityPeriodFormat.fieldNotPresent;
        } else if (this.validityPeriod.getRelativeFormatValue() != null) {
            this.validityPeriodFormat = ValidityPeriodFormat.fieldPresentRelativeFormat;
        } else if (this.validityPeriod.getAbsoluteFormatValue() != null) {
            this.validityPeriodFormat = ValidityPeriodFormat.fieldPresentAbsoluteFormat;
        } else if (this.validityPeriod.getEnhancedFormatValue() != null) {
            this.validityPeriodFormat = ValidityPeriodFormat.fieldPresentEnhancedFormat;
        } else {
            this.validityPeriodFormat = ValidityPeriodFormat.fieldNotPresent;
        }

        this.userData.encode();
        this.userDataHeaderIndicator = this.userData.getEncodedUserDataHeaderIndicator();
        this.userDataLength = this.userData.getEncodedUserDataLength();
        this.dataCodingScheme = this.userData.getDataCodingScheme();

        if (this.userData.getEncodedData().length > _UserDataLimit)
            throw new MAPException("User data field length may not increase " + _UserDataLimit);

        AsnOutputStream res = new AsnOutputStream();

        // byte 0
        res.write(SmsTpduType.SMS_SUBMIT.getEncodedValue() | (this.rejectDuplicates ? _MASK_TP_RD : 0)
                | (this.validityPeriodFormat.getCode() << 3) | (this.replyPathExists ? _MASK_TP_RP : 0)
                | (this.userDataHeaderIndicator ? _MASK_TP_UDHI : 0) | (this.statusReportRequest ? _MASK_TP_SRR : 0));

        res.write(this.messageReference);
        this.destinationAddress.encodeData(res);
        res.write(this.protocolIdentifier.getCode());
        res.write(this.dataCodingScheme.getCode());

        switch (this.validityPeriodFormat) {
            case fieldPresentRelativeFormat:
                res.write(this.validityPeriod.getRelativeFormatValue());
                break;
            case fieldPresentAbsoluteFormat:
                this.validityPeriod.getAbsoluteFormatValue().encodeData(res);
                break;
            case fieldPresentEnhancedFormat:
                res.write(this.validityPeriod.getEnhancedFormatValue().getData());
                break;
        }

        res.write(this.userDataLength);
        res.write(this.userData.getEncodedData());

        return res.toByteArray();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("SMS-SUBMIT tpdu [");

        boolean started = false;
        if (this.rejectDuplicates) {
            sb.append("rejectDuplicates");
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
        if (this.statusReportRequest) {
            if (started)
                sb.append(", ");
            sb.append("statusReportRequest");
            started = true;
        }

        if (started)
            sb.append(", ");
        sb.append("messageReference=");
        sb.append(this.messageReference);
        if (this.destinationAddress != null) {
            sb.append(", destinationAddress [");
            sb.append(this.destinationAddress.toString());
            sb.append("]");
        }
        if (this.protocolIdentifier != null) {
            sb.append(", ");
            sb.append(this.protocolIdentifier.toString());
        }
        if (this.validityPeriod != null) {
            sb.append(", ");
            sb.append(this.validityPeriod.toString());
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
