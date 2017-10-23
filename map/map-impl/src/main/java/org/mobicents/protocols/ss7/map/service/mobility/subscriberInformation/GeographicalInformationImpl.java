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

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GeographicalInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.TypeOfShape;
import org.mobicents.protocols.ss7.map.primitives.OctetStringBase;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class GeographicalInformationImpl extends OctetStringBase implements GeographicalInformation {

    private static double koef23 = Math.pow(2.0, 23) / 90;
    private static double koef24 = Math.pow(2.0, 24) / 360;
    private static double[] uncertaintyTable = initUncertaintyTable();

    private static final String TYPE_OF_SHAPE = "typeOfShape";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String UNCERTAINTY = "uncertainty";

    private static final String DEFAULT_STRING_VALUE = null;
    private static final double DEFAULT_DOUBLE_VALUE = 0;

    private static double[] initUncertaintyTable() {
        double[] res = new double[128];

        double c = 10;
        double x = 0.1;
        for (int i = 1; i < 128; i++) {
            res[i] = c * (Math.pow(1 + x, i) - 1);
        }

        return res;
    }

    public GeographicalInformationImpl() {
        super(8, 8, "GeographicalInformation");
    }

    public GeographicalInformationImpl(byte[] data) {
        super(8, 8, "GeographicalInformation", data);
    }

    public GeographicalInformationImpl(TypeOfShape typeOfShape, double latitude, double longitude, double uncertainty)
            throws MAPException {
        super(8, 8, "GeographicalInformation");
        setData(typeOfShape, latitude, longitude, uncertainty);
    }

    public void setData(TypeOfShape typeOfShape, double latitude, double longitude, double uncertainty) throws MAPException {
        if (typeOfShape != TypeOfShape.EllipsoidPointWithUncertaintyCircle) {
            throw new MAPException(
                    "typeOfShape parameter for GeographicalInformation can be only \"ellipsoid point with uncertainty circle\"");
        }

        this.data = new byte[8];

        this.data[0] = (byte) (typeOfShape.getCode() << 4);

        encodeLatitude(data, 1, latitude);
        encodeLongitude(data, 4, longitude);
        data[7] = (byte) encodeUncertainty(uncertainty);
    }

    public static double decodeLatitude(byte[] data, int begin) {
        int i1 = ((data[begin] & 0xFF) << 16) + ((data[begin + 1] & 0xFF) << 8) + (data[begin + 2] & 0xFF);

        int sign = 1;
        if ((i1 & 0x800000) != 0) {
            sign = -1;
            i1 = i1 & 0x7FFFFF;
        }

        return i1 / koef23 * sign;
    }

    public static double decodeLongitude(byte[] data, int begin) {
        int i1 = ((data[begin] & 0xFF) << 16) | ((data[begin + 1] & 0xFF) << 8) | (data[begin + 2] & 0xFF);

        if ((i1 & 0x800000) != 0) {
            i1 = i1 | ((int) 0xFF000000);
        }

        return i1 / koef24;
    }

    public static double decodeUncertainty(int data) {
        if (data < 0 || data > 127)
            data = 0;
        double d = uncertaintyTable[data];
        return d;
    }

    public static void encodeLatitude(byte[] data, int begin, double val) {
        boolean negativeSign = false;
        if (val < 0) {
            negativeSign = true;
            val = -val;
        }

        int res = (int) (koef23 * val);

        if (res > 0x7FFFFF)
            res = 0x7FFFFF;
        if (negativeSign)
            res |= 0x800000;

        data[begin] = (byte) ((res & 0xFF0000) >> 16);
        data[begin + 1] = (byte) ((res & 0xFF00) >> 8);
        data[begin + 2] = (byte) (res & 0xFF);
    }

    public static void encodeLongitude(byte[] data, int begin, double val) {
        int res = (int) (koef24 * val);

        if (res > 0x7FFFFF)
            res = 0x7FFFFF;

        if (val < 0)
            res |= 0x800000;

        data[begin] = (byte) ((res & 0xFF0000) >> 16);
        data[begin + 1] = (byte) ((res & 0xFF00) >> 8);
        data[begin + 2] = (byte) (res & 0xFF);
    }

    public static int encodeUncertainty(double val) {
        for (int i = 0; i < 127; i++) {
            if (val < uncertaintyTable[i + 1]) {
                return i;
            }
        }

        return 127;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public TypeOfShape getTypeOfShape() {
        if (this.data == null || this.data.length != 8)
            return null;

        return TypeOfShape.getInstance((this.data[0] & 0xFF) >> 4);
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

    @Override
    public double getUncertainty() {
        if (this.data == null || this.data.length != 8)
            return 0;

        return decodeUncertainty(this.data[7]);
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

        sb.append(", Uncertainty=");
        sb.append(this.getUncertainty());

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<GeographicalInformationImpl> GEOGRAPHICAL_INFORMATION_XML = new XMLFormat<GeographicalInformationImpl>(
            GeographicalInformationImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, GeographicalInformationImpl geographicalInformation)
                throws XMLStreamException {
            String str = xml.getAttribute(TYPE_OF_SHAPE, DEFAULT_STRING_VALUE);
            TypeOfShape tos = null;
            if (str != null)
                tos = Enum.valueOf(TypeOfShape.class, str);

            double lat = xml.getAttribute(LATITUDE, DEFAULT_DOUBLE_VALUE);
            double lng = xml.getAttribute(LONGITUDE, DEFAULT_DOUBLE_VALUE);
            double unc = xml.getAttribute(UNCERTAINTY, DEFAULT_DOUBLE_VALUE);

            try {
                geographicalInformation.setData(tos, lat, lng, unc);
            } catch (MAPException e) {
                throw new XMLStreamException("MAPException when deserializing GeographicalInformation", e);
            }
        }

        @Override
        public void write(GeographicalInformationImpl geographicalInformation, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            if (geographicalInformation.getTypeOfShape() != null) {
                xml.setAttribute(TYPE_OF_SHAPE, geographicalInformation.getTypeOfShape().toString());
            }
            xml.setAttribute(LATITUDE, geographicalInformation.getLatitude());
            xml.setAttribute(LONGITUDE, geographicalInformation.getLongitude());
            xml.setAttribute(UNCERTAINTY, geographicalInformation.getUncertainty());
        }
    };
}
