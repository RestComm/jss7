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

package org.mobicents.protocols.ss7.tcap.tc.dialog.events;

import org.mobicents.protocols.asn.External;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.AbortReason;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.EventType;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortRequest;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;

/**
 * 
 * @author amit bhayani
 * 
 */
public class TCUserAbortRequestImpl extends DialogRequestImpl implements
		TCUserAbortRequest {

	private Byte qos;

	// fields
	private ApplicationContextName applicationContextName;
	private UserInformation userInformation;

	private External abortReason;

	TCUserAbortRequestImpl() {
		super(EventType.UAbort);
		// TODO Auto-generated constructor stub
	}
	
	//public External getAbortReason() {
	//	return this.abortReason;
	//}

	public ApplicationContextName getApplicationContextName() {
		return this.applicationContextName;
	}

	public Byte getQOS() {
		return qos;
	}

	public UserInformation getUserInformation() {
		return this.userInformation;
	}

	//public void setAbortReason(External abortReason) {
	//	this.abortReason = abortReason;
	//}

	public void setApplicationContextName(ApplicationContextName acn) {
		this.applicationContextName = acn;
	}

	public void setQOS(Byte b) throws IllegalArgumentException {
		this.qos = b;
	}

	public void setUserInformation(UserInformation userInformation) {
		this.userInformation = userInformation;

	}

}
