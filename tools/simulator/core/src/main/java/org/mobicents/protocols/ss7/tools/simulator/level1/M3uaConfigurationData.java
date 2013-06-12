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

package org.mobicents.protocols.ss7.tools.simulator.level1;

import javolution.text.CharArray;
import javolution.util.FastList;
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
public class M3uaConfigurationData {

    protected static final String STORE_PCAP_TRACE = "storePcapTrace";
    protected static final String IS_SCTP_SERVER = "isSctpServer";
    protected static final String LOCAL_HOST = "localHost";
    protected static final String LOCAL_PORT = "localPort";
    protected static final String REMOTE_HOST = "remoteHost";
    protected static final String REMOTE_PORT = "remotePort";
    protected static final String IP_CHANNEL_TYPE = "ipChannelType";
    protected static final String EXTRA_HOST_ADDRESSES = "extraHostAddresses";

    protected static final String M3UA_FUNCTIONALITY = "m3uaFunctionality";
    protected static final String M3UA_EXCHANGE_TYPE = "m3uaExchangeType";
    protected static final String M3UA_IPSPType = "m3uaIPSPType";
    protected static final String DPC = "dpc";
    protected static final String OPC = "opc";
    protected static final String SI = "si";
    protected static final String ROUTING_CONTEXT = "routingConext";
    protected static final String NETWORK_APPEARANCE = "networkAppearance";

    private boolean storePcapTrace = false;
    private boolean isSctpServer = false;
    private String localHost;
    private int localPort;
    private String remoteHost;
    private int remotePort;
    private IpChannelType ipChannelType = IpChannelType.TCP;
    private String[] extraHostAddresses = new String[0];
    private int dpc = 0;
    private int opc = -1;
    private int si = -1;
    private long routingContext = 101;
    private long networkAppearance = 102;

    private Functionality m3uaFunctionality = Functionality.IPSP;
    private ExchangeType m3uaExchangeType = ExchangeType.SE;
    private IPSPType m3uaIPSPType = IPSPType.CLIENT;

    public boolean getStorePcapTrace() {
        return storePcapTrace;
    }

    public void setStorePcapTrace(boolean val) {
        storePcapTrace = val;
    }

    public boolean getIsSctpServer() {
        return isSctpServer;
    }

    public void setIsSctpServer(boolean val) {
        isSctpServer = val;
    }

    public String getLocalHost() {
        return localHost;
    }

    public void setLocalHost(String val) {
        localHost = val;
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int val) {
        localPort = val;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String val) {
        remoteHost = val;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int val) {
        remotePort = val;
    }

    public IpChannelType getIpChannelType() {
        return ipChannelType;
    }

    public void setIpChannelType(IpChannelType val) {
        ipChannelType = val;
    }

    public String[] getSctpExtraHostAddressesArray() {
        return extraHostAddresses;
    }

    public void setSctpExtraHostAddressesArray(String[] val) {
        extraHostAddresses = val;
    }

    public String getSctpExtraHostAddresses() {
        if (extraHostAddresses == null) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder();
            for (String s : extraHostAddresses) {
                if (sb.length() != 0) {
                    sb.append(" ");
                }
                sb.append(s);
            }
            return sb.toString();
        }
    }

    public void setSctpExtraHostAddresses(String val) {
        if (val == null)
            return;

        String[] ss = val.split(" ");
        FastList<String> fl = new FastList<String>();
        for (String s : ss) {
            if (s.length() != 0) {
                fl.add(s);
            }
        }
        extraHostAddresses = new String[fl.size()];
        fl.toArray(extraHostAddresses);
    }

    public int getDpc() {
        return dpc;
    }

    public void setDpc(int val) {
        dpc = val;
    }

    public int getOpc() {
        return opc;
    }

    public void setOpc(int val) {
        opc = val;
    }

    public int getSi() {
        return si;
    }

    public void setSi(int val) {
        si = val;
    }

    public long getRoutingContext() {
        return routingContext;
    }

    public void setRoutingContext(long val) {
        routingContext = val;
    }

    public long getNetworkAppearance() {
        return networkAppearance;
    }

    public void setNetworkAppearance(long val) {
        networkAppearance = val;
    }

    public Functionality getM3uaFunctionality() {
        return m3uaFunctionality;
    }

    public void setM3uaFunctionality(Functionality val) {
        m3uaFunctionality = val;
    }

    public ExchangeType getM3uaExchangeType() {
        return m3uaExchangeType;
    }

    public void setM3uaExchangeType(ExchangeType val) {
        m3uaExchangeType = val;
    }

    public IPSPType getM3uaIPSPType() {
        return m3uaIPSPType;
    }

    public void setM3uaIPSPType(IPSPType val) {
        m3uaIPSPType = val;
    }

    protected static final XMLFormat<M3uaConfigurationData> XML = new XMLFormat<M3uaConfigurationData>(
            M3uaConfigurationData.class) {

        public void write(M3uaConfigurationData m3ua, OutputElement xml) throws XMLStreamException {
            xml.setAttribute(STORE_PCAP_TRACE, m3ua.storePcapTrace);
            xml.setAttribute(IS_SCTP_SERVER, m3ua.isSctpServer);
            xml.setAttribute(LOCAL_PORT, m3ua.localPort);
            xml.setAttribute(REMOTE_PORT, m3ua.remotePort);
            xml.setAttribute(IP_CHANNEL_TYPE, m3ua.ipChannelType.toString());
            xml.setAttribute(DPC, m3ua.dpc);
            xml.setAttribute(OPC, m3ua.opc);
            xml.setAttribute(SI, m3ua.si);
            xml.setAttribute(ROUTING_CONTEXT, m3ua.routingContext);
            xml.setAttribute(NETWORK_APPEARANCE, m3ua.networkAppearance);
            xml.setAttribute(M3UA_FUNCTIONALITY, m3ua.m3uaFunctionality.toString());
            xml.setAttribute(M3UA_EXCHANGE_TYPE, m3ua.m3uaExchangeType.toString());
            xml.setAttribute(M3UA_IPSPType, m3ua.m3uaIPSPType.toString());

            xml.add(m3ua.localHost, LOCAL_HOST, String.class);
            xml.add(m3ua.remoteHost, REMOTE_HOST, String.class);
            if (m3ua.getSctpExtraHostAddresses() != null && !m3ua.getSctpExtraHostAddresses().equals("")) {
                xml.add(m3ua.getSctpExtraHostAddresses(), EXTRA_HOST_ADDRESSES, String.class);
            }
        }

        public void read(InputElement xml, M3uaConfigurationData m3ua) throws XMLStreamException {
            CharArray ca = xml.getAttribute(STORE_PCAP_TRACE);
            if (ca != null)
                m3ua.storePcapTrace = ca.toBoolean();

            m3ua.isSctpServer = xml.getAttribute(IS_SCTP_SERVER).toBoolean();
            m3ua.localPort = xml.getAttribute(LOCAL_PORT).toInt();
            m3ua.remotePort = xml.getAttribute(REMOTE_PORT).toInt();
            String str = xml.getAttribute(IP_CHANNEL_TYPE).toString();
            m3ua.ipChannelType = IpChannelType.valueOf(str);
            m3ua.dpc = xml.getAttribute(DPC).toInt();
            m3ua.opc = xml.getAttribute(OPC).toInt();
            m3ua.si = xml.getAttribute(SI).toInt();
            m3ua.routingContext = xml.getAttribute(ROUTING_CONTEXT).toInt();
            m3ua.networkAppearance = xml.getAttribute(NETWORK_APPEARANCE).toInt();
            str = xml.getAttribute(M3UA_FUNCTIONALITY).toString();
            m3ua.m3uaFunctionality = Functionality.valueOf(str);
            str = xml.getAttribute(M3UA_EXCHANGE_TYPE).toString();
            m3ua.m3uaExchangeType = ExchangeType.valueOf(str);
            str = xml.getAttribute(M3UA_IPSPType).toString();
            m3ua.m3uaIPSPType = IPSPType.valueOf(str);

            m3ua.localHost = (String) xml.get(LOCAL_HOST, String.class);
            m3ua.remoteHost = (String) xml.get(REMOTE_HOST, String.class);
            m3ua.setSctpExtraHostAddresses((String) xml.get(EXTRA_HOST_ADDRESSES, String.class));
        }
    };
}
