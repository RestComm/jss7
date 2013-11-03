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

package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import java.nio.ByteBuffer;

import org.mobicents.protocols.ss7.m3ua.parameter.DeregistrationResult;
import org.mobicents.protocols.ss7.m3ua.parameter.DeregistrationStatus;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;

/**
 *
 * @author amit bhayani
 *
 */
public class DeregistrationResultImpl extends ParameterImpl implements DeregistrationResult {

    private RoutingContext rc;
    private DeregistrationStatus status;
    private byte[] value;

    private ByteBuffer buffer = ByteBuffer.allocate(16);

    public DeregistrationResultImpl(RoutingContext rc, DeregistrationStatus status) {
        this.tag = Parameter.Deregistration_Result;
        this.rc = rc;
        this.status = status;

        this.encode();
    }

    public DeregistrationResultImpl(byte[] data) {
        this.tag = Parameter.Deregistration_Result;
        int pos = 0;

        while (pos < data.length) {
            short tag = (short) ((data[pos] & 0xff) << 8 | (data[pos + 1] & 0xff));
            short len = (short) ((data[pos + 2] & 0xff) << 8 | (data[pos + 3] & 0xff));

            byte[] value = new byte[len - 4];

            System.arraycopy(data, pos + 4, value, 0, value.length);
            pos += len;
            // parameters.put(tag, factory.createParameter(tag, value));
            switch (tag) {
                case ParameterImpl.Routing_Context:
                    this.rc = new RoutingContextImpl(value);
                    break;

                case ParameterImpl.Deregistration_Status:
                    this.status = new DeregistrationStatusImpl(value);
                    break;

            }

            // The Parameter Length does not include any padding octets. We have
            // to consider padding here
            pos += (pos % 4);
        }// end of while
    }

    private void encode() {
        ((RoutingContextImpl) this.rc).write(buffer);
        ((DeregistrationStatusImpl) this.status).write(buffer);
        value = buffer.array();
    }

    @Override
    protected byte[] getValue() {
        return this.value;
    }

    public DeregistrationStatus getDeregistrationStatus() {
        return this.status;
    }

    public RoutingContext getRoutingContext() {
        return this.rc;
    }

}
