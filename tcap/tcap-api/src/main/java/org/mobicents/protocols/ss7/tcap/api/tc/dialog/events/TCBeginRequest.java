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

/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.api.tc.dialog.events;

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;

/**
 * @author baranowb
 *
 */
public interface TCBeginRequest extends DialogRequest {
	/**
	 * Sets QOS optional parameter. Its passed to lower layer
	 * @param b
	 */
	public void setQOS(Byte b) throws IllegalArgumentException;
	public Byte getQOS();

	//only getter, since we send via Dialog object, ID is ensured to be present.
	
	
	/**
	 * Destination address. If this address is different than one in dialog, this value will overwrite dialog value.
	 */
	public SccpAddress getDestinationAddress();
	public void setDestinationAddress(SccpAddress dest);
	/**
	 * Origin address. If this address is different than one in dialog, this value will overwrite dialog value.
	 */
	public SccpAddress getOriginatingAddress();
	public void setOriginatingAddress(SccpAddress dest);
	
	/**
	 * Application context name for this dialog. 
	 * @return
	 */
	public ApplicationContextName getApplicationContextName();
	public void setApplicationContextName(ApplicationContextName acn);
	/**
	 * User information for this dialog.
	 * @return
	 */
	public UserInformation getUserInformation();	
	public void setUserInformation(UserInformation acn);
	
	
}
