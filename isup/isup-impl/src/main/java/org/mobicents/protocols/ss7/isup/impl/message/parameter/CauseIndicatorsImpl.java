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
 * Start time:15:14:32 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.CauseIndicators;

/**
 * Start time:15:14:32 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class CauseIndicatorsImpl extends AbstractISUPParameter implements CauseIndicators {

	// FIXME: we ignore EXT fields , is this ok ?
	
	private int location = 0;
	private int causeValue = 0;
	private int codingStandard = 0;
	private byte[] diagnostics = null;

	public CauseIndicatorsImpl() {
		super();
		
	}

	public CauseIndicatorsImpl(int codingStandard, int location, int causeValue, byte[] diagnostics) {
		super();
		this.setCodingStandard(codingStandard);
		this.setLocation(location);
		this.setCauseValue(causeValue);
		this.diagnostics = diagnostics;
	}

	public int decode(byte[] b) throws ParameterException {

		// FIXME: there are ext bits, does this mean this param can be from 1 to
		// 3+ bytes?
		// but trace shows that extension bit is always on... does this mean
		// that we can have mutliptle indicators?
		if (b == null || b.length < 2) {
			throw new ParameterException("byte[] must not be null or has size less than 2");
		}
		// Used because of Q.850 - we must ignore recomendation
		int index = 0;
		// first two bytes are mandatory
		int v = 0;
		// remove ext
		v = b[index] & 0x7F;
		this.location = v & 0x0F;
		this.codingStandard = v >> 5;
		if (((b[index] & 0x7F) >> 7) == 0) {
			index += 2;
		} else {
			index++;
		}
		v = 0;
		v = b[1] & 0x7F;
		this.causeValue = v;
		if (b.length == 2) {
			return 2;
		} else {
			if ((b.length - 2) % 3 != 0) {
				throw new ParameterException("Diagnostics part  must have 3xN bytes, it has: " + (b.length - 2));
			}

			int byteCounter = 2;

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			for (int i = 2; i < b.length; i++) {
				bos.write(b[i]);
				byteCounter++;
			}

			this.diagnostics = bos.toByteArray();

			return byteCounter;
		}
	}

	public byte[] encode() throws ParameterException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		int v = this.location & 0x0F;
		v |= (byte) ((this.codingStandard & 0x03) << 5) | (0x01 << 7);
		bos.write(v);
		bos.write(this.causeValue | (0x01 << 7));
		if (this.diagnostics != null){
			try {
				bos.write(this.diagnostics);
			} catch (IOException e) {
				throw new ParameterException(e);
			}
		}
		byte[] b = bos.toByteArray();

		return b;
	}

	public int encode(ByteArrayOutputStream bos) throws ParameterException {
		byte[] b = this.encode();
		try {
			bos.write(b);
		} catch (IOException e) {
			throw new ParameterException(e);
		}
		return b.length;
	}

	public int getCodingStandard() {
		return codingStandard;
	}

	public void setCodingStandard(int codingStandard) {
		this.codingStandard = codingStandard & 0x03;
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location & 0x0F;
	}

	public int getCauseValue() {
		return causeValue & 0x7F;
	}

	public void setCauseValue(int causeValue) {
		this.causeValue = causeValue;
	}

	public byte[] getDiagnostics() {
		return diagnostics;
	}

	public void setDiagnostics(byte[] diagnostics) {
		this.diagnostics = diagnostics;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
