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

package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import javolution.text.TextBuilder;

import org.mobicents.protocols.ss7.m3ua.parameter.LocalRKIdentifier;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;
import org.mobicents.protocols.ss7.m3ua.parameter.RegistrationResult;
import org.mobicents.protocols.ss7.m3ua.parameter.RegistrationStatus;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;

public class RegistrationResultImpl extends ParameterImpl implements RegistrationResult {

    private LocalRKIdentifier localRKId;
    private RegistrationStatus status;
    private RoutingContext rc;

    private ByteBuf buf = Unpooled.buffer(24);
    private byte[] value;

    public RegistrationResultImpl(byte[] data) {
        this.tag = Parameter.Registration_Result;
        int pos = 0;

        while (pos < data.length) {
            short tag = (short) ((data[pos] & 0xff) << 8 | (data[pos + 1] & 0xff));
            short len = (short) ((data[pos + 2] & 0xff) << 8 | (data[pos + 3] & 0xff));

            byte[] value = new byte[len - 4];

            System.arraycopy(data, pos + 4, value, 0, value.length);
            pos += len;
            // parameters.put(tag, factory.createParameter(tag, value));
            switch (tag) {
                case ParameterImpl.Local_Routing_Key_Identifier:
                    this.localRKId = new LocalRKIdentifierImpl(value);
                    break;

                case ParameterImpl.Routing_Context:
                    this.rc = new RoutingContextImpl(value);
                    break;

                case ParameterImpl.Registration_Status:
                    this.status = new RegistrationStatusImpl(value);
                    break;

            }

            // The Parameter Length does not include any padding octets. We have
            // to consider padding here
            pos += (pos % 4);
        }// end of while
    }

    public RegistrationResultImpl(LocalRKIdentifier localRKId, RegistrationStatus status, RoutingContext rc) {
        this.tag = Parameter.Registration_Result;
        this.localRKId = localRKId;
        this.status = status;
        this.rc = rc;

        this.encode();
    }

    private void encode() {
        ((LocalRKIdentifierImpl) this.localRKId).write(buf);

        ((RoutingContextImpl) rc).write(buf);

        ((RegistrationStatusImpl) this.status).write(buf);

        int length = buf.readableBytes();
        value = new byte[length];
        buf.getBytes(buf.readerIndex(), value);
    }

    @Override
    protected byte[] getValue() {
        return this.value;
    }

    public LocalRKIdentifier getLocalRKIdentifier() {
        return this.localRKId;
    }

    public RegistrationStatus getRegistrationStatus() {
        return this.status;
    }

    public RoutingContext getRoutingContext() {
        return this.rc;
    }

    @Override
    public String toString() {
        TextBuilder tb = TextBuilder.newInstance();
        tb.append("RegistrationResult(");
        if (localRKId != null) {
            tb.append(localRKId.toString());
        }

        if (status != null) {
            tb.append(status.toString());
        }

        if (rc != null) {
            tb.append(rc.toString());
        }
        tb.append(")");
        return tb.toString();
    }
}
