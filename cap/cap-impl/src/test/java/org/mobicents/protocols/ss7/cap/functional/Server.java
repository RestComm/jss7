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

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.cap.api.CAPDialog;
import org.mobicents.protocols.ss7.cap.api.CAPDialogListener;
import org.mobicents.protocols.ss7.cap.api.CAPParameterFactory;
import org.mobicents.protocols.ss7.cap.api.CAPProvider;
import org.mobicents.protocols.ss7.cap.api.CAPStack;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPComponentErrorReason;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPGeneralAbortReason;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPUserAbortReason;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessage;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ApplyChargingReportRequestIndication;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ApplyChargingRequestIndication;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPServiceCircuitSwitchedCallListener;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CallInformationReportRequestIndication;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CallInformationRequestRequestIndication;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ConnectRequestIndication;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ContinueRequestIndication;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.EventReportBCSMRequestIndication;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.InitialDPRequestIndication;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ReleaseCallRequestIndication;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.RequestReportBCSMEventRequestIndication;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class Server implements CAPDialogListener, CAPServiceCircuitSwitchedCallListener  {

	private static Logger logger = Logger.getLogger(Server.class);

	private CAPFunctionalTest runningTestCase;
	private SccpAddress thisAddress;
	private SccpAddress remoteAddress;

	private CAPStack capStack;
	private CAPProvider capProvider;

	private CAPParameterFactory capParameterFactory;

	private boolean _S_recievedDialogRequest;
	private boolean _S_recievedInitialDp;

	private int dialogStep;
	private long savedInvokeId;
	private String unexpected = "";

	private FunctionalTestScenario step;

	Server(CAPStack capStack, CAPFunctionalTest runningTestCase, SccpAddress thisAddress, SccpAddress remoteAddress) {
		super();
		this.capStack = capStack;
		this.runningTestCase = runningTestCase;
		this.thisAddress = thisAddress;
		this.remoteAddress = remoteAddress;
		this.capProvider = this.capStack.getCAPProvider();

		this.capParameterFactory = this.capProvider.getCAPParameterFactory();

		this.capProvider.addCAPDialogListener(this);
		this.capProvider.getCAPServiceCircuitSwitchedCall().addCAPServiceListener(this);

		this.capProvider.getCAPServiceCircuitSwitchedCall().acivate();
		this.capProvider.getCAPServiceSms().acivate();
		this.capProvider.getCAPServiceSms().acivate();
	}

	public boolean isFinished() {

		switch( this.step ) {
		case Action_InitilDp:
			return _S_recievedDialogRequest && _S_recievedInitialDp;
		}
		
		return false;
	}

	public String getStatus() {
		String status = "Scenario: " + this.step + "\n";

		switch( this.step ) {
		case Action_InitilDp:
			status += "_S_recievedDialogRequest[" + _S_recievedDialogRequest + "]" + "\n";
			status += "_S_recievedInitialDp[" + _S_recievedInitialDp + "]" + "\n";
			break;
		}

		return status + "\n" + unexpected;
	}
	
	public void reset() {
		this._S_recievedDialogRequest = false;
		this._S_recievedInitialDp = false;

		this.dialogStep = 0;
		this.unexpected = "";
	}

	public void setStep (FunctionalTestScenario step) {
		this.step = step;
	}

	
	
	@Override
	public void onDialogDelimiter(CAPDialog capDialog) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogRequest(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
		
		switch( this.step ) { 
		case Action_InitilDp:
			this._S_recievedDialogRequest = true;
			break;
		}
	}

	@Override
	public void onDialogAccept(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogUserAbort(CAPDialog capDialog, CAPGeneralAbortReason generalReason, CAPUserAbortReason userReason) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogProviderAbort(CAPDialog capDialog, PAbortCauseType abortCause) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogClose(CAPDialog capDialog) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogResease(CAPDialog capDialog) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogTimeout(CAPDialog capDialog) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogNotice(CAPDialog capDialog, CAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
		// TODO Auto-generated method stub
		
	}

	
	
	@Override
	public void onErrorComponent(CAPDialog capDialog, Long invokeId, CAPErrorMessage capErrorMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRejectComponent(CAPDialog capDialog, Long invokeId, Problem problem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInvokeTimeout(CAPDialog capDialog, Long invokeId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderErrorComponent(CAPDialog mapDialog, Long invokeId, CAPComponentErrorReason providerError) {
		// TODO Auto-generated method stub
		
	}

	
	
	@Override
	public void onInitialDPRequestIndication(InitialDPRequestIndication ind) {
		
		switch( this.step ) { 
		case Action_InitilDp:
			if (Client.checkTestInitialDp(ind))
				this._S_recievedInitialDp = true;
			else
				unexpected = "Failure checking received InitialDP";
			break;
		}
	}

	@Override
	public void onRequestReportBCSMEventRequestIndication(RequestReportBCSMEventRequestIndication ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onApplyChargingRequestIndication(ApplyChargingRequestIndication ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEventReportBCSMRequestIndication(EventReportBCSMRequestIndication ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onContinueRequestIndication(ContinueRequestIndication ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onApplyChargingReportRequestIndication(ApplyChargingReportRequestIndication ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReleaseCalltRequestIndication(ReleaseCallRequestIndication ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectRequestIndication(ConnectRequestIndication ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCallInformationRequestRequestIndication(CallInformationRequestRequestIndication ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCallInformationReportRequestIndication(CallInformationReportRequestIndication ind) {
		// TODO Auto-generated method stub
		
	}

}
