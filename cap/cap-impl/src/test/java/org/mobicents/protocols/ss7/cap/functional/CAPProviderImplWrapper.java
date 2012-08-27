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

package org.mobicents.protocols.ss7.cap.functional;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.ss7.cap.CAPProviderImpl;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPServiceBase;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementary;
import org.mobicents.protocols.ss7.tcap.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;

/**
 * 
 * @author amit bhayani
 * @author sergey vetyutnev
 * 
 */
public class CAPProviderImplWrapper extends CAPProviderImpl {

	private int testMode = 0;

//	private final MAPServiceSupplementary mapServiceSupplementaryTest = new MAPServiceSupplementaryImplWrapper(this);

	public CAPProviderImplWrapper(TCAPProvider tcapProvider) {
		super(tcapProvider);
		
		for(CAPServiceBase serv : this.capServices) {
			if( serv instanceof MAPServiceSupplementary ) {
				this.capServices.remove(serv);
				break;
			}
		}
		
//		this.capServices.add(this.mapServiceSupplementaryTest);
	}

//	public MAPServiceSupplementary getMAPServiceSupplementary() {
//		return this.mapServiceSupplementaryTest;
//	}

	public void setTestMode(int testMode) {
		this.testMode = testMode;
	}

	public TCBeginRequest encodeTCBegin(Dialog tcapDialog, ApplicationContextName acn, CAPGprsReferenceNumber gprsReferenceNumber) throws CAPException {
		return super.encodeTCBegin(tcapDialog, acn, gprsReferenceNumber);
	}
	
	public void onTCBegin(TCBeginIndication tcBeginIndication) {
		if (this.testMode == 1) {
			try {
				byte[] data = tcBeginIndication.getUserInformation().getEncodeType();
				data[0] = 0;
			} catch (AsnException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		super.onTCBegin(tcBeginIndication);
	}
}

