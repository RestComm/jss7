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

package org.mobicents.protocols.ss7.m3ua.impl.message.aspsm;

import io.netty.buffer.ByteBuf;

import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterImpl;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.aspsm.Heartbeat;
import org.mobicents.protocols.ss7.m3ua.parameter.HeartbeatData;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;

/**
 *
 * @author amit bhayani
 *
 */
public class HeartbeatImpl extends M3UAMessageImpl implements Heartbeat {

    public HeartbeatImpl() {
        super(MessageClass.ASP_STATE_MAINTENANCE, MessageType.HEARTBEAT, MessageType.S_HEARTBEAT);
    }

    @Override
    protected void encodeParams(ByteBuf buf) {
        if (parameters.containsKey(Parameter.Heartbeat_Data)) {
            ((ParameterImpl) parameters.get(Parameter.Heartbeat_Data)).write(buf);
        }
    }

    public HeartbeatData getHeartbeatData() {
        return (HeartbeatData) parameters.get(Parameter.Heartbeat_Data);
    }

    public void setHeartbeatData(HeartbeatData hrBtData) {
        if (hrBtData != null) {
            parameters.put(Parameter.Heartbeat_Data, hrBtData);
        }
    }

}
