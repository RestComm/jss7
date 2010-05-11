/**
 * Start time:09:18:14 2009-07-18<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.mobicents.protocols.ss7.isup.ISUPClientTransaction;
import org.mobicents.protocols.ss7.isup.ISUPListener;
import org.mobicents.protocols.ss7.isup.ISUPMessageFactory;
import org.mobicents.protocols.ss7.isup.ISUPProvider;
import org.mobicents.protocols.ss7.isup.ISUPServerTransaction;
import org.mobicents.protocols.ss7.isup.ISUPTransaction;
import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.TransactionAlredyExistsException;
import org.mobicents.protocols.ss7.isup.TransactionKey;
import org.mobicents.protocols.ss7.isup.impl.message.ISUPMessageFactoryImpl;
import org.mobicents.protocols.ss7.isup.impl.message.ISUPMessageImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.ISUPParameterFactoryImpl;
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
import org.mobicents.protocols.ss7.mtp.Mtp3;
import org.mobicents.protocols.ss7.sccp.ActionReference;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.stream.MTPListener;
import org.mobicents.protocols.ss7.stream.MTPProvider;



/**
 * Start time:09:18:14 2009-07-18<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
class ISUPMtpProviderImpl extends ISUPProviderBase implements ISUPProvider, MTPListener {

    private MTPProvider mtpProvider;

    
    private boolean linkUp = false;
    
    //preconfigured values of routing label.
	private ActionReference actionReference;

	private int opc;

	private int dpc;

	private int sls;

	private int ssi;

	private int si;
    /**
     * @param provider
     * @param stackImpl
     * @param messageFactoryImpl
     *
     */
    public ISUPMtpProviderImpl(MTPProvider provider, ISUPStackImpl stackImpl, Properties props) {
        
    	super(stackImpl);
    	this.mtpProvider = provider;
    	super.messageFactory = new ISUPMessageFactoryImpl(this);
    	super.parameterFactory = new ISUPParameterFactoryImpl();
        this.opc = Integer.parseInt(props.getProperty("isup.opc"));
        this.dpc = Integer.parseInt(props.getProperty("isup.dpc"));
        this.sls = Integer.parseInt(props.getProperty("isup.sls"));
        this.ssi = Integer.parseInt(props.getProperty("isup.ssf"));
        //this.si  = Integer.parseInt(props.getProperty("isup.si"));
        this.si = Mtp3._SI_SERVICE_ISUP;
        this.actionReference = new ActionReference(opc,dpc,sls,si,ssi);
        
    }


    /*
     * (non-Javadoc)
     *
     * @see
     * org.mobicents.protocols.ss7.isup.ISUPProvider#createClientTransaction(org.mobicents
     * .ss7.isup.message.ISUPMessage)
     */
    public ISUPClientTransaction createClientTransaction(ISUPMessage msg) throws TransactionAlredyExistsException, IllegalArgumentException {
        TransactionKey key = msg.generateTransactionKey();
        if (this.transactionMap.containsKey(key)) {
            throw new TransactionAlredyExistsException("Transaction already exists for key: " + key);
        }
        ActionReference actionReference = ((ISUPMessageImpl)msg).getActionReference();
        if(actionReference == null)
        {
        	actionReference = this.actionReference;
        }
        ISUPClientTransactionImpl ctx = new ISUPClientTransactionImpl(msg, this, this.stack,actionReference);
        this.transactionMap.put(msg.generateTransactionKey(), ctx);

        return ctx;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.mobicents.protocols.ss7.isup.ISUPProvider#createServerTransaction(org.mobicents
     * .ss7.isup.message.ISUPMessage)
     */
    public ISUPServerTransaction createServerTransaction(ISUPMessage msg) throws TransactionAlredyExistsException, IllegalArgumentException {
        TransactionKey key = msg.generateTransactionKey();
        if (this.transactionMap.containsKey(key)) {
            throw new TransactionAlredyExistsException("Transaction already exists for key: " + key);
        }
        ISUPServerTransactionImpl stx = new ISUPServerTransactionImpl(msg, this, this.stack,((ISUPMessageImpl)msg).getActionReference());
        this.transactionMap.put(msg.generateTransactionKey(), stx);

        return stx;

    }


	/*
     * (non-Javadoc)
     *
     * @see
     * org.mobicents.isup.ISUPProvider#sendMessage(org.mobicents.isup.messages
     * .ISUPMessage)
     */
    public void sendMessage(ISUPMessage msg) throws ParameterRangeInvalidException, IOException {
    	//FIXME: sync?
    	if(!linkUp)
    	{
    		throw new IOException("Link is not UP, can not send!");
    	}
        if (msg.hasAllMandatoryParameters()) {
            byte[] encoded = msg.encodeElement();
        
            
            //provider expects properly encoded mtp3 layer frame!
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ISUPMessageImpl msgImpl = (ISUPMessageImpl) msg;
            if(msgImpl.getActionReference()==null)
            {
            	bos.write(this.actionReference.getBackRouteHeader());
            }else
            {
            	bos.write(msgImpl.getActionReference().getBackRouteHeader());
            }
            
            bos.write(encoded);
            mtpProvider.send(bos.toByteArray());

        } else {
            throw new ParameterRangeInvalidException("Message does not have all madnatory parameters");
        }
    }


	public void receive(byte[] msg) {
		 	DeliveryHandler dh = new DeliveryHandler(msg);
	        // possibly we should do here check on timer
	        this.stack.getExecutors().schedule(dh, 0, TimeUnit.MICROSECONDS);
	        //dh.run();
	}
 
	private ISUPTransaction preprocessIncomingMessage(ISUPMessage msg,ActionReference actionReference)
	{
		//FIXME: should we create TX here?
		TransactionKey tk = msg.generateTransactionKey();
		ISUPMessageImpl msgImpl = (ISUPMessageImpl) msg;
		if(this.transactionMap.containsKey(tk))
		{
			//we have TX
			
			ISUPTransactionImpl tx = (ISUPTransactionImpl) this.transactionMap.get(tk);
			if(tx.getActionReference() == null)
			{
				tx.setActionReference(actionReference);
			}
			msgImpl.setTransaction(tx);
			return tx;
		}
		
		msgImpl.setActionReference(actionReference);
		return null;
	}

    private final class DeliveryHandler implements Runnable {

        private byte[] mtp3Frame;

        public DeliveryHandler(byte[] payload) {
            super();
            this.mtp3Frame = payload;

        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Runnable#run()
         */
        public void run() {
            
            try {
            	ActionReference actionReference = new ActionReference();
            	actionReference.setBackRouteHeader(mtp3Frame);
                int commandCode = mtp3Frame[5+2];//5(RoutingLabel)+2(CIC) - http://pt.com/page/tutorials/ss7-tutorial/mtp
                byte[] payload = new byte[mtp3Frame.length -5];
                System.arraycopy(mtp3Frame, 5, payload, 0, payload.length);
                //for post processing
                ISUPMessage msg = messageFactory.createCommand(commandCode);
                msg.decodeElement(payload);
               
                ISUPTransaction  tx =  preprocessIncomingMessage(msg,actionReference);
                


                
                //post process for CTX
                if(tx!=null && !tx.isServerTransaction())
                {
                	ISUPClientTransactionImpl ctx = (ISUPClientTransactionImpl) tx;
                	ctx.answerReceived(msg);
                }

            } catch (Exception e) {
                // FIXME: what should we do here? send back?
                e.printStackTrace();
            }
        }
    }

    

    
}
