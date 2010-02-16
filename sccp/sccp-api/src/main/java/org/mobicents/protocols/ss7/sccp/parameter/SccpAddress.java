/**
 * 
 */
package org.mobicents.protocols.ss7.sccp.parameter;

import java.io.IOException;

/**
 * @author baranowb
 * 
 */
public interface SccpAddress {

	byte[] encode() throws IOException;

	void decode(byte[] buffer) throws IOException;

	public int getPointCodeIndicator();

	public void setPointCodeIndicator(int pointCodeIndicator);

	public int getSsnIndicator();

	public void setSsnIndicator(int ssnIndicator);

	public int getGlobalTitleIndicator();

	public void setGlobalTitleIndicator(int globalTitleIndicator);

	public int getRoutingIndicator();

	public void setRoutingIndicator(int routingIndicator);

	public int getSignalingPointCode();

	public void setSignalingPointCode(int signalingPointCode);

	public int getSsn();

	public void setSsn(int ssn);

	public GlobalTitle getGlobalTitle();

	public void setGlobalTitle(GlobalTitle globalTitle);
}
