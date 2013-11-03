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

package org.mobicents.protocols.ss7.map.service.sms;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessageType;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreRequest;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class AlertServiceCentreRequestImpl extends SmsMessageImpl implements AlertServiceCentreRequest {

    private ISDNAddressString msisdn;
    private AddressString serviceCentreAddress;
    private int operationCode;

    public AlertServiceCentreRequestImpl(int operationCode) {
        this.operationCode = operationCode;
    }

    public AlertServiceCentreRequestImpl(ISDNAddressString msisdn, AddressString serviceCentreAddress) {
        this.msisdn = msisdn;
        this.serviceCentreAddress = serviceCentreAddress;
    }

    public MAPMessageType getMessageType() {
        if (this.operationCode == MAPOperationCode.alertServiceCentre)
            return MAPMessageType.alertServiceCentre_Request;
        else
            return MAPMessageType.alertServiceCentreWithoutResult_Request;
    }

    public int getOperationCode() {
        return this.operationCode;
    }

    public ISDNAddressString getMsisdn() {
        return this.msisdn;
    }

    public AddressString getServiceCentreAddress() {
        return this.serviceCentreAddress;
    }

    public int getTag() throws MAPException {
        return Tag.SEQUENCE;
    }

    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    public boolean getIsPrimitive() {
        return false;
    }

    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {

        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding AlertServiceCentreRequest: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding AlertServiceCentreRequest: " + e.getMessage(),
                    e, MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding AlertServiceCentreRequest: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding AlertServiceCentreRequest: " + e.getMessage(),
                    e, MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        this.msisdn = null;
        this.serviceCentreAddress = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();
            switch (num) {
                case 0:
                    if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_OCTET)
                        throw new MAPParsingComponentException(
                                "Error while decoding AlertServiceCentreRequest.msisdn: bad tag or tag class or is not primitive: TagClass="
                                        + ais.getTagClass() + ", tag=" + tag,
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.msisdn = new ISDNAddressStringImpl();
                    ((ISDNAddressStringImpl) this.msisdn).decodeAll(ais);
                    break;
                case 1:
                    if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_OCTET)
                        throw new MAPParsingComponentException(
                                "Error while decoding AlertServiceCentreRequest.serviceCentreAddress: bad tag or tag class or is not primitive: TagClass="
                                        + ais.getTagClass() + ", tag=" + tag,
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.serviceCentreAddress = new ISDNAddressStringImpl();
                    ((AddressStringImpl) this.serviceCentreAddress).decodeAll(ais);
                    break;
                default:
                    ais.advanceElement();
                    break;
            }

            num++;
        }

        if (this.msisdn == null || this.serviceCentreAddress == null)
            throw new MAPParsingComponentException(
                    "Error while decoding AlertServiceCentreRequest: 2 parameters are mandatory, found " + num,
                    MAPParsingComponentExceptionReason.MistypedParameter);
    }

    public void encodeAll(AsnOutputStream asnOs) throws MAPException {

        this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.SEQUENCE);
    }

    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {

        try {
            asnOs.writeTag(tagClass, false, tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding AlertServiceCentreRequest: " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.msisdn == null || this.serviceCentreAddress == null)
            throw new MAPException(
                    "Error when encoding AlertServiceCentreRequest: msisdn or serviceCentreAddress must not be empty");

        ((ISDNAddressStringImpl) this.msisdn).encodeAll(asnOs);
        ((AddressStringImpl) this.serviceCentreAddress).encodeAll(asnOs);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AlertServiceCentreRequest [");

        if (this.getMAPDialog() != null) {
            sb.append("DialogId=").append(this.getMAPDialog().getLocalDialogId());
        }

        if (this.msisdn != null) {
            sb.append(", msisdn=");
            sb.append(this.msisdn.toString());
        }
        if (this.serviceCentreAddress != null) {
            sb.append(", serviceCentreAddress=");
            sb.append(this.serviceCentreAddress.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
