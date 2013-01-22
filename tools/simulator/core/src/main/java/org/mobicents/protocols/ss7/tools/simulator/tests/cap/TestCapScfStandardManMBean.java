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
				new MBeanAttributeInfo("CapApplicationContext", CapApplicationContextScf.class.getName(), "CAP application context", true, true, false),
				new MBeanAttributeInfo("CapApplicationContext_Value", String.class.getName(), "CAP application context", true, false, false),
				new MBeanAttributeInfo("CurrentRequestDef", String.class.getName(), "Get information of the current request", true, false, false),
		};

		MBeanParameterInfo[] signString = new MBeanParameterInfo[] { new MBeanParameterInfo("val", String.class.getName(), "Index number or value") };

//		MBeanParameterInfo[] performMoForwardSMParam = new MBeanParameterInfo[] { 
//				new MBeanParameterInfo("msg", String.class.getName(), "Message text"), 
//				new MBeanParameterInfo("destIsdnNumber", String.class.getName(), "Destination ISDN number"), 
//				new MBeanParameterInfo("origIsdnNumber", String.class.getName(), "Origination ISDN number"), 
//		};

		MBeanOperationInfo[] operations = new MBeanOperationInfo[] {
//				new MBeanOperationInfo("performMoForwardSM", "Send mo-forwardSM request", performMoForwardSMParam, String.class.getName(), MBeanOperationInfo.ACTION),

				new MBeanOperationInfo("closeCurrentDialog", "Closing the current dialog", null, String.class.getName(), MBeanOperationInfo.ACTION),

				new MBeanOperationInfo("performAssistRequestInstructions", "Open a new Dialog and sending AssistRequestInstructions", null, String.class.getName(), MBeanOperationInfo.ACTION),

				new MBeanOperationInfo("putCapApplicationContext", "CAP application context: " +
						"34:V4_capscf_ssfGeneric", 
						signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),
		};

		return new MBeanInfo(TestCapScfMan.class.getName(), "TestCapScf test parameters management", attributes, null, operations, null);
	}

}
