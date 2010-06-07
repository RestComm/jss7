package org.mobicents.protocols.ss7.stream;

import java.net.UnknownHostException;

import org.mobicents.protocols.ss7.mtp.Mtp3;





/**
 * Interface for forwarding classes(TCP/SCTP)
 * @author baranowb
 *
 */
public interface StreamForwarder {

	/**
	 * Set port to listen for incoming connections
	 * @param port
	 */
	public void setPort(int port);
	public int getPort();
	/**
	 * Set address to listen for incoming connections
	 * @param address
	 * @throws UnknownHostException
	 */
	public void setAddress(String address) throws UnknownHostException;
	public String getAddress();
	
	/**
	 * Set layer3 - this is callback method for MTP class.
	 * @param layer3
	 */
	public void setMtp3(Mtp3 layer3);
	
	/**
	 * Start server
	 * @throws Exception
	 */
	public void start() throws Exception;
	/**
	 * Stop server
	 */
	public void stop();

}
