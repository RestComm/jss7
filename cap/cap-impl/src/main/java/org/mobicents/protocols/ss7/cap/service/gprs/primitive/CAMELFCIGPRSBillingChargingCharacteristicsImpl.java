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
package org.mobicents.protocols.ss7.cap.service.gprs.primitive;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.CAMELFCIGPRSBillingChargingCharacteristics;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.FCIBCCCAMELsequence1;
import org.mobicents.protocols.ss7.cap.primitives.SequenceBase;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class CAMELFCIGPRSBillingChargingCharacteristicsImpl extends SequenceBase implements
        CAMELFCIGPRSBillingChargingCharacteristics {

    public static final int _ID_fcIBCCCAMELsequence1 = 0;

    private FCIBCCCAMELsequence1 fcIBCCCAMELsequence1;

    public CAMELFCIGPRSBillingChargingCharacteristicsImpl() {
        super("CAMELFCIGPRSBillingChargingCharacteristics");
    }

    public CAMELFCIGPRSBillingChargingCharacteristicsImpl(FCIBCCCAMELsequence1 fcIBCCCAMELsequence1) {
        super("CAMELFCIGPRSBillingChargingCharacteristics");
        this.fcIBCCCAMELsequence1 = fcIBCCCAMELsequence1;
    }

    @Override
    public FCIBCCCAMELsequence1 getFCIBCCCAMELsequence1() {
        return this.fcIBCCCAMELsequence1;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws CAPParsingComponentException, IOException, AsnException,
            MAPParsingComponentException {

        this.fcIBCCCAMELsequence1 = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_fcIBCCCAMELsequence1:
                        if (ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".fcIBCCCAMELsequence1: Parameter is primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.fcIBCCCAMELsequence1 = new FCIBCCCAMELsequence1Impl();
                        ((FCIBCCCAMELsequence1Impl) this.fcIBCCCAMELsequence1).decodeAll(ais);
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }

        if (this.fcIBCCCAMELsequence1 == null)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": fcIBCCCAMELsequence1 is mandatory but not found", CAPParsingComponentExceptionReason.MistypedParameter);

    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws CAPException {

        if (this.fcIBCCCAMELsequence1 == null)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": fcIBCCCAMELsequence1 must not be null");

        ((FCIBCCCAMELsequence1Impl) this.fcIBCCCAMELsequence1).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                _ID_fcIBCCCAMELsequence1);

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.fcIBCCCAMELsequence1 != null) {
            sb.append("fcIBCCCAMELsequence1=");
            sb.append(this.fcIBCCCAMELsequence1.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
