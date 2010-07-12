/**
 * Start time:13:34:09 2009-09-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.impl;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.isup.ISUPClientTransaction;
import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.AddressCompleteMessage;
import org.mobicents.protocols.ss7.isup.message.AnswerMessage;
import org.mobicents.protocols.ss7.isup.message.BlockingAckMessage;
import org.mobicents.protocols.ss7.isup.message.BlockingMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupBlockingAckMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupBlockingMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupResetAckMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupResetMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupUnblockingMessage;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.message.InitialAddressMessage;
import org.mobicents.protocols.ss7.isup.message.ReleaseCompleteMessage;
import org.mobicents.protocols.ss7.isup.message.ReleaseMessage;
import org.mobicents.protocols.ss7.isup.message.ResetCircuitMessage;
import org.mobicents.protocols.ss7.isup.message.UnblockingAckMessage;
import org.mobicents.protocols.ss7.isup.message.UnblockingMessage;
import org.mobicents.protocols.ss7.mtp.RoutingLabel;


/**
 * Start time:13:34:09 2009-09-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class ISUPClientTransactionImpl extends ISUPTransactionImpl implements ISUPClientTransaction {
	private static final Logger logger = Logger.getLogger(ISUPClientTransactionImpl.class);
	protected Future receiveResponseTimeout;
	protected ISUPClientTransactionState state = ISUPClientTransactionState.CREATED;

	/**
	 * 
	 * @param message
	 * @param provider
	 * @param stack
	 */
	public ISUPClientTransactionImpl(ISUPMessage message, AbstractISUPProvider provider, ISUPStackImpl stack, RoutingLabel actionReference) {
		super(message, provider, stack, actionReference);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPClientTransaction#sendRequest()
	 */
	public void sendRequest() throws ParameterRangeInvalidException, IOException {

		synchronized (this.state) {
			if (state != ISUPClientTransactionState.CREATED) {
				return;
			}

			this.provider.sendMessage(this.message);
			this.setState(ISUPClientTransactionState.MESSAGE_SENT);
		}

	}

	/**
	 * @param timedout
	 */
	public void setState(ISUPClientTransactionState state) {

		if (state == this.state)
			return;

		this.state = state;
		switch (this.state) {
		case CREATED:
			break;
		case MESSAGE_SENT:
			
			//some txs are one shots :)
			switch (this.message.getMessageType().getCode()) {
			case ResetCircuitMessage.MESSAGE_CODE:
				
				this.setState(ISUPClientTransactionState.TERMINATED);
				break;
			default:
				this.startTimer();
				super.cancelGeneralTimer();
				super.startGeneralTimer();
			}
			
			break;
		case TERMINATED:
			this.cancelGeneralTimer();
			this.cancelTimer();
			this.provider.onTransactionEnded(this);

			break;
		case TIMEDOUT:
			// just in case
			this.cancelGeneralTimer();
			this.cancelTimer();
			this.provider.onTransactionTimeout(this);
			break;
		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.isup.ISUPTransactionImpl#doGeneralTimeout()
	 */
	@Override
	protected void doGeneralTimeout() {

		synchronized (this.state) {
			switch (this.state) {
			case CREATED:
				super.generalTimeoutFuture = null;
				this.cancelTimer();
				this.setState(ISUPClientTransactionState.TIMEDOUT);

				break;
			default:
				//
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPTransaction#isTerminated()
	 */
	public boolean isTerminated() {
		return this.state == ISUPClientTransactionState.TERMINATED;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPTransaction#isTimedout()
	 */
	public boolean isTimedout() {
		return this.state == ISUPClientTransactionState.TIMEDOUT;
	}

	public Object getState() {
		return this.state;
	}

	void answerReceived(ISUPMessage msg) {
		synchronized (this.state) {
			if (this.state != ISUPClientTransactionState.MESSAGE_SENT) {
				return;
			}
			this.cancelTimer();
			// FIXME: add transition to terminated
			switch (super.message.getMessageType().getCode()) {

			// Tx: IAM scenario
			case InitialAddressMessage.MESSAGE_CODE:
				// see: http://en.wikipedia.org/wiki/ISDN_User_Part
				switch (msg.getMessageType().getCode()) {
				case AnswerMessage.MESSAGE_CODE:
					// EOF
					setState(ISUPClientTransactionState.TERMINATED);
					break;
				case AddressCompleteMessage.MESSAGE_CODE:
					// refresh timer?
					cancelGeneralTimer();
					startGeneralTimer();
					
					break;
				default:
					logger.error("Request to received unknown answer: " + msg.getMessageType().getCode() + ", for IAM tx");
					// EOF
					setState(ISUPClientTransactionState.TERMINATED);
				}

				break;
			// Tx: REL scenario
			case ReleaseMessage.MESSAGE_CODE:

				switch (msg.getMessageType().getCode()) {

				case ReleaseCompleteMessage.MESSAGE_CODE:
					// EOF
					setState(ISUPClientTransactionState.TERMINATED);
					break;
				default:
					logger.error("Request to received unknown answer: " + msg.getMessageType().getCode() + ", for REL tx");
					// EOF
					setState(ISUPClientTransactionState.TERMINATED);
				}
				break;
				// Tx: UBL scenario
			case UnblockingMessage.MESSAGE_CODE:

				switch (msg.getMessageType().getCode()) {

				case UnblockingAckMessage.MESSAGE_CODE:
					// EOF
					setState(ISUPClientTransactionState.TERMINATED);
					break;
				default:
					logger.error("Request to received unknown answer: " + msg.getMessageType().getCode() + ", for UBL tx");
					// EOF
					setState(ISUPClientTransactionState.TERMINATED);
				}
				break;
				//Tx: CGU scenario
			case CircuitGroupUnblockingMessage.MESSAGE_CODE:

				switch (msg.getMessageType().getCode()) {

				case CircuitGroupBlockingAckMessage.MESSAGE_CODE:
					// EOF
					setState(ISUPClientTransactionState.TERMINATED);
					break;
				default:
					logger.error("Request to received unknown answer: " + msg.getMessageType().getCode() + ", for CGU tx");
					// EOF
					setState(ISUPClientTransactionState.TERMINATED);
				}
				
				break;
				//Tx: GRS scenario
			case CircuitGroupResetMessage.MESSAGE_CODE:

				switch (msg.getMessageType().getCode()) {

				case CircuitGroupResetAckMessage.MESSAGE_CODE:
					// EOF
					setState(ISUPClientTransactionState.TERMINATED);
					break;
				default:
					logger.error("Request to received unknown answer: " + msg.getMessageType().getCode() + ", for GRS tx");
					// EOF
					setState(ISUPClientTransactionState.TERMINATED);
				}
				break;
				//Tx: CGB scenario
			case CircuitGroupBlockingMessage.MESSAGE_CODE:

				switch (msg.getMessageType().getCode()) {

				case CircuitGroupBlockingAckMessage.MESSAGE_CODE:
					// EOF
					setState(ISUPClientTransactionState.TERMINATED);
					break;
				default:
					logger.error("Request to received unknown answer: " + msg.getMessageType().getCode() + ", for CGB tx");
					// EOF
					setState(ISUPClientTransactionState.TERMINATED);
				}
				break;
				//Tx: BLO scenario
			case BlockingMessage.MESSAGE_CODE:

				switch (msg.getMessageType().getCode()) {

				case BlockingAckMessage.MESSAGE_CODE:
					// EOF
					setState(ISUPClientTransactionState.TERMINATED);
					break;
				default:
					logger.error("Request to received unknown answer: " + msg.getMessageType().getCode() + ", for BLO tx");
					// EOF
					setState(ISUPClientTransactionState.TERMINATED);
				}
				break;
				//for not handled or defined yet.
			default:
				// default case
				this.setState(ISUPClientTransactionState.TERMINATED);
			}
			
			
		}
	}

	/**
	 * 
	 */
	private void cancelTimer() {
		if (this.receiveResponseTimeout != null) {
			this.receiveResponseTimeout.cancel(false);
			this.receiveResponseTimeout = null;
		}

	}

	private void startTimer() {
		this.receiveResponseTimeout = stack.getExecutors().schedule(new ISUPClientTransactionTimeoutTask(this),
				stack.getClientTransactionAnswerTimeout(), TimeUnit.MILLISECONDS);

	}

	private enum ISUPClientTransactionState {
		CREATED, MESSAGE_SENT, TERMINATED, TIMEDOUT;
	}

	private class ISUPClientTransactionTimeoutTask implements Runnable {
		private ISUPClientTransactionImpl ctx;

		public ISUPClientTransactionTimeoutTask(ISUPClientTransactionImpl ctx) {
			super();
			this.ctx = ctx;
		}

		public void run() {
			synchronized (ctx.state) {
				switch (ctx.state) {
				case MESSAGE_SENT:
					//
					ctx.receiveResponseTimeout = null;
					ctx.setState(ISUPClientTransactionState.TIMEDOUT);

					break;
				default:
					// do nothing.
				}
			}

		}

	}

}
