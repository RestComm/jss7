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

package org.mobicents.protocols.ss7.map.service.mobility.authentication;

import java.io.IOException;
import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationQuintuplet;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.QuintupletList;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class QuintupletListImpl implements QuintupletList, MAPAsnPrimitive {

    public static final String _PrimitiveName = "QuintupletList";

    private ArrayList<AuthenticationQuintuplet> quintupletList;

    public QuintupletListImpl() {
    }

    public QuintupletListImpl(ArrayList<AuthenticationQuintuplet> quintupletList) {
        this.quintupletList = quintupletList;
    }

    public ArrayList<AuthenticationQuintuplet> getAuthenticationQuintuplets() {
        return quintupletList;
    }

    public int getTag() throws MAPException {
        return Tag.SEQUENCE;
    }

    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    public boolean getIsPrimitive() {
        return false;
    }

    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {

        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.quintupletList = new ArrayList<AuthenticationQuintuplet>();

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();
            if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {

                switch (tag) {
                    case Tag.SEQUENCE:
                        // authenticationTriplet
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter AuthenticationQuintuplet is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        AuthenticationQuintupletImpl at = new AuthenticationQuintupletImpl();
                        at.decodeAll(ais);
                        this.quintupletList.add(at);
                        break;
                }
            } else {

                ais.advanceElement();
            }
        }

        if (this.quintupletList.size() < 1 || this.quintupletList.size() > 5) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": quintupletList size must be from 1 to 5, found:" + this.quintupletList.size(),
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void encodeAll(AsnOutputStream asnOs) throws MAPException {

        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {

        try {
            asnOs.writeTag(tagClass, false, tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.quintupletList == null || this.quintupletList.size() < 1 || this.quintupletList.size() > 5) {
            throw new MAPException("QuintupletList list must contains from 1 to 5 elemets");
        }

        for (AuthenticationQuintuplet at : this.quintupletList) {
            ((AuthenticationQuintupletImpl) at).encodeAll(asnOs);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("QuintupletList [");

        if (this.quintupletList != null) {
            for (AuthenticationQuintuplet at : this.quintupletList) {
                if (at != null) {
                    sb.append(at.toString());
                    sb.append(", ");
                }
            }
        }

        sb.append("]");

        return sb.toString();
    }
}
