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
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.sccp.ActionReference;

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
	public ISUPClientTransactionImpl(ISUPMessage message, ISUPProviderBase provider, ISUPStackImpl stack, ActionReference actionReference) {
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
			this.startTimer();
			super.cancelGeneralTimer();
			super.startGeneralTimer();
			break;
		case TERMINATED:
			this.cancelGeneralTimer();
			this.cancelTimer();
			this.provider.onTransactionEnded(this);
			
			break;
		case TIMEDOUT:
			//just in case
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
	 * @see org.mobicents.protocols.ss7.isup.ISUPTransactionImpl#doGeneralTimeout()
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
			
			switch (super.message.getMessageType().getCode()) {
			case ISUPMessage._MESSAGE_CODE_IAM:
				// see: http://en.wikipedia.org/wiki/ISDN_User_Part
				switch (msg.getMessageType().getCode()) {
				case ISUPMessage._MESSAGE_CODE_ANM:
					// EOF
					setState(ISUPClientTransactionState.TERMINATED);
					break;
				case ISUPMessage._MESSAGE_CODE_ACM:
					// refresh timer?
					cancelGeneralTimer();
					startGeneralTimer();
					
				default:
					logger.error("Request to received unknown answer: "+msg.getMessageType().getCode()+", for IAM tx");
				}

				break;
				
			case ISUPMessage._MESSAGE_CODE_REL:
				
				switch (msg.getMessageType().getCode()) {

				case ISUPMessage._MESSAGE_CODE_RLC:
					// EOF
					setState(ISUPClientTransactionState.TERMINATED);
				default:
					logger.error("Request to received unknown answer: "+msg.getMessageType().getCode()+", for REL tx");
				}
				
				default:
					//default case
					this.setState(ISUPClientTransactionState.TERMINATED);
			}
			
			this.cancelTimer();
			//FIXME: add transition to terminated
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
		this.receiveResponseTimeout = stack.getExecutors().schedule(new ISUPClientTransactionTimeoutTask(this), stack.getClientTransactionAnswerTimeout(), TimeUnit.MILLISECONDS);
		
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
