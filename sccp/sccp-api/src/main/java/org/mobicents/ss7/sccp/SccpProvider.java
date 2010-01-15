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

package org.mobicents.ss7.sccp;

import java.io.IOException;

import org.mobicents.ss7.sccp.parameter.SccpAddress;

/**
 * 
 * @author Oleg Kulikov
 * @author baranowb
 */
public interface SccpProvider {

	public void setSccpListener(SccpListener listener);

	public SccpListener getListener();

	public void removeListener();

	//FIXME: add indication which unit data?
	public void send(SccpAddress calledParty, SccpAddress callingParty,
			byte[] data) throws IOException;

	public void shutdown();

	public SccpUnitDataFactory getUnitDataFactory();

	public SccpParameterFactory getSccpParameterFactory();
}
