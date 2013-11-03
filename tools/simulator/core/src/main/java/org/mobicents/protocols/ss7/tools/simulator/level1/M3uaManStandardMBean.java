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

package org.mobicents.protocols.ss7.tools.simulator.level1;

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
public class M3uaManStandardMBean extends StandardMBean {

    public M3uaManStandardMBean(M3uaMan impl, Class<M3uaManMBean> intf) throws NotCompliantMBeanException {
        super(impl, intf);
    }

    @Override
    public MBeanInfo getMBeanInfo() {

        MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[] {
                new MBeanAttributeInfo("StorePcapTrace", boolean.class.getName(),
                        "Storing all transmitted/received data into MsgLog_*.pcap file", true, true, true),
                new MBeanAttributeInfo("SctpLocalHost", String.class.getName(), "Local host IP address", true, true, false),
                new MBeanAttributeInfo("SctpLocalHost2", String.class.getName(), "Local host 2 IP address", true, true, false),
                new MBeanAttributeInfo("SctpLocalPort", int.class.getName(), "Local port number", true, true, false),
                new MBeanAttributeInfo("SctpLocalPort2", int.class.getName(), "Local port 2 number", true, true, false),
                new MBeanAttributeInfo("SctpRemoteHost", String.class.getName(), "Remote host IP address", true, true, false),
                new MBeanAttributeInfo("SctpRemoteHost2", String.class.getName(), "Remote host 2 IP address", true, true, false),
                new MBeanAttributeInfo("SctpRemotePort", int.class.getName(), "Remote port number", true, true, false),
                new MBeanAttributeInfo("SctpRemotePort2", int.class.getName(), "Remote port 2 number", true, true, false),
                new MBeanAttributeInfo(
                        "SctpExtraHostAddresses",
                        String.class.getName(),
                        "Extra host IP addresses for multihoming support. In mulihoming case enter IP addresses separated by a space character",
                        true, true, false),
                new MBeanAttributeInfo("SctpIsServer", boolean.class.getName(),
                        "Local SCTP/TCP host is SCTP/TCP server (not a client)", true, true, true),
                new MBeanAttributeInfo("SctpIPChannelType", BIpChannelType.class.getName(), "IP channel type: SCTP or TCP",
                        true, true, false),
                new MBeanAttributeInfo("SctpIPChannelType_Value", String.class.getName(), "IP channel type: SCTP or TCP", true,
                        false, false),

                new MBeanAttributeInfo("M3uaFunctionality", M3uaFunctionality.class.getName(),
                        "M3ua functionality type: IPSP, AS or SGW", true, true, false),
                new MBeanAttributeInfo("M3uaFunctionality_Value", String.class.getName(), "M3ua functionality type", true,
                        false, false),
                new MBeanAttributeInfo("M3uaIPSPType", M3uaIPSPType.class.getName(), "M3ua IPSP type: CLIENT or SERVER", true,
                        true, false),
                new MBeanAttributeInfo("M3uaIPSPType_Value", String.class.getName(), "M3ua IPSP type", true, false, false),
                new MBeanAttributeInfo("M3uaExchangeType", M3uaExchangeType.class.getName(), "M3ua exchange type: SE or DE", true, true, false),
                new MBeanAttributeInfo("M3uaExchangeType_Value", String.class.getName(), "M3ua exchange type", true, false, false),
                new MBeanAttributeInfo("M3uaDpc", int.class.getName(), "Dpc", true, true, false),
                new MBeanAttributeInfo("M3uaDpc2", int.class.getName(), "Dpc2", true, true, false),
                new MBeanAttributeInfo("M3uaOpc", int.class.getName(), "Opc: may be -1", true, true, false),
                new MBeanAttributeInfo("M3uaOpc2", int.class.getName(), "Opc2", true, true, false),
                new MBeanAttributeInfo("M3uaSi", int.class.getName(), "Service indicator: may be -1", true, true, false),
                new MBeanAttributeInfo("M3uaRoutingContext", long.class.getName(), "Routing context", true, true, false),
                new MBeanAttributeInfo("M3uaNetworkAppearance", long.class.getName(), "Network appearance", true, true, false), };

        MBeanParameterInfo[] signString = new MBeanParameterInfo[] { new MBeanParameterInfo("val", String.class.getName(),
                "Index number or value") };

        MBeanOperationInfo[] operations = new MBeanOperationInfo[] {
                new MBeanOperationInfo("putSctpIPChannelType", "IP channel type: SCTP or TCP: 1:TCP,2:SCTP", signString,
                        Void.TYPE.getName(), MBeanOperationInfo.ACTION),

                new MBeanOperationInfo("putM3uaFunctionality", "M3ua functionality type: 1:IPSP,2:AS,3:SGW", signString,
                        Void.TYPE.getName(), MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("putM3uaIPSPType", "M3ua IPSP type: 1:CLIENT,2:SERVER", signString, Void.TYPE.getName(),
                        MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("putM3uaExchangeType", "M3ua exchange type: 1:SE,2:DE", signString, Void.TYPE.getName(),
                        MBeanOperationInfo.ACTION), };

        return new MBeanInfo(M3uaMan.class.getName(), "M3ua Management", attributes, null, operations, null);

    }
}
