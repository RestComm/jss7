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

package org.mobicents.protocols.ss7.m3ua.impl.message.rkm;

import java.nio.ByteBuffer;

import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterImpl;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.rkm.RegistrationResponse;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;
import org.mobicents.protocols.ss7.m3ua.parameter.RegistrationResult;

/**
 *
 * @author amit bhayani
 *
 */
public class RegistrationResponseImpl extends M3UAMessageImpl implements RegistrationResponse {

    public RegistrationResponseImpl() {
        super(MessageClass.ROUTING_KEY_MANAGEMENT, MessageType.REG_RESPONSE, MessageType.S_REG_RESPONSE);
    }

    @Override
    protected void encodeParams(ByteBuffer buffer) {
        ((ParameterImpl) parameters.get(Parameter.Registration_Result)).write(buffer);
    }

    public RegistrationResult getRegistrationResult() {
        return (RegistrationResult) parameters.get(ParameterImpl.Registration_Result);
    }

    public void setRegistrationResult(RegistrationResult rslt) {
        parameters.put(ParameterImpl.Registration_Result, rslt);
    }

}
