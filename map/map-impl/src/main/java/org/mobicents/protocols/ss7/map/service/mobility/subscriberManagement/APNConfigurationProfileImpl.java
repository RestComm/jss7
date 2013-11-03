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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.APNConfiguration;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.APNConfigurationProfile;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class APNConfigurationProfileImpl extends SequenceBase implements APNConfigurationProfile {

    private static final int _TAG_ePSDataList = 1;
    private static final int _TAG_extensionContainer = 2;

    private int defaultContext;
    private boolean completeDataListIncluded;
    private ArrayList<APNConfiguration> ePSDataList;
    private MAPExtensionContainer extensionContainer;

    public APNConfigurationProfileImpl() {
        super("APNConfigurationProfile");
    }

    public APNConfigurationProfileImpl(int defaultContext, boolean completeDataListIncluded,
            ArrayList<APNConfiguration> ePSDataList, MAPExtensionContainer extensionContainer) {
        super("APNConfigurationProfile");
        this.defaultContext = defaultContext;
        this.completeDataListIncluded = completeDataListIncluded;
        this.ePSDataList = ePSDataList;
        this.extensionContainer = extensionContainer;
    }

    @Override
    public int getDefaultContext() {
        return this.defaultContext;
    }

    @Override
    public boolean getCompleteDataListIncluded() {
        return this.completeDataListIncluded;
    }

    @Override
    public ArrayList<APNConfiguration> getEPSDataList() {
        return this.ePSDataList;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.defaultContext = -1;
        this.completeDataListIncluded = false;
        this.ePSDataList = null;
        this.extensionContainer = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (num) {
                case 0:
                    if (tag != Tag.INTEGER || ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".defaultContext: bad tag, tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.defaultContext = (int) ais.readInteger();
                    break;
                default:
                    switch (ais.getTagClass()) {
                        case Tag.CLASS_UNIVERSAL: {
                            switch (tag) {
                                case Tag.NULL:
                                    if (!ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".completeDataListIncluded: Parameter is not primitive",
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
                                case _TAG_ePSDataList:
                                    if (ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".ePSDataList: Parameter is primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);

                                    this.ePSDataList = new ArrayList<APNConfiguration>();

                                    AsnInputStream ais2 = ais.readSequenceStream();

                                    while (true) {
                                        if (ais2.available() == 0)
                                            break;

                                        int tag2 = ais2.readTag();

                                        if (tag2 != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_UNIVERSAL
                                                || ais2.isTagPrimitive())
                                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                    + ": bad tag or tagClass or is primitive when decoding ePSDataList",
                                                    MAPParsingComponentExceptionReason.MistypedParameter);

                                        APNConfigurationImpl elem = new APNConfigurationImpl();
                                        ((APNConfigurationImpl) elem).decodeAll(ais2);
                                        this.ePSDataList.add(elem);

                                        if (this.ePSDataList.size() < 1 || this.ePSDataList.size() > 50)
                                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                    + ".ePSDataList: elements count must be from 1 to 50, found: "
                                                    + this.ePSDataList.size(),
                                                    MAPParsingComponentExceptionReason.MistypedParameter);
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

            num++;
        }

        if (this.defaultContext == -1) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament defaultContext is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
        if (this.ePSDataList == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament ePSDataList is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }

    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.ePSDataList == null)
            throw new MAPException("Error while encoding" + _PrimitiveName + ": ePSDataList must not be null");

        if (this.ePSDataList.size() < 1 || this.ePSDataList.size() > 50) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": Parameter ePSDataList size must be from 1 to 50, found: " + this.ePSDataList.size());
        }

        try {
            asnOs.writeInteger(this.defaultContext);

            if (this.completeDataListIncluded)
                asnOs.writeNull();

            asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_ePSDataList);
            int pos = asnOs.StartContentDefiniteLength();
            for (APNConfiguration be : this.ePSDataList) {
                APNConfigurationImpl bee = (APNConfigurationImpl) be;
                bee.encodeAll(asnOs);
            }
            asnOs.FinalizeContent(pos);

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

        sb.append("defaultContext=");
        sb.append(this.defaultContext);
        sb.append(", ");

        if (this.completeDataListIncluded) {
            sb.append("completeDataListIncluded, ");
        }

        if (this.ePSDataList != null) {
            sb.append("ePSDataList=[");
            boolean firstItem = true;
            for (APNConfiguration be : this.ePSDataList) {
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
