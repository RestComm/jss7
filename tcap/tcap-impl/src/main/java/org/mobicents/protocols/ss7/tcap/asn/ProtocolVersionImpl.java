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

package org.mobicents.protocols.ss7.tcap.asn;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;

/**
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
public class ProtocolVersionImpl implements ProtocolVersion {

    private boolean supportedVersion = true;

    public boolean isSupportedVersion() {

        return supportedVersion;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols .asn.AsnInputStream)
     */
    public void decode(AsnInputStream ais) throws ParseException {

        try {

            BitSetStrictLength readV = ais.readBitString();
            if (readV.getStrictLength() >= 1 && readV.get(0)) {
                // ok
            } else {
                this.supportedVersion = false;
            }
        } catch (IOException e) {
            throw new ParseException(PAbortCauseType.BadlyFormattedTxPortion, null,
                    "IOException while decoding ProtocolVersion: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParseException(PAbortCauseType.BadlyFormattedTxPortion, null,
                    "AsnException while decoding ProtocolVersion: " + e.getMessage(), e);
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#encode(org.mobicents.protocols .asn.AsnOutputStream)
     */
    public void encode(AsnOutputStream aos) throws EncodeException {

        // commented code is the whole case encoding implementation
        // now only one version is supported - we use for optimization purpose simple encoding
        // aos.writeBitString(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_PROTOCOL_VERSION, _VALUE);
        aos.write(128);
        aos.write(2);
        aos.write(7);
        aos.write(128);

        // BitSetStrictLength bs = new BitSetStrictLength(1);
        // bs.set(0);
        // try {
        // aos.writeBitString(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_PROTOCOL_VERSION, bs);
        // } catch (AsnException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
    }
}
