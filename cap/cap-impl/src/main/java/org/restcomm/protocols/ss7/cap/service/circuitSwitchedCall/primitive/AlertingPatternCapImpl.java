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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AlertingPatternCap;
import org.restcomm.protocols.ss7.cap.primitives.OctetStringBase;
import org.restcomm.protocols.ss7.map.api.primitives.AlertingPattern;
import org.restcomm.protocols.ss7.map.primitives.AlertingPatternImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class AlertingPatternCapImpl extends OctetStringBase implements AlertingPatternCap {

    private static final String ALERTING_PATTERN = "alertingPattern";

    public AlertingPatternCapImpl() {
        super(3, 3, "AlertingPatternCap");
    }

    public AlertingPatternCapImpl(byte[] data) {
        super(3, 3, "AlertingPatternCap", data);
    }

    public AlertingPatternCapImpl(AlertingPattern alertingPattern) {
        super(3, 3, "AlertingPatternCap");
        setAlertingPattern(alertingPattern);
    }

    public void setAlertingPattern(AlertingPattern alertingPattern) {

        if (alertingPattern == null)
            return;

        this.data = new byte[3];
        this.data[2] = (byte) alertingPattern.getData();
    }

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public AlertingPattern getAlertingPattern() {

        if (this.data != null && this.data.length == 3)
            return new AlertingPatternImpl(this.data[2]);
        else
            return null;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");
        AlertingPattern ap = this.getAlertingPattern();
        if (ap != null) {
            sb.append("AlertingPattern=");
            sb.append(ap.toString());
        }
        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<AlertingPatternCapImpl> ALERTING_PATTERN_CAP_XML = new XMLFormat<AlertingPatternCapImpl>(
            AlertingPatternCapImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, AlertingPatternCapImpl alertingPattern)
                throws XMLStreamException {
            alertingPattern.setAlertingPattern(xml.get(ALERTING_PATTERN, AlertingPatternImpl.class));
        }

        @Override
        public void write(AlertingPatternCapImpl alertingPattern, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            AlertingPattern ap = alertingPattern.getAlertingPattern();
            if (ap != null)
                xml.add((AlertingPatternImpl) ap, ALERTING_PATTERN, AlertingPatternImpl.class);
        }
    };
}
