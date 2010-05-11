package org.mobicents.protocols.ss7.isup.impl;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.isup.ISUPProvider;
import org.mobicents.protocols.ss7.isup.ISUPServerTransaction;
import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.sccp.ActionReference;

/**
 * Start time:13:34:09 2009-09-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class ISUPServerTransactionImpl extends ISUPTransactionImpl implements ISUPServerTransaction {
	private final static Logger logger = Logger.getLogger(ISUPServerTransactionImpl.class);
	protected ISUPServerTransactionState state = ISUPServerTransactionState.MESSAGE_RECEIVED;

	/**
	 * 
	 * @param message
	 * @param provider
	 * @param stack
	 */
	public ISUPServerTransactionImpl(ISUPMessage message, ISUPProviderBase provider, ISUPStackImpl stack, ActionReference actionReference) {
		super(message, provider, stack,actionReference);

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
		if(msg == null)
		{
			throw new NullPointerException("Message can not be null");
		}
		
		if(msg.getCircuitIdentificationCode() == null)
		{
			throw new IllegalArgumentException("CIC is not set in message.");
		}
		synchronized (this.state) {
			if (this.state != ISUPServerTransactionState.MESSAGE_RECEIVED || super.generalTimeoutFuture == null) {
				throw new IOException("Bad transaction state, either transaction timed out or answer has been already sent back: "+this.state);
			}
			
			// now the whole state machine!
			switch (super.message.getMessageType().getCode()) {
			case ISUPMessage._MESSAGE_CODE_IAM:
				// see: http://en.wikipedia.org/wiki/ISDN_User_Part
				switch (msg.getMessageType().getCode()) {
				case ISUPMessage._MESSAGE_CODE_ANM:
					// EOF
					this.provider.sendMessage(msg);
					setState(ISUPServerTransactionState.TERMINATED);
					break;
				case ISUPMessage._MESSAGE_CODE_ACM:
					// refresh timer?
					cancelGeneralTimer();
					startGeneralTimer();
					this.provider.sendMessage(msg);
					break;
				default:
					logger.error("Request to send unknown answer: "+msg.getMessageType().getCode()+", for IAM tx");
				}

				break;
				
			case ISUPMessage._MESSAGE_CODE_REL:
				
				switch (msg.getMessageType().getCode()) {

				case ISUPMessage._MESSAGE_CODE_RLC:
					// EOF
					this.provider.sendMessage(msg);
					setState(ISUPServerTransactionState.TERMINATED);
				default:
					logger.error("Request to send unknown answer: "+msg.getMessageType().getCode()+", for REL tx");
				}
				
				default:
					//default case
					this.provider.sendMessage(msg);
					this.setState(ISUPServerTransactionState.TERMINATED);
			}
			
			
		}
		

	}

}
