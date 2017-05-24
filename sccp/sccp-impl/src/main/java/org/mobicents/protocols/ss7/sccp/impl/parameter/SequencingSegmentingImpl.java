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
import org.mobicents.protocols.ss7.sccp.parameter.SequencingSegmenting;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SequencingSegmentingImpl extends AbstractParameter implements SequencingSegmenting {

    private byte sendSequenceNumber;
    private byte receiveSequenceNumber;
    private boolean moreData;

    public SequencingSegmentingImpl() {
    }

    public SequencingSegmentingImpl(int sendSequenceNumber, int receiveSequenceNumber, boolean moreData) {
        this.sendSequenceNumber = (byte)sendSequenceNumber;
        this.receiveSequenceNumber = (byte)receiveSequenceNumber;
        this.moreData = moreData;
    }

    @Override
    public int getSendSequenceNumber() {
        return sendSequenceNumber;
    }

    @Override
    public int getReceiveSequenceNumber() {
        return receiveSequenceNumber;
    }

    @Override
    public boolean isMoreData() {
        return false;
    }

    @Override
    public void decode(final InputStream in, final ParameterFactory factory, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        try {
            if (in.read() != 2) {
                throw new ParseException();
            }
            this.sendSequenceNumber = (byte)(in.read() >> 1 & 0x7F);
            int secondOctet = in.read();
            this.receiveSequenceNumber = (byte)(secondOctet >> 1 & 0x7F);
            this.moreData = (secondOctet & 0x01) == 1;
        } catch (IOException ioe) {
            throw new ParseException(ioe);
        }
    }

    @Override
    public void encode(final OutputStream os, final boolean removeSpc, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        try {
            os.write(2);
            os.write(this.sendSequenceNumber << 1 & 0xFE);
            os.write(this.receiveSequenceNumber << 1 & 0xFE | ((moreData) ? 1 : 0));
        } catch (IOException ioe) {
            throw new ParseException(ioe);
        }
    }

    @Override
    public void decode(final byte[] b, final ParameterFactory factory, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        if (b.length < 2) {
            throw new ParseException();
        }
        this.sendSequenceNumber = (byte)(b[0] >> 1 & 0x7F);
        this.receiveSequenceNumber = (byte)(b[1] >> 1 & 0x7F);
        this.moreData = (b[1] & 0x01) == 1;
    }

    @Override
    public byte[] encode(final boolean removeSpc, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        return new byte[] {
                (byte) (this.sendSequenceNumber << 1 & 0xFE),
                (byte) (this.receiveSequenceNumber << 1 & 0xFE | ((moreData) ? 1 : 0))
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SequencingSegmentingImpl that = (SequencingSegmentingImpl) o;

        if (sendSequenceNumber != that.sendSequenceNumber) return false;
        if (receiveSequenceNumber != that.receiveSequenceNumber) return false;
        return moreData == that.moreData;

    }

    @Override
    public int hashCode() {
        int result = (int) sendSequenceNumber;
        result = 31 * result + (int) receiveSequenceNumber;
        result = 31 * result + (moreData ? 1 : 0);
        return result;
    }
}
