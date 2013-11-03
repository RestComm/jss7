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
import org.mobicents.protocols.ss7.map.api.primitives.Time;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.APN;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGId;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGSubscriptionData;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;
import org.mobicents.protocols.ss7.map.primitives.TimeImpl;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class CSGSubscriptionDataImpl extends SequenceBase implements CSGSubscriptionData {

    public static final int _TAG_LipaAllowedAPNList = 0;

    private CSGId csgId;
    private Time expirationDate;
    private MAPExtensionContainer extensionContainer;
    private ArrayList<APN> lipaAllowedAPNList;

    public CSGSubscriptionDataImpl() {
        super("CSGSubscriptionData");
    }

    public CSGSubscriptionDataImpl(CSGId csgId, Time expirationDate, MAPExtensionContainer extensionContainer,
            ArrayList<APN> lipaAllowedAPNList) {
        super("CSGSubscriptionData");
        this.csgId = csgId;
        this.expirationDate = expirationDate;
        this.extensionContainer = extensionContainer;
        this.lipaAllowedAPNList = lipaAllowedAPNList;
    }

    @Override
    public CSGId getCsgId() {
        return this.csgId;
    }

    @Override
    public Time getExpirationDate() {
        return this.expirationDate;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public ArrayList<APN> getLipaAllowedAPNList() {
        return this.lipaAllowedAPNList;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.csgId = null;
        this.expirationDate = null;
        this.extensionContainer = null;
        this.lipaAllowedAPNList = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (num) {
                case 0:
                    if (tag != Tag.STRING_BIT || ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".csgId : bad tag, tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.csgId = new CSGIdImpl();
                    ((CSGIdImpl) this.csgId).decodeAll(ais);
                    break;
                default:
                    switch (ais.getTagClass()) {
                        case Tag.CLASS_UNIVERSAL: {
                            switch (tag) {
                                case Tag.STRING_OCTET:
                                    if (!ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".expirationDate: Parameter is not primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    this.expirationDate = new TimeImpl();
                                    ((TimeImpl) this.expirationDate).decodeAll(ais);
                                    break;
                                case Tag.SEQUENCE:
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
                        case Tag.CLASS_CONTEXT_SPECIFIC: {
                            switch (tag) {
                                case _TAG_LipaAllowedAPNList:
                                    if (ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".lipaAllowedAPNList: Parameter is primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);

                                    this.lipaAllowedAPNList = new ArrayList<APN>();

                                    AsnInputStream ais2 = ais.readSequenceStream();

                                    while (true) {
                                        if (ais2.available() == 0)
                                            break;

                                        int tag2 = ais2.readTag();

                                        if (tag2 != Tag.STRING_OCTET || ais2.getTagClass() != Tag.CLASS_UNIVERSAL
                                                || !ais2.isTagPrimitive())
                                            throw new MAPParsingComponentException(
                                                    "Error while decoding "
                                                            + _PrimitiveName
                                                            + ": bad tag or tagClass or is not primitive when decoding lipaAllowedAPNList",
                                                    MAPParsingComponentExceptionReason.MistypedParameter);

                                        APNImpl elem = new APNImpl();
                                        ((APNImpl) elem).decodeAll(ais2);
                                        this.lipaAllowedAPNList.add(elem);

                                        if (this.lipaAllowedAPNList.size() < 1 || this.lipaAllowedAPNList.size() > 50)
                                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                    + ".lipaAllowedAPNList: elements count must be from 1 to 50, found: "
                                                    + this.lipaAllowedAPNList.size(),
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

            num++;
        }

        if (this.csgId == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament csgId is mandatory but does not found", MAPParsingComponentExceptionReason.MistypedParameter);
        }

    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.csgId == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName + " the mandatory parameter csgId is not defined");
        }

        if (this.lipaAllowedAPNList != null && (this.lipaAllowedAPNList.size() < 1 || this.lipaAllowedAPNList.size() > 50)) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": Parameter lipaAllowedAPNList size must be from 1 to 50, found: " + this.lipaAllowedAPNList.size());
        }

        try {
            if (this.csgId != null)
                ((CSGIdImpl) this.csgId).encodeAll(asnOs);

            if (this.expirationDate != null)
                ((TimeImpl) this.expirationDate).encodeAll(asnOs);

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs);

            if (lipaAllowedAPNList != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_LipaAllowedAPNList);
                int pos = asnOs.StartContentDefiniteLength();
                for (APN be : this.lipaAllowedAPNList) {
                    APNImpl bee = (APNImpl) be;
                    bee.encodeAll(asnOs);
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

        if (this.csgId != null) {
            sb.append("csgId=");
            sb.append(this.csgId.toString());
            sb.append(", ");
        }

        if (this.expirationDate != null) {
            sb.append("expirationDate=");
            sb.append(this.expirationDate.toString());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        if (this.lipaAllowedAPNList != null) {
            sb.append("lipaAllowedAPNList=[");
            boolean firstItem = true;
            for (APN be : this.lipaAllowedAPNList) {
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
