/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.mtp.provider.m3ua;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ConfigurationException;
import org.mobicents.protocols.StartFailedException;
import org.mobicents.protocols.link.DataLink;
import org.mobicents.protocols.link.LinkState;
import org.mobicents.protocols.link.LinkStateListener;
import org.mobicents.protocols.ss7.mtp.provider.AbstractMtpProviderImpl;
import org.mobicents.protocols.ss7.mtp.provider.MtpProvider;
import org.mobicents.protocols.stream.api.SelectorKey;
import org.mobicents.protocols.stream.api.SelectorProvider;
import org.mobicents.protocols.stream.api.StreamSelector;

/**
 * base implementation over stream interface
 * 
 * @author baranowb
 * 
 */
public class M3UAProvider extends AbstractMtpProviderImpl implements MtpProvider, LinkStateListener {
	// oleg is miss using M3UA term....

	private Logger logger = Logger.getLogger(M3UAProvider.class);

	public static final String PROPERTY_LADDRESS = "mtp.address.local";
	public static final String PROPERTY_RADDRESS = "mtp.address.remote";

	protected DataLink remotePeerStream;
	protected StreamSelector selector;
	protected InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8998);
	protected InetSocketAddress remote = new InetSocketAddress("127.0.0.1", 1345);

	// //////////////
	// Some state //
	// //////////////
	protected State state = State.NOT_CONFIGURED;
	protected LinkState linkState = null;
	// ///////////////
	// Thread part //
	// ///////////////
	protected boolean run = false;
	protected Runner r = new Runner();
	protected Thread t = null;

	// ///////////
	// Buffers //
	// ///////////
	protected byte[] rxBuffer = new byte[172];
	// private byte[] txBuffer = new byte[172];
	protected LinkedList<byte[]> inputData = new LinkedList<byte[]>();

	/**
	 * 
	 */
	public M3UAProvider() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.mtp.provider.MtpProvider#configure(java.util
	 * .Properties)
	 */
	public void configure(Properties p) throws ConfigurationException {

		try {
			String s = p.getProperty(PROPERTY_LADDRESS);
			if (s != null) {
				this.address = parseStringAddress(s);
			}

			s = p.getProperty(PROPERTY_RADDRESS);
			if (s != null) {
				this.remote = parseStringAddress(s);
			}
			remotePeerStream = DataLink.open(address, remote);
			((DataLink) remotePeerStream).setListener(this);
			selector = SelectorProvider.getSelector("org.mobicents.protocols.link.SelectorImpl");
			state = State.CONFIGURED;
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.mtp.provider.MtpProvider#send(byte[])
	 */
	public void send(byte[] msu) throws IOException {
		if (this.linkState != LinkState.ACTIVE) {
			throw new IOException("Link is not in active state: " + this.linkState);
		}
		byte[] copy = new byte[msu.length];
		System.arraycopy(msu, 0, copy, 0, msu.length);
		this.inputData.add(copy);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.mtp.provider.MtpProvider#start()
	 */
	public void start() throws StartFailedException {
		if (state != state.CONFIGURED) {
			throw new StartFailedException("Provider not configured!");
		}
		if (logger.isInfoEnabled()) {
			logger.info("Starting M3UAProvider");
		}
		try {
			this.remotePeerStream.register(this.selector);
			this.remotePeerStream.activate(); //indicate activation of this stream end.
		} catch (IOException e) {
			throw new StartFailedException(e);
		}
		this.run = true;
		this.t = new Thread(r, "org.mobicents.ss7.M3-UA");
		this.t.start();
		state = State.RUNNING;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.mtp.provider.AbstractMtpProviderImpl#doStop()
	 */
	@Override
	protected void doStop() {
		if (logger.isInfoEnabled()) {
			logger.info("Stopping M3UAProvider");
		}
		this.selector.close();
		this.remotePeerStream.close();
		state = State.CONFIGURED;
		this.run = false;
		this.t = null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.mtp.provider.MtpProvider#isLinkUp()
	 */
	public boolean isLinkUp() {
		return this.linkState == LinkState.ACTIVE;
	}

	// ///////////////////
	// Link state info //
	// ///////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.link.LinkStateListener#onStateChange(org.mobicents
	 * .protocols.link.LinkState)
	 */
	public void onStateChange(LinkState linkState) {
		if (logger.isInfoEnabled()) {
			logger.info("Datalink change in M3UAProvider: " + linkState);
		}
		LinkState oldLinkState = this.linkState;
		this.linkState = linkState;
		if (oldLinkState == LinkState.ACTIVE && this.linkState != LinkState.ACTIVE) {
			this.inputData.clear();
		}

		if (oldLinkState != linkState.ACTIVE && linkState == LinkState.ACTIVE) {
			if (this.listener != null) {
				this.listener.linkUp();
			}
		} else if (oldLinkState == linkState.ACTIVE && linkState != LinkState.ACTIVE) {
			if (this.listener != null) {
				this.listener.linkDown();
			}
		}

	}

	protected void deliverToListener(byte[] rxBuffer, int len) {
		if (logger.isInfoEnabled()) {
			logger.info("Delivering to M3UAProvider listener: " + this.listener + " - " + len);
		}
		// FIXME: sync this?
		if (super.listener != null) {

			byte[] copy = new byte[len];
			System.arraycopy(rxBuffer, 0, copy, 0, len);
			super.listener.receive(copy);

		}

	}

	private static InetSocketAddress parseStringAddress(String s) {
		String[] d = s.split(":");

		return new InetSocketAddress(d[0], Integer.parseInt(d[1]));
	}

	private class Runner implements Runnable {

		public void run() {

			logger.info("Run LOOP: ---------- START ---------------");

			while (run) {
				try {
					Collection<SelectorKey> keys = selector.selectNow(StreamSelector.OP_READ, 10);
					for (SelectorKey key : keys) {
						logger.info("Run LOOP: ---------- READ ---------------");
						int len = key.getStream().read(rxBuffer);
						deliverToListener(rxBuffer, len);

					}

					keys.clear();
					keys = selector.selectNow(StreamSelector.OP_WRITE, 10);
					for (SelectorKey key : keys) {
						logger.info("Run LOOP: xxxxxxxxxxxxxxx WRITE xxxxxxxxxxxxxxxx");

						while (inputData.size() >= 0) {
							byte[] data = inputData.getFirst();
							int count = key.getStream().write(data);
							if (count != data.length) {
								logger.error("Send count: " + count + ", does not match data: " + data.length);
								// leave that buffer for resend op?
								continue;
							}
							inputData.removeFirst();

						}

					}
					keys.clear();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

	}

	private enum State {
		NOT_CONFIGURED, CONFIGURED, RUNNING;
	}

}
