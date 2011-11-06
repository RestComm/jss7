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

package org.mobicents.protocols.ss7.cap.service.sms;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.cap.CAPDialogImpl;
import org.mobicents.protocols.ss7.cap.CAPProviderImpl;
import org.mobicents.protocols.ss7.cap.CAPServiceBaseImpl;
import org.mobicents.protocols.ss7.cap.api.CAPApplicationContext;
import org.mobicents.protocols.ss7.cap.api.CAPDialog;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.service.sms.CAPDialogSms;
import org.mobicents.protocols.ss7.cap.api.service.sms.CAPServiceSms;
import org.mobicents.protocols.ss7.cap.api.service.sms.CAPServiceSmsListener;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.CAPServiceCircuitSwitchedCallImpl;
import org.mobicents.protocols.ss7.map.api.dialog.ServingCheckData;
import org.mobicents.protocols.ss7.map.api.dialog.ServingCheckResult;
import org.mobicents.protocols.ss7.map.dialog.ServingCheckDataImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class CAPServiceSmsImpl extends CAPServiceBaseImpl implements CAPServiceSms {

	protected Logger loger = Logger.getLogger(CAPServiceSmsImpl.class);

	public CAPServiceSmsImpl(CAPProviderImpl capProviderImpl) {
		super(capProviderImpl);
	}

	@Override
	public CAPDialogSms createNewDialog(CAPApplicationContext appCntx, SccpAddress origAddress, SccpAddress destAddress) throws CAPException {

		// We cannot create a dialog if the service is not activated
		if (!this.isActivated())
			throw new CAPException(
					"Cannot create CAPDialogSms because CAPServiceSms is not activated");

		Dialog tcapDialog = this.createNewTCAPDialog(origAddress, destAddress);
		CAPDialogSmsImpl dialog = new CAPDialogSmsImpl(appCntx, tcapDialog, this.capProviderImpl, this);

		this.putCAPDialogIntoCollection(dialog);

		return dialog;
	}

	@Override
	public void addCAPServiceListener(CAPServiceSmsListener capServiceListener) {
		super.addCAPServiceListener(capServiceListener);
	}

	@Override
	public void removeCAPServiceListener(CAPServiceSmsListener capServiceListener) {
		super.removeCAPServiceListener(capServiceListener);
	}

	@Override
	protected CAPDialogImpl createNewDialogIncoming(CAPApplicationContext appCntx, Dialog tcapDialog) {
		return new CAPDialogSmsImpl(appCntx, tcapDialog, this.capProviderImpl, this);
	}

	@Override
	public ServingCheckData isServingService(CAPApplicationContext dialogApplicationContext) {
		switch (dialogApplicationContext) {
		case CapV3_cap3_sms:
		case CapV4_cap4_sms:
			return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
		}
		
		return new ServingCheckDataImpl(ServingCheckResult.AC_NotServing);
	}

	@Override
	public void processComponent(ComponentType compType, OperationCode oc, Parameter parameter, CAPDialog capDialog, Long invokeId, Long linkedId)
			throws CAPParsingComponentException {
		// TODO Auto-generated method stub
		
	}

}
