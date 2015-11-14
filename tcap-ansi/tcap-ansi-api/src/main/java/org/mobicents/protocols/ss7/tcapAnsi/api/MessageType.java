/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.mobicents.protocols.ss7.tcapAnsi.api;

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

    Unidirectional(1), QueryWithPerm(2), QueryWithoutPerm(3), Response(4),
    ConversationWithPerm(6), ConversationWithoutPerm(7), Abort(22), Unknown(-1);

    private int tagContent = -1;

    private MessageType(int tagContent) {
        this.tagContent = tagContent;
    }

    public MessageType decode(AsnInputStream asnIs) throws IOException {
        int t = asnIs.readTag();
        switch (t) {
            case 1:
                return Unidirectional;
            case 2:
                return QueryWithPerm;
            case 3:
                return QueryWithoutPerm;
            case 4:
                return Response;
            case 5:
                return ConversationWithPerm;
            case 6:
                return ConversationWithoutPerm;
            case 22:
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
