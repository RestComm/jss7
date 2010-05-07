package org.mobicents.protocols.ss7.stream;

import java.net.UnknownHostException;

import org.mobicents.protocols.ss7.mtp.Mtp3;





/**
 * Interface for forwarding classes(TCP/SCTP)
 * @author baranowb
 *
 */
public interface StreamForwarder {

	public void setPort(int port);
	public int getPort();
	public void setAddress(String address) throws UnknownHostException;
	public String getAddress();
	
	public void setMtp3(Mtp3 layer3);
	public void start() throws Exception;
	public void stop();

}
