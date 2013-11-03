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
import org.mobicents.protocols.ss7.map.api.errors.AbsentSubscriberReason;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorCode;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageAbsentSubscriber;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MAPErrorMessageAbsentSubscriberImpl extends MAPErrorMessageImpl implements MAPErrorMessageAbsentSubscriber {

    public static final int AbsentSubscriberReason_TAG = 0;

    private MAPExtensionContainer extensionContainer;
    private AbsentSubscriberReason absentSubscriberReason;

    private Boolean mwdSet;

    /**
     * For MAP V2-3
     *
     * @param extensionContainer
     * @param absentSubscriberReason
     */
    public MAPErrorMessageAbsentSubscriberImpl(MAPExtensionContainer extensionContainer,
            AbsentSubscriberReason absentSubscriberReason) {
        super((long) MAPErrorCode.absentSubscriber);

        this.extensionContainer = extensionContainer;
        this.absentSubscriberReason = absentSubscriberReason;
    }

    /**
     * For MAP V1
     *
     * @param mwdSet
     */
    public MAPErrorMessageAbsentSubscriberImpl(Boolean mwdSet) {
        super((long) MAPErrorCode.absentSubscriber);

        this.mwdSet = mwdSet;
    }

    protected MAPErrorMessageAbsentSubscriberImpl() {
        super((long) MAPErrorCode.absentSubscriber);
    }

    public boolean isEmAbsentSubscriber() {
        return true;
    }

    public MAPErrorMessageAbsentSubscriber getEmAbsentSubscriber() {
        return this;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public AbsentSubscriberReason getAbsentSubscriberReason() {
        return this.absentSubscriberReason;
    }

    public void setExtensionContainer(MAPExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    public void setAbsentSubscriberReason(AbsentSubscriberReason absentSubscriberReason) {
        this.absentSubscriberReason = absentSubscriberReason;
    }

    @Override
    public Boolean getMwdSet() {
        return mwdSet;
    }

    @Override
    public void setMwdSet(Boolean val) {
        mwdSet = val;
    }

    public int getTag() throws MAPException {
        if (this.mwdSet != null)
            return Tag.BOOLEAN;
        else
            return Tag.SEQUENCE;
    }

    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    public boolean getIsPrimitive() {
        if (this.mwdSet != null)
            return true;
        else
            return false;
    }

    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {

        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding MAPErrorMessageAbsentSubscriber: "
                    + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding MAPErrorMessageAbsentSubscriber: "
                    + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding MAPErrorMessageAbsentSubscriber: "
                    + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding MAPErrorMessageAbsentSubscriber: "
                    + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream localAis, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.extensionContainer = null;
        this.absentSubscriberReason = null;
        this.mwdSet = null;

        switch (localAis.getTag()) {
            case Tag.SEQUENCE:
                if (localAis.getTagClass() != Tag.CLASS_UNIVERSAL || localAis.isTagPrimitive())
                    throw new MAPParsingComponentException(
                            "Error decoding MAPErrorMessageAbsentSubscriber: bad tag class or parameter is primitive",
                            MAPParsingComponentExceptionReason.MistypedParameter);

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
                                case AbsentSubscriberReason_TAG:
                                    int code = (int) ais.readInteger();
                                    this.absentSubscriberReason = AbsentSubscriberReason.getInstance(code);
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
                break;

            case Tag.BOOLEAN:
                if (localAis.getTagClass() != Tag.CLASS_UNIVERSAL || !localAis.isTagPrimitive())
                    throw new MAPParsingComponentException(
                            "Error decoding MAPErrorMessageAbsentSubscriber: bad tag class or parameter is not primitive",
                            MAPParsingComponentExceptionReason.MistypedParameter);

                this.mwdSet = localAis.readBooleanData(length);
                break;

            default:
                throw new MAPParsingComponentException("Error decoding MAPErrorMessageAbsentSubscriber: bad tag",
                        MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void encodeAll(AsnOutputStream asnOs) throws MAPException {

        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {

        try {
            asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding MAPErrorMessageAbsentSubscriber: " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream aos) throws MAPException {

        if (this.mwdSet != null) {
            try {
                aos.writeBooleanData(this.mwdSet);
            } catch (IOException e) {
                throw new MAPException("IOException when encoding MAPErrorMessageAbsentSubscriber: " + e.getMessage(), e);
            }
        } else {
            if (this.absentSubscriberReason == null && this.extensionContainer == null)
                return;

            try {
                if (this.extensionContainer != null)
                    ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(aos);
                if (this.absentSubscriberReason != null)
                    aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, AbsentSubscriberReason_TAG,
                            this.absentSubscriberReason.getCode());

            } catch (IOException e) {
                throw new MAPException("IOException when encoding MAPErrorMessageAbsentSubscriber: " + e.getMessage(), e);
            } catch (AsnException e) {
                throw new MAPException("AsnException when encoding MAPErrorMessageAbsentSubscriber: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("MAPErrorMessageAbsentSubscriber [");
        if (this.mwdSet != null)
            sb.append("mwdSet=" + this.mwdSet.toString());
        if (this.extensionContainer != null)
            sb.append(", extensionContainer=" + this.extensionContainer.toString());
        if (this.absentSubscriberReason != null)
            sb.append(", absentSubscriberReason=" + this.absentSubscriberReason.toString());
        sb.append("]");

        return sb.toString();
    }

}
