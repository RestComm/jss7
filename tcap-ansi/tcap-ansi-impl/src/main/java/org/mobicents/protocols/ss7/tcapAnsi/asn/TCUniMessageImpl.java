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
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.DialogPortion;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.EncodeException;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ParseException;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Component;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.PAbortCause;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.TCQueryMessage;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.TCUniMessage;

/**
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
public class TCUniMessageImpl implements TCUniMessage {

    private DialogPortion dp;
    private Component[] component;

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.TCUniMessage#getComponent()
     */
    public Component[] getComponent() {

        return component;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.TCUniMessage#getDialogPortion()
     */
    public DialogPortion getDialogPortion() {

        return dp;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.TCUniMessage#setComponent(org
     * .mobicents.protocols.ss7.tcap.asn.comp.Component[])
     */
    public void setComponent(Component[] c) {
        this.component = c;

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.TCUniMessage#setDialogPortion
     * (org.mobicents.protocols.ss7.tcap.asn.DialogPortion)
     */
    public void setDialogPortion(DialogPortion dp) {
        this.dp = dp;

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols .asn.AsnInputStream)
     */
    public void decode(AsnInputStream ais) throws ParseException {
        this.dp = null;
        this.component = null;

        try {
            AsnInputStream localAis = ais.readSequenceStream();

            // transaction portion
            TransactionID tid = TcapFactory.readTransactionID(localAis);
            if (tid.getFirstElem() != null || tid.getSecondElem() != null) {
                throw new ParseException(PAbortCause.BadlyStructuredTransactionPortion,
                        "Error decoding TCUniMessage: transactionId must contain no data");
            }

            // dialog portion
            if (localAis.available() == 0) {
                throw new ParseException(PAbortCause.UnrecognizedDialoguePortionID,
                        "Error decoding TCUniMessage: neither dialog no component portion is found");
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
            if (this.component == null) {
                throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion, "Error decoding TCUniMessage: neither dialog no component portion is found");
            }

        } catch (IOException e) {
            throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion, "IOException while decoding TCUniMessage: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion, "AsnException while decoding TCUniMessage: " + e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#encode(org.mobicents.protocols .asn.AsnOutputStream)
     */
    public void encode(AsnOutputStream aos) throws EncodeException {

        if (this.component == null || this.component.length == 0)
            throw new EncodeException("Error while encoding TCUniMessage: componentPortion is not defined");

        try {
            aos.writeTag(Tag.CLASS_PRIVATE, false, TCUniMessage._TAG_UNI);
            int pos = aos.StartContentDefiniteLength();

            aos.writeOctetString(Tag.CLASS_PRIVATE, TCQueryMessage._TAG_TRANSACTION_ID, new byte[0]);

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
            throw new EncodeException("IOException while encoding TCUniMessage: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new EncodeException("AsnException while encoding TCUniMessage: " + e.getMessage(), e);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TCUniMessage [");

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

