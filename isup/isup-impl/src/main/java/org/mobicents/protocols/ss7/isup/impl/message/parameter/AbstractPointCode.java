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
 * Start time:12:23:47 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.AbstractPointCodeInterface;

/**
 * Start time:12:23:47 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public abstract class AbstractPointCode extends AbstractISUPParameter implements AbstractPointCodeInterface {

    protected int signalingPointCode;

    public int decode(byte[] b) throws ParameterException {
        if (b == null || b.length != 2) {
            throw new ParameterException("byte[] must  not be null and length must  be 1");
        }

        this.signalingPointCode = b[0];
        // FIXME: should we kill spare bits ?
        this.signalingPointCode |= b[1] << 8;
        return 1;
    }

    public byte[] encode() throws ParameterException {

        byte[] b = new byte[2];
        b[0] = (byte) this.signalingPointCode;
        b[1] = (byte) (this.signalingPointCode >> 8);

        return b;
    }

    public AbstractPointCode() {
        super();

    }

    public AbstractPointCode(byte[] b) throws ParameterException {
        super();
        decode(b);
    }

    public int getSignalingPointCode() {
        return signalingPointCode;
    }

    public void setSignalingPointCode(int signalingPointCode) {
        this.signalingPointCode = signalingPointCode;
    }

}
