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

package org.mobicents.protocols.ss7.sccp;

import java.io.Serializable;
import org.mobicents.protocols.ss7.sccp.message.SccpMessage;

/**
 * 
 * @author Oleg Kulikov
 * @author baranowb
 */
public interface SccpListener extends Serializable {

	/**
	 * Called when proper data is received, it is partially decoded. This method
	 * is called with message payload.
	 * 
	 * @param message
	 *            Message received
	 * @param seqControl
	 *            The SeqControl value between 0 to 31 indicating the SLS over
	 *            which message is received
	 */
	public void onMessage(SccpMessage message, int seqControl);
}
