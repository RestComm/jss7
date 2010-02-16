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

import org.mobicents.protocols.ss7.isup.ISUPClientTransaction;
import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;

/**
 * Start time:13:34:09 2009-09-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class ISUPClientTransactionImpl extends ISUPTransactionImpl implements ISUPClientTransaction {

	protected Future receiveResponseTimeout;
	protected ISUPClientTransactionState state = ISUPClientTransactionState.CREATED;

	/**
	 * 
	 * @param message
	 * @param provider
	 * @param stack
	 */
	public ISUPClientTransactionImpl(ISUPMessage message, ISUPProviderImpl provider, ISUPStackImpl stack) {
		super(message, provider, stack);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPClientTransaction#sendRequest()
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
			this.receiveResponseTimeout = stack.getExecutors().schedule(new ISUPClientTransactionTimeoutTask(this), stack.getClientTransactionAnswerTimeout(), TimeUnit.MILLISECONDS);
			super.cancelGeneralTimer();
			break;
		case TERMINATED:
			this.provider.onTransactionEnded(this);
			break;
		case TIMEDOUT:
			this.provider.onTransactionTimeout(this);
			break;
		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPTransactionImpl#doGeneralTimeout()
	 */
	@Override
	protected void doGeneralTimeout() {
		synchronized (this.state) {
			switch (this.state) {
			case CREATED:
				super.generalTimeoutFuture = null;
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
	 * @see org.mobicents.ss7.isup.ISUPTransaction#isTerminated()
	 */
	public boolean isTerminated() {
		return this.state == ISUPClientTransactionState.TERMINATED;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPTransaction#isTimedout()
	 */
	public boolean isTimedout() {
		return this.state == ISUPClientTransactionState.TIMEDOUT;
	}

	public Object getState() {
		return this.state;
	}

	void answerReceived() {
		synchronized (this.state) {
			if (this.state != ISUPClientTransactionState.MESSAGE_SENT) {
				return;
			}
			this.cancelReceiveAnswerTimer();
			//FIXME: add transition to terminated
		}
	}

	/**
	 * 
	 */
	private void cancelReceiveAnswerTimer() {
		if (this.receiveResponseTimeout != null) {
			this.receiveResponseTimeout.cancel(false);
			this.receiveResponseTimeout = null;
		}

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
