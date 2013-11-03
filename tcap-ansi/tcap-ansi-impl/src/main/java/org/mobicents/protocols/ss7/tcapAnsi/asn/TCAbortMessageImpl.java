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
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.DialogPortion;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.EncodeException;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ParseException;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.UserInformationElement;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.PAbortCause;
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
    private UserInformationElement userAbortInformation;

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

    @Override
    public UserInformationElement getUserAbortInformation() {
        return userAbortInformation;
    }

    @Override
    public void setUserAbortInformation(UserInformationElement uai) {
        userAbortInformation = uai;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols .asn.AsnInputStream)
     */
    public void decode(AsnInputStream ais) throws ParseException {
        this.destinationTransactionId = null;
        this.dp = null;
        this.pAbortCause = null;
        this.userAbortInformation = null;

        try {
            AsnInputStream localAis = ais.readSequenceStream();

            // transaction portion
            TransactionID tid = TcapFactory.readTransactionID(localAis);
            if (tid.getFirstElem() == null || tid.getSecondElem() != null) {
                throw new ParseException(PAbortCause.BadlyStructuredTransactionPortion,
                        "Error decoding TCAbortMessage: transactionId must contain one and only one transactionId");
            }
            this.destinationTransactionId = tid.getFirstElem();

            if (localAis.available() == 0) {
                throw new ParseException(PAbortCause.UnrecognizedDialoguePortionID,
                        "Error decoding TCAbortMessage: neither P-Abort-cause nor dialog portion or userInformation is found");
            }
            int tag = localAis.readTag();
            if (localAis.getTagClass() != Tag.CLASS_PRIVATE)
                throw new ParseException(PAbortCause.UnrecognizedDialoguePortionID,
                        "Error decoding TCAbortMessage: bad tagClass for P-Abort-cause, userInformation or abortCause, found tagClass=" + localAis.getTagClass());

            switch (tag) {
            case TCAbortMessage._TAG_P_ABORT_CAUSE:
                // P-Abort-cause
                if (!localAis.isTagPrimitive())
                    throw new ParseException(PAbortCause.IncorrectTransactionPortion, "Error decoding TCAbortMessage: P_ABORT_CAUSE is not primitive");
                int i1 = (int)localAis.readInteger();
                this.pAbortCause = PAbortCause.getFromInt(i1);
                break;

            case DialogPortion._TAG_DIALOG_PORTION:
            case TCAbortMessage._TAG_USER_ABORT_INFORMATION:
                // Dialog portion (opt) + UserAbortInformation
                if (tag == DialogPortion._TAG_DIALOG_PORTION) {
                    // Dialog portion
                    this.dp = TcapFactory.createDialogPortion(localAis);
                    if (localAis.available() == 0)
                        return;
                    tag = localAis.readTag();
                }

                // UserAbortInformation
                if (tag != TCAbortMessage._TAG_USER_ABORT_INFORMATION || localAis.getTagClass() != Tag.CLASS_PRIVATE || localAis.isTagPrimitive())
                    throw new ParseException(PAbortCause.IncorrectTransactionPortion,
                            "Error decoding TCAbortMessage: bad tag or tagClass or is primitive for userAbortInformation, found tagClass="
                                    + localAis.getTagClass() + ", tag" + tag);
                UserInformationElementImpl uai = new UserInformationElementImpl();
                AsnInputStream localAis2 = localAis.readSequenceStream();
                if (localAis2.available() > 0) {
                    tag = localAis2.readTag();
                    if (tag != Tag.EXTERNAL || localAis2.getTagClass() != Tag.CLASS_UNIVERSAL || localAis2.isTagPrimitive())
                        throw new ParseException(PAbortCause.IncorrectTransactionPortion,
                                "Error decoding TCAbortMessage: bad tag or tagClass or is primitive for userAbortInformation - External, found tagClass="
                                        + localAis2.getTagClass() + ", tag" + tag);
                    uai.decode(localAis2);
                    if (uai.isOid())
                        this.userAbortInformation = uai;
                }
                break;

            default:
                throw new ParseException(PAbortCause.IncorrectTransactionPortion,
                        "Error decoding TCAbortMessage: bad tag for P-Abort-cause, userInformation or abortCause, found tag=" + tag);
            }

        } catch (IOException e) {
            throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion, "IOException while decoding TCAbortMessage: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion, "AsnException while decoding TCAbortMessage: " + e.getMessage(), e);
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

        if (this.pAbortCause == null && (this.dp == null && this.userAbortInformation == null))
            throw new EncodeException("Error while encoding TCAbortMessage: neither PAbortCause nor DialogPortion/UserAbortInformation is defined");
        if (this.pAbortCause != null && (this.dp != null || this.userAbortInformation != null))
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
                if (this.userAbortInformation != null) {
                    aos.writeTag(Tag.CLASS_PRIVATE, false, TCAbortMessage._TAG_USER_ABORT_INFORMATION);
                    int pos2 = aos.StartContentDefiniteLength();
                    ((UserInformationElementImpl) this.userAbortInformation).encode(aos);
                    aos.FinalizeContent(pos2);
                } else {
                    aos.writeTag(Tag.CLASS_PRIVATE, false, TCAbortMessage._TAG_USER_ABORT_INFORMATION);
                    aos.writeLength(0);
                }
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
        if (this.userAbortInformation != null) {
            sb.append("userAbortInformation=");
            sb.append(this.userAbortInformation);
        }
        sb.append("]");
        return sb.toString();
    }

}
