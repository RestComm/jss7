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

import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * @author amit bhayani
 *
 */
public class TCUnidentifiedMessage implements Encodable {

    private static final Logger logger = Logger.getLogger(TCUnidentifiedMessage.class);

    public static final int _TAG_OTX = 0x08;

    public static final int _TAG_DTX = 0x09;

    // mandatory
    private byte[] originatingTransactionId;
    private byte[] destinationTransactionId;

    /**
     *
     */
    public TCUnidentifiedMessage() {
    }

    public byte[] getOriginatingTransactionId() {
        return originatingTransactionId;
    }

    public byte[] getDestinationTransactionId() {
        return destinationTransactionId;
    }

    public void encode(AsnOutputStream aos) throws EncodeException {
        throw new EncodeException("Not Supported");
    }

    public void decode(AsnInputStream ais) throws ParseException {
        try {
            AsnInputStream localAis = ais.readSequenceStream();

            int tag = localAis.readTag();
            if (tag != _TAG_OTX) {
                return;
            }

            // this.originatingTransactionId = Utils.readTransactionId(localAis);
            this.originatingTransactionId = localAis.readOctetString();

            if (localAis.available() > 0) {
                tag = localAis.readTag();

                if (tag != _TAG_DTX) {
                    return;
                }

                // this.destinationTransactionId = Utils.readTransactionId(localAis);
                this.destinationTransactionId = localAis.readOctetString();
            }

        } catch (IOException e) {
            logger.error("Error while decoding for TCUnidentifiedMessage", e);
        } catch (AsnException e) {
            logger.error("Error while decoding for TCUnidentifiedMessage", e);
        }

    }

}
