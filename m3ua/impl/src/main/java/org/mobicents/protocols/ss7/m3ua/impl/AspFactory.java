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

package org.mobicents.protocols.ss7.m3ua.impl;

import java.nio.ByteBuffer;

import javolution.util.FastList;
import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.sctp.Association;
import org.mobicents.protocols.sctp.AssociationListener;
import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.transfer.PayloadData;

/**
 * 
 * @author amit bhayani
 * 
 */
public abstract class AspFactory implements AssociationListener, XMLSerializable {

	private static final Logger logger = Logger.getLogger(AspFactory.class);

	private static final String NAME = "name";
	private static final String STARTED = "started";

	protected String name;

	protected boolean started = false;

	protected Association association = null;

	protected FastList<Asp> aspList = new FastList<Asp>();

	private ByteBuffer txBuffer = ByteBuffer.allocateDirect(8192);

	public AspFactory() {

		// clean transmission buffer
		txBuffer.clear();
		txBuffer.rewind();
		txBuffer.flip();
	}

	public AspFactory(String name) {
		this();
		this.name = name;
	}

	public abstract void start() throws Exception;

	public abstract void stop() throws Exception;

	public boolean getStatus() {
		return this.started;
	}

	public void setAssociation(Association association) {
		// Unset the listener to previous association
		if (this.association != null) {
			this.association.setAssociationListener(null);
		}
		this.association = association;

		// Set the listener for new association
		this.association.setAssociationListener(this);
	}

	public void unsetAssociation() throws Exception {
		if (this.association != null) {
			if (this.association.isStarted()) {
				throw new Exception(String.format("Association=%s is still started. Stop first",
						this.association.getName()));
			}
			this.association.setAssociationListener(null);
			this.association = null;
		}
	}

	public String getName() {
		return this.name;
	}

	public abstract void read(M3UAMessage message);

	public void write(M3UAMessage message) {

		synchronized (txBuffer) {
			try {
				txBuffer.clear();
				((M3UAMessageImpl) message).encode(txBuffer);
				txBuffer.flip();

				byte[] data = new byte[txBuffer.limit()];
				txBuffer.get(data);

				org.mobicents.protocols.sctp.PayloadData payloadData = null;

				switch (message.getMessageClass()) {
				case MessageClass.ASP_STATE_MAINTENANCE:
				case MessageClass.MANAGEMENT:
				case MessageClass.ROUTING_KEY_MANAGEMENT:
					payloadData = new org.mobicents.protocols.sctp.PayloadData(data.length, data, true, true, 3, 0);
					break;
				case MessageClass.TRANSFER_MESSAGES:
					PayloadData payload = (PayloadData) message;
					payloadData = new org.mobicents.protocols.sctp.PayloadData(data.length, data, true, false, 3,
							payload.getData().getSLS());
					break;
				default:
					payloadData = new org.mobicents.protocols.sctp.PayloadData(data.length, data, true, true, 3, 0);
					break;
				}

				this.association.send(payloadData);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public abstract Asp createAsp();

	public boolean destroyAsp(Asp asp) {
		asp.aspFactory = null;
		return this.aspList.remove(asp);
	}

	public FastList<Asp> getAspList() {
		return this.aspList;
	}

	/**
	 * XML Serialization/Deserialization
	 */
	protected static final XMLFormat<AspFactory> ASP_FACTORY_XML = new XMLFormat<AspFactory>(AspFactory.class) {

		@Override
		public void read(javolution.xml.XMLFormat.InputElement xml, AspFactory aspFactory) throws XMLStreamException {
			aspFactory.name = xml.getAttribute(NAME, "");
			aspFactory.started = xml.getAttribute(STARTED).toBoolean();
		}

		@Override
		public void write(AspFactory aspFactory, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
			xml.setAttribute(NAME, aspFactory.name);
			xml.setAttribute(STARTED, aspFactory.started);
		}
	};
}
