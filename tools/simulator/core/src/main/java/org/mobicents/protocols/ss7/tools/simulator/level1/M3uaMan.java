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

import javolution.util.FastList;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;
import org.mobicents.protocols.api.Association;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.sctp.ManagementImpl;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.IPSPType;
import org.mobicents.protocols.ss7.m3ua.impl.As;
import org.mobicents.protocols.ss7.m3ua.impl.AsState;
import org.mobicents.protocols.ss7.m3ua.impl.Asp;
import org.mobicents.protocols.ss7.m3ua.impl.AspFactory;
import org.mobicents.protocols.ss7.m3ua.impl.M3UAManagement;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPart;
import org.mobicents.protocols.ss7.tools.simulator.Stoppable;
import org.mobicents.protocols.ss7.tools.simulator.management.TesterHost;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class M3uaMan implements M3uaManMBean, Stoppable {

	public static String SOURCE_NAME = "M3UA";

	private static final String IS_SCTP_SERVER = "isSctpServer";
	private static final String LOCAL_HOST = "localHost";
	private static final String LOCAL_PORT = "localPort";
	private static final String REMOTE_HOST = "remoteHost";
	private static final String REMOTE_PORT = "remotePort";
	private static final String IP_CHANNEL_TYPE = "ipChannelType";
	private static final String EXTRA_HOST_ADDRESSES = "extraHostAddresses";

	private static final String M3UA_FUNCTIONALITY = "m3uaFunctionality";
	private static final String M3UA_EXCHANGE_TYPE = "m3uaExchangeType";
	private static final String M3UA_IPSPType = "m3uaIPSPType";
	private static final String DPC = "dpc";
	private static final String OPC = "opc";
	private static final String SI = "si";
	private static final String ROUTING_CONTEXT = "routingConext";
	private static final String NETWORK_APPEARANCE = "networkAppearance";

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
	private long routingConext = 101;
	private long networkAppearance = 102;

	private final String name;
	private TesterHost testerHost;
	private ManagementImpl sctpManagement;
	private ParameterFactoryImpl factory = new ParameterFactoryImpl();
	private M3UAManagement m3uaMgmt; 
	private boolean isSctpConnectionUp = false;
	private boolean isM3uaConnectionActive = false;
	private Association assoc;
	
	private Functionality m3uaFunctionality = Functionality.IPSP;
	private ExchangeType m3uaExchangeType = ExchangeType.SE;
	private IPSPType m3uaIPSPType = IPSPType.CLIENT;
	private As localAs;
	private Asp localAsp;
	private AspFactory localAspFactory;


	public M3uaMan() {
		this.name = "???";
	}

	public M3uaMan(String name) {
		this.name = name;
	}

	public void setTesterHost(TesterHost testerHost) {
		this.testerHost = testerHost;
	}

	@Override
	public String getSctpLocalHost() {
		return localHost;
	}

	@Override
	public void setSctpLocalHost(String val) {
		localHost = val;
		this.testerHost.markStore();
	}

	@Override
	public int getSctpLocalPort() {
		return localPort;
	}

	@Override
	public void setSctpLocalPort(int val) {
		localPort = val;
		this.testerHost.markStore();
	}

	@Override
	public String getSctpRemoteHost() {
		return remoteHost;
	}

	@Override
	public void setSctpRemoteHost(String val) {
		remoteHost = val;
		this.testerHost.markStore();
	}

	@Override
	public int getSctpRemotePort() {
		return remotePort;
	}

	@Override
	public void setSctpRemotePort(int val) {
		remotePort = val;
		this.testerHost.markStore();
	}

	@Override
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

	@Override
	public void setSctpExtraHostAddresses(String val) {
		this.doSetExtraHostAddresses(val);
		this.testerHost.markStore();
	}

	public void doSetExtraHostAddresses(String val) {
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

	@Override
	public boolean isSctpIsServer() {
		return isSctpServer;
	}

	@Override
	public void setSctpIsServer(boolean val) {
		isSctpServer = val;
		this.testerHost.markStore();
	}

	@Override
	public BIpChannelType getSctpIPChannelType() {
		if (ipChannelType == IpChannelType.TCP)
			return new BIpChannelType(BIpChannelType.VAL_TCP);
		else
			return new BIpChannelType(BIpChannelType.VAL_SCTP);
	}

	@Override
	public void setSctpIPChannelType(BIpChannelType val) {
		if (val.intValue() == BIpChannelType.VAL_TCP)
			ipChannelType = IpChannelType.TCP;
		else
			ipChannelType = IpChannelType.SCTP;
		this.testerHost.markStore();
	}

	@Override
	public String getSctpIPChannelType_Value() {
		return this.ipChannelType.toString();
	}


	@Override
	public M3uaFunctionality getM3uaFunctionality() {
		if (m3uaFunctionality == Functionality.IPSP) {
			return new M3uaFunctionality(M3uaFunctionality.VAL_IPSP);
		} else if (m3uaFunctionality == Functionality.AS) {
			return new M3uaFunctionality(M3uaFunctionality.VAL_AS);
		} else {
			return new M3uaFunctionality(M3uaFunctionality.VAL_SGW);
		}
	}

	@Override
	public void setM3uaFunctionality(M3uaFunctionality val) {
		if (val.intValue() == M3uaFunctionality.VAL_IPSP)
			m3uaFunctionality = Functionality.IPSP;
		else if (val.intValue() == M3uaFunctionality.VAL_AS)
			m3uaFunctionality = Functionality.AS;
		else
			m3uaFunctionality = Functionality.SGW;
		this.testerHost.markStore();
	}

	@Override
	public String getM3uaFunctionality_Value() {
		return this.m3uaFunctionality.toString();
	}

	@Override
	public M3uaIPSPType getM3uaIPSPType() {
		if (m3uaIPSPType == IPSPType.CLIENT) {
			return new M3uaIPSPType(M3uaIPSPType.VAL_CLIENT);
		} else {
			return new M3uaIPSPType(M3uaIPSPType.VAL_SERVER);
		}
	}

	@Override
	public void setM3uaIPSPType(M3uaIPSPType val) {
		if (val.intValue() == M3uaIPSPType.VAL_CLIENT)
			m3uaIPSPType = IPSPType.CLIENT;
		else
			m3uaIPSPType = IPSPType.SERVER;
		this.testerHost.markStore();
	}

	@Override
	public String getM3uaIPSPType_Value() {
		return this.m3uaIPSPType.toString();
	}

	@Override
	public M3uaExchangeType getM3uaExchangeType() {
		if (m3uaExchangeType == ExchangeType.SE) {
			return new M3uaExchangeType(M3uaExchangeType.VAL_SE);
		} else {
			return new M3uaExchangeType(M3uaExchangeType.VAL_DE);
		}
	}

	@Override
	public void setM3uaExchangeType(M3uaExchangeType val) {
		if (val.intValue() == M3uaExchangeType.VAL_SE)
			m3uaExchangeType = ExchangeType.SE;
		else
			m3uaExchangeType = ExchangeType.DE;
		this.testerHost.markStore();
		
	}

	@Override
	public String getM3uaExchangeType_Value() {
		return this.m3uaExchangeType.toString();
	}

	@Override
	public int getM3uaDpc() {
		return dpc;
	}

	@Override
	public void setM3uaDpc(int val) {
		dpc = val;
		this.testerHost.markStore();
	}

	@Override
	public int getM3uaOpc() {
		return opc;
	}

	@Override
	public void setM3uaOpc(int val) {
		opc = val;
		this.testerHost.markStore();
	}

	@Override
	public int getM3uaSi() {
		return si;
	}

	@Override
	public void setM3uaSi(int val) {
		si = val;
		this.testerHost.markStore();
	}

	@Override
	public long getM3uaRoutingContext() {
		return this.routingConext;
	}

	@Override
	public String getState() {
		StringBuilder sb = new StringBuilder();
		sb.append("SCTP: ");
		sb.append(this.isSctpConnectionUp ? "Connected" : "Disconnected");
		sb.append("  M3UA:");

		this.m3uaMgmt.getAppServers();
		FastList<As> lstAs = this.m3uaMgmt.getAppServers();
		for (As as : lstAs) {
			if (as.getName().equals("testas")) {
				FSM lFsm = as.getLocalFSM();
				FSM pFsm = as.getPeerFSM();
				FSM lFsmP = null;
				FSM pFsmP = null;

				FastList<Asp> lstAsp = as.getAspList();
				for (Asp asp : lstAsp) {
					// we take only the first ASP (it should be a single)
					lFsmP = asp.getLocalFSM();
					pFsmP = asp.getPeerFSM();
					break;
				}

				if (lFsm != null) {
					sb.append(" lFsm:");
					sb.append(lFsm.getState().toString());
				}
				if (pFsm != null) {
					sb.append(" pFsm:");
					sb.append(pFsm.getState().toString());
				}
				if (lFsmP != null) {
					sb.append(" lFsmP:");
					sb.append(lFsmP.getState().toString());
				}
				if (pFsmP != null) {
					sb.append(" pFsmP:");
					sb.append(pFsmP.getState().toString());
				}

				break;
			}
		}

		return sb.toString();
	}

	@Override
	public void setM3uaRoutingContext(long val) {
		this.routingConext = val;
		this.testerHost.markStore();
	}

	@Override
	public long getM3uaNetworkAppearance() {
		return this.networkAppearance;
	}

	@Override
	public void setM3uaNetworkAppearance(long val) {
		this.networkAppearance = val;
		this.testerHost.markStore();
	}


	@Override
	public void putSctpIPChannelType(String val) {
		BIpChannelType x = BIpChannelType.createInstance(val);
		if (x != null)
			this.setSctpIPChannelType(x);
	}

	@Override
	public void putM3uaFunctionality(String val) {
		M3uaFunctionality x = M3uaFunctionality.createInstance(val);
		if (x != null)
			this.setM3uaFunctionality(x);
	}

	@Override
	public void putM3uaIPSPType(String val) {
		M3uaIPSPType x = M3uaIPSPType.createInstance(val);
		if (x != null)
			this.setM3uaIPSPType(x);
	}

	@Override
	public void putM3uaExchangeType(String val) {
		M3uaExchangeType x = M3uaExchangeType.createInstance(val);
		if (x != null)
			this.setM3uaExchangeType(x);
	}


	public boolean start() {
		try {
			this.isSctpConnectionUp = false;
			this.isM3uaConnectionActive = false;
			this.initM3ua(this.isSctpServer, this.localHost, this.localPort, this.remoteHost, this.remotePort, this.ipChannelType, this.extraHostAddresses);
			this.testerHost.sendNotif(SOURCE_NAME, "M3UA has been started", "", true);
			return true;
		} catch (Throwable e) {
			this.testerHost.sendNotif(SOURCE_NAME, "Exception when starting M3uaMan", e, true);
			return false;
		}
	}

	public void stop() {
		try {
			this.stopM3ua();
			this.testerHost.sendNotif(SOURCE_NAME, "M3UA has been stopped", "", true);
		} catch (Exception e) {
			this.testerHost.sendNotif(SOURCE_NAME, "Exception when stopping M3uaMan", e, true);
		}
	}

	@Override
	public void execute() {
		if (this.assoc != null) {
			boolean conn = this.assoc.isConnected();
			if (this.isSctpConnectionUp != conn) {
				this.isSctpConnectionUp = conn;
				this.testerHost.sendNotif(SOURCE_NAME, "Sctp connection is " + (conn ? "up" : "down"), this.assoc.getName(), true);
			}
		}
		if (this.m3uaMgmt != null) {
			boolean active = false;
			FastList<As> lstAs = this.m3uaMgmt.getAppServers();
			for (As as : lstAs) {
				if (as.getName().equals("testas")) {
					FSM lFsm = as.getLocalFSM();
					FSM pFsm = as.getPeerFSM();
					if ((lFsm == null || lFsm.getState().getName().equals(AsState.ACTIVE.toString()))
							&& (pFsm == null || pFsm.getState().getName().equals(AsState.ACTIVE.toString()))) {
						active = true;
					}
					break;
				}
			}
			if (this.isM3uaConnectionActive != active) {
				this.isM3uaConnectionActive = active;
				this.testerHost.sendNotif(SOURCE_NAME, "M3ua connection is " + (active ? "active" : "not active"), this.assoc.getName(), true);
			}
		}
	}

	private void initM3ua(boolean isSctpServer, String localHost, int localPort, String remoteHost, int remotePort, IpChannelType ipChannelType,
			String[] extraHostAddresses) throws Exception {

		this.stopM3ua();

		// init SCTP stack
		this.sctpManagement = new ManagementImpl("SimSCTPServer_" + name);
		this.sctpManagement.setSingleThread(true);
		this.sctpManagement.setConnectDelay(10000);

		this.sctpManagement.start();
		this.sctpManagement.removeAllResourses();

		// init M3UA stack
		this.m3uaMgmt = new M3UAManagement("SimM3uaServer_" + name);
		this.m3uaMgmt.setTransportManagement(this.sctpManagement);
		this.m3uaMgmt.start();
		this.m3uaMgmt.removeAllResourses();

		// configure SCTP stack
		String SERVER_NAME = "Server_" + name;
		String SERVER_ASSOCIATION_NAME = "ServerAss_" + name;
		String ASSOCIATION_NAME = "Ass_" + name;
		String assName;

		if (isSctpServer) {

			// 1. Create SCTP Server
			sctpManagement.addServer(SERVER_NAME, localHost, localPort, ipChannelType, extraHostAddresses);

			// 2. Create SCTP Server Association
			sctpManagement.addServerAssociation(remoteHost, remotePort, SERVER_NAME, SERVER_ASSOCIATION_NAME, ipChannelType);
			this.assoc = sctpManagement.getAssociation(SERVER_ASSOCIATION_NAME);
			assName = SERVER_ASSOCIATION_NAME;

			// 3. Start Server
			sctpManagement.startServer(SERVER_NAME);
		} else {

			// 1. Create SCTP Association
			sctpManagement.addAssociation(localHost, localPort, remoteHost, remotePort, ASSOCIATION_NAME, ipChannelType, extraHostAddresses);
			this.assoc = sctpManagement.getAssociation(ASSOCIATION_NAME); 
			assName = ASSOCIATION_NAME;
		}

		// configure M3UA stack
		// 1. Create AS
		RoutingContext rc = factory.createRoutingContext(new long[] { this.routingConext });
		TrafficModeType trafficModeType = factory.createTrafficModeType(TrafficModeType.Loadshare);
		NetworkAppearance na = factory.createNetworkAppearance(this.networkAppearance);
		localAs = m3uaMgmt.createAs("testas", this.m3uaFunctionality, this.m3uaExchangeType, this.m3uaIPSPType, rc, trafficModeType, na);

		// 2. Create ASP
		localAspFactory = m3uaMgmt.createAspFactory("testasp", assName);

		// 3. Assign ASP to AS
		localAsp = m3uaMgmt.assignAspToAs("testas", "testasp");

		// 4. Define Route
		// Define Route
		m3uaMgmt.addRoute(this.dpc, this.opc, this.si, "testas");

		// starting resources
		// 1. Start Association
//		sctpManagement.startAssociation(assName);

		// 2. Start ASP
		m3uaMgmt.startAsp("testasp");

	}

	private void stopM3ua() throws Exception {
		if (this.m3uaMgmt != null) {
			this.m3uaMgmt.stop();
			this.m3uaMgmt = null;
		}
		if (this.sctpManagement != null) {
			this.sctpManagement.stop();
			this.sctpManagement = null;
		}
	}

	public Mtp3UserPart getMtp3UserPart() {
		return this.m3uaMgmt;
	}	

	protected static final XMLFormat<M3uaMan> XML = new XMLFormat<M3uaMan>(M3uaMan.class) {

		public void write(M3uaMan m3ua, OutputElement xml) throws XMLStreamException {
			xml.setAttribute(IS_SCTP_SERVER, m3ua.isSctpServer);
			xml.setAttribute(LOCAL_PORT, m3ua.localPort);
			xml.setAttribute(REMOTE_PORT, m3ua.remotePort);
			xml.setAttribute(IP_CHANNEL_TYPE, m3ua.ipChannelType.toString());
			xml.setAttribute(DPC, m3ua.dpc);
			xml.setAttribute(OPC, m3ua.opc);
			xml.setAttribute(SI, m3ua.si);
			xml.setAttribute(ROUTING_CONTEXT, m3ua.routingConext);
			xml.setAttribute(NETWORK_APPEARANCE, m3ua.networkAppearance);
			xml.setAttribute(M3UA_FUNCTIONALITY, m3ua.m3uaFunctionality.toString());
			xml.setAttribute(M3UA_EXCHANGE_TYPE, m3ua.m3uaExchangeType.toString());
			xml.setAttribute(M3UA_IPSPType, m3ua.m3uaIPSPType.toString());

			xml.add(m3ua.localHost, LOCAL_HOST);
			xml.add(m3ua.remoteHost, REMOTE_HOST);
			xml.add(m3ua.getSctpExtraHostAddresses(), EXTRA_HOST_ADDRESSES);
		}

		public void read(InputElement xml, M3uaMan m3ua) throws XMLStreamException {
			m3ua.isSctpServer = xml.getAttribute(IS_SCTP_SERVER).toBoolean();
			m3ua.localPort = xml.getAttribute(LOCAL_PORT).toInt();
			m3ua.remotePort = xml.getAttribute(REMOTE_PORT).toInt();
			String str = xml.getAttribute(IP_CHANNEL_TYPE).toString();
			m3ua.ipChannelType = IpChannelType.valueOf(str);
			m3ua.dpc = xml.getAttribute(DPC).toInt();
			m3ua.opc = xml.getAttribute(OPC).toInt();
			m3ua.si = xml.getAttribute(SI).toInt();
			m3ua.routingConext = xml.getAttribute(ROUTING_CONTEXT).toInt();
			m3ua.networkAppearance = xml.getAttribute(NETWORK_APPEARANCE).toInt();
			str = xml.getAttribute(M3UA_FUNCTIONALITY).toString();
			m3ua.m3uaFunctionality = Functionality.valueOf(str);
			str = xml.getAttribute(M3UA_EXCHANGE_TYPE).toString();
			m3ua.m3uaExchangeType = ExchangeType.valueOf(str);
			str = xml.getAttribute(M3UA_IPSPType).toString();
			m3ua.m3uaIPSPType = IPSPType.valueOf(str);

			m3ua.localHost = (String) xml.get(LOCAL_HOST, String.class);
			m3ua.remoteHost = (String) xml.get(REMOTE_HOST, String.class);
			m3ua.doSetExtraHostAddresses((String) xml.get(EXTRA_HOST_ADDRESSES, String.class));
		}
	};
}

