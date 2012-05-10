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

package org.mobicents.protocols.ss7.tools.simulator.testsussd;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessage;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.mobicents.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.map.api.dialog.MAPProviderError;
import org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponse;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tools.simulator.Stoppable;
import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.common.NumberingPlanType;
import org.mobicents.protocols.ss7.tools.simulator.level3.MapMan;
import org.mobicents.protocols.ss7.tools.simulator.management.TesterHost;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class TestUssdServerMan implements TestUssdServerManMBean, Stoppable, MAPDialogListener, MAPServiceSupplementaryListener {

	public static String SOURCE_NAME = "TestUssdServer";

	private static final String MSISDN_ADDRESS = "msisdnAddress";
	private static final String MSISDN_ADDRESS_NATURE = "msisdnAddressNature";
	private static final String MSISDN_NUMBERING_PLAN = "msisdnNumberingPlan";
	private static final String DATA_CODING_SCHEME = "dataCodingScheme";
	private static final String ALERTING_PATTERN = "alertingPattern";
	private static final String PROCESS_SS_REQUEST_ACTION = "processSsRequestAction";
	private static final String AUTO_RESPONSE_STRING = "autoResponseString";
	private static final String AUTO_UNSTRUCTURED_SS_REQUEST_STRING = "autoUnstructured_SS_RequestString";

	private String msisdnAddress = "";
	private AddressNature msisdnAddressNature = AddressNature.international_number;
	private NumberingPlan msisdnNumberingPlan = NumberingPlan.ISDN;
	private int dataCodingScheme = 0x0F;
	private int alertingPattern = -1;
	private ProcessSsRequestAction processSsRequestAction = new ProcessSsRequestAction(ProcessSsRequestAction.VAL_MANUAL_RESPONSE);
	private String autoResponseString = "";
	private String autoUnstructured_SS_RequestString = "";

	private final String name;
	private TesterHost testerHost;
	private MapMan mapMan;

	private int countProcUnstReq = 0;
	private int countProcUnstResp = 0;
	private int countUnstReq = 0;
	private int countUnstResp = 0;
	private int countUnstNotifReq = 0;
	private MAPDialogSupplementary currentDialog = null;
	private Queue<MAPDialogSupplementary> currentDialogQuere = new ConcurrentLinkedQueue<MAPDialogSupplementary>();
	private boolean isStarted = false;
	private String currentRequestDef = "";


	public TestUssdServerMan() {
		this.name = "???";
	}

	public TestUssdServerMan(String name) {
		this.name = name;
	}

	public void setTesterHost(TesterHost testerHost) {
		this.testerHost = testerHost;
	}

	public void setMapMan(MapMan val) {
		this.mapMan = val;
	}	
	

	@Override
	public String getMsisdnAddress() {
		return msisdnAddress;
	}

	@Override
	public void setMsisdnAddress(String val) {
		msisdnAddress = val;
		this.testerHost.markStore();
	}

	@Override
	public AddressNatureType getMsisdnAddressNature() {
		return new AddressNatureType(msisdnAddressNature.getIndicator());
	}

	@Override
	public String getMsisdnAddressNature_Value() {
		return msisdnAddressNature.toString();
	}

	@Override
	public void setMsisdnAddressNature(AddressNatureType val) {
		msisdnAddressNature = AddressNature.getInstance(val.intValue());
		this.testerHost.markStore();
	}

	@Override
	public NumberingPlanType getMsisdnNumberingPlan() {
		return new NumberingPlanType(msisdnNumberingPlan.getIndicator());
	}

	@Override
	public String getMsisdnNumberingPlan_Value() {
		return msisdnNumberingPlan.toString();
	}

	@Override
	public void setMsisdnNumberingPlan(NumberingPlanType val) {
		msisdnNumberingPlan = msisdnNumberingPlan.getInstance(val.intValue());
		this.testerHost.markStore();
	}

	@Override
	public int getDataCodingScheme() {
		return dataCodingScheme;
	}

	@Override
	public void setDataCodingScheme(int val) {
		dataCodingScheme = val;
		this.testerHost.markStore();
	}

	@Override
	public int getAlertingPattern() {
		return alertingPattern;
	}

	@Override
	public void setAlertingPattern(int val) {
		alertingPattern = val;
		this.testerHost.markStore();
	}

	@Override
	public ProcessSsRequestAction getProcessSsRequestAction() {
		return processSsRequestAction;
	}

	@Override
	public String getProcessSsRequestAction_Value() {
		return processSsRequestAction.toString();
	}

	@Override
	public void setProcessSsRequestAction(ProcessSsRequestAction val) {
		processSsRequestAction = val;
		this.testerHost.markStore();
	}

	@Override
	public String getAutoResponseString() {
		return autoResponseString;
	}

	@Override
	public void setAutoResponseString(String val) {
		autoResponseString = val;
		this.testerHost.markStore();
	}

	@Override
	public String getAutoUnstructured_SS_RequestString() {
		return autoUnstructured_SS_RequestString;
	}

	@Override
	public void setAutoUnstructured_SS_RequestString(String val) {
		autoUnstructured_SS_RequestString = val;
		this.testerHost.markStore();
	}

	@Override
	public void putMsisdnAddressNature(String val) {
		AddressNatureType x = AddressNatureType.createInstance(val);
		if (x != null)
			this.setMsisdnAddressNature(x);
	}

	@Override
	public void putMsisdnNumberingPlan(String val) {
		NumberingPlanType x = NumberingPlanType.createInstance(val);
		if (x != null)
			this.setMsisdnNumberingPlan(x);
	}

	@Override
	public void putProcessSsRequestAction(String val) {
		ProcessSsRequestAction x = ProcessSsRequestAction.createInstance(val);
		if (x != null)
			this.setProcessSsRequestAction(x);
	}

	@Override
	public String getCurrentRequestDef() {
		if (this.currentDialog != null)
			return "CurDialog: " + currentRequestDef;
		else
			return "PrevDialog: " + currentRequestDef;
	}

	@Override
	public String getState() {
		StringBuilder sb = new StringBuilder();
		sb.append(SOURCE_NAME);
		sb.append(": CurDialog=");
		MAPDialogSupplementary curDialog = currentDialog;
		if (curDialog != null)
			sb.append(curDialog.getDialogId());
		else
			sb.append("No");
		sb.append(", Pending dialogs=");
		sb.append(this.currentDialogQuere.size());
		sb.append(", Count: processUnstructuredSSRequest-");
		sb.append(countProcUnstReq);
		sb.append(", processUnstructuredSSResponse-");
		sb.append(countProcUnstResp);
		sb.append(", unstructuredSSRequest-");
		sb.append(countUnstReq);
		sb.append(", unstructuredSSResponse-");
		sb.append(countUnstResp);
		sb.append(", unstructuredSSNotify-");
		sb.append(countUnstNotifReq);
		return sb.toString();
	}


	public boolean start() {
		MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();
		mapProvider.getMAPServiceSupplementary().acivate();
		mapProvider.getMAPServiceSupplementary().addMAPServiceListener(this);
		mapProvider.addMAPDialogListener(this);
		this.testerHost.sendNotif(SOURCE_NAME, "USSD Server has been started", "", true);
		isStarted = true;

		return true;
	}

	@Override
	public void stop() {
		MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();
		isStarted = false;
		this.doRemoveDialog();
		mapProvider.getMAPServiceSupplementary().deactivate();
		mapProvider.getMAPServiceSupplementary().removeMAPServiceListener(this);
		mapProvider.removeMAPDialogListener(this);
		this.testerHost.sendNotif(SOURCE_NAME, "USSD Server has been stopped", "", true);
	}

	@Override
	public void execute() {
	}

	@Override
	public String closeCurrentDialog() {
		if (isStarted) {
			MAPDialogSupplementary curDialog = currentDialog;
			if (curDialog != null) {
				try {
					curDialog.close(false);
					this.doRemoveDialog();
					return "The current dialog has been closed";
				} catch (MAPException e) {
					this.doRemoveDialog();
					return "Exception when closing the current dialog: " + e.toString();
				}
			} else {
				return "No current dialog";
			}
		} else {
			return "The tester is not started";
		}
	}

	private void doRemoveDialog() {
		synchronized (this) {
			currentDialog = null;
			currentRequestDef = "";
			
			currentDialog = this.currentDialogQuere.poll();
			if (currentDialog != null) {
				DialogData dd = (DialogData) currentDialog.getUserObject();
				if (dd != null)
					currentRequestDef = dd.currentRequestDef;
			}
		}
	}

	private String createUssdMessageData(long dialogId, int dataCodingScheme, ISDNAddressString msisdn, AlertingPattern alPattern) {
		StringBuilder sb = new StringBuilder();
		sb.append("dialogId=");
		sb.append(dialogId);
		sb.append(" DataCodingSchema=");
		sb.append(dataCodingScheme);
		sb.append(" ");
		if (msisdn != null) {
			sb.append(msisdn.toString());
			sb.append(" ");
		}
		if (alPattern != null) {
			sb.append(alPattern.toString());
			sb.append(" ");
		}
		return sb.toString();
	}

	protected static final XMLFormat<TestUssdServerMan> XML = new XMLFormat<TestUssdServerMan>(TestUssdServerMan.class) {

		public void write(TestUssdServerMan srv, OutputElement xml) throws XMLStreamException {
			xml.setAttribute(DATA_CODING_SCHEME, srv.dataCodingScheme);
			xml.setAttribute(ALERTING_PATTERN, srv.alertingPattern);

			xml.add(srv.msisdnAddress, MSISDN_ADDRESS);
			xml.add(srv.autoResponseString, AUTO_RESPONSE_STRING);
			xml.add(srv.autoUnstructured_SS_RequestString, AUTO_UNSTRUCTURED_SS_REQUEST_STRING);
			
			xml.add(srv.msisdnAddressNature.toString(), MSISDN_ADDRESS_NATURE);
			xml.add(srv.msisdnNumberingPlan.toString(), MSISDN_NUMBERING_PLAN);
			xml.add(srv.processSsRequestAction.toString(), PROCESS_SS_REQUEST_ACTION);
		}

		public void read(InputElement xml, TestUssdServerMan srv) throws XMLStreamException {
			srv.dataCodingScheme = xml.getAttribute(DATA_CODING_SCHEME).toInt();
			srv.alertingPattern = xml.getAttribute(ALERTING_PATTERN).toInt();

			srv.msisdnAddress = (String) xml.get(MSISDN_ADDRESS, String.class);
			srv.autoResponseString = (String) xml.get(AUTO_RESPONSE_STRING, String.class);
			srv.autoUnstructured_SS_RequestString = (String) xml.get(AUTO_UNSTRUCTURED_SS_REQUEST_STRING, String.class);
			
			String an = (String) xml.get(MSISDN_ADDRESS_NATURE, String.class);
			srv.msisdnAddressNature = AddressNature.valueOf(an);
			String np = (String) xml.get(MSISDN_NUMBERING_PLAN, String.class);
			srv.msisdnNumberingPlan = NumberingPlan.valueOf(np);
			String ss_act = (String) xml.get(PROCESS_SS_REQUEST_ACTION, String.class);
			srv.processSsRequestAction = ProcessSsRequestAction.createInstance(ss_act);
		}
	};

	public String sendProcessUnstructuredResponse(MAPDialogSupplementary curDialog, String msg, long invokeId) {

		MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();

		USSDString ussdString = mapProvider.getMAPParameterFactory().createUSSDString(msg);
		MAPApplicationContext mapUssdAppContext = MAPApplicationContext.getInstance(MAPApplicationContextName.networkUnstructuredSsContext,
				MAPApplicationContextVersion.version2);

		try {
			curDialog.addProcessUnstructuredSSResponse(invokeId, (byte) this.dataCodingScheme, ussdString);

			curDialog.close(false);

//			currentRequestDef += "Sent procUnstrSsReq=\"" + msg + "\";";
			this.countProcUnstResp++;
			String uData = this.createUssdMessageData(curDialog.getDialogId(), this.dataCodingScheme, null, null);
			this.testerHost.sendNotif(SOURCE_NAME, "procUnstrSsResp: " + msg, uData, true);

			return "ProcessUnstructuredSSResponse has been sent";
		} catch (MAPException ex) {
			return "Exception when sending ProcessUnstructuredSSResponse: " + ex.toString();
		}
	}

	public String sendUnstructuredRequest(String msg) {

//		if (!isStarted)
//			return "The tester is not started";
//		
//		MAPDialogSupplementary curDialog = currentDialog;
//		if (curDialog == null)
//			return "The current dialog does opened. Open a dialog by UssdClient";
//
//		MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();
//		if (msg == null || msg.equals(""))
//			return "USSD message is empty";
//
//		USSDString ussdString = mapProvider.getMAPParameterFactory().createUSSDString(msg);
//		MAPApplicationContext mapUssdAppContext = MAPApplicationContext.getInstance(MAPApplicationContextName.networkUnstructuredSsContext,
//				MAPApplicationContextVersion.version2);
//
//		try {
//			curDialog = mapProvider.getMAPServiceSupplementary().createNewDialog(mapUssdAppContext, this.mapMan.createOrigAddress(),
//					this.mapMan.createOrigReference(), this.mapMan.createDestAddress(), this.mapMan.createDestReference());
//
//			ISDNAddressString msisdn = null;
//			if (this.msisdnAddress != null && !this.msisdnAddress.equals("")) {
//				msisdn = mapProvider.getMAPParameterFactory().createISDNAddressString(this.msisdnAddressNature, this.msisdnNumberingPlan, this.msisdnAddress);
//			}
//
//			AlertingPattern alPattern = null;
//			if (this.alertingPattern >= 0 && this.alertingPattern <= 255)
//				alPattern = new AlertingPatternImpl(new byte[] { (byte) this.alertingPattern });
//			curDialog.addProcessUnstructuredSSRequest((byte) this.dataCodingScheme, ussdString, alPattern, msisdn);
//
//			curDialog.send();
//
//			currentRequestDef += "Sent procUnstrSsReq=\"" + msg + "\";";
//			this.countProcUnstReq++;
//			String uData = this.createUssdMessageData(curDialog.getDialogId(), this.dataCodingScheme, msisdn, alPattern);
//			this.testerHost.sendNotif(SOURCE_NAME, "procUnstrSsReq: " + msg, uData, true);
//			
//			return "ProcessUnstructuredSSRequest has been sent";
//		} catch (MAPException ex) {
//			return "Exception when sending ProcessUnstructuredSSRequest: " + ex.toString();
//		}		
		
		return "";
	}

	@Override
	public String performProcessUnstructuredResponse(String msg) {

		if (!isStarted)
			return "The tester is not started";

		MAPDialogSupplementary curDialog = currentDialog;
		if (curDialog == null)
			return "The current dialog does not opened. Open a dialog by UssdClient";
		
		DialogData dd = (DialogData)curDialog.getUserObject();
		if (dd == null || dd.invokeId == null)
			return "No pending . Open a dialog by UssdClient";
		long invokeId = dd.invokeId;

		if (msg == null || msg.equals(""))
			return "USSD message is empty ProcessUnstructuredSs request";

		String res = this.sendProcessUnstructuredResponse(curDialog, msg, invokeId);
		if (res.equals("ProcessUnstructuredSSResponse has been sent")) {
			currentRequestDef += "Sent procUnstrSsResp=\"" + msg + "\";";
			dd.invokeId = null;
			currentDialog = null;
		}
		return res;			
	}

	@Override
	public String performUnstructuredRequest(String msg) {
		// TODO Auto-generated method stub
		return "";
		
	}

	@Override
	public String performUnstructuredNotify(String msg) {
		// TODO Auto-generated method stub
		return "";
		
	}

	
	@Override
	public void onErrorComponent(MAPDialog mapDialog, Long invokeId, MAPErrorMessage mapErrorMessage) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderErrorComponent(MAPDialog mapDialog, Long invokeId, MAPProviderError providerError) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInvokeTimeout(MAPDialog mapDialog, Long invokeId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMAPMessage(MAPMessage mapMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest ind) {

		if (!isStarted)
			return;

		DialogData dd = (DialogData) ind.getMAPDialog().getUserObject();
		if (dd == null) {
			dd = new DialogData();
			ind.getMAPDialog().setUserObject(dd);
		}

		dd.currentRequestDef += "procUnstrSsReq=\"" + ind.getUSSDString().getString() + "\";";
		this.countProcUnstReq++;
		String uData = this.createUssdMessageData(ind.getMAPDialog().getDialogId(), ind.getUSSDDataCodingScheme(), ind.getMSISDNAddressString(),
				ind.getAlertingPattern());
		this.testerHost.sendNotif(SOURCE_NAME, "procUnstrSsReq: " + ind.getUSSDString().getString(), uData, true);

		switch (this.getProcessSsRequestAction().intValue()) {
		case ProcessSsRequestAction.VAL_MANUAL_RESPONSE: {
			dd.invokeId = ind.getInvokeId();
		}
			break;
		case ProcessSsRequestAction.VAL_AUTO_ProcessUnstructuredSSResponse: {
			String msg = this.getAutoResponseString();
			if (msg == null || msg.equals(""))
				msg = "???";
			this.sendProcessUnstructuredResponse(ind.getMAPDialog(), msg, ind.getInvokeId());
		}
			break;
		case ProcessSsRequestAction.VAL_AUTO_Unstructured_SS_Request_Then_ProcessUnstructuredSSResponse: {
			// TODO: implement it
			// ................................
		}
			break;
		}		
	}

	@Override
	public void onProcessUnstructuredSSResponse(ProcessUnstructuredSSResponse procUnstrResInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnstructuredSSRequest(UnstructuredSSRequest unstrReqInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnstructuredSSResponse(UnstructuredSSResponse unstrResInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnstructuredSSNotifyRequest(UnstructuredSSNotifyRequest unstrNotifyInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnstructuredSSNotifyResponse(UnstructuredSSNotifyResponse unstrNotifyInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogDelimiter(MAPDialog mapDialog) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference, MAPExtensionContainer extensionContainer) {
		synchronized (this) {
			if (mapDialog instanceof MAPDialogSupplementary) {
				MAPDialogSupplementary dlg = (MAPDialogSupplementary) mapDialog;

				if (this.getProcessSsRequestAction().intValue() == ProcessSsRequestAction.VAL_MANUAL_RESPONSE) {
					MAPDialogSupplementary curDialog = this.currentDialog;
					if (curDialog == null) {
						this.currentDialog = dlg;
					} else {
						this.currentDialogQuere.add(dlg);
					}
				}
			}
		}
	}

	@Override
	public void onDialogRequestEricsson(MAPDialog mapDialog, AddressString destReference, AddressString origReference, IMSI eriImsi, AddressString eriVlrNo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogAccept(MAPDialog mapDialog, MAPExtensionContainer extensionContainer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason, MAPProviderError providerError,
			ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason, MAPExtensionContainer extensionContainer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason, MAPAbortSource abortSource,
			MAPExtensionContainer extensionContainer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogClose(MAPDialog mapDialog) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogNotice(MAPDialog mapDialog, MAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogRelease(MAPDialog mapDialog) {
		if (this.currentDialog == mapDialog)
			this.doRemoveDialog();
	}

	@Override
	public void onDialogTimeout(MAPDialog mapDialog) {
		// TODO Auto-generated method stub
		
	}
	
	private class DialogData {
		public Long invokeId;
		public String currentRequestDef = ""; 
	}
}

