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
 * Start time:16:59:42 2009-04-03<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.SuspendResumeIndicators;

/**
 * Start time:16:59:42 2009-04-03<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class SuspendResumeIndicatorsImpl extends AbstractISUPParameter implements SuspendResumeIndicators {

    private static final int _TURN_ON = 1;
    private static final int _TURN_OFF = 0;

    private boolean suspendResumeIndicator;

    public SuspendResumeIndicatorsImpl() {
        super();

    }

    public SuspendResumeIndicatorsImpl(byte[] b) throws ParameterException {
        super();
        decode(b);
    }

    public SuspendResumeIndicatorsImpl(boolean suspendResumeIndicator) {
        super();
        this.suspendResumeIndicator = suspendResumeIndicator;
    }

    public int decode(byte[] b) throws ParameterException {
        if (b == null || b.length != 1) {
            throw new ParameterException("byte[] must  not be null and length must  be 1");
        }

        this.suspendResumeIndicator = (b[0] & 0x01) == _TURN_ON;

        return 1;
    }

    public byte[] encode() throws ParameterException {
        return new byte[] { (byte) (this.suspendResumeIndicator ? _TURN_ON : _TURN_OFF) };
    }

    public boolean isSuspendResumeIndicator() {
        return suspendResumeIndicator;
    }

    public void setSuspendResumeIndicator(boolean suspendResumeIndicator) {
        this.suspendResumeIndicator = suspendResumeIndicator;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
