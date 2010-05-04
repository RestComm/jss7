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

package org.mobicents.protocols.ss7.sccp.impl.intel;

import java.io.FileInputStream;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.ActionReference;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.impl.Handler;
import org.mobicents.protocols.ss7.sccp.impl.SccpProviderImpl;
import org.mobicents.protocols.ss7.sccp.impl.intel.gt.InterProcessCommunicator;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ProtocolClassImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.mobicents.protocols.ss7.sccp.impl.ud.UnitDataImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.utils.Utils;

import java.net.DatagramSocket;

import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;

/**
 * 
 * @author $Author: kulikoff $
 * @version $Revision: 1.1 $
 */
public class SccpIntelHDCProviderImpl extends SccpProviderImpl implements Runnable, SccpProvider {

	private Properties props;
	private InterProcessCommunicator ipc;
	private DatagramSocket socket;

	private int src = 0;
	private int dst = 0;

	private int opc;
	private int dpc;
	private int sls;
	private int ssf;
	private int si;

	private boolean stopped = false;
	private PooledExecutor threadPool = new PooledExecutor(10);
	private Logger logger = Logger.getLogger(SccpIntelHDCProviderImpl.class);

	/** Creates a new instance of SccpProviderImpl */
	public SccpIntelHDCProviderImpl(Properties props) {
		this.props = props;

		src = Integer.parseInt(props.getProperty("module.src"));
		dst = Integer.parseInt(props.getProperty("module.dest"));

		opc = Integer.parseInt(props.getProperty("sccp.opc"));
		dpc = Integer.parseInt(props.getProperty("sccp.dpc"));
		sls = Integer.parseInt(props.getProperty("sccp.sls"));
		ssf = Integer.parseInt(props.getProperty("sccp.ssf"));
		si = Integer.parseInt(props.getProperty("sccp.si"));

		ipc = new InterProcessCommunicator(src, dst);
		logger.info("Started IPC");

		new Thread(this).start();
		logger.info("Started main loop");
	}

	public synchronized void send(SccpAddress calledParty, SccpAddress callingParty, byte[] data, ActionReference ar) throws IOException {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		if (ar == null) {
			byte b = (byte) (ssf << 4 | si);
			out.write(b);
			b = (byte) dpc;
			out.write(b);

			b = (byte) (((dpc >> 8) & 0x3f) | ((opc & 0x03) << 6));
			out.write(b);

			b = (byte) (opc >> 2);
			out.write(b);

			b = (byte) (((opc >> 10) & 0x0f) | (sls << 4));
			out.write(b);
		} else {
			out.write(ar.getBackRouteHeader());
		}

		UnitDataImpl unitData = new UnitDataImpl(new ProtocolClassImpl(0, 0), calledParty, callingParty, data);
		unitData.encode(out);
		byte[] buf = out.toByteArray();

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

				try {
					threadPool.execute(new Handler(this, packet));
				} catch (InterruptedException ie) {
					logger.error("Thread pool interrupted", ie);
					stopped = true;
				}
			} catch (Exception e) {
				logger.error("I/O error occured while sending data to MTP3 driver", e);
			}
		}
		logger.info("Close main loop");
	}

	public void shutdown() {
		threadPool.shutdownNow();
		stopped = true;
	}

}
