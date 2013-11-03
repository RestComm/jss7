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
 * Start time:13:42:38 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.ConferenceTreatmentIndicators;

/**
 * Start time:13:42:38 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ConferenceTreatmentIndicatorsImpl extends AbstractISUPParameter implements ConferenceTreatmentIndicators {

    private byte[] conferenceAcceptance = null;

    public ConferenceTreatmentIndicatorsImpl(byte[] b) throws ParameterException {
        super();
        decode(b);
    }

    public ConferenceTreatmentIndicatorsImpl() {
        super();

    }

    public int decode(byte[] b) throws ParameterException {
        if (b == null || b.length == 0) {
            throw new ParameterException("byte[] must not be null and length must be greater than 0");
        }
        setConferenceAcceptance(b);
        return b.length;
    }

    public byte[] encode() throws ParameterException {

        for (int index = 0; index < this.conferenceAcceptance.length; index++) {
            this.conferenceAcceptance[index] = (byte) (this.conferenceAcceptance[index] & 0x03);
        }

        this.conferenceAcceptance[this.conferenceAcceptance.length - 1] = (byte) ((this.conferenceAcceptance[this.conferenceAcceptance.length - 1]) | (0x01 << 7));
        return this.conferenceAcceptance;
    }

    public byte[] getConferenceAcceptance() {
        return conferenceAcceptance;
    }

    public void setConferenceAcceptance(byte[] conferenceAcceptance) {
        if (conferenceAcceptance == null || conferenceAcceptance.length == 0) {
            throw new IllegalArgumentException("byte[] must not be null and length must be greater than 0");
        }

        this.conferenceAcceptance = conferenceAcceptance;
    }

    public int getConferenceTreatmentIndicator(byte b) {
        return b & 0x03;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
