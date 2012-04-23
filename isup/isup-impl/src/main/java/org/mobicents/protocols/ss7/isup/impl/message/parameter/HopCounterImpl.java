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
 * Start time:14:58:14 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.HopCounter;

/**
 * Start time:14:58:14 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 */
public class HopCounterImpl extends AbstractISUPParameter implements HopCounter{


	private int hopCounter;

	public HopCounterImpl(byte[] b) throws ParameterException {
		super();
		decode(b);
	}

	public HopCounterImpl() {
		super();
		
	}

	public HopCounterImpl(int hopCounter) {
		super();
		this.hopCounter = hopCounter;
	}

	public int decode(byte[] b) throws ParameterException {
		if (b == null || b.length != 1) {
			throw new ParameterException("byte[] must not be null and length must be 1");
		}
		this.hopCounter = b[0] & 0x1F;
		return 1;
	}

	public byte[] encode() throws ParameterException {
		return new byte[] { (byte) (this.hopCounter & 0x1F) };
	}

	public int getHopCounter() {
		return hopCounter;
	}

	public void setHopCounter(int hopCounter) {
		this.hopCounter = hopCounter;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
