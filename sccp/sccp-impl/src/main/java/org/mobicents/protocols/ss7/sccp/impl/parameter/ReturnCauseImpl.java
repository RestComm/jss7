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
package org.mobicents.protocols.ss7.sccp.impl.parameter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.mobicents.protocols.ss7.sccp.parameter.ReturnCause;

/**
 * 
 * @author baranowb
 */
public class ReturnCauseImpl extends AbstractParameter implements ReturnCause {

	private int value;

	public ReturnCauseImpl() {
	}

	public ReturnCauseImpl(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void decode(InputStream in) throws IOException {
		if(in.read()!=1)
		{
			throw new IOException();
		}
	
		int b = in.read() & 0xFf;

		this.value = b;
	}

	public void encode(OutputStream out) throws IOException {
		byte b = (byte) (this.value);
		out.write(1);
		out.write(b);
	}

	public void decode(byte[] bb) throws IOException {
		int b = bb[0] & 0xff;

		this.value = b;
	}
	
	public byte[] encode() throws IOException {
		return new byte[]{(byte) this.value};
	}

}
