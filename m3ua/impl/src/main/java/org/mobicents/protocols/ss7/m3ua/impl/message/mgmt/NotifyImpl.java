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

package org.mobicents.protocols.ss7.m3ua.impl.message.mgmt;

import io.netty.buffer.ByteBuf;

import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterImpl;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.mgmt.Notify;
import org.mobicents.protocols.ss7.m3ua.parameter.ASPIdentifier;
import org.mobicents.protocols.ss7.m3ua.parameter.InfoString;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.Status;

/**
 *
 * @author amit bhayani
 *
 */
public class NotifyImpl extends M3UAMessageImpl implements Notify {

    public NotifyImpl() {
        super(MessageClass.MANAGEMENT, MessageType.NOTIFY, MessageType.S_NOTIFY);
    }

    @Override
    protected void encodeParams(ByteBuf buf) {
        ((ParameterImpl) parameters.get(Parameter.Status)).write(buf);

        if (parameters.containsKey(Parameter.ASP_Identifier)) {
            ((ParameterImpl) parameters.get(Parameter.ASP_Identifier)).write(buf);
        }

        if (parameters.containsKey(Parameter.Routing_Context)) {
            ((ParameterImpl) parameters.get(Parameter.Routing_Context)).write(buf);
        }

        if (parameters.containsKey(Parameter.INFO_String)) {
            ((ParameterImpl) parameters.get(Parameter.INFO_String)).write(buf);
        }
    }

    public ASPIdentifier getASPIdentifier() {
        return ((ASPIdentifier) parameters.get(Parameter.ASP_Identifier));
    }

    public InfoString getInfoString() {
        return ((InfoString) parameters.get(Parameter.INFO_String));
    }

    public RoutingContext getRoutingContext() {
        return ((RoutingContext) parameters.get(Parameter.Routing_Context));
    }

    public Status getStatus() {
        return ((Status) parameters.get(Parameter.Status));
    }

    public void setASPIdentifier(ASPIdentifier id) {
        if (id != null) {
            parameters.put(Parameter.ASP_Identifier, id);
        }
    }

    public void setInfoString(InfoString str) {
        if (str != null) {
            parameters.put(Parameter.INFO_String, str);
        }
    }

    public void setRoutingContext(RoutingContext rc) {
        if (rc != null) {
            parameters.put(Parameter.Routing_Context, rc);
        }
    }

    public void setStatus(Status status) {
        parameters.put(Parameter.Status, status);
    }
}
