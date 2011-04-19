/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.mtp.pipe;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.mobicents.protocols.ConfigurationException;
import org.mobicents.protocols.StartFailedException;
import org.mobicents.protocols.ss7.mtp.Mtp3Listener;
import org.mobicents.protocols.ss7.mtp.provider.MtpListener;
import org.mobicents.protocols.ss7.mtp.provider.MtpProvider;

/**
 * Pipe Mtp provider
 * 
 * @author baranowb
 * 
 */
public class PipeMtpProviderImpl implements MtpProvider {

	private PipeMtpProviderImpl other;
	private MtpListener listener;

	public PipeMtpProviderImpl() {
		super();
		this.other = new PipeMtpProviderImpl(this);
		_EXECUTOR = Executors.newSingleThreadScheduledExecutor();
	}

	private PipeMtpProviderImpl(PipeMtpProviderImpl other) {
		super();
		this.other = other;
		_EXECUTOR = Executors.newSingleThreadScheduledExecutor();
	}

	/**
	 * @return the other
	 */
	public PipeMtpProviderImpl getOther() {
		return other;
	}

	public void indicateLinkUp() {
		if (this.listener != null)
			this.listener.linkUp();
	}

	public void indicateLinkDown() {
		if (this.listener != null)
			this.listener.linkDown();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.stream.MTPProvider#addMtpListener(org.mobicents
	 * .protocols.ss7.stream.MTPListener)
	 */
	public void setMtpListener(MtpListener lst) {
		listener = lst;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.mtp.provider.MtpProvider#configure(java.util
	 * .Properties)
	 */
	public void configure(Properties p) throws ConfigurationException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.mtp.provider.MtpProvider#isLinkUp()
	 */
	public boolean isLinkUp() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.stream.MTPProvider#send(byte[])
	 */
	public void send(byte[] msg) throws IOException {
		this.other.receive(msg);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.stream.MTPProvider#stop()
	 */
	public void stop() throws IllegalStateException {
		if (_EXECUTOR != null) {
			_EXECUTOR.shutdown();
			_EXECUTOR = null;
		}

	}

	private ScheduledExecutorService _EXECUTOR;

	private void receive(byte[] msg) {

		_EXECUTOR.schedule(new DataPasser(msg), 50, TimeUnit.MILLISECONDS);
	}

	private class DataPasser implements Runnable {

		private byte[] data;

		private DataPasser(byte[] data) {
			super();

			this.data = new byte[data.length];
			System.arraycopy(data, 0, this.data, 0, data.length);
		}

		public void run() {
			try {
				listener.receive(data);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void start() throws StartFailedException, IllegalStateException {
		// TODO Auto-generated method stub

	}

	public void setOriginalPointCode(int opc) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void setAdjacentPointCode(int dpc) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public int getAdjacentPointCode() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public int getOriginalPointCode() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public String getName() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public String getLinksetName() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void setNetworkIndicator(int ni) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public int getNetworkIndicator() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	
}
