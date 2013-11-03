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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSMSClass;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.MSNetworkCapability;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.MSRadioAccessCapability;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 * @author abhayani
 * @author sergey vetyutnev
 *
 */
public class GPRSMSClassImpl implements GPRSMSClass, MAPAsnPrimitive {

    public static final String _PrimitiveName = "GPRSMSClass";

    private static final int _ID_mSNetworkCapability = 0;
    private static final int _ID_mSRadioAccessCapability = 1;

    private MSNetworkCapability mSNetworkCapability;
    private MSRadioAccessCapability mSRadioAccessCapability;

    /**
     *
     */
    public GPRSMSClassImpl() {
    }

    public GPRSMSClassImpl(MSNetworkCapability mSNetworkCapability, MSRadioAccessCapability mSRadioAccessCapability) {
        this.mSNetworkCapability = mSNetworkCapability;
        this.mSRadioAccessCapability = mSRadioAccessCapability;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getTag()
     */
    public int getTag() throws MAPException {
        return Tag.SEQUENCE;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getTagClass()
     */
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getIsPrimitive ()
     */
    public boolean getIsPrimitive() {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#decodeAll( org.mobicents.protocols.asn.AsnInputStream)
     */
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

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#decodeData (org.mobicents.protocols.asn.AsnInputStream,
     * int)
     */
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
        this.mSNetworkCapability = null;
        this.mSRadioAccessCapability = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {

                int tag = ais.readTag();
                switch (tag) {
                    case _ID_mSNetworkCapability:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " mSNetworkCapability: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.mSNetworkCapability = new MSNetworkCapabilityImpl();
                        ((MSNetworkCapabilityImpl) this.mSNetworkCapability).decodeAll(ais);
                        break;
                    case _ID_mSRadioAccessCapability:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " mSRadioAccessCapability: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.mSRadioAccessCapability = new MSRadioAccessCapabilityImpl();
                        ((MSRadioAccessCapabilityImpl) this.mSRadioAccessCapability).decodeAll(ais);
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }

        if (this.mSNetworkCapability == null)
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": mSNetworkCapability must not be null", MAPParsingComponentExceptionReason.MistypedParameter);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeAll( org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeAll( org.mobicents.protocols.asn.AsnOutputStream,
     * int, int)
     */
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

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeData (org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.mSNetworkCapability == null)
            throw new MAPException("MSNetworkCapability cannot be null");

        ((MSNetworkCapabilityImpl) this.mSNetworkCapability).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                _ID_mSNetworkCapability);

        if (this.mSRadioAccessCapability != null)
            ((MSRadioAccessCapabilityImpl) this.mSRadioAccessCapability).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    _ID_mSRadioAccessCapability);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.GPRSMSClass #getMSNetworkCapability()
     */
    public MSNetworkCapability getMSNetworkCapability() {
        return this.mSNetworkCapability;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.GPRSMSClass #getMSRadioAccessCapability()
     */
    public MSRadioAccessCapability getMSRadioAccessCapability() {
        return this.mSRadioAccessCapability;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.mSNetworkCapability != null) {
            sb.append("mSNetworkCapability=");
            sb.append(this.mSNetworkCapability);
        }

        if (this.mSRadioAccessCapability != null) {
            sb.append(", mSRadioAccessCapability=");
            sb.append(this.mSRadioAccessCapability);
        }

        sb.append("]");
        return sb.toString();
    }

}
