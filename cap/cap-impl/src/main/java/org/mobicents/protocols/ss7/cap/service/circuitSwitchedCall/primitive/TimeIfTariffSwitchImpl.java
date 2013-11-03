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
import org.mobicents.protocols.ss7.cap.primitives.SequenceBase;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 *
 */
public class TimeIfTariffSwitchImpl extends SequenceBase implements TimeIfTariffSwitch {

    private static final String TIME_SINCE_TARIFF_SWITCH = "timeSinceTariffSwitch";
    private static final String TARIFF_SWITCH_INTERVAL = "tariffSwitchInterval";

    public static final int _ID_timeSinceTariffSwitch = 0;
    public static final int _ID_tariffSwitchInterval = 1;

    private int timeSinceTariffSwitch;
    private Integer tariffSwitchInterval;

    public TimeIfTariffSwitchImpl() {
        super("TimeIfTariffSwitch");
    }

    public TimeIfTariffSwitchImpl(int timeSinceTariffSwitch, Integer tariffSwitchInterval) {
        super("TimeIfTariffSwitch");
        this.timeSinceTariffSwitch = timeSinceTariffSwitch;
        this.tariffSwitchInterval = tariffSwitchInterval;
    }

    @Override
    public int getTimeSinceTariffSwitch() {
        return timeSinceTariffSwitch;
    }

    @Override
    public Integer getTariffSwitchInterval() {
        return tariffSwitchInterval;
    }

    protected void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, MAPParsingComponentException,
            IOException, AsnException {

        this.timeSinceTariffSwitch = -1;
        this.tariffSwitchInterval = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_timeSinceTariffSwitch:
                        this.timeSinceTariffSwitch = (int) ais.readInteger();
                        break;
                    case _ID_tariffSwitchInterval:
                        this.tariffSwitchInterval = (int) ais.readInteger();
                        break;

                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }

        if (this.timeSinceTariffSwitch == -1)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": timeSinceTariffSwitch is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);
    }

    @Override
    public void encodeData(AsnOutputStream aos) throws CAPException {

        try {
            if (this.timeSinceTariffSwitch < 0 || this.timeSinceTariffSwitch > 864000)
                throw new CAPException("Error while encoding " + _PrimitiveName
                        + ": timeSinceTariffSwitch must be from 0 to 864000");

            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_timeSinceTariffSwitch, this.timeSinceTariffSwitch);

            if (this.tariffSwitchInterval != null) {
                if (this.tariffSwitchInterval < 1 || this.tariffSwitchInterval > 864000)
                    throw new CAPException("Error while encoding " + _PrimitiveName
                            + ": tariffSwitchInterval must be from 1 to 864000");
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_tariffSwitchInterval, this.tariffSwitchInterval);
            }
        } catch (IOException e) {
            throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        sb.append("timeSinceTariffSwitch=");
        sb.append(timeSinceTariffSwitch);
        if (this.tariffSwitchInterval != null) {
            sb.append(", tariffSwitchInterval=");
            sb.append(tariffSwitchInterval);
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<TimeIfTariffSwitchImpl> TIME_IF_TARIFF_SWITCH_XML = new XMLFormat<TimeIfTariffSwitchImpl>(
            TimeIfTariffSwitchImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, TimeIfTariffSwitchImpl timeIfTariffSwitch)
                throws XMLStreamException {
            Integer ival = xml.get(TIME_SINCE_TARIFF_SWITCH, Integer.class);
            if (ival != null)
                timeIfTariffSwitch.timeSinceTariffSwitch = ival;

            timeIfTariffSwitch.tariffSwitchInterval = xml.get(TARIFF_SWITCH_INTERVAL, Integer.class);
        }

        @Override
        public void write(TimeIfTariffSwitchImpl timeIfTariffSwitch, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            xml.add(timeIfTariffSwitch.timeSinceTariffSwitch, TIME_SINCE_TARIFF_SWITCH, Integer.class);

            if (timeIfTariffSwitch.tariffSwitchInterval != null) {
                xml.add(timeIfTariffSwitch.tariffSwitchInterval, TARIFF_SWITCH_INTERVAL, Integer.class);
            }
        }
    };
}
