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

package org.restcomm.protocols.ss7.tcap.api;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * Message type tag - it holds whole representation of tag - along with universal and other bits set.
 *
 * @author baranowb
 *
 */
public enum MessageType {

    Unidirectional(0x61), Begin(0x62), End(0x64), Continue(0x65), Abort(0x67), Unknown(-1);

    private int tagContent = -1;

    private MessageType(int tagContent) {
        this.tagContent = tagContent;
    }

    public MessageType decode(AsnInputStream asnIs) throws IOException {
        int t = asnIs.readTag();
        switch (t) {
            case 0x61:
                return Unidirectional;
            case 0x62:
                return Begin;
            case 0x65:
                return Continue;
            case 0x64:
                return End;
            case 0x67:
                return Abort;
            default:
                return Unknown;
        }

    }

    public void encode(AsnOutputStream asnO) {
        // write directly, we know its applciation class, constructed and num is
        // in range of 5 bits
        // this way its faster.
        asnO.write(tagContent);
    }

    public int getTag() {
        return this.tagContent;
    }
}
