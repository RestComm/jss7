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

/**
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
public class ResultSourceDiagnosticImpl implements ResultSourceDiagnostic {

    private DialogServiceProviderType providerType;
    private DialogServiceUserType userType;

    /*
     * (non-Javadoc)
     *
     * @seeorg.mobicents.protocols.ss7.tcap.asn.ResultSourceDiagnostic# getDialogServiceProviderType()
     */
    public DialogServiceProviderType getDialogServiceProviderType() {

        return providerType;
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.mobicents.protocols.ss7.tcap.asn.ResultSourceDiagnostic# getDialogServiceUserType()
     */
    public DialogServiceUserType getDialogServiceUserType() {

        return userType;
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.mobicents.protocols.ss7.tcap.asn.ResultSourceDiagnostic# setDialogServiceProviderType
     * (org.mobicents.protocols.ss7.tcap.asn.DialogServiceProviderType)
     */
    public void setDialogServiceProviderType(DialogServiceProviderType t) {
        this.providerType = t;
        this.userType = null;

    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.mobicents.protocols.ss7.tcap.asn.ResultSourceDiagnostic# setDialogServiceUserType
     * (org.mobicents.protocols.ss7.tcap.asn.DialogServiceUserType)
     */
    public void setDialogServiceUserType(DialogServiceUserType t) {
        this.userType = t;
        this.providerType = null;

    }

    public String toString() {
        return "ResultSourceDiagnostic[providerType=" + providerType + ", userType=" + userType + "]";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols .asn.AsnInputStream)
     */
    public void decode(AsnInputStream ais) throws ParseException {
        try {
            AsnInputStream localAis = ais.readSequenceStream();

            // int make read whole thing?
            int tag = localAis.readTag();
            if (localAis.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC)
                throw new ParseException(PAbortCauseType.IncorrectTxPortion, null,
                        "Error while decoding AARE-apdu.result-dource-diagnostic sequence part: bad tag class: tagClass="
                                + localAis.getTagClass());

            switch (tag) {
                case _TAG_U:
                    AsnInputStream localAis2 = localAis.readSequenceStream();
                    tag = localAis2.readTag();
                    if (tag != Tag.INTEGER || localAis2.getTagClass() != Tag.CLASS_UNIVERSAL)
                        throw new ParseException(PAbortCauseType.IncorrectTxPortion, null,
                                "Error while decoding AARE-apdu.result-dource-diagnostic integer part: bad tag or tag class class: tagClass="
                                        + localAis.getTagClass() + ", tag=" + tag);
                    long t = localAis2.readInteger();
                    this.userType = DialogServiceUserType.getFromInt(t);
                    break;

                case _TAG_P:
                    localAis2 = localAis.readSequenceStream();
                    tag = localAis2.readTag();
                    if (tag != Tag.INTEGER || localAis2.getTagClass() != Tag.CLASS_UNIVERSAL)
                        throw new ParseException(PAbortCauseType.IncorrectTxPortion, null,
                                "Error while decoding AARE-apdu.result-dource-diagnostic integer part: bad tag or tag class class: tagClass="
                                        + localAis.getTagClass() + ", tag=" + tag);
                    t = localAis2.readInteger();
                    this.providerType = DialogServiceProviderType.getFromInt(t);
                    break;

                default:
                    throw new ParseException(PAbortCauseType.IncorrectTxPortion, null,
                            "Error while decoding AARE-apdu.result-dource-diagnostic sequence part: bad tag: tag=" + tag);
            }
        } catch (IOException e) {
            throw new ParseException(PAbortCauseType.BadlyFormattedTxPortion, null,
                    "IOException while decoding ResultSourceDiagnostic: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParseException(PAbortCauseType.BadlyFormattedTxPortion, null,
                    "AsnException while decoding ResultSourceDiagnostic: " + e.getMessage(), e);
        }

        // tag can have on of two values =
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#encode(org.mobicents.protocols .asn.AsnOutputStream)
     */
    public void encode(AsnOutputStream aos) throws EncodeException {
        if (this.userType == null && this.providerType == null)
            throw new EncodeException("Error encoding ResultSourceDiagnostic: Value not set");

        try {
            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG);
            int pos = aos.StartContentDefiniteLength();

            if (this.userType != null) {
                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_U);
                int pos2 = aos.StartContentDefiniteLength();
                aos.writeInteger(this.userType.getType());
                aos.FinalizeContent(pos2);
            } else {
                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_P);
                int pos2 = aos.StartContentDefiniteLength();
                aos.writeInteger(this.providerType.getType());
                aos.FinalizeContent(pos2);
            }

            aos.FinalizeContent(pos);

        } catch (IOException e) {
            throw new EncodeException("IOException while encoding ResultSourceDiagnostic: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new EncodeException("AsnException while encoding ResultSourceDiagnostic: " + e.getMessage(), e);
        }

    }

}
