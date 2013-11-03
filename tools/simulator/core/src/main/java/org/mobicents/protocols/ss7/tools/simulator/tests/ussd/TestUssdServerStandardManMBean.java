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
public class TestUssdServerStandardManMBean extends StandardMBean {

    public TestUssdServerStandardManMBean(TestUssdServerMan impl, Class<TestUssdServerManMBean> intf)
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
                new MBeanAttributeInfo(
                        "ProcessSsRequestAction",
                        ProcessSsRequestAction.class.getName(),
                        "Action which be performed when ProcessSsUnstructuredRequest has been received. When manual response user must manually send a response or SsUnstructuredRequest to the UssdClient. Other actions are: auto sending \"AutoResponseString\" as a response, auto sending \"AutoUnstructured_SS_RequestString\" as a SsUnstructuredRequest and then auto sending \"AutoResponseString\" as a response to SsUnstructured response",
                        true, true, false),
                new MBeanAttributeInfo(
                        "ProcessSsRequestAction_Value",
                        String.class.getName(),
                        "Action which be performed when ProcessSsUnstructuredRequest has been received. When manual response user must manually send a response or SsUnstructuredRequest to the UssdClient. Other actions are: auto sending \"AutoResponseString\" as a response, auto sending \"AutoUnstructured_SS_RequestString\" as a SsUnstructuredRequest and then auto sending \"AutoResponseString\" as a response to SsUnstructured response",
                        true, false, false),
                new MBeanAttributeInfo("AutoResponseString", String.class.getName(),
                        "Value of auto ProcessSsUnstructured response", true, true, false),
                new MBeanAttributeInfo("AutoUnstructured_SS_RequestString", String.class.getName(),
                        "Value of auto SsUnstructured request", true, true, false),
                new MBeanAttributeInfo("CurrentRequestDef", String.class.getName(), "Definition of the current request Dialog",
                        true, false, false),
                new MBeanAttributeInfo(
                        "OneNotificationFor100Dialogs",
                        boolean.class.getName(),
                        "If true there will be only one notification per every 100 received dialogs (recommended for the auto answering mode)",
                        true, true, true), };

        MBeanParameterInfo[] signString = new MBeanParameterInfo[] { new MBeanParameterInfo("val", String.class.getName(),
                "Index number or value") };

        MBeanOperationInfo[] operations = new MBeanOperationInfo[] {
                new MBeanOperationInfo("performProcessUnstructuredResponse", "Send ProcessUnstructedSs response", signString,
                        String.class.getName(), MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("performUnstructuredRequest", "Send UnstructedSs request", signString,
                        String.class.getName(), MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("performUnstructuredNotify", "Send UnstructedSs notify", signString,
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
                new MBeanOperationInfo(
                        "putProcessSsRequestAction",
                        "Action which be performed when ProcessSsUnstructuredRequest has been received. 1:VAL_MANUAL_RESPONSE,2:VAL_AUTO_ProcessUnstructuredSSResponse,3:VAL_AUTO_Unstructured_SS_Request_Then_ProcessUnstructuredSSResponse",
                        signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION), };

        return new MBeanInfo(TestUssdServerMan.class.getName(), "UssdServer test parameters management", attributes, null,
                operations, null);
    }
}
