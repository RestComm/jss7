package org.mobicents.protocols.ss7.stream;

import java.io.IOException;

import org.mobicents.protocols.ss7.mtp.Mtp3Listener;
import org.mobicents.protocols.ss7.stream.tcp.StartFailedException;

/**
 * Itnerface for mtp3 provider classes. This class abstracts how MTP3 is
 * accessed. User should not access mtp3 directly. Rather use provider classes.
 * 
 * @author baranowb
 * 
 */
public interface MTPProvider {

	/**
	 * Add MTPListener for callbacks
	 * 
	 * @param lst
	 */
	public void addMtp3Listener(Mtp3Listener lst);

	/**
	 * Remote listener
	 * 
	 * @param lst
	 */
	public void removeMtp3Listener(Mtp3Listener lst);

	/**
	 * Push data down the stream - it expects properly formed Mtp3 message
	 * 
	 * @param msg
	 * @throws IOException
	 */
	public void send(byte[] msg) throws IOException;

	/**
	 * Stop provider.
	 * 
	 * @throws IllegalStateException
	 */
	public void stop() throws IllegalStateException;

	/**
	 * Start provider
	 * 
	 * @return
	 * @throws StartFailedException
	 * @throws IllegalStateException
	 */
	public void start() throws StartFailedException, IllegalStateException;
}
