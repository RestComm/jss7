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

package org.mobicents.protocols.ss7.sccp;

import java.io.IOException;

import org.mobicents.protocols.ss7.mtp.ActionReference;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * 
 * @author Oleg Kulikov
 * @author baranowb
 */
public interface SccpProvider {
	
	/**
	 * Sets sccp listener
	 * @param listener
	 */
	public void setSccpListener(SccpListener listener);
	/**
	 * Retrieves listener
	 * @return
	 */
	public SccpListener getListener();
	/**
	 * Removes listener
	 */
	public void removeListener();
	
	/**
	 * Send sccp byte[] to desired addres.
	 * @param calledParty - destination address of this message
	 * @param callingParty - local address
	 * @param data - byte[] encoded of sccp parameters
	 * @param ar - reference with mtp3 routing label
	 * @throws IOException
	 */
	public void send(SccpAddress calledParty, SccpAddress callingParty,
			byte[] data,ActionReference ar) throws IOException;

	public void shutdown();

	public SccpUnitDataFactory getUnitDataFactory();

	public SccpParameterFactory getSccpParameterFactory();
}
