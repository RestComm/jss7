/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.tools.simulator.tests.cap;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

import org.mobicents.protocols.ss7.tools.simulator.common.CapApplicationContextSsf;

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
                        true, false, false), };

        MBeanParameterInfo[] signString = new MBeanParameterInfo[] { new MBeanParameterInfo("val", String.class.getName(),
                "Index number or value") };

        // MBeanParameterInfo[] performMoForwardSMParam = new MBeanParameterInfo[] {
        // new MBeanParameterInfo("msg", String.class.getName(), "Message text"),
        // new MBeanParameterInfo("destIsdnNumber", String.class.getName(), "Destination ISDN number"),
        // new MBeanParameterInfo("origIsdnNumber", String.class.getName(), "Origination ISDN number"),
        // };

        MBeanOperationInfo[] operations = new MBeanOperationInfo[] {
                // new MBeanOperationInfo("performMoForwardSM", "Send mo-forwardSM request", performMoForwardSMParam,
                // String.class.getName(), MBeanOperationInfo.ACTION),

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

        };

        return new MBeanInfo(TestCapScfMan.class.getName(), "TestCapScf test parameters management", attributes, null,
                operations, null);
    }

}
