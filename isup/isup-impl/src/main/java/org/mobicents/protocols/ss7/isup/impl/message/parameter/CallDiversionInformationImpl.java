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
 * Start time:14:32:32 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.CallDiversionInformation;

/**
 * Start time:14:32:32 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 *
 */
public class CallDiversionInformationImpl extends AbstractISUPParameter implements CallDiversionInformation {

    private int redirectingReason = 0;

    private int notificationSubscriptionOptions = 0;

    public CallDiversionInformationImpl(int notificationSubscriptionOptions, int redirectingReason) {
        super();
        this.notificationSubscriptionOptions = notificationSubscriptionOptions;
        this.redirectingReason = redirectingReason;
    }

    public CallDiversionInformationImpl() {
        super();
    }

    public CallDiversionInformationImpl(byte[] b) throws ParameterException {
        super();
        decode(b);
    }

    public int decode(byte[] b) throws ParameterException {
        if (b == null || b.length != 1) {
            throw new ParameterException("byte[] must not be null or have different size than 1");
        }

        int v = b[0];
        this.notificationSubscriptionOptions = v & 0x07;
        this.redirectingReason = (v >> 3) & 0x0F;

        return 1;
    }

    public byte[] encode() throws ParameterException {
        byte[] b = new byte[1];

        int v = 0;
        v |= this.notificationSubscriptionOptions & 0x07;
        v |= (this.redirectingReason & 0x0F) << 3;
        b[0] = (byte) v;
        return b;
    }

    public int encode(ByteArrayOutputStream bos) throws ParameterException {
        byte[] b = this.encode();
        try {
            bos.write(b);
        } catch (IOException e) {
            throw new ParameterException(e);
        }
        return b.length;
    }

    public int getNotificationSubscriptionOptions() {
        return notificationSubscriptionOptions;
    }

    public void setNotificationSubscriptionOptions(int notificationSubscriptionOptions) {
        this.notificationSubscriptionOptions = notificationSubscriptionOptions;
    }

    public int getRedirectingReason() {
        return redirectingReason;
    }

    public void setRedirectingReason(int redirectingReason) {
        this.redirectingReason = redirectingReason;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
