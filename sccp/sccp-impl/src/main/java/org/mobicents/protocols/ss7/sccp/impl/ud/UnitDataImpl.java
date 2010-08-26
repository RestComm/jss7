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
package org.mobicents.protocols.ss7.sccp.impl.ud;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.mobicents.protocols.ss7.sccp.impl.parameter.ProtocolClassImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 *
 * @author Oleg Kulikov
 */
public class UnitDataImpl {

    private static final int _MT = 0x09;
    protected ProtocolClass pClass;
    protected SccpAddressImpl calledParty;
    protected SccpAddressImpl callingParty;
    protected byte[] data;

    /** Creates a new instance of UnitData */
    public UnitDataImpl() {
    }

    public UnitDataImpl(ProtocolClass pClass, SccpAddress calledParty,
            SccpAddress callingParty, byte[] data) {
        this.pClass = pClass;
        this.calledParty = (SccpAddressImpl) calledParty;
        this.callingParty = (SccpAddressImpl) callingParty;
        this.data = data;
    }

    public SccpAddress getCalledParty() {
        return calledParty;
    }

    public SccpAddress getCallingParty() {
        return callingParty;
    }

    public ProtocolClass getpClass() {
        return pClass;
    }

    public byte[] getData() {
        return data;
    }

    public void encode(OutputStream out) throws IOException {
        out.write(0x09);

        pClass.encode(out);

        byte[] cdp = calledParty.encode();
        byte[] cnp = callingParty.encode();

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

        calledParty = new SccpAddressImpl();
        calledParty.decode(buffer);

        in.reset();
        cpaPointer = in.read() & 0xff;
        in.mark(in.available());

        in.skip(cpaPointer - 1);
        len = in.read() & 0xff;

        buffer = new byte[len];
        in.read(buffer);

        callingParty = new SccpAddressImpl();
        callingParty.decode(buffer);

        in.reset();
        cpaPointer = in.read() & 0xff;

        in.skip(cpaPointer - 1);
        len = in.read() & 0xff;

        data = new byte[len];
        in.read(data);
    }
}
