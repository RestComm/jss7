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
 * Start time:18:41:12 2009-04-03<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.TransimissionMediumRequierementPrime;

/**
 * Start time:18:41:12 2009-04-03<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class TransimissionMediumRequierementPrimeImpl extends AbstractISUPParameter implements
        TransimissionMediumRequierementPrime {

    public TransimissionMediumRequierementPrimeImpl() {
        super();

    }

    public TransimissionMediumRequierementPrimeImpl(int transimissionMediumRequirement) {
        super();
        this.transimissionMediumRequirement = transimissionMediumRequirement;
    }

    public TransimissionMediumRequierementPrimeImpl(byte[] b) throws ParameterException {
        super();
        decode(b);
    }

    // Defualt indicate speech
    private int transimissionMediumRequirement;

    // FIXME: again wrapper class but hell there is a lot of statics....

    public int decode(byte[] b) throws ParameterException {
        if (b == null || b.length != 1) {
            throw new ParameterException("byte[] must  not be null and length must  be 1");
        }

        this.transimissionMediumRequirement = b[0];

        return 1;
    }

    public byte[] encode() throws ParameterException {
        return new byte[] { (byte) this.transimissionMediumRequirement };
    }

    public int getTransimissionMediumRequirement() {
        return transimissionMediumRequirement;
    }

    public void setTransimissionMediumRequirement(int transimissionMediumRequirement) {
        this.transimissionMediumRequirement = transimissionMediumRequirement;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }

}
