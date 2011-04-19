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
 * Start time:12:44:04 2009-04-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.UserToUserIndicators;

/**
 * Start time:12:44:04 2009-04-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class UserToUserIndicatorsImpl extends AbstractISUPParameter implements UserToUserIndicators {
	private static final int _TURN_ON = 1;
	private static final int _TURN_OFF = 0;

	private boolean response;
	private int serviceOne;
	private int serviceTwo;
	private int serviceThree;
	private boolean networkDiscardIndicator;

	public UserToUserIndicatorsImpl(byte[] b) throws ParameterException {
		super();
		decode(b);
	}

	public UserToUserIndicatorsImpl() {
		super();
		
	}

	public UserToUserIndicatorsImpl(boolean response, int serviceOne, int serviceTwo, int serviceThree, boolean networkDiscardIndicator) {
		super();
		this.response = response;
		this.serviceOne = serviceOne;
		this.serviceTwo = serviceTwo;
		this.serviceThree = serviceThree;
		this.networkDiscardIndicator = networkDiscardIndicator;
	}

	public int decode(byte[] b) throws ParameterException {
		if (b == null || b.length != 1) {
			throw new ParameterException("byte[] must  not be null and length must  be 1");
		}
		try {
			this.setResponse((b[0] & 0x01) == _TURN_ON);
			this.setServiceOne((b[0] >> 1));
			this.setServiceTwo((b[0] >> 3));
			this.setServiceThree((b[0] >> 5));
			this.setNetworkDiscardIndicator(((b[0] >> 7) & 0x01) == _TURN_ON);
		} catch (Exception e) {
			throw new ParameterException(e);
		}
		return 1;
	}

	public byte[] encode() throws ParameterException {
		int v = this.response ? _TURN_ON : _TURN_OFF;
		v |= (this.serviceOne & 0x03) << 1;
		v |= (this.serviceTwo & 0x03) << 3;
		v |= (this.serviceThree & 0x03) << 5;
		v |= (this.networkDiscardIndicator ? _TURN_ON : _TURN_OFF) << 7;
		return new byte[] { (byte) v };
	}

	public boolean isResponse() {
		return response;
	}

	public void setResponse(boolean response) {
		this.response = response;
	}

	public int getServiceOne() {
		return serviceOne;
	}

	public void setServiceOne(int serviceOne) {
		this.serviceOne = serviceOne & 0x03;
	}

	public int getServiceTwo() {
		return serviceTwo;
	}

	public void setServiceTwo(int serviceTwo) {
		this.serviceTwo = serviceTwo & 0x03;
	}

	public int getServiceThree() {
		return serviceThree;
	}

	public void setServiceThree(int serviceThree) {
		this.serviceThree = serviceThree & 0x03;
	}

	public boolean isNetworkDiscardIndicator() {
		return networkDiscardIndicator;
	}

	public void setNetworkDiscardIndicator(boolean networkDiscardIndicator) {
		this.networkDiscardIndicator = networkDiscardIndicator;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
