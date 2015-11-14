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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import java.io.IOException;
import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessageType;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSSubscriptionDataWithdraw;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSSubscriptionDataWithdraw;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAInformationWithdraw;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SpecificCSIWithdraw;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ZoneCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.mobility.MobilityMessageImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.SSCodeImpl;

/**
*
* @author sergey vetyutnev
*
*/
public class DeleteSubscriberDataRequestImpl extends MobilityMessageImpl implements DeleteSubscriberDataRequest {
    protected static final int _TAG_imsi = 0;
    protected static final int _TAG_basicServiceList = 1;
    protected static final int _TAG_ss_List = 2;
    protected static final int _TAG_roamingRestrictionDueToUnsupportedFeature = 4;
    protected static final int _TAG_regionalSubscriptionIdentifier = 5;
    protected static final int _TAG_vbsGroupIndication = 7;
    protected static final int _TAG_vgcsGroupIndication = 8;
    protected static final int _TAG_camelSubscriptionInfoWithdraw = 9;
    protected static final int _TAG_extensionContainer = 6;
    protected static final int _TAG_gprsSubscriptionDataWithdraw = 10;
    protected static final int _TAG_roamingRestrictedInSgsnDueToUnsuppportedFeature = 11;
    protected static final int _TAG_lsaInformationWithdraw = 12;
    protected static final int _TAG_gmlc_ListWithdraw = 13;
    protected static final int _TAG_istInformationWithdraw = 14;
    protected static final int _TAG_specificCSI_Withdraw = 15;
    protected static final int _TAG_chargingCharacteristicsWithdraw = 16;
    protected static final int _TAG_stn_srWithdraw = 17;
    protected static final int _TAG_epsSubscriptionDataWithdraw = 18;
    protected static final int _TAG_apn_oi_replacementWithdraw = 19;
    protected static final int _TAG_csg_SubscriptionDeleted = 20;

    public static final String _PrimitiveName = "DeleteSubscriberDataRequest";

    private IMSI imsi;
    private ArrayList<ExtBasicServiceCode> basicServiceList;
    private ArrayList<SSCode> ssList;
    private boolean roamingRestrictionDueToUnsupportedFeature;
    private ZoneCode regionalSubscriptionIdentifier;
    private boolean vbsGroupIndication;
    private boolean vgcsGroupIndication;
    private boolean camelSubscriptionInfoWithdraw;
    private MAPExtensionContainer extensionContainer;
    private GPRSSubscriptionDataWithdraw gprsSubscriptionDataWithdraw;
    private boolean roamingRestrictedInSgsnDueToUnsuppportedFeature;
    private LSAInformationWithdraw lsaInformationWithdraw;
    private boolean gmlcListWithdraw;
    private boolean istInformationWithdraw;
    private SpecificCSIWithdraw specificCSIWithdraw;
    private boolean chargingCharacteristicsWithdraw;
    private boolean stnSrWithdraw;
    private EPSSubscriptionDataWithdraw epsSubscriptionDataWithdraw;
    private boolean apnOiReplacementWithdraw;
    private boolean csgSubscriptionDeleted;

    public DeleteSubscriberDataRequestImpl() {
    }

    public DeleteSubscriberDataRequestImpl(IMSI imsi, ArrayList<ExtBasicServiceCode> basicServiceList, ArrayList<SSCode> ssList,
            boolean roamingRestrictionDueToUnsupportedFeature, ZoneCode regionalSubscriptionIdentifier, boolean vbsGroupIndication,
            boolean vgcsGroupIndication, boolean camelSubscriptionInfoWithdraw, MAPExtensionContainer extensionContainer,
            GPRSSubscriptionDataWithdraw gprsSubscriptionDataWithdraw, boolean roamingRestrictedInSgsnDueToUnsuppportedFeature,
            LSAInformationWithdraw lsaInformationWithdraw, boolean gmlcListWithdraw, boolean istInformationWithdraw, SpecificCSIWithdraw specificCSIWithdraw,
            boolean chargingCharacteristicsWithdraw, boolean stnSrWithdraw, EPSSubscriptionDataWithdraw epsSubscriptionDataWithdraw,
            boolean apnOiReplacementWithdraw, boolean csgSubscriptionDeleted) {
        this.imsi = imsi;
        this.basicServiceList = basicServiceList;
        this.ssList = ssList;
        this.roamingRestrictionDueToUnsupportedFeature = roamingRestrictionDueToUnsupportedFeature;
        this.regionalSubscriptionIdentifier = regionalSubscriptionIdentifier;
        this.vbsGroupIndication = vbsGroupIndication;
        this.vgcsGroupIndication = vgcsGroupIndication;
        this.camelSubscriptionInfoWithdraw = camelSubscriptionInfoWithdraw;
        this.extensionContainer = extensionContainer;
        this.gprsSubscriptionDataWithdraw = gprsSubscriptionDataWithdraw;
        this.roamingRestrictedInSgsnDueToUnsuppportedFeature = roamingRestrictedInSgsnDueToUnsuppportedFeature;
        this.lsaInformationWithdraw = lsaInformationWithdraw;
        this.gmlcListWithdraw = gmlcListWithdraw;
        this.istInformationWithdraw = istInformationWithdraw;
        this.specificCSIWithdraw = specificCSIWithdraw;
        this.chargingCharacteristicsWithdraw = chargingCharacteristicsWithdraw;
        this.stnSrWithdraw = stnSrWithdraw;
        this.epsSubscriptionDataWithdraw = epsSubscriptionDataWithdraw;
        this.apnOiReplacementWithdraw = apnOiReplacementWithdraw;
        this.csgSubscriptionDeleted = csgSubscriptionDeleted;
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.deleteSubscriberData_Request;
    }

    public int getOperationCode() {
        return MAPOperationCode.deleteSubscriberData;
    }

    @Override
    public IMSI getImsi() {
        return imsi;
    }

    @Override
    public ArrayList<ExtBasicServiceCode> getBasicServiceList() {
        return basicServiceList;
    }

    @Override
    public ArrayList<SSCode> getSsList() {
        return ssList;
    }

    @Override
    public boolean getRoamingRestrictionDueToUnsupportedFeature() {
        return roamingRestrictionDueToUnsupportedFeature;
    }

    @Override
    public ZoneCode getRegionalSubscriptionIdentifier() {
        return regionalSubscriptionIdentifier;
    }

    @Override
    public boolean getVbsGroupIndication() {
        return vbsGroupIndication;
    }

    @Override
    public boolean getVgcsGroupIndication() {
        return vgcsGroupIndication;
    }

    @Override
    public boolean getCamelSubscriptionInfoWithdraw() {
        return camelSubscriptionInfoWithdraw;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    public GPRSSubscriptionDataWithdraw getGPRSSubscriptionDataWithdraw() {
        return gprsSubscriptionDataWithdraw;
    }

    @Override
    public boolean getRoamingRestrictedInSgsnDueToUnsuppportedFeature() {
        return roamingRestrictedInSgsnDueToUnsuppportedFeature;
    }

    @Override
    public LSAInformationWithdraw getLSAInformationWithdraw() {
        return lsaInformationWithdraw;
    }

    @Override
    public boolean getGmlcListWithdraw() {
        return gmlcListWithdraw;
    }

    @Override
    public boolean getIstInformationWithdraw() {
        return istInformationWithdraw;
    }

    @Override
    public SpecificCSIWithdraw getSpecificCSIWithdraw() {
        return specificCSIWithdraw;
    }

    @Override
    public boolean getChargingCharacteristicsWithdraw() {
        return chargingCharacteristicsWithdraw;
    }

    @Override
    public boolean getStnSrWithdraw() {
        return stnSrWithdraw;
    }

    @Override
    public EPSSubscriptionDataWithdraw getEPSSubscriptionDataWithdraw() {
        return epsSubscriptionDataWithdraw;
    }

    @Override
    public boolean getApnOiReplacementWithdraw() {
        return apnOiReplacementWithdraw;
    }

    @Override
    public boolean getCsgSubscriptionDeleted() {
        return csgSubscriptionDeleted;
    }

    @Override
    public int getTag() throws MAPException {
        return Tag.SEQUENCE;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    @Override
    public boolean getIsPrimitive() {
        return false;
    }

    @Override
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

    @Override
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

        this.imsi = null;
        this.basicServiceList = null;
        this.ssList = null;
        this.roamingRestrictionDueToUnsupportedFeature = false;
        this.regionalSubscriptionIdentifier = null;
        this.vbsGroupIndication = false;
        this.vgcsGroupIndication = false;
        this.camelSubscriptionInfoWithdraw = false;
        this.extensionContainer = null;
        this.gprsSubscriptionDataWithdraw = null;
        this.roamingRestrictedInSgsnDueToUnsuppportedFeature = false;
        this.lsaInformationWithdraw = null;
        this.gmlcListWithdraw = false;
        this.istInformationWithdraw = false;
        this.specificCSIWithdraw = null;
        this.chargingCharacteristicsWithdraw = false;
        this.stnSrWithdraw = false;
        this.epsSubscriptionDataWithdraw = null;
        this.apnOiReplacementWithdraw = false;
        this.csgSubscriptionDeleted = false;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (num) {
            case 0:
                // imsi
                if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive() || tag != _TAG_imsi)
                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                            + ".imsi: Parameter 0 bad tag or tag class or not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                this.imsi = new IMSIImpl();
                ((IMSIImpl) this.imsi).decodeAll(ais);
                break;

            default:
                if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    switch (tag) {
                    case _TAG_basicServiceList:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".basicServiceList: Parameter is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        AsnInputStream ais2 = ais.readSequenceStream();
                        this.basicServiceList = new ArrayList<ExtBasicServiceCode>();
                        while (true) {
                            if (ais2.available() == 0)
                                break;

                            ais2.readTag();

                            ExtBasicServiceCode extBasicServiceCode = new ExtBasicServiceCodeImpl();
                            ((ExtBasicServiceCodeImpl) extBasicServiceCode).decodeAll(ais2);
                            this.basicServiceList.add(extBasicServiceCode);
                        }
                        if (this.basicServiceList.size() < 1 || this.basicServiceList.size() > 70) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter basicServiceList size must be from 1 to 70, found: " + this.ssList.size(),
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        break;

                    case _TAG_ss_List:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".ssList: Parameter is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        ais2 = ais.readSequenceStream();
                        this.ssList = new ArrayList<SSCode>();
                        while (true) {
                            if (ais2.available() == 0)
                                break;

                            int tag2 = ais2.readTag();
                            if (tag2 != Tag.STRING_OCTET || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || !ais2.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": bad ssList element tag or tagClass or is not primitive ", MAPParsingComponentExceptionReason.MistypedParameter);

                            SSCode ssCode = new SSCodeImpl();
                            ((SSCodeImpl) ssCode).decodeAll(ais2);
                            this.ssList.add(ssCode);
                        }
                        if (this.ssList.size() < 1 || this.ssList.size() > 30) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter ssList size must be from 1 to 30, found: " + this.ssList.size(),
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        break;

                    case _TAG_roamingRestrictionDueToUnsupportedFeature:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".roamingRestrictionDueToUnsupportedFeature: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.roamingRestrictionDueToUnsupportedFeature = true;
                        break;
                    case _TAG_regionalSubscriptionIdentifier:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".regionalSubscriptionIdentifier: Parameter regionalSubscriptionIdentifier is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.regionalSubscriptionIdentifier = new ZoneCodeImpl();
                        ((ZoneCodeImpl) this.regionalSubscriptionIdentifier).decodeAll(ais);
                        break;
                    case _TAG_vbsGroupIndication:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding " + _PrimitiveName + ".vbsGroupIndication: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.vbsGroupIndication = true;
                        break;
                    case _TAG_vgcsGroupIndication:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".vgcsGroupIndication: Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.vgcsGroupIndication = true;
                        break;
                    case _TAG_camelSubscriptionInfoWithdraw:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".camelSubscriptionInfoWithdraw: Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.camelSubscriptionInfoWithdraw = true;
                        break;
                    case _TAG_extensionContainer:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".extensionContainer: Parameter extensionContainer is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                        this.extensionContainer = new MAPExtensionContainerImpl();
                        ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                        break;
                    case _TAG_gprsSubscriptionDataWithdraw:
                        ais2 = ais.readSequenceStream();
                        ais2.readTag();
                        this.gprsSubscriptionDataWithdraw = new GPRSSubscriptionDataWithdrawImpl();
                        ((GPRSSubscriptionDataWithdrawImpl) this.gprsSubscriptionDataWithdraw).decodeAll(ais2);
                        break;
                    case _TAG_roamingRestrictedInSgsnDueToUnsuppportedFeature:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".roamingRestrictedInSgsnDueToUnsuppportedFeature: Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.roamingRestrictedInSgsnDueToUnsuppportedFeature = true;
                        break;
                    case _TAG_lsaInformationWithdraw:
                        ais2 = ais.readSequenceStream();
                        ais2.readTag();
                        this.lsaInformationWithdraw = new LSAInformationWithdrawImpl();
                        ((LSAInformationWithdrawImpl) this.lsaInformationWithdraw).decodeAll(ais2);
                        break;
                    case _TAG_gmlc_ListWithdraw:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".gmlcListWithdraw: Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.gmlcListWithdraw = true;
                        break;
                    case _TAG_istInformationWithdraw:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".istInformationWithdraw: Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.istInformationWithdraw = true;
                        break;
                    case _TAG_specificCSI_Withdraw:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".specificCSIWithdraw: Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                        this.specificCSIWithdraw = new SpecificCSIWithdrawImpl();
                        ((SpecificCSIWithdrawImpl) this.specificCSIWithdraw).decodeAll(ais);
                        break;
                    case _TAG_chargingCharacteristicsWithdraw:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".chargingCharacteristicsWithdraw: Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.chargingCharacteristicsWithdraw = true;
                        break;
                    case _TAG_stn_srWithdraw:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".stnSrWithdraw: Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.stnSrWithdraw = true;
                        break;
                    case _TAG_epsSubscriptionDataWithdraw:
                        ais2 = ais.readSequenceStream();
                        ais2.readTag();
                        this.epsSubscriptionDataWithdraw = new EPSSubscriptionDataWithdrawImpl();
                        ((EPSSubscriptionDataWithdrawImpl) this.epsSubscriptionDataWithdraw).decodeAll(ais2);
                        break;
                    case _TAG_apn_oi_replacementWithdraw:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".apnOiReplacementWithdraw: Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.apnOiReplacementWithdraw = true;
                        break;
                    case _TAG_csg_SubscriptionDeleted:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".csgSubscriptionDeleted: Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.csgSubscriptionDeleted = true;
                        break;

                    default:
                        ais.advanceElement();
                        break;
                    }
                } else {

                    ais.advanceElement();
                }
                break;
            }

            num++;
        }

        if (num < 1)
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": Needs at least 1 mandatory parameter, found " + num,
                    MAPParsingComponentExceptionReason.MistypedParameter);
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    @Override
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

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        try {
            if (this.imsi == null)
                throw new MAPException("IMSI parameter must not be null for MAP Version3");

            ((IMSIImpl) this.imsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_imsi);

            if (this.basicServiceList != null) {
                try {
                    asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_basicServiceList);
                    int pos = asnOs.StartContentDefiniteLength();
                    for (ExtBasicServiceCode item : this.basicServiceList) {
                        ((ExtBasicServiceCodeImpl) item).encodeAll(asnOs);
                    }
                    asnOs.FinalizeContent(pos);
                } catch (AsnException e) {
                    throw new MAPException("AsnException when encoding " + _PrimitiveName + ".basicServiceList: " + e.getMessage(), e);
                }
            }

            if (this.ssList != null) {
                try {
                    asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_ss_List);
                    int pos = asnOs.StartContentDefiniteLength();
                    for (SSCode item : this.ssList) {
                        ((SSCodeImpl) item).encodeAll(asnOs);
                    }
                    asnOs.FinalizeContent(pos);
                } catch (AsnException e) {
                    throw new MAPException("AsnException when encoding " + _PrimitiveName + ".ssList: " + e.getMessage(), e);
                }
            }
            if (roamingRestrictionDueToUnsupportedFeature)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_roamingRestrictionDueToUnsupportedFeature);
            if (this.regionalSubscriptionIdentifier != null)
                ((ZoneCodeImpl) this.regionalSubscriptionIdentifier).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_regionalSubscriptionIdentifier);
            if (vbsGroupIndication)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_vbsGroupIndication);
            if (vgcsGroupIndication)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_vgcsGroupIndication);
            if (camelSubscriptionInfoWithdraw)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_camelSubscriptionInfoWithdraw);
            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_extensionContainer);

            if (this.gprsSubscriptionDataWithdraw != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_gprsSubscriptionDataWithdraw);
                int pos = asnOs.StartContentDefiniteLength();
                ((GPRSSubscriptionDataWithdrawImpl) this.gprsSubscriptionDataWithdraw).encodeAll(asnOs);
                asnOs.FinalizeContent(pos);
            }
            if (roamingRestrictedInSgsnDueToUnsuppportedFeature)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_roamingRestrictedInSgsnDueToUnsuppportedFeature);
            if (this.lsaInformationWithdraw != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_lsaInformationWithdraw);
                int pos = asnOs.StartContentDefiniteLength();
                ((LSAInformationWithdrawImpl) this.lsaInformationWithdraw).encodeAll(asnOs);
                asnOs.FinalizeContent(pos);
            }
            if (gmlcListWithdraw)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_gmlc_ListWithdraw);
            if (istInformationWithdraw)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_istInformationWithdraw);
            if (this.specificCSIWithdraw != null)
                ((SpecificCSIWithdrawImpl) this.specificCSIWithdraw).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_specificCSI_Withdraw);
            if (chargingCharacteristicsWithdraw)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_chargingCharacteristicsWithdraw);
            if (stnSrWithdraw)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_stn_srWithdraw);
            if (this.epsSubscriptionDataWithdraw != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_epsSubscriptionDataWithdraw);
                int pos = asnOs.StartContentDefiniteLength();
                ((EPSSubscriptionDataWithdrawImpl) this.epsSubscriptionDataWithdraw).encodeAll(asnOs);
                asnOs.FinalizeContent(pos);
            }
            if (apnOiReplacementWithdraw)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_apn_oi_replacementWithdraw);
            if (csgSubscriptionDeleted)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_csg_SubscriptionDeleted);

        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.imsi != null) {
            sb.append("imsi=");
            sb.append(imsi.toString());
            sb.append(", ");
        }
        if (this.basicServiceList != null) {
            sb.append("basicServiceList=[");
            boolean firstItem = true;
            for (ExtBasicServiceCode be : this.basicServiceList) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }
        if (this.ssList != null) {
            sb.append("ssList=[");
            boolean firstItem = true;
            for (SSCode be : this.ssList) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }
        if (this.roamingRestrictionDueToUnsupportedFeature) {
            sb.append("roamingRestrictionDueToUnsupportedFeature, ");
        }
        if (this.regionalSubscriptionIdentifier != null) {
            sb.append("regionalSubscriptionIdentifier=");
            sb.append(regionalSubscriptionIdentifier);
            sb.append(", ");
        }
        if (this.vbsGroupIndication) {
            sb.append("vbsGroupIndication, ");
        }
        if (this.vgcsGroupIndication) {
            sb.append("vgcsGroupIndication, ");
        }
        if (this.camelSubscriptionInfoWithdraw) {
            sb.append("camelSubscriptionInfoWithdraw, ");
        }
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(extensionContainer);
            sb.append(", ");
        }
        if (this.gprsSubscriptionDataWithdraw != null) {
            sb.append("gprsSubscriptionDataWithdraw=");
            sb.append(gprsSubscriptionDataWithdraw);
            sb.append(", ");
        }
        if (this.roamingRestrictedInSgsnDueToUnsuppportedFeature) {
            sb.append("roamingRestrictedInSgsnDueToUnsuppportedFeature, ");
        }
        if (this.lsaInformationWithdraw != null) {
            sb.append("lsaInformationWithdraw=");
            sb.append(lsaInformationWithdraw);
            sb.append(", ");
        }
        if (this.gmlcListWithdraw) {
            sb.append("gmlcListWithdraw, ");
        }
        if (this.istInformationWithdraw) {
            sb.append("istInformationWithdraw, ");
        }
        if (this.specificCSIWithdraw != null) {
            sb.append("specificCSIWithdraw=");
            sb.append(specificCSIWithdraw);
            sb.append(", ");
        }
        if (this.chargingCharacteristicsWithdraw) {
            sb.append("chargingCharacteristicsWithdraw, ");
        }
        if (this.stnSrWithdraw) {
            sb.append("stnSrWithdraw, ");
        }
        if (this.epsSubscriptionDataWithdraw != null) {
            sb.append("epsSubscriptionDataWithdraw=");
            sb.append(epsSubscriptionDataWithdraw);
            sb.append(", ");
        }
        if (this.apnOiReplacementWithdraw) {
            sb.append("apnOiReplacementWithdraw, ");
        }
        if (this.csgSubscriptionDeleted) {
            sb.append("csgSubscriptionDeleted, ");
        }

        sb.append("]");

        return sb.toString();
    }

}
