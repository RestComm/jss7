/**
 * 
 */
package org.mobicents.protocols.ss7.isup.impl;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ISUPClientTransaction;
import org.mobicents.protocols.ss7.isup.ISUPListener;
import org.mobicents.protocols.ss7.isup.ISUPMessageFactory;
import org.mobicents.protocols.ss7.isup.ISUPProvider;
import org.mobicents.protocols.ss7.isup.ISUPServerTransaction;
import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.TransactionAlredyExistsException;
import org.mobicents.protocols.ss7.isup.TransactionKey;
import org.mobicents.protocols.ss7.isup.impl.message.ISUPMessageFactoryImpl;
import org.mobicents.protocols.ss7.isup.impl.message.ISUPMessageImpl;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.sccp.ActionReference;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * Provider for sccp
 * @author baranowb
 *
 */
public class ISUPSccpProviderImpl extends ISUPProviderBase implements ISUPProvider, SccpListener {

	
	
	
	public ISUPSccpProviderImpl(SccpProvider sccpTransportProvider, ISUPStackImpl isupStackImpl) {
		super(isupStackImpl);
		super.messageFactory = new ISUPMessageFactoryImpl(this);
	}

	public ISUPClientTransaction createClientTransaction(ISUPMessage msg) throws TransactionAlredyExistsException, IllegalArgumentException {
		if(msg == null)
    	{
    		throw new IllegalArgumentException("Parameter is null");
    	}
    	if(msg.getCircuitIdentificationCode() == null)
    	{
    		throw new IllegalArgumentException("CIC is not set in message");
    	}
		TransactionKey key = msg.generateTransactionKey();
        if (this.transactionMap.containsKey(key)) {
            throw new TransactionAlredyExistsException("Transaction already exists for key: " + key);
        }
        ActionReference actionReference = ((ISUPMessageImpl)msg).getActionReference();
        if(actionReference == null)
        {
        	//nothing :), SCCP will provide
        }
        ISUPClientTransactionImpl ctx = new ISUPClientTransactionImpl(msg, this, this.stack,actionReference);
        this.transactionMap.put(msg.generateTransactionKey(), ctx);

        return ctx;
	}

	public ISUPServerTransaction createServerTransaction(ISUPMessage msg) throws TransactionAlredyExistsException, IllegalArgumentException {
		if(msg == null)
    	{
    		throw new IllegalArgumentException("Parameter is null");
    	}
    	if(msg.getCircuitIdentificationCode() == null)
    	{
    		throw new IllegalArgumentException("CIC is not set in message");
    	}
		TransactionKey key = msg.generateTransactionKey();
        if (this.transactionMap.containsKey(key)) {
            throw new TransactionAlredyExistsException("Transaction already exists for key: " + key);
        }
        ISUPServerTransactionImpl stx = new ISUPServerTransactionImpl(msg, this, this.stack,((ISUPMessageImpl)msg).getActionReference());
        this.transactionMap.put(msg.generateTransactionKey(), stx);

        return stx;
	}

	public void sendMessage(ISUPMessage msg) throws ParameterRangeInvalidException, IOException {
		throw new UnsupportedOperationException();
		
	}

	public void onMessage(SccpAddress arg0, SccpAddress arg1, byte[] arg2, ActionReference arg3) {
		// TODO Auto-generated method stub
		
	}

	
}
