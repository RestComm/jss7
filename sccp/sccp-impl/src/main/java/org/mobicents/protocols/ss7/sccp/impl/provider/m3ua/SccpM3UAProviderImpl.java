/*
 * The Java Call Control API for CAMEL 2
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */

package org.mobicents.protocols.ss7.sccp.impl.provider.m3ua;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ConfigurationException;
import org.mobicents.protocols.ss7.mtp.RoutingLabel;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.impl.SccpProviderImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.utils.Utils;

/**
 * 
 * @author Oleg Kulikov
 * @deprecated - its obsolete now?
 */
public class SccpM3UAProviderImpl extends SccpProviderImpl implements Runnable, SccpProvider {

	private DatagramSocket socket;

	private Properties props;

	private String remoteAddress;
	private String localAddress;
	private int localPort;
	private int remotePort;

	//private PooledExecutor threadPool;
	private boolean stopped = false;

	private Logger logger = Logger.getLogger(SccpM3UAProviderImpl.class);

	/** Creates a new instance of SccpM3UAProviderImpl */
	public SccpM3UAProviderImpl() {
		super();
		

	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.sccp.impl.SccpProviderImpl#configure(java.util.Properties)
	 */
	@Override
	public void configure(Properties props) throws ConfigurationException {
		remoteAddress = props.getProperty("remote.address");
		localAddress = props.getProperty("local.address");

		localPort = Integer.parseInt(props.getProperty("local.port"));
		remotePort = Integer.parseInt(props.getProperty("remote.port"));
		super.configure(props);
	}

	public void run() {
		while (!stopped) {
			try {
				byte[] buffer = new byte[8446];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

				socket.receive(packet);
				if (logger.isDebugEnabled()) {
					logger.debug(Utils.hexDump("Packet received\n", packet));
				}

				byte[] data = new byte[packet.getLength()];
				System.arraycopy(packet.getData(), 0, data, 0, data.length);

				logger.debug("--->" + Utils.hexDump(data));
				// threadPool.execute(new Handler(this, data));
				super.receive(data);

			} catch (Exception e) {
				logger.error("I/O error occured while sending data to MTP3 driver", e);
			}
		}
	}

	public void send(SccpAddress calledParty, SccpAddress callingParty, byte[] data, RoutingLabel ar) throws IOException {

		byte[] buff = super.encodeToMSU(calledParty, callingParty, data, ar);

		DatagramPacket packet = new DatagramPacket(buff, buff.length, InetAddress.getByName(remoteAddress), remotePort);
		logger.debug(Utils.hexDump(buff));
		socket.send(packet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.sccp.impl.SccpProviderImpl#start()
	 */
	@Override
	public void start() throws IllegalStateException {
		//this.threadPool = new PooledExecutor(10);
		try {
			socket = new DatagramSocket(localPort, InetAddress.getByName(localAddress));
		} catch (Exception e) {
		}
		new Thread(super.THREAD_GROUP, this).start();
		logger.info("Running main thread");

	}

	public void stop() {
		stopped = true;
		//threadPool.shutdownNow();
		try {
			socket.disconnect();
			socket.close();
		} catch (Exception e) {
		}
	}
}
