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

package org.mobicents.protocols.ss7.tools.simulator.tests.sms;

import javolution.text.CharArray;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.mobicents.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.mobicents.protocols.ss7.tools.simulator.level3.MapProtocolVersion;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TestSmsClientConfigurationData {

    protected static final String ADDRESS_NATURE = "addressNature";
    protected static final String NUMBERING_PLAN = "numberingPlan";
    protected static final String SERVICE_CENTER_ADDRESS = "serviceCenterAddress";
    protected static final String MAP_PROTOCOL_VERSION = "mapProtocolVersion";
    protected static final String SRI_RESPONSE_IMSI = "sriResponseImsi";
    protected static final String SRI_RESPONSE_VLR = "sriResponseVlr";
    protected static final String SMSC_SSN = "smscSsn";
    protected static final String NATIONAL_LANGUAGE_CODE = "nationalLanguageCode";
    protected static final String TYPE_OF_NUMBER = "typeOfNumber";
    protected static final String NUMBERING_PLAN_IDENTIFICATION = "numberingPlanIdentification";
    protected static final String SMS_CODING_TYPE = "smsCodingType";
    protected static final String SRI_REACTION = "sriReaction";
    protected static final String SRI_INFORM_SERVICE_CENTER = "sriInformServiceCenter";
    protected static final String SRI_SC_ADDRESS_NOT_INCLUDED = "sriScAddressNotIncluded";
    protected static final String MT_FSM_REACTION = "mtFSMReaction";
    protected static final String ESM_DEL_STAT = "esmDelStat";
    protected static final String ONE_NOTIFICATION_FOR_100_DIALOGS = "oneNotificationFor100Dialogs";
    protected static final String RETURN_20_PERS_DELIVERY_ERRORS = "return20PersDeliveryErrors";
    protected static final String CONTINUE_DIALOG = "continueDialog";
    protected static final String STATUS_REPORT_REQUEST = "statusReportRequest";

    protected AddressNature addressNature = AddressNature.international_number;
    protected NumberingPlan numberingPlan = NumberingPlan.ISDN;
    protected String serviceCenterAddress = "";
    protected MapProtocolVersion mapProtocolVersion = new MapProtocolVersion(MapProtocolVersion.VAL_MAP_V3);
    protected String sriResponseImsi = "";
    protected String sriResponseVlr = "";
    protected int smscSsn = 8;
    protected TypeOfNumber typeOfNumber = TypeOfNumber.InternationalNumber;
    protected NumberingPlanIdentification numberingPlanIdentification = NumberingPlanIdentification.ISDNTelephoneNumberingPlan;
    protected SmsCodingType smsCodingType = new SmsCodingType(SmsCodingType.VAL_GSM7);
    protected int nationalLanguageCode = 0;
    protected boolean statusReportRequest = false;

    protected SRIReaction sriReaction = new SRIReaction(SRIReaction.VAL_RETURN_SUCCESS);
    protected SRIInformServiceCenter sriInformServiceCenter = new SRIInformServiceCenter(SRIInformServiceCenter.MWD_NO);
    protected boolean sriScAddressNotIncluded = false;
    protected MtFSMReaction mtFSMReaction = new MtFSMReaction(MtFSMReaction.VAL_RETURN_SUCCESS);
    protected ReportSMDeliveryStatusReaction reportSMDeliveryStatusReaction = new ReportSMDeliveryStatusReaction(
            ReportSMDeliveryStatusReaction.VAL_RETURN_SUCCESS);
    protected boolean oneNotificationFor100Dialogs = false;
    protected boolean return20PersDeliveryErrors = false;
    protected boolean continueDialog = false;

    public AddressNature getAddressNature() {
        return addressNature;
    }

    public void setAddressNature(AddressNature addressNature) {
        this.addressNature = addressNature;
    }

    public NumberingPlan getNumberingPlan() {
        return numberingPlan;
    }

    public void setNumberingPlan(NumberingPlan numberingPlan) {
        this.numberingPlan = numberingPlan;
    }

    public String getServiceCenterAddress() {
        return serviceCenterAddress;
    }

    public void setServiceCenterAddress(String serviceCenterAddress) {
        this.serviceCenterAddress = serviceCenterAddress;
    }

    public MapProtocolVersion getMapProtocolVersion() {
        return mapProtocolVersion;
    }

    public void setMapProtocolVersion(MapProtocolVersion mapProtocolVersion) {
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public SRIReaction getSRIReaction() {
        return sriReaction;
    }

    public void setSRIReaction(SRIReaction val) {
        sriReaction = val;
    }

    public SRIInformServiceCenter getSRIInformServiceCenter() {
        return sriInformServiceCenter;
    }

    public void setSRIInformServiceCenter(SRIInformServiceCenter val) {
        sriInformServiceCenter = val;
    }

    public boolean isSRIScAddressNotIncluded() {
        return sriScAddressNotIncluded;
    }

    public void setSRIScAddressNotIncluded(boolean val) {
        sriScAddressNotIncluded = val;
    }

    public MtFSMReaction getMtFSMReaction() {
        return mtFSMReaction;
    }

    public void setMtFSMReaction(MtFSMReaction val) {
        mtFSMReaction = val;
    }

    public ReportSMDeliveryStatusReaction getReportSMDeliveryStatusReaction() {
        return reportSMDeliveryStatusReaction;
    }

    public void setReportSMDeliveryStatusReaction(ReportSMDeliveryStatusReaction val) {
        reportSMDeliveryStatusReaction = val;
    }

    public String getSriResponseImsi() {
        return sriResponseImsi;
    }

    public void setSriResponseImsi(String sriResponseImsi) {
        this.sriResponseImsi = sriResponseImsi;
    }

    public String getSriResponseVlr() {
        return sriResponseVlr;
    }

    public void setSriResponseVlr(String sriResponseVlr) {
        this.sriResponseVlr = sriResponseVlr;
    }

    public int getSmscSsn() {
        return smscSsn;
    }

    public void setSmscSsn(int smscSsn) {
        this.smscSsn = smscSsn;
    }

    public int getNationalLanguageCode() {
        return nationalLanguageCode;
    }

    public void setNationalLanguageCode(int nationalLanguageCode) {
        this.nationalLanguageCode = nationalLanguageCode;
    }

    public boolean isStatusReportRequest() {
        return statusReportRequest;
    }

    public void setStatusReportRequest(boolean statusReportRequest) {
        this.statusReportRequest = statusReportRequest;
    }

    public TypeOfNumber getTypeOfNumber() {
        return typeOfNumber;
    }

    public void setTypeOfNumber(TypeOfNumber typeOfNumber) {
        this.typeOfNumber = typeOfNumber;
    }

    public NumberingPlanIdentification getNumberingPlanIdentification() {
        return numberingPlanIdentification;
    }

    public void setNumberingPlanIdentification(NumberingPlanIdentification numberingPlanIdentification) {
        this.numberingPlanIdentification = numberingPlanIdentification;
    }

    public SmsCodingType getSmsCodingType() {
        return smsCodingType;
    }

    public void setSmsCodingType(SmsCodingType smsCodingType) {
        this.smsCodingType = smsCodingType;
    }

    public boolean isOneNotificationFor100Dialogs() {
        return oneNotificationFor100Dialogs;
    }

    public void setOneNotificationFor100Dialogs(boolean oneNotificationFor100Dialogs) {
        this.oneNotificationFor100Dialogs = oneNotificationFor100Dialogs;
    }

    public boolean isReturn20PersDeliveryErrors() {
        return return20PersDeliveryErrors;
    }

    public void setReturn20PersDeliveryErrors(boolean val) {
        this.return20PersDeliveryErrors = val;
    }

    public boolean isContinueDialog() {
        return this.continueDialog;
    }

    public void setContinueDialog(boolean val) {
        this.continueDialog = val;
    }

    protected static final XMLFormat<TestSmsClientConfigurationData> XML = new XMLFormat<TestSmsClientConfigurationData>(TestSmsClientConfigurationData.class) {

        public void write(TestSmsClientConfigurationData clt, OutputElement xml) throws XMLStreamException {
            xml.setAttribute(SMSC_SSN, clt.smscSsn);
            xml.setAttribute(NATIONAL_LANGUAGE_CODE, clt.nationalLanguageCode);
            xml.setAttribute(SRI_SC_ADDRESS_NOT_INCLUDED, clt.sriScAddressNotIncluded);
            xml.setAttribute(STATUS_REPORT_REQUEST, clt.statusReportRequest);

            xml.setAttribute(ONE_NOTIFICATION_FOR_100_DIALOGS, clt.oneNotificationFor100Dialogs);
            xml.setAttribute(RETURN_20_PERS_DELIVERY_ERRORS, clt.return20PersDeliveryErrors);
            xml.setAttribute(CONTINUE_DIALOG, clt.continueDialog);

            xml.add(clt.serviceCenterAddress, SERVICE_CENTER_ADDRESS, String.class);
            xml.add(clt.sriResponseImsi, SRI_RESPONSE_IMSI, String.class);
            xml.add(clt.sriResponseVlr, SRI_RESPONSE_VLR, String.class);

            xml.add(clt.addressNature.toString(), ADDRESS_NATURE, String.class);
            xml.add(clt.numberingPlan.toString(), NUMBERING_PLAN, String.class);
            xml.add(clt.mapProtocolVersion.toString(), MAP_PROTOCOL_VERSION, String.class);
            xml.add(clt.typeOfNumber.toString(), TYPE_OF_NUMBER, String.class);
            xml.add(clt.numberingPlanIdentification.toString(), NUMBERING_PLAN_IDENTIFICATION, String.class);
            xml.add(clt.smsCodingType.toString(), SMS_CODING_TYPE, String.class);

            xml.add(clt.sriReaction.toString(), SRI_REACTION, String.class);
            xml.add(clt.sriInformServiceCenter.toString(), SRI_INFORM_SERVICE_CENTER, String.class);
            xml.add(clt.reportSMDeliveryStatusReaction.toString(), ESM_DEL_STAT, String.class);
            xml.add(clt.mtFSMReaction.toString(), MT_FSM_REACTION, String.class);
        }

        public void read(InputElement xml, TestSmsClientConfigurationData clt) throws XMLStreamException {
            clt.smscSsn = xml.getAttribute(SMSC_SSN).toInt();
            CharArray val = xml.getAttribute(NATIONAL_LANGUAGE_CODE);
            if (val != null)
                clt.nationalLanguageCode = val.toInt();
            clt.sriScAddressNotIncluded = xml.getAttribute(SRI_SC_ADDRESS_NOT_INCLUDED).toBoolean();
            val = xml.getAttribute(STATUS_REPORT_REQUEST);
            if (val != null)
                clt.statusReportRequest = val.toBoolean();

            CharArray chArr = xml.getAttribute(ONE_NOTIFICATION_FOR_100_DIALOGS);
            if (chArr != null)
                clt.oneNotificationFor100Dialogs = chArr.toBoolean();
            chArr = xml.getAttribute(RETURN_20_PERS_DELIVERY_ERRORS);
            if (chArr != null)
                clt.return20PersDeliveryErrors = chArr.toBoolean();
            chArr = xml.getAttribute(CONTINUE_DIALOG);
            if (chArr != null)
                clt.continueDialog = chArr.toBoolean();

            clt.serviceCenterAddress = (String) xml.get(SERVICE_CENTER_ADDRESS, String.class);
            clt.sriResponseImsi = (String) xml.get(SRI_RESPONSE_IMSI, String.class);
            clt.sriResponseVlr = (String) xml.get(SRI_RESPONSE_VLR, String.class);

            String an = (String) xml.get(ADDRESS_NATURE, String.class);
            clt.addressNature = AddressNature.valueOf(an);
            String np = (String) xml.get(NUMBERING_PLAN, String.class);
            clt.numberingPlan = NumberingPlan.valueOf(np);
            String mpv = (String) xml.get(MAP_PROTOCOL_VERSION, String.class);
            clt.mapProtocolVersion = MapProtocolVersion.createInstance(mpv);
            String ton = (String) xml.get(TYPE_OF_NUMBER, String.class);
            clt.typeOfNumber = TypeOfNumber.valueOf(ton);
            String npi = (String) xml.get(NUMBERING_PLAN_IDENTIFICATION, String.class);
            clt.numberingPlanIdentification = NumberingPlanIdentification.valueOf(npi);
            String sct = (String) xml.get(SMS_CODING_TYPE, String.class);
            clt.smsCodingType = SmsCodingType.createInstance(sct);

            String sriR = (String) xml.get(SRI_REACTION, String.class);
            clt.sriReaction = SRIReaction.createInstance(sriR);
            String sriIsc = (String) xml.get(SRI_INFORM_SERVICE_CENTER, String.class);
            clt.sriInformServiceCenter = SRIInformServiceCenter.createInstance(sriIsc);
            String esmDelStat = (String) xml.get(ESM_DEL_STAT, String.class);
            clt.reportSMDeliveryStatusReaction = ReportSMDeliveryStatusReaction.createInstance(esmDelStat);
            String mtFsmR = (String) xml.get(MT_FSM_REACTION, String.class);
            clt.mtFSMReaction = MtFSMReaction.createInstance(mtFsmR);
        }
    };

}
