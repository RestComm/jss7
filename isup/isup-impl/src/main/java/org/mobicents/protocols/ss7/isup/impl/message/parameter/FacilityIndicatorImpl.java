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

/**
 * Start time:11:50:01 2009-03-31<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.FacilityIndicator;

/**
 * Start time:11:50:01 2009-03-31<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class FacilityIndicatorImpl extends AbstractISUPParameter implements FacilityIndicator {

    private byte facilityIndicator = 0;

    public FacilityIndicatorImpl(byte[] b) throws ParameterException {
        super();
        decode(b);
    }

    public FacilityIndicatorImpl() {
        super();

    }

    public FacilityIndicatorImpl(byte facilityIndicator) {
        super();
        this.facilityIndicator = facilityIndicator;
    }

    public int decode(byte[] b) throws ParameterException {
        if (b == null || b.length != 1) {
            throw new ParameterException("byte[] must not be null or have different size than 1");
        }

        this.facilityIndicator = b[0];
        return 1;
    }

    public byte[] encode() throws ParameterException {
        byte[] b = { (byte) this.facilityIndicator };
        return b;
    }

    public byte getFacilityIndicator() {
        return facilityIndicator;
    }

    public void setFacilityIndicator(byte facilityIndicator) {
        this.facilityIndicator = facilityIndicator;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
