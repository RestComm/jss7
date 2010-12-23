package org.mobicents.ss7.management.transceiver;

import java.nio.ByteBuffer;

public class Message {
	private byte[] data = null;

	protected Message(byte[] data) {
		this.data = data;

	}

	protected void encode(ByteBuffer txBuffer) {
		txBuffer.put(data);
	}

	@Override
	public String toString() {
		return new String(data);
	}
}
