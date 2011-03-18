/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
package org.mobicents.protocols.ss7.m3ua.impl;

import java.util.concurrent.ConcurrentLinkedQueue;

import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.M3UAChannel;
import org.mobicents.protocols.ss7.m3ua.M3UAProvider;
import org.mobicents.protocols.ss7.m3ua.M3UASelectionKey;
import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;

import com.sun.nio.sctp.AbstractNotificationHandler;
import com.sun.nio.sctp.AssociationChangeNotification;
import com.sun.nio.sctp.HandlerResult;
import com.sun.nio.sctp.ShutdownNotification;

/**
 * 
 * @author amit bhayani
 * 
 */
public abstract class AspFactory implements CommunicationListener {

	private static final Logger logger = Logger.getLogger(AspFactory.class);

	private M3UASelectionKey key;
	protected M3UAChannel channel;
	protected String name;
	protected String ip;
	protected int port;
	protected M3UAProvider m3UAProvider;

	protected boolean started = false;

	// Queue for outgoing messages. Message sent to peer
	protected ConcurrentLinkedQueue<M3UAMessage> txQueue = new ConcurrentLinkedQueue<M3UAMessage>();

	protected FastList<Asp> aspList = new FastList<Asp>();
	
	protected AssociationHandler associationHandler = new AssociationHandler();

	public AspFactory(String name, String ip, int port, M3UAProvider m3UAProvider) {
		this.name = name;
		this.ip = ip;
		this.port = port;

		this.m3UAProvider = m3UAProvider;
	}

	public abstract void start();

	public abstract void stop();

	public boolean getStatus() {
		return this.started;
	}

	public String getName() {
		return this.name;
	}

	public String getIp() {
		return this.ip;
	}

	public int getPort() {
		return this.port;
	}

	public void setChannel(M3UAChannel channel) {
		this.channel = channel;
	}

	public abstract void read(M3UAMessage message);

	public void write(M3UAMessage message) {
		// TODO : Instead of one more queue write directly to channel
		this.txQueue.add(message);
	}

	public abstract Asp createAsp();

	public FastList<Asp> getAspList() {
		return this.aspList;
	}

	public M3UAMessage txPoll() {
		return txQueue.poll();
	}

	public AssociationHandler getAssociationHandler() {
		return associationHandler;
	}

	class AssociationHandler extends AbstractNotificationHandler<Object> {
		@Override
		public HandlerResult handleNotification(AssociationChangeNotification not, Object obj) {
			switch (not.event()) {
			case COMM_UP:
				if (logger.isInfoEnabled()) {
					int outbound = not.association().maxOutboundStreams();
					int inbound = not.association().maxInboundStreams();
					logger.info(String.format(
							"AspFactory=%s New association setup with %d outbound streams, and %d inbound streams.\n",
							name, outbound, inbound));
				}
				break;
			}
			return HandlerResult.CONTINUE;
		}

		@Override
		public HandlerResult handleNotification(ShutdownNotification not, Object obj) {
			if (logger.isInfoEnabled()) {
				logger.info(String.format("AspFactory=%s Association SHUTDOWN", name));
			}
			
			onCommStateChange(CommunicationState.SHUTDOWN);
			
			return HandlerResult.RETURN;
		}
	}
}
