/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
