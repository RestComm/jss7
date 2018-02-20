/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

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
import org.restcomm.protocols.ss7.cap.api.primitives.BurstList;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AudibleIndicator;
import org.restcomm.protocols.ss7.cap.primitives.BurstListImpl;
import org.restcomm.protocols.ss7.cap.primitives.CAPAsnPrimitive;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;

/**
*
* @author sergey vetyutnev
*
*/
public class AudibleIndicatorImpl implements AudibleIndicator, CAPAsnPrimitive {
    private static final String TONE = "tone";
    private static final String BURST_LIST = "burstList";

    public static final int _ID_burstList = 1;

    public static final String _PrimitiveName = "AudibleIndicator";

    private Boolean tone;
    private BurstList burstList;

    public AudibleIndicatorImpl() {
    }

    public AudibleIndicatorImpl(Boolean tone) {
        this.tone = tone;
    }

    public AudibleIndicatorImpl(BurstList burstList) {
        this.burstList = burstList;
    }

    @Override
    public Boolean getTone() {
        return tone;
    }

    @Override
    public BurstList getBurstList() {
        return burstList;
    }

    @Override
    public int getTag() throws CAPException {

        if (tone != null) {
            return Tag.BOOLEAN;
        } else if (burstList != null) {
            return _ID_burstList;
        }

        throw new CAPException("Error while encoding " + _PrimitiveName + ": no choice is specified");
    }

    @Override
    public int getTagClass() {
        if (tone != null) {
            return Tag.CLASS_UNIVERSAL;
        } else {
            return Tag.CLASS_CONTEXT_SPECIFIC;
        }
    }

    @Override
    public boolean getIsPrimitive() {
        if (tone != null) {
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

    private void _decode(AsnInputStream ais, int length) throws CAPParsingComponentException, MAPParsingComponentException,
            IOException, AsnException {

        this.tone = null;
        this.burstList = null;

        int tag = ais.getTag();

        if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
            switch (tag) {
            case Tag.BOOLEAN:
                if(!ais.isTagPrimitive())
                    throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + "- tone: parameter is not primitive",
                            CAPParsingComponentExceptionReason.MistypedParameter);

                this.tone = ais.readBooleanData(length);
                break;

                default:
                    throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tag",
                            CAPParsingComponentExceptionReason.MistypedParameter);
            }
        } else if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
            switch (tag) {
            case _ID_burstList:
                this.burstList = new BurstListImpl();
                ((BurstListImpl) this.burstList).decodeData(ais, length);
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
        try {
            if (tone != null) {
                asnOs.writeBooleanData(tone);
                return;
            } else if (burstList != null) {
                ((BurstListImpl) burstList).encodeData(asnOs);
                return;
            }
        } catch (IOException e) {
            throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }

        throw new CAPException("Error while encoding " + _PrimitiveName + ": no choice is specified");
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (tone != null) {
            sb.append("tone=[");
            sb.append(tone);
            sb.append("]");
        } else if (burstList != null) {
            sb.append("burstList=[");
            sb.append(burstList);
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }

    protected static final XMLFormat<AudibleIndicatorImpl> AUDIBLE_INDICATOR_XML = new XMLFormat<AudibleIndicatorImpl>(AudibleIndicatorImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, AudibleIndicatorImpl audibleIndicator) throws XMLStreamException {
            audibleIndicator.tone = xml.get(TONE, Boolean.class);
            audibleIndicator.burstList = xml.get(BURST_LIST, BurstListImpl.class);
        }

        @Override
        public void write(AudibleIndicatorImpl audibleIndicator, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            if (audibleIndicator.tone != null) {
                xml.add(audibleIndicator.tone, TONE, Boolean.class);
            }
            if (audibleIndicator.burstList != null) {
                xml.add((BurstListImpl) audibleIndicator.burstList, BURST_LIST, BurstListImpl.class);
            }
        }
    };

}
