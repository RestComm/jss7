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

package org.mobicents.protocols.ss7.tools.simulator.level3;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MapManStandardMBean extends StandardMBean {

    public MapManStandardMBean(MapMan impl, Class<MapManMBean> intf) throws NotCompliantMBeanException {
        super(impl, intf);
    }

    @Override
    public MBeanInfo getMBeanInfo() {

        MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[] {
                // new MBeanAttributeInfo("LocalSsn", int.class.getName(), "Local Ssn", true, true, false),
                // new MBeanAttributeInfo("RemoteSsn", int.class.getName(), "Remote Ssn", true, true, false),
                new MBeanAttributeInfo("RemoteAddressDigits", String.class.getName(),
                        "Remote address digits. If empty ROUTING_BASED_ON_DPC_AND_SSN is used for CalledPartyAddress (remoteSpc from SCCP), "
                                + "if not empty ROUTING_BASED_ON_GLOBAL_TITLE is used (address and Ssn from MAP)", true, true,
                        false),
                new MBeanAttributeInfo("OrigReference", String.class.getName(), "Origination reference string", true, true,
                        false),
                new MBeanAttributeInfo("OrigReferenceAddressNature", AddressNatureType.class.getName(),
                        "Origination reference parameter: AddressNature", true, true, false),
                new MBeanAttributeInfo("OrigReferenceAddressNature_Value", String.class.getName(),
                        "Origination reference parameter: AddressNature", true, false, false),
                new MBeanAttributeInfo("OrigReferenceNumberingPlan", NumberingPlanMapType.class.getName(),
                        "Origination reference parameter: NumberingPlan", true, true, false),
                new MBeanAttributeInfo("OrigReferenceNumberingPlan_Value", String.class.getName(),
                        "Origination reference parameter: NumberingPlan", true, false, false),
                new MBeanAttributeInfo("DestReference", String.class.getName(), "Destination reference string", true, true,
                        false),
                new MBeanAttributeInfo("DestReferenceAddressNature", AddressNatureType.class.getName(),
                        "Destination reference parameter: AddressNature", true, true, false),
                new MBeanAttributeInfo("DestReferenceAddressNature_Value", String.class.getName(),
                        "Destination reference parameter: AddressNature", true, false, false),
                new MBeanAttributeInfo("DestReferenceNumberingPlan", NumberingPlanMapType.class.getName(),
                        "Destination reference parameter: NumberingPlan", true, true, false),
                new MBeanAttributeInfo("DestReferenceNumberingPlan_Value", String.class.getName(),
                        "Destination reference parameter: NumberingPlan", true, false, false), };

        MBeanParameterInfo[] signString = new MBeanParameterInfo[] { new MBeanParameterInfo("val", String.class.getName(),
                "Index number or value") };

        MBeanOperationInfo[] operations = new MBeanOperationInfo[] {
                new MBeanOperationInfo(
                        "putOrigReferenceAddressNature",
                        "Origination reference parameter: AddressNature: "
                                + "0:unknown,1:international_number,2:national_significant_number,3:network_specific_number,4:subscriber_number,5:reserved,6:abbreviated_number,7:reserved_for_extension",
                        signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),
                new MBeanOperationInfo(
                        "putOrigReferenceNumberingPlan",
                        "Origination reference reference parameter: NumberingPlan: "
                                + "0:unknown,1:ISDN,2:spare_2,3:data,4:telex,5:spare_5,6:land_mobile,7:spare_7,8:national,9:private_plan,15:reserved",
                        signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),
                new MBeanOperationInfo(
                        "putDestReferenceAddressNature",
                        "Destination reference parameter: AddressNature: "
                                + "0:unknown,1:international_number,2:national_significant_number,3:network_specific_number,4:subscriber_number,5:reserved,6:abbreviated_number,7:reserved_for_extension",
                        signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),
                new MBeanOperationInfo(
                        "putDestReferenceNumberingPlan",
                        "Destination reference parameter: NumberingPlan: "
                                + "0:unknown,1:ISDN,2:spare_2,3:data,4:telex,5:spare_5,6:land_mobile,7:spare_7,8:national,9:private_plan,15:reserved",
                        signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION), };

        return new MBeanInfo(MapMan.class.getName(), "Map Management", attributes, null, operations, null);
    }
}
