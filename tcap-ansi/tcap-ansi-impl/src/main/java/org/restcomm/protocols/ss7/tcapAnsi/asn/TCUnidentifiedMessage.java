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

package org.restcomm.protocols.ss7.tcapAnsi.asn;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.DialogPortion;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.Encodable;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.EncodeException;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ParseException;

/**
 * @author amit bhayani
 *
 */
public class TCUnidentifiedMessage implements Encodable {

    private static final Logger logger = Logger.getLogger(TCUnidentifiedMessage.class);

    private byte[] originatingTransactionId;
    private byte[] destinationTransactionId;
    private boolean dialogPortionExists = false;

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

    public boolean isDialogPortionExists() {
        return dialogPortionExists;
    }

    public void encode(AsnOutputStream aos) throws EncodeException {
        throw new EncodeException("Not Supported");
    }

    public void decode(AsnInputStream ais) throws ParseException {
        try {
            AsnInputStream localAis = ais.readSequenceStream();

            // transaction portion
            TransactionID tid = TcapFactory.readTransactionID(localAis);
            this.originatingTransactionId = tid.getFirstElem();
            this.destinationTransactionId = tid.getSecondElem();

            // dialog portion
            int tag = localAis.readTag();
            if (tag != DialogPortion._TAG_DIALOG_PORTION || localAis.getTagClass() != Tag.CLASS_PRIVATE || localAis.isTagPrimitive()) {
                return;
            }
            dialogPortionExists = true;

        } catch (IOException e) {
            logger.error("Error while decoding for TCUnidentifiedMessage", e);
        } catch (AsnException e) {
            logger.error("Error while decoding for TCUnidentifiedMessage", e);
        }

    }

}
