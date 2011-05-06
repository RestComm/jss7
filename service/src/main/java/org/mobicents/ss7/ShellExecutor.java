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

package org.mobicents.ss7;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;

import javolution.util.FastSet;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.oam.M3UAShellExecutor;
import org.mobicents.protocols.ss7.sccp.impl.oam.SccpExecutor;
import org.mobicents.ss7.linkset.oam.LinksetExecutor;
import org.mobicents.ss7.management.console.Subject;
import org.mobicents.ss7.management.transceiver.ChannelProvider;
import org.mobicents.ss7.management.transceiver.ChannelSelectionKey;
import org.mobicents.ss7.management.transceiver.ChannelSelector;
import org.mobicents.ss7.management.transceiver.Message;
import org.mobicents.ss7.management.transceiver.MessageFactory;
import org.mobicents.ss7.management.transceiver.ShellChannel;
import org.mobicents.ss7.management.transceiver.ShellServerChannel;

/**
 * @author amit bhayani
 * @author kulikov
 */
public class ShellExecutor implements Runnable {

	Logger logger = Logger.getLogger(ShellExecutor.class);

	private ChannelProvider provider;
	private ShellServerChannel serverChannel;
	private ShellChannel channel;
	private ChannelSelector selector;
	private ChannelSelectionKey skey;

	private MessageFactory messageFactory = null;

	private String rxMessage = "";
	private String txMessage = "";

	private volatile boolean started = false;

	private String address;

	private int port;

	private volatile LinksetExecutor linksetExecutor = null;

	private volatile M3UAShellExecutor m3UAShellExecutor = null;

	private volatile SccpExecutor sccpExecutor = null;

	public ShellExecutor() throws IOException {

	}

	public SccpExecutor getSccpExecutor() {
		return sccpExecutor;
	}

	public void setSccpExecutor(SccpExecutor sccpExecutor) {
		this.sccpExecutor = sccpExecutor;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void start() throws IOException {
		logger.info("Starting SS7 management shell environment");
		provider = ChannelProvider.provider();
		serverChannel = provider.openServerChannel();
		InetSocketAddress inetSocketAddress = new InetSocketAddress(address, port);
		serverChannel.bind(inetSocketAddress);

		selector = provider.openSelector();
		skey = serverChannel.register(selector, SelectionKey.OP_ACCEPT);

		messageFactory = ChannelProvider.provider().getMessageFactory();

		this.logger.info(String.format("ShellExecutor listening at %s", inetSocketAddress));

		this.started = true;
		new Thread(this).start();
	}

	public void stop() {
		this.started = false;
	}

	public LinksetExecutor getLinksetExecutor() {
		return linksetExecutor;
	}

	public void setLinksetExecutor(LinksetExecutor linksetExecutor) {
		this.linksetExecutor = linksetExecutor;
	}

	public M3UAShellExecutor getM3UAShellExecutor() {
		return m3UAShellExecutor;
	}

	public void setM3UAShellExecutor(M3UAShellExecutor shellExecutor) {
		m3UAShellExecutor = shellExecutor;
	}

	public void run() {

		while (started) {
			try {
				FastSet<ChannelSelectionKey> keys = selector.selectNow();

				for (FastSet.Record record = keys.head(), end = keys.tail(); (record = record.getNext()) != end;) {
					ChannelSelectionKey key = (ChannelSelectionKey) keys.valueOf(record);

					if (key.isAcceptable()) {
						accept();
					} else if (key.isReadable()) {
						ShellChannel chan = (ShellChannel) key.channel();
						Message msg = (Message) chan.receive();

						if (msg != null) {
							rxMessage = msg.toString();
							System.out.println("received " + rxMessage);
							if (rxMessage.compareTo("disconnect") == 0) {
								this.txMessage = "Bye";
								chan.send(messageFactory.createMessage(txMessage));

							} else {
								String[] options = rxMessage.split(" ");
								Subject subject = Subject.getSubject(options[0]);
								if (subject == null) {
									chan.send(messageFactory.createMessage("Invalid Subject"));
								} else {
									// Nullify examined options
									options[0] = null;

									switch (subject) {
									case LINKSET:
										this.txMessage = this.linksetExecutor.execute(options);
										chan.send(messageFactory.createMessage(this.txMessage));
										break;
									case M3UA:
										this.txMessage = this.m3UAShellExecutor.execute(options);
										chan.send(messageFactory.createMessage(this.txMessage));
										break;
									case SCCP:
										this.txMessage = this.sccpExecutor.execute(options);
										chan.send(messageFactory.createMessage(this.txMessage));
										break;
									}
								}
							} // if (rxMessage.compareTo("disconnect")
						} // if (msg != null)

						// TODO Handle message

						rxMessage = "";

					} else if (key.isWritable() && txMessage.length() > 0) {

						if (this.txMessage.compareTo("Bye") == 0) {
							this.closeChannel();
						}

						// else {
						//
						// ShellChannel chan = (ShellChannel) key.channel();
						// System.out.println("Sending " + txMessage);
						// chan.send(messageFactory.createMessage(txMessage));
						// }

						this.txMessage = "";
					}
				}
			} catch (IOException e) {
				logger.error("Error while operating on ChannelSelectionKey", e);
			}

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}

		try {
			skey.cancel();
			if (channel != null) {
				channel.close();
			}
			serverChannel.close();
			selector.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.logger.info("Stopped ShellExecutor service");
	}

	private void accept() throws IOException {
		channel = serverChannel.accept();
		skey.cancel();
		skey = channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
	}

	private void closeChannel() throws IOException {
		if (channel != null) {
			try {
				this.channel.close();
			} catch (IOException e) {
				logger.error("Error closing channel", e);
			}
		}
		skey.cancel();
		skey = serverChannel.register(selector, SelectionKey.OP_ACCEPT);
	}

}