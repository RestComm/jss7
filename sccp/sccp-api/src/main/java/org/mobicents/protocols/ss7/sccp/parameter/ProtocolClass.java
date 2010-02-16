/**
 * 
 */
package org.mobicents.protocols.ss7.sccp.parameter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author baranowb
 * 
 */
public interface ProtocolClass {

	void encode(OutputStream out) throws IOException;

	void decode(InputStream in) throws IOException;

	public int getpClass();

	public void setpClass(int pClass);

	public int getMsgHandling();

	public void setMsgHandling(int msgHandling);
}
