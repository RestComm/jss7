/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2018, Telestax Inc and individual contributors
 * by the @authors tag.
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

package org.restcomm.protocols.ss7.tools.simulator.tests.lcs;

import org.restcomm.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.restcomm.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationRequest;
import org.restcomm.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.restcomm.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 * @author <a href="mailto:falonso@csc.om"> Fernando Alonso </a>
 */

public interface TestLcsServerManMBean {

    //Operations
    String performSendRoutingInfoForLCSResponse();

    void onProvideSubscriberLocationRequest(ProvideSubscriberLocationRequest provideSubscriberLocationRequest);

    String performSubscriberLocationReportRequest();

    //Attributes

    AddressNatureType getAddressNature();

    String getNumberingPlan();

    NumberingPlanMapType getNumberingPlanType();

    void setAddressNature(AddressNatureType addressNatureType);

    void setNumberingPlan(String numberingPlan);

    void setNumberingPlanType(NumberingPlanMapType numberingPlanMapType);

    String getMlcNumber();

    void setMlcNumber(String mlcNumber);

    // PSL Request
    Double getLocationEstimateLatitude();

    void setLocationEstimateLatitude(Double locationEstimateLatitude);

    Double getLocationEstimateLongitude();

    void setLocationEstimateLongitude(Double locationEstimateLongitude);

    TypeOfShapeEnumerated getTypeOfShape();

    void setTypeOfShapeEnumerated(TypeOfShapeEnumerated typeOfShapeEnumerated);

    LocationEstimateTypeEnumerated getLocEstimateType();

    void setLocEstimateType(LocationEstimateTypeEnumerated locEstimate);

    // SLR Request (some apply to PSL too)
    String getNetworkNodeNumber();

    void setNetworkNodeNumber(String val);

    String getHGMLCAddress();

    void setHGMLCAddress(String hgmlcAddress);

    String getIMEI();

    void setIMEI(String imei);

    String getIMSI();

    void setIMSI(String imsi);

    String getLMSI();

    void setLMSI(String lmsi);

    Integer getCellId();

    void setCellId(Integer lac);

    Integer getLAC();

    void setLAC(Integer lac);

    void setAgeOfLocationEstimate(Integer ageOfLocationEstimate);

    Integer getAgeOfLocationEstimate();

    LCSEvent getLCSEvent();

    void setLCSEvent(LCSEvent lcsEvent);

    LCSEventType getLCSEventType();

    void setLCSEventType(LCSEventType val);

    Integer getLCSReferenceNumber();

    void setLCSReferenceNumber(Integer lcsReferenceNumber);

    Integer getMCC();

    void setMCC(Integer mcc);

    Integer getMNC();

    void setMNC(Integer mnc);

    String getMSISDN();

    void setMSISDN(String msisdn);

    Integer getLcsServiceTypeID();

    void setLcsServiceTypeID(Integer lcsServiceTypeID);

    boolean getMoLrShortCircuitIndicator();

    void setMoLrShortCircuitIndicator(boolean moLrShortCircuitIndicator);

    LCSClientTypeEnumerated getLcsClientTypeEnumerated();

    void setLcsClientTypeEnumerated(LCSClientTypeEnumerated lcsClientType);

    void setCodeWordUSSDString(String codeWordUSSDString);

    String getCodeWordUSSDString();

    void setCallSessionUnrelated(PrivacyCheckRelatedActionEnumerated privacyCheckRelatedActionEnumerated);

    PrivacyCheckRelatedActionEnumerated getCallSessionUnrelated();

    void setCallSessionRelated(PrivacyCheckRelatedActionEnumerated privacyCheckRelatedActionEnumerated);

    PrivacyCheckRelatedActionEnumerated getCallSessionRelated();

    void setAreaType(AreaTypeEnumerated areaTypeEnumerated);

    AreaTypeEnumerated getAreaType();

    void setOccurrenceInfo(OccurrenceInfoEnumerated occurrenceInfoEnumerated);

    OccurrenceInfoEnumerated getOccurrenceInfo();

    void setIntervalTime(Integer intervalTime);

    Integer getIntervalTime();

    void setReportingAmount(Integer reportingAmount);

    Integer getReportingAmount();

    void setReportingInterval(Integer reportingInterval);

    Integer getReportingInterval();

    void setDataCodingScheme(Integer dataCodingScheme);

    Integer getDataCodingScheme();

    // SLR Response
    String getNaESRDAddress();

    void setNaESRDAddress(String address);

    // Other

    SRIforLCSReaction getSRIforLCSReaction();

    String getSRIforLCSReaction_Value();

    void setSRIforLCSReaction(SRIforLCSReaction val);

    void putSRIforLCSReaction(String val);

    PSLReaction getPSLReaction();

    String getPSLReaction_Value();

    void setPSLReaction(PSLReaction val);

    void putPSLReaction(String val);

    SLRReaction getSLRReaction();

    String getSLRReaction_Value();

    void setSLRReaction(SLRReaction val);

    void putSLRReaction(String val);

    String getCurrentRequestDef();

    // Methods for configurable properties via HTTP interface for values that are based on EnumeratedBase abstract class

    void putAddressNature(String addressNature);

    void putNumberingPlanType(String NumberingPlanType);

    void putLCSEventType(String lcsEventType);

}

