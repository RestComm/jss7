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

	
	private static final Logger logger = Logger.getLogger(ISUPMtpProviderImpl.class);
	private MtpProvider mtpProvider;



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


	}
	//tests only!
	public ISUPMtpProviderImpl(MtpProvider provider1, ISUPStackImpl isupStackImpl, Properties props) {
		super(isupStackImpl);
		this.mtpProvider = provider1;
		super.parameterFactory = new ISUPParameterFactoryImpl();
		super.messageFactory = new ISUPMessageFactoryImpl(this,super.parameterFactory);
	
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
	
		ISUPClientTransactionImpl ctx = new ISUPClientTransactionImpl(msg, this, this.stack);
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
		ISUPServerTransactionImpl stx = new ISUPServerTransactionImpl(msg, this, this.stack);
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
			
			int opc = mtpProvider.getOriginalPointCode();
	        int dpc = mtpProvider.getAdjacentPointCode();
	        int si = Mtp3._SI_SERVICE_ISUP;//ISUP
	        int sls = 0;//mtp3 will select correct
	        int ssi = mtpProvider.getSSI();

	        ByteArrayOutputStream bout = new ByteArrayOutputStream();
	        //encoding routing label
	        bout.write((byte) (ssi | (si & 0x0F)));
	        bout.write((byte) dpc);
	        bout.write((byte) (((dpc >> 8) & 0x3F) | ((opc & 0x03) << 6)));
	        bout.write((byte) (opc >> 2));
	        bout.write((byte) (((opc >> 10) & 0x0F) | ((sls & 0x0F) << 4)));

	   

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

	private ISUPTransaction preprocessIncomingMessage(ISUPMessage msg) {
		// FIXME: should we create TX here?
		TransactionKey tk = ((ISUPMessageImpl)msg).generateTransactionKey();
		ISUPMessageImpl msgImpl = (ISUPMessageImpl) msg;
		if (this.transactionMap.containsKey(tk)) {
			// we have TX

			ISUPTransactionImpl tx = (ISUPTransactionImpl) this.transactionMap.get(tk);
			
			msgImpl.setTransaction(tx);
			return tx;
		}
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
				int commandCode = mtp3Frame[1 + 2];// 1(SI)+2(CIC) -
													// http://pt.com/page/tutorials/ss7-tutorial/mtp
				byte[] payload = new byte[mtp3Frame.length - 1];
				System.arraycopy(mtp3Frame, 1, payload, 0, payload.length);
				// for post processing
				ISUPMessage msg = messageFactory.createCommand(commandCode);
				msg.decodeElement(payload);

				// FIXME: add preprocessing? to ensure msg is valid?
				ISUPTransaction tx = preprocessIncomingMessage(msg);

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
