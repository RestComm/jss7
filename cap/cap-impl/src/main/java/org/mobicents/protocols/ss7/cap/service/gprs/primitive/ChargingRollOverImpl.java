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
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.ChargingRollOver;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.ElapsedTimeRollOver;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.TransferredVolumeRollOver;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class ChargingRollOverImpl implements ChargingRollOver, CAPAsnPrimitive {

    public static final String _PrimitiveName = "ChargingRollOver";

    public static final int _ID_transferredVolumeRollOver = 0;
    public static final int _ID_elapsedTimeRollOver = 1;

    private TransferredVolumeRollOver transferredVolumeRollOver;
    private ElapsedTimeRollOver elapsedTimeRollOver;

    public ChargingRollOverImpl() {
    }

    public ChargingRollOverImpl(TransferredVolumeRollOver transferredVolumeRollOver) {
        this.transferredVolumeRollOver = transferredVolumeRollOver;
    }

    public ChargingRollOverImpl(ElapsedTimeRollOver elapsedTimeRollOver) {
        this.elapsedTimeRollOver = elapsedTimeRollOver;
    }

    @Override
    public TransferredVolumeRollOver getTransferredVolumeRollOver() {
        return this.transferredVolumeRollOver;
    }

    public ElapsedTimeRollOver getElapsedTimeRollOver() {
        return this.elapsedTimeRollOver;
    }

    @Override
    public int getTag() throws CAPException {
        if (transferredVolumeRollOver != null) {
            return _ID_transferredVolumeRollOver;
        } else {
            return _ID_elapsedTimeRollOver;
        }
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    @Override
    public boolean getIsPrimitive() {

        if (transferredVolumeRollOver != null) {
            return ((TransferredVolumeRollOverImpl) transferredVolumeRollOver).getIsPrimitive();
        } else {
            return ((ElapsedTimeRollOverImpl) elapsedTimeRollOver).getIsPrimitive();
        }
    }

    @Override
    public void decodeAll(AsnInputStream ansIS) throws CAPParsingComponentException {
        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (MAPParsingComponentException e) {
            throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void decodeData(AsnInputStream ansIS, int length) throws CAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (MAPParsingComponentException e) {
            throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        }

    }

    private void _decode(AsnInputStream ais, int length) throws CAPParsingComponentException, IOException, AsnException,
            MAPParsingComponentException {

        this.transferredVolumeRollOver = null;
        this.elapsedTimeRollOver = null;

        int tag = ais.getTag();

        if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {

            switch (tag) {
                case _ID_transferredVolumeRollOver:
                    this.transferredVolumeRollOver = new TransferredVolumeRollOverImpl();
                    ais.readTag();
                    ((TransferredVolumeRollOverImpl) this.transferredVolumeRollOver).decodeAll(ais);
                    break;
                case _ID_elapsedTimeRollOver:
                    this.elapsedTimeRollOver = new ElapsedTimeRollOverImpl();
                    ais.readTag();
                    ((ElapsedTimeRollOverImpl) this.elapsedTimeRollOver).decodeAll(ais);
                    break;
                default:
                    throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tag",
                            CAPParsingComponentExceptionReason.MistypedParameter);
            }
        } else {
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tagClass",
                    CAPParsingComponentExceptionReason.MistypedParameter);
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

        if (this.transferredVolumeRollOver == null && this.elapsedTimeRollOver == null
                || this.transferredVolumeRollOver != null && this.elapsedTimeRollOver != null) {
            throw new CAPException("Error while decoding " + _PrimitiveName + ": One and only one choice must be selected");
        }

        if (this.transferredVolumeRollOver != null) {
            ((TransferredVolumeRollOverImpl) this.transferredVolumeRollOver).encodeAll(asnOs);
        } else {
            ((ElapsedTimeRollOverImpl) this.elapsedTimeRollOver).encodeAll(asnOs);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.transferredVolumeRollOver != null) {
            sb.append("transferredVolumeRollOver=");
            sb.append(this.transferredVolumeRollOver.toString());
        }

        if (this.elapsedTimeRollOver != null) {
            sb.append("elapsedTimeRollOver=");
            sb.append(this.elapsedTimeRollOver.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}