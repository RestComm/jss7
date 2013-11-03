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
package org.mobicents.protocols.ss7.cap.service.sms;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPMessageType;
import org.mobicents.protocols.ss7.cap.api.CAPOperationCode;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.CalledPartyBCDNumber;
import org.mobicents.protocols.ss7.cap.api.service.sms.ConnectSMSRequest;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.SMSAddressString;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsImpl;
import org.mobicents.protocols.ss7.cap.primitives.CalledPartyBCDNumberImpl;
import org.mobicents.protocols.ss7.cap.service.sms.primitive.SMSAddressStringImpl;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class ConnectSMSRequestImpl extends SmsMessageImpl implements ConnectSMSRequest {

    public static final String _PrimitiveName = "ConnectSMSRequest";

    public static final int _ID_callingPartysNumber = 0;
    public static final int _ID_destinationSubscriberNumber = 1;
    public static final int _ID_smscAddress = 2;
    public static final int _ID_extensions = 10;

    private SMSAddressString callingPartysNumber;
    private CalledPartyBCDNumber destinationSubscriberNumber;
    private ISDNAddressString smscAddress;
    private CAPExtensions extensions;

    public ConnectSMSRequestImpl() {
        super();
    }

    public ConnectSMSRequestImpl(SMSAddressString callingPartysNumber,
            CalledPartyBCDNumber destinationSubscriberNumber, ISDNAddressString smscAddress, CAPExtensions extensions) {
        super();
        this.callingPartysNumber = callingPartysNumber;
        this.destinationSubscriberNumber = destinationSubscriberNumber;
        this.smscAddress = smscAddress;
        this.extensions = extensions;
    }

    @Override
    public SMSAddressString getCallingPartysNumber() {
        return this.callingPartysNumber;
    }

    @Override
    public CalledPartyBCDNumber getDestinationSubscriberNumber() {
        return this.destinationSubscriberNumber;
    }

    @Override
    public ISDNAddressString getSMSCAddress() {
        return this.smscAddress;
    }

    @Override
    public CAPExtensions getExtensions() {
        return this.extensions;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.connectSMS_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.connectSMS;
    }

    @Override
    public int getTag() throws CAPException {
        return Tag.SEQUENCE;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    @Override
    public boolean getIsPrimitive() {
        return false;
    }

    @Override
    public void decodeAll(AsnInputStream ansIS) throws CAPParsingComponentException {
        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (MAPParsingComponentException e) {
            throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName
                    + ": " + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void decodeData(AsnInputStream ansIS, int length) throws CAPParsingComponentException {
        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (MAPParsingComponentException e) {
            throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName
                    + ": " + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, IOException,
            AsnException, MAPParsingComponentException {

        this.callingPartysNumber = null;
        this.destinationSubscriberNumber = null;
        this.smscAddress = null;
        this.extensions = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                case _ID_callingPartysNumber:
                    if (!ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".callingPartysNumber: Parameter is not primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.callingPartysNumber = new SMSAddressStringImpl();
                    ((SMSAddressStringImpl) this.callingPartysNumber).decodeAll(ais);
                    break;
                case _ID_destinationSubscriberNumber:
                    if (!ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".destinationSubscriberNumber: Parameter is not primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.destinationSubscriberNumber = new CalledPartyBCDNumberImpl();
                    ((CalledPartyBCDNumberImpl) this.destinationSubscriberNumber).decodeAll(ais);
                    break;
                case _ID_smscAddress:
                    if (!ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".smscAddress: Parameter is not primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.smscAddress = new ISDNAddressStringImpl();
                    ((ISDNAddressStringImpl) this.smscAddress).decodeAll(ais);
                    break;
                case _ID_extensions:
                    if (ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".extensions: Parameter is primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.extensions = new CAPExtensionsImpl();
                    ((CAPExtensionsImpl) this.extensions).decodeAll(ais);
                    break;

                default:
                    ais.advanceElement();
                    break;
                }
            } else {
                ais.advanceElement();
            }
        }

    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws CAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws CAPException {
        try {
            asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws CAPException {

        try {

            if (this.callingPartysNumber != null)
                ((SMSAddressStringImpl) this.callingPartysNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_callingPartysNumber);

            if (this.destinationSubscriberNumber != null)
                ((CalledPartyBCDNumberImpl) this.destinationSubscriberNumber).encodeAll(asnOs,
                        Tag.CLASS_CONTEXT_SPECIFIC, _ID_destinationSubscriberNumber);

            if (this.smscAddress != null)
                ((ISDNAddressStringImpl) this.smscAddress)
                        .encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_smscAddress);

            if (this.extensions != null)
                ((CAPExtensionsImpl) this.extensions).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_extensions);

        } catch (MAPException e) {
            throw new CAPException("MAPException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.callingPartysNumber != null) {
            sb.append("callingPartysNumber=");
            sb.append(callingPartysNumber.toString());
        }
        if (this.destinationSubscriberNumber != null) {
            sb.append(", destinationSubscriberNumber=");
            sb.append(destinationSubscriberNumber.toString());
        }
        if (this.smscAddress != null) {
            sb.append(", smscAddress=");
            sb.append(smscAddress.toString());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
