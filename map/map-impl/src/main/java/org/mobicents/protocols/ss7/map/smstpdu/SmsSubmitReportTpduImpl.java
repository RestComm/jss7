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
import org.mobicents.protocols.ss7.map.api.smstpdu.DataCodingScheme;
import org.mobicents.protocols.ss7.map.api.smstpdu.FailureCause;
import org.mobicents.protocols.ss7.map.api.smstpdu.ParameterIndicator;
import org.mobicents.protocols.ss7.map.api.smstpdu.ProtocolIdentifier;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsSubmitReportTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsTpduType;
import org.mobicents.protocols.ss7.map.api.smstpdu.UserData;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SmsSubmitReportTpduImpl extends SmsTpduImpl implements SmsSubmitReportTpdu {

    private boolean userDataHeaderIndicator;
    private FailureCause failureCause;
    private ParameterIndicator parameterIndicator;
    private AbsoluteTimeStamp serviceCentreTimeStamp;
    private ProtocolIdentifier protocolIdentifier;
    private DataCodingScheme dataCodingScheme;
    private int userDataLength;
    private UserData userData;

    private SmsSubmitReportTpduImpl() {
        this.tpduType = SmsTpduType.SMS_SUBMIT_REPORT;
        this.mobileOriginatedMessage = false;
    }

    public SmsSubmitReportTpduImpl(FailureCause failureCause, AbsoluteTimeStamp serviceCentreTimeStamp,
            ProtocolIdentifier protocolIdentifier, UserData userData) {
        this();

        this.failureCause = failureCause;
        this.serviceCentreTimeStamp = serviceCentreTimeStamp;
        this.protocolIdentifier = protocolIdentifier;
        this.userData = userData;
    }

    public SmsSubmitReportTpduImpl(byte[] data, Charset gsm8Charset) throws MAPException {
        this();

        if (data == null)
            throw new MAPException("Error creating a new SmsSubmitReportTpdu instance: data is empty");
        if (data.length < 1)
            throw new MAPException("Error creating a new SmsSubmitReportTpdu instance: data length is equal zero");

        ByteArrayInputStream stm = new ByteArrayInputStream(data);

        int bt = stm.read();
        if ((bt & _MASK_TP_UDHI) != 0)
            this.userDataHeaderIndicator = true;

        bt = stm.read();
        if (bt == -1)
            throw new MAPException(
                    "Error creating a new SmsDeliverReportTpdu instance: Failure-Cause and Parameter-Indicator fields have not been found");
        if ((bt & 0x80) != 0) {
            // Failure-Cause exists
            this.failureCause = new FailureCauseImpl(bt);

            bt = stm.read();
            if (bt == -1)
                throw new MAPException(
                        "Error creating a new SmsDeliverReportTpdu instance: Parameter-Indicator field has not been found");
        }

        this.parameterIndicator = new ParameterIndicatorImpl(bt);

        this.serviceCentreTimeStamp = AbsoluteTimeStampImpl.createMessage(stm);

        if (this.parameterIndicator.getTP_PIDPresence()) {
            bt = stm.read();
            if (bt == -1)
                throw new MAPException(
                        "Error creating a new SmsDeliverTpduImpl instance: protocolIdentifier field has not been found");
            this.protocolIdentifier = new ProtocolIdentifierImpl(bt);
        }

        if (this.parameterIndicator.getTP_DCSPresence()) {
            bt = stm.read();
            if (bt == -1)
                throw new MAPException(
                        "Error creating a new SmsDeliverTpduImpl instance: dataCodingScheme field has not been found");
            this.dataCodingScheme = new DataCodingSchemeImpl(bt);
        }

        if (this.parameterIndicator.getTP_UDLPresence()) {
            this.userDataLength = stm.read();
            if (this.userDataLength == -1)
                throw new MAPException(
                        "Error creating a new SmsDeliverTpduImpl instance: userDataLength field has not been found");

            int avail = stm.available();
            byte[] buf = new byte[avail];
            try {
                stm.read(buf);
            } catch (IOException e) {
                throw new MAPException("IOException while creating a new SmsDeliverTpduImpl instance: " + e.getMessage(), e);
            }
            userData = new UserDataImpl(buf, dataCodingScheme, userDataLength, userDataHeaderIndicator, gsm8Charset);
        }
    }

    public boolean getUserDataHeaderIndicator() {
        return this.userDataHeaderIndicator;
    }

    public FailureCause getFailureCause() {
        return failureCause;
    }

    public ParameterIndicator getParameterIndicator() {
        return parameterIndicator;
    }

    public AbsoluteTimeStamp getServiceCentreTimeStamp() {
        return serviceCentreTimeStamp;
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

        if (this.serviceCentreTimeStamp == null)
            throw new MAPException("Parameter serviceCentreTimeStamp must not be null");

        if (this.userData != null) {
            this.userData.encode();
            this.userDataHeaderIndicator = this.userData.getEncodedUserDataHeaderIndicator();
            this.userDataLength = this.userData.getEncodedUserDataLength();
            this.dataCodingScheme = this.userData.getDataCodingScheme();

            if (this.userData.getEncodedData().length > _UserDataSubmitReportLimit)
                throw new MAPException("User data field length may not increase " + _UserDataSubmitReportLimit);
        }

        AsnOutputStream res = new AsnOutputStream();

        // byte 0
        res.write(SmsTpduType.SMS_SUBMIT_REPORT.getEncodedValue() | (this.userDataHeaderIndicator ? _MASK_TP_UDHI : 0));

        if (this.failureCause != null)
            res.write(this.failureCause.getCode());

        this.parameterIndicator = new ParameterIndicatorImpl(this.userData != null, this.dataCodingScheme != null,
                this.protocolIdentifier != null);
        res.write(this.parameterIndicator.getCode());
        this.serviceCentreTimeStamp.encodeData(res);

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

        sb.append("SMS-SUBMIT-REPORT tpdu [");

        boolean started = false;
        if (this.userDataHeaderIndicator) {
            sb.append("userDataHeaderIndicator");
            started = true;
        }

        if (this.failureCause != null) {
            if (started)
                sb.append(", ");
            sb.append("failureCause=");
            sb.append(this.failureCause.toString());
            started = true;
        }
        if (this.parameterIndicator != null) {
            if (started)
                sb.append(", ");
            sb.append(this.parameterIndicator.toString());
            started = true;
        }
        if (this.serviceCentreTimeStamp != null) {
            if (started)
                sb.append(", ");
            sb.append("serviceCentreTimeStamp [");
            sb.append(this.serviceCentreTimeStamp.toString());
            sb.append("]");
            started = true;
        }
        if (this.protocolIdentifier != null) {
            if (started)
                sb.append(", ");
            sb.append(this.protocolIdentifier.toString());
            started = true;
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
