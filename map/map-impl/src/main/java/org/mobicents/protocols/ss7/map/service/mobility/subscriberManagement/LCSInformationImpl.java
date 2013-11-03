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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LCSInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LCSPrivacyClass;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MOLRClass;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class LCSInformationImpl extends SequenceBase implements LCSInformation {

    public static final int _TAG_gmlcList = 0;
    public static final int _TAG_lcsPrivacyExceptionList = 1;
    public static final int _TAG_molrList = 2;
    public static final int _TAG_addLcsPrivacyExceptionList = 3;

    private ArrayList<ISDNAddressString> gmlcList;
    private ArrayList<LCSPrivacyClass> lcsPrivacyExceptionList;
    private ArrayList<MOLRClass> molrList;
    private ArrayList<LCSPrivacyClass> addLcsPrivacyExceptionList;

    public LCSInformationImpl() {
        super("LCSInformation");
    }

    public LCSInformationImpl(ArrayList<ISDNAddressString> gmlcList, ArrayList<LCSPrivacyClass> lcsPrivacyExceptionList,
            ArrayList<MOLRClass> molrList, ArrayList<LCSPrivacyClass> addLcsPrivacyExceptionList) {
        super("LCSInformation");
        this.gmlcList = gmlcList;
        this.lcsPrivacyExceptionList = lcsPrivacyExceptionList;
        this.molrList = molrList;
        this.addLcsPrivacyExceptionList = addLcsPrivacyExceptionList;
    }

    @Override
    public ArrayList<ISDNAddressString> getGmlcList() {
        return this.gmlcList;
    }

    @Override
    public ArrayList<LCSPrivacyClass> getLcsPrivacyExceptionList() {
        return this.lcsPrivacyExceptionList;
    }

    @Override
    public ArrayList<MOLRClass> getMOLRList() {
        return this.molrList;
    }

    @Override
    public ArrayList<LCSPrivacyClass> getAddLcsPrivacyExceptionList() {
        return this.addLcsPrivacyExceptionList;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.gmlcList = null;
        this.lcsPrivacyExceptionList = null;
        this.molrList = null;
        this.addLcsPrivacyExceptionList = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_CONTEXT_SPECIFIC: {
                    switch (tag) {
                        case _TAG_gmlcList:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".gmlcList: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);

                            ISDNAddressString isdnAddressString = null;
                            AsnInputStream ais2 = ais.readSequenceStream();
                            this.gmlcList = new ArrayList<ISDNAddressString>();
                            while (true) {
                                if (ais2.available() == 0)
                                    break;

                                int tag2 = ais2.readTag();
                                if (tag2 != Tag.STRING_OCTET || ais2.getTagClass() != Tag.CLASS_UNIVERSAL
                                        || !ais2.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + "ISDNAddressString: bad tag or tagClass or is not primitive ",
                                            MAPParsingComponentExceptionReason.MistypedParameter);

                                isdnAddressString = new ISDNAddressStringImpl();
                                ((ISDNAddressStringImpl) isdnAddressString).decodeAll(ais2);
                                this.gmlcList.add(isdnAddressString);
                            }

                            if (this.gmlcList.size() < 1 || this.gmlcList.size() > 5) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter gmlcList size must be from 1 to 5, found: " + this.gmlcList.size(),
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            break;
                        case _TAG_lcsPrivacyExceptionList:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".lcsPrivacyExceptionList: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);

                            LCSPrivacyClass lcsPrivacyClass = null;
                            AsnInputStream ais3 = ais.readSequenceStream();
                            this.lcsPrivacyExceptionList = new ArrayList<LCSPrivacyClass>();
                            while (true) {
                                if (ais3.available() == 0)
                                    break;

                                int tag2 = ais3.readTag();
                                if (tag2 != Tag.SEQUENCE || ais3.getTagClass() != Tag.CLASS_UNIVERSAL || ais3.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + "LCSPrivacyClass: bad tag or tagClass or is primitive ",
                                            MAPParsingComponentExceptionReason.MistypedParameter);

                                lcsPrivacyClass = new LCSPrivacyClassImpl();
                                ((LCSPrivacyClassImpl) lcsPrivacyClass).decodeAll(ais3);
                                this.lcsPrivacyExceptionList.add(lcsPrivacyClass);
                            }

                            if (this.lcsPrivacyExceptionList.size() < 1 || this.lcsPrivacyExceptionList.size() > 4) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter lcsPrivacyExceptionList size must be from 1 to 4, found: "
                                        + this.lcsPrivacyExceptionList.size(),
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            break;
                        case _TAG_molrList:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".molrList: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);

                            MOLRClass molrClass = null;
                            AsnInputStream ais4 = ais.readSequenceStream();
                            this.molrList = new ArrayList<MOLRClass>();
                            while (true) {
                                if (ais4.available() == 0)
                                    break;

                                int tag2 = ais4.readTag();
                                if (tag2 != Tag.SEQUENCE || ais4.getTagClass() != Tag.CLASS_UNIVERSAL || ais4.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + "molrClass: bad tag or tagClass or is primitive ",
                                            MAPParsingComponentExceptionReason.MistypedParameter);

                                molrClass = new MOLRClassImpl();
                                ((MOLRClassImpl) molrClass).decodeAll(ais4);
                                this.molrList.add(molrClass);
                            }

                            if (this.molrList.size() < 1 || this.molrList.size() > 3) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter molrList size must be from 1 to 3, found: " + this.molrList.size(),
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            break;
                        case _TAG_addLcsPrivacyExceptionList:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".specificAPNInfoList: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);

                            LCSPrivacyClass addLcsPrivacyClass = null;
                            AsnInputStream ais5 = ais.readSequenceStream();
                            this.addLcsPrivacyExceptionList = new ArrayList<LCSPrivacyClass>();
                            while (true) {
                                if (ais5.available() == 0)
                                    break;

                                int tag2 = ais5.readTag();
                                if (tag2 != Tag.SEQUENCE || ais5.getTagClass() != Tag.CLASS_UNIVERSAL || ais5.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + "addLcsPrivacyClass: bad tag or tagClass or is primitive ",
                                            MAPParsingComponentExceptionReason.MistypedParameter);

                                addLcsPrivacyClass = new LCSPrivacyClassImpl();
                                ((LCSPrivacyClassImpl) addLcsPrivacyClass).decodeAll(ais5);
                                this.addLcsPrivacyExceptionList.add(addLcsPrivacyClass);
                            }

                            if (this.addLcsPrivacyExceptionList.size() < 1 || this.addLcsPrivacyExceptionList.size() > 4) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter addLcsPrivacyExceptionList size must be from 1 to 4, found: "
                                        + this.addLcsPrivacyExceptionList.size(),
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

    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.gmlcList != null && (this.gmlcList.size() < 1 || this.gmlcList.size() > 5)) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": Parameter gmlcList size must be from 1 to 5, found: " + this.gmlcList.size());
        }

        if (this.lcsPrivacyExceptionList != null
                && (this.lcsPrivacyExceptionList.size() < 1 || this.lcsPrivacyExceptionList.size() > 4)) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": Parameter lcsPrivacyExceptionList size must be from 1 to 4, found: "
                    + this.lcsPrivacyExceptionList.size());
        }

        if (this.molrList != null && (this.molrList.size() < 1 || this.molrList.size() > 3)) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": Parameter molrList size must be from 1 to 3, found: " + this.molrList.size());
        }

        if (this.addLcsPrivacyExceptionList != null
                && (this.addLcsPrivacyExceptionList.size() < 1 || this.addLcsPrivacyExceptionList.size() > 4)) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": Parameter addLcsPrivacyExceptionList size must be from 1 to 4, found: "
                    + this.addLcsPrivacyExceptionList.size());
        }

        if (this.addLcsPrivacyExceptionList != null
                && !(this.lcsPrivacyExceptionList != null && this.lcsPrivacyExceptionList.size() == 4)) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": addLcsPrivacyExceptionList may be sent only if"
                    + " lcsPrivacyExceptionList is present and contains four instances of LCSPrivacyClass");
        }

        try {

            if (this.gmlcList != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_gmlcList);
                int pos = asnOs.StartContentDefiniteLength();
                for (ISDNAddressString isdnAddressString : this.gmlcList) {
                    ((ISDNAddressStringImpl) isdnAddressString).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }

            if (this.lcsPrivacyExceptionList != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_lcsPrivacyExceptionList);
                int pos = asnOs.StartContentDefiniteLength();
                for (LCSPrivacyClass lcsPrivacyClass : this.lcsPrivacyExceptionList) {
                    ((LCSPrivacyClassImpl) lcsPrivacyClass).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }

            if (this.molrList != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_molrList);
                int pos = asnOs.StartContentDefiniteLength();
                for (MOLRClass molrClass : this.molrList) {
                    ((MOLRClassImpl) molrClass).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }

            if (this.addLcsPrivacyExceptionList != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_addLcsPrivacyExceptionList);
                int pos = asnOs.StartContentDefiniteLength();
                for (LCSPrivacyClass lcsPrivacyClass : this.addLcsPrivacyExceptionList) {
                    ((LCSPrivacyClassImpl) lcsPrivacyClass).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }

        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.gmlcList != null) {
            sb.append("gmlcList=[");
            boolean firstItem = true;
            for (ISDNAddressString be : this.gmlcList) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.lcsPrivacyExceptionList != null) {
            sb.append("lcsPrivacyExceptionList=[");
            boolean firstItem = true;
            for (LCSPrivacyClass be : this.lcsPrivacyExceptionList) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.molrList != null) {
            sb.append("molrList=[");
            boolean firstItem = true;
            for (MOLRClass be : this.molrList) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.addLcsPrivacyExceptionList != null) {
            sb.append("addLcsPrivacyExceptionList=[");
            boolean firstItem = true;
            for (LCSPrivacyClass be : this.addLcsPrivacyExceptionList) {
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
