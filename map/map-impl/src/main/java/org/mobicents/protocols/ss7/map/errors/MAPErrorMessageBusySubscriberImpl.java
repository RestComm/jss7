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

package org.mobicents.protocols.ss7.map.errors;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorCode;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageBusySubscriber;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MAPErrorMessageBusySubscriberImpl extends MAPErrorMessageImpl implements MAPErrorMessageBusySubscriber {

    public static final int _tag_ccbs_Possible = 0;
    public static final int _tag_ccbs_Busy = 1;

    private MAPExtensionContainer extensionContainer;
    private boolean ccbsPossible;
    private boolean ccbsBusy;

    protected String _PrimitiveName = "MAPErrorMessageBusySubscriber";

    public MAPErrorMessageBusySubscriberImpl(MAPExtensionContainer extensionContainer, boolean ccbsPossible, boolean ccbsBusy) {
        super((long) MAPErrorCode.busySubscriber);

        this.extensionContainer = extensionContainer;
        this.ccbsPossible = ccbsPossible;
        this.ccbsBusy = ccbsBusy;
    }

    public MAPErrorMessageBusySubscriberImpl() {
        super((long) MAPErrorCode.busySubscriber);
    }

    public boolean isEmBusySubscriber() {
        return true;
    }

    public MAPErrorMessageBusySubscriber getEmBusySubscriber() {
        return this;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    public boolean getCcbsPossible() {
        return ccbsPossible;
    }

    @Override
    public boolean getCcbsBusy() {
        return ccbsBusy;
    }

    @Override
    public void setExtensionContainer(MAPExtensionContainer val) {
        this.extensionContainer = val;
    }

    @Override
    public void setCcbsPossible(boolean val) {
        this.ccbsPossible = val;
    }

    @Override
    public void setCcbsBusy(boolean val) {
        this.ccbsBusy = val;
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

    @Override
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

    @Override
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

    private void _decode(AsnInputStream localAis, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.extensionContainer = null;
        this.ccbsPossible = false;
        this.ccbsBusy = false;

        if (localAis.getTagClass() != Tag.CLASS_UNIVERSAL || localAis.getTag() != Tag.SEQUENCE || localAis.isTagPrimitive())
            throw new MAPParsingComponentException("Error decoding " + _PrimitiveName
                    + ": bad tag class or tag or parameter is primitive", MAPParsingComponentExceptionReason.MistypedParameter);

        AsnInputStream ais = localAis.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_UNIVERSAL:
                    switch (tag) {
                        case Tag.SEQUENCE:
                            this.extensionContainer = new MAPExtensionContainerImpl();
                            ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                            break;

                        default:
                            ais.advanceElement();
                            break;
                    }
                    break;

                case Tag.CLASS_CONTEXT_SPECIFIC:
                    switch (tag) {
                        case _tag_ccbs_Possible:
                            ais.readNull();
                            this.ccbsPossible = true;
                            break;
                        case _tag_ccbs_Busy:
                            ais.readNull();
                            this.ccbsBusy = true;
                            break;

                        default:
                            ais.advanceElement();
                            break;
                    }
                    break;

                default:
                    ais.advanceElement();
                    break;
            }
        }
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws MAPException {

        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {

        try {
            asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.ccbsPossible == false && this.ccbsBusy == false && this.extensionContainer == null)
            return;

        try {
            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs);
            if (this.ccbsPossible)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _tag_ccbs_Possible);
            if (this.ccbsBusy)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _tag_ccbs_Busy);

        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.extensionContainer != null)
            sb.append("extensionContainer=" + this.extensionContainer.toString());
        if (this.ccbsPossible)
            sb.append(", ccbsPossible");
        if (this.ccbsBusy)
            sb.append(", ccbsBusy");
        sb.append("]");

        return sb.toString();
    }

}
