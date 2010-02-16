package org.mobicents.protocols.ss7.sccp.impl.sctp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.impl.SccpProviderImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ProtocolClassImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.mobicents.protocols.ss7.sccp.impl.ud.UnitDataImpl;
import org.mobicents.protocols.ss7.sccp.impl.ud.XUnitDataImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.ss7.sctp.MTPListener;
import org.mobicents.ss7.sctp.MTPProvider;
import org.mobicents.ss7.sctp.MTPProviderFactory;


public class SccpSCTPProviderImpl extends SccpProviderImpl implements MTPListener {
	private static final Logger logger = Logger.getLogger(SccpSCTPProviderImpl.class);

	private MTPProvider mtpProvider;
	

	/** Creates a new instance of SccpProviderImpl */
	public SccpSCTPProviderImpl(Properties props) {
		this.mtpProvider = MTPProviderFactory.getInstance().getProvider(props);
		this.mtpProvider.addMtpListener(this);
		
	}



	public void receive(int service, int subservice, byte[] arg2) {
		// add check for SIO parts?
		// if(logger.isInfoEnabled())
	    //logger.info("Received MSU on L4, service: "+service+",subservice: "+subservice);
		new DeliveryHandler(arg2).run();

	}



	public void send(SccpAddress calledParty, SccpAddress callingParty, byte[] data) throws IOException {
		
		//FIXME:
		//if (this.linkIsUp) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			UnitDataImpl unitData = new UnitDataImpl(new ProtocolClassImpl(0, 0), calledParty, callingParty, data);
			unitData.encode(out);
			byte[] buf = out.toByteArray();
			// this.mtp3.send(si, ssf,buf);
			//this.txBuffer.add(ByteBuffer.wrap(buf));
			//FIXME: SLS, SI, SSI
			this.mtpProvider.send(0, 0, buf);
		//}

	}

	public void shutdown() {
		// if(this.mtp3 != null)
		// {
		// this.mtp3.stop();
		// }
		this.mtpProvider.removeMtpListener(this);
		this.mtpProvider.close();
		this.mtpProvider = null;

	}

	


	



	private class DeliveryHandler implements Runnable {

		private byte[] msg;

		public DeliveryHandler(byte[] msg) {
			super();
			this.msg = msg;
		}

		public void run() {
			try {
				if (listener != null) {
					ByteArrayInputStream bin = new ByteArrayInputStream(msg);
					DataInputStream in = new DataInputStream(bin);
					int mt;

					mt = in.readUnsignedByte();

					switch (mt) {
					case UnitDataImpl._MT:
						UnitDataImpl unitData = new UnitDataImpl();
						unitData.decode(in);

						listener.onMessage(unitData.getCalledParty(), unitData.getCallingParty(), unitData.getData());

						break;
					// 0x11
					case XUnitDataImpl._MT:
						XUnitDataImpl xunitData = new XUnitDataImpl();
						xunitData.decode(in);

						listener.onMessage(xunitData.getCalledParty(), xunitData.getCallingParty(), xunitData.getData());
						break;
					default:
						logger.error("Undefined message type!!!!! " + mt);
						
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("Failed to pass UD", e);
			}

		}

	}
	
	public static void main(String[] args)
	{
		
	}
}
