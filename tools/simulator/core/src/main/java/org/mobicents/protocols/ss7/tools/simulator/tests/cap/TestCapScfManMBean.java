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

import org.mobicents.protocols.ss7.tools.simulator.common.CapApplicationContextScf;

/**
 *
 * @author sergey vetyutnev
 *
 */
public interface TestCapScfManMBean {

    // Operations

    String closeCurrentDialog();

    String performInitiateCallAttempt(String msg);

    String performApplyCharging(String msg);

    String performCancel(String msg);

    String performConnect(String msg);

    String performContinue(String msg);

    String performReleaseCall(String msg);

    String performRequestReportBCSMEvent(String msg);

    String performConnectToResource(String msg);

    String performFurnishChargingInformation(String msg);

    String performPromptAndCollectUserInformation(String msg);

    String performActivityTest(String msg);

    // Attributes

    CapApplicationContextScf getCapApplicationContext();

    String getCapApplicationContext_Value();

    void setCapApplicationContext(CapApplicationContextScf val);

    String getConnectDestinationRoutingAddressAddress();
    void setConnectDestinationRoutingAddressAddress(String ConDestRouteAddrAddress);
    IsupNatureOfAddressIndicatorType getConnectDestinationRoutingAddressNatureOfAddress();
    String getConnectDestinationRoutingAddressNatureOfAddress_Value();
    void setConnectDestinationRoutingAddressNatureOfAddress(IsupNatureOfAddressIndicatorType value);
    IsupNumberingPlanIndicatorType getConnectDestinationRoutingAddressNumberingPlan();
    String getConnectDestinationRoutingAddressNumberingPlan_Value();
    void setConnectDestinationRoutingAddressNumberingPlan(IsupNumberingPlanIndicatorType value);

    IsupCauseIndicatorCauseValueType getReleaseCauseValue();
    String getReleaseCauseValue_Value();
    void setReleaseCauseValue(IsupCauseIndicatorCauseValueType value);

    IsupCauseIndicatorCodingStandardType getReleaseCauseCodingStandardIndicator();
    String getReleaseCauseCodingStandardIndicator_Value();
    void setReleaseCauseCodingStandardIndicator(IsupCauseIndicatorCodingStandardType value);

    IsupCauseIndicatorLocationType getReleaseCauseLocationIndicator();
    String getReleaseCauseLocationIndicator_Value();
    void setReleaseCauseLocationIndicator(IsupCauseIndicatorLocationType value);

    // Other

    String getCurrentRequestDef();

    // Methods for configurable properties via HTTP interface for values that are based on EnumeratedBase abstract class

    void putCapApplicationContext(String val);

    void putConDestRouteAddrNatureOfAddress(String value);

    void putConDestRouteAddrNumberingPlan(String value);

    void putReleaseCauseValue(String value);

    void putReleaseCauseCodingStandardIndicator(String value);

    void putReleaseCauseLocationIndicator(String value);
}
