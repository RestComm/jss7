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
 * Start time:11:21:05 2009-03-31<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayOutputStream;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.EndOfOptionalParameters;

/**
 * Start time:11:21:05 2009-03-31<br>
 * Project: mobicents-isup-stack<br>
 * This class represent element that encodes end of parameters
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class EndOfOptionalParametersImpl extends AbstractISUPParameter implements EndOfOptionalParameters {

    public EndOfOptionalParametersImpl() {
        super();

    }

    public EndOfOptionalParametersImpl(byte[] b) {
        super();

    }

    /**
     * heeh, value is zero actually :D
     */
    public static final int _PARAMETER_CODE = 0;

    public int decode(byte[] b) throws ParameterException {

        return 0;
    }

    public byte[] encode() throws ParameterException {
        // TODO Auto-generated method stub
        return new byte[] { 0 };
    }

    public int encode(ByteArrayOutputStream bos) throws ParameterException {
        bos.write(0);
        return 1;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
