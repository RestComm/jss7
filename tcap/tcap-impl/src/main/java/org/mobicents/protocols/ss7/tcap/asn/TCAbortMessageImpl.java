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
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCAbortMessage;

/**
 * @author amit bhayani
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
public class TCAbortMessageImpl implements TCAbortMessage {

    private static final String _OCTET_STRING_ENCODE = "US-ASCII";

    private byte[] destTxId;
    private PAbortCauseType type;
    private DialogPortion dp;

    /*
     * (non-Javadoc)
     *
     * @seeorg.mobicents.protocols.ss7.tcap.asn.comp.TCAbortMessage# getDestinationTransactionId()
     */
    public byte[] getDestinationTransactionId() {

        return destTxId;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.TCAbortMessage#getDialogPortion ()
     */
    public DialogPortion getDialogPortion() {

        return dp;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.TCAbortMessage#getPAbortCause()
     */
    public PAbortCauseType getPAbortCause() {

        return type;
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.mobicents.protocols.ss7.tcap.asn.comp.TCAbortMessage# setDestinationTransactionId()
     */
    public void setDestinationTransactionId(byte[] t) {
        this.destTxId = t;

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.TCAbortMessage#setDialogPortion
     * (org.mobicents.protocols.ss7.tcap.asn.DialogPortion)
     */
    public void setDialogPortion(DialogPortion dp) {
        this.dp = dp;
        this.type = null;

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.TCAbortMessage#setPAbortCause
     * (org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType)
     */
    public void setPAbortCause(PAbortCauseType t) {
        this.type = t;
        this.dp = null;

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols .asn.AsnInputStream)
     */
    public void decode(AsnInputStream ais) throws ParseException {
        try {
            AsnInputStream localAis = ais.readSequenceStream();

            int tag = localAis.readTag();
            if (tag != _TAG_DTX || localAis.getTagClass() != Tag.CLASS_APPLICATION)
                throw new ParseException(PAbortCauseType.IncorrectTxPortion, null,
                        "Error decoding TC-Abort: Expected DestinationTransactionId, found tag: " + tag);
            this.destTxId = localAis.readOctetString();

            if (localAis.available() == 0)
                return;
            tag = localAis.readTag();
            if (localAis.getTagClass() != Tag.CLASS_APPLICATION)
                throw new ParseException(PAbortCauseType.IncorrectTxPortion, null,
                        "Error decoding TC-Abort: DialogPortion and P-AbortCause portion must has tag class CLASS_APPLICATION");

            switch (tag) {
                case DialogPortion._TAG:
                    if (localAis.isTagPrimitive())
                        throw new ParseException(PAbortCauseType.IncorrectTxPortion, null,
                                "Error decoding TC-End: DialogPortion must be constructive");
                    this.dp = TcapFactory.createDialogPortion(localAis);
                    break;

                case _TAG_P:
                    // primitive?
                    this.type = PAbortCauseType.getFromInt((int) localAis.readInteger());
                    break;

                default:
                    throw new ParseException(PAbortCauseType.IncorrectTxPortion, null,
                            "Error decoding TC-Abort: bad tag while parsing DialogPortion and P-AbortCause portion: " + tag);
            }

            if (localAis.available() > 0)
                throw new ParseException(PAbortCauseType.IncorrectTxPortion, null, "Error decoding TC-Abort: too mych data");

        } catch (IOException e) {
            throw new ParseException(PAbortCauseType.BadlyFormattedTxPortion, null, "IOException while decoding TC-Abort: "
                    + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParseException(PAbortCauseType.BadlyFormattedTxPortion, null, "AsnException while decoding TC-Abort: "
                    + e.getMessage(), e);
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#encode(org.mobicents.protocols .asn.AsnOutputStream)
     */
    public void encode(AsnOutputStream aos) throws EncodeException {

        try {
            aos.writeTag(Tag.CLASS_APPLICATION, false, _TAG);
            int pos = aos.StartContentDefiniteLength();

            aos.writeOctetString(Tag.CLASS_APPLICATION, _TAG_DTX, this.destTxId);

            if (this.type != null)
                aos.writeInteger(Tag.CLASS_APPLICATION, _TAG_P, this.type.getType());
            else if (this.dp != null)
                this.dp.encode(aos);

            aos.FinalizeContent(pos);

        } catch (IOException e) {
            throw new EncodeException("IOException while encoding TC-Abort: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new EncodeException("AsnException while encoding TC-Abort: " + e.getMessage(), e);
        }

    }

}
