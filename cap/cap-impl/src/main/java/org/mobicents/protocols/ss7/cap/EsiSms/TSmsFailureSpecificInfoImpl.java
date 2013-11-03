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
package org.mobicents.protocols.ss7.cap.EsiSms;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.EsiSms.TSmsFailureSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.MTSMSCause;
import org.mobicents.protocols.ss7.cap.primitives.SequenceBase;
import org.mobicents.protocols.ss7.cap.service.sms.primitive.MTSMSCauseImpl;
import org.mobicents.protocols.ss7.inap.api.INAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class TSmsFailureSpecificInfoImpl extends SequenceBase implements TSmsFailureSpecificInfo {

    public static final int _ID_failureCause = 0;

    private MTSMSCause failureCause;

    public TSmsFailureSpecificInfoImpl() {
        super("TSmsFailureSpecificInfo");
    }

    public TSmsFailureSpecificInfoImpl(MTSMSCause failureCause) {
        super("TSmsFailureSpecificInfo");
        this.failureCause = failureCause;
    }

    @Override
    public MTSMSCause GetFailureCause() {
        return this.failureCause;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws CAPParsingComponentException, IOException,
            AsnException, MAPParsingComponentException, INAPParsingComponentException {
        this.failureCause = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {

                case _ID_failureCause:
                    if (!ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".failureCause: Parameter is not  primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.failureCause = new MTSMSCauseImpl();
                    ((MTSMSCauseImpl) this.failureCause).decodeAll(ais);
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
    public void encodeData(AsnOutputStream asnOs) throws CAPException {
        if (this.failureCause != null)
            ((MTSMSCauseImpl) this.failureCause).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_failureCause);

    }

}
