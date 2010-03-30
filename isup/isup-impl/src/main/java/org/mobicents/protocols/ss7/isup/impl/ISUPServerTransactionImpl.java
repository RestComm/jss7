package org.mobicents.protocols.ss7.isup.impl;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ISUPServerTransaction;
import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;

/**
 * Start time:13:34:09 2009-09-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class ISUPServerTransactionImpl extends ISUPTransactionImpl implements ISUPServerTransaction {

	protected ISUPServerTransactionState state = ISUPServerTransactionState.MESSAGE_RECEIVED;

	/**
	 * 
	 * @param message
	 * @param provider
	 * @param stack
	 */
	public ISUPServerTransactionImpl(ISUPMessage message, ISUPProviderImpl provider, ISUPStackImpl stack) {
		super(message, provider, stack);

	}

	/**
	 * @param timedout
	 */
	public void setState(ISUPServerTransactionState state) {

		if (state == this.state) {
			return;
		}

		this.state = state;
		switch (this.state) {
		case MESSAGE_RECEIVED:
			break;
		case TERMINATED:
			this.cancelGeneralTimer();
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
	 * @see org.mobicents.protocols.ss7.isup.ISUPTransactionImpl#doGeneralTimeout()
	 */
	@Override
	protected void doGeneralTimeout() {
		synchronized (this.state) {
			switch (this.state) {
			case MESSAGE_RECEIVED:
				super.generalTimeoutFuture = null;
				this.setState(ISUPServerTransactionState.TIMEDOUT);
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
		return this.state == ISUPServerTransactionState.TERMINATED;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPTransaction#isTimedout()
	 */
	public boolean isTimedout() {
		return this.state == ISUPServerTransactionState.TIMEDOUT;
	}

	public Object getState() {
		return this.state;
	}

	private enum ISUPServerTransactionState {
		MESSAGE_RECEIVED, TERMINATED, TIMEDOUT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.isup.ISUPServerTransaction#sendAnswer(org.mobicents
	 * .ss7.isup.message.ISUPMessage)
	 */
	public void sendAnswer(ISUPMessage msg) throws ParameterRangeInvalidException, IllegalArgumentException, IOException {
		synchronized (this.state) {
			if (this.state != ISUPServerTransactionState.MESSAGE_RECEIVED || super.generalTimeoutFuture == null) {
				throw new IOException("Failed to send message");
			}
			this.provider.sendMessage(msg);

			this.setState(ISUPServerTransactionState.TERMINATED);
		}

	}

}
