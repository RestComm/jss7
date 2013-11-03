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
import org.mobicents.protocols.ss7.m3ua.message.rkm.DeregistrationResponse;
import org.mobicents.protocols.ss7.m3ua.parameter.DeregistrationResult;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;

/**
 *
 * @author amit bhayani
 *
 */
public class DeregistrationResponseImpl extends M3UAMessageImpl implements DeregistrationResponse {

    public DeregistrationResponseImpl() {
        super(MessageClass.ROUTING_KEY_MANAGEMENT, MessageType.DEREG_RESPONSE, MessageType.S_DEREG_RESPONSE);
    }

    @Override
    protected void encodeParams(ByteBuffer buffer) {
        ((ParameterImpl) parameters.get(Parameter.Deregistration_Result)).write(buffer);
    }

    public DeregistrationResult getDeregistrationResult() {
        return (DeregistrationResult) parameters.get(Parameter.Deregistration_Result);
    }

    public void setDeregistrationResult(DeregistrationResult result) {
        parameters.put(Parameter.Deregistration_Result, result);

    }

}
