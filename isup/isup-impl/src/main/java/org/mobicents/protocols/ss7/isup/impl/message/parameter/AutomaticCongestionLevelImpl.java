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
 * Start time:13:42:13 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayOutputStream;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.AutomaticCongestionLevel;

/**
 * Start time:13:42:13 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class AutomaticCongestionLevelImpl extends AbstractISUPParameter implements AutomaticCongestionLevel {

    private int automaticCongestionLevel = 0;

    public AutomaticCongestionLevelImpl() {
        super();

    }

    public AutomaticCongestionLevelImpl(byte[] b) throws ParameterException {
        super();
        decode(b);
    }

    public int decode(byte[] b) throws ParameterException {
        if (b == null || b.length != 1) {
            throw new ParameterException("byte[] must not be null or have different size than 1");
        }
        this.automaticCongestionLevel = b[0];
        return 1;
    }

    public byte[] encode() throws ParameterException {

        return new byte[] { (byte) this.automaticCongestionLevel };
    }

    public int encode(ByteArrayOutputStream bos) throws ParameterException {
        bos.write(this.automaticCongestionLevel);
        return 1;
    }

    public int getAutomaticCongestionLevel() {
        return automaticCongestionLevel;
    }

    public void setAutomaticCongestionLevel(int automaticCongestionLevel) {
        this.automaticCongestionLevel = automaticCongestionLevel & 0x01;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
