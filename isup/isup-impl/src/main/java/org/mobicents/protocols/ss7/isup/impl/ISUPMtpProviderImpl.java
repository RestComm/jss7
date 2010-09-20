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
import org.mobicents.protocols.ConfigurationException;
import org.mobicents.protocols.StartFailedException;
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
import org.mobicents.protocols.ss7.mtp.Mtp3;
import org.mobicents.protocols.ss7.mtp.RoutingLabel;
import org.mobicents.protocols.ss7.mtp.provider.MtpListener;
import org.mobicents.protocols.ss7.mtp.provider.MtpProvider;
import org.mobicents.protocols.ss7.mtp.provider.MtpProviderFactory;

/**
 * Start time:09:18:14 2009-07-18<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
class ISUPMtpProviderImpl extends AbstractISUPProvider implements ISUPProvider, MtpListener {

	public static final String PROPERTY_OPC = "isup.opc";
	public static final String PROPERTY_DPC = "isup.dpc";
	public static final String PROPERTY_SLS = "isup.sls";
	public static final String PROPERTY_SSI = "isup.ssi";
	
	
	private static final Logger logger = Logger.getLogger(ISUPMtpProviderImpl.class);
	private MtpProvider mtpProvider;

	// private boolean linkUp = false;

	// preconfigured values of routing label.
	private RoutingLabel actionReference;

	/**
	 * @param provider
	 * @param stackImpl
	 * @param messageFactoryImpl
	 * @throws ConfigurationException 
	 * 
	 */
	public ISUPMtpProviderImpl(ISUPStackImpl stackImpl, Properties props) throws ConfigurationException {

		super(stackImpl);
		this.mtpProvider = MtpProviderFactory.getInstance().getProvider(props);
		super.parameterFactory = new ISUPParameterFactoryImpl();
		super.messageFactory = new ISUPMessageFactoryImpl(this,super.parameterFactory);
		
		int opc = Integer.parseInt(props.getProperty(PROPERTY_OPC));
		int dpc = Integer.parseInt(props.getProperty(PROPERTY_DPC));
		int sls = Integer.parseInt(props.getProperty(PROPERTY_SLS));
		int ssi = Integer.parseInt(props.getProperty(PROPERTY_SSI));
		// this.si = Integer.parseInt(props.getProperty("isup.si"));
		int si = Mtp3._SI_SERVICE_ISUP;
		this.actionReference = new RoutingLabel(opc, dpc, sls, si, ssi);

	}
	//tests only!
	public ISUPMtpProviderImpl(MtpProvider provider1, ISUPStackImpl isupStackImpl, Properties props) {
		super(isupStackImpl);
		this.mtpProvider = provider1;
		super.parameterFactory = new ISUPParameterFactoryImpl();
		super.messageFactory = new ISUPMessageFactoryImpl(this,super.parameterFactory);
		
		int opc = Integer.parseInt(props.getProperty(PROPERTY_OPC));
		int dpc = Integer.parseInt(props.getProperty(PROPERTY_DPC));
		int sls = Integer.parseInt(props.getProperty(PROPERTY_SLS));
		int ssi = Integer.parseInt(props.getProperty(PROPERTY_SSI));
		// this.si = Integer.parseInt(props.getProperty("isup.si"));
		int si = Mtp3._SI_SERVICE_ISUP;
		this.actionReference = new RoutingLabel(opc, dpc, sls, si, ssi);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.isup.ISUPProvider#createClientTransaction
	 * (org.mobicents .ss7.isup.message.ISUPMessage)
	 */
	public ISUPClientTransaction createClientTransaction(ISUPMessage msg) throws TransactionAlredyExistsException, IllegalArgumentException {
		if (msg == null) {
			throw new IllegalArgumentException("Parameter is null");
		}
		if (msg.getCircuitIdentificationCode() == null) {
			throw new IllegalArgumentException("CIC is not set in message");
		}
		TransactionKey key = ((ISUPMessageImpl)msg).generateTransactionKey();
		if (this.transactionMap.containsKey(key)) {
			throw new TransactionAlredyExistsException("Transaction already exists for key: " + key);
		}
		RoutingLabel actionReference = ((ISUPMessageImpl) msg).getRoutingLabel();
		if (actionReference == null) {
			//FIXME: this is bad, remove before next rel.
			actionReference = this.actionReference;
		}
		ISUPClientTransactionImpl ctx = new ISUPClientTransactionImpl(msg, this, this.stack, actionReference);
		this.transactionMap.put(((ISUPMessageImpl)msg).generateTransactionKey(), ctx);

		return ctx;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.isup.ISUPProvider#createServerTransaction
	 * (org.mobicents .ss7.isup.message.ISUPMessage)
	 */
	public ISUPServerTransaction createServerTransaction(ISUPMessage msg) throws TransactionAlredyExistsException, IllegalArgumentException {
		if (msg == null) {
			throw new IllegalArgumentException("Parameter is null");
		}
		if (msg.getCircuitIdentificationCode() == null) {
			throw new IllegalArgumentException("CIC is not set in message");
		}
		TransactionKey key = ((ISUPMessageImpl)msg).generateTransactionKey();
		if (this.transactionMap.containsKey(key)) {
			throw new TransactionAlredyExistsException("Transaction already exists for key: " + key);
		}
		ISUPServerTransactionImpl stx = new ISUPServerTransactionImpl(msg, this, this.stack, ((ISUPMessageImpl) msg).getRoutingLabel());
		this.transactionMap.put(((ISUPMessageImpl)msg).generateTransactionKey(), stx);

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
		// FIXME: sync?
		if (!linkUp) {
			throw new IOException("Link is not UP, can not send!");
		}
		if (msg.hasAllMandatoryParameters()) {
			byte[] encoded = msg.encodeElement();

			// provider expects properly encoded mtp3 layer frame!
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ISUPMessageImpl msgImpl = (ISUPMessageImpl) msg;
			if (msgImpl.getRoutingLabel() == null) {
				bos.write(this.actionReference.getBackRouteHeader());
			} else {
				bos.write(msgImpl.getRoutingLabel().getBackRouteHeader());
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
		// dh.run();
	}

	private ISUPTransaction preprocessIncomingMessage(ISUPMessage msg, RoutingLabel actionReference) {
		// FIXME: should we create TX here?
		TransactionKey tk = ((ISUPMessageImpl)msg).generateTransactionKey();
		ISUPMessageImpl msgImpl = (ISUPMessageImpl) msg;
		if (this.transactionMap.containsKey(tk)) {
			// we have TX

			ISUPTransactionImpl tx = (ISUPTransactionImpl) this.transactionMap.get(tk);
			if (tx.getRoutingLabel() == null) {
				tx.setRoutingLabel(actionReference);
			}
			msgImpl.setTransaction(tx);
			return tx;
		}

		msgImpl.setRoutingLabel(actionReference);
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
				RoutingLabel actionReference = new RoutingLabel();
				actionReference.setBackRouteHeader(mtp3Frame);
				int commandCode = mtp3Frame[5 + 2];// 5(RoutingLabel)+2(CIC) -
													// http://pt.com/page/tutorials/ss7-tutorial/mtp
				byte[] payload = new byte[mtp3Frame.length - 5];
				System.arraycopy(mtp3Frame, 5, payload, 0, payload.length);
				// for post processing
				ISUPMessage msg = messageFactory.createCommand(commandCode);
				msg.decodeElement(payload);

				// FIXME: add preprocessing? to ensure msg is valid?
				ISUPTransaction tx = preprocessIncomingMessage(msg, actionReference);

				for (int index = 0; index < listeners.size(); index++) {
					try {
						ISUPListener lst = listeners.get(index);
						lst.onMessage(msg);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				// post process for CTX
				if (tx != null && !tx.isServerTransaction()) {
					ISUPClientTransactionImpl ctx = (ISUPClientTransactionImpl) tx;
					ctx.answerReceived(msg);
				} else if (tx != null) {
					// its server
					ISUPServerTransactionImpl stx = (ISUPServerTransactionImpl) tx;
					stx.requestDelivered(msg);
				}

			} catch (Exception e) {
				// FIXME: what should we do here? send back?
				e.printStackTrace();
				logger.error("Failed on data: " + Arrays.toString(mtp3Frame));
			}
		}
	}

	public void start() throws IllegalStateException, StartFailedException {
		this.mtpProvider.setMtpListener(this);
		this.mtpProvider.start();

	}

	public void stop() {
		
		this.mtpProvider.stop();

	}

}
