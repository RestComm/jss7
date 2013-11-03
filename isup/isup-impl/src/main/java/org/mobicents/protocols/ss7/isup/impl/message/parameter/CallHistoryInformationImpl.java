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
 * Start time:15:04:29 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.CallHistoryInformation;

/**
 * Start time:15:04:29 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class CallHistoryInformationImpl extends AbstractISUPParameter implements CallHistoryInformation {

    // XXX: again this goes aganist usuall way.
    private int callHistory;

    public CallHistoryInformationImpl(byte[] b) throws ParameterException {
        super();
        decode(b);
    }

    public CallHistoryInformationImpl(int callHistory) {
        super();
        this.callHistory = callHistory;
    }

    public CallHistoryInformationImpl() {
        super();

    }

    public int decode(byte[] b) throws ParameterException {
        // This one is other way around, as Eduardo might say.
        if (b == null || b.length != 2) {
            throw new IllegalArgumentException("byte[] must  not be null and length must be 2");
        }

        // this.callHistory = b[0] << 8;
        // this.callHistory |= b[1];
        // //We need this, cause otherwise we get corrupted number
        // this.callHistory &=0xFFFF;
        this.callHistory = ((b[0] << 8) | b[1]) & 0xFFFF;

        return b.length;
    }

    public byte[] encode() throws ParameterException {

        byte b0 = (byte) (this.callHistory >> 8);
        byte b1 = (byte) this.callHistory;
        return new byte[] { b0, b1 };
    }

    public int getCallHistory() {
        return callHistory;
    }

    public void setCallHistory(int callHistory) {
        this.callHistory = callHistory;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
