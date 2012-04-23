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

package org.mobicents.protocols.ss7.sccp.impl.parameter;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;

/**
 * 
 * @author Oleg Kulikov
 */
public class ProtocolClassImpl extends AbstractParameter implements ProtocolClass {

	private int pClass;
	private int msgHandling;

	/** Creates a new instance of UnitDataMandatotyFixedPart */
	public ProtocolClassImpl() {
	}

	public ProtocolClassImpl(int pClass, boolean returnMessageOnError) {
		this.pClass = pClass;
		if (pClass == 0 || pClass == 1)
			this.msgHandling = (returnMessageOnError ? HANDLING_RET_ERR : 0);
		else
			this.msgHandling = 0;
	}

	@Override
	public int getProtocolClass() {
		return this.pClass;
	}

	@Override
	public boolean getReturnMessageOnError() {
		return (this.msgHandling & HANDLING_RET_ERR) != 0 ? true : false;
	}

	public void decode(InputStream in) throws IOException {
		if (in.read() != 1) {
			throw new IOException();
		}

		int b = in.read() & 0xff;

		pClass = b & 0x0f;
		msgHandling = (b & 0xf0) >> 4;
	}

	public void encode(OutputStream out) throws IOException {
		byte b = (byte) (pClass | (msgHandling << 4));
		out.write(1);
		out.write(b);
	}

	public void decode(byte[] bb) throws IOException {
		int b = bb[0] & 0xff;

		pClass = b & 0x0f;
		msgHandling = (b & 0xf0) >> 4;
	}
	
	public byte[] encode() throws IOException {
		return new byte[]{(byte) (pClass | (msgHandling << 4))};
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + msgHandling;
		result = prime * result + pClass;
		return result;
	}

	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (getClass() != obj.getClass())
			return false;
		ProtocolClassImpl other = (ProtocolClassImpl) obj;
		if (msgHandling != other.msgHandling)
			return false;
		if (pClass != other.pClass)
			return false;
		return true;
	}

	
	public String toString() {
		return "ProtocolClass [msgHandling=" + msgHandling + ", pClass=" + pClass + "]";
	}
}

