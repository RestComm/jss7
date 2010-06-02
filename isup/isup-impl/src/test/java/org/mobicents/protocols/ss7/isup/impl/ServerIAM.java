/**
 * 
 */
package org.mobicents.protocols.ss7.isup.impl;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ISUPClientTransaction;
import org.mobicents.protocols.ss7.isup.ISUPListener;
import org.mobicents.protocols.ss7.isup.ISUPMessageFactory;
import org.mobicents.protocols.ss7.isup.ISUPParameterFactory;
import org.mobicents.protocols.ss7.isup.ISUPProvider;
import org.mobicents.protocols.ss7.isup.ISUPServerTransaction;
import org.mobicents.protocols.ss7.isup.ISUPStack;
import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.TransactionAlredyExistsException;
import org.mobicents.protocols.ss7.isup.message.AddressCompleteMessage;
import org.mobicents.protocols.ss7.isup.message.AnswerMessage;
import org.mobicents.protocols.ss7.isup.message.ApplicationTransportMessage;
import org.mobicents.protocols.ss7.isup.message.BlockingAckMessage;
import org.mobicents.protocols.ss7.isup.message.BlockingMessage;
import org.mobicents.protocols.ss7.isup.message.CallProgressMessage;
import org.mobicents.protocols.ss7.isup.message.ChargeInformationMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupBlockingAckMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupBlockingMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupQueryMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupQueryResponseMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupResetAckMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupResetMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupUnblockingAckMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupUnblockingMessage;
import org.mobicents.protocols.ss7.isup.message.ConfusionMessage;
import org.mobicents.protocols.ss7.isup.message.ConnectMessage;
import org.mobicents.protocols.ss7.isup.message.ContinuityCheckRequestMessage;
import org.mobicents.protocols.ss7.isup.message.ContinuityMessage;
import org.mobicents.protocols.ss7.isup.message.FacilityAcceptedMessage;
import org.mobicents.protocols.ss7.isup.message.FacilityMessage;
import org.mobicents.protocols.ss7.isup.message.FacilityRejectedMessage;
import org.mobicents.protocols.ss7.isup.message.FacilityRequestMessage;
import org.mobicents.protocols.ss7.isup.message.ForwardTransferMessage;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.message.IdentificationRequestMessage;
import org.mobicents.protocols.ss7.isup.message.IdentificationResponseMessage;
import org.mobicents.protocols.ss7.isup.message.InformationMessage;
import org.mobicents.protocols.ss7.isup.message.InformationRequestMessage;
import org.mobicents.protocols.ss7.isup.message.InitialAddressMessage;
import org.mobicents.protocols.ss7.isup.message.LoopPreventionMessage;
import org.mobicents.protocols.ss7.isup.message.LoopbackAckMessage;
import org.mobicents.protocols.ss7.isup.message.NetworkResourceManagementMessage;
import org.mobicents.protocols.ss7.isup.message.OverloadMessage;
import org.mobicents.protocols.ss7.isup.message.PassAlongMessage;
import org.mobicents.protocols.ss7.isup.message.PreReleaseInformationMessage;
import org.mobicents.protocols.ss7.isup.message.ReleaseCompleteMessage;
import org.mobicents.protocols.ss7.isup.message.ReleaseMessage;
import org.mobicents.protocols.ss7.isup.message.ResetCircuitMessage;
import org.mobicents.protocols.ss7.isup.message.ResumeMessage;
import org.mobicents.protocols.ss7.isup.message.SegmentationMessage;
import org.mobicents.protocols.ss7.isup.message.SubsequentAddressMessage;
import org.mobicents.protocols.ss7.isup.message.SubsequentDirectoryNumberMessage;
import org.mobicents.protocols.ss7.isup.message.SuspendMessage;
import org.mobicents.protocols.ss7.isup.message.UnblockingAckMessage;
import org.mobicents.protocols.ss7.isup.message.UnblockingMessage;
import org.mobicents.protocols.ss7.isup.message.UnequippedCICMessage;
import org.mobicents.protocols.ss7.isup.message.User2UserInformationMessage;
import org.mobicents.protocols.ss7.isup.message.UserPartAvailableMessage;
import org.mobicents.protocols.ss7.isup.message.UserPartTestMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.BackwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyCategory;
import org.mobicents.protocols.ss7.isup.message.parameter.ForwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.NatureOfConnectionIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.TransmissionMediumRequirement;

/**
 * @author baranowb
 * 
 */
public class ServerIAM implements ISUPListener {

	private ISUPStack isupStack;
	private ISUPProvider provider;
	private ISUPMessageFactory factory;
	private ISUPParameterFactory parameterFactory;

	private boolean _RCV_IAM, _SND_ACM, _SND_ANM, _RCV_TX_TERM;
	private boolean passed = true;
	private StringBuilder status = new StringBuilder();
	private ISUPServerTransaction stx;

	public ServerIAM(ISUPStack isupStack) {
		super();
		this.isupStack = isupStack;
		this.provider = this.isupStack.getIsupProvider();
		this.factory = this.provider.getMessageFactory();
		this.parameterFactory = this.provider.getParameterFactory();
	}

	public void start() throws IllegalArgumentException, TransactionAlredyExistsException, ParameterRangeInvalidException, IOException {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.isup.ISUPListener#onMessage(org.mobicents
	 * .protocols.ss7.isup.message.ISUPMessage)
	 */
	public void onMessage(ISUPMessage message) {
		try {
			switch (message.getMessageType().getCode()) {
			case InitialAddressMessage.MESSAGE_CODE:
				if (_RCV_IAM) {
					passed = false;
					status.append("Received IAM message more than once!\n");
				}
			
				_RCV_IAM = true;
				this.stx = this.provider.createServerTransaction(message);
				// send ACM
				AddressCompleteMessage acm = this.factory.createACM();
				BackwardCallIndicators bci = this.parameterFactory.createBackwardCallIndicators();
				bci.setHoldingIndicator(true);
				acm.setBackwardCallIndicators(bci);
				acm.setCircuitIdentificationCode(message.getCircuitIdentificationCode());

				
				this.stx.sendAnswer(acm);
				_SND_ACM = true;
				// wait
				Thread.currentThread().sleep(2000);

				AnswerMessage anm = this.factory.createANM();
				anm.setCircuitIdentificationCode(message.getCircuitIdentificationCode());
				this.stx.sendAnswer(anm);
				_SND_ANM = true;
				break;

			default:
				passed = false;
				status.append("Received unexpected message, code: " + message.getMessageType().getCode() + "\n");
			}
		} catch (Exception e) {
			passed = false;
			e.printStackTrace();
			status.append("Failed due to exception: ").append(e).append("\n");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.isup.ISUPListener#onTransactionEnded(org.
	 * mobicents.protocols.ss7.isup.ISUPClientTransaction)
	 */
	public void onTransactionEnded(ISUPClientTransaction tx) {
		passed = false;
		status.append("Received CTX TERM !\n");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.isup.ISUPListener#onTransactionEnded(org.
	 * mobicents.protocols.ss7.isup.ISUPServerTransaction)
	 */
	public void onTransactionEnded(ISUPServerTransaction tx) {
		if (_RCV_TX_TERM) {
			passed = false;
			status.append("Received STX TERM more than once!\n");
		}
		_RCV_TX_TERM = true;
		
		

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.isup.ISUPListener#onTransactionTimeout(org
	 * .mobicents.protocols.ss7.isup.ISUPClientTransaction)
	 */
	public void onTransactionTimeout(ISUPClientTransaction tx) {
		passed = false;
		status.append("Received CTX Timeout !\n");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.isup.ISUPListener#onTransactionTimeout(org
	 * .mobicents.protocols.ss7.isup.ISUPServerTransaction)
	 */
	public void onTransactionTimeout(ISUPServerTransaction tx) {
		passed = false;
		status.append("Received STX Timeout !\n");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPListener#onTransportDown()
	 */
	public void onTransportDown() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPListener#onTransportUp()
	 */
	public void onTransportUp() {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the passed
	 */
	public boolean isPassed() {
		return passed && _RCV_IAM && _SND_ACM && _SND_ANM && _RCV_TX_TERM;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		status.append("IAM[" + _RCV_IAM + "]").append("\n");
		status.append("ACM[" + _SND_ANM + "]").append("\n");
		status.append("ANM[" + _SND_ANM + "]").append("\n");
		status.append("TX TERM[" + _RCV_TX_TERM + "]").append("\n");
		return status.toString();
	}

}
