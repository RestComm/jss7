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

package org.mobicents.ss7.hardware.dahdi;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.mobicents.protocols.ss7.mtp.Mtp1;
import org.mobicents.protocols.stream.api.SelectorKey;
import org.mobicents.protocols.stream.api.SelectorProvider;
import org.mobicents.protocols.stream.api.StreamSelector;

/**
 * Implementation for {@link org.mobicents.protocols.ss7.mtp.Mtp1}. Opens the channel (JNI) and works with underlying dahdi link
 *
 * @author kulikov
 * @author baranowb
 * @author amit bhayani
 */
public class Channel implements Mtp1 {

    private static final String LIB_NAME = "mobicents-dahdi-linux";

    static {
        try {
            System.loadLibrary(LIB_NAME);
            System.out.println("Loaded library mobicents-dahdi-linux");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int span;
    private int channelID;
    private int code;
    private String linkName;
    private int ioBufferSize = 32; // lets default to 32, it seems optimal.
    protected int fd;

    private Object link;

    protected SelectorKey selectorKey;

    public Channel() {
    }

    public int getSpan() {
        return span;
    }

    public void setSpan(int span) {
        this.span = span;
    }

    public int getChannelID() {
        return channelID;
    }

    public void setChannelID(int channelID) {
        this.channelID = channelID;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLinkName() {
        return linkName;
    }

    public int getIOBufferSize() {
        return ioBufferSize;
    }

    public void setIOBufferSize(int bufferSize) {
        this.ioBufferSize = bufferSize;
    }

    public void open() {
        int zapid = 31 * (span - 1) + channelID;
        fd = openChannel(zapid, ioBufferSize);
    }

    /**
     * Opens this channel and prepares it for reading.
     *
     * @param id - id of zap device, its appended at the end of path, for instance: /dev/dahdi/${id}
     * @param bufferSize - size of buffer to be used for I/O ops.
     */
    public native int openChannel(int id, int bufferSize);

    /**
     * Reads data from this pipe.
     *
     * @param buffer the byte buffer to read data.
     * @return the number of bytes actualy read.
     */
    public int read(byte[] buffer) throws IOException {
        int result = readData(fd, buffer, ioBufferSize);
        if (result == -1) {
            doUnregister(fd);
            close();
            int zapid = 31 * (span - 1) + channelID;
            fd = openChannel(zapid, ioBufferSize);
            doRegister(fd);

            return 0;
        }

        return result;
    }

    public native int readData(int fd, byte[] buffer, int bufferSize);

    /**
     * Writes specified data to the pipe.
     *
     * @param buffer the buffer with data to write
     * @param len the length of the buffer.
     */
    public int write(byte[] buffer) throws IOException {
        writeData(fd, buffer, buffer.length);
        return buffer.length; // ?
    }

    public native void writeData(int fd, byte[] buffer, int len);

    /**
     * Registers pipe for polling.
     *
     * @param fd the file descriptor.
     */
    public native void doRegister(int fd);

    /**
     * Unregisters pipe from polling.
     *
     * @param fd the file descriptor.
     */
    public native void doUnregister(int fd);

    public void close() {
        closeChannel(fd);
    }

    /**
     * Closes this pipe.
     */
    public native void closeChannel(int fd);

    @Override
    public String toString() {
        return Integer.toString(channelID);
    }

    public void setLink(Object link) {
        this.link = link;
    }

    public Object getLink() {
        return this.link;
    }

    protected void doRegister(StreamSelector selector) {
        doRegister(fd);
    }

    protected void doUnregister(StreamSelector selector) {
        doUnregister(fd);
    }

    public boolean isReadable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isWriteable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public SelectorProvider provider() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public SelectorKey register(StreamSelector selector) throws IOException {
        return ((Selector) selector).register(this);
    }

    public void write(byte[] data, int len) throws IOException {
        this.writeData(fd, data, len);
    }

    public int read(ByteBuffer arg0) throws IOException {
        // TODO Auto-generated method stub
        return 0;
    }

    public int write(ByteBuffer arg0) throws IOException {
        // TODO Auto-generated method stub
        return 0;
    }

}