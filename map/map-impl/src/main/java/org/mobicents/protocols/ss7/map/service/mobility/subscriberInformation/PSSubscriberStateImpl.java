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
import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.NotReachableReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.PDPContextInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.PSSubscriberState;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.PSSubscriberStateChoice;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class PSSubscriberStateImpl implements PSSubscriberState, MAPAsnPrimitive {

    public static final int _ID_notProvidedFromSGSNorMME = 0;
    public static final int _ID_ps_Detached = 1;
    public static final int _ID_ps_AttachedNotReachableForPaging = 2;
    public static final int _ID_ps_AttachedReachableForPaging = 3;
    public static final int _ID_ps_PDP_ActiveNotReachableForPaging = 4;
    public static final int _ID_ps_PDP_ActiveReachableForPaging = 5;

    public static final String _PrimitiveName = "PSSubscriberState";

    private PSSubscriberStateChoice choice;
    private NotReachableReason netDetNotReachable;
    private ArrayList<PDPContextInfo> pdpContextInfoList;

    public PSSubscriberStateImpl() {
    }

    public PSSubscriberStateImpl(PSSubscriberStateChoice choice, NotReachableReason netDetNotReachable,
            ArrayList<PDPContextInfo> pdpContextInfoList) {
        this.choice = choice;
        this.netDetNotReachable = netDetNotReachable;
        this.pdpContextInfoList = pdpContextInfoList;
    }

    @Override
    public PSSubscriberStateChoice getChoice() {
        return choice;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation. PSSubscriberState#getNetDetNotReachable()
     */
    public NotReachableReason getNetDetNotReachable() {
        return this.netDetNotReachable;
    }

    @Override
    public ArrayList<PDPContextInfo> getPDPContextInfoList() {
        return pdpContextInfoList;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getTag()
     */
    public int getTag() throws MAPException {
        if (this.choice == PSSubscriberStateChoice.notProvidedFromSGSNorMME) {
            return _ID_notProvidedFromSGSNorMME;
        } else if (this.choice == PSSubscriberStateChoice.psDetached) {
            return _ID_ps_Detached;
        } else if (this.choice == PSSubscriberStateChoice.psAttachedNotReachableForPaging) {
            return _ID_ps_AttachedNotReachableForPaging;
        } else if (this.choice == PSSubscriberStateChoice.psAttachedReachableForPaging) {
            return _ID_ps_AttachedReachableForPaging;
        } else if (this.choice == PSSubscriberStateChoice.psPDPActiveNotReachableForPaging) {
            return _ID_ps_PDP_ActiveNotReachableForPaging;
        } else if (this.choice == PSSubscriberStateChoice.psPDPActiveReachableForPaging) {
            return _ID_ps_PDP_ActiveReachableForPaging;
        } else if (this.choice == PSSubscriberStateChoice.netDetNotReachable) {
            return Tag.ENUMERATED;
        }

        throw new MAPException("Error encoding " + _PrimitiveName + ": Bad hoice value");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getTagClass()
     */
    public int getTagClass() {
        if (this.choice == PSSubscriberStateChoice.netDetNotReachable)
            return Tag.CLASS_UNIVERSAL;
        else
            return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getIsPrimitive ()
     */
    public boolean getIsPrimitive() {
        if (this.choice == PSSubscriberStateChoice.psPDPActiveNotReachableForPaging
                || this.choice == PSSubscriberStateChoice.psPDPActiveReachableForPaging)
            return false;
        else
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

        this.choice = null;
        this.netDetNotReachable = null;
        this.pdpContextInfoList = null;

        if (ansIS.getTagClass() == Tag.CLASS_UNIVERSAL) {
            switch (ansIS.getTag()) {
                case Tag.ENUMERATED:
                    if (!ansIS.isTagPrimitive())
                        throw new MAPParsingComponentException(
                                "Error while decoding netDetNotReachable choice: Parameter is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.choice = PSSubscriberStateChoice.netDetNotReachable;
                    int i1 = (int) ansIS.readIntegerData(length);
                    this.netDetNotReachable = NotReachableReason.getInstance(i1);
                    break;

                default:
                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                            + ": bad tag for Universal TagClass: " + ansIS.getTag(),
                            MAPParsingComponentExceptionReason.MistypedParameter);
            }
        } else if (ansIS.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
            switch (ansIS.getTag()) {
                case _ID_notProvidedFromSGSNorMME:
                    if (!ansIS.isTagPrimitive())
                        throw new MAPParsingComponentException(
                                "Error while decoding notProvidedFromSGSNorMME choice: Parameter is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.choice = PSSubscriberStateChoice.notProvidedFromSGSNorMME;
                    ansIS.readNullData(length);
                    break;

                case _ID_ps_Detached:
                    if (!ansIS.isTagPrimitive())
                        throw new MAPParsingComponentException(
                                "Error while decoding psDetached choice: Parameter is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.choice = PSSubscriberStateChoice.psDetached;
                    ansIS.readNullData(length);
                    break;

                case _ID_ps_AttachedNotReachableForPaging:
                    if (!ansIS.isTagPrimitive())
                        throw new MAPParsingComponentException(
                                "Error while decoding psAttachedNotReachableForPaging choice: Parameter is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.choice = PSSubscriberStateChoice.psAttachedNotReachableForPaging;
                    ansIS.readNullData(length);
                    break;

                case _ID_ps_AttachedReachableForPaging:
                    if (!ansIS.isTagPrimitive())
                        throw new MAPParsingComponentException(
                                "Error while decoding psAttachedReachableForPaging choice: Parameter is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.choice = PSSubscriberStateChoice.psAttachedReachableForPaging;
                    ansIS.readNullData(length);
                    break;

                case _ID_ps_PDP_ActiveNotReachableForPaging:
                    if (ansIS.isTagPrimitive())
                        throw new MAPParsingComponentException(
                                "Error while decoding psPDPActiveNotReachableForPaging choice: Parameter is primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.choice = PSSubscriberStateChoice.psPDPActiveNotReachableForPaging;
                    this.decodePdpContextInfoList(ansIS, length);
                    break;

                case _ID_ps_PDP_ActiveReachableForPaging:
                    if (ansIS.isTagPrimitive())
                        throw new MAPParsingComponentException(
                                "Error while decoding psPDPActiveReachableForPaging choice: Parameter is primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.choice = PSSubscriberStateChoice.psPDPActiveReachableForPaging;
                    this.decodePdpContextInfoList(ansIS, length);
                    break;

                default:
                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                            + ": bad tag for ContextSpecific TagClass: " + ansIS.getTag(),
                            MAPParsingComponentExceptionReason.MistypedParameter);
            }
        } else {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad tagClass: "
                    + ansIS.getTagClass(), MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void decodePdpContextInfoList(AsnInputStream ansIS, int length) throws AsnException, IOException,
            MAPParsingComponentException {
        this.pdpContextInfoList = new ArrayList<PDPContextInfo>();
        while (true) {
            if (ansIS.available() == 0)
                break;

            int tag2 = ansIS.readTag();
            if (ansIS.getTagClass() != Tag.CLASS_UNIVERSAL || tag2 != Tag.SEQUENCE || ansIS.isTagPrimitive())
                throw new MAPParsingComponentException("Error when decoding " + _PrimitiveName
                        + " pdpContextInfoList parameter components: bad tag class or tag or is primitive",
                        MAPParsingComponentExceptionReason.MistypedParameter);

            PDPContextInfoImpl elem = new PDPContextInfoImpl();
            elem.decodeAll(ansIS);
            this.pdpContextInfoList.add(elem);
        }
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

        try {
            if (this.choice == null)
                throw new MAPException("Error while encoding the " + _PrimitiveName + ": choice is not defined");

            if ((this.choice == PSSubscriberStateChoice.psPDPActiveNotReachableForPaging || this.choice == PSSubscriberStateChoice.psPDPActiveReachableForPaging)
                    && this.pdpContextInfoList == null)
                throw new MAPException(
                        "Error while encoding the "
                                + _PrimitiveName
                                + ": for choice psPDPActiveNotReachableForPaging or psPDPActiveReachableForPaging - pdpContextInfoList must not be null");

            if ((this.choice == PSSubscriberStateChoice.netDetNotReachable) && this.netDetNotReachable == null)
                throw new MAPException("Error while encoding the " + _PrimitiveName
                        + ": for choice netDetNotReachable - netDetNotReachable must not be null");

            switch (this.choice) {
                case notProvidedFromSGSNorMME:
                case psDetached:
                case psAttachedNotReachableForPaging:
                case psAttachedReachableForPaging:
                    asnOs.writeNullData();
                    break;
                case psPDPActiveNotReachableForPaging:
                case psPDPActiveReachableForPaging:

                    if (this.pdpContextInfoList.size() < 1 || this.pdpContextInfoList.size() > 50)
                        throw new MAPException("Error while encoding " + _PrimitiveName
                                + ": pdpContextInfoList size must be from 1 to 50");

                    for (PDPContextInfo cii : this.pdpContextInfoList) {
                        PDPContextInfoImpl ci = (PDPContextInfoImpl) cii;
                        ci.encodeAll(asnOs);
                    }
                    break;
                case netDetNotReachable:
                    asnOs.writeIntegerData(this.netDetNotReachable.getCode());
                    break;
            }

        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.choice != null)
            sb.append(this.choice.toString());
        if (this.netDetNotReachable != null) {
            sb.append(", netDetNotReachable=");
            sb.append(this.netDetNotReachable.toString());
        }
        if (this.pdpContextInfoList != null && this.pdpContextInfoList.size() > 0) {
            sb.append(", pdpContextInfoList [");
            for (PDPContextInfo p : pdpContextInfoList) {
                sb.append("PDPContextInfo=");
                sb.append(p);
                sb.append(", ");
            }
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
}
