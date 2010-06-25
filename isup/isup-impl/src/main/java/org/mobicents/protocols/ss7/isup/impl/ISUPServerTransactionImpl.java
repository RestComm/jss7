package org.mobicents.protocols.ss7.isup.impl;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.isup.ISUPServerTransaction;
import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.AddressCompleteMessage;
import org.mobicents.protocols.ss7.isup.message.AnswerMessage;
import org.mobicents.protocols.ss7.isup.message.BlockingAckMessage;
import org.mobicents.protocols.ss7.isup.message.BlockingMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupBlockingAckMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupBlockingMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupResetAckMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupResetMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupUnblockingAckMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupUnblockingMessage;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.message.InitialAddressMessage;
import org.mobicents.protocols.ss7.isup.message.ReleaseCompleteMessage;
import org.mobicents.protocols.ss7.isup.message.ReleaseMessage;
import org.mobicents.protocols.ss7.isup.message.ResetCircuitMessage;
import org.mobicents.protocols.ss7.isup.message.UnblockingAckMessage;
import org.mobicents.protocols.ss7.isup.message.UnblockingMessage;
import org.mobicents.protocols.ss7.mtp.RoutingLabel;;

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
	public ISUPServerTransactionImpl(ISUPMessage message, ISUPProviderBase provider, ISUPStackImpl stack, RoutingLabel actionReference) {
		super(message, provider, stack, actionReference);

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
	 * @see
	 * org.mobicents.protocols.ss7.isup.ISUPTransactionImpl#doGeneralTimeout()
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
	 * org.mobicents.protocols.ss7.isup.ISUPServerTransaction#sendAnswer(org
	 * .mobicents .ss7.isup.message.ISUPMessage)
	 */
	public void sendAnswer(ISUPMessage msg) throws ParameterRangeInvalidException, IllegalArgumentException, IOException {
		if (msg == null) {
			throw new NullPointerException("Message can not be null");
		}

		if (msg.getCircuitIdentificationCode() == null) {
			throw new IllegalArgumentException("CIC is not set in message.");
		}
		synchronized (this.state) {
			if (this.state != ISUPServerTransactionState.MESSAGE_RECEIVED || super.generalTimeoutFuture == null) {
				throw new IOException("Bad transaction state, either transaction timed out or answer has been already sent back: "
						+ this.state);
			}

			// now the whole state machine!
			switch (super.message.getMessageType().getCode()) {
			
			//Tx: IAM scenario
			case InitialAddressMessage.MESSAGE_CODE:
				// see: http://en.wikipedia.org/wiki/ISDN_User_Part
				switch (msg.getMessageType().getCode()) {
				case AnswerMessage.MESSAGE_CODE:
					// EOF
					this.provider.sendMessage(msg);
					setState(ISUPServerTransactionState.TERMINATED);
					break;
				case AddressCompleteMessage.MESSAGE_CODE:
					// refresh timer?
					cancelGeneralTimer();
					startGeneralTimer();
					this.provider.sendMessage(msg);
					break;
				default:
					logger.error("Request to send unknown answer: " + msg.getMessageType().getCode() + ", for IAM tx");
					// EOF
					setState(ISUPServerTransactionState.TERMINATED);
					throw new IllegalArgumentException("Wrong message type!");
				}

				break;
				// Tx: REL scenario
			case ReleaseMessage.MESSAGE_CODE:

				switch (msg.getMessageType().getCode()) {

				case ReleaseCompleteMessage.MESSAGE_CODE:
					// EOF
					this.provider.sendMessage(msg);
					setState(ISUPServerTransactionState.TERMINATED);
					break;
				default:
					logger.error("Request to send unknown answer: " + msg.getMessageType().getCode() + ", for REL tx");
					// EOF
					setState(ISUPServerTransactionState.TERMINATED);
					throw new IllegalArgumentException("Wrong message type!");
				}
				break;
				// Tx: UBL scenario
			case UnblockingMessage.MESSAGE_CODE:

				switch (msg.getMessageType().getCode()) {

				case UnblockingAckMessage.MESSAGE_CODE:
					this.provider.sendMessage(msg);
					// EOF
					setState(ISUPServerTransactionState.TERMINATED);
					break;
				default:
					logger.error("Request to send unknown answer: " + msg.getMessageType().getCode() + ", for UBL tx");
					// EOF
					setState(ISUPServerTransactionState.TERMINATED);
					throw new IllegalArgumentException("Wrong message type!");
				}
				break;
				//Tx: CGU scenario
			case CircuitGroupUnblockingMessage.MESSAGE_CODE:

				switch (msg.getMessageType().getCode()) {

				case CircuitGroupUnblockingAckMessage.MESSAGE_CODE:
					this.provider.sendMessage(msg);
					// EOF
					setState(ISUPServerTransactionState.TERMINATED);
					break;
				default:
					logger.error("Request to send unknown answer: " + msg.getMessageType().getCode() + ", for CGU tx");
					// EOF
					setState(ISUPServerTransactionState.TERMINATED);
					throw new IllegalArgumentException("Wrong message type!");
				}
				
				break;
				//Tx: GRS scenario
			case CircuitGroupResetMessage.MESSAGE_CODE:

				switch (msg.getMessageType().getCode()) {

				case CircuitGroupResetAckMessage.MESSAGE_CODE:
					this.provider.sendMessage(msg);
					// EOF
					setState(ISUPServerTransactionState.TERMINATED);
					break;
				default:
					logger.error("Request to send unknown answer: " + msg.getMessageType().getCode() + ", for GRS tx");
					// EOF
					setState(ISUPServerTransactionState.TERMINATED);
					throw new IllegalArgumentException("Wrong message type!");
				}
				break;
				//Tx: CGB scenario
			case CircuitGroupBlockingMessage.MESSAGE_CODE:

				switch (msg.getMessageType().getCode()) {

				case CircuitGroupBlockingAckMessage.MESSAGE_CODE:
					this.provider.sendMessage(msg);
					// EOF
					setState(ISUPServerTransactionState.TERMINATED);
					break;
				default:
					logger.error("Request to send unknown answer: " + msg.getMessageType().getCode() + ", for CGB tx");
					// EOF
					setState(ISUPServerTransactionState.TERMINATED);
					throw new IllegalArgumentException("Wrong message type!");
				}
				break;
				//Tx: BLO scenario
			case BlockingMessage.MESSAGE_CODE:

				switch (msg.getMessageType().getCode()) {

				case BlockingAckMessage.MESSAGE_CODE:
					this.provider.sendMessage(msg);
					// EOF
					setState(ISUPServerTransactionState.TERMINATED);
					break;
				default:
					logger.error("Request to send unknown answer: " + msg.getMessageType().getCode() + ", for BLO tx");
					// EOF
					setState(ISUPServerTransactionState.TERMINATED);
					throw new IllegalArgumentException("Wrong message type!");
				}
				break;
			default:
				// default case
				this.provider.sendMessage(msg);
				this.setState(ISUPServerTransactionState.TERMINATED);
			}

		}

	}
	/**
	 * This method is called after passed msg has been delivered
	 * @param msg
	 */
	public void requestDelivered(ISUPMessage msg) {
		switch (msg.getMessageType().getCode()) {
		case ResetCircuitMessage.MESSAGE_CODE:
			
			this.setState(ISUPServerTransactionState.TERMINATED);
			break;
		default:
			break;
		}
		
	}

}
