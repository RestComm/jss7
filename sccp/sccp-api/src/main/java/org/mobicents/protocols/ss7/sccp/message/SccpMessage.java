/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */

package org.mobicents.protocols.ss7.sccp.message;

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * Specifies the SCCP messages formats and codes for the support of
 * connection-oriented services, connectionless services and the management of
 * SCCP.
 * 
 * A SCCP message consists of the following parts:
 * <ul>
 * <li>the message type code;</li>
 * <li>the mandatory fixed part;</li>
 * <li>the mandatory variable part;</li>
 * <li>the optional part, which may contain fixed length and variable length
 * fields.</li>
 * </ul>
 * 
 * @author baranowb
 * @author kulikov
 */
public interface SccpMessage {
	/**
	 * The message type code consists of a one octet field and is mandatory for
	 * all messages. The message type code uniquely defines the function and
	 * format of each SCCP message.
	 * 
	 * @return message type code
	 */
	public int getType();

	//public ProtocolClass getProtocolClass(); //FIXME check if this is present in others, think it is.

	public SccpAddress getCalledPartyAddress();

	public SccpAddress getCallingPartyAddress();
	
//	public void setProtocolClass(ProtocolClass v); //FIXME check if this is present in others, think it is.
//
	public void setCalledPartyAddress(SccpAddress v);

	public void setCallingPartyAddress(SccpAddress v);
}
