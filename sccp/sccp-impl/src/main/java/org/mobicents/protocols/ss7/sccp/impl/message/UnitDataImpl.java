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

package org.mobicents.protocols.ss7.sccp.impl.message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.mobicents.protocols.ss7.sccp.impl.parameter.AbstractParameter;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ProtocolClassImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressCodec;
import org.mobicents.protocols.ss7.sccp.message.UnitData;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 *
 * @author Oleg Kulikov
 */
public class UnitDataImpl extends SccpMessageImpl implements UnitData {

    protected byte[] data;

    private SccpAddressCodec addressCodec = new SccpAddressCodec();
    
    protected UnitDataImpl() {
        super(MESSAGE_TYPE);
    }
    
    protected UnitDataImpl(ProtocolClass pClass, SccpAddress calledParty,
            SccpAddress callingParty) {
        super(MESSAGE_TYPE);
        this.protocolClass = (ProtocolClassImpl) pClass;
        this.calledParty = (SccpAddress) calledParty;
        this.callingParty = (SccpAddress) callingParty;
    }

    public byte[] getData() {
        return data;
    }

    public void encode(OutputStream out) throws IOException {
        out.write(0x09);

        out.write(((AbstractParameter)protocolClass).encode());

        byte[] cdp = addressCodec.encode(calledParty);
        byte[] cnp = addressCodec.encode(callingParty);

        int len = 3;
        out.write(len);

        len = (cdp.length + 3);
        out.write(len);

        len += (cnp.length);
        out.write(len);

        out.write((byte) cdp.length);
        out.write(cdp);

        out.write((byte) cnp.length);
        out.write(cnp);

        out.write((byte) data.length);
        out.write(data);
    }

    public void decode(InputStream in) throws IOException {
    	protocolClass = new ProtocolClassImpl();
    	((AbstractParameter)protocolClass).decode(new byte[]{(byte) in.read()});

        int cpaPointer = in.read() & 0xff;
        in.mark(in.available());

        in.skip(cpaPointer - 1);
        int len = in.read() & 0xff;

        byte[] buffer = new byte[len];
        in.read(buffer);

        calledParty = addressCodec.decode(buffer);

        in.reset();
        cpaPointer = in.read() & 0xff;
        in.mark(in.available());

        in.skip(cpaPointer - 1);
        len = in.read() & 0xff;

        buffer = new byte[len];
        in.read(buffer);

        callingParty = addressCodec.decode(buffer);

        in.reset();
        cpaPointer = in.read() & 0xff;

        in.skip(cpaPointer - 1);
        len = in.read() & 0xff;

        data = new byte[len];
        in.read(data);
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    
    public String toString() {
        return "UDT[calledPartyAddress=" + calledParty + ", callingPartyAddress=" + callingParty + "data length=" + data.length + "]";
    }
    
}
