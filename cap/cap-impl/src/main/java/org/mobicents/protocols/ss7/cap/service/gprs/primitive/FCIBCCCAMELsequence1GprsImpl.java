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

package org.mobicents.protocols.ss7.cap.service.gprs.primitive;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.primitives.AppendFreeFormatData;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.FCIBCCCAMELsequence1Gprs;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.FreeFormatDataGprs;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPID;
import org.mobicents.protocols.ss7.cap.primitives.SequenceBase;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class FCIBCCCAMELsequence1GprsImpl extends SequenceBase implements FCIBCCCAMELsequence1Gprs {

    public static final int _ID_freeFormatData = 0;
    public static final int _ID_pdpID = 1;
    public static final int _ID_appendFreeFormatData = 2;

    public static final int _ID_FCIBCCCAMELsequence1 = 0;

    private FreeFormatDataGprs freeFormatData;
    private PDPID pdpID;
    private AppendFreeFormatData appendFreeFormatData;

    public FCIBCCCAMELsequence1GprsImpl() {
        super("FCIBCCCAMELsequence1Gprs");
    }

    public FCIBCCCAMELsequence1GprsImpl(FreeFormatDataGprs freeFormatData, PDPID pdpID, AppendFreeFormatData appendFreeFormatData) {
        super("FCIBCCCAMELsequence1Gprs");
        this.freeFormatData = freeFormatData;
        this.pdpID = pdpID;
        this.appendFreeFormatData = appendFreeFormatData;
    }

    @Override
    public FreeFormatDataGprs getFreeFormatData() {
        return this.freeFormatData;
    }

    @Override
    public PDPID getPDPID() {
        return this.pdpID;
    }

    @Override
    public AppendFreeFormatData getAppendFreeFormatData() {
        return this.appendFreeFormatData;
    }

    @Override
    public int getTag() throws CAPException {
        return _ID_FCIBCCCAMELsequence1;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws CAPParsingComponentException, IOException, AsnException,
            MAPParsingComponentException {

        this.freeFormatData = null;
        this.pdpID = null;
        this.appendFreeFormatData = AppendFreeFormatData.overwrite;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_freeFormatData:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".freeFormatData: Parameter is not primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.freeFormatData = new FreeFormatDataGprsImpl();
                        ((FreeFormatDataGprsImpl) this.freeFormatData).decodeAll(ais);
                        break;
                    case _ID_pdpID:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".pdpID: Parameter is not primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.pdpID = new PDPIDImpl();
                        ((PDPIDImpl) this.pdpID).decodeAll(ais);
                        break;
                    case _ID_appendFreeFormatData:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".appendFreeFormatData: Parameter is not primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        int i1 = (int) ais.readInteger();
                        this.appendFreeFormatData = AppendFreeFormatData.getInstance(i1);
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }

        if (this.freeFormatData == null)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": parameter freeFormatData is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws CAPException {

        if (this.freeFormatData == null)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": freeFormatData must not be null");

        try {

            ((FreeFormatDataGprsImpl) this.freeFormatData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_freeFormatData);

            if (this.pdpID != null)
                ((PDPIDImpl) this.pdpID).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_pdpID);

            if (this.appendFreeFormatData != null) {
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_appendFreeFormatData, this.appendFreeFormatData.getCode());
            }

        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (IOException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.freeFormatData != null) {
            sb.append("freeFormatData=");
            sb.append(this.freeFormatData.toString());
            sb.append(", ");
        }

        if (this.pdpID != null) {
            sb.append("pdpID=");
            sb.append(this.pdpID.toString());
            sb.append(", ");
        }

        if (this.appendFreeFormatData != null) {
            sb.append("appendFreeFormatData=");
            sb.append(this.appendFreeFormatData.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
