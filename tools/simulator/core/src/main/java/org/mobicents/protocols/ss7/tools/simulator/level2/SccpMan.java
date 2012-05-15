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

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPart;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.SccpStack;
import org.mobicents.protocols.ss7.sccp.impl.RemoteSignalingPointCode;
import org.mobicents.protocols.ss7.sccp.impl.RemoteSubSystem;
import org.mobicents.protocols.ss7.sccp.impl.SccpResource;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.router.Mtp3Destination;
import org.mobicents.protocols.ss7.sccp.impl.router.Mtp3ServiceAccessPoint;
import org.mobicents.protocols.ss7.sccp.impl.router.Router;
import org.mobicents.protocols.ss7.sccp.impl.router.Rule;
import org.mobicents.protocols.ss7.sccp.impl.router.RuleType;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tools.simulator.Stoppable;
import org.mobicents.protocols.ss7.tools.simulator.management.TesterHost;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class SccpMan implements SccpManMBean, Stoppable {

	public static String SOURCE_NAME = "SCCP";

	private static final String DPC = "dpc";
	private static final String OPC = "opc";
	private static final String NI = "ni";
	private static final String REMOTE_SSN = "remoteSsn";

	private final String name;
	private TesterHost testerHost;

	private Mtp3UserPart mtp3UserPart;
	private int remoteSsn;
	private int dpc = 0;
	private int opc = 0;
	private int ni = 0;

	private SccpStackImpl sccpStack;
	private SccpProvider sccpProvider;
	private SccpResource resource;
	private Router router;
	private boolean isRspcUp = true;
	private boolean isRssUp = true;
	
	public SccpMan() {
		this.name = "???";
	}

	public SccpMan(String name) {
		this.name = name;
	}

	public void setTesterHost(TesterHost testerHost) {
		this.testerHost = testerHost;
	}

	public void setMtp3UserPart(Mtp3UserPart val) {
		this.mtp3UserPart = val;
	}	

	public SccpStack getSccpStack() {
		return this.sccpStack;
	}	


	@Override
	public int getDpc() {
		return dpc;
	}

	@Override
	public void setDpc(int val) {
		dpc = val;
		this.testerHost.markStore();
	}

	@Override
	public int getOpc() {
		return opc;
	}

	@Override
	public void setOpc(int val) {
		opc = val;
		this.testerHost.markStore();
	}

	@Override
	public int getNi() {
		return ni;
	}

	@Override
	public void setNi(int val) {
		ni = val;
		this.testerHost.markStore();
	}

	@Override
	public int getRemoteSsn() {
		return this.remoteSsn;
	}

	@Override
	public void setRemoteSsn(int val) {
		this.remoteSsn = val;
		this.testerHost.markStore();
	}

	@Override
	public String getState() {
		StringBuilder sb = new StringBuilder();
		sb.append("SCCP: Rspc: ");
		sb.append(this.isRspcUp ? "Enabled" : "Disabled");
		sb.append("  Rss: ");
		sb.append(this.isRssUp ? "Enabled" : "Disabled");
		return sb.toString();
	}


	public boolean start() {
		try {
			this.isRspcUp = true;
			this.isRssUp = true;
			this.initSccp(this.mtp3UserPart, this.remoteSsn, this.dpc, this.opc, this.ni);
			this.testerHost.sendNotif(SOURCE_NAME, "SCCP has been started", "", true);
			return true;
		} catch (Throwable e) {
			this.testerHost.sendNotif(SOURCE_NAME, "Exception when starting SccpMan", e, true);
			return false;
		}
	}

	@Override
	public void stop() {
		try {
			this.stopSccp();
			this.testerHost.sendNotif(SOURCE_NAME, "SCCP has been stopped", "", true);
		} catch (Exception e) {
			this.testerHost.sendNotif(SOURCE_NAME, "Exception when stopping SccpMan", e, true);
		}
	}

	@Override
	public void execute() {
		if (this.resource != null) {
			RemoteSignalingPointCode rspc = this.resource.getRemoteSpc(1);
			RemoteSubSystem rss = this.resource.getRemoteSsn(1);
			if (rspc != null) {
				boolean conn = !rspc.isRemoteSpcProhibited();
				if (this.isRspcUp != conn) {
					this.isRspcUp = conn;
					this.testerHost.sendNotif(SOURCE_NAME, "SCCP RemoteSignalingPoint is " + (conn ? "enabled" : "disabled"), "Dpc=" + this.dpc, true);
				}
			}			
			if (rss != null) {
				boolean conn = !rss.isRemoteSsnProhibited();
				if (this.isRssUp != conn) {
					this.isRssUp = conn;
					this.testerHost.sendNotif(SOURCE_NAME, "SCCP RemoteSubSystem is " + (conn ? "enabled" : "disabled"), "Dpc=" + this.dpc + " Ssn="
							+ this.remoteSsn, true);
				}
			}
		}
	}

	private void initSccp(Mtp3UserPart mtp3UserPart, int remoteSsn, int dpc, int opc, int ni) {

		this.sccpStack = new SccpStackImpl("TestingSccp");

		this.sccpStack.setMtp3UserPart(1, mtp3UserPart);
		this.sccpStack.start();
		this.sccpStack.removeAllResourses();

		Mtp3ServiceAccessPoint sap = new Mtp3ServiceAccessPoint(1, opc, ni);
		this.sccpStack.getRouter().addMtp3ServiceAccessPoint(1, sap);
		Mtp3Destination dest = new Mtp3Destination(dpc, dpc, 0, 255, 255);
		this.sccpStack.getRouter().addMtp3Destination(1, 1, dest);

		this.sccpProvider = this.sccpStack.getSccpProvider();

		// router1 = sccpStack1.getRouter();

		this.resource = this.sccpStack.getSccpResource();

		this.resource.addRemoteSpc(1, new RemoteSignalingPointCode(dpc, 0, 0));
		this.resource.addRemoteSsn(1, new RemoteSubSystem(dpc, remoteSsn, 0, false));

		// ..............................
		this.router = this.sccpStack.getRouter();
		this.router.addPrimaryAddress(1, new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, dpc, null, remoteSsn));
		SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance("*"), 0);
		String mask = "R";
		Rule rule = new Rule(RuleType.Solitary, null, pattern, mask);
		this.router.addRule(1, rule);
		// ..............................
	}

	private void stopSccp() {

		this.sccpStack.removeAllResourses();
		this.sccpStack.stop();
	}

	protected static final XMLFormat<SccpMan> XML = new XMLFormat<SccpMan>(SccpMan.class) {

		public void write(SccpMan sccp, OutputElement xml) throws XMLStreamException {
			xml.setAttribute(DPC, sccp.dpc);
			xml.setAttribute(OPC, sccp.opc);
			xml.setAttribute(NI, sccp.ni);
			xml.setAttribute(REMOTE_SSN, sccp.remoteSsn);
		}

		public void read(InputElement xml, SccpMan sccp) throws XMLStreamException {
			sccp.dpc = xml.getAttribute(DPC).toInt();
			sccp.opc = xml.getAttribute(OPC).toInt();
			sccp.ni = xml.getAttribute(NI).toInt();
			sccp.remoteSsn = xml.getAttribute(REMOTE_SSN).toInt();
		}
	};
}

