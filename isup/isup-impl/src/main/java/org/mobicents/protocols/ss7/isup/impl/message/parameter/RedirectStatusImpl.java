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
 * Start time:09:49:43 2009-04-06<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectStatus;

/**
 * Start time:09:49:43 2009-04-06<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class RedirectStatusImpl extends AbstractISUPParameter implements RedirectStatus {

    private byte[] status;

    public RedirectStatusImpl() {
        super();

    }

    public RedirectStatusImpl(byte[] b) throws ParameterException {
        super();
        decode(b);
    }

    public int decode(byte[] b) throws ParameterException {
        try {
            setStatus(b);
        } catch (Exception e) {
            throw new ParameterException(e);
        }
        return b.length;
    }

    public byte[] encode() throws ParameterException {

        for (int index = 0; index < this.status.length; index++) {
            this.status[index] = (byte) (this.status[index] & 0x03);
        }

        this.status[this.status.length - 1] = (byte) ((this.status[this.status.length - 1]) | (0x01 << 7));
        return this.status;
    }

    public byte[] getStatus() {
        return status;
    }

    public void setStatus(byte[] status) {
        if (status == null || status.length == 0) {
            throw new IllegalArgumentException("byte[] must not be null and length must be greater than 0");
        }
        this.status = status;
    }

    public int getStatusIndicator(byte b) {
        return b & 0x03;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
