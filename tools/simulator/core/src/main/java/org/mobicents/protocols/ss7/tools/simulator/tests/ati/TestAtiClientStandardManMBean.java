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

package org.mobicents.protocols.ss7.tools.simulator.tests.ati;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;

/**
*
* @author sergey vetyutnev
*
*/
public class TestAtiClientStandardManMBean extends StandardMBean {

    public TestAtiClientStandardManMBean(TestAtiClientMan impl, Class<TestAtiClientManMBean> intf) throws NotCompliantMBeanException {
        super(impl, intf);
    }

    @Override
    public MBeanInfo getMBeanInfo() {

        MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[] {
                new MBeanAttributeInfo("AddressNature", AddressNatureType.class.getName(), "AddressNature parameter for AddressString creating", true, true,
                        false),
                new MBeanAttributeInfo("AddressNature_Value", String.class.getName(), "AddressNature parameter for AddressString creating", true, false, false),
                new MBeanAttributeInfo("NumberingPlan", NumberingPlanMapType.class.getName(), "NumberingPlan parameter for AddressString creating", true, true,
                        false),
                new MBeanAttributeInfo("NumberingPlan_Value", String.class.getName(), "NumberingPlan parameter for AddressString creating", true, false, false),
                new MBeanAttributeInfo("SubscriberIdentityType", SubscriberIdentityType.class.getName(), "Subscriber identity type: IMSI or MSISDN", true,
                        true, false),
                new MBeanAttributeInfo("SubscriberIdentityType_Value", String.class.getName(), "Subscriber identity type: IMSI or MSISDN", true, false, false),

                new MBeanAttributeInfo("GetLocationInformation", boolean.class.getName(), "ATI Requesting: LocationInformation", true, true, true),
                new MBeanAttributeInfo("GetSubscriberState", boolean.class.getName(), "ATI Requesting: SubscriberState", true, true, true),
                new MBeanAttributeInfo("GetCurrentLocation", boolean.class.getName(), "ATI Requesting: CurrentLocation", true, true, true),
                new MBeanAttributeInfo("GetRequestedDomain", AtiDomainType.class.getName(), "ATI Requesting: RequestedDomain - csDomain or psDomain", true,
                        true, false),
                new MBeanAttributeInfo("GetRequestedDomain_Value", String.class.getName(), "ATI Requesting: RequestedDomain - csDomain or psDomain", true,
                        false, false), new MBeanAttributeInfo("GetImei", boolean.class.getName(), "ATI Requesting: Imei", true, true, true),
                new MBeanAttributeInfo("GetMsClassmark", boolean.class.getName(), "ATI Requesting: MsClassmark", true, true, true),
                new MBeanAttributeInfo("GetMnpRequestedInfo", boolean.class.getName(), "ATI Requesting: MnpRequestedInfo", true, true, true),

                new MBeanAttributeInfo("GsmSCFAddress", String.class.getName(), "ATI Requesting: gsmSCF-Address digits", true, true, false), };

        MBeanParameterInfo[] signString = new MBeanParameterInfo[] { new MBeanParameterInfo("val", String.class.getName(), "Index number or value") };

        MBeanParameterInfo[] performATIParam = new MBeanParameterInfo[] { new MBeanParameterInfo("msg", String.class.getName(), "Message text"),
                new MBeanParameterInfo("address", String.class.getName(), "SubscriberIdentity: IMSI or MSISDN"), };

        MBeanOperationInfo[] operations = new MBeanOperationInfo[] {
                new MBeanOperationInfo("performAtiRequest", "Send ATI request", performATIParam, String.class.getName(), MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("closeCurrentDialog", "Closing the current dialog", null, String.class.getName(), MBeanOperationInfo.ACTION),

                new MBeanOperationInfo(
                        "putAddressNature",
                        "AddressNature parameter for AddressString creating: "
                                + "0:unknown,1:international_number,2:national_significant_number,3:network_specific_number,4:subscriber_number,5:reserved,6:abbreviated_number,7:reserved_for_extension",
                        signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("putNumberingPlan", "NumberingPlan parameter for AddressString creating: "
                        + "0:unknown,1:ISDN,2:spare_2,3:data,4:telex,5:spare_5,6:land_mobile,7:spare_7,8:national,9:private_plan,15:reserved", signString,
                        Void.TYPE.getName(), MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("putSubscriberIdentityType", "Subscriber identity type: 0:IMSI, 1:MSISDN", signString, Void.TYPE.getName(),
                        MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("putGetRequestedDomain", "ATI Requesting RequestedDomain: 0:csDomain, 1:psDomain", signString, Void.TYPE.getName(),
                        MBeanOperationInfo.ACTION), };

        return new MBeanInfo(TestAtiClientMan.class.getName(), "AtiClient test parameters management", attributes, null, operations, null);
    }

}
