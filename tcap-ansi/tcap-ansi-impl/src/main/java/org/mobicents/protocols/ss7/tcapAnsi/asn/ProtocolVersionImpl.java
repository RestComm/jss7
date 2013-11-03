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

package org.mobicents.protocols.ss7.tcapAnsi.asn;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.EncodeException;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ParseException;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ProtocolVersion;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.PAbortCause;

/**
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
public class ProtocolVersionImpl implements ProtocolVersion {

    private int data;

    /**
     * Creating ProtocolVersion that support both T1_114_1996Supported and T1_114_2000Supported
     */
    public ProtocolVersionImpl() {
        this.data = ProtocolVersion._TAG_T1_114_1996 + ProtocolVersion._TAG_T1_114_2000;
    }

    @Override
    public boolean isT1_114_1996Supported() {
        if ((this.data & ProtocolVersion._TAG_T1_114_1996) != 0)
            return true;
        else
            return false;
    }

    @Override
    public boolean isT1_114_2000Supported() {
        if ((this.data & ProtocolVersion._TAG_T1_114_2000) != 0)
            return true;
        else
            return false;
    }

    @Override
    public boolean isSupportedVersion() {
        if ((this.data & ProtocolVersion._TAG_T1_114_1996) != 0 || (this.data & ProtocolVersion._TAG_T1_114_1996) != 0)
            return true;
        else
            return false;
    }

    public void setT1_114_1996Supported(boolean val) {
        if (val)
            this.data = this.data | ProtocolVersion._TAG_T1_114_1996;
        else
            this.data = this.data & (ProtocolVersion._TAG_T1_114_1996 ^ 0xFF);
    }

    public void setT1_114_2000Supported(boolean val) {
        if (val)
            this.data = this.data | ProtocolVersion._TAG_T1_114_2000;
        else
            this.data = this.data & (ProtocolVersion._TAG_T1_114_2000 ^ 0xFF);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols .asn.AsnInputStream)
     */
    public void decode(AsnInputStream ais) throws ParseException {

        try {

            byte[] buf = ais.readOctetString();
            if (buf == null || buf.length != 1)
                throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion, "Error decoding ProtocolVersion: bad octet string length");

            this.data = (buf[0] & 0xFF);
        } catch (IOException e) {
            throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion, "IOException while decoding ProtocolVersion: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion, "AsnException while decoding ProtocolVersion: " + e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#encode(org.mobicents.protocols .asn.AsnOutputStream)
     */
    public void encode(AsnOutputStream aos) throws EncodeException {
        try {
            aos.writeOctetString(Tag.CLASS_PRIVATE, ProtocolVersion._TAG_PROTOCOL_VERSION, new byte[] { (byte) this.data });
        } catch (IOException e) {
            throw new EncodeException("IOException while encoding ProtocolVersion: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new EncodeException("AsnException while encoding ProtocolVersion: " + e.getMessage(), e);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ProtocolVersion[");
        if (this.isT1_114_1996Supported()) {
            sb.append("T1_114_1996Supported, ");
        }
        if (this.isT1_114_2000Supported()) {
            sb.append("T1_114_2000Supported, ");
        }
        sb.append("]");
        return sb.toString();
    }
}
