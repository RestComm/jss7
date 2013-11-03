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
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.SMDeliveryOutcome;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ReportSMDeliveryStatusRequestImpl extends SmsMessageImpl implements ReportSMDeliveryStatusRequest {

    protected static final int _TAG_AbsentSubscriberDiagnosticSM = 0;
    protected static final int _TAG_ExtensionContainer = 1;
    protected static final int _TAG_GprsSupportIndicator = 2;
    protected static final int _TAG_DeliveryOutcomeIndicator = 3;
    protected static final int _TAG_AdditionalSMDeliveryOutcome = 4;
    protected static final int _TAG_AdditionalAbsentSubscriberDiagnosticSM = 5;

    protected String _PrimitiveName = "ReportSMDeliveryStatusRequest";

    private ISDNAddressString msisdn;
    private AddressString serviceCentreAddress;
    private SMDeliveryOutcome sMDeliveryOutcome;
    private Integer absentSubscriberDiagnosticSM;
    private MAPExtensionContainer extensionContainer;
    private boolean gprsSupportIndicator;
    private boolean deliveryOutcomeIndicator;
    private SMDeliveryOutcome additionalSMDeliveryOutcome;
    private Integer additionalAbsentSubscriberDiagnosticSM;
    private long mapProtocolVersion;

    public ReportSMDeliveryStatusRequestImpl(long mapProtocolVersion) {
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public ReportSMDeliveryStatusRequestImpl(long mapProtocolVersion, ISDNAddressString msisdn,
            AddressString serviceCentreAddress, SMDeliveryOutcome sMDeliveryOutcome, Integer absentSubscriberDiagnosticSM,
            MAPExtensionContainer extensionContainer, boolean gprsSupportIndicator, boolean deliveryOutcomeIndicator,
            SMDeliveryOutcome additionalSMDeliveryOutcome, Integer additionalAbsentSubscriberDiagnosticSM) {
        this.mapProtocolVersion = mapProtocolVersion;
        this.msisdn = msisdn;
        this.serviceCentreAddress = serviceCentreAddress;
        this.sMDeliveryOutcome = sMDeliveryOutcome;
        this.absentSubscriberDiagnosticSM = absentSubscriberDiagnosticSM;
        this.extensionContainer = extensionContainer;
        this.gprsSupportIndicator = gprsSupportIndicator;
        this.deliveryOutcomeIndicator = deliveryOutcomeIndicator;
        this.additionalSMDeliveryOutcome = additionalSMDeliveryOutcome;
        this.additionalAbsentSubscriberDiagnosticSM = additionalAbsentSubscriberDiagnosticSM;
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.reportSM_DeliveryStatus_Request;
    }

    public int getOperationCode() {
        return MAPOperationCode.reportSM_DeliveryStatus;
    }

    public ISDNAddressString getMsisdn() {
        return this.msisdn;
    }

    public AddressString getServiceCentreAddress() {
        return this.serviceCentreAddress;
    }

    public SMDeliveryOutcome getSMDeliveryOutcome() {
        return this.sMDeliveryOutcome;
    }

    public Integer getAbsentSubscriberDiagnosticSM() {
        return this.absentSubscriberDiagnosticSM;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public boolean getGprsSupportIndicator() {
        return this.gprsSupportIndicator;
    }

    public boolean getDeliveryOutcomeIndicator() {
        return this.deliveryOutcomeIndicator;
    }

    public SMDeliveryOutcome getAdditionalSMDeliveryOutcome() {
        return this.additionalSMDeliveryOutcome;
    }

    public Integer getAdditionalAbsentSubscriberDiagnosticSM() {
        return this.additionalAbsentSubscriberDiagnosticSM;
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

        this.msisdn = null;
        this.serviceCentreAddress = null;
        this.sMDeliveryOutcome = null;
        this.absentSubscriberDiagnosticSM = null;
        this.extensionContainer = null;
        this.gprsSupportIndicator = false;
        this.deliveryOutcomeIndicator = false;
        this.additionalSMDeliveryOutcome = null;
        this.additionalAbsentSubscriberDiagnosticSM = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (num) {
                case 0:
                    // msisdn
                    if (tag != Tag.STRING_OCTET || ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".msisdn: Parameter bad tag or tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.msisdn = new ISDNAddressStringImpl();
                    ((ISDNAddressStringImpl) this.msisdn).decodeAll(ais);
                    break;

                case 1:
                    // serviceCentreAddress
                    if (tag != Tag.STRING_OCTET || ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".serviceCentreAddress: Parameter bad tag or tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.serviceCentreAddress = new AddressStringImpl();
                    ((AddressStringImpl) this.serviceCentreAddress).decodeAll(ais);
                    break;

                case 2:
                    // sMDeliveryOutcome
                    if (tag != Tag.ENUMERATED || ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".sMDeliveryOutcome: Parameter bad tag or tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    int i1 = (int) ais.readInteger();
                    this.sMDeliveryOutcome = SMDeliveryOutcome.getInstance(i1);
                    break;

                default:
                    if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {

                        switch (tag) {
                            case _TAG_AbsentSubscriberDiagnosticSM:
                                // absentSubscriberDiagnosticSM
                                if (!ais.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": Parameter absentSubscriberDiagnosticSM is not primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                this.absentSubscriberDiagnosticSM = (int) ais.readInteger();
                                break;

                            case _TAG_ExtensionContainer:
                                // extensionContainer
                                if (ais.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": Parameter extensionContainer is primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                this.extensionContainer = new MAPExtensionContainerImpl();
                                ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                                break;

                            case _TAG_GprsSupportIndicator:
                                // gprsSupportIndicator
                                if (!ais.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": Parameter gprsSupportIndicator is not primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                ais.readNull();
                                this.gprsSupportIndicator = true;
                                break;

                            case _TAG_DeliveryOutcomeIndicator:
                                // deliveryOutcomeIndicator
                                if (!ais.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": Parameter deliveryOutcomeIndicator is not primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                ais.readNull();
                                this.deliveryOutcomeIndicator = true;
                                break;

                            case _TAG_AdditionalSMDeliveryOutcome:
                                // additionalSMDeliveryOutcome
                                if (!ais.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": Parameter additionalSMDeliveryOutcome is not primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                i1 = (int) ais.readInteger();
                                this.additionalSMDeliveryOutcome = SMDeliveryOutcome.getInstance(i1);
                                break;

                            case _TAG_AdditionalAbsentSubscriberDiagnosticSM:
                                // additionalAbsentSubscriberDiagnosticSM
                                if (!ais.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": Parameter additionalAbsentSubscriberDiagnosticSM is not primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                this.additionalAbsentSubscriberDiagnosticSM = (int) ais.readInteger();
                                break;

                            default:
                                ais.advanceElement();
                                break;
                        }

                    } else {

                        ais.advanceElement();
                    }
                    break;
            }

            num++;
        }

        if (num < 3 && this.mapProtocolVersion >= 2 || num < 2 && this.mapProtocolVersion == 1)
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Needs at least 3 mandatory parameters, found " + num,
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
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.mapProtocolVersion == 1) {
            if (this.msisdn == null || this.serviceCentreAddress == null)
                throw new MAPException("msisdn and serviceCentreAddress must not be null");
        } else {
            if (this.msisdn == null || this.serviceCentreAddress == null || this.sMDeliveryOutcome == null)
                throw new MAPException("msisdn, serviceCentreAddress and sMDeliveryOutcome must not be null");
        }

        try {
            ((ISDNAddressStringImpl) this.msisdn).encodeAll(asnOs);
            ((AddressStringImpl) this.serviceCentreAddress).encodeAll(asnOs);
            if (this.mapProtocolVersion > 1)
                asnOs.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, this.sMDeliveryOutcome.getCode());

            if (this.absentSubscriberDiagnosticSM != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_AbsentSubscriberDiagnosticSM,
                        this.absentSubscriberDiagnosticSM);
            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_ExtensionContainer);
            if (this.gprsSupportIndicator == true)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_GprsSupportIndicator);
            if (this.deliveryOutcomeIndicator == true)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_DeliveryOutcomeIndicator);
            if (this.additionalSMDeliveryOutcome != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_AdditionalSMDeliveryOutcome,
                        this.additionalSMDeliveryOutcome.getCode());
            if (this.additionalAbsentSubscriberDiagnosticSM != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_AdditionalAbsentSubscriberDiagnosticSM,
                        this.additionalAbsentSubscriberDiagnosticSM);
        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
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

        if (this.msisdn != null) {
            sb.append(", msisdn=");
            sb.append(this.msisdn.toString());
        }
        if (this.serviceCentreAddress != null) {
            sb.append(", serviceCentreAddress=");
            sb.append(this.serviceCentreAddress.toString());
        }
        if (this.sMDeliveryOutcome != null) {
            sb.append(", sMDeliveryOutcome=");
            sb.append(this.sMDeliveryOutcome.toString());
        }
        if (this.absentSubscriberDiagnosticSM != null) {
            sb.append(", absentSubscriberDiagnosticSM=");
            sb.append(this.absentSubscriberDiagnosticSM.toString());
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }
        if (this.gprsSupportIndicator) {
            sb.append(", gprsSupportIndicator");
        }
        if (this.deliveryOutcomeIndicator) {
            sb.append(", deliveryOutcomeIndicator");
        }
        if (this.additionalSMDeliveryOutcome != null) {
            sb.append(", additionalSMDeliveryOutcome=");
            sb.append(this.additionalSMDeliveryOutcome.toString());
        }
        if (this.additionalAbsentSubscriberDiagnosticSM != null) {
            sb.append(", additionalAbsentSubscriberDiagnosticSM=");
            sb.append(this.additionalAbsentSubscriberDiagnosticSM.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
