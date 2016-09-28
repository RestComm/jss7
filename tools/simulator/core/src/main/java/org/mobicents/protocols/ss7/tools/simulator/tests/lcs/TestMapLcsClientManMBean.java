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


package org.mobicents.protocols.ss7.tools.simulator.tests.lcs;

import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;


/**
 * @author falonso@csc.com
 *
 */
public interface TestMapLcsClientManMBean {

    // Operations

    String performSendRoutingInfoForLCSRequest(String addressIMSI);

    String performSubscriberLocationReportRequest();

    String performProvideSubscriberLocationRequest();

    // Attributes

    AddressNatureType getAddressNature();

    void setAddressNature(AddressNatureType val);

    String getNumberingPlan();

    void setNumberingPlan(String numPlan);

    NumberingPlanMapType getNumberingPlanType();

    void setNumberingPlanType(NumberingPlanMapType val);

    // SLR Response
    String getNaESRDAddress();
    void setNaESRDAddress(String address);

    // PSL Request
    LocationEstimateTypeEnumerated getLocEstimateType();
    void setLocEstimateType(LocationEstimateTypeEnumerated locEstimate);

    // SLR Request
    String getNetworkNodeNumberAddress();
    void setNetworkNodeNumberAddress(String val);
    String getHGMLCAddress();
    void setHGMLCAddress(String hgmlcAddress);
    String getIMEI();
    void setIMEI(String imei);
    String getIMSI();
    void setIMSI(String imsi);
    Integer getCellId();
    void setCellId(Integer lac);
    Integer getLAC();
    void setAgeOfLocationEstimate(Integer ageOfLocationEstimate);
    Integer getAgeOfLocationEstimate();
    void setLAC(Integer lac);
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
    LCSClientTypeEnumerated getLcsClientType();
    void setLcsClientType(LCSClientTypeEnumerated lcsClientType);
    void setCodeWordUSSDString(String codeWordUSSDString);
    String getCodeWordUSSDString();
    void setCallSessionUnrelated(PrivacyCheckRelatedActionEnumerated val);
    PrivacyCheckRelatedActionEnumerated getCallSessionUnrelated();
    void setCallSessionRelated(PrivacyCheckRelatedActionEnumerated val);
    PrivacyCheckRelatedActionEnumerated getCallSessionRelated();
    void setAreaType(AreaTypeEnumerated val);
    AreaTypeEnumerated getAreaType();
    void setReportingAmmount(Integer val);
    Integer getReportingAmmount();
    void setReportingInterval(Integer val);
    Integer getReportingInterval();
    void setDataCodingScheme(Integer val);
    Integer getDataCodingScheme();

    // Other

    String getCurrentRequestDef();

    // Methods for configurable properties via HTTP interface for values that are based on EnumeratedBase abstract class

    void putAddressNature(String val);

    void putNumberingPlanType(String val);

    void putLCSEventType(String val);

}
