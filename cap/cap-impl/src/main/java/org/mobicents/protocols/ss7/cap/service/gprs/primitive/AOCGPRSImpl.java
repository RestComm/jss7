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
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AOCSubsequent;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAI_GSM0224;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.AOCGPRS;
import org.mobicents.protocols.ss7.cap.primitives.SequenceBase;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.AOCSubsequentImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.CAI_GSM0224Impl;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class AOCGPRSImpl extends SequenceBase implements AOCGPRS {

    public static final int _ID_aocInitial = 0;
    public static final int _ID_aocSubsequent = 1;

    private CAI_GSM0224 aocInitial;
    private AOCSubsequent aocSubsequent;

    public AOCGPRSImpl() {
        super("AOCGPRS");
    }

    public AOCGPRSImpl(CAI_GSM0224 aocInitial, AOCSubsequent aocSubsequent) {
        super("AOCGPRS");
        this.aocInitial = aocInitial;
        this.aocSubsequent = aocSubsequent;
    }

    @Override
    public CAI_GSM0224 getAOCInitial() {
        return this.aocInitial;
    }

    @Override
    public AOCSubsequent getAOCSubsequent() {
        return this.aocSubsequent;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws CAPParsingComponentException, IOException, AsnException,
            MAPParsingComponentException {

        this.aocInitial = null;
        this.aocSubsequent = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_aocInitial:
                        if (ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".aocInitial: Parameter is primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.aocInitial = new CAI_GSM0224Impl();
                        ((CAI_GSM0224Impl) this.aocInitial).decodeAll(ais);
                        break;
                    case _ID_aocSubsequent:
                        if (ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".aocSubsequent: Parameter is primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.aocSubsequent = new AOCSubsequentImpl();
                        ((AOCSubsequentImpl) this.aocSubsequent).decodeAll(ais);
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }

        if (this.aocInitial == null)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": aocInitial is mandatory but not found", CAPParsingComponentExceptionReason.MistypedParameter);
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws CAPException {

        if (this.aocInitial == null)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": aocInitial must not be null");

        ((CAI_GSM0224Impl) this.aocInitial).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_aocInitial);

        if (this.aocSubsequent != null)
            ((AOCSubsequentImpl) this.aocSubsequent).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_aocSubsequent);

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.aocInitial != null) {
            sb.append("aocInitial=");
            sb.append(this.aocInitial.toString());
            sb.append(", ");
        }

        if (this.aocSubsequent != null) {
            sb.append("aocSubsequent=");
            sb.append(this.aocSubsequent.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

}
