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

package org.mobicents.protocols.ss7.tools.simulator.level2;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.common.NumberingPlanType;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class SccpManStandardMBean extends StandardMBean {

	public SccpManStandardMBean(SccpMan impl, Class<SccpManMBean> intf) throws NotCompliantMBeanException {
		super(impl, intf);
	}

	@Override
	public MBeanInfo getMBeanInfo() {

		MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[] { 
				new MBeanAttributeInfo("RemoteSpc", int.class.getName(), "Remote Signal point code", true, true, false),
				new MBeanAttributeInfo("LocalSpc", int.class.getName(), "Local Signal point code", true, true, false),
				new MBeanAttributeInfo("Ni", int.class.getName(), "Network indicator", true, true, false),
				new MBeanAttributeInfo("RemoteSsn", int.class.getName(), "Remote SSN number", true, true, false),
				new MBeanAttributeInfo("LocalSsn", int.class.getName(), "Local SSN number", true, true, false),
				new MBeanAttributeInfo("GlobalTitleType", GlobalTitleType.class.getName(), "GlobalTitle type for creating SccpAddress (when routing on GT)", true, true, false),
				new MBeanAttributeInfo("GlobalTitleType_Value", String.class.getName(), "GlobalTitle type for creating SccpAddress (when routing on GT)", true, false, false),
				new MBeanAttributeInfo("AddressNature", AddressNatureType.class.getName(), "AddressNature parameter for creating SccpAddress (when routing on GT)", true, true, false),
				new MBeanAttributeInfo("AddressNature_Value", String.class.getName(), "AddressNature parameter for creating SccpAddress (when routing on GT)", true, false, false),
				new MBeanAttributeInfo("NumberingPlan", NumberingPlanType.class.getName(), "NumberingPlan parameter for creating SccpAddress (when routing on GT)", true, true, false),
				new MBeanAttributeInfo("NumberingPlan_Value", String.class.getName(), "NumberingPlan parameter for creating SccpAddress (when routing on GT)", true, false, false),
				new MBeanAttributeInfo("TranslationType", int.class.getName(), "Translation Type parameter for creating SccpAddress (when routing on GT)", true, true, false),
		};

		MBeanParameterInfo[] signString = new MBeanParameterInfo[] { new MBeanParameterInfo("val", String.class.getName(), "Index number or value") };

		MBeanOperationInfo[] operations = new MBeanOperationInfo[] {
				new MBeanOperationInfo("putGlobalTitleType", "GlobalTitle type: "
						+ "1:VAL_NOA_ONLY,2:VAL_TT_ONLY,3:VAL_TT_NP_ES,4:VAL_TT_NP_ES_NOA", 
						signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),
				new MBeanOperationInfo("putAddressNature", "Parameter: AddressNature: "
						+ "0:unknown,1:international_number,2:national_significant_number,3:network_specific_number,4:subscriber_number,5:reserved,6:abbreviated_number,7:reserved_for_extension", 
						signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),
				new MBeanOperationInfo("putNumberingPlan", "Parameter: NumberingPlan: " + 
						"0:unknown,1:ISDN,2:spare_2,3:data,4:telex,5:spare_5,6:land_mobile,7:spare_7,8:national,9:private_plan,15:reserved", 
						signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),
		};

		return new MBeanInfo(SccpMan.class.getName(), "Sccp Management", attributes, null, operations, null);
	}
}

