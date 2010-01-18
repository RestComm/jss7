/*
 * The Java Call Control API for CAMEL 2
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */

package org.mobicents.ss7.sccp.ud;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.mobicents.ss7.sccp.parameter.ProtocolClass;
import org.mobicents.ss7.sccp.parameter.SccpAddress;

/**
 * 
 * @author Oleg Kulikov
 */
public interface UDBase {

	public byte[] getData();

	public void setData(byte[] data);

	public void setCalledParty(SccpAddress calledParty);

	public void setCallingParty(SccpAddress callingParty);

	public SccpAddress getCalledParty();

	public SccpAddress getCallingParty();

	public ProtocolClass getpClass();

	public void setpClass(ProtocolClass pClass);
	
	public void decode(InputStream in) throws IOException;

	public void encode(OutputStream out) throws IOException;

}
