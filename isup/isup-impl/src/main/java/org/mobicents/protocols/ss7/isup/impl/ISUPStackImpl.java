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

/**
 * Start time:12:14:57 2009-09-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ConfigurationException;
import org.mobicents.protocols.StartFailedException;
import org.mobicents.protocols.ss7.isup.ISUPMessageFactory;
import org.mobicents.protocols.ss7.isup.ISUPParameterFactory;
import org.mobicents.protocols.ss7.isup.ISUPProvider;
import org.mobicents.protocols.ss7.isup.ISUPStack;
import org.mobicents.protocols.ss7.isup.impl.message.AbstractISUPMessage;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.mtp.Mtp3;
import org.mobicents.protocols.ss7.mtp.Utils;
import org.mobicents.protocols.stream.api.SelectorKey;
import org.mobicents.ss7.linkset.oam.Layer4;
import org.mobicents.ss7.linkset.oam.Linkset;
import org.mobicents.ss7.linkset.oam.LinksetSelector;
import org.mobicents.ss7.linkset.oam.LinksetStream;

/**
 * Start time:12:14:57 2009-09-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class ISUPStackImpl implements ISUPStack, Layer4 {

	private Logger logger = Logger.getLogger(ISUPStackImpl.class);

	private static final int OP_READ_WRITE = 3;
	private static final int MAX_MESSAGES = 30; // max msgs in queue

	private State state = State.IDLE;
	//dont quite like the idea of so many threads... but.
	private ExecutorService executor;
	private ExecutorService layer3exec;

	// Hold LinkSet here. LinkSet's name as key and actual LinkSet as Object
	protected FastMap<String, Linkset> linksets = new FastMap<String, Linkset>();

	// Hold the byte[] that needs to be writtent to Linkset
	private FastMap<String, ConcurrentLinkedQueue<byte[]>> linksetQueue = new FastMap<String, ConcurrentLinkedQueue<byte[]>>();

	private LinksetSelector linkSetSelector = new LinksetSelector();

	private ISUPProviderImpl provider;
	// local vars
	private ISUPMessageFactory messageFactory;
	private ISUPParameterFactory parameterFactory;

	public ISUPStackImpl() {
		super();
	}

	public ISUPProvider getIsupProvider() {
		return provider;
	}

	public void start() throws IllegalStateException, StartFailedException {
		if (state != State.CONFIGURED) {
			throw new IllegalStateException("Stack has not been configured or is already running!");
		}
		if(state == State.RUNNING)
		{
			throw new StartFailedException("Can not start stack again!");
		}
		this.executor = Executors.newFixedThreadPool(1);
		this.layer3exec = Executors.newFixedThreadPool(1);
		this.provider.start();
		this.layer3exec.execute(new MtpStreamHandler());
		this.state = State.RUNNING;

	}

	public void stop() {
		if (state != State.RUNNING) {
			throw new IllegalStateException("Stack is not running!");
		}
		if(state == State.CONFIGURED)
		{
			throw new IllegalStateException("Can not stop stack again!");
		}
		this.executor.shutdown();
		this.layer3exec.shutdown();
		this.provider.stop();
		this.state = State.CONFIGURED;

	}

	// ///////////////
	// CONF METHOD //
	// ///////////////
	/**
     *
     */
	public void configure(Properties props) throws ConfigurationException {
		if (state != State.IDLE) {
			throw new IllegalStateException("Stack already been configured or is already running!");
		}

		this.provider = new ISUPProviderImpl(this,props);
		this.parameterFactory = this.provider.getParameterFactory();
		this.messageFactory = this.provider.getMessageFactory();
		this.state = State.CONFIGURED;
	}

	public void add(Linkset linkset) {

		try {
			linkset.getLinksetStream().register(this.linkSetSelector);
			linksets.put(linkset.getName(), linkset);
			linksetQueue.put(linkset.getName(), new ConcurrentLinkedQueue<byte[]>());
		} catch (IOException ex) {
			logger.error(String.format("Registration for %s LinksetStream failed", linkset.getName()), ex);
		}
	}

	public void remove(Linkset linkset) {
		//should be done by name?
		if(linksets.remove(linkset.getName()) !=null)
		{	
			//FIXME: add deregister
			linksetQueue.remove(linkset.getName());
		}
	}

	// ---------------- private methods and class defs

	/**
	 * @param message
	 */
	void send(byte[] message) {
		// here we have encoded msg, nothing more, need to add MTP3 label.
		if (linksets.size() == 0) {
			return;
		}
		Linkset linkset = this.linksets.values().iterator().next();
		int opc = linkset.getOpc();
		int dpc = linkset.getApc();
		int si = Mtp3._SI_SERVICE_ISUP;
		int ni = linkset.getNi();
		int sls = 0;
		int ssi = ni << 2;

		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		// encoding routing label
		bout.write((byte) (((ssi & 0x0F) << 4) | (si & 0x0F)));
		bout.write((byte) dpc);
		bout.write((byte) (((dpc >> 8) & 0x3F) | ((opc & 0x03) << 6)));
		bout.write((byte) (opc >> 2));
		bout.write((byte) (((opc >> 10) & 0x0F) | ((sls & 0x0F) << 4)));
	
		try {
			bout.write(message);
			byte[] msg = bout.toByteArray();
			ConcurrentLinkedQueue<byte[]> queue = this.linksetQueue.get(linkset.getName());
			queue.add(msg);
			if (queue.size() > MAX_MESSAGES) {
				queue.remove();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private class MtpStreamHandler implements Runnable {

		public void run() {
			byte[] rxBuffer = new byte[1000];
			byte[] txBuffer;
			// Execute only till state is Running
			while (state == State.RUNNING) {

				try {
					//FIXME
					FastList<SelectorKey> selected = linkSetSelector.selectNow(OP_READ_WRITE, 1);
					for (FastList.Node<SelectorKey> n =selected.head(), end = selected.tail(); (n = n
			                .getNext()) != end;) {
						LinksetStream stream = (LinksetStream) n.getValue().getStream();
						int read = stream.read(rxBuffer);

						
						// Read data
						if (read>0) {
							byte[] inByte = new byte[read];
							System.arraycopy(rxBuffer, 0, inByte, 0, read);
							MessageHandler handler = new MessageHandler(inByte);
							executor.execute(handler);
						}

						// write data
						txBuffer = linksetQueue.get(stream.getName()).poll();
						if (txBuffer != null) {
							stream.write(txBuffer);
						}

					}

					// TODO : should add any Thread.wait() or let it iterate
					// continuously?
				} catch (IOException ex) {
					logger.error("Error while reading data from LinksetStream", ex);
				}
			}
		}

	}

	private class MessageHandler implements Runnable {
		// MSU as input stream
		private byte[] msu;
		private ISUPMessage message;

		protected MessageHandler(byte[] msu) {

			this.msu = msu;

		}

		private ISUPMessage parse() throws IOException {
			try {
				// FIXME: change this, dont copy over and over.
				
				int commandCode = msu[7];//	1(SIO) +    3(RL) + 1(SLS) + 2(CIC) + 1(CODE)
												// http://pt.com/page/tutorials/ss7-tutorial/mtp
				byte[] payload = new byte[msu.length - 5];
				System.arraycopy(msu, 5, payload, 0, payload.length);
				// for post processing
				AbstractISUPMessage msg = (AbstractISUPMessage) messageFactory.createCommand(commandCode);
				msg.decode(payload, parameterFactory);

				return msg;

			} catch (Exception e) {
				// FIXME: what should we do here? send back?
				e.printStackTrace();
				logger.error("Failed on data: " + Utils.hexDump(null, msu));
			}
			return null;
		}

		public void run() {
			if (message == null) {
				try {
					message = parse();
				} catch (IOException e) {
					logger.warn("Corrupted message received");
					return;
				}
			}
			// deliver to provider, so it can check up on circuit, play with
			// timers and deliver.
			provider.receive(message);
		}
	}

	private enum State {
		IDLE, CONFIGURED, RUNNING;
	}

}
