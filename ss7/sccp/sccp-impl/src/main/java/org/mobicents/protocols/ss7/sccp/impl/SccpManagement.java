/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.protocols.ss7.sccp.impl;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpMessageImpl;
import org.mobicents.protocols.ss7.sccp.message.SccpMessage;
import org.mobicents.protocols.ss7.sccp.message.UnitData;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * @author amit bhayani
 * 
 */
public class SccpManagement implements SccpListener {
	private static final Logger logger = Logger.getLogger(SccpManagement.class);

	protected static final int MTP3_PAUSE = 3;
	protected static final int MTP3_RESUME = 4;
	protected static final int MTP3_STATUS = 5;

	protected static final int SSA = 1;
	protected static final int SSP = 2;
	protected static final int SST = 3;
	protected static final int SOR = 4;
	protected static final int SOG = 5;
	protected static final int SSC = 6;

	private static final String S_SSA = "SSA";
	private static final String S_SSP = "SSP";
	private static final String S_SST = "SST";
	private static final String S_SOR = "SOR";
	private static final String S_SOG = "SOG";
	private static final String S_SSC = "SSC";
	private static final String S_DEFAULT = "UNIDENTIFIED";

	protected static final int UNAVAILABILITY_CAUSE_UNKNOWN = 0;
	protected static final int UNAVAILABILITY_CAUSE_UNEQUIPED = 1;
	protected static final int UNAVAILABILITY_CAUSE_INACCESSIBLE = 2;

	private SccpProviderImpl sccpProviderImpl;
	private SccpStackImpl sccpStackImpl;
	private SccpRoutingControl sccpRoutingControl;

	private ScheduledExecutorService managementExecutors;

	// Keeps track of how many SST are running for given DPC
	private final FastMap<Integer, FastList<SubSystemTest>> dpcVsSst = new FastMap<Integer, FastList<SubSystemTest>>();

	public SccpManagement(SccpProviderImpl sccpProviderImpl, SccpStackImpl sccpStackImpl) {
		this.sccpProviderImpl = sccpProviderImpl;
		this.sccpStackImpl = sccpStackImpl;
	}

	public SccpRoutingControl getSccpRoutingControl() {
		return sccpRoutingControl;
	}

	public void setSccpRoutingControl(SccpRoutingControl sccpRoutingControl) {
		this.sccpRoutingControl = sccpRoutingControl;
	}

	/**
	 * <p>
	 * Handles the MTP3/User Part Communication PAUSE, RESUME and STATUS
	 * </p>
	 * <ul>
	 * <li>
	 * The structure of <i>PAUSE</i> is SI=0 (byte), type=3 (byte), affected dpc
	 * = int(4 bytes)</li>
	 * <li>
	 * The structure of <i>RESUME</i> is SI=0 (byte), type=4 (byte), affected
	 * dpc = int(4 bytes)</li>
	 * <li>
	 * The structure of <i>STATUS</i> is SI=0 (byte), type=5 (byte), status=1 or
	 * 2 (byte) where 1 = Remote User Unavailable and 2 = Signaling Network
	 * Congestion, affected dpc = int(4 bytes), congestion status = 2 bytes in
	 * range of 0 to 3 where 0 means no congestion and 3 means maximum
	 * congestion, Unavailabilty cause = 2 bytes (if status = Remote User
	 * Unavailable(1)). The unavailabilty cause may be one of the following: 0 =
	 * Unknown 1 = Unequipped User 2 = Inaccessible User
	 * 
	 * </li>
	 * </ul>
	 * 
	 * @param in
	 */
	protected void handleMtp3Primitive(DataInputStream in) {
		try {
			int mtpParam = in.readUnsignedByte();
			int affectedPc;
			switch (mtpParam) {
			case MTP3_PAUSE:
				affectedPc = in.readInt();
				if (logger.isEnabledFor(Level.WARN)) {
					logger.warn(String.format("MTP3 PAUSE received for dpc=%d", affectedPc));
				}
				this.handleMtp3Pause(affectedPc);
				break;
			case MTP3_RESUME:
				affectedPc = in.readInt();
				if (logger.isInfoEnabled()) {
					logger.info(String.format("MTP3 RESUME received for dpc=%d", affectedPc));
				}
				this.handleMtp3Resume(affectedPc);
				break;
			case MTP3_STATUS:
				int status = in.readUnsignedByte();
				affectedPc = in.readInt();
				int congStatus = in.readShort();
				int unavailabiltyCause = in.readShort();
				if (logger.isEnabledFor(Level.WARN)) {
					logger.warn(String
							.format("MTP3 STATUS received for dpc=%d, status=%d, Congestion Status=%d, Unavailability Cause=%d",
									affectedPc, status, congStatus, unavailabiltyCause));
				}
				this.handleMtp3Status(status, affectedPc, congStatus, unavailabiltyCause);
				break;
			default:
				logger.error(String.format("Received unrecognized MTP3 primitive %d", mtpParam));
				break;
			}
		} catch (IOException e) {
			logger.error("Error while parsing MTP Parameter ", e);
		}
	}

	@Override
	public void onMessage(SccpMessage message, int seqControl) {
		byte[] data = ((UnitData) message).getData();
		int messgType = data[0] & 0xff;
		int affectedSsn = data[1] & 0xff;
		int affectedPc = (data[2] & 0xff) | ((data[3] & 0xff) << 8);
		int subsystemMultiplicity = data[3] & 0xff;

		switch (messgType) {
		case SSA:

			if (logger.isInfoEnabled()) {
				logger.info(String
						.format("Received SCMG message. Message Type=SSA, Affected SSN=%d, Affected PC=%d, Subsystem Multiplicity Ind=%d SeqControl=%d",
								affectedSsn, affectedPc, subsystemMultiplicity, seqControl));
			}

			// Mark remote SSN Allowed
			this.allowSsn(affectedPc, affectedSsn);

			// Stop the SST if already started
			FastList<SubSystemTest> ssts1 = dpcVsSst.get(affectedPc);
			SubSystemTest sst1 = null;
			if (ssts1 != null) {
				for (FastList.Node<SubSystemTest> n = ssts1.head(), end = ssts1.tail(); (n = n.getNext()) != end;) {
					sst1 = n.getValue();
					if (sst1.getSsn() == affectedSsn) {
						break;
					}
				}

				if (sst1 != null) {
					sst1.stopTest();
					// ssts1.remove(sst1);
				}
			}
			break;
		case SSP:
			if (logger.isEnabledFor(Level.WARN)) {
				logger.warn(String
						.format("Received SCMG message. Message Type=SSP, Affected SSN=%d, Affected PC=%d, Subsystem Multiplicity Ind=%d SeqControl=%d",
								affectedSsn, affectedPc, subsystemMultiplicity, seqControl));
			}

			this.prohibitSsn(affectedPc, affectedSsn);
			// Initiate SubSystem Status Test Procedure

			SubSystemTest sst = null;
			FastList<SubSystemTest> ssts = dpcVsSst.get(affectedPc);
			if (ssts == null) {
				ssts = new FastList<SubSystemTest>();
				dpcVsSst.put(affectedPc, ssts);
			}

			for (FastList.Node<SubSystemTest> n = ssts.head(), end = ssts.tail(); (n = n.getNext()) != end;) {
				sst = n.getValue();
				if (sst.getSsn() == affectedSsn) {
					break;
				}
			}

			if (sst == null) {
				sst = new SubSystemTest(affectedSsn, affectedPc, ssts);
			}
			// ssts.add(sst);
			sst.startTest();

			break;
		case SST:
			if (affectedSsn == 1) {
				// In the case where the Subsystem-Status-Test message is
				// testing the status of SCCP management (SSN = 1), if the SCCP
				// at the destination node is functioning, then a Subsystem
				// Allowed message with SSN = 1 is sent to SCCP management at
				// the node conducting the test. If the SCCP is not functioning,
				// then the MTP cannot deliver the SST message to the SCCP. A
				// UPU message is returned to the SST initiating node by the
				// MTP.

				this.sendSSA(message, affectedSsn);
				return;
			}

			SccpListener listener = this.sccpProviderImpl.ssnToListener.get(affectedSsn);
			if (listener != null) {
				this.sendSSA(message, affectedSsn);
				return;
			}

			if (logger.isInfoEnabled()) {
				logger.info(String.format("Received SST for unavailable SSN=%d", affectedSsn));
			}

			break;
		case SOR:
			if (logger.isEnabledFor(Level.WARN)) {
				logger.warn("Received SOR. SOR not yet implemented, dropping message");
			}
			break;
		case SOG:
			if (logger.isEnabledFor(Level.WARN)) {
				logger.warn("Received SOG. SOG not yet implemented, dropping message");
			}
			break;
		case SSC:
			if (logger.isEnabledFor(Level.WARN)) {
				logger.warn("Received SSC. SSC not yet implemented, dropping message");
			}
			break;
		default:
			logger.error("Received SCMG with unknown MessageType.");
			break;
		}

	}

	private void sendSSA(SccpMessage msg, int affectedSsn) {
		SccpAddress calledAdd = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN,
				((SccpMessageImpl) msg).getOpc(), null, 1);// SSN=1 for SCMG
		SccpAddress callingAdd = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, sccpStackImpl.localSpc,
				null, 1);// SSN=1 for SCMG

		ProtocolClass pClass = sccpProviderImpl.getParameterFactory().createProtocolClass(0, 0);
		UnitData udt = sccpProviderImpl.getMessageFactory().createUnitData(pClass, calledAdd, callingAdd);
		byte[] data = new byte[5];
		data[0] = SSA;
		data[1] = (byte) affectedSsn; // affected SSN
		data[2] = (byte) (sccpStackImpl.localSpc & 0x000000ff);
		data[3] = (byte) ((sccpStackImpl.localSpc & 0x0000ff00) >> 8);
		data[4] = 0;
		udt.setData(data);

		// set the SLS
		((SccpMessageImpl) udt).setSls(((SccpMessageImpl) msg).getSls());

		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Tx :SSA SCCP Message=%s Affected SSN=%d", msg.toString(), affectedSsn));
		}

		try {
			this.sccpRoutingControl.send(udt);
		} catch (IOException e) {
			logger.error(String.format("Exception while trying to send SSP message=%s", udt), e);
		}
	}

	protected void recdMsgForProhibitedSsn(SccpMessage msg, int ssn) {
		// Send SSP
		int opc = ((SccpMessageImpl) msg).getOpc();
		if (opc != -1) {
			// received message from MTP for SSN which is prohibited

			SccpAddress calledAdd = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN,
					((SccpMessageImpl) msg).getOpc(), null, 1);// SSN=1 for SCMG
			SccpAddress callingAdd = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN,
					sccpStackImpl.localSpc, null, 1);// SSN=1 for SCMG

			ProtocolClass pClass = sccpProviderImpl.getParameterFactory().createProtocolClass(0, 0);
			UnitData udt = sccpProviderImpl.getMessageFactory().createUnitData(pClass, calledAdd, callingAdd);
			byte[] data = new byte[5];
			data[0] = SSP;
			data[1] = (byte) ssn; // affected SSN
			data[2] = (byte) (sccpStackImpl.localSpc & 0x000000ff);
			data[3] = (byte) ((sccpStackImpl.localSpc & 0x0000ff00) >> 8);
			data[4] = 0;
			udt.setData(data);

			// set the SLS
			((SccpMessageImpl) udt).setSls(((SccpMessageImpl) msg).getSls());

			if (logger.isDebugEnabled()) {
				logger.debug(String.format("Tx :SSP SCCP Message=%s Affected SSN=%d", msg.toString(), ssn));
			}

			try {
				this.sccpRoutingControl.send(udt);
			} catch (IOException e) {
				logger.error(String.format("Exception while trying to send SSP message=%s", udt), e);
			}
		}

		// else we drop it; SCCP Management for local SSN is not taken care
	}

	private void handleMtp3Pause(int affectedPc) {
		// Look at Q.714 Section 5.2.2
		this.cancelAllSst(affectedPc, true);
		this.prohibitRsp(affectedPc);

	}

	private void handleMtp3Resume(int affectedPc) {
		// Look at Q.714 Section 5.2.2
		this.allowRsp(affectedPc);

	}

	private void handleMtp3Status(int status, int affectedPc, int congStatus, int unavailabiltyCause) {
		if (status == 2) {
			// Signaling Network Congestion

		} else if (status == 1) {
			// Remote User Unavailable
			switch (unavailabiltyCause) {
			case UNAVAILABILITY_CAUSE_UNKNOWN:
			case UNAVAILABILITY_CAUSE_INACCESSIBLE:

				this.prohibitAllSsn(affectedPc);

				SubSystemTest sstForSsn1 = this.cancelAllSst(affectedPc, false);
				if (sstForSsn1 != null) {
					sstForSsn1.setRecdMtpStatusResp(true);
				} else {
					// ITU-T Q.714 5.3.4.2 Actions at the initiating node

					// A subsystem status test for SSN = 1 is initiated when an
					// MTP-STATUS indication primitive is received with
					// "remote user inaccessibility" or "unknown" information
					// for the SCCP at a remote signalling point

					// Start sending the SST for SSN1
					FastList<SubSystemTest> ssts = dpcVsSst.get(affectedPc);
					if (ssts == null) {
						ssts = new FastList<SubSystemTest>();
						dpcVsSst.put(affectedPc, ssts);
					}
					SubSystemTest sst = new SubSystemTest(1, affectedPc, ssts);

					// ssts.add(sst);
					sst.startTest();
				}

				break;

			case UNAVAILABILITY_CAUSE_UNEQUIPED:
				// See ITU-T Q.714 5.2.2 Signalling point prohibited

				// In the case where the SCCP has received an MTP-STATUS
				// indication primitive relating to an unavailable SCCP, the
				// SCCP marks the status of the SCCP and each SSN for the
				// relevant destination to "prohibited" and initiates a
				// subsystem status test with SSN = 1. If the cause in the
				// MTP-STATUS indication primitive indicates "unequipped user",
				// then no subsystem status test is initiated.
				this.prohibitAllSsn(affectedPc);

				// Discontinues all subsystem status tests (including SSN = 1)
				// if an MTP-PAUSE or MTP-STATUS indication primitive is
				// received with a cause of "unequipped SCCP"
				this.cancelAllSst(affectedPc, true);
				break;
			default:
				logger.error(String.format("Error in handling MTP3 STATUS. Received unknown unavailability cisue=%d",
						unavailabiltyCause));
				break;
			}

		} else {
			logger.error(String.format("Error in handling MTP3 STATUS. Received unknown status=%d", status));
		}
	}

	private void prohibitAllSsn(int affectedPc) {
		FastMap<Integer, RemoteSubSystem> remoteSsns = this.sccpStackImpl.getSccpResource().getRemoteSsns();
		for (FastMap.Entry<Integer, RemoteSubSystem> e = remoteSsns.head(), end = remoteSsns.tail(); (e = e.getNext()) != end;) {
			RemoteSubSystem remoteSsn = e.getValue();
			if (remoteSsn.getRemoteSpc() == affectedPc) {
				remoteSsn.setRemoteSsnProhibited(true);
			}
		}
	}

	private void allowAllSsn(int affectedPc) {
		FastMap<Integer, RemoteSubSystem> remoteSsns = this.sccpStackImpl.getSccpResource().getRemoteSsns();
		for (FastMap.Entry<Integer, RemoteSubSystem> e = remoteSsns.head(), end = remoteSsns.tail(); (e = e.getNext()) != end;) {
			RemoteSubSystem remoteSsn = e.getValue();
			if (remoteSsn.getRemoteSpc() == affectedPc) {
				remoteSsn.setRemoteSsnProhibited(false);
			}
		}
	}

	private void prohibitRsp(int affectedPc) {
		RemoteSignalingPointCode remoteSpc = this.sccpStackImpl.getSccpResource().getRemoteSpcByPC(affectedPc);
		if (remoteSpc != null) {
			remoteSpc.setRemoteSpcProhibited(true);
		}

		this.prohibitAllSsn(affectedPc);
	}

	private void allowRsp(int affectedPc) {
		RemoteSignalingPointCode remoteSpc = this.sccpStackImpl.getSccpResource().getRemoteSpcByPC(affectedPc);
		if (remoteSpc != null) {
			remoteSpc.setRemoteSpcProhibited(false);
		}

		this.allowAllSsn(affectedPc);
	}

	private void prohibitSsn(int affectedPc, int ssn) {
		FastMap<Integer, RemoteSubSystem> remoteSsns = this.sccpStackImpl.getSccpResource().getRemoteSsns();
		for (FastMap.Entry<Integer, RemoteSubSystem> e = remoteSsns.head(), end = remoteSsns.tail(); (e = e.getNext()) != end;) {
			RemoteSubSystem remoteSsn = e.getValue();
			if (remoteSsn.getRemoteSpc() == affectedPc && remoteSsn.getRemoteSsn() == ssn) {
				remoteSsn.setRemoteSsnProhibited(true);
				break;
			}
		}
	}

	private void allowSsn(int affectedPc, int ssn) {
		FastMap<Integer, RemoteSubSystem> remoteSsns = this.sccpStackImpl.getSccpResource().getRemoteSsns();
		for (FastMap.Entry<Integer, RemoteSubSystem> e = remoteSsns.head(), end = remoteSsns.tail(); (e = e.getNext()) != end;) {
			RemoteSubSystem remoteSsn = e.getValue();
			if (remoteSsn.getRemoteSpc() == affectedPc && (ssn == 1 || remoteSsn.getRemoteSsn() == ssn)) {
				remoteSsn.setRemoteSsnProhibited(false);
				break;
			}
		}
	}

	private SubSystemTest cancelAllSst(int affectedPc, boolean cancelSstForSsn1) {
		SubSystemTest sstForSsn1 = null;
		// cancel all SST if any
		FastList<SubSystemTest> ssts = dpcVsSst.get(affectedPc);
		if (ssts != null) {
			// TODO : Amit: Added n.getValue() != null check. Evaluate
			// javolution.FastList as why for loop continues even after removing
			// last element?
			for (FastList.Node<SubSystemTest> n = ssts.head(), endSst = ssts.tail(); ((n = n.getNext()) != endSst)
					&& n.getValue() != null;) {
				SubSystemTest sst = n.getValue();

				// If SSN = 1 but flag ssn1 is false, means we don't stop this
				// SST and return back the reference to it
				if (sst.getSsn() == 1 && !cancelSstForSsn1) {
					sstForSsn1 = sst;
					continue;
				}
				sst.stopTest();
				// ssts.remove(sst);
			}
		}

		return sstForSsn1;
	}

	private class SubSystemTest implements Runnable {
		// FIXME: remove "Thread", so we eat less resources.

		private volatile boolean started = false;

		// Flag to check if received an MTP-STATUS indication primitive stating
		// User Part Unavailable.
		private volatile boolean recdMtpStatusResp = true;

		private Future testFuture;
		private FastList<SubSystemTest> testsList; // just a ref to list of
													// testse for DPC, instances
													// of this classes should be
													// there.

		private int ssn = 0;
		private int affectedPc = 0;

		private SccpAddress callingAdd = null;
		private SccpAddress calledAdd = null;

		private UnitData udt = null;

		SubSystemTest(int ssn, int affectedPc, FastList<SubSystemTest> testsList) {
			this.ssn = ssn;
			this.affectedPc = affectedPc;
			this.testsList = testsList;

			// SSN=1 for SCMG
			calledAdd = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, affectedPc, null, 1);
			callingAdd = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, sccpStackImpl.localSpc, null, 1);

			ProtocolClass pClass = sccpProviderImpl.getParameterFactory().createProtocolClass(0, 0);
			udt = sccpProviderImpl.getMessageFactory().createUnitData(pClass, calledAdd, callingAdd);
			byte[] data = new byte[5];
			data[0] = 3; // SST
			data[1] = (byte) ssn; // affected SSN
			data[2] = (byte) (this.affectedPc & 0x000000ff);
			data[3] = (byte) ((this.affectedPc & 0x0000ff00) >> 8);
			data[4] = 0;
			udt.setData(data);
		}

		public int getSsn() {
			return ssn;
		}

		public void setRecdMtpStatusResp(boolean recdMtpStatusResp) {
			this.recdMtpStatusResp = recdMtpStatusResp;
		}

		synchronized void stopTest() {
			started = false;
			Future f = this.testFuture;
			if (f != null) {
				this.testsList.remove(this);
				this.testFuture = null;
				f.cancel(false);
			}

			notify();
		}

		synchronized void startTest() {
			if (!started) {
				this.testFuture = managementExecutors.schedule(this, 10000, TimeUnit.MILLISECONDS);
				started = true;
				this.testsList.add(this);
			}

		}

		public synchronized void run() {

			if (started) {

				if (this.ssn == 1 && !this.recdMtpStatusResp) {
					// If no MTP STATUS received, means we consider previously
					// unavailable (SCCP) has recovered

					// TODO Take care of updating translation table;
					this.stopTest();
					return;

				}
				// Set it false again so we wait for response again after
				// sending SST for SSN = 1 bellow
				this.recdMtpStatusResp = false;

				if (logger.isDebugEnabled()) {
					logger.debug(String.format("Tx :SST SCCP Message=%s Affected SSN=%d", udt.toString(), ssn));
				}

				try {
					sccpRoutingControl.send(udt);
				} catch (IOException e1) {
					logger.error(String
							.format("Error while trying to send SST UnitData Called Address=%s Calling Address=%s Affected SSN=%d",
									calledAdd.toString(), callingAdd.toString(), this.ssn));
				}

				// TODO : How much to sleep?
				this.stopTest();
				this.startTest();

			}// while

		}// run

	}// SubSystemTest

	/**
	 * 
	 */
	public void start() {

		this.dpcVsSst.clear();
		managementExecutors = Executors.newScheduledThreadPool(1);

	}

	/**
	 * 
	 */
	public void stop() {
		// no need to stop, it will clean on start, and scheduler is dead.
		managementExecutors.shutdownNow();

	}

	private String getMessageType(int msgType) {
		switch (msgType) {
		case SSA:
			return S_SSA;
		case SSP:
			return S_SSP;
		case SST:
			return S_SST;
		case SOR:
			return S_SOR;
		case SOG:
			return S_SOG;
		case SSC:
			return S_SSC;
		default:
			return S_DEFAULT;
		}
	}
}
