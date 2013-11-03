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
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.ElapsedTime;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.TimeGPRSIfTariffSwitch;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class ElapsedTimeImpl implements ElapsedTime, CAPAsnPrimitive {

    public static final String _PrimitiveName = "ElapsedTime";

    public static final int _ID_timeGPRSIfNoTariffSwitch = 0;
    public static final int _ID_timeGPRSIfTariffSwitch = 1;

    private Integer timeGPRSIfNoTariffSwitch;
    private TimeGPRSIfTariffSwitch timeGPRSIfTariffSwitch;

    public ElapsedTimeImpl() {

    }

    public ElapsedTimeImpl(Integer timeGPRSIfNoTariffSwitch) {
        this.timeGPRSIfNoTariffSwitch = timeGPRSIfNoTariffSwitch;
    }

    public ElapsedTimeImpl(TimeGPRSIfTariffSwitch timeGPRSIfTariffSwitch) {
        this.timeGPRSIfTariffSwitch = timeGPRSIfTariffSwitch;
    }

    @Override
    public Integer getTimeGPRSIfNoTariffSwitch() {
        return this.timeGPRSIfNoTariffSwitch;
    }

    @Override
    public TimeGPRSIfTariffSwitch getTimeGPRSIfTariffSwitch() {
        return this.timeGPRSIfTariffSwitch;
    }

    @Override
    public int getTag() throws CAPException {
        if (timeGPRSIfNoTariffSwitch != null) {
            return _ID_timeGPRSIfNoTariffSwitch;
        } else {
            return _ID_timeGPRSIfTariffSwitch;
        }
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    @Override
    public boolean getIsPrimitive() {
        if (timeGPRSIfNoTariffSwitch != null) {
            return true;
        } else {
            return false;
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

        this.timeGPRSIfNoTariffSwitch = null;
        this.timeGPRSIfTariffSwitch = null;

        int tag = ais.getTag();

        if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
            switch (tag) {
                case _ID_timeGPRSIfNoTariffSwitch:
                    if (!ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".timeGPRSIfNoTariffSwitch: Parameter is not primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.timeGPRSIfNoTariffSwitch = (int) ais.readIntegerData(length);
                    break;
                case _ID_timeGPRSIfTariffSwitch:
                    if (ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".timeGPRSIfTariffSwitch: Parameter is primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.timeGPRSIfTariffSwitch = new TimeGPRSIfTariffSwitchImpl();
                    ((TimeGPRSIfTariffSwitchImpl) this.timeGPRSIfTariffSwitch).decodeData(ais, length);
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
        if (this.timeGPRSIfNoTariffSwitch == null && this.timeGPRSIfTariffSwitch == null
                || this.timeGPRSIfNoTariffSwitch != null && this.timeGPRSIfTariffSwitch != null) {
            throw new CAPException("Error while decoding " + _PrimitiveName + ": One and only one choice must be selected");
        }
        try {
            if (this.timeGPRSIfNoTariffSwitch != null) {
                asnOs.writeIntegerData(timeGPRSIfNoTariffSwitch.intValue());
            } else {
                ((TimeGPRSIfTariffSwitchImpl) this.timeGPRSIfTariffSwitch).encodeData(asnOs);
            }
        } catch (IOException e) {
            throw new CAPException("MAPException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.timeGPRSIfNoTariffSwitch != null) {
            sb.append("timeGPRSIfNoTariffSwitch=");
            sb.append(this.timeGPRSIfNoTariffSwitch.toString());
        }

        if (this.timeGPRSIfTariffSwitch != null) {
            sb.append(", timeGPRSIfTariffSwitch=");
            sb.append(this.timeGPRSIfTariffSwitch.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}