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

package org.mobicents.protocols.ss7.map.service.lsm;

import org.mobicents.protocols.ss7.map.api.service.lsm.ExtGeographicalInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.TypeOfShape;
import org.mobicents.protocols.ss7.map.primitives.OctetStringBase;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.GeographicalInformationImpl;

/**
 *
 * @author sergey vetyutnev
 * 
 */
public class ExtGeographicalInformationImpl extends OctetStringBase implements ExtGeographicalInformation {

	public ExtGeographicalInformationImpl() {
		super(1, 20, "ExtGeographicalInformation");
	}

	public ExtGeographicalInformationImpl(byte[] data) {
		super(1, 20, "ExtGeographicalInformation", data);
	}

	public byte[] getData() {
		return data;
	}

	@Override
	public TypeOfShape getTypeOfShape() {
		if (this.data == null || this.data.length < 1)
			return null;

		return TypeOfShape.getInstance(this.data[1]);
	}

	@Override
	public double getLatitude() {
		if (this.data == null || this.data.length < 7)
			return 0;

		return GeographicalInformationImpl.decodeLatitude(this.data, 1);
	}

	@Override
	public double getLongitude() {
		if (this.data == null || this.data.length < 7)
			return 0;

		return GeographicalInformationImpl.decodeLongitude(this.data, 4);
	}

	@Override
	public double getUncertainty() {
		if (this.getTypeOfShape() != TypeOfShape.EllipsoidPointWithUncertaintyCircle || this.data == null || this.data.length != 8)
			return 0;

		return GeographicalInformationImpl.decodeUncertainty(this.data[7]);
	}

	@Override
	public double getUncertaintySemiMajorAxis() {
		switch (this.getTypeOfShape()) {
		case EllipsoidPointWithUncertaintyEllipse:
			if (this.data == null || this.data.length != 11)
				return 0;
			return GeographicalInformationImpl.decodeUncertainty(this.data[7]);

		case EllipsoidPointWithAltitudeAndUncertaintyEllipsoid:
			if (this.data == null || this.data.length != 14)
				return 0;
			return GeographicalInformationImpl.decodeUncertainty(this.data[9]);
		}

		return 0;
	}

	@Override
	public double getUncertaintySemiMinorAxis() {
		switch (this.getTypeOfShape()) {
		case EllipsoidPointWithUncertaintyEllipse:
			if (this.data == null || this.data.length != 11)
				return 0;
			return GeographicalInformationImpl.decodeUncertainty(this.data[8]);

		case EllipsoidPointWithAltitudeAndUncertaintyEllipsoid:
			if (this.data == null || this.data.length != 14)
				return 0;
			return GeographicalInformationImpl.decodeUncertainty(this.data[10]);
		}

		return 0;
	}

	@Override
	public double getAngleOfMajorAxis() {
		switch (this.getTypeOfShape()) {
		case EllipsoidPointWithUncertaintyEllipse:
			if (this.data == null || this.data.length != 11)
				return 0;
			return (data[9] & 0xFF) * 2;

		case EllipsoidPointWithAltitudeAndUncertaintyEllipsoid:
			if (this.data == null || this.data.length != 14)
				return 0;
			return (data[11] & 0xFF) * 2;
		}

		return 0;
	}

	@Override
	public int getConfidence() {
		switch (this.getTypeOfShape()) {
		case EllipsoidPointWithUncertaintyEllipse:
			if (this.data == null || this.data.length != 11)
				return 0;
			return this.data[10];

		case EllipsoidPointWithAltitudeAndUncertaintyEllipsoid:
			if (this.data == null || this.data.length != 14)
				return 0;
			return this.data[13];

		case EllipsoidArc:
			if (this.data == null || this.data.length != 13)
				return 0;
			return this.data[12];
		}

		return 0;
	}

	@Override
	public int getAltitude() {
		if (this.getTypeOfShape() != TypeOfShape.EllipsoidPointWithAltitudeAndUncertaintyEllipsoid || this.data == null || this.data.length != 14)
			return 0;

		int i1 = ((data[7] & 0xFF) << 8) + (data[8] & 0xFF);
		int sign = 1;
		if ((i1 & 0x8000) != 0) {
			sign = -1;
			i1 = i1 & 0x7FFF;
		}
		return i1 * sign;
	}

	@Override
	public double getUncertaintyAltitude() {
		if (this.getTypeOfShape() != TypeOfShape.EllipsoidPointWithAltitudeAndUncertaintyEllipsoid || this.data == null || this.data.length != 14)
			return 0;

		return GeographicalInformationImpl.decodeUncertainty(this.data[12]);
	}

	@Override
	public int getInnerRadius() {
		if (this.getTypeOfShape() != TypeOfShape.EllipsoidArc || this.data == null || this.data.length != 13)
			return 0;

		int i1 = ((data[7] & 0xFF) << 8) + (data[8] & 0xFF);
		return i1;
	}

	@Override
	public double getUncertaintyRadius() {
		if (this.getTypeOfShape() != TypeOfShape.EllipsoidArc || this.data == null || this.data.length != 13)
			return 0;

		return GeographicalInformationImpl.decodeUncertainty(this.data[9]);
	}

	@Override
	public double getOffsetAngle() {
		if (this.getTypeOfShape() != TypeOfShape.EllipsoidArc || this.data == null || this.data.length != 13)
			return 0;

		return (data[10] & 0xFF) * 2;
	}

	@Override
	public double getIncludedAngle() {
		if (this.getTypeOfShape() != TypeOfShape.EllipsoidArc || this.data == null || this.data.length != 13)
			return 0;

		return (data[11] & 0xFF) * 2;
	}	
}
