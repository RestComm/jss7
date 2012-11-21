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

package org.mobicents.protocols.ss7.map.primitives;

import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class IMEIImpl extends TbcdString implements IMEI {

	public IMEIImpl() {
		// There are some fake mobiles that IMEI length != 15
		super(1, 8, "IMEI");
	}

	public IMEIImpl(String data) {
		// There are some fake mobiles that IMEI length != 15
		super(1, 8, "IMEI", data);
	}

	public String getIMEI() {
		return this.data;
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		if (this.data == null)
			throw new MAPException("Error while encoding the IMEI: IMEI must not be null");
				
		super.encodeData(asnOs);
	}
}
