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

package org.mobicents.protocols.ss7.cap.EsiBcsm;

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
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.MidCallEvents;
import org.mobicents.protocols.ss7.cap.api.isup.Digits;
import org.mobicents.protocols.ss7.cap.isup.DigitsImpl;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
*
* @author sergey vetyutnev
*
*/
public class MidCallEventsImpl implements MidCallEvents, CAPAsnPrimitive {
    private static final String DTMF_DIGITS_COMPLETED = "dtmfDigitsCompleted";
    private static final String DTMF_DIGITS_TIME_OUT = "dtmfDigitsTimeOut";

    public static final int _ID_dTMFDigitsCompleted = 3;
    public static final int _ID_dTMFDigitsTimeOut = 4;

    public static final String _PrimitiveName = "MidCallEvents";

    private Digits dtmfDigitsCompleted;
    private Digits dtmfDigitsTimeOut;

    public MidCallEventsImpl() {
    }

    public MidCallEventsImpl(Digits dtmfDigits, boolean isDtmfDigitsCompleted) {
        if (isDtmfDigitsCompleted)
            dtmfDigitsCompleted = dtmfDigits;
        else
            dtmfDigitsTimeOut = dtmfDigits;
    }

    @Override
    public Digits getDTMFDigitsCompleted() {
        return dtmfDigitsCompleted;
    }

    @Override
    public Digits getDTMFDigitsTimeOut() {
        return dtmfDigitsTimeOut;
    }


    @Override
    public int getTag() throws CAPException {

        if (dtmfDigitsCompleted != null) {
            return _ID_dTMFDigitsCompleted;
        } else if (dtmfDigitsTimeOut != null) {
            return _ID_dTMFDigitsTimeOut;
        }

        throw new CAPException("Error while encoding " + _PrimitiveName + ": no choice is specified");
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    @Override
    public boolean getIsPrimitive() {
        return true;
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

        this.dtmfDigitsCompleted = null;
        this.dtmfDigitsTimeOut = null;

        int tag = ais.getTag();

        if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
            switch (tag) {
            case _ID_dTMFDigitsCompleted:
                this.dtmfDigitsCompleted = new DigitsImpl();
                this.dtmfDigitsCompleted.setIsGenericDigits();
                ((DigitsImpl) this.dtmfDigitsCompleted).decodeData(ais, length);
                break;
            case _ID_dTMFDigitsTimeOut:
                this.dtmfDigitsTimeOut = new DigitsImpl();
                this.dtmfDigitsTimeOut.setIsGenericDigits();
                ((DigitsImpl) this.dtmfDigitsTimeOut).decodeData(ais, length);
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
        if (dtmfDigitsCompleted != null) {
            ((DigitsImpl) dtmfDigitsCompleted).encodeData(asnOs);
            return;
        } else if (dtmfDigitsTimeOut != null) {
            ((DigitsImpl) dtmfDigitsTimeOut).encodeData(asnOs);
            return;
        }

        throw new CAPException("Error while encoding " + _PrimitiveName + ": no choice is specified");
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (dtmfDigitsCompleted != null) {
            sb.append("dtmfDigitsCompleted=[");
            sb.append(dtmfDigitsCompleted.toString());
            sb.append("]");
        } else if (dtmfDigitsTimeOut != null) {
            sb.append("dtmfDigitsTimeOut=[");
            sb.append(dtmfDigitsTimeOut.toString());
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }

    protected static final XMLFormat<MidCallEventsImpl> MID_CALL_EVENTS_XML = new XMLFormat<MidCallEventsImpl>(MidCallEventsImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, MidCallEventsImpl midCallEvents) throws XMLStreamException {
            midCallEvents.dtmfDigitsCompleted = xml.get(DTMF_DIGITS_COMPLETED, DigitsImpl.class);
            if (midCallEvents.dtmfDigitsCompleted != null)
                midCallEvents.dtmfDigitsCompleted.setIsGenericDigits();
            midCallEvents.dtmfDigitsTimeOut = xml.get(DTMF_DIGITS_TIME_OUT, DigitsImpl.class);
            if (midCallEvents.dtmfDigitsTimeOut != null)
                midCallEvents.dtmfDigitsTimeOut.setIsGenericDigits();
        }

        @Override
        public void write(MidCallEventsImpl midCallEvents, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            if (midCallEvents.dtmfDigitsCompleted != null) {
                xml.add((DigitsImpl) midCallEvents.dtmfDigitsCompleted, DTMF_DIGITS_COMPLETED, DigitsImpl.class);
            }
            if (midCallEvents.dtmfDigitsTimeOut != null) {
                xml.add((DigitsImpl) midCallEvents.dtmfDigitsTimeOut, DTMF_DIGITS_TIME_OUT, DigitsImpl.class);
            }
        }
    };

}
