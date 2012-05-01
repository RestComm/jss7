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

package org.mobicents.protocols.ss7.tcap.api.tc.dialog.events;

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;

public interface TCContinueRequest extends DialogRequest {

	/**
	 * Sets QOS optional parameter. Its passed to SCCP layer?
	 * 
	 * @param b
	 */
	public void setQOS(Byte b) throws IllegalArgumentException;

	public Byte getQOS();

	/**
	 * Sets origin address. This parameter is used only in first TCContinue,
	 * sent as response to TCBegin. This parameter, if set, changes local peer
	 * address(remote end will send request to value set by this method).
	 * 
	 * @return
	 */
	public SccpAddress getOriginatingAddress();

	public void setOriginatingAddress(SccpAddress dest);

	/**
	 * Application context name for this dialog.
	 * 
	 * @return
	 */
	public ApplicationContextName getApplicationContextName();

	public void setApplicationContextName(ApplicationContextName acn);

	/**
	 * User information for this dialog.
	 * 
	 * @return
	 */
	public UserInformation getUserInformation();

	public void setUserInformation(UserInformation acn);

}
