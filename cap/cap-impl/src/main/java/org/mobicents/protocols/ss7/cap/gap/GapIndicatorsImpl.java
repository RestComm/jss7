/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.mobicents.protocols.ss7.cap.gap;

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
import org.mobicents.protocols.ss7.cap.api.gap.GapIndicators;
import org.mobicents.protocols.ss7.cap.primitives.SequenceBase;

/**
 *
 * @author <a href="mailto:bartosz.krok@pro-ids.com"> Bartosz Krok (ProIDS sp. z o.o.)</a>
 */
public class GapIndicatorsImpl extends SequenceBase implements GapIndicators {

    private static final String DURATION = "duration";
    private static final String GAP_INTERVAL = "gapInterval";

    public static final int _ID_Duration = 0;
    public static final int _ID_Gap_Interval = 1;

    private static int DEFAULT_VALUE = 0;

    private int duration;
    private int gapInterval;

    public GapIndicatorsImpl() {
        super("GapIndicators");
    }

    public GapIndicatorsImpl(int duration, int gapInterval) {
        super("GapIndicators");
        this.duration = duration;
        this.gapInterval = gapInterval;
    }

    public int getDuration() {
        return duration;
    }

    public int getGapInterval() {
        return gapInterval;
    }

    protected void _decode(AsnInputStream asnIS, int length) throws CAPParsingComponentException, IOException, AsnException {

        this.duration = 0;
        this.gapInterval = 0;

        boolean foundDuration = false;
        boolean foundGapInterval = false;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0) {
                break;
            }

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {

                switch (tag) {
                    case _ID_Duration: {
                        this.duration = (int) ais.readInteger();
                        if (this.duration < -2 || this.duration > 86400) {
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + "-duration: possible value -2..86400, received="
                                    + duration, CAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        foundDuration = true;
                        break;
                    }
                    case _ID_Gap_Interval: {
                        this.gapInterval = (int) ais.readInteger();
                        if (this.gapInterval < -1 || this.gapInterval > 60000) {
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + "-gapInterval: possible value -1..60000, received="
                                    + gapInterval, CAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        foundGapInterval = true;
                        break;
                    }
                    default: {
                        ais.advanceElement();
                        break;
                    }
                }
            } else {
                ais.advanceElement();
            }
        }

        if (!foundDuration) {
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": duration parameter is not found", CAPParsingComponentExceptionReason.MistypedParameter);
        }
        if (!foundGapInterval) {
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": gapInterval parameter is not found", CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void encodeData(AsnOutputStream asnOs) throws CAPException {

        try {
            if (this.duration < -2 || this.duration > 86400) {
                throw new CAPException("Error when encoding " + _PrimitiveName + ": duration must be -2..86400, supplied="
                        + duration);
            }
            asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_Duration, duration);
            if (this.gapInterval < -1 || this.gapInterval > 60000) {
                throw new CAPException("Error when encoding " + _PrimitiveName + ": gapInterval must be -1..60000, supplied="
                        + gapInterval);
            }
            asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_Gap_Interval, gapInterval);
        } catch (IOException e) {
            throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException ex) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + ex.getMessage(), ex);
        }
    }

    protected static final XMLFormat<GapIndicatorsImpl> GAP_INDICATORS_XML = new XMLFormat<GapIndicatorsImpl>(GapIndicatorsImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, GapIndicatorsImpl gapIndicators) throws XMLStreamException {
            gapIndicators.duration = xml.getAttribute(DURATION, DEFAULT_VALUE);
            gapIndicators.gapInterval = xml.getAttribute(GAP_INTERVAL, DEFAULT_VALUE);
        }

        @Override
        public void write(GapIndicatorsImpl gapIndicators, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            xml.setAttribute(DURATION, gapIndicators.duration);
            xml.setAttribute(GAP_INTERVAL, gapIndicators.gapInterval);
        }
    };

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        sb.append("duration=");
        sb.append(duration);
        sb.append(", gapInterval=");
        sb.append(gapInterval);

        sb.append("]");

        return sb.toString();
    }

}
