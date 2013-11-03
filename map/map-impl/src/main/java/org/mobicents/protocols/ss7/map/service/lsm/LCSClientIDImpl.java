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

package org.mobicents.protocols.ss7.map.service.lsm;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientExternalID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientInternalID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientName;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSRequestorID;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.APN;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.APNImpl;

/**
 * @author amit bhayani
 *
 */
public class LCSClientIDImpl implements LCSClientID, MAPAsnPrimitive {
    private static final int _TAG_LCS_CLIENT_TYPE = 0;
    private static final int _TAG_LCS_CLIENT_EXTERNAL_ID = 1;
    private static final int _TAG_LCS_CLIENT_DIALED_BY_MS = 2;
    private static final int _TAG_LCS_CLIENT_INTERNAL_ID = 3;
    private static final int _TAG_LCS_CLIENT_NAME = 4;
    private static final int _TAG_LCS_APN = 5;
    private static final int _TAG_LCS_REQUESTOR_ID = 6;

    public static final String _PrimitiveName = "LCSClientID";

    private LCSClientType lcsClientType;
    private LCSClientExternalID lcsClientExternalID;
    private LCSClientInternalID lcsClientInternalID;
    private LCSClientName lcsClientName;
    private AddressString lcsClientDialedByMS;
    private APN lcsAPN;
    private LCSRequestorID lcsRequestorID;

    /**
     * @param lcsClientType
     * @param lcsClientExternalID
     * @param lcsClientInternalID
     * @param lcsClientName
     * @param lcsClientDialedByMS
     * @param lcsAPN
     * @param lcsRequestorID
     */
    public LCSClientIDImpl(LCSClientType lcsClientType, LCSClientExternalID lcsClientExternalID,
            LCSClientInternalID lcsClientInternalID, LCSClientName lcsClientName, AddressString lcsClientDialedByMS,
            APN lcsAPN, LCSRequestorID lcsRequestorID) {
        super();
        this.lcsClientType = lcsClientType;
        this.lcsClientExternalID = lcsClientExternalID;
        this.lcsClientInternalID = lcsClientInternalID;
        this.lcsClientName = lcsClientName;
        this.lcsClientDialedByMS = lcsClientDialedByMS;
        this.lcsAPN = lcsAPN;
        this.lcsRequestorID = lcsRequestorID;
    }

    /**
     *
     */
    public LCSClientIDImpl() {
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID#getLCSClientType ()
     */
    public LCSClientType getLCSClientType() {
        return this.lcsClientType;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID# getLCSClientExternalID()
     */
    public LCSClientExternalID getLCSClientExternalID() {
        return this.lcsClientExternalID;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID# getLCSClientDialedByMS()
     */
    public AddressString getLCSClientDialedByMS() {
        return this.lcsClientDialedByMS;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID# getLCSClientInternalID()
     */
    public LCSClientInternalID getLCSClientInternalID() {
        return this.lcsClientInternalID;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID#getLCSClientName ()
     */
    public LCSClientName getLCSClientName() {
        return this.lcsClientName;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID#getLCSAPN()
     */
    public APN getLCSAPN() {
        return this.lcsAPN;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID#getLCSRequestorID ()
     */
    public LCSRequestorID getLCSRequestorID() {
        return this.lcsRequestorID;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTag()
     */
    public int getTag() throws MAPException {
        return Tag.SEQUENCE;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTagClass ()
     */
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getIsPrimitive ()
     */
    public boolean getIsPrimitive() {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#decodeAll
     * (org.mobicents.protocols.asn.AsnInputStream)
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
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#decodeData
     * (org.mobicents.protocols.asn.AsnInputStream, int)
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

    private void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.lcsClientType = null;
        this.lcsClientExternalID = null;
        this.lcsClientInternalID = null;
        this.lcsClientName = null;
        this.lcsClientDialedByMS = null;
        this.lcsAPN = null;
        this.lcsRequestorID = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        int tag = ais.readTag();
        // Decode mandatory lcsClientType [0] LCSClientType,
        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive() || tag != _TAG_LCS_CLIENT_TYPE) {
            throw new MAPParsingComponentException(
                    "Error while decoding LCSClientID: Parameter 0[lcsClientType [0] LCSClientType] bad tag class, tag or not constructed",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
        int lcsCltType = (int) ais.readInteger();
        this.lcsClientType = LCSClientType.getLCSClientType(lcsCltType);

        while (true) {
            if (ais.available() == 0)
                break;

            tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _TAG_LCS_CLIENT_EXTERNAL_ID:
                        // Optional lcsClientExternalID [1] LCSClientExternalID
                        // OPTIONAL,
                        if (ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Error while decoding LCSClientExternalID: not constructed",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        this.lcsClientExternalID = new LCSClientExternalIDImpl();
                        ((LCSClientExternalIDImpl) this.lcsClientExternalID).decodeAll(ais);
                        break;
                    case _TAG_LCS_CLIENT_DIALED_BY_MS:
                        // lcsClientDialedByMS [2] AddressString OPTIONAL,
                        if (!ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Error while decoding lcsClientDialedByMS: not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        this.lcsClientDialedByMS = new AddressStringImpl();
                        ((AddressStringImpl) this.lcsClientDialedByMS).decodeAll(ais);

                        break;
                    case _TAG_LCS_CLIENT_INTERNAL_ID:
                        // lcsClientInternalID [3] LCSClientInternalID OPTIONAL
                        if (!ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Error while decoding lcsClientInternalID: not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        int i1 = (int) ais.readInteger();
                        this.lcsClientInternalID = LCSClientInternalID.getLCSClientInternalID(i1);
                        break;
                    case _TAG_LCS_CLIENT_NAME:
                        // lcsClientName [4] LCSClientName OPTIONAL,
                        if (ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Error while decoding lcsClientName: not constructed",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        this.lcsClientName = new LCSClientNameImpl();
                        ((LCSClientNameImpl) this.lcsClientName).decodeAll(ais);
                        break;
                    case _TAG_LCS_APN:
                        // lcsAPN [5] APN OPTIONAL,
                        if (!ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Error while decoding lcsAPN: not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        this.lcsAPN = new APNImpl();
                        ((APNImpl) this.lcsAPN).decodeAll(ais);
                        break;
                    case _TAG_LCS_REQUESTOR_ID:
                        // lcsRequestorID [6] LCSRequestorID OPTIONAL
                        if (ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Error while decoding lcsRequestorID: not constructed",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        this.lcsRequestorID = new LCSRequestorIDImpl();
                        ((LCSRequestorIDImpl) this.lcsRequestorID).decodeAll(ais);
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }// while
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeAll
     * (org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeAll
     * (org.mobicents.protocols.asn.AsnOutputStream, int, int)
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
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeData
     * (org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.lcsClientType == null)
            throw new MAPException("lcsClientType must not be null");

        try {
            asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_CLIENT_TYPE, this.lcsClientType.getType());
        } catch (IOException e) {
            throw new MAPException("IOException when encoding parameter lcsClientType: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter lcsClientType: ", e);
        }

        if (this.lcsClientExternalID != null) {
            // Encode lcsClientExternalID [1] LCSClientExternalID OPTIONAL,
            ((LCSClientExternalIDImpl) this.lcsClientExternalID).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    _TAG_LCS_CLIENT_EXTERNAL_ID);
        }

        if (this.lcsClientDialedByMS != null) {
            // lcsClientDialedByMS [2] AddressString OPTIONAL,
            ((AddressStringImpl) this.lcsClientDialedByMS).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    _TAG_LCS_CLIENT_DIALED_BY_MS);
        }

        if (this.lcsClientInternalID != null) {
            // lcsClientInternalID [3] LCSClientInternalID OPTIONAL,
            try {
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_CLIENT_INTERNAL_ID, this.lcsClientInternalID.getId());
            } catch (IOException e) {
                throw new MAPException("IOException when encoding parameter lcsClientInternalID: ", e);
            } catch (AsnException e) {
                throw new MAPException("AsnException when encoding parameter lcsClientInternalID: ", e);
            }
        }

        if (this.lcsClientName != null) {
            // lcsClientName [4] LCSClientName
            ((LCSClientNameImpl) this.lcsClientName).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_CLIENT_NAME);
        }

        if (this.lcsAPN != null) {
            // lcsAPN [5] APN OPTIONAL,
            ((APNImpl) this.lcsAPN).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_APN);
        }

        if (this.lcsRequestorID != null) {
            // lcsRequestorID [6] LCSRequestorID
            ((LCSRequestorIDImpl) this.lcsRequestorID).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_REQUESTOR_ID);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((lcsAPN == null) ? 0 : lcsAPN.hashCode());
        result = prime * result + ((lcsClientDialedByMS == null) ? 0 : lcsClientDialedByMS.hashCode());
        result = prime * result + ((lcsClientExternalID == null) ? 0 : lcsClientExternalID.hashCode());
        result = prime * result + ((lcsClientInternalID == null) ? 0 : lcsClientInternalID.hashCode());
        result = prime * result + ((lcsClientName == null) ? 0 : lcsClientName.hashCode());
        result = prime * result + ((lcsClientType == null) ? 0 : lcsClientType.hashCode());
        result = prime * result + ((lcsRequestorID == null) ? 0 : lcsRequestorID.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LCSClientIDImpl other = (LCSClientIDImpl) obj;
        if (lcsAPN == null) {
            if (other.lcsAPN != null)
                return false;
        } else if (!lcsAPN.equals(other.lcsAPN))
            return false;
        if (lcsClientDialedByMS == null) {
            if (other.lcsClientDialedByMS != null)
                return false;
        } else if (!lcsClientDialedByMS.equals(other.lcsClientDialedByMS))
            return false;
        if (lcsClientExternalID == null) {
            if (other.lcsClientExternalID != null)
                return false;
        } else if (!lcsClientExternalID.equals(other.lcsClientExternalID))
            return false;
        if (lcsClientInternalID != other.lcsClientInternalID)
            return false;
        if (lcsClientName == null) {
            if (other.lcsClientName != null)
                return false;
        } else if (!lcsClientName.equals(other.lcsClientName))
            return false;
        if (lcsClientType != other.lcsClientType)
            return false;
        if (lcsRequestorID == null) {
            if (other.lcsRequestorID != null)
                return false;
        } else if (!lcsRequestorID.equals(other.lcsRequestorID))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.lcsClientType != null) {
            sb.append("lcsClientType=");
            sb.append(this.lcsClientType.toString());
        }
        if (this.lcsClientExternalID != null) {
            sb.append(", lcsClientExternalID=");
            sb.append(this.lcsClientExternalID.toString());
        }
        if (this.lcsClientInternalID != null) {
            sb.append(", lcsClientInternalID=");
            sb.append(this.lcsClientInternalID.toString());
        }
        if (this.lcsClientName != null) {
            sb.append(", lcsClientName=");
            sb.append(this.lcsClientName.toString());
        }
        if (this.lcsClientDialedByMS != null) {
            sb.append(", lcsClientDialedByMS=");
            sb.append(this.lcsClientDialedByMS.toString());
        }
        if (this.lcsAPN != null) {
            sb.append(", lcsAPN=");
            sb.append(this.lcsAPN.toString());
        }
        if (this.lcsRequestorID != null) {
            sb.append(", lcsRequestorID=");
            sb.append(this.lcsRequestorID.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
