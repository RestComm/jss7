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

package org.mobicents.protocols.ss7.map.errors;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorCode;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageSsIncompatibility;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSStatus;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.BasicServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.SSStatusImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MAPErrorMessageSsIncompatibilityImpl extends MAPErrorMessageImpl implements MAPErrorMessageSsIncompatibility {

    public static final int _tag_ss_Code = 1;
    public static final int _tag_ss_Status = 4;

    private SSCode ssCode;
    private BasicServiceCode basicService;
    private SSStatus ssStatus;

    protected String _PrimitiveName = "MAPErrorMessageSsIncompatibility";

    public MAPErrorMessageSsIncompatibilityImpl(SSCode ssCode, BasicServiceCode basicService, SSStatus ssStatus) {
        super((long) MAPErrorCode.ssIncompatibility);

        this.ssCode = ssCode;
        this.basicService = basicService;
        this.ssStatus = ssStatus;
    }

    public MAPErrorMessageSsIncompatibilityImpl() {
        super((long) MAPErrorCode.ssIncompatibility);
    }

    public boolean isEmSsIncompatibility() {
        return true;
    }

    public MAPErrorMessageSsIncompatibility getEmSsIncompatibility() {
        return this;
    }

    @Override
    public SSCode getSSCode() {
        return ssCode;
    }

    @Override
    public BasicServiceCode getBasicService() {
        return basicService;
    }

    @Override
    public SSStatus getSSStatus() {
        return ssStatus;
    }

    @Override
    public void setSSCode(SSCode val) {
        ssCode = val;
    }

    @Override
    public void setBasicService(BasicServiceCode val) {
        basicService = val;
    }

    @Override
    public void setSSStatus(SSStatus val) {
        ssStatus = val;
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

    private void _decode(AsnInputStream localAis, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.ssCode = null;
        this.basicService = null;
        this.ssStatus = null;

        if (localAis.getTagClass() != Tag.CLASS_UNIVERSAL || localAis.getTag() != Tag.SEQUENCE || localAis.isTagPrimitive())
            throw new MAPParsingComponentException("Error decoding " + _PrimitiveName
                    + ": bad tag class or tag or parameter is primitive", MAPParsingComponentExceptionReason.MistypedParameter);

        AsnInputStream ais = localAis.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_CONTEXT_SPECIFIC:
                    switch (tag) {
                        case _tag_ss_Code:
                            this.ssCode = new SSCodeImpl();
                            ((SSCodeImpl) this.ssCode).decodeAll(ais);
                            break;
                        case BasicServiceCodeImpl._TAG_bearerService:
                        case BasicServiceCodeImpl._TAG_teleservice:
                            // AsnInputStream ais2 = ais.readSequenceStream();
                            // ais2.readTag();
                            this.basicService = new BasicServiceCodeImpl();
                            ((BasicServiceCodeImpl) this.basicService).decodeAll(ais);
                            break;
                        case _tag_ss_Status:
                            this.ssStatus = new SSStatusImpl();
                            ((SSStatusImpl) this.ssStatus).decodeAll(ais);
                            break;

                        default:
                            ais.advanceElement();
                            break;
                    }
                    break;

                default:
                    ais.advanceElement();
                    break;
            }
        }
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

        if (this.ssCode == null && this.basicService == null && this.ssStatus == null)
            return;

        if (this.ssCode != null)
            ((SSCodeImpl) this.ssCode).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _tag_ss_Code);
        if (this.basicService != null) {
            ((BasicServiceCodeImpl) this.basicService).encodeAll(asnOs,
                    ((BasicServiceCodeImpl) this.basicService).getTagClass(),
                    ((BasicServiceCodeImpl) this.basicService).getTag());
        }
        if (this.ssStatus != null)
            ((SSStatusImpl) this.ssStatus).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _tag_ss_Status);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.ssCode != null)
            sb.append("ssCode=" + this.ssCode.toString());
        if (this.basicService != null)
            sb.append(", basicService=" + this.basicService.toString());
        if (this.ssStatus != null)
            sb.append(", ssStatus=" + this.ssStatus.toString());
        sb.append("]");

        return sb.toString();
    }

}
