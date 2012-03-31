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

package org.mobicents.protocols.ss7.sccp.impl;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

/**
 * @author amit bhayani
 * 
 */
public class RemoteSignalingPointCode implements XMLSerializable {
	private static final String REMOTE_SPC = "remoteSpc";
	private static final String REMOTE_SPC_FLAG = "remoteSpcFlag";
	private static final String MASK = "mask";

	private int remoteSpc;
	private int remoteSpcFlag;
	private int mask;
	private boolean remoteSpcProhibited;
	private boolean remoteSccpProhibited;
	
	public RemoteSignalingPointCode(){
		
	}

	/**
	 * 
	 */
	public RemoteSignalingPointCode(int remoteSpc, int remoteSpcFlag, int mask) {
		this.remoteSpc = remoteSpc;
		this.remoteSpcFlag = remoteSpcFlag;
		this.mask = mask;
	}

	public int getRemoteSpc() {
		return remoteSpc;
	}

	public int getRemoteSpcFlag() {
		return remoteSpcFlag;
	}

	public int getMask() {
		return mask;
	}

	public boolean isRemoteSpcProhibited() {
		return remoteSpcProhibited;
	}

	public boolean isRemoteSccpProhibited() {
		return remoteSccpProhibited;
	}

	public void setRemoteSpcProhibited(boolean remoteSpcProhibited) {
		this.remoteSpcProhibited = remoteSpcProhibited;
	}

	public void setRemoteSccpProhibited(boolean remoteSccpProhibited) {
		this.remoteSccpProhibited = remoteSccpProhibited;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("rsp=").append(this.remoteSpc).append(" rsp-flag=").append(this.remoteSpcFlag).append(" mask=").append(this.mask).append(" rsp-prohibited=")
				.append(this.remoteSpcProhibited).append(" rsccp-prohibited=").append(this.remoteSccpProhibited);
		return sb.toString();
	}

	protected static final XMLFormat<RemoteSignalingPointCode> XML = new XMLFormat<RemoteSignalingPointCode>(
			RemoteSignalingPointCode.class) {

		public void write(RemoteSignalingPointCode ai, OutputElement xml) throws XMLStreamException {
			xml.setAttribute(REMOTE_SPC, ai.remoteSpc);
			xml.setAttribute(REMOTE_SPC_FLAG, ai.remoteSpcFlag);
			xml.setAttribute(MASK, ai.mask);

		}

		public void read(InputElement xml, RemoteSignalingPointCode ai) throws XMLStreamException {
			ai.remoteSpc = xml.getAttribute(REMOTE_SPC).toInt();
			ai.remoteSpcFlag = xml.getAttribute(REMOTE_SPC_FLAG).toInt();
			ai.mask = xml.getAttribute(MASK).toInt();
		}
	};

}
