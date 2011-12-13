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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.mtp.Mtp3;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.router.Rule;
import org.mobicents.protocols.ss7.sccp.message.SccpMessage;
import org.mobicents.protocols.ss7.sccp.message.UnitData;
import org.mobicents.protocols.ss7.sccp.message.UnitDataService;
import org.mobicents.protocols.ss7.sccp.message.XUnitData;
import org.mobicents.protocols.ss7.sccp.message.XUnitDataService;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.HopCounter;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;
import org.mobicents.protocols.ss7.sccp.parameter.ReturnCause;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * 
 * @author amit bhayani
 * 
 */
public class SccpRoutingControl {
	private static final Logger logger = Logger.getLogger(SccpRoutingControl.class);

	private SccpStackImpl sccpStackImpl = null;
	private SccpProviderImpl sccpProviderImpl = null;

	private SccpManagement sccpManagement = null;

	private MessageFactoryImpl messageFactory;

	public SccpRoutingControl(SccpProviderImpl sccpProviderImpl, SccpStackImpl sccpStackImpl) {
		this.messageFactory = new MessageFactoryImpl();
		this.sccpProviderImpl = sccpProviderImpl;
		this.sccpStackImpl = sccpStackImpl;
	}

	public SccpManagement getSccpManagement() {
		return sccpManagement;
	}

	public void setSccpManagement(SccpManagement sccpManagement) {
		this.sccpManagement = sccpManagement;
	}

	public void start() {
		// NOP for now

	}

	public void stop() {
		// NOP for now

	}

	protected void routeMssgFromMtp(SccpMessageImpl msg) throws IOException {
		// TODO if the local SCCP or node is in an overload condition, SCRC
		// shall inform SCMG
		//boolean returnError = msg.getProtocolClass().getHandling() == ProtocolClass.HANDLING_RET_ERR;
		
		boolean returnError = false;
		
		ProtocolClass protocolClass = ((SccpMessageImpl) msg).getProtocolClass();
		
		if(protocolClass != null){
			returnError = protocolClass.getHandling() == ProtocolClass.HANDLING_RET_ERR;
		}
		
		SccpAddress calledPartyAddress = msg.getCalledPartyAddress();
		RoutingIndicator ri = calledPartyAddress.getAddressIndicator().getRoutingIndicator();
		switch (ri) {
		case ROUTING_BASED_ON_DPC_AND_SSN:
			int ssn = msg.getCalledPartyAddress().getSubsystemNumber();
			if (ssn == 1) {
				// This is for management
				this.sccpManagement.onMessage(msg, msg.getSls());
				return;
			}
			//SccpListener listener = this.sccpProviderImpl.ssnToListener.get(ssn);
			SccpListener listener = this.sccpProviderImpl.getSccpListener(ssn);
			if (listener == null) {

				// Notify Management
				this.sccpManagement.recdMsgForProhibitedSsn(msg, ssn);

				if (logger.isEnabledFor(Level.WARN)) {
					logger.warn(String.format("Received SccpMessage=%s from MTP but the SSN is not available for local routing", msg));
				}
				if (returnError) {
					this.sendSccpError(msg, ReturnCause.SUBSYSTEM_FAILURE, true);
				}
				return;
			}

			// Notify Listener
			try {
				// JIC: user may behave bad and throw something here.
				listener.onMessage(msg, msg.getSls());
			} catch (Exception e) {
				if (logger.isEnabledFor(Level.WARN)) {
					logger.warn(String.format("Received SccpMessage=%s from MTP and user threw exception from listener!", msg));
				}
			}
			break;
		case ROUTING_BASED_ON_GLOBAL_TITLE:
			this.translationFunction(msg, returnError, true);
			break;
		default:
			// This can never happen
			logger.error(String.format("Invalid Routing Indictaor received for message=%s from MTP3", msg));
			break;
		}
	}

	protected void routeMssgFromSccpUser(SccpMessage msg) throws IOException {
		this.route(msg, false);
	}

	protected void send(SccpMessage message) throws IOException {

		int dpc = message.getCalledPartyAddress().getSignalingPointCode();
		int sls = ((SccpMessageImpl) message).getSls();
//		int ssi = this.sccpStackImpl.ni << 2;

		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		((SccpMessageImpl) message).encode(bout);
		
		Mtp3TransferPrimitive msg = new Mtp3TransferPrimitive(Mtp3._SI_SERVICE_SCCP, this.sccpStackImpl.ni, 0, this.sccpStackImpl.localSpc, dpc, sls,
				bout.toByteArray());
		this.sccpStackImpl.mtp3UserPart.sendMessage(msg);
		
//		ByteArrayOutputStream bout = new ByteArrayOutputStream();
//		// encoding routing label
//		bout.write((byte) (((ssi & 0x0F) << 4) | (SccpStackImpl.SI_SCCP & 0x0F)));
//		bout.write((byte) dpc);
//		bout.write((byte) (((dpc >> 8) & 0x3F) | ((this.sccpStackImpl.localSpc & 0x03) << 6)));
//		bout.write((byte) (this.sccpStackImpl.localSpc >> 2));
//		bout.write((byte) (((this.sccpStackImpl.localSpc >> 10) & 0x0F) | ((sls & 0x0F) << 4)));
//
//		((SccpMessageImpl) message).encode(bout);
//
//		byte[] msg = bout.toByteArray();
//		this.sccpStackImpl.txDataQueue.add(msg);
	}

	private void translationFunction(SccpMessage msg, final boolean returnError, final boolean fromMtp) throws IOException {

		SccpAddress calledPartyAddress = msg.getCalledPartyAddress();

		// boolean returnError = ((SccpMessageImpl)
		// msg).getProtocolClass().getHandling() ==
		// ProtocolClass.HANDLING_RET_ERR;

		Rule rule = this.sccpStackImpl.router.find(calledPartyAddress);

		if (rule == null) {
			if (logger.isEnabledFor(Level.WARN)) {
				logger.warn(String.format("Received SccpMessage=%s for Translation but no matching Rule found for local routing", msg));
			}

			// Translation failed return error
			if (returnError) {
				this.sendSccpError(msg, ReturnCause.NO_TRANSLATION_FOR_NATURE, fromMtp);
			}
			return;
		}

		// Check whether to use primary or backup address
		SccpAddress translationAddress = this.sccpStackImpl.router.getPrimaryAddresses().get(rule.getPrimaryAddressId());

		if (translationAddress == null) {
			if (logger.isEnabledFor(Level.WARN)) {
				logger.warn(String.format("Received SccpMessage=%s for Translation but no matching Primary Address defined for Rule=%s for routing", msg, rule));
			}
			// Translation failed return error
			if (returnError) {
				this.sendSccpError(msg, ReturnCause.NO_TRANSLATION_FOR_NATURE, fromMtp);
			}
			return;
		}

		// If the translated pointcode is local point code no test of point
		// code availability is needed
		if (translationAddress.getSignalingPointCode() != this.sccpStackImpl.localSpc) {
			// Check if the Primary DPC is prohibited
			RemoteSignalingPointCode remoteSpc = this.sccpStackImpl.getSccpResource().getRemoteSpcByPC(translationAddress.getSignalingPointCode());

			if (remoteSpc == null) {
				if (logger.isEnabledFor(Level.WARN)) {
					logger.warn(String.format("Received SccpMessage=%s for Translation but no Remote Signaling Pointcode = %d resource defined ", msg,
							translationAddress.getSignalingPointCode()));
				}

				if (returnError) {
					this.sendSccpError(msg, ReturnCause.NO_TRANSLATION_FOR_NATURE, fromMtp);
				}
				return;
			}

			if (remoteSpc.isRemoteSpcProhibited()) {
				// try secondary address
				translationAddress = this.sccpStackImpl.router.getBackupAddresses().get(rule.getSecondaryAddressId());

				if (translationAddress == null) {
					// failed. Secondary Translation Address not available.
					// Error cause must be DPC not available
					if (logger.isEnabledFor(Level.WARN)) {
						logger.warn(String
								.format("Received SccpMessage=%s for Translation but Primary Signaling Pointcode = %d is prohibited and no Secondary Address is defined",
										msg, remoteSpc.getRemoteSpc()));
					}

					if (returnError) {
						this.sendSccpError(msg, ReturnCause.MTP_FAILURE, fromMtp);
					}
					return;
				}

				// Check if the Secondary DPC is prohibited
				remoteSpc = this.sccpStackImpl.getSccpResource().getRemoteSpcByPC(translationAddress.getSignalingPointCode());

				if (remoteSpc == null) {
					if (logger.isEnabledFor(Level.WARN)) {
						logger.warn(String
								.format("Received SccpMessage=%s for Translation but Primary Signaling Pointcode is prohibited and no Point Code=%d resource is defined for Secondary Address",
										msg, translationAddress.getSignalingPointCode()));
					}

					if (returnError) {
						this.sendSccpError(msg, ReturnCause.MTP_FAILURE, fromMtp);
					}
					return;
				}

				if (remoteSpc.isRemoteSpcProhibited()) {
					// Error. Cause should be DPC not available
					if (logger.isEnabledFor(Level.WARN)) {
						logger.warn(String
								.format("Received SccpMessage=%s for Translation but Primary Signaling Pointcode is prohibited and Secondary Signaling Pointcode=%d is also prohibited",
										msg, remoteSpc.getRemoteSpc()));
					}

					if (returnError) {
						this.sendSccpError(msg, ReturnCause.MTP_FAILURE, fromMtp);
					}
					return;
				}
			}
		}

		// translate address
		SccpAddress address = rule.translate(calledPartyAddress, translationAddress);
		msg.setCalledPartyAddress(address);

		if (logger.isDebugEnabled()) {
			logger.debug(String.format("CalledPartyAddress after translation = %s", address));
		}

		// routing procedures then continue's
		this.route(msg, fromMtp);
	}

	private void route(SccpMessage msg, boolean fromMtp) throws IOException {

		boolean returnError = false;
		
		ProtocolClass protocolClass = ((SccpMessageImpl) msg).getProtocolClass();
		
		if(protocolClass != null){
			returnError = protocolClass.getHandling() == ProtocolClass.HANDLING_RET_ERR;
		}
		
		SccpAddress calledPartyAddress = msg.getCalledPartyAddress();

		int dpc = calledPartyAddress.getSignalingPointCode();
		int ssn = calledPartyAddress.getSubsystemNumber();
		GlobalTitle gt = calledPartyAddress.getGlobalTitle();

		if (calledPartyAddress.getAddressIndicator().pcPresent()) {
			// DPC present

			if (dpc == this.sccpStackImpl.localSpc) {
				// This message is for local routing

				if (ssn > 0) {
					// if a non-zero SSN is present but not the GT (case 2 a) of
					// 2.2.2), then the message is passed based on the message
					// type to either connection-oriented control or
					// connectionless control and based on the availability of
					// the subsystem;
					//SccpListener listener = this.sccpProviderImpl.ssnToListener.get(ssn);
					SccpListener listener = this.sccpProviderImpl.getSccpListener(ssn);
					if (listener == null) {
						// TODO: Notification to management for local SSN is not
						// taken care yet

						if (logger.isEnabledFor(Level.WARN)) {
							logger.warn(String.format("Received SccpMessage=%s for routing but the SSN is not available for local routing", msg));
						}
						if (returnError) {
							this.sendSccpError(msg, ReturnCause.SUBSYSTEM_FAILURE, fromMtp);
						}
						return;
					}
					// Notify Listener
					try {
						// JIC: user may behave bad and throw something here.
						listener.onMessage(msg, ((SccpMessageImpl) msg).getSls());
					} catch (Exception e) {
						if (logger.isEnabledFor(Level.WARN)) {
							logger.warn(String.format("Received SccpMessage=%s from MTP and user threw exception from listener!", msg));
						}
					}
				} else if (gt != null) {
					// if the GT is present but no SSN or a zero SSN is present
					// (case 2 b) of 2.2.2), then the message is passed to the
					// translation function;

					if (calledPartyAddress.isTranslated()) {
						// Called address already translated once. This is loop
						// condition and error
						logger.error(String.format("Droping message. Received SCCPMessage=%s for routing but CalledPartyAddress is already translated once",
								msg));
						return;
					}

					this.translationFunction(msg, returnError, fromMtp);

				} else {
					// if an SSN equal to zero is present but not a GT (case 2
					// d) of 2.2.2), then the address information is incomplete
					// and the message shall be discarded. This abnormality is
					// similar to the one described in 3.8.3.3, item 1) b6.

					logger.error(String.format("Received SCCPMessage=%s for routing, but neither SSN nor GT present", msg));
				}

			} else {
				// DPC present but its not local pointcode. This message should
				// be Tx to MTP
				if (ssn > 0) {
					// if a non-zero SSN is present but not the GT (case 2 a) of
					// 2.2.2), then the called party address provided shall
					// contain this SSN and the routing indicator shall be set
					// to "Route on SSN"; See 2.2.2.1 point 2 of ITU-T Q.714
					// If routing based on SSN, check remote SSN is available
					RemoteSubSystem remoteSsn = this.sccpStackImpl.getSccpResource().getRemoteSsn(dpc, calledPartyAddress.getSubsystemNumber());
					if (remoteSsn == null) {
						if (logger.isEnabledFor(Level.WARN)) {
							logger.warn(String.format("Received SCCPMessage=%s for routing, but no Remote SubSystem = %d resource defined ", msg,
									calledPartyAddress.getSubsystemNumber()));
						}

						// Routing failed return error
						if (returnError) {
							this.sendSccpError(msg, ReturnCause.SUBSYSTEM_FAILURE, fromMtp);
						}
						return;
					}

					if (remoteSsn.isRemoteSsnProhibited()) {
						if (logger.isEnabledFor(Level.WARN)) {
							logger.warn(String.format("Routing of Sccp Message=%s failed as Remote SubSystem = %d is prohibited ", msg,
									calledPartyAddress.getSubsystemNumber()));
						}
						if (returnError) {
							this.sendSccpError(msg, ReturnCause.SUBSYSTEM_FAILURE, fromMtp);
						}
						return;
					}

					if (logger.isDebugEnabled()) {
						logger.debug(String.format("Tx : SCCP Message=%s", msg.toString()));
					}

					this.send(msg);
				} else if (gt != null) {
					// if the GT is present but no SSN or a zero SSN is present
					// (case 2 b) of 2.2.2), then the DPC identifies where the
					// global title translation occurs. The called party address
					// provided shall contain this GT and the routing indicator
					// shall be set to "Route on GT"; See 2.2.2.1 point 3 of
					// ITU-T Q.714

					// Check if the Primary DPC is prohibited
					RemoteSignalingPointCode remoteSpc = this.sccpStackImpl.getSccpResource().getRemoteSpcByPC(dpc);

					if (remoteSpc == null) {
						if (logger.isEnabledFor(Level.WARN)) {
							logger.warn(String.format("Received SccpMessage=%s for routing but no Remote Signaling Pointcode = %d resource defined ", msg, dpc));
						}

						if (returnError) {
							this.sendSccpError(msg, ReturnCause.NO_TRANSLATION_FOR_NATURE, fromMtp);
						}
						return;
					}

					if (remoteSpc.isRemoteSpcProhibited()) {
						if (logger.isEnabledFor(Level.WARN)) {
							logger.warn(String.format("Received SccpMessage=%s for routing but Remote Signaling Pointcode = %d is prohibited", msg, dpc));
						}

						if (returnError) {
							this.sendSccpError(msg, ReturnCause.NO_TRANSLATION_FOR_NATURE, fromMtp);
						}
						return;
					}

					if (logger.isDebugEnabled()) {
						logger.debug(String.format("Tx : SCCP Message=%s", msg.toString()));
					}

					// send to MTP
					this.send(msg);
				} else {
					logger.error(String.format("Received SCCPMessage=%s for routing, but neither SSN nor GT present", msg));
				}

			}

		} else {
			// DPC not present

			// If the DPC is not present, (case 3 of 2.2.2), then a global title
			// translation is required before the message can be sent out.
			// Translation results in a DPC and possibly a new SSN or new GT or
			// both.

			if (gt == null) {
				// No DPC, and no GT. This is insufficient information

				if (logger.isEnabledFor(Level.WARN)) {
					logger.warn(String.format("Received SccpMessage=%s for routing from local SCCP user part but no pointcode and no GT or SSN included", msg,
							dpc));
				}

				if (returnError) {
					this.sendSccpError(msg, ReturnCause.NO_TRANSLATION_FOR_NATURE, fromMtp);
				}
				return;
			}

			if (calledPartyAddress.isTranslated()) {
				// Called address already translated once. This is loop
				// condition and error
				logger.error(String.format("Droping message. Received SCCPMessage=%s for Routing , but CalledPartyAddress is already translated once", msg));
				return;
			}

			this.translationFunction(msg, returnError, fromMtp);

		}

	}

	private void sendSccpError(SccpMessage msg, int returnCauseInt, boolean fromMtp) throws IOException {
		// in case we did not consume and this message has arrived from
		// other end.... we have to reply in some way Q.714 4.2 for now
		SccpMessage ans = null;
		// not sure if its proper
		ReturnCause returnCause = this.sccpProviderImpl.getParameterFactory().createReturnCause(returnCauseInt);
		switch (msg.getType()) {
		case UnitData.MESSAGE_TYPE:
			ans = messageFactory.createUnitDataService(returnCause, msg.getCallingPartyAddress(), msg.getCalledPartyAddress()); // switch
																																// addresses
			((UnitDataService) ans).setData(((UnitData) msg).getData());
			break;
		case XUnitData.MESSAGE_TYPE:
			HopCounter hc = this.sccpProviderImpl.getParameterFactory().createHopCounter(HopCounter.COUNT_HIGH);
			ans = messageFactory.createXUnitDataService(hc, returnCause, msg.getCallingPartyAddress(), msg.getCalledPartyAddress()); // switch
																																		// addresses
			((XUnitDataService) ans).setData(((XUnitData) msg).getData());
			break;
		default:
			if (logger.isEnabledFor(Level.WARN)) {
				logger.warn(String.format("Not supported error condition! Message=%s ", msg));
			}
		}

		// TODO : SeqControl should be set from original message?
		if (ans != null) {
			if (fromMtp) {
				if (logger.isDebugEnabled()) {
					logger.debug(String.format("Tx : SCCP Message=%s", msg.toString()));
				}
				this.send(ans);
			} else {
				this.route(ans, fromMtp);
			}
		}
	}

}
