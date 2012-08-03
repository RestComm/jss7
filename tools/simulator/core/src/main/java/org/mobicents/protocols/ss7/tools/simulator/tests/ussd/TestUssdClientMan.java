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

package org.mobicents.protocols.ss7.tools.simulator.tests.ussd;

import java.util.concurrent.atomic.AtomicInteger;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.apache.log4j.Level;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessage;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
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
import org.mobicents.protocols.ss7.map.dialog.MAPUserAbortChoiceImpl;
import org.mobicents.protocols.ss7.map.primitives.AlertingPatternImpl;
import org.mobicents.protocols.ss7.tools.simulator.Stoppable;
import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.common.NumberingPlanType;
import org.mobicents.protocols.ss7.tools.simulator.common.TesterBase;
import org.mobicents.protocols.ss7.tools.simulator.level3.MapMan;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class TestUssdClientMan extends TesterBase implements TestUssdClientManMBean, Stoppable, MAPDialogListener, MAPServiceSupplementaryListener {

	public static String SOURCE_NAME = "TestUssdClient";

	private static final String MSISDN_ADDRESS = "msisdnAddress";
	private static final String MSISDN_ADDRESS_NATURE = "msisdnAddressNature";
	private static final String MSISDN_NUMBERING_PLAN = "msisdnNumberingPlan";
	private static final String DATA_CODING_SCHEME = "dataCodingScheme";
	private static final String ALERTING_PATTERN = "alertingPattern";
	private static final String USSD_CLIENT_ACTION = "ussdClientAction";
	private static final String AUTO_REQUEST_STRING = "autoRequestString";
	private static final String MAX_CONCURENT_DIALOGS = "maxConcurrentDialogs";
	private static final String ONE_NOTIFICATION_FOR_100_DIALOGS = "oneNotificationFor100Dialogs";

	private String msisdnAddress = "";
	private AddressNature msisdnAddressNature = AddressNature.international_number;
	private NumberingPlan msisdnNumberingPlan = NumberingPlan.ISDN;
	private int dataCodingScheme = 0x0F;
	private int alertingPattern = -1;

	private UssdClientAction ussdClientAction = new UssdClientAction(UssdClientAction.VAL_MANUAL_OPERATION);
	private String autoRequestString = "???";
	private int maxConcurrentDialogs = 10;
	private boolean oneNotificationFor100Dialogs = false;

	private final String name;
//	private TesterHost testerHost;
	private MapMan mapMan;

	private int countProcUnstReq = 0;
	private int countProcUnstResp = 0;
	private int countProcUnstReqNot = 0;
	private int countUnstReq = 0;
	private int countUnstResp = 0;
	private int countUnstNotifReq = 0;
	private MAPDialogSupplementary currentDialog = null;
	private Long invokeId = null;
	private boolean isStarted = false;
	private String currentRequestDef = "";
	private boolean needSendSend = false;
	private boolean needSendClose = false;

	private AtomicInteger nbConcurrentDialogs = new AtomicInteger();
	private MessageSender sender = null;

	public TestUssdClientMan() {
		super(SOURCE_NAME);
		this.name = "???";
	}

	public TestUssdClientMan(String name) {
		super(SOURCE_NAME);
		this.name = name;
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
		msisdnNumberingPlan = NumberingPlan.getInstance(val.intValue());
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
	public UssdClientAction getUssdClientAction() {
		return ussdClientAction;
	}

	@Override
	public String getUssdClientAction_Value() {
		return ussdClientAction.toString();
	}

	@Override
	public void setUssdClientAction(UssdClientAction val) {
		ussdClientAction = val;
		this.testerHost.markStore();
	}

	@Override
	public String getAutoRequestString() {
		return autoRequestString;
	}

	@Override
	public void setAutoRequestString(String val) {
		autoRequestString = val;
		this.testerHost.markStore();
	}

	@Override
	public int getMaxConcurrentDialogs() {
		return maxConcurrentDialogs;
	}

	@Override
	public void setMaxConcurrentDialogs(int val) {
		maxConcurrentDialogs = val;
		this.testerHost.markStore();
	}

	@Override
	public boolean isOneNotificationFor100Dialogs() {
		return oneNotificationFor100Dialogs;
	}

	@Override
	public void setOneNotificationFor100Dialogs(boolean val) {
		oneNotificationFor100Dialogs = val;
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
	public void putUssdClientAction(String val) {
		UssdClientAction x = UssdClientAction.createInstance(val);
		if (x != null)
			this.setUssdClientAction(x);
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
		sb.append("<html>");
		sb.append(SOURCE_NAME);
		sb.append(": CurDialog=");
		MAPDialogSupplementary curDialog = currentDialog;
		if (curDialog != null)
			sb.append(curDialog.getDialogId());
		else
			sb.append("No");
		sb.append("<br>Count: processUnstructuredSSRequest-");
		sb.append(countProcUnstReq);
		sb.append(", processUnstructuredSSResponse-");
		sb.append(countProcUnstResp);
		sb.append("<br>unstructuredSSRequest-");
		sb.append(countUnstReq);
		sb.append(", unstructuredSSResponse-");
		sb.append(countUnstResp);
		sb.append(", unstructuredSSNotify-");
		sb.append(countUnstNotifReq);
		sb.append("</html>");
		return sb.toString();
	}

	public boolean start() {
		MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();
		mapProvider.getMAPServiceSupplementary().acivate();
		mapProvider.getMAPServiceSupplementary().addMAPServiceListener(this);
		mapProvider.addMAPDialogListener(this);
		this.testerHost.sendNotif(SOURCE_NAME, "USSD Client has been started", "", Level.INFO);
		isStarted = true;
		
		if (ussdClientAction.intValue() == UssdClientAction.VAL_AUTO_SendProcessUnstructuredSSRequest) {
			nbConcurrentDialogs = new AtomicInteger();
			this.sender = new MessageSender();
			Thread thr = new Thread(this.sender);
			thr.start();
		}

		return true;
	}

	@Override
	public void stop() {
		MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();
		isStarted = false;

		if (this.sender != null) {
			this.sender.stop();
			try {
				this.sender.notify();
			} catch (Exception e) {
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
			this.sender = null;
		}

		this.doRemoveDialog();
		mapProvider.getMAPServiceSupplementary().deactivate();
		mapProvider.getMAPServiceSupplementary().removeMAPServiceListener(this);
		mapProvider.removeMAPDialogListener(this);
		this.testerHost.sendNotif(SOURCE_NAME, "USSD Client has been stopped", "", Level.INFO);
	}

	@Override
	public void execute() {
	}

	@Override
	public String closeCurrentDialog() {
		if (!isStarted)
			return "The tester is not started";
		if (this.sender != null)
			return "The tester is not ion manual mode";

		MAPDialogSupplementary curDialog = currentDialog;
		if (curDialog != null) {
			try {
				MAPUserAbortChoice choice = new MAPUserAbortChoiceImpl();
				choice.setUserSpecificReason();
				curDialog.abort(choice);
				this.doRemoveDialog();
				return "The current dialog has been closed";
			} catch (MAPException e) {
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
	public String performProcessUnstructuredRequest(String msg) {
		if (!isStarted)
			return "The tester is not started";
		if (this.sender != null)
			return "The tester is not in manual mode";

		MAPDialogSupplementary curDialog = currentDialog;
		if (curDialog != null)
			return "The current dialog exists. Finish it previousely";
		if (msg == null || msg.equals(""))
			return "USSD message is empty";

		currentRequestDef = "";

		return this.doPerformProcessUnstructuredRequest(msg, true);		
	}

	private String doPerformProcessUnstructuredRequest(String msg, boolean manualMode) {
		
		MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();
		USSDString ussdString = mapProvider.getMAPParameterFactory().createUSSDString(msg);
		MAPApplicationContext mapUssdAppContext = MAPApplicationContext.getInstance(MAPApplicationContextName.networkUnstructuredSsContext,
				MAPApplicationContextVersion.version2);

		try {
			MAPDialogSupplementary curDialog = mapProvider.getMAPServiceSupplementary().createNewDialog(mapUssdAppContext, this.mapMan.createOrigAddress(),
					this.mapMan.createOrigReference(), this.mapMan.createDestAddress(), this.mapMan.createDestReference());
			if (manualMode)
				currentDialog = curDialog;
			invokeId = null;

			ISDNAddressString msisdn = null;
			if (this.msisdnAddress != null && !this.msisdnAddress.equals("")) {
				msisdn = mapProvider.getMAPParameterFactory().createISDNAddressString(this.msisdnAddressNature, this.msisdnNumberingPlan, this.msisdnAddress);
			}

			AlertingPattern alPattern = null;
			if (this.alertingPattern >= 0 && this.alertingPattern <= 255)
				alPattern = new AlertingPatternImpl(new byte[] { (byte) this.alertingPattern });
			curDialog.addProcessUnstructuredSSRequest((byte) this.dataCodingScheme, ussdString, alPattern, msisdn);

			curDialog.send();

			if (manualMode)
				currentRequestDef += "Sent procUnstrSsReq=\"" + msg + "\";";
			this.countProcUnstReq++;
			if (this.oneNotificationFor100Dialogs) {
				int i1 = countProcUnstReq / 100;
				if (countProcUnstReqNot < i1) {
					countProcUnstReqNot = i1;
					this.testerHost.sendNotif(SOURCE_NAME, "Sent: procUnstrSsReq: " + (countProcUnstReqNot * 100) + " messages sent", "", Level.DEBUG);
				}
			} else {
				String uData = this.createUssdMessageData(curDialog.getDialogId(), this.dataCodingScheme, msisdn, alPattern);
				this.testerHost.sendNotif(SOURCE_NAME, "Sent: procUnstrSsReq: " + msg, uData, Level.DEBUG);
			}

			return "ProcessUnstructuredSSRequest has been sent";
		} catch (MAPException ex) {
			return "Exception when sending ProcessUnstructuredSSRequest: " + ex.toString();
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

	protected static final XMLFormat<TestUssdClientMan> XML = new XMLFormat<TestUssdClientMan>(TestUssdClientMan.class) {

		public void write(TestUssdClientMan clt, OutputElement xml) throws XMLStreamException {
			xml.setAttribute(DATA_CODING_SCHEME, clt.dataCodingScheme);
			xml.setAttribute(ALERTING_PATTERN, clt.alertingPattern);
			xml.setAttribute(MAX_CONCURENT_DIALOGS, clt.maxConcurrentDialogs);
			xml.setAttribute(ONE_NOTIFICATION_FOR_100_DIALOGS, clt.oneNotificationFor100Dialogs);

			xml.add(clt.msisdnAddress, MSISDN_ADDRESS);
			xml.add(clt.msisdnAddressNature.toString(), MSISDN_ADDRESS_NATURE);
			xml.add(clt.msisdnNumberingPlan.toString(), MSISDN_NUMBERING_PLAN);
			
			xml.add(clt.ussdClientAction.toString(), USSD_CLIENT_ACTION);
			xml.add(clt.autoRequestString, AUTO_REQUEST_STRING);
		}

		public void read(InputElement xml, TestUssdClientMan clt) throws XMLStreamException {
			clt.dataCodingScheme = xml.getAttribute(DATA_CODING_SCHEME).toInt();
			clt.alertingPattern = xml.getAttribute(ALERTING_PATTERN).toInt();
			clt.maxConcurrentDialogs = xml.getAttribute(MAX_CONCURENT_DIALOGS).toInt();
			clt.oneNotificationFor100Dialogs = xml.getAttribute(ONE_NOTIFICATION_FOR_100_DIALOGS).toBoolean();

			clt.msisdnAddress = (String) xml.get(MSISDN_ADDRESS, String.class);
			String an = (String) xml.get(MSISDN_ADDRESS_NATURE, String.class);
			clt.msisdnAddressNature = AddressNature.valueOf(an);
			String np = (String) xml.get(MSISDN_NUMBERING_PLAN, String.class);
			clt.msisdnNumberingPlan = NumberingPlan.valueOf(np);

			String uca = (String) xml.get(USSD_CLIENT_ACTION, String.class);
			clt.ussdClientAction = UssdClientAction.createInstance(uca);
			clt.autoRequestString = (String) xml.get(AUTO_REQUEST_STRING, String.class);
		}
	};

	@Override
	public String performUnstructuredResponse(String msg) {
		if (!isStarted)
			return "The tester is not started";
		if (this.sender != null)
			return "The tester is not ion manual mode";

		MAPDialogSupplementary curDialog = currentDialog;
		if (curDialog == null)
			return "No current dialog exists. Start it previousely";
		if (invokeId == null)
			return "No pending unstructured request";

		MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();
		if (msg == null || msg.equals(""))
			return "USSD message is empty";
		USSDString ussdString = mapProvider.getMAPParameterFactory().createUSSDString(msg);

		try {
			curDialog.addUnstructuredSSResponse(invokeId, (byte) this.dataCodingScheme, ussdString);

			curDialog.send();

			invokeId = null;

			currentRequestDef += "Sent unstrSsResp=\"" + msg + "\";";
			this.countUnstResp++;
			String uData = this.createUssdMessageData(curDialog.getDialogId(), this.dataCodingScheme, null, null);
			this.testerHost.sendNotif(SOURCE_NAME, "Sent: unstrSsResp: " + msg, uData, Level.DEBUG);
			
			return "UnstructuredSSResponse has been sent";
		} catch (MAPException ex) {
			return "Exception when sending UnstructuredSSResponse: " + ex.toString();
		}		
	}


	@Override
	public void onMAPMessage(MAPMessage mapMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProcessUnstructuredSSResponse(ProcessUnstructuredSSResponse ind) {
		if (!isStarted)
			return;

		if (this.sender == null) {
			MAPDialogSupplementary curDialog = currentDialog;
			if (curDialog != ind.getMAPDialog())
				return;
			currentRequestDef += "procUnstrSsResp=\"" + ind.getUSSDString().getString() + "\";";
		}

		this.countProcUnstResp++;
		if (!this.oneNotificationFor100Dialogs) {
			String uData = this.createUssdMessageData(ind.getMAPDialog().getDialogId(), ind.getUSSDDataCodingScheme(), null, null);
			this.testerHost.sendNotif(SOURCE_NAME, "Rcvd: procUnstrSsResp: " + ind.getUSSDString().getString(), uData, Level.DEBUG);
		}
		
		this.doRemoveDialog();
	}

	@Override
	public void onUnstructuredSSRequest(UnstructuredSSRequest ind) {
		if (!isStarted)
			return;
		MAPDialogSupplementary curDialog = currentDialog;
		if (curDialog != ind.getMAPDialog())
			return;

		invokeId = ind.getInvokeId();
		
		currentRequestDef += "Rcvd: unstrSsReq=\"" + ind.getUSSDString().getString() + "\";";
		this.countUnstReq++;
		String uData = this.createUssdMessageData(curDialog.getDialogId(), ind.getUSSDDataCodingScheme(), null, null);
		this.testerHost.sendNotif(SOURCE_NAME, "Rcvd: unstrSsReq: " + ind.getUSSDString().getString(), uData, Level.DEBUG);
	}

	@Override
	public void onUnstructuredSSResponse(UnstructuredSSResponse unstrResInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnstructuredSSNotifyRequest(UnstructuredSSNotifyRequest ind) {
		if (!isStarted)
			return;

		MAPDialogSupplementary dlg = ind.getMAPDialog();
		invokeId = ind.getInvokeId();
		
		this.countUnstNotifReq++;
		String uData = this.createUssdMessageData(dlg.getDialogId(), ind.getUSSDDataCodingScheme(), null, null);
		this.testerHost.sendNotif(SOURCE_NAME, "Rcvd: unstrSsNotify: " + ind.getUSSDString().getString(), uData, Level.DEBUG);

		try {
			dlg.addUnstructuredSSNotifyResponse(invokeId);
			this.needSendClose = true;
		} catch (MAPException e) {
			this.testerHost.sendNotif(SOURCE_NAME, "Exception when invoking addUnstructuredSSNotifyResponse() : " + e.getMessage(), e, Level.ERROR);
		}
	}

	@Override
	public void onUnstructuredSSNotifyResponse(UnstructuredSSNotifyResponse unstrNotifyInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogDelimiter(MAPDialog mapDialog) {
		try {
			if (needSendSend) {
				needSendSend = false;
				mapDialog.send();
			}
		} catch (Exception e) {
			this.testerHost.sendNotif(SOURCE_NAME, "Exception when invoking send() : " + e.getMessage(), e, Level.ERROR);
		}
		try {
			if (needSendClose) {
				needSendClose = false;
				mapDialog.close(false);
			}
		} catch (Exception e) {
			this.testerHost.sendNotif(SOURCE_NAME, "Exception when invoking close() : " + e.getMessage(), e, Level.ERROR);
		}
	}

	@Override
	public void onDialogRelease(MAPDialog mapDialog) {
		if (this.currentDialog == mapDialog)
			this.doRemoveDialog();

		nbConcurrentDialogs.decrementAndGet();
		if (this.sender != null) {
			if (nbConcurrentDialogs.get() < maxConcurrentDialogs / 2)
				this.sender.notify();
		}
	}

	private class MessageSender implements Runnable {
		
		private boolean needStop = false;

		public void stop() {
			needStop = true;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			while (true) {		
				if (needStop)
					break;

				if (nbConcurrentDialogs.get() < maxConcurrentDialogs) {
					doPerformProcessUnstructuredRequest(autoRequestString, false);
					nbConcurrentDialogs.incrementAndGet();
				}

				if (nbConcurrentDialogs.get() >= maxConcurrentDialogs) {
					try {
						this.wait(100);
					} catch (Exception ex) {
					}
				}
			}
		}
	}
}

