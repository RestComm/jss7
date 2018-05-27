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

import org.restcomm.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.restcomm.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 * @author <a href="mailto:falonso@csc.om"> Fernando Alonso </a>
 */
public interface TestLcsClientManMBean {

    // Operations
    String performSendRoutingInfoForLCSRequest();

    String performProvideSubscriberLocationRequest();

    String performSubscriberLocationReportRequest();

    String performSubscriberLocationReportResponse();

    // Other
    String getCurrentRequestDef();

    // Attributes
    String getNetworkNodeNumber();

    void setNetworkNodeNumber(String address);

    AddressNatureType getAddressNature();

    NumberingPlanMapType getNumberingPlanType();

    void setAddressNature(AddressNatureType val);

    void setNumberingPlanType(NumberingPlanMapType val);

    String getMSISDN();

    void setMSISDN(String msisdn);

    String getIMSI();

    void setIMSI(String imsi);

    Integer getCellId();

    void setCellId(Integer lac);

    String getIMEI();

    void setIMEI(String imei);

    String getHGMLCAddress();

    void setHGMLCAddress(String hgmlcAddress);

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

    String getNaESRDAddress();

    void setNaESRDAddress(String address);

    String getNaESRKAddress();

    void setNaESRKAddress(String address);

    // Methods for configurable properties via HTTP interface for values that are based on EnumeratedBase abstract class

    void putAddressNature(String val);

    void putNumberingPlanType(String val);

    void putLCSEventType(String val);


}

