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

package org.mobicents.protocols.ss7.map.service.lsm;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.PlmnId;
import org.mobicents.protocols.ss7.map.api.service.lsm.RANTechnology;
import org.mobicents.protocols.ss7.map.api.service.lsm.ReportingPLMN;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.PlmnIdImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ReportingPLMNImpl implements ReportingPLMN, MAPAsnPrimitive {

    public static final int _ID_plmn_Id = 0;
    public static final int _ID_ran_Technology = 1;
    public static final int _ID_ran_PeriodicLocationSupport = 2;

    public static final String _PrimitiveName = "ReportingPLMN";

    private PlmnId plmnId;
    private RANTechnology ranTechnology;
    private boolean ranPeriodicLocationSupport;

    public ReportingPLMNImpl() {
    }

    public ReportingPLMNImpl(PlmnId plmnId, RANTechnology ranTechnology, boolean ranPeriodicLocationSupport) {
        this.plmnId = plmnId;
        this.ranTechnology = ranTechnology;
        this.ranPeriodicLocationSupport = ranPeriodicLocationSupport;
    }

    public PlmnId getPlmnId() {
        return plmnId;
    }

    public RANTechnology getRanTechnology() {
        return ranTechnology;
    }

    public boolean getRanPeriodicLocationSupport() {
        return ranPeriodicLocationSupport;
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

        this.plmnId = null;
        this.ranTechnology = null;
        this.ranPeriodicLocationSupport = false;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {

                    case _ID_plmn_Id:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " plmnId: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.plmnId = new PlmnIdImpl();
                        ((PlmnIdImpl) this.plmnId).decodeAll(ais);
                        break;
                    case _ID_ran_Technology:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " ranTechnology: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        int i1 = (int) ais.readInteger();
                        this.ranTechnology = RANTechnology.getInstance(i1);
                        break;
                    case _ID_ran_PeriodicLocationSupport:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " ranPeriodicLocationSupport: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.ranPeriodicLocationSupport = true;
                        break;

                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }

        if (plmnId == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament plmnId is mandatory but does not found", MAPParsingComponentExceptionReason.MistypedParameter);
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

        if (this.plmnId == null)
            throw new MAPException("plmnId parameter must not be null");

        try {
            ((PlmnIdImpl) plmnId).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_plmn_Id);

            if (this.ranTechnology != null) {
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_ran_Technology, this.ranTechnology.getCode());
            }

            if (this.ranPeriodicLocationSupport) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_ran_PeriodicLocationSupport);
            }
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

        if (this.plmnId != null) {
            sb.append("plmnId=");
            sb.append(this.plmnId.toString());
        }
        if (this.ranTechnology != null) {
            sb.append(", ranTechnology=");
            sb.append(this.ranTechnology.toString());
        }
        if (this.ranPeriodicLocationSupport) {
            sb.append(", ranPeriodicLocationSupport");
        }

        sb.append("]");

        return sb.toString();
    }
}
