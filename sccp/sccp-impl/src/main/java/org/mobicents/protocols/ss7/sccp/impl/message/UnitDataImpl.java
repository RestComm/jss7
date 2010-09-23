/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.sccp.impl.message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.mobicents.protocols.ss7.sccp.impl.message.SccpMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ProtocolClassImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressCodec;
import org.mobicents.protocols.ss7.sccp.message.MessageType;
import org.mobicents.protocols.ss7.sccp.message.UnitData;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 *
 * @author Oleg Kulikov
 */
public class UnitDataImpl extends SccpMessageImpl implements UnitData {

    protected ProtocolClassImpl pClass;
    protected SccpAddress calledParty;
    protected SccpAddress callingParty;
    protected byte[] data;

    private SccpAddressCodec addressCodec = new SccpAddressCodec();
    
    protected UnitDataImpl() {
        super(MessageType.UDT);
    }
    
    protected UnitDataImpl(ProtocolClass pClass, SccpAddress calledParty,
            SccpAddress callingParty) {
        super(MessageType.UDT);
        this.pClass = (ProtocolClassImpl) pClass;
        this.calledParty = (SccpAddress) calledParty;
        this.callingParty = (SccpAddress) callingParty;
    }

    public SccpAddress getCalledParty() {
        return calledParty;
    }

    public SccpAddress getCallingParty() {
        return callingParty;
    }

    public ProtocolClass getProtocolClass() {
        return pClass;
    }

    public byte[] getData() {
        return data;
    }

    public void encode(OutputStream out) throws IOException {
        out.write(0x09);

        pClass.encode(out);

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
        pClass = new ProtocolClassImpl();
        pClass.decode(in);

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

    public SccpAddress getCalledPartyAddress() {
        return this.calledParty;
    }

    public SccpAddress getCallingPartyAddress() {
        return this.callingParty;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

}
