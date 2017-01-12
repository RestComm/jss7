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

    private static final int _ID_Duration = 0;
    private static final int _ID_Gap_Interval = 1;

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
                        break;
                    }
                    case _ID_Gap_Interval: {
                        this.gapInterval = (int) ais.readInteger();
                        if (this.gapInterval < -1 || this.gapInterval > 60000) {
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + "-gapInterval: possible value -1..60000, received="
                                    + gapInterval, CAPParsingComponentExceptionReason.MistypedParameter);
                        }
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
    }

    public void encodeData(AsnOutputStream asnOs) throws CAPException {

        try {
            if (duration != 0) {
                if (this.duration < -2 || this.duration > 86400) {
                    throw new CAPException("Error when encoding " + _PrimitiveName + ": duration must be -2..86400, supplied=" + duration);
                }
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_Duration, duration);
            }
            if (gapInterval != 0) {
                if (this.gapInterval < -1 || this.gapInterval > 60000) {
                    throw new CAPException("Error when encoding " + _PrimitiveName + ": gapInterval must be -1..60000, supplied=" + gapInterval);
                }
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_Gap_Interval, gapInterval);
            }
        } catch (IOException e) {
            throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException ex) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + ex.getMessage(), ex);
        }
    }

    protected static final XMLFormat<GapIndicatorsImpl> GAP_INDICATORS_XML = new XMLFormat<GapIndicatorsImpl>(GapIndicatorsImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, GapIndicatorsImpl gapIndicators) throws XMLStreamException {
            Integer i1 = xml.get(DURATION, Integer.class);
            if (i1 != null) {
                gapIndicators.duration = i1;
            }

            Integer i2 = xml.get(GAP_INTERVAL, Integer.class);
            if (i2 != null) {
                gapIndicators.gapInterval = i2;
            }
        }

        @Override
        public void write(GapIndicatorsImpl gapIndicators, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            if (gapIndicators.duration != 0) {
                xml.add((Integer) gapIndicators.duration, DURATION, Integer.class);
            }
            if (gapIndicators.gapInterval != 0) {
                xml.add((Integer) gapIndicators.gapInterval, GAP_INTERVAL, Integer.class);
            }
        }
    };

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (duration != 0) {
            sb.append("duration=[");
            sb.append(duration);
            sb.append("]");
        } else if (gapInterval != 0) {
            sb.append("gapInterval=[");
            sb.append(gapInterval);
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }

}
