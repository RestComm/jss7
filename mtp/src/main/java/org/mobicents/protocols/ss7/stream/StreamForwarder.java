package org.mobicents.protocols.ss7.stream;

import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.mobicents.protocols.ss7.mtp.Mtp3;





/**
 * Interface for forwarding classes
 * @author baranowb
 *
 */
public interface StreamForwarder {

	/**
	 * Set local port. 
	 * @param port
	 */
	public void setLocalPort(int port);
	public int getLocalPort();
	/**
	 * Set local address
	 * @param address
	 * @throws UnknownHostException
	 */
	public void setLocalAddress(String address) throws UnknownHostException;
	public String getLocalAddress();
	
	/**
	 * Set local port. 
	 * @param port
	 */
	public void setRemotePort(int port);
	public int getRemotePort();
	/**
	 * Set local address
	 * @param address
	 * @throws UnknownHostException
	 */
	public void setRemoteAddress(String address) throws UnknownHostException;
	public String getRemoteAddress();
	
	
	/**
	 * Set reference to hook allowing local resouce to intercept MSUs before streaming.
	 * @param ih
	 */
	public void setInterceptorHook(InterceptorHook ih);
	
	/**
	 * Start server
	 * @throws Exception
	 */
	public void start() throws Exception;
	/**
	 * Stop server
	 */
	public void stop();

	/**
	 * Performs send op of this data. It directly streams it. How this method acts depends on impl. 
	 */
	public void streamData(ByteBuffer data);
	
	//those method will propably change
	
	public void setMtp3(Mtp3 mtp);
	
	public void addMTPListener(MTPListener lst);
	
	public void removeMTPListener(MTPListener lst);
	
	//junit method
	public LinkStateProtocol getLinkStateProtocol();
	
}
