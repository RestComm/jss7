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
 * Start time:13:44:22 2009-03-31<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNotificationIndicator;

/**
 * Start time:13:44:22 2009-03-31<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class GenericNotificationIndicatorImpl extends AbstractISUPParameter implements GenericNotificationIndicator {

    private int[] notificationIndicator;

    public GenericNotificationIndicatorImpl(byte[] b) throws ParameterException {
        super();
        decode(b);
    }

    public GenericNotificationIndicatorImpl() {
        super();

    }

    public GenericNotificationIndicatorImpl(int[] notificationIndicator) {
        super();
        this.setNotificationIndicator(notificationIndicator);
    }

    public int decode(byte[] b) throws ParameterException {
        if (b == null || b.length < 2) {
            throw new ParameterException("byte[] must  not be null and length must be 1 or greater");
        }

        this.notificationIndicator = new int[b.length];

        for (int i = 0; i < b.length; i++) {
            int extFlag = (b[i] >> 7) & 0x01;
            if (extFlag == 0x01 && (b.length - 1 > i)) {
                throw new ParameterException("Extenstion flag idnicates end of data, however byte[] has more octets. Index: "
                        + i);
            }
            this.notificationIndicator[i] = b[i] & 0x7F;
        }
        return this.notificationIndicator.length;
    }

    public byte[] encode() throws ParameterException {
        byte[] b = new byte[this.notificationIndicator.length];

        for (int index = 0; index < b.length; index++) {
            b[index] = (byte) (this.notificationIndicator[index] & 0x7F);
        }

        // sets extension bit to show that we dont have more octets
        b[b.length - 1] |= 1 << 7;

        return b;
    }

    public int[] getNotificationIndicator() {
        return notificationIndicator;
    }

    public void setNotificationIndicator(int[] notificationIndicator) {
        if (notificationIndicator == null) {
            throw new IllegalArgumentException("Notification indicator must not be null");
        }
        this.notificationIndicator = notificationIndicator;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
