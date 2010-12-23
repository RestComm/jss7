package org.mobicents.ss7.management.transceiver;

import java.nio.ByteBuffer;

public class Message {
    private byte[] data = null;
        

    protected Message() {

    }
    
    protected Message(byte[] data) {
        this.data = data;
    }


    protected void decode(byte[] data) {
        this.data = data;
    }

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
