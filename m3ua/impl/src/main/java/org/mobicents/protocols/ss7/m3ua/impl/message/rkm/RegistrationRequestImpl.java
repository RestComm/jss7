/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.m3ua.impl.message.rkm;

import io.netty.buffer.ByteBuf;

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
    protected void encodeParams(ByteBuf buf) {
        if (parameters.containsKey(Parameter.Routing_Key)) {
            ((ParameterImpl) parameters.get(Parameter.Routing_Key)).write(buf);
        }
    }

    public RoutingKey getRoutingKey() {
        return (RoutingKey) parameters.get(Parameter.Routing_Key);
    }

    public void setRoutingKey(RoutingKey key) {
        parameters.put(Parameter.Routing_Key, key);
    }

}
