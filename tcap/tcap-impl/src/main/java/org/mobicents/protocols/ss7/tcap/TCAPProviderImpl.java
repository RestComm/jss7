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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.sccp.RemoteSccpStatus;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.SignallingPointStatus;
import org.mobicents.protocols.ss7.sccp.message.MessageFactory;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;
import org.mobicents.protocols.ss7.sccp.message.SccpNoticeMessage;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory;
import org.mobicents.protocols.ss7.tcap.api.DialogPrimitiveFactory;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcap.api.TCListener;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.TRPseudoState;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.DialogPortion;
import org.mobicents.protocols.ss7.tcap.asn.DialogRequestAPDUImpl;
import org.mobicents.protocols.ss7.tcap.asn.DialogResponseAPDU;
import org.mobicents.protocols.ss7.tcap.asn.DialogServiceProviderType;
import org.mobicents.protocols.ss7.tcap.asn.InvokeImpl;
import org.mobicents.protocols.ss7.tcap.asn.Result;
import org.mobicents.protocols.ss7.tcap.asn.ResultSourceDiagnostic;
import org.mobicents.protocols.ss7.tcap.asn.ResultType;
import org.mobicents.protocols.ss7.tcap.asn.TCAbortMessageImpl;
import org.mobicents.protocols.ss7.tcap.asn.TCNoticeIndicationImpl;
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

	private List<TCListener> tcListeners = new CopyOnWriteArrayList<TCListener>();
	protected ScheduledExecutorService _EXECUTOR;
	// boundry for Uni directional dialogs :), tx id is always encoded
	// on 4 octets, so this is its max value
	//private static final long _4_OCTETS_LONG_FILL = 4294967295l;
	private ComponentPrimitiveFactory componentPrimitiveFactory;
	private DialogPrimitiveFactory dialogPrimitiveFactory;
	private SccpProvider sccpProvider;

	private MessageFactory messageFactory;
	private ParameterFactory parameterFactory;
 
	private TCAPStackImpl stack; // originating TX id ~=Dialog, its direct
									// mapping, but not described
	// explicitly...
	private Map<Long, DialogImpl> dialogs = new HashMap<Long, DialogImpl>();
	
	private int seqControl = 0;
	private int ssn;
	private long curDialogId = 0;


	protected TCAPProviderImpl(SccpProvider sccpProvider, TCAPStackImpl stack, int ssn) {
		super();
		this.sccpProvider = sccpProvider;
		this.ssn = ssn;
		messageFactory = sccpProvider.getMessageFactory();
		parameterFactory = sccpProvider.getParameterFactory();
		this.stack = stack;

		this.componentPrimitiveFactory = new ComponentPrimitiveFactoryImpl(this);
		this.dialogPrimitiveFactory = new DialogPrimitiveFactoryImpl(this.componentPrimitiveFactory);
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
		if (this.dialogs.size() >= this.stack.getMaxDialogs())
			throw new TCAPException("Current dialog count exceeds its maximum value");

		while (true) {
			if (++this.curDialogId > Integer.MAX_VALUE)
				this.curDialogId = 1;
			Long id = this.curDialogId;
			if (!this.dialogs.containsKey(id))
				return id;
		}
	}

	// get next Seq Control value available
	private synchronized int getNextSeqControl() {
		seqControl++;
		if (seqControl > 255) {
			seqControl = 0;

		}
		return seqControl;
	}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.TCAPProvider#getNewUnstructuredDialog
	 * (org.mobicents.protocols.ss7.sccp.parameter.SccpAddress,
	 * org.mobicents.protocols.ss7.sccp.parameter.SccpAddress)
	 */
	public Dialog getNewUnstructuredDialog(SccpAddress localAddress, SccpAddress remoteAddress) throws TCAPException {
		return _getDialog(localAddress, remoteAddress, false, getNextSeqControl());
	}

	private Dialog getNewDialog(SccpAddress localAddress, SccpAddress remoteAddress, int seqControl) throws TCAPException {
		return _getDialog(localAddress, remoteAddress, true, seqControl);
	}

	private Dialog _getDialog(SccpAddress localAddress, SccpAddress remoteAddress, boolean structured, int seqControl) throws TCAPException {

		if (localAddress == null) {
			throw new NullPointerException("LocalAddress must not be null");
		}
		if (remoteAddress == null) {
			throw new NullPointerException("RemoteAddress must not be null");
		}

		if (structured) {
			synchronized (this.dialogs) {
				Long id = this.getAvailableTxId();
				DialogImpl di = new DialogImpl(localAddress, remoteAddress, id, structured, this._EXECUTOR, this, seqControl);

				this.dialogs.put(id, di);

				return di;
			}
		} else {
			DialogImpl di = new DialogImpl(localAddress, remoteAddress, null, structured, this._EXECUTOR, this, seqControl);
			return di;
		}
	}

	public void send(byte[] data, boolean returnMessageOnError, SccpAddress destinationAddress, SccpAddress originatingAddress, int seqControl)
			throws IOException {
		SccpDataMessage msg = messageFactory.createDataMessageClass1(destinationAddress, originatingAddress, data, seqControl, this.ssn, returnMessageOnError,
				null, null);
		sccpProvider.send(msg);
	}

	public int getMaxUserDataLength(SccpAddress calledPartyAddress, SccpAddress callingPartyAddress) {
		return this.sccpProvider.getMaxUserDataLength(calledPartyAddress, callingPartyAddress);
	}

	public void deliver(DialogImpl dialogImpl, TCBeginIndicationImpl msg) {

		try {
			for (TCListener lst : this.tcListeners) {
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
			for (TCListener lst : this.tcListeners) {
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
			for (TCListener lst : this.tcListeners) {
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
			for (TCListener lst : this.tcListeners) {
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
			for (TCListener lst : this.tcListeners) {
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
			for (TCListener lst : this.tcListeners) {
				lst.onTCUni(tcUniIndication);
			}
		} catch (Exception e) {
			if (logger.isEnabledFor(Level.ERROR)) {
				logger.error("Received exception while delivering data to transport layer.", e);
			}
		}
	}

	public void deliver(DialogImpl dialogImpl, TCNoticeIndicationImpl tcNoticeIndication) {
		try {
			for (TCListener lst : this.tcListeners) {
				lst.onTCNotice(tcNoticeIndication);
			}
		} catch (Exception e) {
			if (logger.isEnabledFor(Level.ERROR)) {
				logger.error("Received exception while delivering data to transport layer.", e);
			}
		}
	}

	public void release(DialogImpl d) {
	    Long did = d.getDialogId();
		synchronized (this.dialogs) {
			this.dialogs.remove(did);
		}
		try {
			for (TCListener lst : this.tcListeners) {
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
			for (TCListener lst : this.tcListeners) {
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
			for (TCListener lst : this.tcListeners) {
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

	protected void sendProviderAbort(PAbortCauseType pAbortCause, long remoteTransactionId, SccpAddress remoteAddress, SccpAddress localAddress, int seqControl) {

		TCAbortMessageImpl msg = (TCAbortMessageImpl) TcapFactory.createTCAbortMessage();
		msg.setDestinationTransactionId(remoteTransactionId);
		msg.setPAbortCause(pAbortCause);

		AsnOutputStream aos = new AsnOutputStream();
		try {
			msg.encode(aos);
			this.send(aos.toByteArray(), false, remoteAddress, localAddress, seqControl);
		} catch (Exception e) {
			if (logger.isEnabledFor(Level.ERROR)) {
				logger.error("Failed to send message: ", e);
			}
		}
	}

	protected void sendProviderAbort(DialogServiceProviderType pt, long remoteTransactionId, SccpAddress remoteAddress, SccpAddress localAddress,
			int seqControl, ApplicationContextName acn) {

		DialogPortion dp = TcapFactory.createDialogPortion();
		dp.setUnidirectional(false);

		DialogResponseAPDU apdu = TcapFactory.createDialogAPDUResponse();

		Result res = TcapFactory.createResult();
		res.setResultType(ResultType.RejectedPermanent);
		ResultSourceDiagnostic rsd = TcapFactory.createResultSourceDiagnostic();
		rsd.setDialogServiceProviderType(pt);
		apdu.setResultSourceDiagnostic(rsd);
		apdu.setResult(res);
		apdu.setApplicationContextName(acn);
		dp.setDialogAPDU(apdu);

		TCAbortMessageImpl msg = (TCAbortMessageImpl) TcapFactory.createTCAbortMessage();
		msg.setDestinationTransactionId(remoteTransactionId);
		msg.setDialogPortion(dp);

		AsnOutputStream aos = new AsnOutputStream();
		try {
			msg.encode(aos);
			this.send(aos.toByteArray(), false, remoteAddress, localAddress, seqControl);
		} catch (Exception e) {
			if (logger.isEnabledFor(Level.ERROR)) {
				logger.error("Failed to send message: ", e);
			}
		}
	}

	@Override
	public void onCoordRequest(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCoordResponse(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessage(SccpDataMessage message) {
		
		try {
			byte[] data = message.getData();
			SccpAddress localAddress = message.getCalledPartyAddress();
			SccpAddress remoteAddress = message.getCallingPartyAddress();

			// FIXME: Qs state that OtxID and DtxID consittute to dialog id.....

			// asnData - it should pass
			AsnInputStream ais = new AsnInputStream(data);

			// this should have TC message tag :)
			int tag = ais.readTag();

			switch (tag) {
			// continue first, usually we will get more of those. small perf
			// boost
			case TCContinueMessage._TAG:
				TCContinueMessage tcm = null;
				try {
					tcm = TcapFactory.createTCContinueMessage(ais);
				} catch (Exception e) {
					logger.error("Exception when parsing TCContinueMessage: " + e.toString(), e);

					// parsing OriginatingTransactionId
					ais = new AsnInputStream(data);
					tag = ais.readTag();
					TCUnidentifiedMessage tcUnidentified = new TCUnidentifiedMessage();
					tcUnidentified.decode(ais);
					if (tcUnidentified.getOriginatingTransactionId() != null) {
						this.sendProviderAbort(PAbortCauseType.BadlyFormattedTxPortion, tcUnidentified.getOriginatingTransactionId(), remoteAddress,
								localAddress, message.getSls());
					}
					return;
				}

				Long dialogId = tcm.getDestinationTransactionId();
				DialogImpl di = this.dialogs.get(dialogId);
				if (di == null) {
					logger.error("No dialog/transaction for id: " + dialogId);
					this.sendProviderAbort(PAbortCauseType.UnrecognizedTxID, tcm.getOriginatingTransactionId(), remoteAddress, localAddress,
							message.getSls());
				} else {
					di.processContinue(tcm, localAddress, remoteAddress);
				}
				break;

			case TCBeginMessage._TAG:
				TCBeginMessage tcb = null;
				try {
					tcb = TcapFactory.createTCBeginMessage(ais);
				} catch (Exception e) {
					logger.error("Exception when parsing TCBeginMessage: " + e.toString(), e);

					// parsing OriginatingTransactionId
					ais = new AsnInputStream(data);
					tag = ais.readTag();
					TCUnidentifiedMessage tcUnidentified = new TCUnidentifiedMessage();
					tcUnidentified.decode(ais);
					if (tcUnidentified.getOriginatingTransactionId() != null) {
						this.sendProviderAbort(PAbortCauseType.BadlyFormattedTxPortion, tcUnidentified.getOriginatingTransactionId(), remoteAddress,
								localAddress, message.getSls());
					}
					return;
				}
				if (tcb.getDialogPortion() != null && tcb.getDialogPortion().getDialogAPDU() != null
						&& tcb.getDialogPortion().getDialogAPDU() instanceof DialogRequestAPDUImpl) {
					DialogRequestAPDUImpl dlg = (DialogRequestAPDUImpl) tcb.getDialogPortion().getDialogAPDU();
					if (!dlg.getProtocolVersion().isSupportedVersion()) {
						logger.error("Unsupported protocol version of  has been received when parsing TCBeginMessage");
						this.sendProviderAbort(DialogServiceProviderType.NoCommonDialogPortion, tcb.getOriginatingTransactionId(), remoteAddress, localAddress,
								message.getSls(), dlg.getApplicationContextName());
						return;
					}
				}

				di = null;
				try {
					di = (DialogImpl) this.getNewDialog(localAddress, remoteAddress, message.getSls());
				} catch (TCAPException e) {
					this.sendProviderAbort(PAbortCauseType.ResourceLimitation, tcb.getOriginatingTransactionId(), remoteAddress, localAddress,
							message.getSls());
					logger.error("Too many registered current dialogs when receiving TCBeginMessage");
					return;
				}
				di.processBegin(tcb, localAddress, remoteAddress);

				break;

			case TCEndMessage._TAG:
				TCEndMessage teb = null;
				try {
					teb = TcapFactory.createTCEndMessage(ais);
				} catch (Exception e) {
					logger.error("Exception when parsing TCEndMessage: " + e.toString(), e);
					return;
				}

				dialogId = teb.getDestinationTransactionId();
				di = this.dialogs.get(dialogId);
				if (di == null) {
					logger.error("No dialog/transaction for id: " + dialogId);
				} else {
					di.processEnd(teb, localAddress, remoteAddress);

				}
				break;
			
			case TCAbortMessage._TAG:
				TCAbortMessage tub = null;
				try {
					tub = TcapFactory.createTCAbortMessage(ais);
				} catch (Exception e) {
					logger.error("Exception when parsing TCAbortMessage: " + e.toString(), e);
					return;
				}

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

					if (tcUnidentified.getDestinationTransactionId() != null) {
						Long dtid = tcUnidentified.getDestinationTransactionId();
						di = this.dialogs.get(dtid);
						if (di == null) {
							this.sendProviderAbort(PAbortCauseType.UnrecognizedMessageType, otid, remoteAddress, localAddress, message.getSls());
						} else {
							di.sendProviderAbort(PAbortCauseType.UnrecognizedMessageType);
						}
					} else {
						this.sendProviderAbort(PAbortCauseType.UnrecognizedMessageType, otid, remoteAddress, localAddress, message.getSls());
					}
				}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(String.format("Error while decoding Rx SccpMessage=%s", message), e);
		}
	}

	@Override
	public void onNotice(SccpNoticeMessage msg) {

		DialogImpl dialog = null;

		try {
			byte[] data = msg.getData();
			AsnInputStream ais = new AsnInputStream(data);

			// this should have TC message tag :)
			int tag = ais.readTag();

			TCUnidentifiedMessage tcUnidentified = new TCUnidentifiedMessage();
			tcUnidentified.decode(ais);

			if (tcUnidentified.getOriginatingTransactionId() != null) {
				long otid = tcUnidentified.getOriginatingTransactionId();
				dialog = this.dialogs.get(otid);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(String.format("Error while decoding Rx SccpNoticeMessage=%s", msg), e);
		}

		TCNoticeIndicationImpl ind = new TCNoticeIndicationImpl();
		ind.setRemoteAddress(msg.getCallingPartyAddress());
		ind.setLocalAddress(msg.getCalledPartyAddress());
		ind.setDialog(dialog);
		ind.setReportCause(msg.getReturnCause().getValue());

		if (dialog != null) {
			try {
				dialog.dialogLock.lock();

				this.deliver(dialog, ind);

				if (dialog.getState() != TRPseudoState.Active) {
					dialog.release();
				}
			} finally {
				dialog.dialogLock.unlock();
			}
		} else {
			this.deliver(dialog, ind);
		}
	}

	@Override
	public void onPcState(int arg0, SignallingPointStatus arg1, int arg2, RemoteSccpStatus arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onState(int arg0, int arg1, boolean arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}
}

