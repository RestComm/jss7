package org.mobicents.ss7.management.transceiver;

import java.nio.ByteBuffer;

/**
 * Represents the Shell Command protocol data unit
 * 
 * @author amit bhayani
 * 
 */
public class Message {
    private byte[] data = null;

    protected Message() {

    }

    protected Message(byte[] data) {
        this.data = data;
    }

    /**
     * Decodes the received byte[]
     * 
     * @param data
     */
    protected void decode(byte[] data) {
        this.data = data;
    }

    /**
     * Encodes this message. The protocol is first 4 bytes are length of this
     * command followed by the byte stream of command
     * 
     * @param txBuffer
     */
    protected void encode(ByteBuffer txBuffer) {
        txBuffer.position(4); // Length int
        txBuffer.put(data);

        int length = txBuffer.position();

        txBuffer.rewind();

        txBuffer.putInt(length);

        txBuffer.position(length);
    }

    @Override
    public String toString() {
        return new String(data);
    }
}
