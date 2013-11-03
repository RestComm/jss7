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
 * Start time:08:28:43 2009-04-06<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.QueryOnReleaseCapability;

/**
 * Start time:08:28:43 2009-04-06<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class QueryOnReleaseCapabilityImpl extends AbstractISUPParameter implements QueryOnReleaseCapability {

    private static final int _TURN_ON = 1;
    private static final int _TURN_OFF = 0;
    private byte[] capabilities;

    public QueryOnReleaseCapabilityImpl() {
        super();

    }

    public int decode(byte[] b) throws ParameterException {
        this.setCapabilities(b);
        return b.length;
    }

    public byte[] encode() throws ParameterException {
        for (int index = 0; index < this.capabilities.length; index++) {
            this.capabilities[index] = (byte) (this.capabilities[index] & 0x7F);
        }

        this.capabilities[this.capabilities.length - 1] = (byte) ((this.capabilities[this.capabilities.length - 1]) | (0x01 << 7));
        return this.capabilities;
    }

    public byte[] getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(byte[] capabilities) {
        if (capabilities == null || capabilities.length == 0) {
            throw new IllegalArgumentException("byte[] must not be null and length must be greater than 0");
        }
        this.capabilities = capabilities;
    }

    public boolean isQoRSupport(byte b) {
        return (b & 0x01) == _TURN_ON;
    }

    public byte createQoRSupport(boolean enabled) {

        return (byte) (enabled ? _TURN_ON : _TURN_OFF);
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
