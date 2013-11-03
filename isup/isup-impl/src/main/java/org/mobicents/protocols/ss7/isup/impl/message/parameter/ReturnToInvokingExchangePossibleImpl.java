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
 * Start time:09:06:33 2009-04-06<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.InformationType;
import org.mobicents.protocols.ss7.isup.message.parameter.ReturnToInvokingExchangePossible;

/**
 * Start time:09:06:33 2009-04-06<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ReturnToInvokingExchangePossibleImpl extends AbstractInformationImpl implements ReturnToInvokingExchangePossible {
    //its empty param, its existence is information.
    private static final byte[] DATA = new byte[]{};
    public ReturnToInvokingExchangePossibleImpl() {
        super(InformationType.ReturnToInvokingExchangePossible);
        //This is always 0x01;
        super.tag = 0x01;
    }

    public void decode(byte[] b) throws ParameterException {
        return;
    }

    public byte[] encode() throws ParameterException {
        return DATA;
    }


}
