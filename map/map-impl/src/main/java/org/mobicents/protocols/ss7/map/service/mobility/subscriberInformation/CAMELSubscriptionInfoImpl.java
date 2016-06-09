/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2016, Telestax Inc and individual contributors
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

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CAMELSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MGCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MTsmsCAMELTDPCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTdpCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SSCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SpecificCSIWithdraw;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTdpCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TCSI;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.DCSIImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.GPRSCSIImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.MCSIImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.MGCSIImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.MTsmsCAMELTDPCriteriaImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.OBcsmCamelTdpCriteriaImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.OCSIImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.SMSCSIImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.SSCSIImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.SpecificCSIWithdrawImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.TBcsmCamelTdpCriteriaImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.TCSIImpl;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by vsubbotin on 25/05/16.
 */
public class CAMELSubscriptionInfoImpl extends SequenceBase implements CAMELSubscriptionInfo {
    private static final int _TAG_O_CSI = 0;
    private static final int _TAG_O_BSCM_CAMEL_TDP_CRITERIA_LIST = 1;
    private static final int _TAG_D_CSI = 2;
    private static final int _TAG_T_CSI = 3;
    private static final int _TAG_T_BCSM_CAMEL_TDP_CRITERIA_LIST = 4;
    private static final int _TAG_VT_CSI = 5;
    private static final int _TAG_VT_BCSM_CAMEL_TDP_CRITERIA_LIST = 6;
    private static final int _TAG_TIF_CSI = 7;
    private static final int _TAG_TIF_CSI_NOTIFICATION_TO_CSE = 8;
    private static final int _TAG_GPRS_CSI = 9;
    private static final int _TAG_MO_SMS_CSI = 10;
    private static final int _TAG_SS_CSI = 11;
    private static final int _TAG_M_CSI = 12;
    private static final int _TAG_EXTENSION_CONTAINTER = 13;
    private static final int _TAG_SPECIFIC_CSI_DELETE_LIST = 14;
    private static final int _TAG_MT_SMS_CSI = 15;
    private static final int _TAG_MT_SMS_CAMEL_TDP_CRITERIA_LIST = 16;
    private static final int _TAG_MG_CSI = 17;
    private static final int _TAG_O_IM_SCI = 18;
    private static final int _TAG_O_IM_BCSM_CAMEL_TDP_CRITERIA_LIST = 19;
    private static final int _TAG_D_IM_CSI = 20;
    private static final int _TAG_VT_IM_CSI = 21;
    private static final int _TAG_VT_IM_BCSM_CAMEL_TDP_CRITERIA_LIST = 22;

    public static final String _PrimitiveName = "CAMELSubscriptionInfo";

    private OCSI oCsi;
    private ArrayList<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList;
    private DCSI dCsi;
    private TCSI tCsi;
    private ArrayList<TBcsmCamelTdpCriteria> tBcsmCamelTdpCriteriaList;
    private TCSI vtCsi;
    private ArrayList<TBcsmCamelTdpCriteria> vtBcsmCamelTdpCriteriaList;
    private boolean tifCsi;
    private boolean tifCsiNotificationToCSE;
    private GPRSCSI gprsCsi;
    private SMSCSI moSmsCsi;
    private SSCSI ssCsi;
    private MCSI mCsi;
    private MAPExtensionContainer extensionContainer;
    private SpecificCSIWithdraw specificCSIDeletedList;
    private SMSCSI mtSmsCsi;
    private ArrayList<MTsmsCAMELTDPCriteria> mtSmsCamelTdpCriteriaList;
    private MGCSI mgCsi;
    private OCSI oImCsi;
    private ArrayList<OBcsmCamelTdpCriteria> oImBcsmCamelTdpCriteriaList;
    private DCSI dImCsi;
    private TCSI vtImCsi;
    private ArrayList<TBcsmCamelTdpCriteria> vtImBcsmCamelTdpCriteriaList;

    public CAMELSubscriptionInfoImpl() {
        super(_PrimitiveName);
    }

    public CAMELSubscriptionInfoImpl(OCSI oCsi, ArrayList<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList, DCSI dCsi,
            TCSI tCsi, ArrayList<TBcsmCamelTdpCriteria> tBcsmCamelTdpCriteriaList, TCSI vtCsi,
            ArrayList<TBcsmCamelTdpCriteria> vtBcsmCamelTdpCriteriaList, boolean tifCsi, boolean tifCsiNotificationToCSE,
            GPRSCSI gprsCsi, SMSCSI moSmsCsi, SSCSI ssCsi, MCSI mCsi, MAPExtensionContainer extensionContainer,
            SpecificCSIWithdraw specificCSIDeletedList, SMSCSI mtSmsCsi, ArrayList<MTsmsCAMELTDPCriteria> mtSmsCamelTdpCriteriaList,
            MGCSI mgCsi, OCSI oImCsi, ArrayList<OBcsmCamelTdpCriteria> oImBcsmCamelTdpCriteriaList, DCSI dImCsi, TCSI vtImCsi,
            ArrayList<TBcsmCamelTdpCriteria> vtImBcsmCamelTdpCriteriaList) {
        super(_PrimitiveName);
        this.oCsi = oCsi;
        this.oBcsmCamelTDPCriteriaList = oBcsmCamelTDPCriteriaList;
        this.dCsi = dCsi;
        this.tCsi = tCsi;
        this.tBcsmCamelTdpCriteriaList = tBcsmCamelTdpCriteriaList;
        this.vtCsi = vtCsi;
        this.vtBcsmCamelTdpCriteriaList = vtBcsmCamelTdpCriteriaList;
        this.tifCsi = tifCsi;
        this.tifCsiNotificationToCSE = tifCsiNotificationToCSE;
        this.gprsCsi = gprsCsi;
        this.moSmsCsi = moSmsCsi;
        this.ssCsi = ssCsi;
        this.mCsi = mCsi;
        this.extensionContainer = extensionContainer;
        this.specificCSIDeletedList = specificCSIDeletedList;
        this.mtSmsCsi = mtSmsCsi;
        this.mtSmsCamelTdpCriteriaList = mtSmsCamelTdpCriteriaList;
        this.mgCsi = mgCsi;
        this.oImCsi = oImCsi;
        this.oImBcsmCamelTdpCriteriaList = oImBcsmCamelTdpCriteriaList;
        this.dImCsi = dImCsi;
        this.vtImCsi = vtImCsi;
        this.vtImBcsmCamelTdpCriteriaList = vtImBcsmCamelTdpCriteriaList;
    }

    public OCSI getOCsi() {
        return this.oCsi;
    }

    public ArrayList<OBcsmCamelTdpCriteria> getOBcsmCamelTDPCriteriaList() {
        return this.oBcsmCamelTDPCriteriaList;
    }

    public DCSI getDCsi() {
        return this.dCsi;
    }

    public TCSI getTCsi() {
        return this.tCsi;
    }

    public ArrayList<TBcsmCamelTdpCriteria> getTBcsmCamelTdpCriteriaList() {
        return this.tBcsmCamelTdpCriteriaList;
    }

    public TCSI getVtCsi() {
        return this.vtCsi;
    }

    public ArrayList<TBcsmCamelTdpCriteria> getVtBcsmCamelTdpCriteriaList() {
        return this.vtBcsmCamelTdpCriteriaList;
    }

    public boolean getTifCsi() {
        return this.tifCsi;
    }

    public boolean getTifCsiNotificationToCSE() {
        return this.tifCsiNotificationToCSE;
    }

    public GPRSCSI getGprsCsi() {
        return this.gprsCsi;
    }

    public SMSCSI getMoSmsCsi() {
        return this.moSmsCsi;
    }

    public SSCSI getSsCsi() {
        return this.ssCsi;
    }

    public MCSI getMCsi() {
        return this.mCsi;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public SpecificCSIWithdraw getSpecificCSIDeletedList() {
        return this.specificCSIDeletedList;
    }

    public SMSCSI getMtSmsCsi() {
        return this.mtSmsCsi;
    }

    public ArrayList<MTsmsCAMELTDPCriteria> getMtSmsCamelTdpCriteriaList() {
        return this.mtSmsCamelTdpCriteriaList;
    }

    public MGCSI getMgCsi() {
        return this.mgCsi;
    }

    public OCSI geToImCsi() {
        return this.oImCsi;
    }

    public ArrayList<OBcsmCamelTdpCriteria> getOImBcsmCamelTdpCriteriaList() {
        return this.oImBcsmCamelTdpCriteriaList;
    }

    public DCSI getDImCsi() {
        return this.dImCsi;
    }

    public TCSI getVtImCsi() {
        return this.vtImCsi;
    }

    public ArrayList<TBcsmCamelTdpCriteria> getVtImBcsmCamelTdpCriteriaList() {
        return this.vtImBcsmCamelTdpCriteriaList;
    }

    public boolean getIsPrimitive() {
        return false;
    }

    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        this.oCsi = null;
        this.oBcsmCamelTDPCriteriaList = null;
        this.dCsi = null;
        this.tCsi = null;
        this.tBcsmCamelTdpCriteriaList = null;
        this.vtCsi = null;
        this.vtBcsmCamelTdpCriteriaList = null;
        this.tifCsi = false;
        this.tifCsiNotificationToCSE = false;
        this.gprsCsi = null;
        this.moSmsCsi = null;
        this.ssCsi = null;
        this.mCsi = null;
        this.extensionContainer = null;
        this.specificCSIDeletedList = null;
        this.mtSmsCsi = null;
        this.mtSmsCamelTdpCriteriaList = null;
        this.mgCsi = null;
        this.oImCsi = null;
        this.oImBcsmCamelTdpCriteriaList = null;
        this.dImCsi = null;
        this.vtImCsi = null;
        this.vtImBcsmCamelTdpCriteriaList = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0) {
                break;
            }

            int tag = ais.readTag();
            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _TAG_O_CSI:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter oCsi not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.oCsi = new OCSIImpl();
                        ((OCSIImpl)this.oCsi).decodeAll(ais);
                        break;
                    case _TAG_O_BSCM_CAMEL_TDP_CRITERIA_LIST:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".oBcsmCamelTDPCriteriaList: Parameter oBcsmCamelTDPCriteriaList is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        OBcsmCamelTdpCriteria oBcsmCamelTdpCriteria;
                        AsnInputStream ais2 = ais.readSequenceStream();
                        this.oBcsmCamelTDPCriteriaList = new ArrayList<OBcsmCamelTdpCriteria>();
                        while (true) {
                            if (ais2.available() == 0)
                                break;

                            if (ais2.readTag() != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || ais2.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".oBcsmCamelTdpCriteria: Parameter oBcsmCamelTdpCriteria is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);

                            oBcsmCamelTdpCriteria = new OBcsmCamelTdpCriteriaImpl();
                            ((OBcsmCamelTdpCriteriaImpl)oBcsmCamelTdpCriteria).decodeAll(ais2);
                            this.oBcsmCamelTDPCriteriaList.add(oBcsmCamelTdpCriteria);
                        }

                        if (this.oBcsmCamelTDPCriteriaList.size() < 1 || this.oBcsmCamelTDPCriteriaList.size() > 10) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter oBcsmCamelTDPCriteriaList size must be from 1 to 10, found: "
                                    + this.oBcsmCamelTDPCriteriaList.size(), MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        break;
                    case _TAG_D_CSI:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".dCsi: Parameter dCsi is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.dCsi = new DCSIImpl();
                        ((DCSIImpl)this.dCsi).decodeAll(ais);
                        break;
                    case _TAG_T_CSI:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".tCsi: Parameter tCsi is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.tCsi = new TCSIImpl();
                        ((TCSIImpl)this.tCsi).decodeAll(ais);
                        break;
                    case _TAG_T_BCSM_CAMEL_TDP_CRITERIA_LIST:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".tBcsmCamelTdpCriteriaList: Parameter tBcsmCamelTdpCriteriaList is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        TBcsmCamelTdpCriteria tBcsmCamelTdpCriteria;
                        this.tBcsmCamelTdpCriteriaList = new ArrayList<TBcsmCamelTdpCriteria>();
                        ais2 = ais.readSequenceStream();
                        while (true) {
                            if (ais2.available() == 0)
                                break;

                            if (ais2.readTag() != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || ais2.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".tBcsmCamelTdpCriteria: Parameter tBcsmCamelTdpCriteria is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);

                            tBcsmCamelTdpCriteria = new TBcsmCamelTdpCriteriaImpl();
                            ((TBcsmCamelTdpCriteriaImpl)tBcsmCamelTdpCriteria).decodeAll(ais2);
                            this.tBcsmCamelTdpCriteriaList.add(tBcsmCamelTdpCriteria);
                        }

                        if (this.tBcsmCamelTdpCriteriaList.size() < 1 || this.tBcsmCamelTdpCriteriaList.size() > 10) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter tBcsmCamelTdpCriteriaList size must be from 1 to 10, found: "
                                    + this.tBcsmCamelTdpCriteriaList.size(), MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        break;
                    case _TAG_VT_CSI:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".vtCsi: Parameter vtCsi is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.vtCsi = new TCSIImpl();
                        ((TCSIImpl)this.vtCsi).decodeAll(ais);
                        break;
                    case _TAG_VT_BCSM_CAMEL_TDP_CRITERIA_LIST:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".vtBcsmCamelTdpCriteriaList: Parameter vtBcsmCamelTdpCriteriaList is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        TBcsmCamelTdpCriteria vtBcsmCamelTdpCriteria;
                        this.vtBcsmCamelTdpCriteriaList = new ArrayList<TBcsmCamelTdpCriteria>();
                        ais2 = ais.readSequenceStream();
                        while (true) {
                            if (ais2.available() == 0)
                                break;

                            if (ais2.readTag() != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || ais2.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".vtBcsmCamelTdpCriteria: Parameter vtBcsmCamelTdpCriteria is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);

                            vtBcsmCamelTdpCriteria = new TBcsmCamelTdpCriteriaImpl();
                            ((TBcsmCamelTdpCriteriaImpl)vtBcsmCamelTdpCriteria).decodeAll(ais2);
                            this.vtBcsmCamelTdpCriteriaList.add(vtBcsmCamelTdpCriteria);
                        }

                        if (this.vtBcsmCamelTdpCriteriaList.size() < 1 || this.vtBcsmCamelTdpCriteriaList.size() > 10) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter vtBcsmCamelTdpCriteriaList size must be from 1 to 10, found: "
                                    + this.vtBcsmCamelTdpCriteriaList.size(), MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        break;
                    case _TAG_TIF_CSI:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".tifCsi: Parameter tifCsi is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        ais.readNull();
                        this.tifCsi = Boolean.TRUE;
                        break;
                    case _TAG_TIF_CSI_NOTIFICATION_TO_CSE:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".tifCsiNotificationToCSE: Parameter tifCsiNotificationToCSE is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        ais.readNull();
                        this.tifCsiNotificationToCSE = Boolean.TRUE;
                        break;
                    case _TAG_GPRS_CSI:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".gprsCsi: Parameter gprsCsi is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.gprsCsi = new GPRSCSIImpl();
                        ((GPRSCSIImpl)this.gprsCsi).decodeAll(ais);
                        break;
                    case _TAG_MO_SMS_CSI:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".moSmsCsi: Parameter moSmsCsi is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.moSmsCsi = new SMSCSIImpl();
                        ((SMSCSIImpl)this.moSmsCsi).decodeAll(ais);
                        break;
                    case _TAG_SS_CSI:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".ssCsi: Parameter ssCsi is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.ssCsi = new SSCSIImpl();
                        ((SSCSIImpl)this.ssCsi).decodeAll(ais);
                        break;
                    case _TAG_M_CSI:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".mCsi: Parameter mCsi is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.mCsi = new MCSIImpl();
                        ((MCSIImpl)this.mCsi).decodeAll(ais);
                        break;
                    case _TAG_EXTENSION_CONTAINTER:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".extensionContainer: Parameter extensionContainer is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.extensionContainer = new MAPExtensionContainerImpl();
                        ((MAPExtensionContainerImpl)this.extensionContainer).decodeAll(ais);
                        break;
                    case _TAG_SPECIFIC_CSI_DELETE_LIST:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".specificCSIDeletedList: Parameter specificCSIDeletedList is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.specificCSIDeletedList = new SpecificCSIWithdrawImpl();
                        ((SpecificCSIWithdrawImpl)this.specificCSIDeletedList).decodeAll(ais);
                        break;
                    case _TAG_MT_SMS_CSI:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".mtSmsCsi: Parameter mtSmsCsi is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.mtSmsCsi = new SMSCSIImpl();
                        ((SMSCSIImpl)this.mtSmsCsi).decodeAll(ais);
                        break;
                    case _TAG_MT_SMS_CAMEL_TDP_CRITERIA_LIST:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".mtSmsCamelTdpCriteriaList: Parameter mtSmsCamelTdpCriteriaList is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        MTsmsCAMELTDPCriteria mTsmsCAMELTDPCriteria;
                        this.mtSmsCamelTdpCriteriaList = new ArrayList<MTsmsCAMELTDPCriteria>();
                        ais2 = ais.readSequenceStream();
                        while (true) {
                            if (ais2.available() == 0)
                                break;

                            if (ais2.readTag() != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || ais2.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".mTsmsCAMELTDPCriteria: Parameter mTsmsCAMELTDPCriteria is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);

                            mTsmsCAMELTDPCriteria = new MTsmsCAMELTDPCriteriaImpl();
                            ((MTsmsCAMELTDPCriteriaImpl)mTsmsCAMELTDPCriteria).decodeAll(ais2);
                            this.mtSmsCamelTdpCriteriaList.add(mTsmsCAMELTDPCriteria);
                        }

                        if (this.mtSmsCamelTdpCriteriaList.size() < 1 || this.mtSmsCamelTdpCriteriaList.size() > 10) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter mtSmsCamelTdpCriteriaList size must be from 1 to 10, found: "
                                    + this.mtSmsCamelTdpCriteriaList.size(), MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        break;
                    case _TAG_MG_CSI:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".mgCsi: Parameter mgCsi is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.mgCsi = new MGCSIImpl();
                        ((MGCSIImpl)this.mgCsi).decodeAll(ais);
                        break;
                    case _TAG_O_IM_SCI:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".oImCsi: Parameter oImCsi is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.oImCsi = new OCSIImpl();
                        ((OCSIImpl)this.oImCsi).decodeAll(ais);
                        break;
                    case _TAG_O_IM_BCSM_CAMEL_TDP_CRITERIA_LIST:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".oImBcsmCamelTdpCriteriaList: Parameter oImBcsmCamelTdpCriteriaList is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        OBcsmCamelTdpCriteria oImBcsmCamelTdpCriteria;
                        this.oImBcsmCamelTdpCriteriaList = new ArrayList<OBcsmCamelTdpCriteria>();
                        ais2 = ais.readSequenceStream();
                        while (true) {
                            if (ais2.available() == 0)
                                break;

                            if (ais2.readTag() != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || ais2.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".oImBcsmCamelTdpCriteria: Parameter oImBcsmCamelTdpCriteria is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);

                            oImBcsmCamelTdpCriteria = new OBcsmCamelTdpCriteriaImpl();
                            ((OBcsmCamelTdpCriteriaImpl)oImBcsmCamelTdpCriteria).decodeAll(ais2);
                            this.oImBcsmCamelTdpCriteriaList.add(oImBcsmCamelTdpCriteria);
                        }

                        if (this.oImBcsmCamelTdpCriteriaList.size() < 1 || this.oImBcsmCamelTdpCriteriaList.size() > 10) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter oImBcsmCamelTdpCriteriaList size must be from 1 to 10, found: "
                                    + this.oImBcsmCamelTdpCriteriaList.size(), MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        break;
                    case _TAG_D_IM_CSI:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".dImCsi: Parameter dImCsi is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.dImCsi = new DCSIImpl();
                        ((DCSIImpl)this.dImCsi).decodeAll(ais);
                        break;
                    case _TAG_VT_IM_CSI:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".vtImCsi: Parameter vtImCsi is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.vtImCsi = new TCSIImpl();
                        ((TCSIImpl)this.vtImCsi).decodeAll(ais);
                        break;
                    case _TAG_VT_IM_BCSM_CAMEL_TDP_CRITERIA_LIST:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".vtImBcsmCamelTdpCriteriaList: Parameter vtImBcsmCamelTdpCriteriaList is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        TBcsmCamelTdpCriteria vtImBcsmCamelTdpCriteria;
                        this.vtImBcsmCamelTdpCriteriaList = new ArrayList<TBcsmCamelTdpCriteria>();
                        ais2 = ais.readSequenceStream();
                        while (true) {
                            if (ais2.available() == 0)
                                break;

                            if (ais2.readTag() != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || ais2.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".vtImBcsmCamelTdpCriteria: Parameter vtImBcsmCamelTdpCriteria is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);

                            vtImBcsmCamelTdpCriteria = new TBcsmCamelTdpCriteriaImpl();
                            ((TBcsmCamelTdpCriteriaImpl)vtImBcsmCamelTdpCriteria).decodeAll(ais2);
                            this.vtImBcsmCamelTdpCriteriaList.add(vtImBcsmCamelTdpCriteria);
                        }

                        if (this.vtImBcsmCamelTdpCriteriaList.size() < 1 || this.vtImBcsmCamelTdpCriteriaList.size() > 10) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter vtBcsmCamelTdpCriteriaList size must be from 1 to 10, found: "
                                    + this.vtImBcsmCamelTdpCriteriaList.size(), MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.oCsi != null) {
            ((OCSIImpl)this.oCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_O_CSI);
        }

        try {
            if (this.oBcsmCamelTDPCriteriaList != null) {
                if (this.oBcsmCamelTDPCriteriaList.size() < 1 || this.oBcsmCamelTDPCriteriaList.size() > 10) {
                    throw new MAPException("Error while encoding " + _PrimitiveName
                            + " size oBcsmCamelTDPCriteriaList is out of range (1..10). Actual size: " + this.oBcsmCamelTDPCriteriaList.size());
                }

                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_O_BSCM_CAMEL_TDP_CRITERIA_LIST);
                int pos = asnOs.StartContentDefiniteLength();
                for (OBcsmCamelTdpCriteria oBcsmCamelTdpCriteria: this.oBcsmCamelTDPCriteriaList) {
                    ((OBcsmCamelTdpCriteriaImpl)oBcsmCamelTdpCriteria).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }
        } catch (AsnException ae) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + ae.getMessage(), ae);
        }

        if (this.dCsi != null) {
            ((DCSIImpl)this.dCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_D_CSI);
        }

        if (this.tCsi != null) {
            ((TCSIImpl)this.tCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_T_CSI);
        }

        try {
            if (this.tBcsmCamelTdpCriteriaList != null) {
                if (this.tBcsmCamelTdpCriteriaList.size() < 1 || this.tBcsmCamelTdpCriteriaList.size() > 10) {
                    throw new MAPException("Error while encoding " + _PrimitiveName
                            + " size tBcsmCamelTdpCriteriaList is out of range (1..10). Actual size: " + this.tBcsmCamelTdpCriteriaList.size());
                }

                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_T_BCSM_CAMEL_TDP_CRITERIA_LIST);
                int pos = asnOs.StartContentDefiniteLength();
                for (TBcsmCamelTdpCriteria tBcsmCamelTdpCriteria: this.tBcsmCamelTdpCriteriaList) {
                    ((TBcsmCamelTdpCriteriaImpl)tBcsmCamelTdpCriteria).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }
        } catch (AsnException ae) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + ae.getMessage(), ae);
        }

        if (this.vtCsi != null) {
            ((TCSIImpl)this.vtCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_VT_CSI);
        }

        try {
            if (this.vtBcsmCamelTdpCriteriaList != null) {
                if (this.vtBcsmCamelTdpCriteriaList.size() < 1 || this.vtBcsmCamelTdpCriteriaList.size() > 10) {
                    throw new MAPException("Error while encoding " + _PrimitiveName
                            + " size vtBcsmCamelTdpCriteriaList is out of range (1..10). Actual size: " + this.vtBcsmCamelTdpCriteriaList.size());
                }

                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_VT_BCSM_CAMEL_TDP_CRITERIA_LIST);
                int pos = asnOs.StartContentDefiniteLength();
                for (TBcsmCamelTdpCriteria vtBcsmCamelTdpCriteria: this.vtBcsmCamelTdpCriteriaList) {
                    ((TBcsmCamelTdpCriteriaImpl)vtBcsmCamelTdpCriteria).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }
        } catch (AsnException ae) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + ae.getMessage(), ae);
        }

        if (this.tifCsi) {
            try {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_TIF_CSI);
            } catch (IOException e) {
                throw new MAPException("IOException when encoding parameter tifCsi: ", e);
            } catch (AsnException e) {
                throw new MAPException("AsnException when encoding parameter tifCsi: ", e);
            }
        }

        if (this.tifCsiNotificationToCSE) {
            try {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_TIF_CSI_NOTIFICATION_TO_CSE);
            } catch (IOException e) {
                throw new MAPException("IOException when encoding parameter tifCsiNotificationToCSE: ", e);
            } catch (AsnException e) {
                throw new MAPException("AsnException when encoding parameter tifCsiNotificationToCSE: ", e);
            }
        }

        if (this.gprsCsi != null) {
            ((GPRSCSIImpl)this.gprsCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_GPRS_CSI);
        }

        if (this.moSmsCsi != null) {
            ((SMSCSIImpl)this.moSmsCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_MO_SMS_CSI);
        }

        if (this.ssCsi != null) {
            ((SSCSIImpl)this.ssCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_SS_CSI);
        }

        if (this.mCsi != null) {
            ((MCSIImpl)this.mCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_M_CSI);
        }

        if (this.extensionContainer != null) {
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_EXTENSION_CONTAINTER);
        }

        if (this.specificCSIDeletedList != null) {
            ((SpecificCSIWithdrawImpl)this.specificCSIDeletedList).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_SPECIFIC_CSI_DELETE_LIST);
        }

        if (this.mtSmsCsi != null) {
            ((SMSCSIImpl)this.mtSmsCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_MT_SMS_CSI);
        }

        try {
            if (this.mtSmsCamelTdpCriteriaList != null) {
                if (this.mtSmsCamelTdpCriteriaList.size() < 1 || this.mtSmsCamelTdpCriteriaList.size() > 10) {
                    throw new MAPException("Error while encoding " + _PrimitiveName
                            + " size mtSmsCamelTdpCriteriaList is out of range (1..10). Actual size: " + this.mtSmsCamelTdpCriteriaList.size());
                }

                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_MT_SMS_CAMEL_TDP_CRITERIA_LIST);
                int pos = asnOs.StartContentDefiniteLength();
                for (MTsmsCAMELTDPCriteria mTsmsCAMELTDPCriteria: this.mtSmsCamelTdpCriteriaList) {
                    ((MTsmsCAMELTDPCriteriaImpl)mTsmsCAMELTDPCriteria).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }
        } catch (AsnException ae) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + ae.getMessage(), ae);
        }

        if (this.mgCsi != null) {
            ((MGCSIImpl)this.mgCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_MG_CSI);
        }

        if (this.oImCsi != null) {
            ((OCSIImpl)this.oImCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_O_IM_SCI);
        }

        try {
            if (this.oImBcsmCamelTdpCriteriaList != null) {
                if (this.oImBcsmCamelTdpCriteriaList.size() < 1 || this.oImBcsmCamelTdpCriteriaList.size() > 10) {
                    throw new MAPException("Error while encoding " + _PrimitiveName
                            + " size oImBcsmCamelTdpCriteriaList is out of range (1..10). Actual size: " + this.oImBcsmCamelTdpCriteriaList.size());
                }

                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_O_IM_BCSM_CAMEL_TDP_CRITERIA_LIST);
                int pos = asnOs.StartContentDefiniteLength();
                for (OBcsmCamelTdpCriteria oImBcsmCamelTdpCriteria: this.oImBcsmCamelTdpCriteriaList) {
                    ((OBcsmCamelTdpCriteriaImpl)oImBcsmCamelTdpCriteria).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }
        } catch (AsnException ae) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + ae.getMessage(), ae);
        }

        if (this.dImCsi != null) {
            ((DCSIImpl)this.dImCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_D_IM_CSI);
        }

        if (this.vtImCsi != null) {
            ((TCSIImpl)this.vtImCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_VT_IM_CSI);
        }

        try {
            if (this.vtImBcsmCamelTdpCriteriaList != null) {
                if (this.vtImBcsmCamelTdpCriteriaList.size() < 1 || this.vtImBcsmCamelTdpCriteriaList.size() > 10) {
                    throw new MAPException("Error while encoding " + _PrimitiveName
                            + " size vtImBcsmCamelTdpCriteriaList is out of range (1..10). Actual size: " + this.vtImBcsmCamelTdpCriteriaList.size());
                }

                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_VT_IM_BCSM_CAMEL_TDP_CRITERIA_LIST);
                int pos = asnOs.StartContentDefiniteLength();
                for (TBcsmCamelTdpCriteria vtImBcsmCamelTdpCriteria: this.vtImBcsmCamelTdpCriteriaList) {
                    ((TBcsmCamelTdpCriteriaImpl)vtImBcsmCamelTdpCriteria).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }
        } catch (AsnException ae) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + ae.getMessage(), ae);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.oCsi != null) {
            sb.append("ssForBSCode=");
            sb.append(this.oCsi);
        }
        if (this.oBcsmCamelTDPCriteriaList != null) {
            sb.append(", oBcsmCamelTDPCriteriaList=[");
            boolean firstItem = true;
            for (OBcsmCamelTdpCriteria oBcsmCamelTdpCriteria: oBcsmCamelTDPCriteriaList) {
                if (firstItem) {
                    firstItem = false;
                } else {
                    sb.append(", ");
                }
                sb.append(oBcsmCamelTdpCriteria);
            }
            sb.append("], ");
        }
        if (this.dCsi != null) {
            sb.append(", dCsi=");
            sb.append(this.dCsi);
        }
        if (this.tCsi != null) {
            sb.append(", tCsi=");
            sb.append(this.tCsi);
        }
        if (this.tBcsmCamelTdpCriteriaList != null) {
            sb.append(", tBcsmCamelTdpCriteriaList=[");
            boolean firstItem = true;
            for (TBcsmCamelTdpCriteria tBcsmCamelTdpCriteria: tBcsmCamelTdpCriteriaList) {
                if (firstItem) {
                    firstItem = false;
                } else {
                    sb.append(", ");
                }
                sb.append(tBcsmCamelTdpCriteria);
            }
            sb.append("], ");
        }
        if (this.vtCsi != null) {
            sb.append(", vtCsi=");
            sb.append(this.vtCsi);
        }
        if (this.vtBcsmCamelTdpCriteriaList != null) {
            sb.append(", vtBcsmCamelTdpCriteriaList=[");
            boolean firstItem = true;
            for (TBcsmCamelTdpCriteria vtBcsmCamelTdpCriteria: vtBcsmCamelTdpCriteriaList) {
                if (firstItem) {
                    firstItem = false;
                } else {
                    sb.append(", ");
                }
                sb.append(vtBcsmCamelTdpCriteria);
            }
            sb.append("], ");
        }
        if (this.tifCsi)
            sb.append(", tifCsi");
        if (this.tifCsiNotificationToCSE)
            sb.append(", tifCsiNotificationToCSE");
        if (this.gprsCsi != null) {
            sb.append(", gprsCsi=");
            sb.append(this.gprsCsi);
        }
        if (this.moSmsCsi != null) {
            sb.append(", moSmsCsi=");
            sb.append(this.moSmsCsi);
        }
        if (this.ssCsi != null) {
            sb.append(", ssCsi=");
            sb.append(this.ssCsi);
        }
        if (this.mCsi != null) {
            sb.append(", mCsi=");
            sb.append(this.mCsi);
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }
        if (this.specificCSIDeletedList != null) {
            sb.append(", specificCSIDeletedList=");
            sb.append(this.specificCSIDeletedList);
        }
        if (this.mtSmsCsi != null) {
            sb.append(", mtSmsCsi=");
            sb.append(this.mtSmsCsi);
        }
        if (this.mtSmsCamelTdpCriteriaList != null) {
            sb.append(", mtSmsCamelTdpCriteriaList=[");
            boolean firstItem = true;
            for (MTsmsCAMELTDPCriteria mTsmsCAMELTDPCriteria: mtSmsCamelTdpCriteriaList) {
                if (firstItem) {
                    firstItem = false;
                } else {
                    sb.append(", ");
                }
                sb.append(mTsmsCAMELTDPCriteria);
            }
            sb.append("], ");
        }
        if (this.mgCsi != null) {
            sb.append(", mgCsi=");
            sb.append(this.mgCsi);
        }
        if (this.oImCsi != null) {
            sb.append(", oImCsi=");
            sb.append(this.oImCsi);
        }
        if (this.oImBcsmCamelTdpCriteriaList != null) {
            sb.append(", oImBcsmCamelTdpCriteriaList=[");
            boolean firstItem = true;
            for (OBcsmCamelTdpCriteria oImBcsmCamelTdpCriteria: oImBcsmCamelTdpCriteriaList) {
                if (firstItem) {
                    firstItem = false;
                } else {
                    sb.append(", ");
                }
                sb.append(oImBcsmCamelTdpCriteria);
            }
            sb.append("], ");
        }
        if (this.dImCsi != null) {
            sb.append(", dImCsi=");
            sb.append(this.dImCsi);
        }
        if (this.vtImCsi != null) {
            sb.append(", vtImCsi=");
            sb.append(this.vtImCsi);
        }
        if (this.vtImBcsmCamelTdpCriteriaList != null) {
            sb.append(", vtImBcsmCamelTdpCriteriaList=[");
            boolean firstItem = true;
            for (TBcsmCamelTdpCriteria tBcsmCamelTdpCriteria: vtImBcsmCamelTdpCriteriaList) {
                if (firstItem) {
                    firstItem = false;
                } else {
                    sb.append(", ");
                }
                sb.append(tBcsmCamelTdpCriteria);
            }
            sb.append("], ");
        }

        sb.append("]");
        return sb.toString();
    }
}
