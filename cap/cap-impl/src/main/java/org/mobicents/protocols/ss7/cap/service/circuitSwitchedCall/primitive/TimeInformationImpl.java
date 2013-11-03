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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.TimeIfTariffSwitch;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.TimeInformation;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 *
 */
public class TimeInformationImpl implements TimeInformation, CAPAsnPrimitive {

    private static final String TIME_IF_NO_TARIFF_SWITCH = "timeIfNoTariffSwitch";
    private static final String TIME_IF_TARIFF_SWITCH = "timeIfTariffSwitch";

    public static final int _ID_timeIfNoTariffSwitch = 0;
    public static final int _ID_timeIfTariffSwitch = 1;

    public static final String _PrimitiveName = "TimeInformation";

    private Integer timeIfNoTariffSwitch;
    private TimeIfTariffSwitch timeIfTariffSwitch;

    public TimeInformationImpl() {
    }

    public TimeInformationImpl(int timeIfNoTariffSwitch) {
        this.timeIfNoTariffSwitch = timeIfNoTariffSwitch;
    }

    public TimeInformationImpl(TimeIfTariffSwitch timeIfTariffSwitch) {
        this.timeIfTariffSwitch = timeIfTariffSwitch;
    }

    @Override
    public Integer getTimeIfNoTariffSwitch() {
        return timeIfNoTariffSwitch;
    }

    @Override
    public TimeIfTariffSwitch getTimeIfTariffSwitch() {
        return timeIfTariffSwitch;
    }

    @Override
    public int getTag() throws CAPException {
        if (timeIfNoTariffSwitch != null)
            return _ID_timeIfNoTariffSwitch;
        else
            return _ID_timeIfTariffSwitch;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    @Override
    public boolean getIsPrimitive() {
        if (timeIfNoTariffSwitch != null)
            return true;
        else
            return false;
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

    private void _decode(AsnInputStream ais, int length) throws CAPParsingComponentException, MAPParsingComponentException,
            IOException, AsnException {

        this.timeIfNoTariffSwitch = null;
        this.timeIfTariffSwitch = null;

        int tag = ais.getTag();

        if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
            switch (tag) {
                case _ID_timeIfNoTariffSwitch:
                    this.timeIfNoTariffSwitch = (int) ais.readIntegerData(length);
                    break;
                case _ID_timeIfTariffSwitch:
                    this.timeIfTariffSwitch = new TimeIfTariffSwitchImpl();
                    ((TimeIfTariffSwitchImpl) this.timeIfTariffSwitch).decodeData(ais, length);
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
    public void encodeData(AsnOutputStream aos) throws CAPException {

        if (this.timeIfNoTariffSwitch == null && this.timeIfTariffSwitch == null)
            throw new CAPException("Error while encoding " + _PrimitiveName
                    + ": both timeIfNoTariffSwitch and this.timeIfTariffSwitch must not be null");

        try {
            if (this.timeIfNoTariffSwitch != null) {
                if (this.timeIfNoTariffSwitch < 0 || this.timeIfNoTariffSwitch > 864000)
                    throw new CAPException("Error while encoding " + _PrimitiveName
                            + ": timeIfNoTariffSwitch must be from 0 to 864000");
                aos.writeIntegerData(this.timeIfNoTariffSwitch);
            } else {
                ((TimeIfTariffSwitchImpl) this.timeIfTariffSwitch).encodeData(aos);
            }
        } catch (IOException e) {
            throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.timeIfNoTariffSwitch != null) {
            sb.append("timeIfNoTariffSwitch=");
            sb.append(timeIfNoTariffSwitch);
        }
        if (this.timeIfTariffSwitch != null) {
            sb.append("timeIfTariffSwitch=");
            sb.append(timeIfTariffSwitch.toString());
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<TimeInformationImpl> TIME_INFORMATION_XML = new XMLFormat<TimeInformationImpl>(
            TimeInformationImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, TimeInformationImpl timeInformation)
                throws XMLStreamException {
            timeInformation.timeIfNoTariffSwitch = xml.get(TIME_IF_NO_TARIFF_SWITCH, Integer.class);
            timeInformation.timeIfTariffSwitch = xml.get(TIME_IF_TARIFF_SWITCH, TimeIfTariffSwitchImpl.class);
        }

        @Override
        public void write(TimeInformationImpl timeInformation, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {

            if (timeInformation.timeIfNoTariffSwitch != null) {
                xml.add(timeInformation.timeIfNoTariffSwitch, TIME_IF_NO_TARIFF_SWITCH, Integer.class);
            }

            if (timeInformation.timeIfTariffSwitch != null) {
                xml.add((TimeIfTariffSwitchImpl) timeInformation.timeIfTariffSwitch, TIME_IF_TARIFF_SWITCH,
                        TimeIfTariffSwitchImpl.class);
            }
        }
    };
}
