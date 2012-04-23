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
 * Start time:17:10:53 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitStateIndicator;

/**
 * Start time:17:10:53 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class CircuitStateIndicatorImpl extends AbstractISUPParameter implements CircuitStateIndicator {

	private byte[] circuitState = null;

	public CircuitStateIndicatorImpl(byte[] circuitState) throws ParameterException {
		super();
		this.decode(circuitState);
	}

	public CircuitStateIndicatorImpl() {
		super();
		
	}

	public int decode(byte[] b) throws ParameterException {
		try {
			setCircuitState(b);
		} catch (Exception e) {
			throw new ParameterException(e);
		}
		return b.length;
	}

	public byte[] encode() throws ParameterException {
		return this.circuitState;
	}

	public byte[] getCircuitState() {
		return circuitState;
	}

	public void setCircuitState(byte[] circuitState) throws IllegalArgumentException {
		if (circuitState == null || circuitState.length == 0) {
			throw new IllegalArgumentException("byte[] must nto be null and length must be greater than 0");
		}
		this.circuitState = circuitState;
	}

	public byte createCircuitState(int MBS, int CPS, int HBS) {
		int v = 0;
		// FIXME: Shoudl we here enforce proper coding? according to note or
		// move it to encode??
		if (HBS != _HBS_NO_BLOCKING) {
			// NOTE � If bits F E are not coded 0 0, bits D C must be coded 1 1.
			// - this means CPS == 3
			CPS = _CPS_IDLE;
		}

		v = MBS & 0x03;
		v |= (CPS & 0x03) << 2;
		v |= (HBS & 0x03) << 4;
		return (byte) v;
	}

	public int getCallProcessingState(byte b) {
		return (b >> 2) & 0x03;
	}

	public int getHardwareBlockingState(byte b) {
		return (b >> 4) & 0x03;
	}

	public int getMaintenanceBlockingState(byte b) {
		return b & 0x03;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}

}
