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
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAOnlyAccessIndicator;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class LSAInformationImpl extends SequenceBase implements LSAInformation {

    public static final int _TAG_lsaOnlyAccessIndicator = 1;
    public static final int _TAG_lsaDataList = 2;
    public static final int _TAG_extensionContainer = 3;

    private boolean completeDataListIncluded;
    private LSAOnlyAccessIndicator lsaOnlyAccessIndicator;
    private ArrayList<LSAData> lsaDataList;
    private MAPExtensionContainer extensionContainer;

    public LSAInformationImpl() {
        super("LSAInformation");
    }

    public LSAInformationImpl(boolean completeDataListIncluded, LSAOnlyAccessIndicator lsaOnlyAccessIndicator,
            ArrayList<LSAData> lsaDataList, MAPExtensionContainer extensionContainer) {
        super("LSAInformation");
        this.completeDataListIncluded = completeDataListIncluded;
        this.lsaOnlyAccessIndicator = lsaOnlyAccessIndicator;
        this.lsaDataList = lsaDataList;
        this.extensionContainer = extensionContainer;
    }

    @Override
    public boolean getCompleteDataListIncluded() {
        return this.completeDataListIncluded;
    }

    @Override
    public LSAOnlyAccessIndicator getLSAOnlyAccessIndicator() {
        return this.lsaOnlyAccessIndicator;
    }

    @Override
    public ArrayList<LSAData> getLSADataList() {
        return this.lsaDataList;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.completeDataListIncluded = false;
        this.lsaOnlyAccessIndicator = null;
        this.lsaDataList = null;
        this.extensionContainer = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_UNIVERSAL: {
                    switch (tag) {
                        case Tag.NULL:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".completeDataListIncluded: Parameter not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            ais.readNull();
                            this.completeDataListIncluded = true;
                            break;
                        default:
                            ais.advanceElement();
                            break;
                    }
                }
                    break;
                case Tag.CLASS_CONTEXT_SPECIFIC: {
                    switch (tag) {
                        case _TAG_lsaOnlyAccessIndicator:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".lsaOnlyAccessIndicator: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            int code = (int) ais.readInteger();
                            this.lsaOnlyAccessIndicator = LSAOnlyAccessIndicator.getInstance(code);
                            break;
                        case _TAG_lsaDataList:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".lsaDataList: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);

                            LSAData lsaData = null;
                            AsnInputStream ais2 = ais.readSequenceStream();
                            this.lsaDataList = new ArrayList<LSAData>();
                            while (true) {
                                if (ais2.available() == 0)
                                    break;

                                int tag2 = ais2.readTag();
                                if (tag2 != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || ais2.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + "LSADataImpl: bad tag or tagClass or is primitive ",
                                            MAPParsingComponentExceptionReason.MistypedParameter);

                                lsaData = new LSADataImpl();
                                ((LSADataImpl) lsaData).decodeAll(ais2);
                                this.lsaDataList.add(lsaData);
                            }

                            if (this.lsaDataList.size() < 1 || this.lsaDataList.size() > 20) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter lsaDataList size must be from 1 to 20, found: "
                                        + this.lsaDataList.size(), MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            break;
                        case _TAG_extensionContainer:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".extensionContainer: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.extensionContainer = new MAPExtensionContainerImpl();
                            ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
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

        if (this.lsaDataList != null && (this.lsaDataList.size() < 1 || this.lsaDataList.size() > 20)) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": Parameter lsaDataList size must be from 1 to 20, found: " + this.lsaDataList.size());
        }

        try {
            if (this.completeDataListIncluded)
                asnOs.writeNull();

            if (this.lsaOnlyAccessIndicator != null) {
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_lsaOnlyAccessIndicator,
                        this.lsaOnlyAccessIndicator.getCode());
            }

            if (this.lsaDataList != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_lsaDataList);
                int pos = asnOs.StartContentDefiniteLength();
                for (LSAData lsaData : this.lsaDataList) {
                    ((LSADataImpl) lsaData).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_extensionContainer);

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

        if (this.completeDataListIncluded) {
            sb.append("completeDataListIncluded, ");
        }

        if (this.lsaOnlyAccessIndicator != null) {
            sb.append("lsaOnlyAccessIndicator=");
            sb.append(this.lsaOnlyAccessIndicator.toString());
            sb.append(", ");
        }

        if (this.lsaDataList != null) {
            sb.append("lsaDataList=[");
            boolean firstItem = true;
            for (LSAData be : this.lsaDataList) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(" ");
        }

        sb.append("]");

        return sb.toString();
    }
}
