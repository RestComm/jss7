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

package org.mobicents.protocols.ss7.tools.simulator.level3;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.map.MAPStackImpl;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.MAPStack;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.sccp.SccpStack;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tools.simulator.Stoppable;
import org.mobicents.protocols.ss7.tools.simulator.management.TesterHost;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class MapMan implements MapManMBean, Stoppable {

	public static String SOURCE_NAME = "MAP";

	private static final String LOCAL_PC = "localPc";
	private static final String REMOTE_PC = "remotePc";
	private static final String LOCAL_SSN = "localSsn";
	private static final String REMOTE_SSN = "remoteSsn";
	private static final String ORIG_REFERENCE = "origReference";
	private static final String DEST_REFERENCE = "destReference";

	private final String name;
	private TesterHost testerHost;

	private SccpStack sccpStack;

	private int localPc;
	private int remotePc;
	private int localSsn;
	private int remoteSsn;
	private String origReference;
	private String destReference;

	private MAPStackImpl mapStack;
	private MAPProvider mapProvider;


	public MapMan() {
		this.name = "???";
	}

	public MapMan(String name) {
		this.name = name;
	}

	public void setTesterHost(TesterHost testerHost) {
		this.testerHost = testerHost;
	}

	public void setSccpStack(SccpStack val) {
		this.sccpStack = val;
	}	


	@Override
	public int getLocalPc() {
		return localPc;
	}

	@Override
	public void setLocalPc(int val) {
		localPc = val;
		this.testerHost.markStore();
	}

	@Override
	public int getRemotePc() {
		return remotePc;
	}

	@Override
	public void setRemotePc(int val) {
		remotePc = val;
		this.testerHost.markStore();
	}

	@Override
	public int getRemoteSsn() {
		return remoteSsn;
	}

	@Override
	public void setRemoteSsn(int val) {
		remoteSsn = val;
		this.testerHost.markStore();
	}

	@Override
	public int getLocalSsn() {
		return localSsn;
	}

	@Override
	public void setLocalSsn(int val) {
		localSsn = val;
		this.testerHost.markStore();
	}

	@Override
	public String getOrigReference() {
		return origReference;
	}

	@Override
	public void setOrigReference(String val) {
		origReference = val;
		this.testerHost.markStore();
	}

	@Override
	public String getDestReference() {
		return destReference;
	}

	@Override
	public void setDestReference(String val) {
		destReference = val;
		this.testerHost.markStore();
	}


	@Override
	public String getState() {
		StringBuilder sb = new StringBuilder();
		sb.append("TCAP+MAP: Started");
		return sb.toString();
	}


	public boolean start() {
		try {
			this.initMap(this.sccpStack, this.localSsn);
			this.testerHost.sendNotif(SOURCE_NAME, "TCAP+MAP has been started", "", true);
			return true;
		} catch (Throwable e) {
			this.testerHost.sendNotif(SOURCE_NAME, "Exception when starting MapMan", e, true);
			return false;
		}
	}

	@Override
	public void stop() {
		try {
			this.stopMap();
			this.testerHost.sendNotif(SOURCE_NAME, "TCAP+MAP has been stopped", "", true);
		} catch (Exception e) {
			this.testerHost.sendNotif(SOURCE_NAME, "Exception when stopping MapMan", e, true);
		}
	}

	@Override
	public void execute() {
	}

	private void initMap(SccpStack sccpStack, int ssn) {

		this.mapStack = new MAPStackImpl(sccpStack.getSccpProvider(), ssn);
		this.mapStack.start();
	}

	private void stopMap() {

		this.mapStack.stop();
	}

	public MAPStack getMAPStack() {
		return this.mapStack;
	}
	
	public SccpAddress createOrigAddress() {
		return new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, this.localPc, null, this.localSsn);
	}

	public SccpAddress createDestAddress() {
		return new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, this.remotePc, null, this.remoteSsn);
	}

	public AddressString createOrigReference() {
		if (this.origReference == null || this.origReference.equals(""))
			return null;
		else
			return new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, this.origReference);
	}

	public AddressString createDestReference() {
		if (this.destReference == null || this.destReference.equals(""))
			return null;
		else
			return new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, this.destReference);
	}

	protected static final XMLFormat<MapMan> XML = new XMLFormat<MapMan>(MapMan.class) {

		public void write(MapMan sccp, OutputElement xml) throws XMLStreamException {
			xml.setAttribute(LOCAL_PC, sccp.localPc);
			xml.setAttribute(REMOTE_PC, sccp.remotePc);
			xml.setAttribute(LOCAL_SSN, sccp.localSsn);
			xml.setAttribute(REMOTE_SSN, sccp.remoteSsn);

			xml.add(sccp.origReference, ORIG_REFERENCE);
			xml.add(sccp.destReference, DEST_REFERENCE);
		}

		public void read(InputElement xml, MapMan sccp) throws XMLStreamException {
			sccp.localPc = xml.getAttribute(LOCAL_PC).toInt();
			sccp.remotePc = xml.getAttribute(REMOTE_PC).toInt();
			sccp.localSsn = xml.getAttribute(LOCAL_SSN).toInt();
			sccp.remoteSsn = xml.getAttribute(REMOTE_SSN).toInt();

			sccp.origReference = (String) xml.get(ORIG_REFERENCE, String.class);
			sccp.destReference = (String) xml.get(DEST_REFERENCE, String.class);
		}
	};
}

