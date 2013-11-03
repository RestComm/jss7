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
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.TimeGPRSIfTariffSwitch;
import org.mobicents.protocols.ss7.cap.primitives.SequenceBase;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class TimeGPRSIfTariffSwitchImpl extends SequenceBase implements TimeGPRSIfTariffSwitch {

    public static final int _ID_timeGPRSSinceLastTariffSwitch = 0;
    public static final int _ID_timeGPRSTariffSwitchInterval = 1;

    public int timeGPRSSinceLastTariffSwitch;
    public Integer timeGPRSTariffSwitchInterval;

    public TimeGPRSIfTariffSwitchImpl() {
        super("TimeGPRSIfTariffSwitch");
    }

    public TimeGPRSIfTariffSwitchImpl(int timeGPRSSinceLastTariffSwitch, Integer timeGPRSTariffSwitchInterval) {
        super("TimeGPRSIfTariffSwitch");
        this.timeGPRSSinceLastTariffSwitch = timeGPRSSinceLastTariffSwitch;
        this.timeGPRSTariffSwitchInterval = timeGPRSTariffSwitchInterval;
    }

    @Override
    public int getTimeGPRSSinceLastTariffSwitch() {
        return this.timeGPRSSinceLastTariffSwitch;
    }

    @Override
    public Integer getTimeGPRSTariffSwitchInterval() {
        return this.timeGPRSTariffSwitchInterval;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws CAPParsingComponentException, IOException, AsnException {

        boolean istimeGPRSSinceLastTariffSwitchIncluded = false;

        this.timeGPRSSinceLastTariffSwitch = -1;
        this.timeGPRSTariffSwitchInterval = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_timeGPRSSinceLastTariffSwitch:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".timeGPRSSinceLastTariffSwitch: Parameter is primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.timeGPRSSinceLastTariffSwitch = (int) ais.readInteger();
                        istimeGPRSSinceLastTariffSwitchIncluded = true;
                        break;
                    case _ID_timeGPRSTariffSwitchInterval:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".timeGPRSTariffSwitchInterval: Parameter is primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.timeGPRSTariffSwitchInterval = (int) ais.readInteger();
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }

        if (!istimeGPRSSinceLastTariffSwitchIncluded)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": timeGPRSSinceLastTariffSwitch is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws CAPException {

        try {
            asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_timeGPRSSinceLastTariffSwitch,
                    this.timeGPRSSinceLastTariffSwitch);

            if (this.timeGPRSTariffSwitchInterval != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_timeGPRSTariffSwitchInterval,
                        this.timeGPRSTariffSwitchInterval.intValue());

        } catch (IOException e) {
            throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        sb.append("timeGPRSSinceLastTariffSwitch=");
        sb.append(this.timeGPRSSinceLastTariffSwitch);
        sb.append(", ");

        if (this.timeGPRSTariffSwitchInterval != null) {
            sb.append("timeGPRSTariffSwitchInterval=");
            sb.append(this.timeGPRSTariffSwitchInterval.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
