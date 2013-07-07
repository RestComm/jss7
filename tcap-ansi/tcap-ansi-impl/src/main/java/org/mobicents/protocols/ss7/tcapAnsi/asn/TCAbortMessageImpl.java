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

package org.mobicents.protocols.ss7.tcapAnsi.asn;

import java.io.IOException;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.DialogPortion;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.EncodeException;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ParseException;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.PAbortCause;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.RejectProblem;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.TCAbortMessage;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.TCQueryMessage;

/**
 * @author amit bhayani
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
public class TCAbortMessageImpl implements TCAbortMessage {

    private byte[] destinationTransactionId;
    private DialogPortion dp;
    private PAbortCause pAbortCause;
    // TODO: implement userAbortInformation
//    private External userAbortInformation;


    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.TCBeginMessage#getDialogPortion ()
     */
    public DialogPortion getDialogPortion() {

        return this.dp;
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.mobicents.protocols.ss7.tcap.asn.comp.TCBeginMessage# getOriginatingTransactionId()
     */
    public byte[] getDestinationTransactionId() {

        return this.destinationTransactionId;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.TCBeginMessage#setDialogPortion
     * (org.mobicents.protocols.ss7.tcap.asn.DialogPortion)
     */
    public void setDialogPortion(DialogPortion dp) {
        this.dp = dp;

    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.mobicents.protocols.ss7.tcap.asn.comp.TCBeginMessage# setOriginatingTransactionId(java.lang.String)
     */
    public void setDestinationTransactionId(byte[] t) {
        if (t != null && t.length != 4)
            throw new IllegalArgumentException("TransactionId leng must be 4 bytes, found: " + t.length);
        this.destinationTransactionId = t;
    }

    @Override
    public PAbortCause getPAbortCause() {
        return this.pAbortCause;
    }

    @Override
    public void setPAbortCause(PAbortCause t) {
        this.pAbortCause = t;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols .asn.AsnInputStream)
     */
    public void decode(AsnInputStream ais) throws ParseException {
        this.dp = null;
        this.destinationTransactionId = null;
        this.pAbortCause = null;

        try {
            AsnInputStream localAis = ais.readSequenceStream();

            int tag = localAis.readTag();
            if (tag != TCQueryMessage._TAG_TRANSACTION_ID || localAis.getTagClass() != Tag.CLASS_PRIVATE || !localAis.isTagPrimitive())
                throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion, RejectProblem.transactionBadlyStructuredTransPortion,
                        "Error decoding TCAbortMessage: bad tag or tagClass or not primitive for originatingTransactionId, found tagClass=" + localAis.getTagClass()
                                + ", tag=" + tag);
            this.destinationTransactionId = localAis.readOctetString();
            if (this.destinationTransactionId.length != 4)
                throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion, RejectProblem.transactionBadlyStructuredTransPortion,
                        "Error decoding TCAbortMessage: destinationTransactionId bad length, must be 4, found =" + this.destinationTransactionId.length);

            while (true) {
                if (localAis.available() == 0)
                    break;

                tag = localAis.readTag();
                if (localAis.isTagPrimitive() || localAis.getTagClass() != Tag.CLASS_PRIVATE)
                    throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion, RejectProblem.transactionBadlyStructuredTransPortion,
                            "Error decoding TCAbortMessage: bad tagClass or primitive for dialogPortion or componentPortion, found tagClass="
                                    + localAis.getTagClass());

                switch (tag) {
                case DialogPortion._TAG_DIALOG_PORTION:
                    if (this.dp != null) {
                        throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion, RejectProblem.transactionBadlyStructuredTransPortion,
                                "Error decoding TCAbortMessage: double DialogPortion");
                    }
                    this.dp = TcapFactory.createDialogPortion(localAis);
                    break;

                case TCAbortMessage._TAG_P_ABORT_CAUSE:
                    if (this.pAbortCause != null) {
                        throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion, RejectProblem.transactionBadlyStructuredTransPortion,
                                "Error decoding TCAbortMessage: double PAbortCause");
                    }
                    int i1 = (int) localAis.readInteger();
                    this.pAbortCause = PAbortCause.getFromInt(i1);
                    if (this.pAbortCause == null) {
                        throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion, RejectProblem.transactionBadlyStructuredTransPortion,
                                "Error decoding TCAbortMessage: bad PAbortCause value");
                    }
                    break;

                // TODO: implement userAbortInformation

                default:
                    throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion, RejectProblem.transactionBadlyStructuredTransPortion,
                            "Error decoding TCAbortMessage: bad tag: " + tag);
                }
            }

            if (this.pAbortCause == null && this.dp == null)
                throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion, RejectProblem.transactionBadlyStructuredTransPortion,
                        "Error decoding TCAbortMessage: no parameters has been found");

            if (this.pAbortCause != null && this.dp != null)
                throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion, RejectProblem.transactionBadlyStructuredTransPortion,
                        "Error decoding TCAbortMessage: both pAbortCause and DialogPortion/UserAbortInformation has been found");

        } catch (IOException e) {
            throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion, RejectProblem.transactionBadlyStructuredTransPortion,
                    "IOException while decoding TCAbortMessage: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion, RejectProblem.transactionBadlyStructuredTransPortion,
                    "AsnException while decoding TCAbortMessage: " + e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#encode(org.mobicents.protocols .asn.AsnOutputStream)
     */
    public void encode(AsnOutputStream aos) throws EncodeException {

        if (this.destinationTransactionId == null || this.destinationTransactionId.length != 4)
            throw new EncodeException("Error while encoding TCAbortMessage: destinationTransactionId is not defined or has not a length 4");

        if (this.pAbortCause == null && this.dp == null)
            throw new EncodeException("Error while encoding TCAbortMessage: neither PAbortCause nor DialogPortion/UserAbortInformation is defined");
        if (this.pAbortCause != null && this.dp != null)
            throw new EncodeException("Error while encoding TCAbortMessage: both PAbortCause and DialogPortion/UserAbortInformation is defined");

        try {
            aos.writeTag(Tag.CLASS_PRIVATE, false, TCAbortMessage._TAG_ABORT);
            int pos = aos.StartContentDefiniteLength();

            aos.writeOctetString(Tag.CLASS_PRIVATE, TCQueryMessage._TAG_TRANSACTION_ID, this.destinationTransactionId);

            if(this.pAbortCause!=null){
                aos.writeInteger(Tag.CLASS_PRIVATE, TCAbortMessage._TAG_P_ABORT_CAUSE, this.pAbortCause.getType());
            } else {
                if (this.dp != null)
                    this.dp.encode(aos);
                // TODO: implement userAbortInformation
            }

            aos.FinalizeContent(pos);

        } catch (IOException e) {
            throw new EncodeException("IOException while encoding TCAbortMessage: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new EncodeException("AsnException while encoding TCAbortMessage: " + e.getMessage(), e);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TCAbortMessage [");

        if (this.destinationTransactionId != null) {
            sb.append("destinationTransactionId=[");
            sb.append(Arrays.toString(this.destinationTransactionId));
            sb.append("], ");
        }
        if (this.pAbortCause != null) {
            sb.append("PAbortCause=");
            sb.append(this.pAbortCause);
            sb.append(", ");
        }
        if (this.dp != null) {
            sb.append("DialogPortion=");
            sb.append(this.dp);
            sb.append(", ");
        }
        // TODO: implement userAbortInformation
        sb.append("]");
        return sb.toString();
    }

}
