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

package org.mobicents.protocols.ss7.sccp.impl.parameter;

import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;
import org.mobicents.protocols.ss7.sccp.message.ParseException;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.SegmentingReassembling;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SegmentingReassemblingImpl extends AbstractParameter implements SegmentingReassembling {

    private byte value;

    public SegmentingReassemblingImpl() {
    }

    public SegmentingReassemblingImpl(boolean value) {
        this.value = (value) ? (byte)1 : 0;
    }

    public boolean isMoreData() {
        return value == 1;
    }

    public void setValue(boolean value) {
        this.value = (value) ? (byte)1 : 0;
    }

    @Override
    public void decode(final InputStream in, final ParameterFactory factory, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        try {
            if (in.read() != 1) {
                throw new ParseException();
            }
            this.value = (byte)(in.read() & 0x01);
        } catch (IOException ioe) {
            throw new ParseException(ioe);
        }
    }

    @Override
    public void encode(final OutputStream os, final boolean removeSpc, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        try {
            os.write(1);
            os.write(this.value & 0x01);
        } catch (IOException ioe) {
            throw new ParseException(ioe);
        }
    }

    @Override
    public void decode(final byte[] b, final ParameterFactory factory, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        if (b.length < 1) {
            throw new ParseException();
        }
        this.value = (byte)(b[0] & 0x01);

    }

    @Override
    public byte[] encode(final boolean removeSpc, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        return new byte[] { (byte) (this.value & 0x01) };
    }
}
