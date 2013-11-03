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

package org.mobicents.protocols.ss7.map.service.mobility.locationManagement;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SuperChargerInfo;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 * @author amit bhayani
 *
 */
public class SuperChargerInfoImpl implements SuperChargerInfo, MAPAsnPrimitive {

    public static final int _ID_sendSubscriberData = 0;
    public static final int _ID_subscriberDataStored = 1;

    public static final String _PrimitiveName = "SuperChargerInfo";

    private Boolean sendSubscriberData;
    private byte[] subscriberDataStored;

    /**
     *
     */
    public SuperChargerInfoImpl() {
    }

    /**
     * @param sendSubscriberData
     */
    public SuperChargerInfoImpl(Boolean sendSubscriberData) {
        super();
        this.sendSubscriberData = sendSubscriberData;
    }

    /**
     * @param subscriberDataStored
     */
    public SuperChargerInfoImpl(byte[] subscriberDataStored) {
        super();
        this.subscriberDataStored = subscriberDataStored;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement .SuperChargerInfo#getSendSubscriberData()
     */
    public Boolean getSendSubscriberData() {
        return this.sendSubscriberData;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement .SuperChargerInfo#getSubscriberDataStored()
     */
    public byte[] getSubscriberDataStored() {
        return this.subscriberDataStored;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getTag()
     */
    public int getTag() throws MAPException {
        if (this.sendSubscriberData != null)
            return _ID_sendSubscriberData;
        else
            return _ID_subscriberDataStored;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getTagClass()
     */
    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getIsPrimitive ()
     */
    public boolean getIsPrimitive() {
        return true;
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

        this.sendSubscriberData = null;
        this.subscriberDataStored = null;

        if (ansIS.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ansIS.isTagPrimitive())
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": bad tag class or is not primitive: TagClass=" + ansIS.getTagClass(),
                    MAPParsingComponentExceptionReason.MistypedParameter);

        switch (ansIS.getTag()) {
            case _ID_sendSubscriberData:
                try {
                    ansIS.readNullData(length);
                    this.sendSubscriberData = Boolean.TRUE;
                } catch (AsnException e) {
                    throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": "
                            + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
                } catch (IOException e) {
                    throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": "
                            + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
                }
                break;

            case _ID_subscriberDataStored:
                this.subscriberDataStored = ansIS.readOctetStringData(length);
                break;

            default:
                throw new MAPParsingComponentException("Error while SuperChargerInfo: bad tag: " + ansIS.getTag(),
                        MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeAll( org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        this.encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, this.getTag());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeAll( org.mobicents.protocols.asn.AsnOutputStream,
     * int, int)
     */
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
        try {
            asnOs.writeTag(tagClass, true, tag);
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
        if (this.sendSubscriberData != null) {
            asnOs.writeNullData();
        } else {
            asnOs.writeOctetStringData(this.subscriberDataStored);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SuperChargerInfo [");

        if (sendSubscriberData != null)
            sb.append("sendSubscriberData, ");
        if (subscriberDataStored != null) {
            sb.append("subscriberDataStored=[");
            sb.append(printDataArr(subscriberDataStored));
            sb.append("], ");
        }

        sb.append("]");

        return sb.toString();
    }

    private String printDataArr(byte[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int b : arr) {
            sb.append(b);
            sb.append(", ");
        }

        return sb.toString();
    }
}
