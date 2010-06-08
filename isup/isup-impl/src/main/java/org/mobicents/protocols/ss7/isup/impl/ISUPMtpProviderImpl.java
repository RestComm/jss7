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
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.isup.ISUPClientTransaction;
import org.mobicents.protocols.ss7.isup.ISUPListener;
import org.mobicents.protocols.ss7.isup.ISUPProvider;
import org.mobicents.protocols.ss7.isup.ISUPServerTransaction;
import org.mobicents.protocols.ss7.isup.ISUPTransaction;
import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.TransactionAlredyExistsException;
import org.mobicents.protocols.ss7.isup.TransactionKey;
import org.mobicents.protocols.ss7.isup.impl.message.ISUPMessageFactoryImpl;
import org.mobicents.protocols.ss7.isup.impl.message.ISUPMessageImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.ISUPParameterFactoryImpl;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.mtp.ActionReference;
import org.mobicents.protocols.ss7.mtp.Mtp3;
import org.mobicents.protocols.ss7.stream.MTPListener;
import org.mobicents.protocols.ss7.stream.MTPProvider;



/**
 * Start time:09:18:14 2009-07-18<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
class ISUPMtpProviderImpl extends ISUPProviderBase implements ISUPProvider, MTPListener {

	private static final Logger logger = Logger.getLogger(ISUPMtpProviderImpl.class);
    private MTPProvider mtpProvider;

    
   //private boolean linkUp = false;
    
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
        this.ssi = Integer.parseInt(props.getProperty("isup.ssi"));
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
            throw new ParameterRangeInvalidException("Message does not have all mandatory parameters");
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
               
                //FIXME: add preprocessing? to ensure msg is valid?
                ISUPTransaction  tx =  preprocessIncomingMessage(msg,actionReference);
                
                for(int index = 0 ;index<listeners.size();index++)
                {
                	try{
                		ISUPListener lst = listeners.get(index);
                		lst.onMessage(msg);
                	}catch(Exception e)
                	{
                		e.printStackTrace();
                	}
                }

                
                //post process for CTX
                if(tx!=null && !tx.isServerTransaction())
                {
                	ISUPClientTransactionImpl ctx = (ISUPClientTransactionImpl) tx;
                	ctx.answerReceived(msg);
                }

            } catch (Exception e) {
                // FIXME: what should we do here? send back?
                e.printStackTrace();
                logger.error("Failed on data: "+Arrays.toString(mtp3Frame));
            }
        }
    }

    

    
}
