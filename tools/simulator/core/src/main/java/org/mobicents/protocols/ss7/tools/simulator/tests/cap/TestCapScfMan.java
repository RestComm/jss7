/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.mobicents.protocols.ss7.tools.simulator.tests.cap;

import org.apache.log4j.Level;
import org.mobicents.protocols.ss7.cap.api.CAPDialog;
import org.mobicents.protocols.ss7.cap.api.CAPDialogListener;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPMessage;
import org.mobicents.protocols.ss7.cap.api.CAPProvider;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPDialogState;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPGeneralAbortReason;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPUserAbortReason;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessage;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ActivityTestRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ActivityTestResponse;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ApplyChargingReportRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ApplyChargingRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.AssistRequestInstructionsRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPDialogCircuitSwitchedCall;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPServiceCircuitSwitchedCallListener;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CallInformationReportRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CallInformationRequestRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CancelRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ConnectRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ConnectToResourceRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ContinueRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectForwardConnectionRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.EstablishTemporaryConnectionRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.EventReportBCSMRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.FurnishChargingInformationRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.InitialDPRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.PlayAnnouncementRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.PromptAndCollectUserInformationRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.PromptAndCollectUserInformationResponse;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ReleaseCallRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.RequestReportBCSMEventRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ResetTimerRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.SendChargingInformationRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.SpecializedResourceReportRequest;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tools.simulator.Stoppable;
import org.mobicents.protocols.ss7.tools.simulator.common.CapApplicationContextScf;
import org.mobicents.protocols.ss7.tools.simulator.common.TesterBase;
import org.mobicents.protocols.ss7.tools.simulator.level3.CapMan;
import org.mobicents.protocols.ss7.tools.simulator.management.TesterHost;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class TestCapScfMan extends TesterBase implements TestCapScfManMBean, Stoppable, CAPDialogListener, CAPServiceCircuitSwitchedCallListener {

	public static String SOURCE_NAME = "TestCapScf";

	private final String name;
	private CapMan capMan;

	private boolean isStarted = false;
	private int countInitialDp = 0;
	private int countInitiateCallAttempt = 0;
	private int countAssistRequestInstructions = 0;
	private String currentRequestDef = "";
	private CAPDialogCircuitSwitchedCall currentDialog = null;

	public TestCapScfMan() {
		super(SOURCE_NAME);
		this.name = "???";
	}

	public TestCapScfMan(String name) {
		super(SOURCE_NAME);
		this.name = name;
	}

	public void setTesterHost(TesterHost testerHost) {
		this.testerHost = testerHost;
	}

	public void setCapMan(CapMan val) {
		this.capMan = val;
	}

	@Override
	public CapApplicationContextScf getCapApplicationContext() {
		return this.testerHost.getConfigurationData().getTestCapScfConfigurationData().getCapApplicationContext();
	}

	@Override
	public String getCapApplicationContext_Value() {
		return this.testerHost.getConfigurationData().getTestCapScfConfigurationData().getCapApplicationContext().toString();
	}

	@Override
	public void setCapApplicationContext(CapApplicationContextScf val) {
		this.testerHost.getConfigurationData().getTestCapScfConfigurationData().setCapApplicationContext(val);
		this.testerHost.markStore();
	}

	@Override
	public void putCapApplicationContext(String val) {
		CapApplicationContextScf x = CapApplicationContextScf.createInstance(val);
		if (x != null)
			this.setCapApplicationContext(x);
	}


	@Override
	public String getCurrentRequestDef() {
		return "LastDialog: " + currentRequestDef;
	}

	@Override
	public String getState() {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append(SOURCE_NAME);
		sb.append(": ");
		if (this.currentDialog != null) {
			sb.append(", curDialog: ");
			sb.append(this.currentDialog.getState());
		}
		sb.append("<br>Count: countInitialDp-");
		sb.append(countInitialDp);
		sb.append(", countInitiateCallAttempt-");
		sb.append(countInitiateCallAttempt);
		sb.append("<br>countAssistRequestInstructions-");
		sb.append(countAssistRequestInstructions);
		sb.append("</html>");
		return sb.toString();
	}

	public boolean start() {
		CAPProvider capProvider = this.capMan.getCAPStack().getCAPProvider();
		capProvider.getCAPServiceCircuitSwitchedCall().acivate();
		capProvider.getCAPServiceCircuitSwitchedCall().addCAPServiceListener(this);
		capProvider.addCAPDialogListener(this);
		this.testerHost.sendNotif(SOURCE_NAME, "CAP SCF has been started", "", Level.INFO);
		currentDialog = null;
		isStarted = true;

		return true;
	}

	@Override
	public void stop() {
		CAPProvider capProvider = this.capMan.getCAPStack().getCAPProvider();
		isStarted = false;
		capProvider.getCAPServiceCircuitSwitchedCall().deactivate();
		capProvider.getCAPServiceCircuitSwitchedCall().removeCAPServiceListener(this);
		capProvider.removeCAPDialogListener(this);
		this.testerHost.sendNotif(SOURCE_NAME, "CAP SCF has been stopped", "", Level.INFO);
	}

	@Override
	public void execute() {
	}

	@Override
	public String closeCurrentDialog() {
		if (!isStarted)
			return "The tester is not started";

		CAPDialogCircuitSwitchedCall curDialog = currentDialog;
		if (curDialog != null) {
			try {
				curDialog.close(false);
				this.doRemoveDialog();
				return "The current dialog has been closed";
			} catch (CAPException e) {
				this.doRemoveDialog();
				return "Exception when closing the current dialog: " + e.toString();
			}
		} else {
			return "No current dialog";
		}
	}

	private void doRemoveDialog() {
		currentDialog = null;
	}

	@Override
	public String performAssistRequestInstructions(String msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCAPMessage(CAPMessage msg) {
		this.testerHost.sendNotif(SOURCE_NAME, "Rsvd: " + msg.getMessageType().toString(), msg.toString(), Level.DEBUG);
	}

	@Override
	public void onErrorComponent(CAPDialog dlg, Long invokeId, CAPErrorMessage msg) {
		this.testerHost.sendNotif(SOURCE_NAME, "Rsvd: Error, InvokeId=" + invokeId + ", Error=" + msg.getErrorCode(), msg.toString(), Level.DEBUG);
	}

	@Override
	public void onInvokeTimeout(CAPDialog arg0, Long arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRejectComponent(CAPDialog dlg, Long invokeId, Problem problem, boolean isLocalOriginated) {
		this.testerHost.sendNotif(SOURCE_NAME, "Rsvd: Reject, InvokeId=" + invokeId + (isLocalOriginated ? ", local" : ", remote"), problem.toString(),
				Level.DEBUG);
	}

	@Override
	public void onActivityTestRequest(ActivityTestRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onActivityTestResponse(ActivityTestResponse arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onApplyChargingReportRequest(ApplyChargingReportRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onApplyChargingRequest(ApplyChargingRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAssistRequestInstructionsRequest(AssistRequestInstructionsRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCallInformationReportRequest(CallInformationReportRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCallInformationRequestRequest(CallInformationRequestRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCancelRequest(CancelRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectRequest(ConnectRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectToResourceRequest(ConnectToResourceRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onContinueRequest(ContinueRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnectForwardConnectionRequest(DisconnectForwardConnectionRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEstablishTemporaryConnectionRequest(EstablishTemporaryConnectionRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEventReportBCSMRequest(EventReportBCSMRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFurnishChargingInformationRequest(FurnishChargingInformationRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInitialDPRequest(InitialDPRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayAnnouncementRequest(PlayAnnouncementRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPromptAndCollectUserInformationRequest(PromptAndCollectUserInformationRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPromptAndCollectUserInformationResponse(PromptAndCollectUserInformationResponse arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReleaseCallRequest(ReleaseCallRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRequestReportBCSMEventRequest(RequestReportBCSMEventRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResetTimerRequest(ResetTimerRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSendChargingInformationRequest(SendChargingInformationRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSpecializedResourceReportRequest(SpecializedResourceReportRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogAccept(CAPDialog dlg, CAPGprsReferenceNumber arg1) {
		this.testerHost.sendNotif(SOURCE_NAME, "Rsvd: DlgAccept", "TrId=" + dlg.getLocalDialogId(), Level.DEBUG);
	}

	@Override
	public void onDialogClose(CAPDialog dlg) {
		this.testerHost.sendNotif(SOURCE_NAME, "Rsvd: DlgClose", "TrId=" + dlg.getLocalDialogId(), Level.DEBUG);
	}

	@Override
	public void onDialogDelimiter(CAPDialog dlg) {
		try {
			if (dlg.getState() == CAPDialogState.InitialReceived)
				dlg.send();
		} catch (CAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onDialogNotice(CAPDialog dlg, CAPNoticeProblemDiagnostic problem) {
		this.testerHost.sendNotif(SOURCE_NAME, "Rsvd: DlgNotice", "Problem: " + problem, Level.DEBUG);
	}

	@Override
	public void onDialogProviderAbort(CAPDialog dlg, PAbortCauseType problem) {
		this.testerHost.sendNotif(SOURCE_NAME, "Rsvd: DlgProviderAbort", "Problem: " + problem, Level.DEBUG);
	}

	@Override
	public void onDialogRelease(CAPDialog arg0) {
		this.doRemoveDialog();
		this.testerHost.sendNotif(SOURCE_NAME, "DlgClosed:", "", Level.DEBUG);
	}

	@Override
	public void onDialogRequest(CAPDialog capDialog, CAPGprsReferenceNumber referenceNumber) {
		synchronized (this) {
			if (capDialog instanceof CAPDialogCircuitSwitchedCall) {
				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;

				CAPDialogCircuitSwitchedCall curDialog = this.currentDialog;
				currentRequestDef = "";
				if (curDialog == null) {
					this.currentDialog = dlg;
					this.testerHost.sendNotif(SOURCE_NAME, "DlgAccepted:", "TrId=" + capDialog.getRemoteDialogId(), Level.DEBUG);
				} else {
					try {
						capDialog.abort(CAPUserAbortReason.congestion);
					} catch (CAPException e) {
						e.printStackTrace();
					}
					this.testerHost.sendNotif(SOURCE_NAME, "Rejected incoming Dialog:", "TrId=" + capDialog.getRemoteDialogId(), Level.DEBUG);
				}
			}
		}
	}

	@Override
	public void onDialogTimeout(CAPDialog dlg) {
		dlg.keepAlive();
	}

	@Override
	public void onDialogUserAbort(CAPDialog dlg, CAPGeneralAbortReason reason, CAPUserAbortReason userAbort) {
		this.testerHost.sendNotif(SOURCE_NAME, "Rsvd: DlgProviderAbort", reason + " - " + userAbort, Level.DEBUG);
	}

}
