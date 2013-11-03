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
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.mobicents.protocols.ss7.map.api.service.sms.SmsSignalInfo;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ForwardShortMessageRequestImpl extends SmsMessageImpl implements ForwardShortMessageRequest {

    private SM_RP_DA sM_RP_DA;
    private SM_RP_OA sM_RP_OA;
    private SmsSignalInfoImpl sM_RP_UI;
    private boolean moreMessagesToSend;

    public ForwardShortMessageRequestImpl() {
    }

    public ForwardShortMessageRequestImpl(SM_RP_DA sM_RP_DA, SM_RP_OA sM_RP_OA, SmsSignalInfo sM_RP_UI,
            boolean moreMessagesToSend) {
        this.sM_RP_DA = sM_RP_DA;
        this.sM_RP_OA = sM_RP_OA;
        this.sM_RP_UI = (SmsSignalInfoImpl) sM_RP_UI;
        this.moreMessagesToSend = moreMessagesToSend;
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.forwardSM_Request;
    }

    public int getOperationCode() {
        return MAPOperationCode.mo_forwardSM;
    }

    public SM_RP_DA getSM_RP_DA() {
        return this.sM_RP_DA;
    }

    public SM_RP_OA getSM_RP_OA() {
        return this.sM_RP_OA;
    }

    public SmsSignalInfo getSM_RP_UI() {
        return this.sM_RP_UI;
    }

    public boolean getMoreMessagesToSend() {
        return this.moreMessagesToSend;
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
            throw new MAPParsingComponentException("IOException when decoding forwardShortMessageRequest: " + e.getMessage(),
                    e, MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding forwardShortMessageRequest: " + e.getMessage(),
                    e, MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding forwardShortMessageRequest: " + e.getMessage(),
                    e, MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding forwardShortMessageRequest: " + e.getMessage(),
                    e, MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.sM_RP_DA = null;
        this.sM_RP_OA = null;
        this.sM_RP_UI = null;
        this.moreMessagesToSend = false;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (num) {
                case 0:
                    // SM_RP_DA
                    if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive())
                        throw new MAPParsingComponentException(
                                "Error while decoding forwardShortMessageRequest: Parameter 0 bad tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.sM_RP_DA = new SM_RP_DAImpl();
                    ((SM_RP_DAImpl) this.sM_RP_DA).decodeAll(ais);
                    break;

                case 1:
                    // SM_RP_OA
                    if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive())
                        throw new MAPParsingComponentException(
                                "Error while decoding forwardShortMessageRequest: Parameter 1 bad tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.sM_RP_OA = new SM_RP_OAImpl();
                    ((SM_RP_OAImpl) this.sM_RP_OA).decodeAll(ais);
                    break;

                case 2:
                    // sm-RP-UI
                    if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive())
                        throw new MAPParsingComponentException(
                                "Error while decoding forwardShortMessageRequest: Parameter 2 bad tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    if (tag != Tag.STRING_OCTET)
                        throw new MAPParsingComponentException(
                                "Error while decoding forwardShortMessageRequest: Parameter 2 tag must be STRING_OCTET, found: "
                                        + tag, MAPParsingComponentExceptionReason.MistypedParameter);
                    this.sM_RP_UI = new SmsSignalInfoImpl();
                    this.sM_RP_UI.decodeAll(ais);
                    break;

                default:
                    if (tag == Tag.NULL && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding forwardShortMessageRequest: Parameter moreMessagesToSend is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.moreMessagesToSend = true;
                    } else {
                        ais.advanceElement();
                    }
                    break;
            }

            num++;
        }

        if (num < 3)
            throw new MAPParsingComponentException(
                    "Error while decoding forwardShortMessageRequest: Needs at least 3 mandatory parameters, found " + num,
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
            throw new MAPException("AsnException when encoding forwardShortMessageRequest: " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.sM_RP_DA == null || this.sM_RP_OA == null || this.sM_RP_UI == null)
            throw new MAPException("sm_RP_DA,sm_RP_OA and sm_RP_UI must not be null");

        try {
            ((SM_RP_DAImpl) this.sM_RP_DA).encodeAll(asnOs);
            ((SM_RP_OAImpl) this.sM_RP_OA).encodeAll(asnOs);
            this.sM_RP_UI.encodeAll(asnOs);

            if (this.moreMessagesToSend)
                asnOs.writeNull();
        } catch (IOException e) {
            throw new MAPException("IOException when encoding forwardShortMessageRequest: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding forwardShortMessageRequest: " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ForwardShortMessageRequest [");

        if (this.getMAPDialog() != null) {
            sb.append("DialogId=").append(this.getMAPDialog().getLocalDialogId());
        }

        if (this.sM_RP_DA != null) {
            sb.append(", sm_RP_DA=");
            sb.append(this.sM_RP_DA.toString());
        }
        if (this.sM_RP_OA != null) {
            sb.append(", sm_RP_OA=");
            sb.append(this.sM_RP_OA.toString());
        }
        if (this.sM_RP_UI != null) {
            sb.append(", sm_RP_UI=[");
            sb.append(this.sM_RP_UI.toString());
            sb.append("]");
        }
        if (this.moreMessagesToSend) {
            sb.append(", moreMessagesToSend");
        }

        sb.append("]");

        return sb.toString();
    }

}
