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

package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.ChargedPartyIdentification;

/**
 *
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ChargedPartyIdentificationImpl extends AbstractISUPParameter implements ChargedPartyIdentification {

  //FIXME: XXX
    @Override
    public int getCode() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int decode(byte[] b) throws ParameterException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public byte[] encode() throws ParameterException {
        // TODO Auto-generated method stub
        return null;
    }

//    3.75
//    Charged party identification (national use)
//   The format of the charged party identification parameter is national network specific. The format is
//   similar to the format of the corresponding INAP parameter in the "FurnishChargingInformation"
//   operation (see ITU-T Recommendations Q.1218 [11] and Q.1228 [12]).

}
