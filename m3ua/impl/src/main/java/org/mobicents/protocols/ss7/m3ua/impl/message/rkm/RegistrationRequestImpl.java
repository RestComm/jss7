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
import org.mobicents.protocols.ss7.m3ua.message.rkm.RegistrationRequest;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingKey;

/**
 *
 * @author amit bhayani
 *
 */
public class RegistrationRequestImpl extends M3UAMessageImpl implements RegistrationRequest {

    public RegistrationRequestImpl() {
        super(MessageClass.ROUTING_KEY_MANAGEMENT, MessageType.REG_REQUEST, MessageType.S_REG_REQUEST);
    }

    @Override
    protected void encodeParams(ByteBuffer buffer) {
        if (parameters.containsKey(Parameter.Routing_Key)) {
            ((ParameterImpl) parameters.get(Parameter.Routing_Key)).write(buffer);
        }
    }

    public RoutingKey getRoutingKey() {
        return (RoutingKey) parameters.get(Parameter.Routing_Key);
    }

    public void setRoutingKey(RoutingKey key) {
        parameters.put(Parameter.Routing_Key, key);
    }

}
