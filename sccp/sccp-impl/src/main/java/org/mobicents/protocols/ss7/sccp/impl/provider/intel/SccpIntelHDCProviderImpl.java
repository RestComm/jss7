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

package org.mobicents.protocols.ss7.sccp.impl.provider.intel;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ConfigurationException;
import org.mobicents.protocols.StartFailedException;
import org.mobicents.protocols.ss7.mtp.RoutingLabel;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.impl.SccpProviderImpl;
import org.mobicents.protocols.ss7.sccp.impl.provider.intel.gt.InterProcessCommunicator;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.utils.Utils;

/**
 * 
 * @author $Author: kulikoff $
 * @version $Revision: 1.1 $
 */
public class SccpIntelHDCProviderImpl extends SccpProviderImpl implements Runnable, SccpProvider {

	private InterProcessCommunicator ipc;

	private int src = 0;
	private int dst = 0;

	private boolean stopped = false;

	private Logger logger = Logger.getLogger(SccpIntelHDCProviderImpl.class);

	/** Creates a new instance of SccpProviderImpl */
	public SccpIntelHDCProviderImpl() {
		super();
		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.sccp.impl.SccpProviderImpl#configure(java.util.Properties)
	 */
	@Override
	public void configure(Properties props) throws ConfigurationException {

		src = Integer.parseInt(props.getProperty("module.src"));
		dst = Integer.parseInt(props.getProperty("module.dest"));
		super.configure(props);
	}

	public synchronized void send(SccpAddress calledParty, SccpAddress callingParty, byte[] data, RoutingLabel ar) throws IOException {

		
		byte[] buf = super.encodeToMSU(calledParty, callingParty, data, ar);

		ipc.send(buf);

		if (logger.isDebugEnabled()) {
			logger.debug(Utils.hexDump("Sent message\n", buf));
		}
	}

	public void run() {
		while (!stopped) {
			try {
				if (logger.isDebugEnabled()) {
					logger.debug("Waiting for packet delivery");
				}

				byte[] packet = ipc.receive();

				if (logger.isDebugEnabled()) {
					logger.debug(Utils.hexDump("Packet received\n", packet));
				}

//				try {
//					threadPool.execute(new Handler(this, packet));
//				} catch (InterruptedException ie) {
//					logger.error("Thread pool interrupted", ie);
//					stopped = true;
//				}
				super.receive(packet);
			} catch (Exception e) {
				logger.error("I/O error occured while sending data to MTP3 driver", e);
			}
		}
		logger.info("Close main loop");
	}

	
	
	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.sccp.impl.SccpProviderImpl#start()
	 */
	@Override
	public void start() throws IllegalStateException {
		//this.threadPool = new PooledExecutor(10);
		new Thread(super.THREAD_GROUP,this).start();
		ipc = new InterProcessCommunicator(src, dst);
		logger.info("Started IPC");

		
		logger.info("Started main loop");
		
	}

	public void stop() {
		//threadPool.shutdownNow();
		stopped = true;
		
	}

}
