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
package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import java.io.IOException;
import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DestinationNumberCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MatchType;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class DestinationNumberCriteriaImpl extends SequenceBase implements DestinationNumberCriteria {

    private static final int _TAG_matchType = 0;
    private static final int _TAG_destinationNumberList = 1;
    private static final int _TAG_destinationNumberLengthList = 2;

    private MatchType matchType;
    private ArrayList<ISDNAddressString> destinationNumberList;
    private ArrayList<Integer> destinationNumberLengthList;

    public DestinationNumberCriteriaImpl() {
        super("DestinationNumberCriteria");
    }

    public DestinationNumberCriteriaImpl(MatchType matchType, ArrayList<ISDNAddressString> destinationNumberList,
            ArrayList<Integer> destinationNumberLengthList) {
        super("DestinationNumberCriteria");
        this.matchType = matchType;
        this.destinationNumberList = destinationNumberList;
        this.destinationNumberLengthList = destinationNumberLengthList;
    }

    @Override
    public MatchType getMatchType() {
        return this.matchType;
    }

    @Override
    public ArrayList<ISDNAddressString> getDestinationNumberList() {
        return this.destinationNumberList;
    }

    @Override
    public ArrayList<Integer> getDestinationNumberLengthList() {
        return this.destinationNumberLengthList;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.matchType = null;
        this.destinationNumberList = null;
        this.destinationNumberLengthList = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_CONTEXT_SPECIFIC: {
                    switch (tag) {
                        case _TAG_matchType:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".matchType: Parameter is not primitive ",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            int code = (int) ais.readInteger();
                            this.matchType = MatchType.getInstance(code);
                            break;
                        case _TAG_destinationNumberList:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".destinationNumberList: Parameter is primitive ",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            ISDNAddressString isdnAddressString = null;

                            AsnInputStream ais2 = ais.readSequenceStream();
                            this.destinationNumberList = new ArrayList<ISDNAddressString>();
                            while (true) {
                                if (ais2.available() == 0)
                                    break;

                                int tag2 = ais2.readTag();

                                if (tag2 != Tag.STRING_OCTET || ais2.getTagClass() != Tag.CLASS_UNIVERSAL
                                        || !ais2.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": bad tag or tagClass or is primitive when decoding destinationNumberList",
                                            MAPParsingComponentExceptionReason.MistypedParameter);

                                isdnAddressString = new ISDNAddressStringImpl();
                                ((ISDNAddressStringImpl) isdnAddressString).decodeAll(ais2);
                                this.destinationNumberList.add(isdnAddressString);
                            }
                            if (this.destinationNumberList.size() < 1 && this.destinationNumberList.size() > 10) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter destinationNumberList size must be from 1 to 10, found: "
                                        + this.destinationNumberList.size(),
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            break;
                        case _TAG_destinationNumberLengthList:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".destinationNumberLengthList: Parameter is primitive ",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            Integer integer = null;
                            AsnInputStream ais3 = ais.readSequenceStream();
                            this.destinationNumberLengthList = new ArrayList<Integer>();
                            while (true) {
                                if (ais3.available() == 0)
                                    break;

                                int tag3 = ais3.readTag();

                                if (tag3 != Tag.INTEGER || ais3.getTagClass() != Tag.CLASS_UNIVERSAL || !ais3.isTagPrimitive())
                                    throw new MAPParsingComponentException(
                                            "Error while decoding "
                                                    + _PrimitiveName
                                                    + ": bad tag or tagClass or is primitive when decoding destinationNumberLengthList",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                int destNumberLength = (int) ais3.readInteger();
                                integer = new Integer(destNumberLength);
                                this.destinationNumberLengthList.add(integer);
                            }
                            if (this.destinationNumberLengthList.size() < 1 && this.destinationNumberLengthList.size() > 3) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter destinationNumberLengthList size must be from 1 to 3, found: "
                                        + this.destinationNumberLengthList.size(),
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            break;
                        default:
                            ais.advanceElement();
                            break;
                    }
                }
                    break;
                default:
                    ais.advanceElement();
                    break;
            }
        }

        if (this.matchType == null)
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament matchType is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (this.destinationNumberList == null && this.destinationNumberLengthList == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ". Parament destinationNumberList or "
                    + "destinationNumberLengthList or both should be present but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.matchType == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter matchType is not defined");
        }

        if (this.destinationNumberList == null && this.destinationNumberLengthList == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName + ". one or both of destinationNumberList and "
                    + "destinationNumberLengthList needs to be present");
        }

        if (this.destinationNumberList != null
                && (this.destinationNumberList.size() < 1 || this.destinationNumberList.size() > 10)) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": Parameter destinationNumberList size must be from 1 to 10, found: "
                    + this.destinationNumberList.size());
        }

        if (this.destinationNumberLengthList != null
                && (this.destinationNumberLengthList.size() < 1 || this.destinationNumberLengthList.size() > 3)) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": Parameter destinationNumberLengthList size must be from 1 to 3, found: "
                    + this.destinationNumberList.size());
        }

        try {
            asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_matchType, this.matchType.getCode());

            if (this.destinationNumberList != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_destinationNumberList);
                int pos = asnOs.StartContentDefiniteLength();
                for (ISDNAddressString isdnAddressString : this.destinationNumberList) {
                    ((ISDNAddressStringImpl) isdnAddressString).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }

            if (this.destinationNumberLengthList != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_destinationNumberLengthList);
                int pos = asnOs.StartContentDefiniteLength();
                for (Integer integer : this.destinationNumberLengthList) {
                    asnOs.writeInteger(integer);
                }
                asnOs.FinalizeContent(pos);
            }

        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.matchType != null) {
            sb.append("matchType=");
            sb.append(this.matchType.toString());
            sb.append(", ");
        }

        if (this.destinationNumberList != null) {
            sb.append("destinationNumberList=[");
            boolean firstItem = true;
            for (ISDNAddressString be : this.destinationNumberList) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.destinationNumberLengthList != null) {
            sb.append("destinationNumberLengthList=[");
            boolean firstItem = true;
            for (Integer be : this.destinationNumberLengthList) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("] ");
        }

        sb.append("]");

        return sb.toString();
    }
}
