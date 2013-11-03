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
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.LMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SM_RP_DAImpl implements SM_RP_DA, MAPAsnPrimitive {

    private static final int _TAG_IMSI = 0;
    private static final int _TAG_LMSI = 1;
    private static final int _TAG_ServiceCentreAddressDA = 4;
    private static final int _TAG_NoSM_RP_DA = 5;

    public static final String _PrimitiveName = "SM_RP_DA";

    private IMSI imsi;
    private LMSI lmsi;
    private AddressString serviceCentreAddressDA;

    public SM_RP_DAImpl() {
    }

    public SM_RP_DAImpl(IMSI imsi) {
        this.imsi = imsi;
    }

    public SM_RP_DAImpl(LMSI lmsi) {
        this.lmsi = lmsi;
    }

    public SM_RP_DAImpl(AddressString serviceCentreAddressDA) {
        this.serviceCentreAddressDA = serviceCentreAddressDA;
    }

    public IMSI getIMSI() {
        return this.imsi;
    }

    public LMSI getLMSI() {
        return this.lmsi;
    }

    public AddressString getServiceCentreAddressDA() {
        return serviceCentreAddressDA;
    }

    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    public int getTag() {
        if (this.imsi != null)
            return _TAG_IMSI;
        else if (this.lmsi != null)
            return _TAG_LMSI;
        else if (this.serviceCentreAddressDA != null)
            return _TAG_ServiceCentreAddressDA;
        else
            return _TAG_NoSM_RP_DA;
    }

    public boolean getIsPrimitive() {
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

        this.imsi = null;
        this.lmsi = null;
        this.serviceCentreAddressDA = null;

        if (ansIS.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ansIS.isTagPrimitive())
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": bad tag class or is not primitive: TagClass=" + ansIS.getTagClass(),
                    MAPParsingComponentExceptionReason.MistypedParameter);

        switch (ansIS.getTag()) {
            case _TAG_IMSI:
                this.imsi = new IMSIImpl();
                ((IMSIImpl) this.imsi).decodeData(ansIS, length);
                break;

            case _TAG_LMSI:
                this.lmsi = new LMSIImpl();
                ((LMSIImpl) this.lmsi).decodeData(ansIS, length);
                break;

            case _TAG_ServiceCentreAddressDA:
                this.serviceCentreAddressDA = new AddressStringImpl();
                ((AddressStringImpl) this.serviceCentreAddressDA).decodeData(ansIS, length);
                break;

            case _TAG_NoSM_RP_DA:
                try {
                    ansIS.readNullData(length);
                } catch (AsnException e) {
                    throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": "
                            + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
                } catch (IOException e) {
                    throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": "
                            + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
                }
                break;

            default:
                throw new MAPParsingComponentException("Error while SM_RP_DA: bad tag: " + ansIS.getTag(),
                        MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void encodeAll(AsnOutputStream asnOs) throws MAPException {

        this.encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, this.getTag());
    }

    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {

        try {
            asnOs.writeTag(tagClass, true, tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.imsi != null)
            ((IMSIImpl) this.imsi).encodeData(asnOs);
        else if (this.lmsi != null)
            ((LMSIImpl) this.lmsi).encodeData(asnOs);
        else if (this.serviceCentreAddressDA != null)
            ((AddressStringImpl) this.serviceCentreAddressDA).encodeData(asnOs);
        else {
            asnOs.writeNullData();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SM_RP_DA [");

        if (this.imsi != null) {
            sb.append("imsi=");
            sb.append(this.imsi.toString());
        }
        if (this.lmsi != null) {
            sb.append("lmsi=");
            sb.append(this.lmsi.toString());
        }
        if (this.serviceCentreAddressDA != null) {
            sb.append("serviceCentreAddressDA=");
            sb.append(this.serviceCentreAddressDA.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
