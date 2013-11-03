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

package org.mobicents.protocols.ss7.map.service.mobility.locationManagement;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.ADDInfo;
import org.mobicents.protocols.ss7.map.primitives.IMEIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ADDInfoImpl implements ADDInfo, MAPAsnPrimitive {

    public static final int _TAG_imeisv = 0;
    public static final int _TAG_skipSubscriberDataUpdate = 1;

    public static final String _PrimitiveName = "ADDInfo";

    private IMEI imeisv;
    private boolean skipSubscriberDataUpdate;

    public ADDInfoImpl() {
    }

    public ADDInfoImpl(IMEI imeisv, boolean skipSubscriberDataUpdate) {
        this.imeisv = imeisv;
        this.skipSubscriberDataUpdate = skipSubscriberDataUpdate;
    }

    public IMEI getImeisv() {
        return imeisv;
    }

    public boolean getSkipSubscriberDataUpdate() {
        return skipSubscriberDataUpdate;
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

        this.imeisv = null;
        this.skipSubscriberDataUpdate = false;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (num) {
                case 0:
                    // imeisv
                    if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive() || tag != _TAG_imeisv)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".imeisv: Parameter 0 bad tag or tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.imeisv = new IMEIImpl();
                    ((IMEIImpl) this.imeisv).decodeAll(ais);
                    break;

                default:
                    if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                        switch (tag) {
                            case _TAG_skipSubscriberDataUpdate:
                                // skipSubscriberDataUpdate
                                if (!ais.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".skipSubscriberDataUpdate: Parameter is not primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                ais.readNull();
                                this.skipSubscriberDataUpdate = true;
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

        if (num < 1)
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Needs at least 1 mandatory parameters, found " + num,
                    MAPParsingComponentExceptionReason.MistypedParameter);
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

        try {
            if (this.imeisv == null)
                throw new MAPException("imeisv parameter must not be null");

            ((IMEIImpl) this.imeisv).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_imeisv);

            if (skipSubscriberDataUpdate)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_skipSubscriberDataUpdate);
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

        if (this.imeisv != null) {
            sb.append("imeisv=");
            sb.append(imeisv.toString());
            sb.append(", ");
        }
        if (this.skipSubscriberDataUpdate) {
            sb.append("skipSubscriberDataUpdate, ");
        }

        sb.append("]");

        return sb.toString();
    }
}
