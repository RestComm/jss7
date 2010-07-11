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
import org.mobicents.protocols.ss7.isup.message.CircuitGroupResetAckMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupResetMessage;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.RangeAndStatus;

/**
 * @author baranowb
 * 
 */
public class ClientGRS implements ISUPListener {

	private ISUPStack isupStack;
	private ISUPProvider provider;
	private ISUPMessageFactory factory;
	private ISUPParameterFactory parameterFactory;

	private boolean _RCV_GRSA,  _RCV_TX_TERM;
	private boolean passed = true;
	private StringBuilder status = new StringBuilder();
	private ISUPClientTransaction ctx;
	
	
	public ClientGRS(ISUPStack isupStack) {
		super();
		this.isupStack = isupStack;
		this.provider = this.isupStack.getIsupProvider();
		this.factory = this.provider.getMessageFactory();
		this.parameterFactory = this.provider.getParameterFactory();
	}

	public void start() throws IllegalArgumentException, TransactionAlredyExistsException, ParameterRangeInvalidException, IOException {
		CircuitGroupResetMessage grs = this.factory.createGRS(12);

		
		
		// create obligatory params!
		
		RangeAndStatus ras = this.parameterFactory.createRangeAndStatus();
		ras.setRange((byte) 0x11);
		ras.setStatus(new byte[]{1,2,3});
		grs.setRangeAndStatus(ras);
		ctx = this.provider.createClientTransaction(grs);

		ctx.sendRequest();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.isup.ISUPListener#onMessage(org.mobicents
	 * .protocols.ss7.isup.message.ISUPMessage)
	 */
	public void onMessage(ISUPMessage message) {
		switch (message.getMessageType().getCode()) {
		case CircuitGroupResetAckMessage.MESSAGE_CODE:
			if(_RCV_GRSA)
			{
				passed = false;
				status.append("Received GRSA message more than once!\n");
			}
			
			if(message.getTransaction() == null || (message.getTransaction() instanceof ISUPServerTransaction) || !message.getTransaction().equals(ctx) )
			{
				passed = false;
				status.append("Wrong transaction object on GRSA "+message.getTransaction()+", local: "+this.ctx+"!\n");
			}
			_RCV_GRSA = true;
			break;
	
		default:
			passed = false;
			status.append("Received unexpected message, code: " + message.getMessageType().getCode() + "\n");
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
		if(_RCV_TX_TERM)
		{
			passed = false;
			status.append("Received CTX TERM more than once!\n");
		}
		_RCV_TX_TERM = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.isup.ISUPListener#onTransactionEnded(org.
	 * mobicents.protocols.ss7.isup.ISUPServerTransaction)
	 */
	public void onTransactionEnded(ISUPServerTransaction tx) {
		passed = false;
		status.append("Received STX TERM !\n");

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
		return passed && _RCV_GRSA && _RCV_TX_TERM;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		status.append("GRSA["+_RCV_GRSA+"]").append("\n");
		status.append("TX TERM["+_RCV_TX_TERM+"]").append("\n");
		return status.toString();
	}
	
	
	

}
