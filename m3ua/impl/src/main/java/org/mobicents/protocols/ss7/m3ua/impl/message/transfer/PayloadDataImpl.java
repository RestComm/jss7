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

package org.mobicents.protocols.ss7.m3ua.impl.message.transfer;

import io.netty.buffer.ByteBuf;

import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.CorrelationIdImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ProtocolDataImpl;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.transfer.PayloadData;
import org.mobicents.protocols.ss7.m3ua.parameter.CorrelationId;
import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;
import org.mobicents.protocols.ss7.m3ua.parameter.ProtocolData;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;

/**
 * @author amit bhayani
 * @author kulikov
 */
public class PayloadDataImpl extends M3UAMessageImpl implements PayloadData {

    public PayloadDataImpl() {
        super(MessageClass.TRANSFER_MESSAGES, MessageType.PAYLOAD, MessageType.S_PAYLOAD);
    }

    public NetworkAppearance getNetworkAppearance() {
        return (NetworkAppearance) parameters.get(Parameter.Network_Appearance);
    }

    public void setNetworkAppearance(NetworkAppearance p) {
        if (p != null) {
            parameters.put(Parameter.Network_Appearance, p);
        }
    }

    public RoutingContext getRoutingContext() {
        return (RoutingContext) parameters.get(Parameter.Routing_Context);
    }

    public void setRoutingContext(RoutingContext p) {
        if (p != null) {
            parameters.put(Parameter.Routing_Context, p);
        }
    }

    public ProtocolData getData() {
        return (ProtocolDataImpl) parameters.get(Parameter.Protocol_Data);
    }

    public void setData(ProtocolData p) {
        parameters.put(Parameter.Protocol_Data, p);
    }

    public CorrelationId getCorrelationId() {
        return (CorrelationIdImpl) parameters.get(Parameter.Correlation_ID);
    }

    public void setCorrelationId(CorrelationId corrId) {
        if (corrId != null) {
            parameters.put(Parameter.Correlation_ID, corrId);
        }
    }

    @Override
    public String toString() {
        return "TransferMessage: " + parameters;
    }

    @Override
    protected void encodeParams(ByteBuf buf) {
        if (parameters.containsKey(Parameter.Network_Appearance)) {
            ((ParameterImpl) parameters.get(Parameter.Network_Appearance)).write(buf);
        }
        if (parameters.containsKey(Parameter.Routing_Context)) {
            ((ParameterImpl) parameters.get(Parameter.Routing_Context)).write(buf);
        }
        if (parameters.containsKey(Parameter.Protocol_Data)) {
            ((ParameterImpl) parameters.get(Parameter.Protocol_Data)).write(buf);
        }
        if (parameters.containsKey(Parameter.Correlation_ID)) {
            ((ParameterImpl) parameters.get(Parameter.Correlation_ID)).write(buf);
        }
    }
}
