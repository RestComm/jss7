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
public class DialogAbortAPDUImpl implements DialogAbortAPDU {

    private AbortSource abortSource;
    private UserInformation userInformation;

    /**
     * @return the abortSource
     */
    public AbortSource getAbortSource() {
        return abortSource;
    }

    /**
     * @param abortSource the abortSource to set
     */
    public void setAbortSource(AbortSource abortSource) {
        this.abortSource = abortSource;
    }

    /**
     * @return the userInformation
     */
    public UserInformation getUserInformation() {
        return userInformation;
    }

    /**
     * @param userInformation the userInformation to set
     */
    public void setUserInformation(UserInformation userInformation) {
        this.userInformation = userInformation;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.DialogAPDU#getType()
     */
    public DialogAPDUType getType() {
        return DialogAPDUType.Abort;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.DialogAPDU#isUniDirectional()
     */
    public boolean isUniDirectional() {

        return false;
    }

    public String toString() {
        return "DialogAbortAPDU[abortSource=" + abortSource + ", userInformation=" + userInformation + "]";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols.asn.AsnInputStream)
     */
    public void decode(AsnInputStream ais) throws ParseException {
        try {
            AsnInputStream localAis = ais.readSequenceStream();

            int tag = localAis.readTag();
            if (tag != AbortSource._TAG || localAis.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC)
                throw new ParseException(PAbortCauseType.IncorrectTxPortion, null,
                        "Error decoding DialogAbortAPDU.abort-source: bad tag or tagClass, found tag=" + tag + ", tagClass="
                                + localAis.getTagClass());
            this.abortSource = TcapFactory.createAbortSource(localAis);

            // optional sequence.
            if (localAis.available() == 0)
                return;

            tag = localAis.readTag();
            if (tag != UserInformation._TAG || localAis.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC)
                throw new ParseException(PAbortCauseType.IncorrectTxPortion, null,
                        "Error decoding DialogAbortAPDU.user-information: bad tag or tagClass, found tag=" + tag
                                + ", tagClass=" + localAis.getTagClass());
            this.userInformation = TcapFactory.createUserInformation(localAis);

        } catch (IOException e) {
            throw new ParseException(PAbortCauseType.BadlyFormattedTxPortion, null,
                    "IOException while decoding DialogAbortAPDU: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParseException(PAbortCauseType.BadlyFormattedTxPortion, null,
                    "AsnException while decoding DialogAbortAPDU: " + e.getMessage(), e);
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#encode(org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encode(AsnOutputStream aos) throws EncodeException {

        if (abortSource == null)
            throw new EncodeException("Error encoding DialogAbortAPDU: Abort Source Name must not be null");

        try {

            aos.writeTag(Tag.CLASS_APPLICATION, false, _TAG_ABORT);
            int pos = aos.StartContentDefiniteLength();

            this.abortSource.encode(aos);

            if (this.userInformation != null)
                this.userInformation.encode(aos);

            aos.FinalizeContent(pos);

        } catch (AsnException e) {
            throw new EncodeException("AsnException while encoding DialogAbortAPDU: " + e.getMessage(), e);
        }
    }

}
