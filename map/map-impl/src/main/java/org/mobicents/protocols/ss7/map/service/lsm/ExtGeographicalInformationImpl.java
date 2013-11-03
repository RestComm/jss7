/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.map.service.lsm;

import org.mobicents.protocols.ss7.map.api.MAPException;
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

    protected ExtGeographicalInformationImpl(int minLength, int maxLength, String _PrimitiveName) {
        super(minLength, maxLength, _PrimitiveName);
    }

    public ExtGeographicalInformationImpl(byte[] data) {
        super(1, 20, "ExtGeographicalInformation", data);
    }

    protected ExtGeographicalInformationImpl(int minLength, int maxLength, String _PrimitiveName, byte[] data) {
        super(minLength, maxLength, _PrimitiveName, data);
    }

    public ExtGeographicalInformationImpl(TypeOfShape typeOfShape, double latitude, double longitude, double uncertainty,
            double uncertaintySemiMajorAxis, double uncertaintySemiMinorAxis, double angleOfMajorAxis, int confidence,
            int altitude, double uncertaintyAltitude, int innerRadius, double uncertaintyRadius, double offsetAngle,
            double includedAngle) throws MAPException {
        super(1, 20, "ExtGeographicalInformation");

        initData(typeOfShape, latitude, longitude, uncertainty, uncertaintySemiMajorAxis, uncertaintySemiMinorAxis,
                angleOfMajorAxis, confidence, altitude, uncertaintyAltitude, innerRadius, uncertaintyRadius, offsetAngle,
                includedAngle);
    }

    protected void initData(TypeOfShape typeOfShape, double latitude, double longitude, double uncertainty,
            double uncertaintySemiMajorAxis, double uncertaintySemiMinorAxis, double angleOfMajorAxis, int confidence,
            int altitude, double uncertaintyAltitude, int innerRadius, double uncertaintyRadius, double offsetAngle,
            double includedAngle) throws MAPException {

        if (typeOfShape == null) {
            throw new MAPException("typeOfShape parameter is null");
        }

        switch (typeOfShape) {
            case EllipsoidPointWithUncertaintyCircle:
                this.initData(8, typeOfShape, latitude, longitude);
                data[7] = (byte) GeographicalInformationImpl.encodeUncertainty(uncertainty);
                break;

            case EllipsoidPointWithUncertaintyEllipse:
                this.initData(11, typeOfShape, latitude, longitude);
                data[7] = (byte) GeographicalInformationImpl.encodeUncertainty(uncertaintySemiMajorAxis);
                data[8] = (byte) GeographicalInformationImpl.encodeUncertainty(uncertaintySemiMinorAxis);
                data[9] = (byte) (angleOfMajorAxis / 2);
                data[10] = (byte) confidence;
                break;

            case EllipsoidPointWithAltitudeAndUncertaintyEllipsoid:
                this.initData(14, typeOfShape, latitude, longitude);

                boolean negativeSign = false;
                if (altitude < 0) {
                    negativeSign = true;
                    altitude = -altitude;
                }
                if (altitude > 0x7FFF)
                    altitude = 0x7FFF;
                if (negativeSign)
                    altitude |= 0x8000;
                data[7] = (byte) ((altitude & 0xFF00) >> 8);
                data[8] = (byte) (altitude & 0xFF);

                data[9] = (byte) GeographicalInformationImpl.encodeUncertainty(uncertaintySemiMajorAxis);
                data[10] = (byte) GeographicalInformationImpl.encodeUncertainty(uncertaintySemiMinorAxis);
                data[11] = (byte) (angleOfMajorAxis / 2);
                data[12] = (byte) GeographicalInformationImpl.encodeUncertainty(uncertaintyAltitude);
                data[13] = (byte) confidence;
                break;

            case EllipsoidArc:
                this.initData(13, typeOfShape, latitude, longitude);

                if (innerRadius > 0x7FFF)
                    innerRadius = 0x7FFF;
                data[7] = (byte) ((innerRadius & 0xFF00) >> 8);
                data[8] = (byte) (innerRadius & 0xFF);
                data[9] = (byte) GeographicalInformationImpl.encodeUncertainty(uncertaintyRadius);
                data[10] = (byte) (offsetAngle / 2);
                data[11] = (byte) (includedAngle / 2);
                data[12] = (byte) confidence;

                break;

            case EllipsoidPoint:
                this.initData(7, typeOfShape, latitude, longitude);
                break;

            default:
                throw new MAPException("typeOfShape parameter has bad value");
        }
    }

    private void initData(int len, TypeOfShape typeOfShape, double latitude, double longitude) {
        this.data = new byte[len];
        this.data[0] = (byte) (typeOfShape.getCode() << 4);
        GeographicalInformationImpl.encodeLatitude(data, 1, latitude);
        GeographicalInformationImpl.encodeLongitude(data, 4, longitude);
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public TypeOfShape getTypeOfShape() {
        if (this.data == null || this.data.length < 1)
            return null;

        return TypeOfShape.getInstance((this.data[0] & 0xFF) >> 4);
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
        if (this.getTypeOfShape() != TypeOfShape.EllipsoidPointWithUncertaintyCircle || this.data == null
                || this.data.length != 8)
            return 0;

        return GeographicalInformationImpl.decodeUncertainty(this.data[7]);
    }

    @Override
    public double getUncertaintySemiMajorAxis() {
        TypeOfShape typeOfShape = this.getTypeOfShape();
        if (typeOfShape != null) {
            switch (typeOfShape) {
                case EllipsoidPointWithUncertaintyEllipse:
                    if (this.data == null || this.data.length != 11)
                        return 0;
                    return GeographicalInformationImpl.decodeUncertainty(this.data[7]);

                case EllipsoidPointWithAltitudeAndUncertaintyEllipsoid:
                    if (this.data == null || this.data.length != 14)
                        return 0;
                    return GeographicalInformationImpl.decodeUncertainty(this.data[9]);
            }
        }

        return 0;
    }

    @Override
    public double getUncertaintySemiMinorAxis() {
        TypeOfShape typeOfShape = this.getTypeOfShape();
        if (typeOfShape != null) {
            switch (typeOfShape) {
                case EllipsoidPointWithUncertaintyEllipse:
                    if (this.data == null || this.data.length != 11)
                        return 0;
                    return GeographicalInformationImpl.decodeUncertainty(this.data[8]);

                case EllipsoidPointWithAltitudeAndUncertaintyEllipsoid:
                    if (this.data == null || this.data.length != 14)
                        return 0;
                    return GeographicalInformationImpl.decodeUncertainty(this.data[10]);
            }
        }

        return 0;
    }

    @Override
    public double getAngleOfMajorAxis() {
        TypeOfShape typeOfShape = this.getTypeOfShape();
        if (typeOfShape != null) {
            switch (typeOfShape) {
                case EllipsoidPointWithUncertaintyEllipse:
                    if (this.data == null || this.data.length != 11)
                        return 0;
                    return (data[9] & 0xFF) * 2;

                case EllipsoidPointWithAltitudeAndUncertaintyEllipsoid:
                    if (this.data == null || this.data.length != 14)
                        return 0;
                    return (data[11] & 0xFF) * 2;
            }
        }

        return 0;
    }

    @Override
    public int getConfidence() {
        TypeOfShape typeOfShape = this.getTypeOfShape();
        if (typeOfShape != null) {
            switch (typeOfShape) {
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
        }

        return 0;
    }

    @Override
    public int getAltitude() {
        if (this.getTypeOfShape() != TypeOfShape.EllipsoidPointWithAltitudeAndUncertaintyEllipsoid || this.data == null
                || this.data.length != 14)
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
        if (this.getTypeOfShape() != TypeOfShape.EllipsoidPointWithAltitudeAndUncertaintyEllipsoid || this.data == null
                || this.data.length != 14)
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        sb.append("TypeOfShape=");
        sb.append(this.getTypeOfShape());

        sb.append(", Latitude=");
        sb.append(this.getLatitude());

        sb.append(", Longitude=");
        sb.append(this.getLongitude());

        if (this.getTypeOfShape() == TypeOfShape.EllipsoidPointWithUncertaintyCircle) {
            sb.append(", Uncertainty=");
            sb.append(this.getUncertainty());
        }

        if (this.getTypeOfShape() == TypeOfShape.EllipsoidPointWithUncertaintyEllipse
                || this.getTypeOfShape() == TypeOfShape.EllipsoidPointWithAltitudeAndUncertaintyEllipsoid) {
            sb.append(", UncertaintySemiMajorAxis=");
            sb.append(this.getUncertaintySemiMajorAxis());

            sb.append(", UncertaintySemiMinorAxis=");
            sb.append(this.getUncertaintySemiMinorAxis());

            sb.append(", AngleOfMajorAxis=");
            sb.append(this.getAngleOfMajorAxis());
        }

        if (this.getTypeOfShape() == TypeOfShape.EllipsoidPointWithAltitudeAndUncertaintyEllipsoid) {
            sb.append(", Altitude=");
            sb.append(this.getAltitude());

            sb.append(", UncertaintyAltitude=");
            sb.append(this.getUncertaintyAltitude());
        }

        if (this.getTypeOfShape() == TypeOfShape.EllipsoidArc) {
            sb.append(", InnerRadius=");
            sb.append(this.getInnerRadius());

            sb.append(", UncertaintyRadius=");
            sb.append(this.getUncertaintyRadius());

            sb.append(", OffsetAngle=");
            sb.append(this.getOffsetAngle());

            sb.append(", IncludedAngle=");
            sb.append(this.getIncludedAngle());
        }

        if (this.getTypeOfShape() == TypeOfShape.EllipsoidPointWithUncertaintyEllipse
                || this.getTypeOfShape() == TypeOfShape.EllipsoidPointWithAltitudeAndUncertaintyEllipsoid
                || this.getTypeOfShape() == TypeOfShape.EllipsoidArc) {
            sb.append(", Confidence=");
            sb.append(this.getConfidence());
        }

        sb.append("]");

        return sb.toString();
    }
}
