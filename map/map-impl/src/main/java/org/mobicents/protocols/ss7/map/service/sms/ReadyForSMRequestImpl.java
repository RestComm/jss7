/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertReason;
import org.mobicents.protocols.ss7.map.api.service.sms.ReadyForSMRequest;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
*
* @author sergey vetyutnev
*
*/
public class ReadyForSMRequestImpl extends SmsMessageImpl implements ReadyForSMRequest {
    protected static final int _TAG_imsi = 0;
    protected static final int _TAG_additionalAlertReasonIndicator = 1;

    public static final String _PrimitiveName = "ReadyForSMRequest";

    private IMSI imsi;
    private AlertReason alertReason;
    private boolean alertReasonIndicator;
    private MAPExtensionContainer extensionContainer;
    private boolean additionalAlertReasonIndicator;

    public ReadyForSMRequestImpl() {
    }

    public ReadyForSMRequestImpl(IMSI imsi, AlertReason alertReason, boolean alertReasonIndicator, MAPExtensionContainer extensionContainer,
            boolean additionalAlertReasonIndicator) {
        this.imsi = imsi;
        this.alertReason = alertReason;
        this.alertReasonIndicator = alertReasonIndicator;
        this.extensionContainer = extensionContainer;
        this.additionalAlertReasonIndicator = additionalAlertReasonIndicator;
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.readyForSM_Request;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.readyForSM;
    }

    @Override
    public IMSI getImsi() {
        return imsi;
    }

    @Override
    public AlertReason getAlertReason() {
        return alertReason;
    }

    @Override
    public boolean getAlertReasonIndicator() {
        return alertReasonIndicator;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    public boolean getAdditionalAlertReasonIndicator() {
        return additionalAlertReasonIndicator;
    }

    @Override
    public int getTag() throws MAPException {
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

    @Override
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

        this.imsi = null;
        this.alertReason = null;
        this.alertReasonIndicator = false;
        this.extensionContainer = null;
        this.additionalAlertReasonIndicator = false;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                switch (tag) {
                case Tag.ENUMERATED:
                    if (!ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".alertReason: Parameter is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    int i1 = (int)ais.readInteger();
                    this.alertReason = AlertReason.getInstance(i1);
                    break;
                case Tag.NULL:
                    if (!ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".alertReasonIndicator: Parameter is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    ais.readNull();
                    this.alertReasonIndicator = true;
                    break;
                case Tag.SEQUENCE:
                    if (ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".extensionContainer: Parameter extensionContainer is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                    this.extensionContainer = new MAPExtensionContainerImpl();
                    ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                    break;

                default:
                    ais.advanceElement();
                    break;
                }
            } else if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                case _TAG_imsi:
                    if (!ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".imsi: Parameter is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.imsi = new IMSIImpl();
                    ((IMSIImpl) this.imsi).decodeAll(ais);
                    break;
                case _TAG_additionalAlertReasonIndicator:
                    if (!ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".additionalAlertReasonIndicator: Parameter is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    ais.readNull();
                    this.additionalAlertReasonIndicator = true;
                    break;

                default:
                    ais.advanceElement();
                    break;
                }
            } else {
                ais.advanceElement();
            }

            num++;
        }

        if (this.imsi == null)
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": Parameter imsi is mandator but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        if (this.alertReason == null)
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": Parameter alertReason is mandator but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    @Override
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

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        try {
            if (this.imsi == null)
                throw new MAPException("IMSI parameter must not be null");
            if (this.alertReason == null)
                throw new MAPException("alertReason parameter must not be null");

            ((IMSIImpl) this.imsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_imsi);
            asnOs.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, this.alertReason.getCode());

            if (this.alertReasonIndicator)
                asnOs.writeNull();
            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs);

            if (this.additionalAlertReasonIndicator)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_additionalAlertReasonIndicator);

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

        if (this.imsi != null) {
            sb.append("imsi=");
            sb.append(imsi);
            sb.append(", ");
        }
        if (this.alertReason != null) {
            sb.append("alertReason");
            sb.append(alertReason);
            sb.append(", ");
        }
        if (this.alertReasonIndicator) {
            sb.append("alertReasonIndicator, ");
        }
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(extensionContainer);
            sb.append(", ");
        }
        if (this.additionalAlertReasonIndicator) {
            sb.append("additionalAlertReasonIndicator, ");
        }

        sb.append("]");

        return sb.toString();
    }

}
