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
 * Start time:09:49:43 2009-04-06<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectStatus;

/**
 * Start time:09:49:43 2009-04-06<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 */
public class RedirectStatusImpl extends AbstractISUPParameter implements RedirectStatus{

	private byte[] status;

	public RedirectStatusImpl() {
		super();
		
	}

	public RedirectStatusImpl(byte[] b) throws ParameterException {
		super();
		decode(b);
	}

	public int decode(byte[] b) throws ParameterException {
		try{
			setStatus(b);
		}catch(Exception e)
		{
			throw new ParameterException(e);
		}
		return b.length;
	}

	public byte[] encode() throws ParameterException {

		for (int index = 0; index < this.status.length; index++) {
			this.status[index] = (byte) (this.status[index] & 0x03);
		}

		this.status[this.status.length - 1] = (byte) ((this.status[this.status.length - 1]) | (0x01 << 7));
		return this.status;
	}

	public byte[] getStatus() {
		return status;
	}

	public void setStatus(byte[] status) {
		if (status == null || status.length == 0) {
			throw new IllegalArgumentException("byte[] must not be null and length must be greater than 0");
		}
		this.status = status;
	}

	public int getStatusIndicator(byte b) {
		return b & 0x03;
	}
	public int getCode() {

		return _PARAMETER_CODE;
	}
}
