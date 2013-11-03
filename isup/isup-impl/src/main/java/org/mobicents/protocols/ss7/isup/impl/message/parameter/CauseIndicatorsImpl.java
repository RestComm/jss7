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

package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.CauseIndicators;

/**
 * Start time:15:14:32 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author sergey vetyutnev
 */
public class CauseIndicatorsImpl extends AbstractISUPParameter implements CauseIndicators {

    private static final String LOCATION = "location";
    private static final String CAUSE_VALUE = "causeValue";
    private static final String CODING_STANDARD = "codingStandard";
    private static final String RECOMMENDATION = "recommendation";
    private static final String DIAGNOSTICS = "diagnostics";

    private static final int DEFAULT_VALUE = 0;

    private int location = 0;
    private int causeValue = 0;
    private int codingStandard = 0;
    private int recommendation = 0;
    private byte[] diagnostics = null;

    public CauseIndicatorsImpl() {
        super();

    }

    public CauseIndicatorsImpl(int codingStandard, int location, int recommendation, int causeValue, byte[] diagnostics) {
        super();
        this.setCodingStandard(codingStandard);
        this.setLocation(location);
        this.setRecommendation(recommendation);
        this.setCauseValue(causeValue);
        this.diagnostics = diagnostics;
    }

    public int decode(byte[] b) throws ParameterException {

        // NOTE: there are ext bits but we do not care about them
        // FIXME: "Recommendation" optional field must be encoded/decoded when codingStandard!=_CODING_STANDARD_ITUT

        if (b == null || b.length < 2) {
            throw new ParameterException("byte[] must not be null or has size less than 2");
        }
        // Used because of Q.850 - we must ignore recomendation
        int index = 0;
        // first two bytes are mandatory
        int v = 0;
        // remove ext
        v = b[index] & 0x7F;
        this.location = v & 0x0F;
        this.codingStandard = v >> 5;
        if (((b[index] & 0x7F) >> 7) == 0) {
            index += 2;
        } else {
            index++;
        }
        v = 0;
        v = b[1] & 0x7F;
        this.causeValue = v;
        if (b.length == 2) {
            return 2;
        } else {
            if ((b.length - 2) % 3 != 0) {
                throw new ParameterException("Diagnostics part  must have 3xN bytes, it has: " + (b.length - 2));
            }

            int byteCounter = 2;

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            for (int i = 2; i < b.length; i++) {
                bos.write(b[i]);
                byteCounter++;
            }

            this.diagnostics = bos.toByteArray();

            return byteCounter;
        }
    }

    public byte[] encode() throws ParameterException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        int v = this.location & 0x0F;
        v |= (byte) ((this.codingStandard & 0x03) << 5) | (0x01 << 7);
        bos.write(v);
        bos.write(this.causeValue | (0x01 << 7));
        if (this.diagnostics != null) {
            try {
                bos.write(this.diagnostics);
            } catch (IOException e) {
                throw new ParameterException(e);
            }
        }
        byte[] b = bos.toByteArray();

        return b;
    }

    public int encode(ByteArrayOutputStream bos) throws ParameterException {
        byte[] b = this.encode();
        try {
            bos.write(b);
        } catch (IOException e) {
            throw new ParameterException(e);
        }
        return b.length;
    }

    public int getCodingStandard() {
        return codingStandard;
    }

    public void setCodingStandard(int codingStandard) {
        this.codingStandard = codingStandard & 0x03;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location & 0x0F;
    }

    public int getCauseValue() {
        return causeValue & 0x7F;
    }

    public int getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(int recommendation) {
        this.recommendation = recommendation & 0x7F;
    }

    public void setCauseValue(int causeValue) {
        this.causeValue = causeValue;
    }

    public byte[] getDiagnostics() {
        return diagnostics;
    }

    public void setDiagnostics(byte[] diagnostics) {
        this.diagnostics = diagnostics;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("CauseIndicators [");

        sb.append("codingStandard=");
        sb.append(codingStandard);
        sb.append(", location=");
        sb.append(location);
        sb.append(", recommendation=");
        sb.append(recommendation);
        sb.append(", causeValue=");
        sb.append(causeValue);

        if (this.diagnostics != null) {
            sb.append(", diagnostics=[");
            sb.append(printDataArr(this.diagnostics));
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }

    protected String printDataArr(byte[] data) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        if (data != null) {
            for (int b : data) {
                if (first)
                    first = false;
                else
                    sb.append(", ");
                sb.append(b);
            }
        }

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<CauseIndicatorsImpl> ISUP_CAUSE_INDICATORS_XML = new XMLFormat<CauseIndicatorsImpl>(
            CauseIndicatorsImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, CauseIndicatorsImpl causeIndicators)
                throws XMLStreamException {
            causeIndicators.location = xml.getAttribute(LOCATION, DEFAULT_VALUE);
            causeIndicators.causeValue = xml.getAttribute(CAUSE_VALUE, DEFAULT_VALUE);
            causeIndicators.codingStandard = xml.getAttribute(CODING_STANDARD, DEFAULT_VALUE);
            causeIndicators.recommendation = xml.getAttribute(RECOMMENDATION, DEFAULT_VALUE);

            ByteArrayContainer bc = xml.get(DIAGNOSTICS, ByteArrayContainer.class);
            if (bc != null) {
                causeIndicators.diagnostics = bc.getData();
            }
        }

        @Override
        public void write(CauseIndicatorsImpl causeIndicators, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            xml.setAttribute(LOCATION, causeIndicators.location);
            xml.setAttribute(CAUSE_VALUE, causeIndicators.causeValue);
            xml.setAttribute(CODING_STANDARD, causeIndicators.codingStandard);
            xml.setAttribute(RECOMMENDATION, causeIndicators.recommendation);

            if (causeIndicators.diagnostics != null) {
                ByteArrayContainer bac = new ByteArrayContainer(causeIndicators.diagnostics);
                xml.add(bac, DIAGNOSTICS, ByteArrayContainer.class);
            }
        }
    };
}
