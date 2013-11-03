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
 * Start time:15:02:53 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.CollectCallRequest;

/**
 * Start time:15:02:53 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class CollectCallRequestImpl extends AbstractISUPParameter implements CollectCallRequest {

    private static final int _TURN_ON = 1;
    private static final int _TURN_OFF = 0;

    private boolean collectCallRequested = false;

    public CollectCallRequestImpl(boolean collectCallRequested) {
        super();
        this.collectCallRequested = collectCallRequested;
    }

    public CollectCallRequestImpl() {
        super();

    }

    public CollectCallRequestImpl(byte[] b) throws ParameterException {
        super();
        decode(b);
    }

    public int decode(byte[] b) throws ParameterException {
        if (b == null || b.length != 1) {
            throw new ParameterException("byte[] must not be null and length must be 1");
        }
        this.collectCallRequested = (b[0] & 0x01) == _TURN_ON;

        return 1;
    }

    public byte[] encode() throws ParameterException {
        return new byte[] { (byte) (this.collectCallRequested ? _TURN_ON : _TURN_OFF) };
    }

    public boolean isCollectCallRequested() {
        return collectCallRequested;
    }

    public void setCollectCallRequested(boolean collectCallRequested) {
        this.collectCallRequested = collectCallRequested;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
