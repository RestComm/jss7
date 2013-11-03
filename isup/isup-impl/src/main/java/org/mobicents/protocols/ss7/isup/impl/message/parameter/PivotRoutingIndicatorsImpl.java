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
 * Start time:16:16:18 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.PivotRoutingIndicators;

/**
 * Start time:16:16:18 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class PivotRoutingIndicatorsImpl extends AbstractISUPParameter implements PivotRoutingIndicators {

    private byte[] pivotRoutingIndicators;

    public PivotRoutingIndicatorsImpl() {
        super();

    }

    public PivotRoutingIndicatorsImpl(byte[] pivotRoutingIndicators) throws ParameterException {
        super();
        decode(pivotRoutingIndicators);
    }

    public byte[] encode() throws ParameterException {
        for (int index = 0; index < this.pivotRoutingIndicators.length; index++) {
            this.pivotRoutingIndicators[index] = (byte) (this.pivotRoutingIndicators[index] & 0x7F);
        }

        this.pivotRoutingIndicators[this.pivotRoutingIndicators.length - 1] = (byte) ((this.pivotRoutingIndicators[this.pivotRoutingIndicators.length - 1]) | (0x01 << 7));
        return this.pivotRoutingIndicators;
    }

    public int decode(byte[] b) throws ParameterException {

        setPivotRoutingIndicators(b);
        return b.length;
    }

    public byte[] getPivotRoutingIndicators() {
        return pivotRoutingIndicators;
    }

    public void setPivotRoutingIndicators(byte[] pivotRoutingIndicators) {
        if (pivotRoutingIndicators == null || pivotRoutingIndicators.length == 0) {
            throw new IllegalArgumentException("byte[] must not be null and length must be greater than 0");
        }
        this.pivotRoutingIndicators = pivotRoutingIndicators;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
