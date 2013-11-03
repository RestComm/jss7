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

                new MBeanOperationInfo("putCapApplicationContext", "CAP application context: " + "34:V4_capscf_ssfGeneric",
                        signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION), };

        return new MBeanInfo(TestCapScfMan.class.getName(), "TestCapScf test parameters management", attributes, null,
                operations, null);
    }

}
