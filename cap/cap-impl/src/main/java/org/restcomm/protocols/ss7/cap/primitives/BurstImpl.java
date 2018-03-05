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

package org.restcomm.protocols.ss7.cap.primitives;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.CAPParsingComponentException;
import org.restcomm.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.restcomm.protocols.ss7.cap.api.primitives.Burst;
import org.restcomm.protocols.ss7.inap.api.INAPParsingComponentException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;

/**
*
* @author sergey vetyutnev
*
*/
public class BurstImpl extends SequenceBase implements Burst {

    public static final int _ID_numberOfBursts = 0;
    public static final int _ID_burstInterval = 1;
    public static final int _ID_numberOfTonesInBurst = 2;
    public static final int _ID_toneDuration = 3;
    public static final int _ID_toneInterval = 4;

    private static final String NUMBER_OF_BURSTS = "numberOfBursts";
    private static final String BURST_INTERVAL = "burstInterval";
    private static final String NUMBER_OF_TONES_IN_BURST = "numberOfTonesInBurst";
    private static final String TONE_DURATION = "toneDuration";
    private static final String TONE_INTERVAL = "toneInterval";

    private Integer numberOfBursts;
    private Integer burstInterval;
    private Integer numberOfTonesInBurst;
    private Integer toneDuration;
    private Integer toneInterval;

    public BurstImpl() {
        super("Burst");
    }

    public BurstImpl(Integer numberOfBursts, Integer burstInterval, Integer numberOfTonesInBurst, Integer toneDuration, Integer toneInterval) {
        super("Burst");

        this.numberOfBursts = numberOfBursts;
        this.burstInterval = burstInterval;
        this.numberOfTonesInBurst = numberOfTonesInBurst;
        this.toneDuration = toneDuration;
        this.toneInterval = toneInterval;
    }

    @Override
    public Integer getNumberOfBursts() {
        return numberOfBursts;
    }

    @Override
    public Integer getBurstInterval() {
        return burstInterval;
    }

    @Override
    public Integer getNumberOfTonesInBurst() {
        return numberOfTonesInBurst;
    }

    @Override
    public Integer getToneDuration() {
        return toneDuration;
    }

    @Override
    public Integer getToneInterval() {
        return toneInterval;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws CAPParsingComponentException, IOException, AsnException, MAPParsingComponentException,
            INAPParsingComponentException {

        this.numberOfBursts = null;
        this.burstInterval = null;
        this.numberOfTonesInBurst = null;
        this.toneDuration = null;
        this.toneInterval = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                case _ID_numberOfBursts:
                    this.numberOfBursts = (int) ais.readInteger();
                    if (this.numberOfBursts < 1 || this.numberOfBursts > 3)
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": numberOfBursts must be 1..3, received: "
                                + this.numberOfBursts, CAPParsingComponentExceptionReason.MistypedParameter);
                    break;
                case _ID_burstInterval:
                    this.burstInterval = (int) ais.readInteger();
                    if (this.burstInterval < 1 || this.burstInterval > 1200)
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": burstInterval must be 1..1200, received: "
                                + this.burstInterval, CAPParsingComponentExceptionReason.MistypedParameter);
                    break;
                case _ID_numberOfTonesInBurst:
                    this.numberOfTonesInBurst = (int) ais.readInteger();
                    if (this.numberOfTonesInBurst < 1 || this.numberOfTonesInBurst > 3)
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": numberOfTonesInBurst must be 1..3, received: "
                                + this.numberOfTonesInBurst, CAPParsingComponentExceptionReason.MistypedParameter);
                    break;
                case _ID_toneDuration:
                    this.toneDuration = (int) ais.readInteger();
                    if (this.toneDuration < 1 || this.toneDuration > 20)
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": toneDuration must be 1..20, received: "
                                + this.toneDuration, CAPParsingComponentExceptionReason.MistypedParameter);
                    break;
                case _ID_toneInterval:
                    this.toneInterval = (int) ais.readInteger();
                    if (this.toneInterval < 1 || this.toneInterval > 20)
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": toneInterval must be 1..20, received: "
                                + this.toneInterval, CAPParsingComponentExceptionReason.MistypedParameter);
                    break;

                default:
                    ais.advanceElement();
                    break;
                }
            } else {
                ais.advanceElement();
            }
        }
    }

    @Override
    public void encodeData(AsnOutputStream aos) throws CAPException {

        if (this.numberOfBursts != null && (this.numberOfBursts < 1 || this.numberOfBursts > 3))
            throw new CAPException("Error while encoding " + _PrimitiveName + ": numberOfBursts must be 1..3, supplied: " + this.numberOfBursts);
        if (this.burstInterval != null && (this.burstInterval < 1 || this.burstInterval > 1200))
            throw new CAPException("Error while encoding " + _PrimitiveName + ": burstInterval must be 1..1200, supplied: " + this.burstInterval);
        if (this.numberOfTonesInBurst != null && (this.numberOfTonesInBurst < 1 || this.numberOfTonesInBurst > 3))
            throw new CAPException("Error while encoding " + _PrimitiveName + ": numberOfTonesInBurst must be 1..3, supplied: " + this.numberOfTonesInBurst);
        if (this.toneDuration != null && (this.toneDuration < 1 || this.toneDuration > 20))
            throw new CAPException("Error while encoding " + _PrimitiveName + ": toneDuration must be 1..20, supplied: " + this.toneDuration);
        if (this.toneInterval != null && (this.toneInterval < 1 || this.toneInterval > 20))
            throw new CAPException("Error while encoding " + _PrimitiveName + ": toneInterval must be 1..20, supplied: " + this.toneInterval);

        try {
            if (numberOfBursts != null)
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_numberOfBursts, numberOfBursts);
            if (burstInterval != null)
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_burstInterval, burstInterval);
            if (numberOfTonesInBurst != null)
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_numberOfTonesInBurst, numberOfTonesInBurst);
            if (toneDuration != null)
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_toneDuration, toneDuration);
            if (toneInterval != null)
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_toneInterval, toneInterval);
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
        if (this.numberOfBursts != null) {
            sb.append("numberOfBursts=");
            sb.append(numberOfBursts);
            sb.append(", ");
        }
        if (this.burstInterval != null) {
            sb.append("burstInterval=");
            sb.append(burstInterval);
            sb.append(", ");
        }
        if (this.numberOfTonesInBurst != null) {
            sb.append("numberOfTonesInBurst=");
            sb.append(numberOfTonesInBurst);
            sb.append(", ");
        }
        if (this.toneDuration != null) {
            sb.append("toneDuration=");
            sb.append(toneDuration);
            sb.append(", ");
        }
        if (this.toneInterval != null) {
            sb.append("toneInterval=");
            sb.append(toneInterval);
            sb.append(", ");
        }
        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<BurstImpl> BURST_XML = new XMLFormat<BurstImpl>(BurstImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, BurstImpl burst) throws XMLStreamException {
            burst.numberOfBursts = xml.get(NUMBER_OF_BURSTS, Integer.class);
            burst.burstInterval = xml.get(BURST_INTERVAL, Integer.class);
            burst.numberOfTonesInBurst = xml.get(NUMBER_OF_TONES_IN_BURST, Integer.class);
            burst.toneDuration = xml.get(TONE_DURATION, Integer.class);
            burst.toneInterval = xml.get(TONE_INTERVAL, Integer.class);
        }

        @Override
        public void write(BurstImpl burst, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            if (burst.numberOfBursts != null)
                xml.add(burst.numberOfBursts, NUMBER_OF_BURSTS, Integer.class);
            if (burst.burstInterval != null)
                xml.add(burst.burstInterval, BURST_INTERVAL, Integer.class);
            if (burst.numberOfTonesInBurst != null)
                xml.add(burst.numberOfTonesInBurst, NUMBER_OF_TONES_IN_BURST, Integer.class);
            if (burst.toneDuration != null)
                xml.add(burst.toneDuration, TONE_DURATION, Integer.class);
            if (burst.toneInterval != null)
                xml.add(burst.toneInterval, TONE_INTERVAL, Integer.class);
        }
    };

}
