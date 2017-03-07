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

package org.mobicents.protocols.ss7.tools.simulator.tests.cap;

import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.common.CapApplicationContextSsf;
import org.mobicents.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;

/**
 *
 * @author sergey vetyutnev
 *
 */
public interface TestCapSsfManMBean {

    // Operations

    String performInitialDp(String msg);

    String performAssistRequestInstructions(String msg);

    String performApplyChargingReport(String msg);

    String performEventReportBCSM(String msg);

    String closeCurrentDialog();


    // Attributes

    CapApplicationContextSsf getCapApplicationContext();

    String getCapApplicationContext_Value();

    void setCapApplicationContext(CapApplicationContextSsf val);

    void putCapApplicationContext(String val);

    int getServiceKey();
    void setServiceKey(int serviceKey);

    EventTypeBCSMType getIdpEventTypeBCSM();
    String getIdpEventTypeBCSM_Value();
    void setIdpEventTypeBCSM(EventTypeBCSMType value);

    boolean isUseCldInsteadOfCldBCDNumber();
    void setUseCldInsteadOfCldBCDNumber(boolean value);

    String getCallingPartyNumberAddress();
    void setCallingPartyNumberAddress(String callingPartyNumberAddress);
    IsupNatureOfAddressIndicatorType getCallingPartyNumberNatureOfAddress();
    String getCallingPartyNumberNatureOfAddress_Value();
    void setCallingPartyNumberNatureOfAddress(IsupNatureOfAddressIndicatorType callingPartyNumberNature);
    IsupNumberingPlanIndicatorType getCallingPartyNumberNumberingPlan();
    String getCallingPartyNumberNumberingPlan_Value();
    void setCallingPartyNumberNumberingPlan(IsupNumberingPlanIndicatorType callingPartyNumberPlan);

    String getCalledPartyBCDNumberAddress();
    void setCalledPartyBCDNumberAddress(String calledPartyBCDNumberAddress);
    AddressNatureType getCalledPartyBCDNumberAddressNature();
    String getCalledPartyBCDNumberAddressNature_Value();
    void setCalledPartyBCDNumberAddressNature(AddressNatureType val);
    NumberingPlanMapType getCalledPartyBCDNumberNumberingPlan();
    String getCalledPartyBCDNumberNumberingPlan_Value();
    void setCalledPartyBCDNumberNumberingPlan(NumberingPlanMapType val);

    String getCalledPartyNumberAddress();
    void setCalledPartyNumberAddress(String calledPartyNumberAddress);
    IsupNatureOfAddressIndicatorType getCalledPartyNumberNatureOfAddress();
    String getCalledPartyNumberNatureOfAddress_Value();
    void setCalledPartyNumberNatureOfAddress(IsupNatureOfAddressIndicatorType calledPartyNumberNatureOfAddress);
    IsupNumberingPlanIndicatorType getCalledPartyNumberNumberingPlan();
    String getCalledPartyNumberNumberingPlan_Value();
    void setCalledPartyNumberNumberingPlan(IsupNumberingPlanIndicatorType calledPartyNumberNumberingPlan);

    String getMscAddressAddress();
    void setMscAddressAddress(String mscAddressAddress);
    AddressNatureType getMscAddressNatureOfAddress();
    String getMscAddressNatureOfAddress_Value();
    void setMscAddressNatureOfAddress(AddressNatureType val);
    NumberingPlanMapType getMscAddressNumberingPlan();
    String getMscAddressNumberingPlan_Value();
    void setMscAddressNumberingPlan(NumberingPlanMapType val);


    // Other

    String getCurrentRequestDef();

    // Methods for configurable properties via HTTP interface for values that are based on EnumeratedBase abstract class

    void putCallingPartyNumberNatureOfAddress(String value);
    void putCallingPartyNumberNumberingPlan(String value);
    void putCalledPartyNumberNatureOfAddress(String value);
    void putCalledPartyNumberNumberingPlan(String value);
    void putIdpEventTypeBCSM(String value);
    void putCalledPartyBCDNumberAddressNature(String val);
    void putCalledPartyBCDNumberNumberingPlan(String val);
    void putMscAddressNatureOfAddress(String val);
    void putMscAddressNumberingPlan(String val);

}
