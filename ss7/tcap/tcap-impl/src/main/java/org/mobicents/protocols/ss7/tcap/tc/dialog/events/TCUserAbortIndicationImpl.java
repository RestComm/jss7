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
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortIndication;
import org.mobicents.protocols.ss7.tcap.asn.AbortSource;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.ResultSourceDiagnostic;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;

public class TCUserAbortIndicationImpl extends DialogIndicationImpl implements
		TCUserAbortIndication {
	//fields_1
	//private External abortReason;
	// fields_2 
	//private ApplicationContextName applicationContextName;
	private UserInformation userInformation;
	private AbortSource abortSource;
	private ApplicationContextName acn;
	private ResultSourceDiagnostic resultSourceDiagnostic;
	private Boolean aareApdu = false;
	private Boolean abrtApdu = false;
	
	//XXX: fields_1 and fields_2 are mutually exclusive!
	TCUserAbortIndicationImpl() {
		super(EventType.UAbort);
		// TODO Auto-generated constructor stub
	}

	//public External getAbortReason() {
	//	
	//	return abortReason;
	//}
	
	public Boolean IsAareApdu() {
		return this.aareApdu; 
	}
	
	public void SetAareApdu() {
		this.aareApdu = true; 
	}
	
	public Boolean IsAbrtApdu() {
		return this.abrtApdu; 
		
	}
	
	public void SetAbrtApdu() {
		this.abrtApdu = true; 
	}


	public UserInformation getUserInformation() {
		
		return userInformation;
	}

	/**
	 * @param userInformation the userInformation to set
	 */
	public void setUserInformation(UserInformation userInformation) {
		this.userInformation = userInformation;
	}
	
	/**
	 * @return the abortSource
	 */
	public AbortSource getAbortSource() {
		return abortSource;
	}

	public void setAbortSource(AbortSource abortSource) {
		this.abortSource = abortSource;
		
	}

	public ApplicationContextName getApplicationContextName() {
		return this.acn;
	}

	public void setApplicationContextName( ApplicationContextName acn ) {
		this.acn = acn;
	}

	public ResultSourceDiagnostic getResultSourceDiagnostic() {
		return this.resultSourceDiagnostic;
	}
	
	public void setResultSourceDiagnostic( ResultSourceDiagnostic resultSourceDiagnostic ) {
		this.resultSourceDiagnostic = resultSourceDiagnostic;
	}

}
