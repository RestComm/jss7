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

package org.mobicents.protocols.ss7.map.primitives;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;

/**
 * @author amit bhayani
 *
 */
public class SubscriberIdentityImpl implements SubscriberIdentity, MAPAsnPrimitive {

    public static final String _PrimitiveName = "SubscriberIdentity";

    private static final int _TAG_IMSI = 0;
    private static final int _TAG_MSISDN = 1;

    private IMSI imsi = null;
    private ISDNAddressString msisdn = null;

    /**
     *
     */
    public SubscriberIdentityImpl() {
        super();
    }

    /**
     * @param imsi
     */
    public SubscriberIdentityImpl(IMSI imsi) {
        super();
        this.imsi = imsi;
        this.msisdn = null;
    }

    /**
     * @param msisdn
     */
    public SubscriberIdentityImpl(ISDNAddressString msisdn) {
        super();
        this.msisdn = msisdn;
        this.imsi = null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberIdentity#getIMSI ()
     */
    public IMSI getIMSI() {
        return this.imsi;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberIdentity#getMSISDN ()
     */
    public ISDNAddressString getMSISDN() {
        return this.msisdn;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTag()
     */
    public int getTag() throws MAPException {
        if (this.imsi != null) {
            return _TAG_IMSI;
        } else {
            return _TAG_MSISDN;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTagClass ()
     */
    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getIsPrimitive ()
     */
    public boolean getIsPrimitive() {
        return true;
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
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": ", e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": ", e,
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

        if (asnIS.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !asnIS.isTagPrimitive())
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": bad tag class or is not primitive: TagClass=" + asnIS.getTagClass(),
                    MAPParsingComponentExceptionReason.MistypedParameter);

        switch (asnIS.getTag()) {
            case _TAG_IMSI:
                this.imsi = new IMSIImpl();
                ((IMSIImpl) this.imsi).decodeData(asnIS, length);
                break;
            case _TAG_MSISDN:
                this.msisdn = new ISDNAddressStringImpl();
                ((ISDNAddressStringImpl) this.msisdn).decodeData(asnIS, length);
                break;
            default:
                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                        + ": Expexted imsi [0] IMSI or msisdn [1] ISDN-AddressString, but found " + asnIS.getTag(),
                        MAPParsingComponentExceptionReason.MistypedParameter);
        }
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

        if (this.imsi == null && this.msisdn == null)
            throw new MAPException("Error while encoding " + _PrimitiveName + ": all choices must not be null");
        if (this.imsi != null && this.msisdn != null)
            throw new MAPException("Error while encoding " + _PrimitiveName + ": all choices must not be not null");

        if (this.imsi != null) {
            ((IMSIImpl) this.imsi).encodeData(asnOs);
        } else {
            ((ISDNAddressStringImpl) this.msisdn).encodeData(asnOs);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((imsi == null) ? 0 : imsi.hashCode());
        result = prime * result + ((msisdn == null) ? 0 : msisdn.hashCode());
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
        SubscriberIdentityImpl other = (SubscriberIdentityImpl) obj;
        if (imsi == null) {
            if (other.imsi != null)
                return false;
        } else if (!imsi.equals(other.imsi))
            return false;
        if (msisdn == null) {
            if (other.msisdn != null)
                return false;
        } else if (!msisdn.equals(other.msisdn))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.imsi != null) {
            sb.append(" imsi=");
            sb.append(this.imsi);
        }
        if (this.msisdn != null) {
            sb.append(" msisdn=");
            sb.append(this.msisdn);
        }

        sb.append("]");

        return sb.toString();
    }
}
