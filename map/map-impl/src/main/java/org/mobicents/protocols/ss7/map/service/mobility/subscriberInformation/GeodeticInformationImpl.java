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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GeodeticInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.TypeOfShape;
import org.mobicents.protocols.ss7.map.primitives.OctetStringBase;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class GeodeticInformationImpl extends OctetStringBase implements GeodeticInformation {

    public GeodeticInformationImpl() {
        super(10, 10, "GeodeticInformation");
    }

    public GeodeticInformationImpl(byte[] data) {
        super(10, 10, "GeodeticInformation", data);
    }

    public GeodeticInformationImpl(int screeningAndPresentationIndicators, TypeOfShape typeOfShape, double latitude,
            double longitude, double uncertainty, int confidence) throws MAPException {
        super(10, 10, "GeodeticInformation");

        if (typeOfShape != TypeOfShape.EllipsoidPointWithUncertaintyCircle) {
            throw new MAPException(
                    "typeOfShape parameter for GeographicalInformation can be only \" ellipsoid point with uncertainty circle\"");
        }

        this.data = new byte[10];

        this.data[0] = (byte) screeningAndPresentationIndicators;
        this.data[1] = (byte) (typeOfShape.getCode() << 4);

        GeographicalInformationImpl.encodeLatitude(data, 2, latitude);
        GeographicalInformationImpl.encodeLongitude(data, 5, longitude);
        data[8] = (byte) GeographicalInformationImpl.encodeUncertainty(uncertainty);
        data[9] = (byte) confidence;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public int getScreeningAndPresentationIndicators() {
        if (this.data == null || this.data.length != 10)
            return 0;

        return this.data[0];
    }

    @Override
    public TypeOfShape getTypeOfShape() {
        if (this.data == null || this.data.length != 10)
            return null;

        return TypeOfShape.getInstance((this.data[1] & 0xFF) >> 4);
    }

    @Override
    public double getLatitude() {
        if (this.data == null || this.data.length != 10)
            return 0;

        return GeographicalInformationImpl.decodeLatitude(this.data, 2);
    }

    @Override
    public double getLongitude() {
        if (this.data == null || this.data.length != 10)
            return 0;

        return GeographicalInformationImpl.decodeLongitude(this.data, 5);
    }

    @Override
    public double getUncertainty() {
        if (this.data == null || this.data.length != 10)
            return 0;

        return GeographicalInformationImpl.decodeUncertainty(this.data[8]);
    }

    @Override
    public int getConfidence() {
        if (this.data == null || this.data.length != 10)
            return 0;

        return this.data[9];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        sb.append("ScreeningAndPresentationIndicators=");
        sb.append(this.getScreeningAndPresentationIndicators());

        sb.append(", TypeOfShape=");
        sb.append(this.getTypeOfShape());

        sb.append(", Latitude=");
        sb.append(this.getLatitude());

        sb.append(", Longitude=");
        sb.append(this.getLongitude());

        sb.append(", Uncertainty=");
        sb.append(this.getUncertainty());

        sb.append(", Confidence=");
        sb.append(this.getConfidence());

        sb.append("]");

        return sb.toString();
    }
}
