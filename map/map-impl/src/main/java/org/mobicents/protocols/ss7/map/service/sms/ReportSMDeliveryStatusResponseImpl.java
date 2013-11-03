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
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusResponse;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ReportSMDeliveryStatusResponseImpl extends SmsMessageImpl implements ReportSMDeliveryStatusResponse {

    private ISDNAddressString storedMSISDN;
    private MAPExtensionContainer extensionContainer;

    private long mapProtocolVersion;

    protected String _PrimitiveName = "ReportSMDeliveryStatusResponse";

    public ReportSMDeliveryStatusResponseImpl(long mapProtocolVersion) {
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public ReportSMDeliveryStatusResponseImpl(long mapProtocolVersion, ISDNAddressString storedMSISDN,
            MAPExtensionContainer extensionContainer) {
        this.mapProtocolVersion = mapProtocolVersion;
        this.storedMSISDN = storedMSISDN;
        this.extensionContainer = extensionContainer;
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.reportSM_DeliveryStatus_Response;
    }

    public int getOperationCode() {
        return MAPOperationCode.reportSM_DeliveryStatus;
    }

    public ISDNAddressString getStoredMSISDN() {
        return this.storedMSISDN;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public long getMapProtocolVersion() {
        return this.mapProtocolVersion;
    }

    public int getTag() throws MAPException {
        if (this.mapProtocolVersion >= 3)
            return Tag.SEQUENCE;
        else
            return Tag.STRING_OCTET;
    }

    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    public boolean getIsPrimitive() {
        if (this.mapProtocolVersion >= 3)
            return false;
        else
            return true;
    }

    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {

        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.storedMSISDN = null;
        this.extensionContainer = null;

        if (this.mapProtocolVersion >= 3) {
            AsnInputStream ais = ansIS.readSequenceStreamData(length);
            while (true) {
                if (ais.available() == 0)
                    break;

                int tag = ais.readTag();

                if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                    switch (tag) {
                        case Tag.STRING_OCTET:
                            // storedMSISDN
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter storedMSISDN is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.storedMSISDN = new ISDNAddressStringImpl();
                            ((ISDNAddressStringImpl) this.storedMSISDN).decodeAll(ais);
                            break;

                        case Tag.SEQUENCE:
                            // ExtensionContainer
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter extensionContainer is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.extensionContainer = new MAPExtensionContainerImpl();
                            ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                            break;

                        default:
                            ais.advanceElement();
                            break;
                    }

                } else {
                    ais.advanceElement();
                }
            }
        } else {
            this.storedMSISDN = new ISDNAddressStringImpl();
            ((ISDNAddressStringImpl) this.storedMSISDN).decodeData(ansIS, length);
        }
    }

    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
        try {
            asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.mapProtocolVersion >= 3) {
            if (this.storedMSISDN != null)
                ((ISDNAddressStringImpl) this.storedMSISDN).encodeAll(asnOs);
            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs);
        } else {
            if (this.storedMSISDN == null)
                throw new MAPException("storedMSISDN must not be null");
            ((ISDNAddressStringImpl) this.storedMSISDN).encodeData(asnOs);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.getMAPDialog() != null) {
            sb.append("DialogId=").append(this.getMAPDialog().getLocalDialogId());
        }

        if (this.storedMSISDN != null) {
            sb.append(", storedMSISDN=");
            sb.append(this.storedMSISDN.toString());
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }

        sb.append(", mapProtocolVersion=");
        sb.append(this.mapProtocolVersion);

        sb.append("]");

        return sb.toString();
    }

}
