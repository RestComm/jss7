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
package org.mobicents.protocols.ss7.sccp.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.mobicents.protocols.ConfigurationException;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpMessageImpl;
import org.mobicents.protocols.ss7.sccp.message.SccpMessage;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * @author baranowb
 * 
 */
public class PipeSccpProviderImpl extends AbstractSccpProvider {
	// If you ever touch/remove this class. I will find you!

	private PipeSccpProviderImpl other;
	private SccpListener listener;

	/**
     *
     */
	public PipeSccpProviderImpl() {
		this.other = new PipeSccpProviderImpl(this);

	}

	private PipeSccpProviderImpl(PipeSccpProviderImpl other) {
		super();
		this.other = other;

	}

	/**
	 * @return the other
	 */
	public PipeSccpProviderImpl getOther() {
		return other;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.sccp.SccpProvider#configure(java.util.Properties
	 * )
	 */
	public void configure(Properties p) throws ConfigurationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void start() throws IllegalStateException {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.sccp.SccpProvider#shutdown()
	 */
	public void stop() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.sccp.SccpProvider#send(org.mobicents.protocols
	 * .ss7.sccp.message.SccpMessage)
	 */
	@Override
	public void send(SccpMessage message) throws IOException {
		 this.other.receiveData(message);

	}

	private ScheduledExecutorService _EXECUTOR = Executors.newSingleThreadScheduledExecutor();

	private void receiveData(SccpMessage message) {
		_EXECUTOR.schedule(new DataPasser((SccpMessageImpl)message), 50, TimeUnit.MILLISECONDS);

	}

	private class DataPasser implements Runnable {
		
		private byte[] data;

		private DataPasser(SccpMessageImpl message) {
			super();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(3);
			try {
				message.encode(baos);
				data = baos.toByteArray();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		public void run() {
			try {
				listener.onMessage(parse(new ByteArrayInputStream(data)));
				
			} catch (Exception e) {
				e.printStackTrace();
				// test.fail("Failed on error: "+e);
			}
		}

	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.sccp.SccpProvider#registerSccpListener(org.mobicents.protocols.ss7.sccp.parameter.SccpAddress, org.mobicents.protocols.ss7.sccp.SccpListener)
	 */
	@Override
	public void addSccpListener( SccpListener listener) {
		this.listener = listener;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.sccp.SccpProvider#deregisterSccpListener(org.mobicents.protocols.ss7.sccp.parameter.SccpAddress)
	 */
	@Override
	public void removeSccpListener(SccpListener localAddress) {
		this.listener = null;
		
	}
}
