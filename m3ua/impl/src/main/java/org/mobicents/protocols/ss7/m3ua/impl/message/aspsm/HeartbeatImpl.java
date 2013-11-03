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

package org.mobicents.protocols.ss7.m3ua.impl.message.aspsm;

import java.nio.ByteBuffer;

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
    protected void encodeParams(ByteBuffer buffer) {
        if (parameters.containsKey(Parameter.Heartbeat_Data)) {
            ((ParameterImpl) parameters.get(Parameter.Heartbeat_Data)).write(buffer);
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
