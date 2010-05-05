package org.mobicents.protocols.ss7.stream;

import java.io.IOException;

public interface MTPProvider {

	public void addMtpListener(MTPListener lst);

	public void removeMtpListener(MTPListener lst);

	public void send(byte[] msg) throws IOException;

	public void stop() throws IllegalStateException;
}
