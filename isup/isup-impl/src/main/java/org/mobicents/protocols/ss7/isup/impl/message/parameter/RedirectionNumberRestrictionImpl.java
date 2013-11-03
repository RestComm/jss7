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
 * Start time:16:55:01 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectionNumberRestriction;

/**
 * Start time:16:55:01 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 *
 */
public class RedirectionNumberRestrictionImpl extends AbstractISUPParameter implements RedirectionNumberRestriction {

    private int presentationRestrictedIndicator;

    public RedirectionNumberRestrictionImpl(int presentationRestrictedIndicator) {
        super();
        this.presentationRestrictedIndicator = presentationRestrictedIndicator;
    }

    public RedirectionNumberRestrictionImpl() {
        super();

    }

    public RedirectionNumberRestrictionImpl(byte[] b) throws ParameterException {
        super();
        decode(b);
    }

    public int decode(byte[] b) throws ParameterException {
        if (b == null || b.length != 1) {
            throw new ParameterException("byte[] must  not be null and length must  be 1");
        }

        this.presentationRestrictedIndicator = (byte) (b[0] & 0x03);
        return 1;
    }

    public byte[] encode() throws ParameterException {
        return new byte[] { (byte) (this.presentationRestrictedIndicator & 0x03) };
    }

    public int getPresentationRestrictedIndicator() {
        return presentationRestrictedIndicator;
    }

    public void setPresentationRestrictedIndicator(int presentationRestrictedIndicator) {
        this.presentationRestrictedIndicator = presentationRestrictedIndicator;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
