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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

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

    private static final String SCREENING_AND_PRESENTATION_INDICATORS = "screeningAndPresentationIndicators";
    private static final String TYPE_OF_SHAPE = "typeOfShape";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String UNCERTAINTY = "uncertainty";
    private static final String CONFIDENCE = "confidence";

    private static final String DEFAULT_STRING_VALUE = null;
    private static final double DEFAULT_DOUBLE_VALUE = 0;
    private static final int DEFAULT_INT_VALUE = 0;

    public GeodeticInformationImpl() {
        super(10, 10, "GeodeticInformation");
    }

    public GeodeticInformationImpl(byte[] data) {
        super(10, 10, "GeodeticInformation", data);
    }

    public GeodeticInformationImpl(int screeningAndPresentationIndicators, TypeOfShape typeOfShape, double latitude,
            double longitude, double uncertainty, int confidence) throws MAPException {
        super(10, 10, "GeodeticInformation");
        this.setData(screeningAndPresentationIndicators, typeOfShape, latitude, longitude, uncertainty, confidence);
    }

    public void setData(int screeningAndPresentationIndicators, TypeOfShape typeOfShape, double latitude, double longitude,
            double uncertainty, int confidence) throws MAPException {

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

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<GeodeticInformationImpl> GEODETIC_INFORMATION_XML = new XMLFormat<GeodeticInformationImpl>(
            GeodeticInformationImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, GeodeticInformationImpl geodeticInformation)
                throws XMLStreamException {
            int sapi = xml.getAttribute(SCREENING_AND_PRESENTATION_INDICATORS, DEFAULT_INT_VALUE);

            String str = xml.getAttribute(TYPE_OF_SHAPE, DEFAULT_STRING_VALUE);
            TypeOfShape tos = null;
            if (str != null)
                tos = Enum.valueOf(TypeOfShape.class, str);

            double lat = xml.getAttribute(LATITUDE, DEFAULT_DOUBLE_VALUE);
            double lng = xml.getAttribute(LONGITUDE, DEFAULT_DOUBLE_VALUE);
            double unc = xml.getAttribute(UNCERTAINTY, DEFAULT_DOUBLE_VALUE);
            int conf = xml.getAttribute(CONFIDENCE, DEFAULT_INT_VALUE);

            try {
                geodeticInformation.setData(sapi, tos, lat, lng, unc, conf);
            } catch (MAPException e) {
                throw new XMLStreamException("MAPException when deserializing GeodeticInformationImpl", e);
            }
        }

        @Override
        public void write(GeodeticInformationImpl geodeticInformation, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            xml.setAttribute(SCREENING_AND_PRESENTATION_INDICATORS, geodeticInformation.getScreeningAndPresentationIndicators());
            if (geodeticInformation.getTypeOfShape() != null) {
                xml.setAttribute(TYPE_OF_SHAPE, geodeticInformation.getTypeOfShape().toString());
            }
            xml.setAttribute(LATITUDE, geodeticInformation.getLatitude());
            xml.setAttribute(LONGITUDE, geodeticInformation.getLongitude());
            xml.setAttribute(UNCERTAINTY, geodeticInformation.getUncertainty());
            xml.setAttribute(CONFIDENCE, geodeticInformation.getConfidence());
        }
    };
}
