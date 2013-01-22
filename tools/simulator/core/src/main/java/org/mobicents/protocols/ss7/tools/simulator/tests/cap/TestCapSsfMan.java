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

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.apache.log4j.Level;
import org.mobicents.protocols.ss7.cap.api.CAPDialog;
import org.mobicents.protocols.ss7.cap.api.CAPDialogListener;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPMessage;
import org.mobicents.protocols.ss7.cap.api.CAPProvider;
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
import org.mobicents.protocols.ss7.tools.simulator.common.CapApplicationContextSsf;
import org.mobicents.protocols.ss7.tools.simulator.common.TesterBase;
import org.mobicents.protocols.ss7.tools.simulator.level3.CapMan;
import org.mobicents.protocols.ss7.tools.simulator.management.TesterHost;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class TestCapSsfMan extends TesterBase implements TestCapSsfManMBean, Stoppable, CAPDialogListener, CAPServiceCircuitSwitchedCallListener {

	public static String SOURCE_NAME = "TestCapSsf";

	private static final String CAP_PROTOCOL_VERSION = "capProtocolVersion";

	private CapApplicationContextSsf capProtocolVersion = new CapApplicationContextSsf(CapApplicationContextSsf.VAL_CAP_V1_gsmSSF_to_gsmSCF);

	private final String name;
	private CapMan capMan;

	private boolean isStarted = false;
	private int countInitialDp = 0;
	private int countInitiateCallAttempt = 0;
	private int countAssistRequestInstructions = 0;
	private String currentRequestDef = "";
	private CAPDialogCircuitSwitchedCall currentDialog = null;

	public TestCapSsfMan() {
		super(SOURCE_NAME);
		this.name = "???";
	}

	public TestCapSsfMan(String name) {
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
	public CapApplicationContextSsf getCapApplicationContext() {
		return this.capProtocolVersion;
	}

	@Override
	public String getCapApplicationContext_Value() {
		return this.capProtocolVersion.toString();
	}

	@Override
	public void setCapApplicationContext(CapApplicationContextSsf val) {
		capProtocolVersion = val;
		this.testerHost.markStore();
	}

	@Override
	public void putCapApplicationContext(String val) {
		CapApplicationContextSsf x = CapApplicationContextSsf.createInstance(val);
		if (x != null)
			this.setCapApplicationContext(x);
	}

	protected static final XMLFormat<TestCapSsfMan> XML = new XMLFormat<TestCapSsfMan>(TestCapSsfMan.class) {

		public void write(TestCapSsfMan srv, OutputElement xml) throws XMLStreamException {
			xml.add(srv.capProtocolVersion.toString(), CAP_PROTOCOL_VERSION);
		}

		public void read(InputElement xml, TestCapSsfMan srv) throws XMLStreamException {
			String cpv = (String) xml.get(CAP_PROTOCOL_VERSION, String.class);
			srv.capProtocolVersion = CapApplicationContextSsf.createInstance(cpv);
		}
	};


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
		this.testerHost.sendNotif(SOURCE_NAME, "CAP SSF has been started", "", Level.INFO);
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
		this.testerHost.sendNotif(SOURCE_NAME, "CAP SSF has been stopped", "", Level.INFO);
	}

	@Override
	public void execute() {
	}


	// ...................................

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
//		currentRequestDef = "";
	}

	@Override
	public String performInitialDp(String msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String performInitiateCallAttempt(String msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCAPMessage(CAPMessage arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onErrorComponent(CAPDialog arg0, Long arg1, CAPErrorMessage arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInvokeTimeout(CAPDialog arg0, Long arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRejectComponent(CAPDialog arg0, Long arg1, Problem arg2, boolean arg3) {
		// TODO Auto-generated method stub
		
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
	public void onDialogAccept(CAPDialog arg0, CAPGprsReferenceNumber arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogClose(CAPDialog arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogDelimiter(CAPDialog arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogNotice(CAPDialog arg0, CAPNoticeProblemDiagnostic arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogProviderAbort(CAPDialog arg0, PAbortCauseType arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogRelease(CAPDialog arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogRequest(CAPDialog arg0, CAPGprsReferenceNumber arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogTimeout(CAPDialog arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogUserAbort(CAPDialog arg0, CAPGeneralAbortReason arg1, CAPUserAbortReason arg2) {
		// TODO Auto-generated method stub
		
	}

}
