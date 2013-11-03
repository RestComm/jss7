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
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Component;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.PAbortCause;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.TCConversationMessage;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.TCQueryMessage;

/**
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
public class TCConversationMessageImpl implements TCConversationMessage {

    private boolean dialogTermitationPermission;
    private byte[] originatingTransactionId;
    private byte[] destinationTransactionId;
    private DialogPortion dp;
    private Component[] component;

    @Override
    public boolean getDialogTermitationPermission() {
        return dialogTermitationPermission;
    }

    @Override
    public void setDialogTermitationPermission(boolean perm) {
        dialogTermitationPermission = perm;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.TCBeginMessage#getComponent()
     */
    public Component[] getComponent() {

        return this.component;
    }

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
    public byte[] getOriginatingTransactionId() {

        return this.originatingTransactionId;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.TCBeginMessage#setComponent
     * (org.mobicents.protocols.ss7.tcap.asn.comp.Component[])
     */
    public void setComponent(Component[] c) {
        this.component = c;

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
    public void setOriginatingTransactionId(byte[] t) {
        if (t != null && t.length != 4)
            throw new IllegalArgumentException("TransactionId leng must be 4 bytes, found: " + t.length);
        this.originatingTransactionId = t;

    }

    @Override
    public byte[] getDestinationTransactionId() {
        return destinationTransactionId;
    }

    @Override
    public void setDestinationTransactionId(byte[] t) {
        if (t != null && t.length != 4)
            throw new IllegalArgumentException("TransactionId leng must be 4 bytes, found: " + t.length);
        destinationTransactionId = t;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols .asn.AsnInputStream)
     */
    public void decode(AsnInputStream ais) throws ParseException {
        this.dialogTermitationPermission = false;
        this.originatingTransactionId = null;
        this.destinationTransactionId = null;
        this.dp = null;
        this.component = null;

        try {
            if (ais.getTag() == TCConversationMessage._TAG_CONVERSATION_WITH_PERM)
                dialogTermitationPermission = true;
            else
                dialogTermitationPermission = false;

            AsnInputStream localAis = ais.readSequenceStream();

            // transaction portion
            TransactionID tid = TcapFactory.readTransactionID(localAis);
            if (tid.getFirstElem() == null || tid.getSecondElem() == null) {
                throw new ParseException(PAbortCause.BadlyStructuredTransactionPortion,
                        "Error decoding TCConversationMessage: transactionId must contain both origination and resination transactionId");
            }
            this.originatingTransactionId = tid.getFirstElem();
            this.destinationTransactionId = tid.getSecondElem();

            // dialog portion
            if (localAis.available() == 0) {
                throw new ParseException(PAbortCause.UnrecognizedDialoguePortionID,
                        "Error decoding TCConversationMessage: neither dialog no component portion is found");
            }
            int tag = localAis.readTag();
            if (tag == DialogPortion._TAG_DIALOG_PORTION) {
                this.dp = TcapFactory.createDialogPortion(localAis);
                if (localAis.available() == 0)
                    return;
                tag = localAis.readTag();
            }

            // component portion
            this.component = TcapFactory.readComponents(localAis);

        } catch (IOException e) {
            throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion, "IOException while decoding TCConversationMessage: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion, "AsnException while decoding TCConversationMessage: " + e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#encode(org.mobicents.protocols .asn.AsnOutputStream)
     */
    public void encode(AsnOutputStream aos) throws EncodeException {

        if (this.originatingTransactionId == null || this.originatingTransactionId.length != 4)
            throw new EncodeException("Error while encoding TCConversationMessage: originatingTransactionId is not defined or has not a length 4");
        if (this.destinationTransactionId == null || this.destinationTransactionId.length != 4)
            throw new EncodeException("Error while encoding TCConversationMessage: destinationTransactionId is not defined or has not a length 4");

        // TODO: should we send a Component without both components and dialogPortion ?
        if ((this.component == null || this.component.length == 0) && this.dp == null)
            throw new EncodeException("Error while encoding TCConversationMessage: neither dialogPortion nor componentPortion is defined");

        try {
            if (this.dialogTermitationPermission)
                aos.writeTag(Tag.CLASS_PRIVATE, false, TCConversationMessage._TAG_CONVERSATION_WITH_PERM);
            else
                aos.writeTag(Tag.CLASS_PRIVATE, false, TCConversationMessage._TAG_CONVERSATION_WITHOUT_PERM);
            int pos = aos.StartContentDefiniteLength();

            byte[] buf = new byte[8];
            System.arraycopy(this.originatingTransactionId, 0, buf, 0, 4);
            System.arraycopy(this.destinationTransactionId, 0, buf, 4, 4);
            aos.writeOctetString(Tag.CLASS_PRIVATE, TCQueryMessage._TAG_TRANSACTION_ID, buf);

            if (this.dp != null)
                this.dp.encode(aos);

            if (component != null && component.length > 0) {
                aos.writeTag(Tag.CLASS_PRIVATE, false, TCQueryMessage._TAG_COMPONENT_SEQUENCE);
                int pos2 = aos.StartContentDefiniteLength();
                for (Component c : this.component) {
                    c.encode(aos);
                }
                aos.FinalizeContent(pos2);
            }

            aos.FinalizeContent(pos);

        } catch (IOException e) {
            throw new EncodeException("IOException while encoding TCConversationMessage: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new EncodeException("AsnException while encoding TCConversationMessage: " + e.getMessage(), e);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TCConversationMessage [");

        sb.append("dialogTermitationPermission=");
        sb.append(this.dialogTermitationPermission);
        sb.append(", ");

        if (this.originatingTransactionId != null) {
            sb.append("originatingTransactionId=[");
            sb.append(Arrays.toString(this.originatingTransactionId));
            sb.append("], ");
        }
        if (this.destinationTransactionId != null) {
            sb.append("destinationTransactionId=[");
            sb.append(Arrays.toString(this.destinationTransactionId));
            sb.append("], ");
        }
        if (this.dp != null) {
            sb.append("DialogPortion=");
            sb.append(this.dp);
            sb.append(", ");
        }
        if (this.component != null && this.component.length > 0) {
            sb.append("Components=[");
            int i1 = 0;
            for (Component comp : this.component) {
                if (i1 == 0)
                    i1 = 1;
                else
                    sb.append(", ");
                sb.append(comp.toString());
            }
            sb.append("], ");
        }
        sb.append("]");
        return sb.toString();
    }
}
