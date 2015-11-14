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

package org.mobicents.protocols.ss7.map.smstpdu;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.smstpdu.CommandData;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CommandDataImpl implements CommandData {

    private byte[] encodedData;
    private String decodedMessage;

    private boolean isDecoded;
    private boolean isEncoded;

    public CommandDataImpl(byte[] data) {
        this.encodedData = data;

        this.isEncoded = true;
    }

    public CommandDataImpl(String decodedMessage) {
        this.decodedMessage = decodedMessage;

        this.isDecoded = true;
    }

    public byte[] getEncodedData() {
        return this.encodedData;
    }

    public String getDecodedMessage() {
        return decodedMessage;
    }

    public void encode() throws MAPException {

        if (this.isEncoded)
            return;
        this.isEncoded = true;

        this.encodedData = null;

        if (this.decodedMessage == null)
            this.decodedMessage = "";

        // TODO: what is an encoding algorithm ?
        Charset chs = Charset.forName("US-ASCII");
        ByteBuffer bb = chs.encode(this.decodedMessage);
        this.encodedData = new byte[bb.limit()];
        bb.get(this.encodedData);
    }

    public void decode() throws MAPException {

        if (this.isDecoded)
            return;
        this.isDecoded = true;

        this.decodedMessage = null;

        if (this.encodedData == null)
            throw new MAPException("Error decoding a text from Sms CommandData: encodedData field is null");

        // TODO: what is an encoding algorithm ?
        Charset chs = Charset.forName("US-ASCII");
        byte[] buf = this.encodedData;
        ByteBuffer bb = ByteBuffer.wrap(buf);
        CharBuffer bf = chs.decode(bb);
        this.decodedMessage = bf.toString();
    }

    private String printDataArr(byte[] arr) {
        if (arr == null)
            return "null";

        StringBuilder sb = new StringBuilder();
        for (int b : arr) {
            sb.append(b);
            sb.append(", ");
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("TP-Command-Data [");
        if (this.decodedMessage == null) {
            if (this.encodedData != null)
                sb.append(printDataArr(this.encodedData));
        } else {
            sb.append("Msg:[");
            sb.append(this.decodedMessage);
            sb.append("]");
        }
        sb.append("]");

        return sb.toString();
    }
}
