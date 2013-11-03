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

package org.mobicents.protocols.ss7.tools.simulator.tests.ussd;

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
public class TestUssdClientStandardManMBean extends StandardMBean {

    public TestUssdClientStandardManMBean(TestUssdClientMan impl, Class<TestUssdClientManMBean> intf)
            throws NotCompliantMBeanException {
        super(impl, intf);
    }

    @Override
    public MBeanInfo getMBeanInfo() {

        MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[] {
                new MBeanAttributeInfo("MsisdnAddress", String.class.getName(), "Msisdn parameter: string", true, true, false),
                new MBeanAttributeInfo("MsisdnAddressNature", AddressNatureType.class.getName(),
                        "Msisdn parameter: AddressNature", true, true, false),
                new MBeanAttributeInfo("MsisdnAddressNature_Value", String.class.getName(), "Msisdn parameter: AddressNature",
                        true, false, false),
                new MBeanAttributeInfo("MsisdnNumberingPlan", NumberingPlanMapType.class.getName(),
                        "Msisdn parameter: NumberingPlan", true, true, false),
                new MBeanAttributeInfo("MsisdnNumberingPlan_Value", String.class.getName(), "Msisdn parameter: NumberingPlan",
                        true, false, false),
                new MBeanAttributeInfo("DataCodingScheme", int.class.getName(), "USSD DataCodingScheme (default value: 15)",
                        true, true, false),
                new MBeanAttributeInfo("AlertingPattern", int.class.getName(),
                        "AlertingPattern value (-1 means no AlertingPattern parameter)", true, true, false),
                new MBeanAttributeInfo("CurrentRequestDef", String.class.getName(), "Definition of the current request Dialog",
                        true, false, false),

                new MBeanAttributeInfo(
                        "UssdClientAction",
                        UssdClientAction.class.getName(),
                        "The mode of UssdClient work. When manual response user can manually send ProcessSsUnstructured request, when VAL_AUTO_SendProcessUnstructuredSSRequest the tester sends ProcessSsUnstructured requests without dealay (load test)",
                        true, true, false),
                new MBeanAttributeInfo(
                        "UssdClientAction_Value",
                        String.class.getName(),
                        "The mode of UssdClient work. When manual response user can manually send ProcessSsUnstructured request, when VAL_AUTO_SendProcessUnstructuredSSRequest the tester sends ProcessSsUnstructured requests without dealay (load test)",
                        true, false, false),
                new MBeanAttributeInfo("AutoRequestString", String.class.getName(),
                        "Value of auto ProcessSsUnstructured request", true, true, false),
                new MBeanAttributeInfo("MaxConcurrentDialogs", int.class.getName(),
                        "The count of maximum active MAP dialogs when the auto sending mode", true, true, false),
                new MBeanAttributeInfo(
                        "OneNotificationFor100Dialogs",
                        boolean.class.getName(),
                        "If true there will be only one notification per every 100 sent dialogs (recommended for the auto sending mode)",
                        true, true, true), };

        MBeanParameterInfo[] signString = new MBeanParameterInfo[] { new MBeanParameterInfo("val", String.class.getName(),
                "Index number or value") };

        MBeanOperationInfo[] operations = new MBeanOperationInfo[] {
                new MBeanOperationInfo("performProcessUnstructuredRequest", "Send ProcessUnstructedSs request", signString,
                        String.class.getName(), MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("performUnstructuredResponse", "Send UnstructedSs response", signString,
                        String.class.getName(), MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("closeCurrentDialog", "Closing the current dialog", null, String.class.getName(),
                        MBeanOperationInfo.ACTION),

                new MBeanOperationInfo(
                        "putMsisdnAddressNature",
                        "Msisdn parameter: AddressNature: "
                                + "0:unknown,1:international_number,2:national_significant_number,3:network_specific_number,4:subscriber_number,5:reserved,6:abbreviated_number,7:reserved_for_extension",
                        signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),
                new MBeanOperationInfo(
                        "putMsisdnNumberingPlan",
                        "Msisdn parameter: NumberingPlan: "
                                + "0:unknown,1:ISDN,2:spare_2,3:data,4:telex,5:spare_5,6:land_mobile,7:spare_7,8:national,9:private_plan,15:reserved",
                        signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("putUssdClientAction",
                        "The mode of UssdClient work. 1:VAL_MANUAL_OPERATION,2:VAL_AUTO_SendProcessUnstructuredSSRequest",
                        signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION), };

        return new MBeanInfo(TestUssdClientMan.class.getName(), "UssdClient test parameters management", attributes, null,
                operations, null);
    }
}
