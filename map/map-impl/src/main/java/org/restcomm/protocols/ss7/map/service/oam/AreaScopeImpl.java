/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.restcomm.protocols.ss7.map.service.oam;

import java.io.IOException;
import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.restcomm.protocols.ss7.map.api.primitives.GlobalCellId;
import org.restcomm.protocols.ss7.map.api.primitives.LAIFixedLength;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.EUtranCgi;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RAIdentity;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TAId;
import org.restcomm.protocols.ss7.map.api.service.oam.AreaScope;
import org.restcomm.protocols.ss7.map.primitives.GlobalCellIdImpl;
import org.restcomm.protocols.ss7.map.primitives.LAIFixedLengthImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.primitives.SequenceBase;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.EUtranCgiImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.RAIdentityImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.TAIdImpl;

/**
*
* @author sergey vetyutnev
*
*/
public class AreaScopeImpl extends SequenceBase implements AreaScope {
    public static final int _ID_cgiList = 0;
    public static final int _ID_eUtranCgiList = 1;
    public static final int _ID_routingAreaIdList = 2;
    public static final int _ID_locationAreaIdList = 3;
    public static final int _ID_trackingAreaIdList = 4;
    public static final int _ID_extensionContainer = 5;

    private ArrayList<GlobalCellId> cgiList;
    private ArrayList<EUtranCgi> eUtranCgiList;
    private ArrayList<RAIdentity> routingAreaIdList;
    private ArrayList<LAIFixedLength> locationAreaIdList;
    private ArrayList<TAId> trackingAreaIdList;
    private MAPExtensionContainer extensionContainer;

    public AreaScopeImpl() {
        super("AreaScope");
    }

    public AreaScopeImpl(ArrayList<GlobalCellId> cgiList, ArrayList<EUtranCgi> eUtranCgiList, ArrayList<RAIdentity> routingAreaIdList,
            ArrayList<LAIFixedLength> locationAreaIdList, ArrayList<TAId> trackingAreaIdList, MAPExtensionContainer extensionContainer) {
        super("AreaScope");

        this.cgiList = cgiList;
        this.eUtranCgiList = eUtranCgiList;
        this.routingAreaIdList = routingAreaIdList;
        this.locationAreaIdList = locationAreaIdList;
        this.trackingAreaIdList = trackingAreaIdList;
        this.extensionContainer = extensionContainer;
    }

    @Override
    public ArrayList<GlobalCellId> getCgiList() {
        return cgiList;
    }

    @Override
    public ArrayList<EUtranCgi> getEUutranCgiList() {
        return eUtranCgiList;
    }

    @Override
    public ArrayList<RAIdentity> getRoutingAreaIdList() {
        return routingAreaIdList;
    }

    @Override
    public ArrayList<LAIFixedLength> getLocationAreaIdList() {
        return locationAreaIdList;
    }

    @Override
    public ArrayList<TAId> getTrackingAreaIdList() {
        return trackingAreaIdList;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.cgiList = null;
        this.eUtranCgiList = null;
        this.routingAreaIdList = null;
        this.locationAreaIdList = null;
        this.trackingAreaIdList = null;
        this.extensionContainer = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
            case Tag.CLASS_CONTEXT_SPECIFIC: {
                switch (tag) {

                case _ID_cgiList:
                    if (ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".cgiList: is primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    AsnInputStream ais2 = ais.readSequenceStream();
                    GlobalCellId globalCellId = null;
                    this.cgiList = new ArrayList<GlobalCellId>();
                    while (true) {
                        if (ais2.available() == 0)
                            break;

                        int tag2 = ais2.readTag();
                        if (tag2 != Tag.STRING_OCTET || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || !ais2.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": bad tag or tagClass or is not primitive when decoding cgiList", MAPParsingComponentExceptionReason.MistypedParameter);

                        globalCellId = new GlobalCellIdImpl();
                        ((GlobalCellIdImpl) globalCellId).decodeAll(ais2);
                        this.cgiList.add(globalCellId);
                    }
                    if (this.cgiList.size() < 1 && this.cgiList.size() > 32) {
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ": Parameter cgiList size must be from 1 to 32, found: " + this.cgiList.size(),
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    }
                    break;

                case _ID_eUtranCgiList:
                    if (ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".eUtranCgiList: is primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    ais2 = ais.readSequenceStream();
                    EUtranCgi eUtranCgi = null;
                    this.eUtranCgiList = new ArrayList<EUtranCgi>();
                    while (true) {
                        if (ais2.available() == 0)
                            break;

                        int tag2 = ais2.readTag();
                        if (tag2 != Tag.STRING_OCTET || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || !ais2.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": bad tag or tagClass or is not primitive when decoding eUtranCgiList",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        eUtranCgi = new EUtranCgiImpl();
                        ((EUtranCgiImpl) eUtranCgi).decodeAll(ais2);
                        this.eUtranCgiList.add(eUtranCgi);
                    }
                    if (this.eUtranCgiList.size() < 1 && this.eUtranCgiList.size() > 32) {
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ": Parameter eUtranCgiList size must be from 1 to 32, found: " + this.eUtranCgiList.size(),
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    }
                    break;

                case _ID_routingAreaIdList:
                    if (ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".routingAreaIdList: is primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    ais2 = ais.readSequenceStream();
                    RAIdentity raIdentity = null;
                    this.routingAreaIdList = new ArrayList<RAIdentity>();
                    while (true) {
                        if (ais2.available() == 0)
                            break;

                        int tag2 = ais2.readTag();
                        if (tag2 != Tag.STRING_OCTET || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || !ais2.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": bad tag or tagClass or is not primitive when decoding routingAreaIdList",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        raIdentity = new RAIdentityImpl();
                        ((RAIdentityImpl) raIdentity).decodeAll(ais2);
                        this.routingAreaIdList.add(raIdentity);
                    }
                    if (this.routingAreaIdList.size() < 1 && this.routingAreaIdList.size() > 8) {
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ": Parameter routingAreaIdList size must be from 1 to 8, found: " + this.routingAreaIdList.size(),
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    }
                    break;

                case _ID_locationAreaIdList:
                    if (ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".locationAreaIdList: is primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    ais2 = ais.readSequenceStream();
                    LAIFixedLength laiFixedLength = null;
                    this.locationAreaIdList = new ArrayList<LAIFixedLength>();
                    while (true) {
                        if (ais2.available() == 0)
                            break;

                        int tag2 = ais2.readTag();
                        if (tag2 != Tag.STRING_OCTET || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || !ais2.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": bad tag or tagClass or is not primitive when decoding locationAreaIdList",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        laiFixedLength = new LAIFixedLengthImpl();
                        ((LAIFixedLengthImpl) laiFixedLength).decodeAll(ais2);
                        this.locationAreaIdList.add(laiFixedLength);
                    }
                    if (this.locationAreaIdList.size() < 1 && this.locationAreaIdList.size() > 8) {
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ": Parameter locationAreaIdList size must be from 1 to 8, found: " + this.locationAreaIdList.size(),
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    }
                    break;

                case _ID_trackingAreaIdList:
                    if (ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".trackingAreaIdList: is primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    ais2 = ais.readSequenceStream();
                    TAId taId = null;
                    this.trackingAreaIdList = new ArrayList<TAId>();
                    while (true) {
                        if (ais2.available() == 0)
                            break;

                        int tag2 = ais2.readTag();
                        if (tag2 != Tag.STRING_OCTET || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || !ais2.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": bad tag or tagClass or is not primitive when decoding trackingAreaIdList",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        taId = new TAIdImpl();
                        ((TAIdImpl) taId).decodeAll(ais2);
                        this.trackingAreaIdList.add(taId);
                    }
                    if (this.trackingAreaIdList.size() < 1 && this.trackingAreaIdList.size() > 8) {
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ": Parameter trackingAreaIdList size must be from 1 to 8, found: " + this.trackingAreaIdList.size(),
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    }
                    break;

                case _ID_extensionContainer:
                    if (ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".extensionContainer: Parameter extensionContainer is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
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

        try {
            if (this.cgiList != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_cgiList);
                int pos = asnOs.StartContentDefiniteLength();
                for (GlobalCellId globalCellId : this.cgiList) {
                    ((GlobalCellIdImpl) globalCellId).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }
            if (this.eUtranCgiList != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_eUtranCgiList);
                int pos = asnOs.StartContentDefiniteLength();
                for (EUtranCgi eUtranCgi : this.eUtranCgiList) {
                    ((EUtranCgiImpl) eUtranCgi).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }
            if (this.routingAreaIdList != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_routingAreaIdList);
                int pos = asnOs.StartContentDefiniteLength();
                for (RAIdentity raIdentity : this.routingAreaIdList) {
                    ((RAIdentityImpl) raIdentity).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }
            if (this.locationAreaIdList != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_locationAreaIdList);
                int pos = asnOs.StartContentDefiniteLength();
                for (LAIFixedLength laiFixedLength : this.locationAreaIdList) {
                    ((LAIFixedLengthImpl) laiFixedLength).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }
            if (this.trackingAreaIdList != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_trackingAreaIdList);
                int pos = asnOs.StartContentDefiniteLength();
                for (TAId taId : this.trackingAreaIdList) {
                    ((TAIdImpl) taId).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_extensionContainer);

        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this._PrimitiveName);
        sb.append(" [");

        if (this.cgiList != null) {
            sb.append("cgiList=[");
            boolean firstItem = true;
            for (GlobalCellId be : this.cgiList) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("]");
        }
        if (this.eUtranCgiList != null) {
            sb.append("eUtranCgiList=[");
            boolean firstItem = true;
            for (EUtranCgi be : this.eUtranCgiList) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("]");
        }
        if (this.routingAreaIdList != null) {
            sb.append("routingAreaIdList=[");
            boolean firstItem = true;
            for (RAIdentity be : this.routingAreaIdList) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("]");
        }
        if (this.locationAreaIdList != null) {
            sb.append("locationAreaIdList=[");
            boolean firstItem = true;
            for (LAIFixedLength be : this.locationAreaIdList) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("]");
        }
        if (this.trackingAreaIdList != null) {
            sb.append("trackingAreaIdList=[");
            boolean firstItem = true;
            for (TAId be : this.trackingAreaIdList) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("]");
        }

        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
