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

package org.mobicents.protocols.ss7.tools.simulator.tests.sms;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;
import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.common.MapProtocolVersion;
import org.mobicents.protocols.ss7.tools.simulator.common.NumberingPlanType;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class TestSmsClientStandardManMBean extends StandardMBean {

	public TestSmsClientStandardManMBean(TestSmsClientMan impl, Class<TestSmsClientManMBean> intf) throws NotCompliantMBeanException {
		super(impl, intf);
	}

	@Override
	public MBeanInfo getMBeanInfo() {

		MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[] { 
				new MBeanAttributeInfo("AddressNature", AddressNatureType.class.getName(), "AddressNature parameter for AddressString creating", true, true, false),
				new MBeanAttributeInfo("AddressNature_Value", String.class.getName(), "AddressNature parameter for AddressString creating", true, false, false),
				new MBeanAttributeInfo("NumberingPlan", NumberingPlanType.class.getName(), "NumberingPlan parameter for AddressString creating", true, true, false),
				new MBeanAttributeInfo("NumberingPlan_Value", String.class.getName(), "NumberingPlan parameter for AddressString creating", true, false, false),
				new MBeanAttributeInfo("ServiceCenterAddress", String.class.getName(), "Destination Service center address string", true, true, false),
				new MBeanAttributeInfo("MapProtocolVersion", MapProtocolVersion.class.getName(), "MAP protocol version", true, true, false),
				new MBeanAttributeInfo("MapProtocolVersion_Value", String.class.getName(), "MAP protocol version", true, false, false),
				new MBeanAttributeInfo("SRIResponseImsi", String.class.getName(), "IMSI for auto sendRoutingInfoForSM response", true, true, false),
				new MBeanAttributeInfo("SRIResponseVlr", String.class.getName(), "VLR address for auto sendRoutingInfoForSM response", true, true, false),
				new MBeanAttributeInfo("SmscSsn", int.class.getName(), "SMSC SSN for outgoing SccpAddress (default value: 8)", true, true, false),
				new MBeanAttributeInfo("TypeOfNumber", TypeOfNumberType.class.getName(), "TypeOfNumber parameter for SMS tpdu destAddress", true, true, false),
				new MBeanAttributeInfo("TypeOfNumber_Value", String.class.getName(), "TypeOfNumber parameter for SMS tpdu destAddress", true, false, false),
				new MBeanAttributeInfo("NumberingPlanIdentification", NumberingPlanIdentificationType.class.getName(), "NumberingPlanIdentification parameter for SMS tpdu destAddress", true, true, false),
				new MBeanAttributeInfo("NumberingPlanIdentification_Value", String.class.getName(), "NumberingPlanIdentification parameter for SMS tpdu destAddress", true, false, false),
				new MBeanAttributeInfo("CurrentRequestDef", String.class.getName(), "Get information of the current request", true, false, false),
				new MBeanAttributeInfo("SmsCodingType", SmsCodingType.class.getName(), "Character set for SMS encoding", true, true, false),
		};

		MBeanParameterInfo[] signString = new MBeanParameterInfo[] { new MBeanParameterInfo("val", String.class.getName(), "Index number or value") };

		MBeanParameterInfo[] performMoForwardSMParam = new MBeanParameterInfo[] { 
				new MBeanParameterInfo("msg", String.class.getName(), "Message text"), 
				new MBeanParameterInfo("destIsdnNumber", String.class.getName(), "Destination ISDN number"), 
				new MBeanParameterInfo("origIsdnNumber", String.class.getName(), "Origination ISDN number"), 
		};

		MBeanOperationInfo[] operations = new MBeanOperationInfo[] {
				new MBeanOperationInfo("performMoForwardSM", "Send mo-forwardSM request", performMoForwardSMParam, String.class.getName(), MBeanOperationInfo.ACTION),
				new MBeanOperationInfo("closeCurrentDialog", "Closing the current dialog", null, String.class.getName(), MBeanOperationInfo.ACTION),

				new MBeanOperationInfo("putAddressNature", "AddressNature parameter for AddressString creating: "
						+ "0:unknown,1:international_number,2:national_significant_number,3:network_specific_number,4:subscriber_number,5:reserved,6:abbreviated_number,7:reserved_for_extension", 
						signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),
				new MBeanOperationInfo("putNumberingPlan", "NumberingPlan parameter for AddressString creating: " + 
						"0:unknown,1:ISDN,2:spare_2,3:data,4:telex,5:spare_5,6:land_mobile,7:spare_7,8:national,9:private_plan,15:reserved", 
						signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),
				new MBeanOperationInfo("putMapProtocolVersion", "MAP protocol version: " +
						"1, 2 or 3", 
						signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),
				new MBeanOperationInfo("putTypeOfNumber", "TypeOfNumber parameter for SMS tpdu destAddress: " +
						"0:Unknown,1:InternationalNumber,2:NationalNumber,3:NetworkSpecificNumber,4:SubscriberNumber,5:Alphanumeric,6:AbbreviatedNumber,7:Reserved",
						signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),
				new MBeanOperationInfo("putNumberingPlanIdentification", "NumberingPlanIdentification parameter for SMS tpdu destAddress: " +
						"0:Unknown,1:ISDNTelephoneNumberingPlan,3:DataNumberingPlan,4:TelexNumberingPlan,5:ServiceCentreSpecificPlan1,6:ServiceCentreSpecificPlan2,8:NationalNumberingPlan,9:PrivateNumberingPlan,10:ERMESNumberingPlan,15:Reserved", 
						signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),
				new MBeanOperationInfo("putSmsCodingType", "Character set for SMS encoding: " +
						"1:GSM7,2:UCS2", 
						signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),
		};

		return new MBeanInfo(TestSmsServerMan.class.getName(), "SmsClient test parameters management", attributes, null, operations, null);
	}

}
