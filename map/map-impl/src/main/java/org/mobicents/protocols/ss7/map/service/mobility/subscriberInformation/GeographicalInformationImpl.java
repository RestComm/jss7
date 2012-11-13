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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GeographicalInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.TypeOfShape;
import org.mobicents.protocols.ss7.map.primitives.OctetStringBase;

/**
 * TODO add details as described in 3GPP TS 23.032 instead of using rawData
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class GeographicalInformationImpl extends OctetStringBase implements GeographicalInformation {

	public GeographicalInformationImpl() {
		super(8, 8, "GeographicalInformation");
	}

	public GeographicalInformationImpl(byte[] data) {
		super(8, 8, "GeographicalInformation", data);
	}

	public GeographicalInformationImpl(TypeOfShape typeOfShape, double latitude, double longitude) throws MAPException {
		super(8, 8, "GeographicalInformation");

		if (typeOfShape != TypeOfShape.EllipsoidPointWithUncertaintyCircle) {
			throw new MAPException("typeOfShape parameter for GeographicalInformation can be only \"ellipsoid point with uncertainty circle\"");
		}

		this.data = new byte[8];

		this.data[0] = (byte) typeOfShape.getCode();

		encodeLatitude(data, 1, latitude);
		encodeLongitude(data, 4, longitude);

		// .........................
	}

	public byte[] getData() {
		return data;
	}
	
	private static double koef23 = Math.pow(2.0, 23) / 90;
	private static double koef24 = Math.pow(2.0, 24) / 360;

	public static double decodeLatitude(byte[] data, int begin) {
		int i1 = ((data[begin] & 0xFF) << 16) + ((data[begin + 1] & 0xFF) << 8) + (data[begin + 1] & 0xFF);

		int sign = 1;
		if ((i1 & 0x800000) != 0) {
			sign = -1;
			i1 = i1 & 0x7FFFFF;
		}

		return koef23 / i1 * sign;
	}

	public static double decodeLongitude(byte[] data, int begin) {
		int i1 = ((data[begin] & 0xFF) << 16) + ((data[begin + 1] & 0xFF) << 8) + (data[begin + 1] & 0xFF);

		int sign = 1;
		if ((i1 & 0x800000) != 0) {
			sign = -1;
			i1 = i1 & 0x7FFFFF;
		}

		return koef24 / i1 * sign;
	}

	public static void encodeLatitude(byte[] data, int begin, double val) {
		boolean negativeSign = false;
		if (val < 0) {
			negativeSign = true;
			val = -val;
		}

		int res = (int)(koef23 * val);
		
		if (res > 0x7FFFFF)
			res = 0x7FFFFF;
		if (negativeSign)
			res |= 0x800000;

		data[begin] = (byte) ((res & 0xFF0000) >> 16);
		data[begin+1] = (byte) ((res & 0xFF00) >> 8);
		data[begin+2] = (byte) (res & 0xFF);
	}

	public static void encodeLongitude(byte[] data, int begin, double val) {
		boolean negativeSign = false;
		if (val < 0) {
			negativeSign = true;
			val = -val;
		}

		int res = (int)(koef24 * val);

		if (res > 0x7FFFFF)
			res = 0x7FFFFF;
		if (negativeSign)
			res |= 0x800000;

		data[begin] = (byte) ((res & 0xFF0000) >> 16);
		data[begin+1] = (byte) ((res & 0xFF00) >> 8);
		data[begin+2] = (byte) (res & 0xFF);
	}

	@Override
	public TypeOfShape getTypeOfShape() {
		if (this.data == null || this.data.length != 8)
			return null;

		return TypeOfShape.getInstance(this.data[0]);
	}

	@Override
	public double getLatitude() {
		if (this.data == null || this.data.length != 8)
			return 0;

		return decodeLatitude(this.data, 1);
	}

	@Override
	public double getLongitude() {
		if (this.data == null || this.data.length != 8)
			return 0;

		return decodeLongitude(this.data, 4);
	}	
}
