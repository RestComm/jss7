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
 * Start time:09:11:07 2009-04-06<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.InvokingPivotReason;

/**
 * Start time:09:11:07 2009-04-06<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class InvokingPivotReasonImpl extends AbstractISUPParameter implements InvokingPivotReason {

	private byte[] reasons = null;

	public InvokingPivotReasonImpl(byte[] reasons) throws ParameterException {
		super();
		decode(reasons);
	}

	public InvokingPivotReasonImpl() {
		super();
		
	}

	public int decode(byte[] b) throws ParameterException {
		try {
			this.setReasons(b);
		} catch (Exception e) {
			throw new ParameterException(e);
		}
		return b.length;
	}

	public byte[] encode() throws ParameterException {
		for (int index = 0; index < this.reasons.length; index++) {
			this.reasons[index] = (byte) (this.reasons[index] & 0x7F);
		}

		this.reasons[this.reasons.length - 1] = (byte) ((this.reasons[this.reasons.length - 1]) | (0x01 << 7));
		return this.reasons;
	}

	public byte[] getReasons() {
		return reasons;
	}

	public void setReasons(byte[] reasons) throws IllegalArgumentException {
		if (reasons == null || reasons.length == 0) {
			throw new IllegalArgumentException("byte[] must not be null and length must be greater than 0");
		}
		this.reasons = reasons;
	}

	public int getReason(byte b) {
		return b & 0x7F;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
