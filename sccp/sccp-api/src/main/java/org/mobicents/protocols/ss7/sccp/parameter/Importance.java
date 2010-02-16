/**
 * 
 */
package org.mobicents.protocols.ss7.sccp.parameter;

import java.io.IOException;

/**
 * @author baranowb
 * 
 */
public interface Importance {

	public static final byte _PARAMETER_CODE = 0x12;

	byte[] encode() throws IOException;

	void decode(byte[] buffer) throws IOException;

	public byte getImportance();

	public void setImportance(byte importance);
}
