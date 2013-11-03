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
 * Start time:12:50:23 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.CallDiversionTreatmentIndicators;

/**
 * Start time:12:50:23 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class CallDiversionTreatmentIndicatorsImpl extends AbstractISUPParameter implements CallDiversionTreatmentIndicators {

    private byte[] callDivertedIndicators = null;

    public CallDiversionTreatmentIndicatorsImpl() {
        super();

    }

    public CallDiversionTreatmentIndicatorsImpl(byte[] b) throws ParameterException {
        super();
        decode(b);
    }

    public int decode(byte[] b) throws ParameterException {
        if (b == null || b.length == 0) {
            throw new ParameterException("byte[] must  not be null and length must  be greater than 0");
        }
        this.callDivertedIndicators = b;
        return b.length;
    }

    public byte[] encode() throws ParameterException {
        for (int index = 0; index < this.callDivertedIndicators.length; index++) {
            this.callDivertedIndicators[index] = (byte) (this.callDivertedIndicators[index] & 0x03);
        }

        this.callDivertedIndicators[this.callDivertedIndicators.length - 1] = (byte) ((this.callDivertedIndicators[this.callDivertedIndicators.length - 1]) | (0x01 << 7));
        return this.callDivertedIndicators;
    }

    public byte[] getCallDivertedIndicators() {
        return callDivertedIndicators;
    }

    public void setCallDivertedIndicators(byte[] callDivertedIndicators) {
        this.callDivertedIndicators = callDivertedIndicators;
    }

    public int getDiversionIndicator(byte b) {
        return b & 0x03;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
