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
 * 
 * @author sergey vetyutnev
 * 
 */
public class ConcernedSignalingPointCode implements XMLSerializable {
	private static final String REMOTE_SPC = "remoteSpc";

	private int remoteSpc;

	public ConcernedSignalingPointCode() {
	}

	public ConcernedSignalingPointCode(int remoteSpc) {
		this.remoteSpc = remoteSpc;
	}

	public int getRemoteSpc() {
		return remoteSpc;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("rsp=").append(this.remoteSpc);
		return sb.toString();
	}

	protected static final XMLFormat<ConcernedSignalingPointCode> XML = new XMLFormat<ConcernedSignalingPointCode>(
			ConcernedSignalingPointCode.class) {

		public void write(ConcernedSignalingPointCode ai, OutputElement xml) throws XMLStreamException {
			xml.setAttribute(REMOTE_SPC, ai.remoteSpc);

		}

		public void read(InputElement xml, ConcernedSignalingPointCode ai) throws XMLStreamException {
			ai.remoteSpc = xml.getAttribute(REMOTE_SPC).toInt();
		}
	};
}

