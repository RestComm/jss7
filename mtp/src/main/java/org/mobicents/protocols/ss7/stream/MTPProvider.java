package org.mobicents.protocols.ss7.stream;

import java.io.IOException;

import org.mobicents.protocols.ss7.stream.tcp.StartFailedException;

public interface MTPProvider {
	
	/**
	 * Add MTPListener for callbacks
	 * @param lst
	 */
	public void addMtpListener(MTPListener lst);
	/**
	 * Remote listener
	 * @param lst
	 */
	public void removeMtpListener(MTPListener lst);
	/**
	 * Push data down the stream - it expects properly formed Mtp3 message
	 * @param msg
	 * @throws IOException
	 */
	public void send(byte[] msg) throws IOException;

	/**
	 * Stop provider.
	 * @throws IllegalStateException
	 */
	public void stop() throws IllegalStateException;
	/**
	 * Start provider
	 * @return
	 * @throws StartFailedException
	 * @throws IllegalStateException
	 */
	public void start() throws StartFailedException,IllegalStateException;
}
