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

    private final List<ISUPListener> listeners = new ArrayList<ISUPListener>();
    private ISUPStackImpl stack;
    private ISUPMessageFactory messageFactory;
    private Map<TransactionKey, ISUPTransaction> transactionMap = new HashMap<TransactionKey, ISUPTransaction>();
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
            // byte[] should be only isup message, first byte is command code
            // Yeah its an overkill a bit, but its more user friendly to provide
            // methods for all, if we support more, we can add more/or generic
            // one?
        	
            // FIXME: we should use
            try {
            	ActionReference actionReference = new ActionReference();
            	actionReference.setBackRouteHeader(mtp3Frame);
                int commandCode = mtp3Frame[5+2];//5(RoutingLabel)+2(CIC) - http://pt.com/page/tutorials/ss7-tutorial/mtp
                byte[] payload = new byte[mtp3Frame.length -5];
                System.arraycopy(mtp3Frame, 5, payload, 0, payload.length);
                //for post processing
                ISUPMessage msg;
                ISUPTransaction tx;


                switch (commandCode) {
                    case ISUPMessage._MESSAGE_CODE_IAM:
                        InitialAddressMessage IAM = messageFactory.createIAM();
                        IAM.decodeElement(payload);
                        tx =  preprocessIncomingMessage(IAM,actionReference);
                        msg =IAM;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onIAM(IAM);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        break;
                    case ISUPMessage._MESSAGE_CODE_ACM:
                        AddressCompleteMessage ACM = messageFactory.createACM();
                        ACM.decodeElement(payload);
                        tx =  preprocessIncomingMessage(ACM,actionReference);
                        msg =ACM;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onACM(ACM);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    case ISUPMessage._MESSAGE_CODE_REL:
                        ReleaseMessage REL = messageFactory.createREL();
                        REL.decodeElement(payload);
                        tx =  preprocessIncomingMessage(REL,actionReference);
                        msg =REL;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onREL(REL);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    case ISUPMessage._MESSAGE_CODE_RLC:
                        ReleaseCompleteMessage RLC = messageFactory.createRLC();
                        RLC.decodeElement(payload);
                        tx =  preprocessIncomingMessage(RLC,actionReference);
                        msg =RLC;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onRLC(RLC);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_APT:
                        ApplicationTransportMessage APT = messageFactory.createAPT();
                        APT.decodeElement(payload);
                        tx =  preprocessIncomingMessage(APT,actionReference);
                        msg =APT;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onAPT(APT);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_ANM:
                        AnswerMessage ANM = messageFactory.createANM();
                        ANM.decodeElement(payload);
                        tx =  preprocessIncomingMessage(ANM,actionReference);
                        msg =ANM;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onANM(ANM);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_CPG:
                        CallProgressMessage CPG = messageFactory.createCPG();
                        CPG.decodeElement(payload);
                        tx =  preprocessIncomingMessage(CPG,actionReference);
                        msg =CPG;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onCPG(CPG);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_GRA:
                        CircuitGroupResetAckMessage GRA = messageFactory.createGRA();
                        GRA.decodeElement(payload);
                        tx =  preprocessIncomingMessage(GRA,actionReference);
                        msg =GRA;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onGRA(GRA);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_CFN:
                        ConfusionMessage CFN = messageFactory.createCNF();
                        CFN.decodeElement(payload);
                        tx =  preprocessIncomingMessage(CFN,actionReference);
                        msg =CFN;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onCFN(CFN);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_CON:
                        ConnectMessage CON = messageFactory.createCON();
                        CON.decodeElement(payload);
                        tx =  preprocessIncomingMessage(CON,actionReference);
                        msg =CON;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onCON(CON);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_COT:
                        ContinuityMessage COT = messageFactory.createCOT();
                        COT.decodeElement(payload);
                        tx =  preprocessIncomingMessage(COT,actionReference);
                        msg =COT;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onCOT(COT);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_FRJ:
                        FacilityRejectedMessage FRJ = messageFactory.createFRJ();
                        FRJ.decodeElement(payload);
                        tx =  preprocessIncomingMessage(FRJ,actionReference);
                        msg =FRJ;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onFRJ(FRJ);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_INF:
                        InformationMessage INF = messageFactory.createINF();
                        INF.decodeElement(payload);
                        tx =  preprocessIncomingMessage(INF,actionReference);
                        msg =INF;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onINF(INF);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        break;

                    case ISUPMessage._MESSAGE_CODE_INR:
                        InformationRequestMessage INR = messageFactory.createINR();
                        INR.decodeElement(payload);
                        tx =  preprocessIncomingMessage(INR,actionReference);
                        msg =INR;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onINR(INR);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_SAM:
                        SubsequentAddressMessage SAM = messageFactory.createSAM();
                        SAM.decodeElement(payload);
                        tx =  preprocessIncomingMessage(SAM,actionReference);
                        msg =SAM;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onSAM(SAM);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_SDN:
                        SubsequentDirectoryNumberMessage SDN = messageFactory.createSDN();
                        SDN.decodeElement(payload);
                        tx =  preprocessIncomingMessage(SDN,actionReference);
                        msg =SDN;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onSDN(SDN);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_FOT:
                        ForwardTransferMessage FOT = messageFactory.createFOT();
                        FOT.decodeElement(payload);
                        tx =  preprocessIncomingMessage(FOT,actionReference);
                        msg =FOT;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onFOT(FOT);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_RES:
                        ResumeMessage RES = messageFactory.createRES();
                        RES.decodeElement(payload);
                        tx =  preprocessIncomingMessage(RES,actionReference);
                        msg =RES;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onRES(RES);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_BLO:
                        BlockingMessage BLO = messageFactory.createBLO();
                        BLO.decodeElement(payload);
                        tx =  preprocessIncomingMessage(BLO,actionReference);
                        msg =BLO;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onBLO(BLO);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_BLA:
                        BlockingAckMessage BLA = messageFactory.createBLA();
                        BLA.decodeElement(payload);
                        tx =  preprocessIncomingMessage(BLA,actionReference);
                        msg =BLA;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onBLA(BLA);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_CCR:
                        ContinuityCheckRequestMessage CCR = messageFactory.createCCR();
                        CCR.decodeElement(payload);
                        tx =  preprocessIncomingMessage(CCR,actionReference);
                        msg =CCR;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onCCR(CCR);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_LPA:
                        LoopbackAckMessage LPA = messageFactory.createLPA();
                        LPA.decodeElement(payload);
                        tx =  preprocessIncomingMessage(LPA,actionReference);
                        msg =LPA;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onLPA(LPA);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_LPP:
                        LoopPreventionMessage LPP = messageFactory.createLPP();
                        LPP.decodeElement(payload);
                        tx =  preprocessIncomingMessage(LPP,actionReference);
                        msg =LPP;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onLPP(LPP);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_OLM:
                        OverloadMessage OLM = messageFactory.createOLM();
                        OLM.decodeElement(payload);
                        tx =  preprocessIncomingMessage(OLM,actionReference);
                        msg =OLM;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onOLM(OLM);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_SUS:
                        SuspendMessage SUS = messageFactory.createSUS();
                        SUS.decodeElement(payload);
                        tx =  preprocessIncomingMessage(SUS,actionReference);
                        msg =SUS;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onSUS(SUS);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_RSC:
                        ResetCircuitMessage RSC = messageFactory.createRSC();
                        RSC.decodeElement(payload);
                        tx =  preprocessIncomingMessage(RSC,actionReference);
                        msg =RSC;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onRSC(RSC);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_UBL:
                        UnblockingMessage UBL = messageFactory.createUBL();
                        UBL.decodeElement(payload);
                        tx =  preprocessIncomingMessage(UBL,actionReference);
                        msg =UBL;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onUBL(UBL);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_UBA:
                        UnblockingAckMessage UBA = messageFactory.createUBA();
                        UBA.decodeElement(payload);
                        tx =  preprocessIncomingMessage(UBA,actionReference);
                        msg =UBA;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onUBA(UBA);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_UCIC:
                        UnequippedCICMessage UCIC = messageFactory.createUCIC();
                        UCIC.decodeElement(payload);
                        tx =  preprocessIncomingMessage(UCIC,actionReference);
                        msg =UCIC;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onUCIC(UCIC);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_CGB:
                        CircuitGroupBlockingMessage CGB = messageFactory.createCGB();
                        CGB.decodeElement(payload);
                        tx =  preprocessIncomingMessage(CGB,actionReference);
                        msg =CGB;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onCGB(CGB);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_CGBA:
                        CircuitGroupBlockingAckMessage CGBA = messageFactory.createCGBA();
                        CGBA.decodeElement(payload);
                        tx =  preprocessIncomingMessage(CGBA,actionReference);
                        msg =CGBA;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onCGBA(CGBA);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_CGU:
                        CircuitGroupUnblockingMessage CGU = messageFactory.createCGU();
                        CGU.decodeElement(payload);
                        tx =  preprocessIncomingMessage(CGU,actionReference);
                        msg =CGU;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onCGU(CGU);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_CGUA:
                        CircuitGroupUnblockingAckMessage CGUA = messageFactory.createCGUA();
                        CGUA.decodeElement(payload);
                        tx =  preprocessIncomingMessage(CGUA,actionReference);
                        msg =CGUA;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onCGUA(CGUA);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_GRS:
                        CircuitGroupResetMessage GRS = messageFactory.createGRS();
                        GRS.decodeElement(payload);
                        tx =  preprocessIncomingMessage(GRS,actionReference);
                        msg =GRS;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onGRS(GRS);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_CQR:
                        CircuitGroupQueryResponseMessage CQR = messageFactory.createCQR();
                        CQR.decodeElement(payload);
                        tx =  preprocessIncomingMessage(CQR,actionReference);
                        msg =CQR;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onCQR(CQR);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_CQM:
                        CircuitGroupQueryMessage CQM = messageFactory.createCQM();
                        CQM.decodeElement(payload);
                        tx =  preprocessIncomingMessage(CQM,actionReference);
                        msg =CQM;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onCQM(CQM);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_FAA:
                        FacilityAcceptedMessage FAA = messageFactory.createFAA();
                        FAA.decodeElement(payload);
                        tx =  preprocessIncomingMessage(FAA,actionReference);
                        msg =FAA;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onFAA(FAA);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_FAR:
                        FacilityRequestMessage FAR = messageFactory.createFAR();
                        FAR.decodeElement(payload);
                        tx =  preprocessIncomingMessage(FAR,actionReference);
                        msg =FAR;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onFAR(FAR);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_PAM:
                        PassAlongMessage PAM = messageFactory.createPAM();
                        PAM.decodeElement(payload);
                        tx =  preprocessIncomingMessage(PAM,actionReference);
                        msg =PAM;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onPAM(PAM);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_PRI:
                        PreReleaseInformationMessage PRI = messageFactory.createPRI();
                        PRI.decodeElement(payload);
                        tx =  preprocessIncomingMessage(PRI,actionReference);
                        msg =PRI;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onPRI(PRI);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_FAC:
                        FacilityMessage FAC = messageFactory.createFAC();
                        FAC.decodeElement(payload);
                        tx =  preprocessIncomingMessage(FAC,actionReference);
                        msg =FAC;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onFAC(FAC);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_NRM:
                        NetworkResourceManagementMessage NRM = messageFactory.createNRM();
                        NRM.decodeElement(payload);
                        tx =  preprocessIncomingMessage(NRM,actionReference);
                        msg =NRM;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onNRM(NRM);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_IDR:
                        IdentificationRequestMessage IDR = messageFactory.createIDR();
                        IDR.decodeElement(payload);
                        tx =  preprocessIncomingMessage(IDR,actionReference);
                        msg =IDR;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onIDR(IDR);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_IRS:
                        IdentificationResponseMessage IRS = messageFactory.createIRS();
                        IRS.decodeElement(payload);
                        tx =  preprocessIncomingMessage(IRS,actionReference);
                        msg =IRS;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onIRS(IRS);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_SGM:
                        SegmentationMessage SGM = messageFactory.createSGM();
                        SGM.decodeElement(payload);
                        tx =  preprocessIncomingMessage(SGM,actionReference);
                        msg =SGM;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onSGM(SGM);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_CIM:
                        ChargeInformationMessage CIM = messageFactory.createCIM();
                        CIM.decodeElement(payload);
                        tx =  preprocessIncomingMessage(CIM,actionReference);
                        msg =CIM;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onCIM(CIM);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_UPA:
                        UserPartAvailableMessage UPA = messageFactory.createUPA();
                        UPA.decodeElement(payload);
                        tx =  preprocessIncomingMessage(UPA,actionReference);
                        msg =UPA;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onUPA(UPA);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_UPT:
                        UserPartTestMessage UPT = messageFactory.createUPT();
                        UPT.decodeElement(payload);
                        tx =  preprocessIncomingMessage(UPT,actionReference);
                        msg =UPT;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onUPT(UPT);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case ISUPMessage._MESSAGE_CODE_USR:
                        User2UserInformationMessage USR = messageFactory.createUSR();
                        USR.decodeElement(payload);
                        tx =  preprocessIncomingMessage(USR,actionReference);
                        msg =USR;
                        for (int li = 0; li < listeners.size(); li++) {
                            ISUPListener listener = listeners.get(li);
                            if (listener != null) {
                                try {
                                    listener.onUSR(USR);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Not supported comamnd code: " + commandCode);
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
            }
        }
    }

    

    
}
