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

import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.common.CapApplicationContextSsf;
import org.mobicents.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TestCapSsfStandardManMBean extends StandardMBean {

    public TestCapSsfStandardManMBean(TestCapSsfMan impl, Class<TestCapSsfManMBean> intf) throws NotCompliantMBeanException {
        super(impl, intf);
    }

    @Override
    public MBeanInfo getMBeanInfo() {

        MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[] {
            new MBeanAttributeInfo("CapApplicationContext", CapApplicationContextSsf.class.getName(),
                    "CAP application context", true, true, false),
            new MBeanAttributeInfo("CapApplicationContext_Value", String.class.getName(), "CAP application context", true,
                    false, false),
            new MBeanAttributeInfo("CurrentRequestDef", String.class.getName(), "Get information of the current request",
                    true, false, false),

            new MBeanAttributeInfo("UseCldInsteadOfCldBCDNumber", boolean.class.getName(),
                    "If true then for MOC the CaledPartyNumber instead of CaledPartyBCDNumber will be used (Call Forwarding case). For MTC parameter is ignored (CaledPartyBCDNumber is always used)",
                    true, true, true),

            new MBeanAttributeInfo("ServiceKey", int.class.getName(),
                    "Service Key parameter value of InitialDP message", true,
                    true, false),

            new MBeanAttributeInfo("IdpEventTypeBCSM", EventTypeBCSMType.class.getName(), "EventTypeBCSM parameter value of InitialDP message", true, true, false),
            new MBeanAttributeInfo("IdpEventTypeBCSM_Value", String.class.getName(), "EventTypeBCSM parameter value of InitialDP message", true, false, false),

            new MBeanAttributeInfo("", String.class.getName(),
                    "Address value of CallingPartyNumberAddress parameter in InitialDP operation message", true, true, false),
            new MBeanAttributeInfo("CallingPartyNumberNatureOfAddress", IsupNatureOfAddressIndicatorType.class.getName(),
                    "Isup Nature Of Address Indicator for InitialDP.CallingPartyNumber parameter creation",
                    true, true, false),
            new MBeanAttributeInfo("CallingPartyNumberNatureOfAddress_Value", String.class.getName(),
                    "Isup Nature Of Address Indicator for InitialDP.CallingPartyNumber parameter creation",
                    true, false, false),
            new MBeanAttributeInfo("CallingPartyNumberNumberingPlan", IsupNumberingPlanIndicatorType.class.getName(),
                    "Isup Numbering Plan Indicator for InitialDP.CallingPartyNumber parameter creation",
                    true, true, false),
            new MBeanAttributeInfo("CallingPartyNumberNumberingPlan_Value", String.class.getName(),
                    "Isup Numbering Plan Indicator for InitialDP.CallingPartyNumber parameter creation",
                    true, false, false),

            new MBeanAttributeInfo("CalledPartyBCDNumberAddress", String.class.getName(),
                    "Address value of CalledPartyBCDNumberAddress parameter in InitialDP operation message", true, true, false),
            new MBeanAttributeInfo("CalledPartyBCDNumberAddressNature", AddressNatureType.class.getName(),
                    "Address Nature for InitialDP.CalledPartyBCDNumber parameter creation",
                    true, true, false),
            new MBeanAttributeInfo("CalledPartyBCDNumberAddressNature_Value", String.class.getName(),
                    "Address Nature for InitialDP.CalledPartyBCDNumber parameter creation",
                    true, false, false),
            new MBeanAttributeInfo("CalledPartyBCDNumberNumberingPlan", NumberingPlanMapType.class.getName(),
                    "Numbering Plan for InitialDP.CalledPartyBCDNumber parameter creation",
                    true, true, false),
            new MBeanAttributeInfo("CalledPartyBCDNumberNumberingPlan_Value", String.class.getName(),
                    "Numbering Plan for InitialDP.CalledPartyBCDNumber parameter creation",
                    true, false, false),

            new MBeanAttributeInfo("CalledPartyNumberAddress", String.class.getName(),
                    "Address value of CalledPartyNumberAddress parameter in InitialDP operation message", true, true, false),
            new MBeanAttributeInfo("CalledPartyNumberNatureOfAddress", IsupNatureOfAddressIndicatorType.class.getName(),
                    "Isup Nature Of Address Indicator for InitialDP.CalledPartyNumber parameter creation",
                    true, true, false),
            new MBeanAttributeInfo("CalledPartyNumberNatureOfAddress_Value", String.class.getName(),
                    "Isup Nature Of Address Indicator for InitialDP.CalledPartyNumber parameter creation",
                    true, false, false),
            new MBeanAttributeInfo("CalledPartyNumberNumberingPlan", IsupNumberingPlanIndicatorType.class.getName(),
                    "Isup Numbering Plan Indicator for InitialDP.CalledPartyNumber parameter creation",
                    true, true, false),
            new MBeanAttributeInfo("CalledPartyNumberNumberingPlan_Value", String.class.getName(),
                    "Isup Numbering Plan Indicator for InitialDP.CalledPartyNumber parameter creation",
                    true, false, false),

            new MBeanAttributeInfo("MscAddressAddress", String.class.getName(),
                    "Address value of MscAddress parameter in InitialDP operation message", true, true, false),
            new MBeanAttributeInfo("MscAddressNatureOfAddress", AddressNatureType.class.getName(),
                    "Address Nature for InitialDP.MscAddress parameter creation",
                    true, true, false),
            new MBeanAttributeInfo("MscAddressNatureOfAddress_Value", String.class.getName(),
                    "Address Nature for InitialDP.MscAddress parameter creation",
                    true, false, false),
            new MBeanAttributeInfo("MscAddressNumberingPlan", NumberingPlanMapType.class.getName(),
                    "Numbering Plan for InitialDP.MscAddress parameter creation",
                    true, true, false),
            new MBeanAttributeInfo("MscAddressNumberingPlan_Value", String.class.getName(),
                    "Numbering Plan for InitialDP.MscAddress parameter creation",
                    true, false, false),
        };

        MBeanParameterInfo[] signString = new MBeanParameterInfo[] { new MBeanParameterInfo("val", String.class.getName(),
                "Index number or value") };


        MBeanOperationInfo[] operations = new MBeanOperationInfo[] {

                new MBeanOperationInfo("closeCurrentDialog", "Closing the current dialog", null, String.class.getName(),
                        MBeanOperationInfo.ACTION),

                new MBeanOperationInfo("performInitialDp", "Open a new Dialog and sending InitialDp", null,
                        String.class.getName(), MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("performAssistRequestInstructions",
                        "Open a new Dialog and sending AssistRequestInstructions", null, String.class.getName(),
                        MBeanOperationInfo.ACTION),

                new MBeanOperationInfo("performApplyChargingReport", "Sending ApplyChargingReport", null,
                        String.class.getName(), MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("performEventReportBCSM", "Sending EventReportBCSM", null, String.class.getName(),
                        MBeanOperationInfo.ACTION),

                new MBeanOperationInfo(
                        "putCapApplicationContext",
                        "CAP application context: "
                                + "1:V1_gsmSSF_to_gsmSCF 0.50.0, 2:V2_gsmSSF_to_gsmSCF 0.50.1, 3:V3_scfGeneric 21.3.4, 4:V4_scfGeneric 23.3.4, "
                                + "12:V2_assist_gsmSSF_to_gsmSCF 0.51.1, 13:V3_scfAssistHandoffAC 21.3.6, 14:V4_scfAssistHandoffAC 23.3.6, "
                                + "22:V2_gsmSRF_to_gsmSCF 0.52.1, 23:V3_gsmSRF_gsmSCF 20.3.14, 24:V4_gsmSRF_gsmSCF 22.3.14",
                        signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),

                new MBeanOperationInfo("putIdpEventTypeBCSM",
                        getEnumHint(EventTypeBCSM.collectedInfo, "<html>Event Type BCSM for InitialDP message parameter creation having one of follwoing values: ", "<br>", "</html>"),
                        signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),

                new MBeanOperationInfo("putCallingPartyNumberNatureOfAddress",
                        getEnumHint(IsupNatureOfAddressIndicator.internationalNumber, "<html>Nature of Address for InitialDP.CallingPartyNumber parameter creation having one of follwoing values: ", "<br>", "</html>"),
                        signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("putCallingPartyNumberNumberingPlan",
                        getEnumHint(IsupNumberingPlanIndicator.ISDN, "<html>Numbering Plan for InitialDP.CallingPartyNumber parameter creation having one of follwoing values: ", "<br>", "</html>"),
                        signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),

                new MBeanOperationInfo("putCalledPartyBCDNumberAddressNature",
                        getEnumHint(AddressNature.international_number, "<html>Address Nature for InitialDP.CalledPartyBCDNumber parameter creation having one of follwoing values: ", "<br>", "</html>"),
                        signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("putCalledPartyBCDNumberNumberingPlan",
                        getEnumHint(NumberingPlan.ISDN, "<html>Numbering Plan for InitialDP.CalledPartyBCDNumber parameter creation having one of follwoing values: ", "<br>", "</html>"),
                        signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),

                new MBeanOperationInfo("putCalledPartyNumberNatureOfAddress",
                        getEnumHint(IsupNatureOfAddressIndicator.internationalNumber, "<html>Nature of Address for InitialDP.CalledPartyNumber parameter creation having one of follwoing values: ", "<br>", "</html>"),
                        signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("putCalledPartyNumberNumberingPlan",
                        getEnumHint(IsupNumberingPlanIndicator.ISDN, "<html>Numbering Plan for InitialDP.CalledPartyNumber parameter creation having one of follwoing values: ", "<br>", "</html>"),
                        signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),

                new MBeanOperationInfo("putMscAddressNatureOfAddress",
                        getEnumHint(AddressNature.international_number, "<html>Address Nature for InitialDP.MscAddress parameter creation having one of follwoing values: ", "<br>", "</html>"),
                        signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("putMscAddressNumberingPlan",
                        getEnumHint(NumberingPlan.ISDN, "<html>Numbering Plan for InitialDP.MscAddress parameter creation having one of follwoing values: ", "<br>", "</html>"),
                        signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),
        };

        return new MBeanInfo(TestCapScfMan.class.getName(), "TestCapScf test parameters management", attributes, null,
                operations, null);
    }

    private static String getEnumHint(Enum<?> e, String prefix, String separator, String suffix) {
        StringBuilder sb = new StringBuilder(prefix);
        if (e instanceof EventTypeBCSM) {
            for (EventTypeBCSM etbcsm : EventTypeBCSM.values()) {
                sb.append(separator).append(etbcsm.getCode()).append(":").append(etbcsm.name());
            }
        } else if (e instanceof IsupNumberingPlanIndicator) {
            for (IsupNumberingPlanIndicator npi : IsupNumberingPlanIndicator.values()) {
                sb.append(separator).append(npi.getCode()).append(":").append(npi.name());
            }
        } else if (e instanceof IsupNatureOfAddressIndicator) {
            for (IsupNatureOfAddressIndicator nai : IsupNatureOfAddressIndicator.values()) {
                sb.append(separator).append(nai.getCode()).append(":").append(nai.name());
            }
        } else if (e instanceof AddressNature) {
            for (AddressNature an : AddressNature.values()) {
                sb.append(separator).append(an.getIndicator()).append(":").append(an.name());
            }
        } else if (e instanceof NumberingPlan) {
            for (NumberingPlan np : NumberingPlan.values()) {
                sb.append(separator).append(np.getIndicator()).append(":").append(np.name());
            }
        }
        sb.append(suffix);
        return sb.toString();
    }

}
