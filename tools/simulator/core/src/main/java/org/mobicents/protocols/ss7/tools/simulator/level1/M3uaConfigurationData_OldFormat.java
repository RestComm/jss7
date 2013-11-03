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

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.IPSPType;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class M3uaConfigurationData_OldFormat extends M3uaConfigurationData {

    protected static final XMLFormat<M3uaConfigurationData_OldFormat> XML_OLD = new XMLFormat<M3uaConfigurationData_OldFormat>(
            M3uaConfigurationData_OldFormat.class) {

        public void write(M3uaConfigurationData_OldFormat m3ua, OutputElement xml) throws XMLStreamException {
        }

        public void read(InputElement xml, M3uaConfigurationData_OldFormat m3ua) throws XMLStreamException {
            m3ua.setIsSctpServer(xml.getAttribute(IS_SCTP_SERVER).toBoolean());
            m3ua.setLocalPort(xml.getAttribute(LOCAL_PORT).toInt());
            m3ua.setRemotePort(xml.getAttribute(REMOTE_PORT).toInt());
            String str = xml.getAttribute(IP_CHANNEL_TYPE).toString();
            m3ua.setIpChannelType(IpChannelType.valueOf(str));
            m3ua.setDpc(xml.getAttribute(DPC).toInt());
            m3ua.setOpc(xml.getAttribute(OPC).toInt());
            m3ua.setSi(xml.getAttribute(SI).toInt());
            m3ua.setRoutingContext(xml.getAttribute(ROUTING_CONTEXT).toInt());
            m3ua.setNetworkAppearance(xml.getAttribute(NETWORK_APPEARANCE).toInt());
            str = xml.getAttribute(M3UA_FUNCTIONALITY).toString();
            m3ua.setM3uaFunctionality(Functionality.valueOf(str));
            str = xml.getAttribute(M3UA_EXCHANGE_TYPE).toString();
            m3ua.setM3uaExchangeType(ExchangeType.valueOf(str));
            str = xml.getAttribute(M3UA_IPSPType).toString();
            m3ua.setM3uaIPSPType(IPSPType.valueOf(str));

            m3ua.setLocalHost((String) xml.get(LOCAL_HOST, String.class));
            m3ua.setRemoteHost((String) xml.get(REMOTE_HOST, String.class));
            m3ua.setSctpExtraHostAddresses((String) xml.get(EXTRA_HOST_ADDRESSES, String.class));
        }
    };

}
