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
 * Start time:13:49:42 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.UIDActionIndicators;

/**
 * Start time:13:49:42 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 *
 */
public class UIDActionIndicatorsImpl extends AbstractISUPParameter implements UIDActionIndicators {

    private static final int _TURN_ON = 1;
    private static final int _TURN_OFF = 0;

    private byte[] udiActionIndicators = null;

    public UIDActionIndicatorsImpl(byte[] udiActionIndicators) throws ParameterException {
        super();
        decode(udiActionIndicators);
    }

    public UIDActionIndicatorsImpl() {
        super();

    }

    public int decode(byte[] b) throws ParameterException {
        try {
            setUdiActionIndicators(b);
        } catch (Exception e) {
            throw new ParameterException(e);
        }
        return b.length;
    }

    public byte[] encode() throws ParameterException {
        for (int index = 0; index < this.udiActionIndicators.length; index++) {
            this.udiActionIndicators[index] = (byte) (this.udiActionIndicators[index] & 0x7F);
        }

        this.udiActionIndicators[this.udiActionIndicators.length - 1] = (byte) ((this.udiActionIndicators[this.udiActionIndicators.length - 1]) | (0x01 << 7));
        return this.udiActionIndicators;
    }

    public byte[] getUdiActionIndicators() {
        return udiActionIndicators;
    }

    public void setUdiActionIndicators(byte[] udiActionIndicators) {
        if (udiActionIndicators == null || udiActionIndicators.length == 0) {
            throw new IllegalArgumentException("byte[] must not be null and length must be greater than 0");
        }
        this.udiActionIndicators = udiActionIndicators;
    }

    public byte createUIDAction(boolean TCII, boolean T9) {
        byte b = (byte) (TCII ? _TURN_ON : _TURN_OFF);
        b |= (T9 ? _TURN_ON : _TURN_OFF) << 1;
        return b;
    }

    public boolean getT9Indicator(byte b) {
        return ((b >> 1) & 0x01) == _TURN_ON;
    }

    public boolean getTCIIndicator(byte b) {
        return ((b >> 1) & 0x01) == _TURN_ON;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
