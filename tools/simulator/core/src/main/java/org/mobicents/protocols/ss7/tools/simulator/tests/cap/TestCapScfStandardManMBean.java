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

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

import org.mobicents.protocols.ss7.tools.simulator.common.CapApplicationContextScf;


/**
 *
 * @author sergey vetyutnev
 *
 */
public class TestCapScfStandardManMBean extends StandardMBean {

    public TestCapScfStandardManMBean(TestCapScfMan impl, Class<TestCapScfManMBean> intf) throws NotCompliantMBeanException {
        super(impl, intf);
    }

    @Override
    public MBeanInfo getMBeanInfo() {

        MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[] {

                new MBeanAttributeInfo("CapApplicationContext", CapApplicationContextScf.class.getName(),
                        "CAP application context", true, true, false),
                new MBeanAttributeInfo("CapApplicationContext_Value", String.class.getName(), "CAP application context", true,
                        false, false),
                new MBeanAttributeInfo("CurrentRequestDef", String.class.getName(), "Get information of the current request",
                        true, false, false),

                new MBeanAttributeInfo("ConnectDestinationRoutingAddressAddress", String.class.getName(),
                        "Address value of destinationRoutingAddress parameter in CONNECT operation message", true, true, false),


                new MBeanAttributeInfo("ConnectDestinationRoutingAddressNatureOfAddress", IsupNatureOfAddressIndicatorType.class.getName(),
                        "Isup Nature Of Address Indicator for Connect.destinationRoutingAddress parameter creation",
                        true, true, false),
                new MBeanAttributeInfo("ConnectDestinationRoutingAddressNatureOfAddress_Value", String.class.getName(),
                        "Isup Nature Of Address Indicator for Connect.destinationRoutingAddress parameter creation",
                        true, false, false),

                new MBeanAttributeInfo("ConnectDestinationRoutingAddressNumberingPlan", IsupNumberingPlanIndicatorType.class.getName(),
                        "Isup Numbering Plan Indicator for Connect.destinationRoutingAddress parameter creation",
                        true, true, false),
                new MBeanAttributeInfo("ConnectDestinationRoutingAddressNumberingPlan_Value", String.class.getName(),
                        "Isup Numbering Plan Indicator for Connect.destinationRoutingAddress parameter creation",
                        true, false, false),

                new MBeanAttributeInfo("ReleaseCauseValue", IsupNumberingPlanIndicatorType.class.getName(),
                        "Isup Cause Value for Release message parameter creation",
                        true, true, false),
                new MBeanAttributeInfo("ReleaseCauseValue_Value", String.class.getName(),
                        "Isup Cause Value for Release message parameter creation",
                        true, false, false),

                new MBeanAttributeInfo("ReleaseCauseCodingStandardIndicator", IsupNumberingPlanIndicatorType.class.getName(),
                        "Isup Coding Standard Cause Indicator value for Release message parameter creation",
                        true, true, false),
                new MBeanAttributeInfo("ReleaseCauseCodingStandardIndicator_Value", String.class.getName(),
                        "Isup Coding Standard Cause Indicator value for Release message parameter creation",
                        true, false, false),

                new MBeanAttributeInfo("ReleaseCauseLocationIndicator", IsupNumberingPlanIndicatorType.class.getName(),
                        "Isup Location Cause Indicator value for Release message parameter creation",
                        true, true, false),
                new MBeanAttributeInfo("ReleaseCauseLocationIndicator_Value", String.class.getName(),
                        "Isup Location Cause Indicator value for Release message parameter creation",
                        true, false, false),

        };

        MBeanParameterInfo[] signString = new MBeanParameterInfo[] { new MBeanParameterInfo("val", String.class.getName(),
                "Index number or value") };


        MBeanOperationInfo[] operations = new MBeanOperationInfo[] {

                new MBeanOperationInfo("closeCurrentDialog", "Closing the current dialog", null, String.class.getName(),
                        MBeanOperationInfo.ACTION),

                new MBeanOperationInfo("performInitiateCallAttempt", "Open a new Dialog and sending InitiateCallAttempt", null,
                        String.class.getName(), MBeanOperationInfo.ACTION),

                new MBeanOperationInfo("performApplyCharging", "Sending ApplyCharging", null, String.class.getName(),
                        MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("performCancel", "Sending Cancel", null, String.class.getName(),
                        MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("performConnect", "Sending Connect", null, String.class.getName(),
                        MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("performContinue", "Sending Continue", null, String.class.getName(),
                        MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("performReleaseCall", "Sending ReleaseCall", null, String.class.getName(),
                        MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("performRequestReportBCSMEvent", "Sending RequestReportBCSMEvent", null,
                        String.class.getName(), MBeanOperationInfo.ACTION),

                new MBeanOperationInfo("performConnectToResource", "Sending ConnectToResource", null, String.class.getName(),
                        MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("performFurnishChargingInformation", "Sending FurnishChargingInformation", null,
                        String.class.getName(), MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("performPromptAndCollectUserInformation", "Sending PromptAndCollectUserInformation",
                        null, String.class.getName(), MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("performActivityTest", "Sending ActivityTest", null, String.class.getName(),
                        MBeanOperationInfo.ACTION),

                new MBeanOperationInfo("putCapApplicationContext", "CAP application context: " + "34:V4_capscf_ssfGeneric",
                        signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),

                new MBeanOperationInfo("putConDestRouteAddrNatureOfAddress",
                        getEnumHint(IsupNatureOfAddressIndicator.internationalNumber, "<html>Nature of Address for DestinationRoutingAddress Connect message parameter creation having one of follwoing values: ", "<br>", "</html>"),
                        signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),

                new MBeanOperationInfo("putConDestRouteAddrNumberingPlan",
                        getEnumHint(IsupNumberingPlanIndicator.ISDN, "<html>Numbering Plan for DestinationRoutingAddress Connect message parameter creation having one of follwoing values: ", "<br>", "</html>"),
                        signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),

                new MBeanOperationInfo("putReleaseCauseValue",
                         getEnumHint(IsupCauseIndicatorCauseValue.allClear, "<html>Isup Cause Value for Release message parameter creation having one of follwoing values: ", "<br>", "</html>"),
                        signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),

                new MBeanOperationInfo("putReleaseCauseCodingStandardIndicator",
                        getEnumHint(IsupCauseIndicatorCodingStandard.ITUT, "<html>Isup Coding Standard Cause Indicator value for Release message parameter creation having one of follwoing values: ", "<br>", "</html>"),
                        signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),

                new MBeanOperationInfo("putReleaseCauseLocationIndicator",
                        getEnumHint(IsupCauseIndicatorLocation.internationalNetwork, "<html>Isup Location Cause Indicator value for Release message parameter creation having one of follwoing values:", "<br>", "</html>"),
                        signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),

                };

        return new MBeanInfo(TestCapScfMan.class.getName(), "TestCapScf test parameters management", attributes, null,
                operations, null);
    }

    private static String getEnumHint(Enum<?> e, String prefix, String separator, String suffix) {
        StringBuilder sb = new StringBuilder(prefix);
        if (e instanceof IsupCauseIndicatorCodingStandard) {
            for (IsupCauseIndicatorCodingStandard icics : IsupCauseIndicatorCodingStandard.values()) {
                sb.append(separator).append(icics.getCode()).append(":").append(icics.name());
            }
        } else if (e instanceof IsupCauseIndicatorLocation) {
            for (IsupCauseIndicatorLocation icil : IsupCauseIndicatorLocation.values()) {
                sb.append(separator).append(icil.getCode()).append(":").append(icil.name());
            }
        } else if (e instanceof IsupCauseIndicatorCauseValue) {
            for (IsupCauseIndicatorCauseValue icicv : IsupCauseIndicatorCauseValue.values()) {
                sb.append(separator).append(icicv.getCode()).append(":").append(icicv.name());
            }
        } else if (e instanceof IsupNumberingPlanIndicator) {
            for (IsupNumberingPlanIndicator npi : IsupNumberingPlanIndicator.values()) {
                sb.append(separator).append(npi.getCode()).append(":").append(npi.name());
            }
        } else if (e instanceof IsupNatureOfAddressIndicator) {
            for (IsupNatureOfAddressIndicator nai : IsupNatureOfAddressIndicator.values()) {
                sb.append(separator).append(nai.getCode()).append(":").append(nai.name());
            }
        }
        sb.append(suffix);
        return sb.toString();
    }

}
