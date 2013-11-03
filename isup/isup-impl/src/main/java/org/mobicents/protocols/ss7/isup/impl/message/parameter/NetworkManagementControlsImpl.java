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
 * Start time:12:02:43 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.NetworkManagementControls;

/**
 * Start time:12:02:43 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class NetworkManagementControlsImpl extends AbstractISUPParameter implements NetworkManagementControls {

    private static final int _TURN_ON = 1;
    private static final int _TURN_OFF = 0;
    // FIXME - should we switch to boolean[] - its a slight perf loss :P
    private byte[] networkManagementControls = null;

    public NetworkManagementControlsImpl(byte[] b) throws ParameterException {
        super();
        decode(b);
    }

    public NetworkManagementControlsImpl() {
        super();

    }

    public int decode(byte[] b) throws ParameterException {
        try {
            setNetworkManagementControls(b);
        } catch (Exception e) {
            throw new ParameterException(e);
        }
        return b.length;
    }

    public byte[] encode() throws ParameterException {

        for (int index = 0; index < this.networkManagementControls.length; index++) {
            this.networkManagementControls[index] = (byte) (this.networkManagementControls[index] & 0x01);
        }

        this.networkManagementControls[this.networkManagementControls.length - 1] = (byte) ((this.networkManagementControls[this.networkManagementControls.length - 1]) | (0x01 << 7));
        return this.networkManagementControls;
    }

    public boolean isTARControlEnabled(byte b) {
        return (b & 0x01) == _TURN_ON;
    }

    public byte createTAREnabledByte(boolean enabled) {
        return (byte) (enabled ? _TURN_ON : _TURN_OFF);
    }

    public byte[] getNetworkManagementControls() {
        return networkManagementControls;
    }

    public void setNetworkManagementControls(byte[] networkManagementControls) throws IllegalArgumentException {
        if (networkManagementControls == null || networkManagementControls.length == 0) {
            throw new IllegalArgumentException("byte[] must not be null and length must be greater than 0");
        }
        this.networkManagementControls = networkManagementControls;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
