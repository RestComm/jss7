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
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientInternalID;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExternalClient;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LCSPrivacyClass;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.NotificationToMSUser;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ServiceType;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;
import org.mobicents.protocols.ss7.map.service.supplementary.SSCodeImpl;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class LCSPrivacyClassImpl extends SequenceBase implements LCSPrivacyClass {

    private static final int _TAG_notificationToMSUser = 0;
    private static final int _TAG_externalClientList = 1;
    private static final int _TAG_plmnClientList = 2;
    private static final int _TAG_extensionContainer = 3;
    private static final int _TAG_extExternalClientList = 4;
    private static final int _TAG_serviceTypeList = 5;

    private SSCode ssCode;
    private ExtSSStatus ssStatus;
    private NotificationToMSUser notificationToMSUser;
    private ArrayList<ExternalClient> externalClientList;
    private ArrayList<LCSClientInternalID> plmnClientList;
    private MAPExtensionContainer extensionContainer;
    private ArrayList<ExternalClient> extExternalClientList;
    private ArrayList<ServiceType> serviceTypeList;

    public LCSPrivacyClassImpl() {
        super("LCSPrivacyClass");
    }

    public LCSPrivacyClassImpl(SSCode ssCode, ExtSSStatus ssStatus, NotificationToMSUser notificationToMSUser,
            ArrayList<ExternalClient> externalClientList, ArrayList<LCSClientInternalID> plmnClientList,
            MAPExtensionContainer extensionContainer, ArrayList<ExternalClient> extExternalClientList,
            ArrayList<ServiceType> serviceTypeList) {
        super("LCSPrivacyClass");
        this.ssCode = ssCode;
        this.ssStatus = ssStatus;
        this.notificationToMSUser = notificationToMSUser;
        this.externalClientList = externalClientList;
        this.plmnClientList = plmnClientList;
        this.extensionContainer = extensionContainer;
        this.extExternalClientList = extExternalClientList;
        this.serviceTypeList = serviceTypeList;
    }

    @Override
    public SSCode getSsCode() {
        return this.ssCode;
    }

    @Override
    public ExtSSStatus getSsStatus() {
        return this.ssStatus;
    }

    @Override
    public NotificationToMSUser getNotificationToMSUser() {
        return this.notificationToMSUser;
    }

    @Override
    public ArrayList<ExternalClient> getExternalClientList() {
        return this.externalClientList;
    }

    @Override
    public ArrayList<LCSClientInternalID> getPLMNClientList() {
        return this.plmnClientList;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public ArrayList<ExternalClient> getExtExternalClientList() {
        return this.extExternalClientList;
    }

    @Override
    public ArrayList<ServiceType> getServiceTypeList() {
        return this.serviceTypeList;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.ssCode = null;
        this.ssStatus = null;
        this.notificationToMSUser = null;
        this.externalClientList = null;
        this.plmnClientList = null;
        this.extensionContainer = null;
        this.extExternalClientList = null;
        this.serviceTypeList = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (num) {
                case 0:
                    if (tag != Tag.STRING_OCTET || ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".ssCode: bad tag, tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.ssCode = new SSCodeImpl();
                    ((SSCodeImpl) this.ssCode).decodeAll(ais);
                    break;
                case 1:
                    if (tag != Tag.STRING_OCTET || ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".ssStatus: bad tag, tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.ssStatus = new ExtSSStatusImpl();
                    ((ExtSSStatusImpl) this.ssStatus).decodeAll(ais);
                    break;
                default:
                    switch (ais.getTagClass()) {
                        case Tag.CLASS_CONTEXT_SPECIFIC: {
                            switch (tag) {
                                case _TAG_notificationToMSUser:
                                    if (!ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".notificationToMSUser: Parameter is not primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    int i2 = (int) ais.readInteger();
                                    this.notificationToMSUser = NotificationToMSUser.getInstance(i2);
                                    break;
                                case _TAG_externalClientList:
                                    if (ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".externalClientList: Parameter is primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);

                                    this.externalClientList = new ArrayList<ExternalClient>();

                                    AsnInputStream ais2 = ais.readSequenceStream();

                                    while (true) {
                                        if (ais2.available() == 0)
                                            break;

                                        int tag2 = ais2.readTag();

                                        if (tag2 != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_UNIVERSAL
                                                || ais2.isTagPrimitive())
                                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                    + ": bad tag or tagClass or is primitive when decoding externalClientList",
                                                    MAPParsingComponentExceptionReason.MistypedParameter);

                                        ExternalClientImpl elem = new ExternalClientImpl();
                                        ((ExternalClientImpl) elem).decodeAll(ais2);
                                        this.externalClientList.add(elem);

                                        if (this.externalClientList.size() < 0 || this.externalClientList.size() > 5)
                                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                    + ".externalClientList: elements count must be from 1 to 5, found: "
                                                    + this.externalClientList.size(),
                                                    MAPParsingComponentExceptionReason.MistypedParameter);
                                    }
                                    break;
                                case _TAG_plmnClientList:
                                    if (ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".plmnClientList: Parameter is primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);

                                    this.plmnClientList = new ArrayList<LCSClientInternalID>();

                                    AsnInputStream ais3 = ais.readSequenceStream();

                                    while (true) {
                                        if (ais3.available() == 0)
                                            break;

                                        int tag2 = ais3.readTag();

                                        if (tag2 != Tag.INTEGER || ais3.getTagClass() != Tag.CLASS_UNIVERSAL
                                                || !ais3.isTagPrimitive())
                                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                    + ": bad tag or tagClass or is not primitive when decoding plmnClientList",
                                                    MAPParsingComponentExceptionReason.MistypedParameter);

                                        int lcsId = (int) ais3.readInteger();
                                        LCSClientInternalID elem = LCSClientInternalID.getLCSClientInternalID(lcsId);

                                        this.plmnClientList.add(elem);

                                        if (this.plmnClientList.size() < 1 || this.plmnClientList.size() > 5)
                                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                    + ".plmnClientList: elements count must be from 1 to 5, found: "
                                                    + this.plmnClientList.size(),
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
                                case _TAG_extExternalClientList:
                                    if (ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".extExternalClientList: Parameter is primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);

                                    this.extExternalClientList = new ArrayList<ExternalClient>();

                                    AsnInputStream ais4 = ais.readSequenceStream();

                                    while (true) {
                                        if (ais4.available() == 0)
                                            break;

                                        int tag2 = ais4.readTag();

                                        if (tag2 != Tag.SEQUENCE || ais4.getTagClass() != Tag.CLASS_UNIVERSAL
                                                || ais4.isTagPrimitive())
                                            throw new MAPParsingComponentException(
                                                    "Error while decoding "
                                                            + _PrimitiveName
                                                            + ": bad tag or tagClass or is primitive when decoding extExternalClientList",
                                                    MAPParsingComponentExceptionReason.MistypedParameter);

                                        ExternalClientImpl elem = new ExternalClientImpl();
                                        ((ExternalClientImpl) elem).decodeAll(ais4);

                                        this.extExternalClientList.add(elem);

                                        if (this.extExternalClientList.size() < 1 || this.extExternalClientList.size() > 35)
                                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                    + ".extExternalClientList: elements count must be from 1 to 35, found: "
                                                    + this.extExternalClientList.size(),
                                                    MAPParsingComponentExceptionReason.MistypedParameter);
                                    }
                                    break;
                                case _TAG_serviceTypeList:
                                    if (ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".plmnClientList: Parameter is primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);

                                    this.serviceTypeList = new ArrayList<ServiceType>();

                                    AsnInputStream ais5 = ais.readSequenceStream();

                                    while (true) {
                                        if (ais5.available() == 0)
                                            break;

                                        int tag2 = ais5.readTag();

                                        if (tag2 != Tag.SEQUENCE || ais5.getTagClass() != Tag.CLASS_UNIVERSAL
                                                || ais5.isTagPrimitive())
                                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                    + ": bad tag or tagClass or is primitive when decoding serviceTypeList",
                                                    MAPParsingComponentExceptionReason.MistypedParameter);

                                        ServiceTypeImpl elem = new ServiceTypeImpl();
                                        ((ServiceTypeImpl) elem).decodeAll(ais5);

                                        this.serviceTypeList.add(elem);

                                        if (this.serviceTypeList.size() < 1 || this.serviceTypeList.size() > 32)
                                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                    + ".serviceTypeList: elements count must be from 1 to 32, found: "
                                                    + this.serviceTypeList.size(),
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

        if (this.ssCode == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament ssCode is mandatory but does not found", MAPParsingComponentExceptionReason.MistypedParameter);
        }
        if (this.ssStatus == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament ssStatus is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }

    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.ssCode == null)
            throw new MAPException("Error while encoding" + _PrimitiveName + ": ssCode must not be null");

        if (this.ssStatus == null)
            throw new MAPException("Error while encoding" + _PrimitiveName + ": ssStatus must not be null");

        if (this.externalClientList != null && (this.externalClientList.size() < 0 || this.externalClientList.size() > 5)) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": Parameter externalClientList size must be from 1 to 5, found: " + this.externalClientList.size());
        }

        if (this.plmnClientList != null && (this.plmnClientList.size() < 1 || this.plmnClientList.size() > 5)) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": Parameter plmnClientList size must be from 1 to 5, found: " + this.plmnClientList.size());
        }

        if (this.extExternalClientList != null
                && (this.extExternalClientList.size() < 1 || this.extExternalClientList.size() > 35)) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": Parameter extExternalClientList size must be from 1 to 35, found: "
                    + this.extExternalClientList.size());
        }

        if (this.serviceTypeList != null && (this.serviceTypeList.size() < 1 || this.serviceTypeList.size() > 32)) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": Parameter serviceTypeList size must be from 1 to 32, found: " + this.serviceTypeList.size());
        }

        try {

            ((SSCodeImpl) this.ssCode).encodeAll(asnOs);

            ((ExtSSStatusImpl) this.ssStatus).encodeAll(asnOs);

            if (this.notificationToMSUser != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_notificationToMSUser, this.notificationToMSUser.getCode());

            if (externalClientList != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_externalClientList);
                int pos = asnOs.StartContentDefiniteLength();
                for (ExternalClient be : this.externalClientList) {
                    ExternalClientImpl bee = (ExternalClientImpl) be;
                    bee.encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }

            if (plmnClientList != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_plmnClientList);
                int pos = asnOs.StartContentDefiniteLength();
                for (LCSClientInternalID be : this.plmnClientList) {
                    asnOs.writeInteger(be.getId());
                }
                asnOs.FinalizeContent(pos);
            }

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_extensionContainer);

            if (extExternalClientList != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_extExternalClientList);
                int pos = asnOs.StartContentDefiniteLength();
                for (ExternalClient be : this.extExternalClientList) {
                    ExternalClientImpl bee = (ExternalClientImpl) be;
                    bee.encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }

            if (serviceTypeList != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_serviceTypeList);
                int pos = asnOs.StartContentDefiniteLength();
                for (ServiceType be : this.serviceTypeList) {
                    ServiceTypeImpl bee = (ServiceTypeImpl) be;
                    bee.encodeAll(asnOs);
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

        if (this.ssCode != null) {
            sb.append("ssCode=");
            sb.append(this.ssCode.toString());
            sb.append(", ");
        }

        if (this.ssStatus != null) {
            sb.append("ssStatus=");
            sb.append(this.ssStatus.toString());
            sb.append(", ");
        }

        if (this.notificationToMSUser != null) {
            sb.append("notificationToMSUser=");
            sb.append(this.notificationToMSUser.toString());
            sb.append(", ");
        }

        if (this.externalClientList != null) {
            sb.append("externalClientList=[");
            boolean firstItem = true;
            for (ExternalClient be : this.externalClientList) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.plmnClientList != null) {
            sb.append("plmnClientList=[");
            boolean firstItem = true;
            for (LCSClientInternalID be : this.plmnClientList) {
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
            sb.append(", ");
        }

        if (this.extExternalClientList != null) {
            sb.append("extExternalClientList=[");
            boolean firstItem = true;
            for (ExternalClient be : this.extExternalClientList) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.serviceTypeList != null) {
            sb.append("serviceTypeList=[");
            boolean firstItem = true;
            for (ServiceType be : this.serviceTypeList) {
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
