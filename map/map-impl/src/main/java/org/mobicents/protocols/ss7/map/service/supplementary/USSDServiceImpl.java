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

package org.mobicents.protocols.ss7.map.service.supplementary;

import org.mobicents.protocols.ss7.map.MAPMessageImpl;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDService;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;

/**
 * @author amit bhayani
 * 
 */
public abstract class USSDServiceImpl extends MAPMessageImpl implements
		USSDService {
	private byte ussdDataCodingSch;

	private USSDString ussdString;

	public USSDServiceImpl(byte ussdDataCodingSch, USSDString ussdString) {
		this.ussdDataCodingSch = ussdDataCodingSch;
		this.ussdString = ussdString;
	}

	public byte getUSSDDataCodingScheme() {
		return ussdDataCodingSch;
	}

	public USSDString getUSSDString() {
		return this.ussdString;
	}

	public void setUSSDDataCodingScheme(byte ussdDataCodingSch) {
		this.ussdDataCodingSch = ussdDataCodingSch;
	}

	public void setUSSDString(USSDString ussdString) {
		this.ussdString = ussdString;
	}
	

}
