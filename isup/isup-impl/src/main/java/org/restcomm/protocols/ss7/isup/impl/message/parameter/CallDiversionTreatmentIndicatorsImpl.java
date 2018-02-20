/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

/**
 * Start time:12:50:23 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.CallDiversionTreatmentIndicators;

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
