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

package org.mobicents.protocols.ss7.tcap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.message.MessageFactory;
import org.mobicents.protocols.ss7.sccp.message.SccpMessage;
import org.mobicents.protocols.ss7.sccp.message.UnitData;
import org.mobicents.protocols.ss7.sccp.message.UnitDataService;
import org.mobicents.protocols.ss7.sccp.message.XUnitData;
import org.mobicents.protocols.ss7.sccp.message.XUnitDataService;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory;
import org.mobicents.protocols.ss7.tcap.api.DialogPrimitiveFactory;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcap.api.TCListener;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.InvokeImpl;
import org.mobicents.protocols.ss7.tcap.asn.TCAbortMessageImpl;
import org.mobicents.protocols.ss7.tcap.asn.TCUnidentifiedMessage;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCAbortMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCBeginMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCContinueMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCEndMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCUniMessage;
import org.mobicents.protocols.ss7.tcap.tc.component.ComponentPrimitiveFactoryImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.DialogPrimitiveFactoryImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCBeginIndicationImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCContinueIndicationImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCEndIndicationImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCPAbortIndicationImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCUniIndicationImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCUserAbortIndicationImpl;

/**
 * @author amit bhayani
 * @author baranowb
 * @author sergey vetyutnev
 * 
 */
public class TCAPProviderImpl implements TCAPProvider, SccpListener {

	private static final Logger logger = Logger.getLogger(TCAPProviderImpl.class); // listenres
	private List<TCListener> tcListeners = new ArrayList<TCListener>();
	protected ScheduledExecutorService _EXECUTOR;
	// boundry for Uni directional dialogs :), tx id is always encoded
	// on 4 octets, so this is its max value
	//private static final long _4_OCTETS_LONG_FILL = 4294967295l;
	private DialogIdIndex dialogIdIndex;
	private ComponentPrimitiveFactory componentPrimitiveFactory;
	private DialogPrimitiveFactory dialogPrimitiveFactory;
	private SccpProvider sccpProvider;

	private MessageFactory messageFactory;
	private ParameterFactory parameterFactory;

	private static int seqControl = 0;
	private TCAPStackImpl stack; // originating TX id ~=Dialog, its direct
									// mapping, but not described
	// explicitly...
	private Map<Long, DialogImpl> dialogs = new HashMap<Long, DialogImpl>();
	private int ssn;

	protected TCAPProviderImpl(SccpProvider sccpProvider, TCAPStackImpl stack, int ssn, int maxDialogs) {
		super();
		this.sccpProvider = sccpProvider;
		this.ssn = ssn;
		messageFactory = sccpProvider.getMessageFactory();
		parameterFactory = sccpProvider.getParameterFactory();
		this.stack = stack;

		this.componentPrimitiveFactory = new ComponentPrimitiveFactoryImpl(this);
		this.dialogPrimitiveFactory = new DialogPrimitiveFactoryImpl(this.componentPrimitiveFactory);
		this.dialogIdIndex = new DialogIdIndex(maxDialogs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.TCAPStack#addTCListener(org.mobicents
	 * .protocols.ss7.tcap.api.TCListener)
	 */

	public void addTCListener(TCListener lst) {
		if (this.tcListeners.contains(lst)) {
		} else {
			this.tcListeners.add(lst);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.TCAPStack#removeTCListener(org.mobicents
	 * .protocols.ss7.tcap.api.TCListener)
	 */
	public void removeTCListener(TCListener lst) {
		this.tcListeners.remove(lst);

	}

	// some help methods... crude but will work for first impl.
	private Long getAvailableTxId() throws TCAPException {
		Long poped = this.dialogIdIndex.pop();
		if(poped == null)
		    throw new TCAPException("Not enough resources!");
		return poped;
	}

	// get next Seq Control value available
	private int getNextSeqControl() {
		seqControl++;
		if (seqControl > 31) {
			seqControl = 0;

		}
		return seqControl;
	}

//	private Long getAvailableUniTxId() throws TCAPException {
//		for (long l = -1; l >= Long.MIN_VALUE; l--) {
//			Long ll = new Long(l);
//			if (this.dialogs.containsKey(ll)) {
//			} else {
//				return ll;
//			}
//		}
//		throw new TCAPException("Not enough resources!");
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.api.TCAPProvider#
	 * getComopnentPrimitiveFactory()
	 */
	public ComponentPrimitiveFactory getComponentPrimitiveFactory() {

		return this.componentPrimitiveFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.TCAPProvider#getDialogPrimitiveFactory
	 * ()
	 */
	public DialogPrimitiveFactory getDialogPrimitiveFactory() {

		return this.dialogPrimitiveFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.TCAPProvider#getNewDialog(org.mobicents
	 * .protocols.ss7.sccp.parameter.SccpAddress,
	 * org.mobicents.protocols.ss7.sccp.parameter.SccpAddress)
	 */
	public Dialog getNewDialog(SccpAddress localAddress, SccpAddress remoteAddress) throws TCAPException {
		return getNewDialog(localAddress, remoteAddress, getNextSeqControl());
	}

	private Dialog getNewDialog(SccpAddress localAddress, SccpAddress remoteAddress, int seqControl)
			throws TCAPException {
		Long id = this.getAvailableTxId();
		Dialog dialog = _getDialog(localAddress, remoteAddress, id, true, seqControl);
		return dialog;
	}

	private Dialog _getDialog(SccpAddress localAddress, SccpAddress remoteAddress, Long id, boolean structured,
			int seqControl) {
		if (localAddress == null) {
			throw new NullPointerException("LocalAddress must not be null");
		}

		if (remoteAddress == null) {
			throw new NullPointerException("RemoteAddress must not be null");
		}
		DialogImpl di = new DialogImpl(localAddress, remoteAddress, id, structured, this._EXECUTOR, this, seqControl);

		this.dialogs.put(di.getDialogId(), di);
		return di;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.TCAPProvider#getNewUnstructuredDialog
	 * (org.mobicents.protocols.ss7.sccp.parameter.SccpAddress,
	 * org.mobicents.protocols.ss7.sccp.parameter.SccpAddress)
	 */
	public Dialog getNewUnstructuredDialog(SccpAddress localAddress, SccpAddress remoteAddress) throws TCAPException {
		Long id = this.getAvailableTxId();
		return _getDialog(localAddress, remoteAddress, id, false, getNextSeqControl());
	}

	public void onMessage(SccpMessage message, int seqControl) {
		try {
			byte[] data = null;
			SccpAddress localAddress = null;
			SccpAddress remoteAddress = null;

			switch (message.getType()) {

			case UnitData.MESSAGE_TYPE:
				data = ((UnitData) message).getData();
				localAddress = ((UnitData) message).getCalledPartyAddress();
				remoteAddress = ((UnitData) message).getCallingPartyAddress();
				break;
			case XUnitData.MESSAGE_TYPE:
				data = ((XUnitData) message).getData();
				localAddress = ((XUnitData) message).getCalledPartyAddress();
				remoteAddress = ((XUnitData) message).getCallingPartyAddress();
				break;
			// TODO: determine action based on cause?
			case UnitDataService.MESSAGE_TYPE:
				data = ((XUnitData) message).getData();
				localAddress = ((XUnitData) message).getCalledPartyAddress();
				remoteAddress = ((XUnitData) message).getCallingPartyAddress();
				break;
			case XUnitDataService.MESSAGE_TYPE:
				data = ((XUnitData) message).getData();
				localAddress = ((XUnitData) message).getCalledPartyAddress();
				remoteAddress = ((XUnitData) message).getCallingPartyAddress();
				break;
			}
			// FIXME: Qs state that OtxID and DtxID consittute to dialog id.....

			// asnData - it should pass
			AsnInputStream ais = new AsnInputStream(data);

			// this should have TC message tag :)
			int tag = ais.readTag();

			switch (tag) {
			// continue first, usually we will get more of those. small perf
			// boost
			case TCContinueMessage._TAG:
				TCContinueMessage tcm = TcapFactory.createTCContinueMessage(ais);
				// received continue, destID == localDialogId(originatingTxId of
				// begin);
				Long dialogId = tcm.getDestinationTransactionId();
				DialogImpl di = this.dialogs.get(dialogId);
				if (di == null) {
					logger.error("No dialog/transaction for id: " + dialogId);
				} else {
					di.processContinue(tcm, localAddress, remoteAddress);
				}
				break;

			case TCBeginMessage._TAG:
				TCBeginMessage tcb = TcapFactory.createTCBeginMessage(ais);

				// received continue, destID == localDialogId(originatingTxId of
				// begin);

				di = (DialogImpl) this.getNewDialog(localAddress, remoteAddress, seqControl);
				di.processBegin(tcb, localAddress, remoteAddress);

				break;

			case TCEndMessage._TAG:
				TCEndMessage teb = TcapFactory.createTCEndMessage(ais);
				dialogId = teb.getDestinationTransactionId();
				di = this.dialogs.get(dialogId);
				if (di == null) {
					logger.error("No dialog/transaction for id: " + dialogId);
				} else {
					di.processEnd(teb, localAddress, remoteAddress);

				}
				break;
			case TCAbortMessage._TAG:
				// this can be only TC-U-Abort, since TC-P-Abort is only local?
				TCAbortMessage tub = TcapFactory.createTCAbortMessage(ais);
				// di = (DialogImpl) this.getNewUnstructuredDialog(localAddress,
				// remoteAddress);
				// di.setActionReference(ar);
				// di.processAbort(tub, localAddress, remoteAddress);
				dialogId = tub.getDestinationTransactionId();
				di = this.dialogs.get(dialogId);
				if (di == null) {
					logger.error("No dialog/transaction for id: " + dialogId);
				} else {
					di.processAbort(tub, localAddress, remoteAddress);
				}
				break;
			case TCUniMessage._TAG:
				TCUniMessage tcuni = TcapFactory.createTCUniMessage(ais);
				DialogImpl uniDialog = (DialogImpl) this.getNewUnstructuredDialog(localAddress, remoteAddress);
				uniDialog.processUni(tcuni, localAddress, remoteAddress);
				break;
			default:
				logger.error(String.format("Rx unidentified messageType=%s. SccpMessage=%s", tag, message));
				TCUnidentifiedMessage tcUnidentified = new TCUnidentifiedMessage();
				tcUnidentified.decode(ais);

				if (tcUnidentified.getOriginatingTransactionId() != null) {
					long otid = tcUnidentified.getOriginatingTransactionId();
					long dtid = -1;
					if (tcUnidentified.getDestinationTransactionId() != null) {
						dtid = tcUnidentified.getDestinationTransactionId();
					}

					TCAbortMessageImpl msg = (TCAbortMessageImpl) TcapFactory.createTCAbortMessage();
					msg.setDestinationTransactionId(otid);
					msg.setPAbortCause(PAbortCauseType.UnrecogniedMessageType);

					AsnOutputStream aos = new AsnOutputStream();
					msg.encode(aos);
					try {
						this.send(aos.toByteArray(), (byte) 0, remoteAddress, localAddress, seqControl);
					} catch (IOException ioe) {
						logger.error(String.format("Error while sending TCAbortMessage=%s", msg), ioe);
					}

					if (dtid != -1) {
						di = this.dialogs.get(dtid);
						if (di == null) {
							logger.error("No dialog/transaction for id: " + dtid);
						} else {
							di.processAbort(msg, localAddress, remoteAddress);
						}
					}
				}// if (tcUnidentified.getOriginatingTransactionId() != null)
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(String.format("Error while decoding Rx SccpMessage=%s", message), e);
		}
	}

	public void send(byte[] data, Byte desiredQos, SccpAddress destinationAddress, SccpAddress originatingAddress,
			int seqControl) throws IOException {
		// FIXME: add QOS
		ProtocolClass pClass = parameterFactory.createProtocolClass(0, 0);
		UnitData msg = messageFactory.createUnitData(pClass, destinationAddress, originatingAddress);
		msg.setData(data);
		sccpProvider.send(msg, seqControl);
	}

	public void deliver(DialogImpl dialogImpl, TCBeginIndicationImpl msg) {

		try {
			for (int index = 0; index < this.tcListeners.size(); index++) {
				TCListener lst = this.tcListeners.get(index);
				lst.onTCBegin(msg);
			}
		} catch (Exception e) {
			if (logger.isEnabledFor(Level.ERROR)) {
				logger.error("Received exception while delivering data to transport layer.", e);
			}
		}

	}

	public void deliver(DialogImpl dialogImpl, TCContinueIndicationImpl tcContinueIndication) {
		try {
			for (int index = 0; index < this.tcListeners.size(); index++) {
				TCListener lst = this.tcListeners.get(index);
				lst.onTCContinue(tcContinueIndication);
			}
		} catch (Exception e) {
			if (logger.isEnabledFor(Level.ERROR)) {
				logger.error("Received exception while delivering data to transport layer.", e);
			}
		}

	}

	public void deliver(DialogImpl dialogImpl, TCEndIndicationImpl tcEndIndication) {
		try {
			for (int index = 0; index < this.tcListeners.size(); index++) {
				TCListener lst = this.tcListeners.get(index);
				lst.onTCEnd(tcEndIndication);
			}
		} catch (Exception e) {
			if (logger.isEnabledFor(Level.ERROR)) {
				logger.error("Received exception while delivering data to transport layer.", e);
			}
		}
	}

	public void deliver(DialogImpl dialogImpl, TCPAbortIndicationImpl tcAbortIndication) {
		try {
			for (int index = 0; index < this.tcListeners.size(); index++) {
				TCListener lst = this.tcListeners.get(index);
				lst.onTCPAbort(tcAbortIndication);
			}
		} catch (Exception e) {
			if (logger.isEnabledFor(Level.ERROR)) {
				logger.error("Received exception while delivering data to transport layer.", e);
			}
		}

	}

	public void deliver(DialogImpl dialogImpl, TCUserAbortIndicationImpl tcAbortIndication) {
		try {
			for (int index = 0; index < this.tcListeners.size(); index++) {
				TCListener lst = this.tcListeners.get(index);
				lst.onTCUserAbort(tcAbortIndication);
			}
		} catch (Exception e) {
			if (logger.isEnabledFor(Level.ERROR)) {
				logger.error("Received exception while delivering data to transport layer.", e);
			}
		}

	}

	public void deliver(DialogImpl dialogImpl, TCUniIndicationImpl tcUniIndication) {
		try {
			for (int index = 0; index < this.tcListeners.size(); index++) {
				TCListener lst = this.tcListeners.get(index);
				lst.onTCUni(tcUniIndication);
			}
		} catch (Exception e) {
			if (logger.isEnabledFor(Level.ERROR)) {
				logger.error("Received exception while delivering data to transport layer.", e);
			}
		}
	}

	public void release(DialogImpl d) {
	    Long did = d.getDialogId();
		this.dialogs.remove(did);
		this.dialogIdIndex.push(did);
		try {
			for (int index = 0; index < this.tcListeners.size(); index++) {
				TCListener lst = this.tcListeners.get(index);
				lst.onDialogReleased(d);
			}
		} catch (Exception e) {
			if (logger.isEnabledFor(Level.ERROR)) {
				logger.error("Received exception while delivering dialog release.", e);
			}
		}

	}
	
	/**
	 * @param d
	 */
	public void timeout(DialogImpl d) {
		try {
			for (int index = 0; index < this.tcListeners.size(); index++) {
				TCListener lst = this.tcListeners.get(index);
				lst.onDialogTimeout(d);
			}
		} catch (Exception e) {
			if (logger.isEnabledFor(Level.ERROR)) {
				logger.error("Received exception while delivering dialog release.", e);
			}
		}
		
	}
	
	public TCAPStackImpl getStack()
	{
		return this.stack;
	}
	
	// ///////////////////////////////////////////
	// Some methods invoked by operation FSM //
	// //////////////////////////////////////////
	public Future createOperationTimer(Runnable operationTimerTask, long invokeTimeout) {

		return this._EXECUTOR.schedule(operationTimerTask, invokeTimeout, TimeUnit.MILLISECONDS);
	}

	public void operationTimedOut(InvokeImpl tcInvokeRequestImpl) {
		try {
			for (int index = 0; index < this.tcListeners.size(); index++) {
				TCListener lst = this.tcListeners.get(index);
				lst.onInvokeTimeout(tcInvokeRequestImpl);
			}
		} catch (Exception e) {
			if (logger.isEnabledFor(Level.ERROR)) {
				logger.error("Received exception while delivering Begin.", e);
			}
		}
	}


	void start() {
		logger.info("Starting TCAP Provider");
		this._EXECUTOR = Executors.newScheduledThreadPool(4);		
		this.sccpProvider.registerSccpListener(ssn, this);
		logger.info("Registered SCCP listener with address " + ssn);
	}

	void stop() {
		this._EXECUTOR.shutdown();
		this.sccpProvider.deregisterSccpListener(ssn);

	}

	
}
