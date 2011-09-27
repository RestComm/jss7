/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
