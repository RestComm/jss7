/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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
package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBHPLMNData;
import org.mobicents.protocols.ss7.map.primitives.BitStringBase;


/**
 * 
 * @author Lasith Waruna Perera
 * 
 */
public class ODBHPLMNDataImpl  extends BitStringBase  implements ODBHPLMNData{

	private static final int _INDEX_plmnSpecificBarringType1 = 0;
	private static final int _INDEX_plmnSpecificBarringType2 = 1;
	private static final int _INDEX_plmnSpecificBarringType3 = 2;
	private static final int _INDEX_plmnSpecificBarringType4 = 3;
	
	public ODBHPLMNDataImpl() {
		super(4, 32, 4, "ODBHPLMNData");
	}

	public ODBHPLMNDataImpl( boolean plmnSpecificBarringType1,
			boolean plmnSpecificBarringType2, boolean plmnSpecificBarringType3,
			boolean plmnSpecificBarringType4) {
		super(4, 32, 4, "ODBHPLMNData");
		if (plmnSpecificBarringType1)
			this.bitString.set(_INDEX_plmnSpecificBarringType1);
		if (plmnSpecificBarringType2)
			this.bitString.set(_INDEX_plmnSpecificBarringType2);
		if (plmnSpecificBarringType3)
			this.bitString.set(_INDEX_plmnSpecificBarringType3);
		if (plmnSpecificBarringType4)
			this.bitString.set(_INDEX_plmnSpecificBarringType4);

	}

	@Override
	public boolean getPlmnSpecificBarringType1() {
		return this.bitString.get(_INDEX_plmnSpecificBarringType1);
	}

	@Override
	public boolean getPlmnSpecificBarringType2() {
		return this.bitString.get(_INDEX_plmnSpecificBarringType2);
	}

	@Override
	public boolean getPlmnSpecificBarringType3() {
		return this.bitString.get(_INDEX_plmnSpecificBarringType3);
	}

	@Override
	public boolean getPlmnSpecificBarringType4() {
		return this.bitString.get(_INDEX_plmnSpecificBarringType4);
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ODBHPLMNData [");

		if (getPlmnSpecificBarringType1())
			sb.append("plmnSpecificBarringType1Supported, ");
		if (getPlmnSpecificBarringType2())
			sb.append("plmnSpecificBarringType2Supported, ");
		if (getPlmnSpecificBarringType3())
			sb.append("plmnSpecificBarringType3Supported, ");
		if (getPlmnSpecificBarringType4())
			sb.append("plmnSpecificBarringType4Supported, ");

		sb.append("]");

		return sb.toString();
	}

}
