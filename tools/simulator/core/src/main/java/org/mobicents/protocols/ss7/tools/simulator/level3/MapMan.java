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

import org.apache.log4j.Level;
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
import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.common.NumberingPlanType;
import org.mobicents.protocols.ss7.tools.simulator.management.TesterHost;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class MapMan implements MapManMBean, Stoppable {

	public static String SOURCE_NAME = "MAP";

	private static final String LOCAL_SSN = "localSsn";
	private static final String REMOTE_SSN = "remoteSsn";
	private static final String REMOTE_ADDRESS_DIGITS = "remoteAddressDigits";
	private static final String ORIG_REFERENCE = "origReference";
	private static final String ORIG_REFERENCE_ADDRESS_NATURE = "origReferenceAddressNature";
	private static final String ORIG_REFERENCE_NUMBERING_PLAN = "origReferenceNumberingPlan";
	private static final String DEST_REFERENCE = "destReference";
	private static final String DEST_REFERENCE_ADDRESS_NATURE = "destReferenceAddressNature";
	private static final String DEST_REFERENCE_NUMBERING_PLAN = "destReferenceNumberingPlan";

	private final String name;
	private TesterHost testerHost;

	private SccpStack sccpStack;

	private int localSsn;
	private int remoteSsn;
	private String remoteAddressDigits;
	
	private String origReference;
	private AddressNature origReferenceAddressNature = AddressNature.international_number;
	private NumberingPlan origReferenceNumberingPlan = NumberingPlan.ISDN;
	private String destReference;
	private AddressNature destReferenceAddressNature = AddressNature.international_number;
	private NumberingPlan destReferenceNumberingPlan = NumberingPlan.ISDN;

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
	public String getRemoteAddressDigits() {
		return remoteAddressDigits;
	}

	@Override
	public void setRemoteAddressDigits(String val) {
		remoteAddressDigits = val;
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
	public AddressNatureType getOrigReferenceAddressNature() {
		return new AddressNatureType(origReferenceAddressNature.getIndicator());
	}

	@Override
	public String getOrigReferenceAddressNature_Value() {
		return origReferenceAddressNature.toString();
	}

	@Override
	public void setOrigReferenceAddressNature(AddressNatureType val) {
		origReferenceAddressNature = AddressNature.getInstance(val.intValue());
		this.testerHost.markStore();
	}

	@Override
	public NumberingPlanType getOrigReferenceNumberingPlan() {
		return new NumberingPlanType(origReferenceNumberingPlan.getIndicator());
	}

	@Override
	public String getOrigReferenceNumberingPlan_Value() {
		return origReferenceNumberingPlan.toString();
	}

	@Override
	public void setOrigReferenceNumberingPlan(NumberingPlanType val) {
		origReferenceNumberingPlan = NumberingPlan.getInstance(val.intValue());
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
	public AddressNatureType getDestReferenceAddressNature() {
		return new AddressNatureType(destReferenceAddressNature.getIndicator());
	}

	@Override
	public String getDestReferenceAddressNature_Value() {
		return destReferenceAddressNature.toString();
	}

	@Override
	public void setDestReferenceAddressNature(AddressNatureType val) {
		destReferenceAddressNature = AddressNature.getInstance(val.intValue());
		this.testerHost.markStore();
	}

	@Override
	public NumberingPlanType getDestReferenceNumberingPlan() {
		return new NumberingPlanType(destReferenceNumberingPlan.getIndicator());
	}

	@Override
	public String getDestReferenceNumberingPlan_Value() {
		return destReferenceNumberingPlan.toString();
	}

	@Override
	public void setDestReferenceNumberingPlan(NumberingPlanType val) {
		destReferenceNumberingPlan = NumberingPlan.getInstance(val.intValue());
		this.testerHost.markStore();
	}

	@Override
	public void putOrigReferenceAddressNature(String val) {
		AddressNatureType x = AddressNatureType.createInstance(val);
		if (x != null)
			this.setOrigReferenceAddressNature(x);
	}

	@Override
	public void putOrigReferenceNumberingPlan(String val) {
		NumberingPlanType x = NumberingPlanType.createInstance(val);
		if (x != null)
			this.setOrigReferenceNumberingPlan(x);
	}

	@Override
	public void putDestReferenceAddressNature(String val) {
		AddressNatureType x = AddressNatureType.createInstance(val);
		if (x != null)
			this.setDestReferenceAddressNature(x);
	}

	@Override
	public void putDestReferenceNumberingPlan(String val) {
		NumberingPlanType x = NumberingPlanType.createInstance(val);
		if (x != null)
			this.setDestReferenceNumberingPlan(x);
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
			this.testerHost.sendNotif(SOURCE_NAME, "TCAP+MAP has been started", "", Level.INFO);
			return true;
		} catch (Throwable e) {
			this.testerHost.sendNotif(SOURCE_NAME, "Exception when starting MapMan", e, Level.ERROR);
			return false;
		}
	}

	@Override
	public void stop() {
		try {
			this.stopMap();
			this.testerHost.sendNotif(SOURCE_NAME, "TCAP+MAP has been stopped", "", Level.INFO);
		} catch (Exception e) {
			this.testerHost.sendNotif(SOURCE_NAME, "Exception when stopping MapMan", e, Level.ERROR);
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
		return this.testerHost.getSccpMan().createCallingPartyAddress();
	}

	public SccpAddress createDestAddress() {
		if (this.remoteAddressDigits == null || this.remoteAddressDigits.equals("")) {
			return this.testerHost.getSccpMan().createCalledPartyAddress();
		} else {
			return this.testerHost.getSccpMan().createCalledPartyAddress(this.remoteAddressDigits, this.remoteSsn);
		}
	}

	public SccpAddress createDestAddress(String address, int ssn) {
		return this.testerHost.getSccpMan().createCalledPartyAddress(address, ssn);
	}

	public AddressString createOrigReference() {
		if (this.origReference == null || this.origReference.equals(""))
			return null;
		else
			return new AddressStringImpl(this.origReferenceAddressNature, this.origReferenceNumberingPlan, this.origReference);
	}

	public AddressString createDestReference() {
		if (this.destReference == null || this.destReference.equals(""))
			return null;
		else
			return new AddressStringImpl(this.destReferenceAddressNature, this.destReferenceNumberingPlan, this.destReference);
	}

	protected static final XMLFormat<MapMan> XML = new XMLFormat<MapMan>(MapMan.class) {

		public void write(MapMan map, OutputElement xml) throws XMLStreamException {
			xml.setAttribute(LOCAL_SSN, map.localSsn);
			xml.setAttribute(REMOTE_SSN, map.remoteSsn);

			xml.add(map.remoteAddressDigits, REMOTE_ADDRESS_DIGITS);
			xml.add(map.origReference, ORIG_REFERENCE);
			xml.add(map.destReference, DEST_REFERENCE);

			xml.add(map.origReferenceAddressNature.toString(), ORIG_REFERENCE_ADDRESS_NATURE);
			xml.add(map.origReferenceNumberingPlan.toString(), ORIG_REFERENCE_NUMBERING_PLAN);
			xml.add(map.destReferenceAddressNature.toString(), DEST_REFERENCE_ADDRESS_NATURE);
			xml.add(map.destReferenceNumberingPlan.toString(), DEST_REFERENCE_NUMBERING_PLAN);
		}

		public void read(InputElement xml, MapMan map) throws XMLStreamException {
			map.localSsn = xml.getAttribute(LOCAL_SSN).toInt();
			map.remoteSsn = xml.getAttribute(REMOTE_SSN).toInt();

			map.remoteAddressDigits = (String) xml.get(REMOTE_ADDRESS_DIGITS, String.class);
			map.origReference = (String) xml.get(ORIG_REFERENCE, String.class);
			map.destReference = (String) xml.get(DEST_REFERENCE, String.class);

			String an = (String) xml.get(ORIG_REFERENCE_ADDRESS_NATURE, String.class);
			map.origReferenceAddressNature = AddressNature.valueOf(an);
			String np = (String) xml.get(ORIG_REFERENCE_NUMBERING_PLAN, String.class);
			map.origReferenceNumberingPlan = NumberingPlan.valueOf(np);
			an = (String) xml.get(DEST_REFERENCE_ADDRESS_NATURE, String.class);
			map.destReferenceAddressNature = AddressNature.valueOf(an);
			np = (String) xml.get(DEST_REFERENCE_NUMBERING_PLAN, String.class);
			map.destReferenceNumberingPlan = NumberingPlan.valueOf(np);
		}
	};

}

