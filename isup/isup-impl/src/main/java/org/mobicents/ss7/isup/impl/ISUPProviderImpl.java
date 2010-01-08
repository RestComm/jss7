/**
 * Start time:09:18:14 2009-07-18<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * 
 */
package org.mobicents.ss7.isup.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.mobicents.ss7.SS7PayloadListener;
import org.mobicents.ss7.SS7Provider;
import org.mobicents.ss7.isup.ISUPClientTransaction;
import org.mobicents.ss7.isup.ISUPListener;
import org.mobicents.ss7.isup.ISUPMessageFactory;
import org.mobicents.ss7.isup.ISUPProvider;
import org.mobicents.ss7.isup.ISUPServerTransaction;
import org.mobicents.ss7.isup.ISUPTransaction;
import org.mobicents.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.ss7.isup.TransactionAlredyExistsException;
import org.mobicents.ss7.isup.TransactionKey;
import org.mobicents.ss7.isup.impl.message.ISUPMessageFactoryImpl;
import org.mobicents.ss7.isup.message.AddressCompleteMessage;
import org.mobicents.ss7.isup.message.AnswerMessage;
import org.mobicents.ss7.isup.message.ApplicationTransportMessage;
import org.mobicents.ss7.isup.message.BlockingAckMessage;
import org.mobicents.ss7.isup.message.BlockingMessage;
import org.mobicents.ss7.isup.message.CallProgressMessage;
import org.mobicents.ss7.isup.message.ChargeInformationMessage;
import org.mobicents.ss7.isup.message.CircuitGroupBlockingAckMessage;
import org.mobicents.ss7.isup.message.CircuitGroupBlockingMessage;
import org.mobicents.ss7.isup.message.CircuitGroupQueryMessage;
import org.mobicents.ss7.isup.message.CircuitGroupQueryResponseMessage;
import org.mobicents.ss7.isup.message.CircuitGroupResetAckMessage;
import org.mobicents.ss7.isup.message.CircuitGroupResetMessage;
import org.mobicents.ss7.isup.message.CircuitGroupUnblockingAckMessage;
import org.mobicents.ss7.isup.message.CircuitGroupUnblockingMessage;
import org.mobicents.ss7.isup.message.ConfusionMessage;
import org.mobicents.ss7.isup.message.ConnectMessage;
import org.mobicents.ss7.isup.message.ContinuityCheckRequestMessage;
import org.mobicents.ss7.isup.message.ContinuityMessage;
import org.mobicents.ss7.isup.message.FacilityAcceptedMessage;
import org.mobicents.ss7.isup.message.FacilityMessage;
import org.mobicents.ss7.isup.message.FacilityRejectedMessage;
import org.mobicents.ss7.isup.message.FacilityRequestMessage;
import org.mobicents.ss7.isup.message.ForwardTransferMessage;
import org.mobicents.ss7.isup.message.ISUPMessage;
import org.mobicents.ss7.isup.message.IdentificationRequestMessage;
import org.mobicents.ss7.isup.message.IdentificationResponseMessage;
import org.mobicents.ss7.isup.message.InformationMessage;
import org.mobicents.ss7.isup.message.InformationRequestMessage;
import org.mobicents.ss7.isup.message.InitialAddressMessage;
import org.mobicents.ss7.isup.message.LoopPreventionMessage;
import org.mobicents.ss7.isup.message.LoopbackAckMessage;
import org.mobicents.ss7.isup.message.NetworkResourceManagementMessage;
import org.mobicents.ss7.isup.message.OverloadMessage;
import org.mobicents.ss7.isup.message.PassAlongMessage;
import org.mobicents.ss7.isup.message.PreReleaseInformationMessage;
import org.mobicents.ss7.isup.message.ReleaseCompleteMessage;
import org.mobicents.ss7.isup.message.ReleaseMessage;
import org.mobicents.ss7.isup.message.ResetCircuitMessage;
import org.mobicents.ss7.isup.message.ResumeMessage;
import org.mobicents.ss7.isup.message.SegmentationMessage;
import org.mobicents.ss7.isup.message.SubsequentAddressMessage;
import org.mobicents.ss7.isup.message.SubsequentDirectoryNumberMessage;
import org.mobicents.ss7.isup.message.SuspendMessage;
import org.mobicents.ss7.isup.message.UnblockingAckMessage;
import org.mobicents.ss7.isup.message.UnblockingMessage;
import org.mobicents.ss7.isup.message.UnequippedCICMessage;
import org.mobicents.ss7.isup.message.User2UserInformationMessage;
import org.mobicents.ss7.isup.message.UserPartAvailableMessage;
import org.mobicents.ss7.isup.message.UserPartTestMessage;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;

/**
 * Start time:09:18:14 2009-07-18<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
class ISUPProviderImpl implements ISUPProvider, SS7PayloadListener {

	private SS7Provider transportProvider;
	private final List<ISUPListener> listeners = new ArrayList<ISUPListener>();
	private ISUPStackImpl stack;
	private ISUPMessageFactory messageFactory;
	private ConcurrentHashMap transactionMap = new ConcurrentHashMap();

	/**
	 * @param provider
	 * @param stackImpl
	 * @param messageFactoryImpl
	 * 
	 */
	public ISUPProviderImpl(SS7Provider provider, ISUPStackImpl stackImpl) {
		this.transportProvider = provider;
		this.stack = stackImpl;
		this.messageFactory = new ISUPMessageFactoryImpl(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPProvider#getTransportProvider()
	 */
	public SS7Provider getTransportProvider() {

		return transportProvider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.isup.ISUPProvider#addListener(org.mobicents.isup.ISUPListener
	 * )
	 */
	public void addListener(ISUPListener listener) {
		listeners.add(listener);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.isup.ISUPProvider#removeListener(org.mobicents.isup.
	 * ISUPListener)
	 */
	public void removeListener(ISUPListener listener) {
		listeners.remove(listener);

	}

	/**
	 * @param mtp
	 */
	void setTransportProvider(SS7Provider mtp) {
		this.transportProvider = mtp;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.ss7.isup.ISUPProvider#createClientTransaction(org.mobicents
	 * .ss7.isup.message.ISUPMessage)
	 */
	public ISUPClientTransaction createClientTransaction(ISUPMessage msg) throws TransactionAlredyExistsException, IllegalArgumentException {
		TransactionKey key = msg.generateTransactionKey();
		if (this.transactionMap.containsKey(key)) {
			throw new TransactionAlredyExistsException("Transaction already exists for key: " + key);
		}
		ISUPClientTransactionImpl ctx = new ISUPClientTransactionImpl(msg, this, this.stack);
		this.transactionMap.put(msg.generateTransactionKey(), ctx);

		return ctx;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.ss7.isup.ISUPProvider#createServerTransaction(org.mobicents
	 * .ss7.isup.message.ISUPMessage)
	 */
	public ISUPServerTransaction createServerTransaction(ISUPMessage msg) throws TransactionAlredyExistsException, IllegalArgumentException {
		TransactionKey key = msg.generateTransactionKey();
		if (this.transactionMap.containsKey(key)) {
			throw new TransactionAlredyExistsException("Transaction already exists for key: " + key);
		}
		ISUPServerTransactionImpl stx = new ISUPServerTransactionImpl(msg, this, this.stack);
		this.transactionMap.put(msg.generateTransactionKey(), stx);

		return stx;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPProvider#getMessageFactory()
	 */
	public ISUPMessageFactory getMessageFactory() {
		return this.messageFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.isup.ISUPProvider#sendMessage(org.mobicents.isup.messages
	 * .ISUPMessage)
	 */
	public void sendMessage(ISUPMessage msg) throws ParameterRangeInvalidException, IOException {
		if (msg.hasAllMandatoryParameters()) {
			byte[] encoded = msg.encodeElement();
			transportProvider.sendData(encoded);

		} else {
			throw new ParameterRangeInvalidException("Message does not have all madnatory parameters");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.SS7PayloadListener#receivedMessage(byte[])
	 */
	public void receivedMessage(byte[] message) {
		DeliveryHandler dh = new DeliveryHandler(message);
		// possibly we should do here check on timer
		this.stack.getExecutors().schedule(dh, 0, TimeUnit.MICROSECONDS);

	}

	private final class DeliveryHandler implements Runnable {

		private byte[] payload;

		public DeliveryHandler(byte[] payload) {
			super();
			this.payload = payload;

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
				int commandCode = payload[0];

//				ISUPMessage msg = messageFactory.createCommand(commandCode);
//				for (int li = 0; li < listeners.size(); li++) {
//					ISUPListener listener = listeners.get(li);
//					if (listener != null) {
//						try {
//							listener.onCommand(msg);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				}

				
				
				
				switch (commandCode) {
				case ISUPMessage._MESSAGE_CODE_IAM:
					InitialAddressMessage IAM = messageFactory.createIAM();
					IAM.decodeElement(payload);

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

			} catch (Exception e) {
				// FIXME: what should we do here? send back?
				e.printStackTrace();
			}
		}
	}

	// FIXME: should we wait here to get all messages?

	void onTransactionTimeout(ISUPClientTransaction tx) {
		for (ISUPListener l : this.listeners) {
			try {
				l.onTransactionTimeout(tx);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		this.transactionMap.remove(tx.getOriginalMessage().generateTransactionKey());

	}

	void onTransactionTimeout(ISUPServerTransaction tx) {
		for (ISUPListener l : this.listeners) {
			try {
				l.onTransactionTimeout(tx);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		this.transactionMap.remove(tx.getOriginalMessage().generateTransactionKey());
	}

	void onTransactionEnded(ISUPClientTransaction tx) {
		for (ISUPListener l : this.listeners) {
			try {
				l.onTransactionEnded(tx);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		this.transactionMap.remove(tx.getOriginalMessage().generateTransactionKey());
	}

	void onTransactionEnded(ISUPServerTransaction tx) {
		for (ISUPListener l : this.listeners) {
			try {
				l.onTransactionEnded(tx);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		this.transactionMap.remove(tx.getOriginalMessage().generateTransactionKey());
	}



}
