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
    protected void encodeParams(ByteBuf buf) {
        ((ParameterImpl) parameters.get(Parameter.Registration_Result)).write(buf);
    }

    public RegistrationResult getRegistrationResult() {
        return (RegistrationResult) parameters.get(ParameterImpl.Registration_Result);
    }

    public void setRegistrationResult(RegistrationResult rslt) {
        parameters.put(ParameterImpl.Registration_Result, rslt);
    }

}
