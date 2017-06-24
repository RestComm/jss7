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
                new MBeanAttributeInfo(
                        "RouteOnGtMode",
                        boolean.class.getName(),
                        "False: ROUTING_BASED_ON_DPC_AND_SSN is used for Called/CallingPartyAddresses, "
                                + "True: ROUTING_BASED_ON_GLOBAL_TITLE is used (CalledPartyAddresses digits must be supplied by upper layers)",
                        true, true, true),
                new MBeanAttributeInfo("RemoteSpc", int.class.getName(), "Remote Signal point code", true, true, false),
                new MBeanAttributeInfo("LocalSpc", int.class.getName(), "Local Signal point code", true, true, false),
                new MBeanAttributeInfo("Ni", int.class.getName(), "Network indicator", true, true, false),
                new MBeanAttributeInfo("RemoteSsn", int.class.getName(), "Remote SSN number", true, true, false),
                new MBeanAttributeInfo("LocalSsn", int.class.getName(), "Local SSN number", true, true, false),
                new MBeanAttributeInfo("LocalSsn2", int.class.getName(), "Second Local SSN number", true, true, false),
                new MBeanAttributeInfo("GlobalTitleType", GlobalTitleType.class.getName(),
                        "GlobalTitle type for creating SccpAddress (when routing on GT)", true, true, false),
                new MBeanAttributeInfo("GlobalTitleType_Value", String.class.getName(),
                        "GlobalTitle type for creating SccpAddress (when routing on GT)", true, false, false),
                new MBeanAttributeInfo("NatureOfAddress", NatureOfAddressType.class.getName(),
                        "NatureOfAddress parameter for creating SccpAddress (when routing on GT)", true, true, false),
                new MBeanAttributeInfo("NatureOfAddress_Value", String.class.getName(),
                        "NatureOfAddress parameter for creating SccpAddress (when routing on GT)", true, false, false),
                new MBeanAttributeInfo("NumberingPlan", NumberingPlanSccpType.class.getName(),
                        "NumberingPlan parameter for creating SccpAddress (when routing on GT)", true, true, false),
                new MBeanAttributeInfo("NumberingPlan_Value", String.class.getName(),
                        "NumberingPlan parameter for creating SccpAddress (when routing on GT)", true, false, false),
                new MBeanAttributeInfo("TranslationType", int.class.getName(),
                        "Translation Type parameter for creating SccpAddress (when routing on GT)", true, true, false),
                new MBeanAttributeInfo("CallingPartyAddressDigits", String.class.getName(), "CallingPartyAddress digits for RouteOnGt mode", true, true, false),
                new MBeanAttributeInfo("SccpProtocolVersion", SccpProtocolVersionType.class.getName(), "Sccp protocol version (ITU/ANSI)", true, true, false),
                new MBeanAttributeInfo("SccpProtocolVersion_Value", String.class.getName(), "Sccp protocol version (ITU/ANSI)", true, false, false),
                };

        MBeanParameterInfo[] signString = new MBeanParameterInfo[] { new MBeanParameterInfo("val", String.class.getName(),
                "Index number or value") };

        MBeanOperationInfo[] operations = new MBeanOperationInfo[] {
                new MBeanOperationInfo("putGlobalTitleType", "GlobalTitle type: "
                        + "1:VAL_NOA_ONLY,2:VAL_TT_ONLY,3:VAL_TT_NP_ES,4:VAL_TT_NP_ES_NOA", signString, Void.TYPE.getName(),
                        MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("putNatureOfAddress", "Parameter: NatureOfAddress: "
                        + "0:UNKNOWN,1:SUBSCRIBER,2:RESERVED_NATIONAL_2,3:NATIONAL,4:INTERNATIONAL,127:RESERVED", signString,
                        Void.TYPE.getName(), MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("putNumberingPlan", "Parameter: NumberingPlan: "
                        + "0:UNKNOWN,1:ISDN_TELEPHONY,2:GENERIC,3:DATA,4:TELEX,5:MERITIME_MOBILE,6:LAND_MOBILE,7:ISDN_MOBILE,14:PRIVATE,15:RESERVED",
                        signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("putSccpProtocolVersion", "Parameter: SccpProtocolVersion: " + "1:ITU,2:ANSI", signString, Void.TYPE.getName(),
                        MBeanOperationInfo.ACTION), };

        return new MBeanInfo(SccpMan.class.getName(), "Sccp Management", attributes, null, operations, null);
    }
}
