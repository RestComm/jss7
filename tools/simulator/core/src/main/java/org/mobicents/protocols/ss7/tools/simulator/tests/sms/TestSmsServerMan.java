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

package org.mobicents.protocols.ss7.tools.simulator.tests.sms;

import java.util.Calendar;
import java.util.GregorianCalendar;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.apache.log4j.Level;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.InformServiceCentreRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.LocationInfoWithLMSI;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPServiceSmsListener;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.mobicents.protocols.ss7.map.api.smstpdu.AbsoluteTimeStamp;
import org.mobicents.protocols.ss7.map.api.smstpdu.AddressField;
import org.mobicents.protocols.ss7.map.api.smstpdu.DataCodingScheme;
import org.mobicents.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.mobicents.protocols.ss7.map.api.smstpdu.ProtocolIdentifier;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsDeliverTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsSubmitTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.mobicents.protocols.ss7.map.api.smstpdu.UserData;
import org.mobicents.protocols.ss7.map.smstpdu.AbsoluteTimeStampImpl;
import org.mobicents.protocols.ss7.map.smstpdu.AddressFieldImpl;
import org.mobicents.protocols.ss7.map.smstpdu.DataCodingSchemeImpl;
import org.mobicents.protocols.ss7.map.smstpdu.ProtocolIdentifierImpl;
import org.mobicents.protocols.ss7.map.smstpdu.SmsDeliverTpduImpl;
import org.mobicents.protocols.ss7.map.smstpdu.UserDataImpl;
import org.mobicents.protocols.ss7.tools.simulator.Stoppable;
import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.common.MapProtocolVersion;
import org.mobicents.protocols.ss7.tools.simulator.common.NumberingPlanType;
import org.mobicents.protocols.ss7.tools.simulator.common.TesterBase;
import org.mobicents.protocols.ss7.tools.simulator.level3.MapMan;
import org.mobicents.protocols.ss7.tools.simulator.management.TesterHost;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class TestSmsServerMan extends TesterBase implements TestSmsServerManMBean, Stoppable, MAPDialogListener, MAPServiceSmsListener {

	public static String SOURCE_NAME = "TestSmsServer";

	private static final String ADDRESS_NATURE = "addressNature";
	private static final String NUMBERING_PLAN = "numberingPlan";
	private static final String SERVICE_CENTER_ADDRESS = "serviceCenterAddress";
	private static final String MAP_PROTOCOL_VERSION = "mapProtocolVersion";
	private static final String HLR_SSN = "hlrSsn";
	private static final String VLR_SSN = "vlrSsn";
	private static final String TYPE_OF_NUMBER = "typeOfNumber";
	private static final String NUMBERING_PLAN_IDENTIFICATION = "numberingPlanIdentification";
	private static final String SMS_CODING_TYPE = "smsCodingType";

	private AddressNature addressNature = AddressNature.international_number;
	private NumberingPlan numberingPlan = NumberingPlan.ISDN;
	private String serviceCenterAddress = "";
	private MapProtocolVersion mapProtocolVersion = new MapProtocolVersion(MapProtocolVersion.VAL_MAP_V3);
	private int hlrSsn = 6;
	private int vlrSsn = 8;
	private TypeOfNumber typeOfNumber = TypeOfNumber.InternationalNumber;
	private NumberingPlanIdentification numberingPlanIdentification = NumberingPlanIdentification.ISDNTelephoneNumberingPlan;
	private SmsCodingType smsCodingType = new SmsCodingType(SmsCodingType.VAL_GSM7);

	private final String name;
	private MapMan mapMan;

	private boolean isStarted = false;
	private int countSriReq = 0;
	private int countSriResp = 0;
	private int countMtFsmReq = 0;
	private int countMtFsmResp = 0;
	private int countMoFsmReq = 0;
	private int countMoFsmResp = 0;
	private String currentRequestDef = "";
	private boolean needSendSend = false;
	private boolean needSendClose = false;


	public TestSmsServerMan() {
		super(SOURCE_NAME);
		this.name = "???";
	}

	public TestSmsServerMan(String name) {
		super(SOURCE_NAME);
		this.name = name;
	}

	public void setTesterHost(TesterHost testerHost) {
		this.testerHost = testerHost;
	}

	public void setMapMan(MapMan val) {
		this.mapMan = val;
	}	


	@Override
	public AddressNatureType getAddressNature() {
		return new AddressNatureType(addressNature.getIndicator());
	}

	@Override
	public String getAddressNature_Value() {
		return addressNature.toString();
	}

	@Override
	public void setAddressNature(AddressNatureType val) {
		addressNature = AddressNature.getInstance(val.intValue());
		this.testerHost.markStore();
	}

	@Override
	public NumberingPlanType getNumberingPlan() {
		return new NumberingPlanType(numberingPlan.getIndicator());
	}

	@Override
	public String getNumberingPlan_Value() {
		return numberingPlan.toString();
	}

	@Override
	public void setNumberingPlan(NumberingPlanType val) {
		numberingPlan = NumberingPlan.getInstance(val.intValue());
		this.testerHost.markStore();
	}

	@Override
	public MapProtocolVersion getMapProtocolVersion() {
		return mapProtocolVersion;
	}

	@Override
	public String getServiceCenterAddress() {
		return serviceCenterAddress;
	}

	@Override
	public void setServiceCenterAddress(String val) {
		serviceCenterAddress = val;
		this.testerHost.markStore();
	}

	@Override
	public String getMapProtocolVersion_Value() {
		return mapProtocolVersion.toString();
	}

	@Override
	public void setMapProtocolVersion(MapProtocolVersion val) {
		mapProtocolVersion = val;
		this.testerHost.markStore();
	}

	@Override
	public int getHlrSsn() {
		return hlrSsn;
	}

	@Override
	public void setHlrSsn(int val) {
		hlrSsn = val;
		this.testerHost.markStore();
	}

	@Override
	public int getVlrSsn() {
		return vlrSsn;
	}

	@Override
	public void setVlrSsn(int val) {
		vlrSsn = val;
		this.testerHost.markStore();
	}

	@Override
	public TypeOfNumberType getTypeOfNumber() {
		return new TypeOfNumberType(typeOfNumber.getCode());
	}

	@Override
	public String getTypeOfNumber_Value() {
		return typeOfNumber.toString();
	}

	@Override
	public void setTypeOfNumber(TypeOfNumberType val) {
		typeOfNumber = TypeOfNumber.getInstance(val.intValue());
		this.testerHost.markStore();
	}

	@Override
	public NumberingPlanIdentificationType getNumberingPlanIdentification() {
		return new NumberingPlanIdentificationType(numberingPlanIdentification.getCode());
	}

	@Override
	public String getNumberingPlanIdentification_Value() {
		return numberingPlanIdentification.toString();
	}

	@Override
	public void setNumberingPlanIdentification(NumberingPlanIdentificationType val) {
		numberingPlanIdentification = NumberingPlanIdentification.getInstance(val.intValue());
		this.testerHost.markStore();
	}

	@Override
	public SmsCodingType getSmsCodingType() {
		return smsCodingType;
	}

	@Override
	public String getSmsCodingType_Value() {
		return smsCodingType.toString();
	}

	@Override
	public void setSmsCodingType(SmsCodingType val) {
		smsCodingType = val;
		this.testerHost.markStore();
	}


	@Override
	public void putAddressNature(String val) {
		AddressNatureType x = AddressNatureType.createInstance(val);
		if (x != null)
			this.setAddressNature(x);
	}

	@Override
	public void putNumberingPlan(String val) {
		NumberingPlanType x = NumberingPlanType.createInstance(val);
		if (x != null)
			this.setNumberingPlan(x);
	}

	@Override
	public void putMapProtocolVersion(String val) {
		MapProtocolVersion x = MapProtocolVersion.createInstance(val);
		if (x != null)
			this.setMapProtocolVersion(x);
	}

	@Override
	public void putTypeOfNumber(String val) {
		TypeOfNumberType x = TypeOfNumberType.createInstance(val);
		if (x != null)
			this.setTypeOfNumber(x);
	}

	@Override
	public void putNumberingPlanIdentification(String val) {
		NumberingPlanIdentificationType x = NumberingPlanIdentificationType.createInstance(val);
		if (x != null)
			this.setNumberingPlanIdentification(x);
	}

	@Override
	public void putSmsCodingType(String val) {
		SmsCodingType x = SmsCodingType.createInstance(val);
		if (x != null)
			this.setSmsCodingType(x);
	}

	protected static final XMLFormat<TestSmsServerMan> XML = new XMLFormat<TestSmsServerMan>(TestSmsServerMan.class) {

		public void write(TestSmsServerMan srv, OutputElement xml) throws XMLStreamException {
			xml.setAttribute(HLR_SSN, srv.hlrSsn);
			xml.setAttribute(VLR_SSN, srv.vlrSsn);

			xml.add(srv.serviceCenterAddress, SERVICE_CENTER_ADDRESS);

			xml.add(srv.addressNature.toString(), ADDRESS_NATURE);
			xml.add(srv.numberingPlan.toString(), NUMBERING_PLAN);
			xml.add(srv.mapProtocolVersion.toString(), MAP_PROTOCOL_VERSION);
			xml.add(srv.typeOfNumber.toString(), TYPE_OF_NUMBER);
			xml.add(srv.numberingPlanIdentification.toString(), NUMBERING_PLAN_IDENTIFICATION);
			xml.add(srv.smsCodingType.toString(), SMS_CODING_TYPE);
		}

		public void read(InputElement xml, TestSmsServerMan srv) throws XMLStreamException {
			srv.hlrSsn = xml.getAttribute(HLR_SSN).toInt();
			srv.vlrSsn = xml.getAttribute(VLR_SSN).toInt();
			
			srv.serviceCenterAddress = (String) xml.get(SERVICE_CENTER_ADDRESS, String.class);
			
			String an = (String) xml.get(ADDRESS_NATURE, String.class);
			srv.addressNature = AddressNature.valueOf(an);
			String np = (String) xml.get(NUMBERING_PLAN, String.class);
			srv.numberingPlan = NumberingPlan.valueOf(np);
			String mpv = (String) xml.get(MAP_PROTOCOL_VERSION, String.class);
			srv.mapProtocolVersion = MapProtocolVersion.createInstance(mpv);
			String ton = (String) xml.get(TYPE_OF_NUMBER, String.class);
			srv.typeOfNumber = TypeOfNumber.valueOf(ton);
			String npi = (String) xml.get(NUMBERING_PLAN_IDENTIFICATION, String.class);
			srv.numberingPlanIdentification = NumberingPlanIdentification.valueOf(npi);
			String sct = (String) xml.get(SMS_CODING_TYPE, String.class);
			srv.smsCodingType = SmsCodingType.createInstance(sct);
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
		sb.append("<br>Count: countSriReq-");
		sb.append(countSriReq);
		sb.append(", countSriResp-");
		sb.append(countSriResp);
		sb.append("<br>countMtFsmReq-");
		sb.append(countMtFsmReq);
		sb.append(", countMtFsmResp-");
		sb.append(countMtFsmResp);
		sb.append("<br> countMoFsmReq-");
		sb.append(countMoFsmReq);
		sb.append(", countMoFsmResp-");
		sb.append(countMoFsmResp);
		sb.append("</html>");
		return sb.toString();
	}

	public boolean start() {
		MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();
		mapProvider.getMAPServiceSms().acivate();
		mapProvider.getMAPServiceSms().addMAPServiceListener(this);
		mapProvider.addMAPDialogListener(this);
		this.testerHost.sendNotif(SOURCE_NAME, "SMS Server has been started", "", Level.INFO);
		isStarted = true;

		return true;
	}

	@Override
	public void stop() {
		MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();
		isStarted = false;
		mapProvider.getMAPServiceSms().deactivate();
		mapProvider.getMAPServiceSms().removeMAPServiceListener(this);
		mapProvider.removeMAPDialogListener(this);
		this.testerHost.sendNotif(SOURCE_NAME, "SMS Server has been stopped", "", Level.INFO);
	}

	@Override
	public void execute() {
	}


	@Override
	public String closeCurrentDialog() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String performSRIForSM(String destIsdnNumber) {
		if (!isStarted)
			return "The tester is not started";
		if (destIsdnNumber == null || destIsdnNumber.equals(""))
			return "DestIsdnNumber is empty";

		currentRequestDef = "";

		return doSendSri(destIsdnNumber, this.getServiceCenterAddress(), null);
	}

	private String doSendSri(String destIsdnNumber, String serviceCentreAddr, MoMessageData messageData) {

		MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();

		MAPApplicationContextVersion vers;
		switch(this.mapProtocolVersion.intValue()){
		case MapProtocolVersion.VAL_MAP_V1:
			vers = MAPApplicationContextVersion.version1;
			break;
		case MapProtocolVersion.VAL_MAP_V2:
			vers = MAPApplicationContextVersion.version2;
			break;
		default:
			vers = MAPApplicationContextVersion.version3;
			break;
		}
		MAPApplicationContext mapAppContext = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgGatewayContext, vers);
		
		ISDNAddressString msisdn = mapProvider.getMAPParameterFactory().createISDNAddressString(this.addressNature, this.numberingPlan, destIsdnNumber);
		AddressString serviceCentreAddress = mapProvider.getMAPParameterFactory().createAddressString(this.addressNature, this.numberingPlan, serviceCentreAddr);

		try {
			MAPDialogSms curDialog = mapProvider.getMAPServiceSms().createNewDialog(mapAppContext, this.mapMan.createOrigAddress(), null,
					this.mapMan.createDestAddress(destIsdnNumber, this.hlrSsn), null);
			curDialog.setUserObject(messageData);

			curDialog.addSendRoutingInfoForSMRequest(msisdn, true, serviceCentreAddress, null, false, null, null);
			curDialog.send();

			String sriData = createSriData(curDialog.getDialogId(), destIsdnNumber, serviceCentreAddr);
			currentRequestDef += "Sent SriReq;";
			this.countSriReq++;
			this.testerHost.sendNotif(SOURCE_NAME, "Sent: sriReq", sriData, Level.DEBUG);

			return "SendRoutingInfoForSMRequest has been sent";
		} catch (MAPException ex) {
			return "Exception when sending SendRoutingInfoForSMRequest: " + ex.toString();
		}
	}

	private String createSriData(long dialogId, String destIsdnNumber, String serviceCentreAddr){
		StringBuilder sb = new StringBuilder();
		sb.append("dialogId=");
		sb.append(dialogId);
		sb.append(", destIsdnNumber=\"");
		sb.append(destIsdnNumber);
		sb.append("\", serviceCentreAddr=\"");
		sb.append(serviceCentreAddr);
		sb.append("\"");
		return sb.toString();
	}

	@Override
	public String performSRIForSM_MtForwardSM(String msg, String destIsdnNumber, String origIsdnNumber) {
		if (!isStarted)
			return "The tester is not started";
		if (origIsdnNumber == null || origIsdnNumber.equals(""))
			return "OrigIsdnNumber is empty";
		if (destIsdnNumber == null || destIsdnNumber.equals(""))
			return "DestIsdnNumber is empty";
		if (msg == null || msg.equals(""))
			return "Msg is empty";
		int maxMsgLen = this.smsCodingType.getSupportesMaxMessageLength();
		if (msg.length() > maxMsgLen)
			return "Simulator does not support message length for current encoding type more than " + maxMsgLen;

		currentRequestDef = "";

		MoMessageData mmd = new MoMessageData();
		mmd.msg = msg;
		mmd.origIsdnNumber = origIsdnNumber;

		return doSendSri(destIsdnNumber, this.getServiceCenterAddress(), mmd);
	}

	@Override
	public String performMtForwardSM(String msg, String destImsi, String vlrNumber, String origIsdnNumber) {
		if (!isStarted)
			return "The tester is not started";
		if (msg == null || msg.equals(""))
			return "Msg is empty";
		if (destImsi == null || destImsi.equals(""))
			return "DestImsi is empty";
		if (vlrNumber == null || vlrNumber.equals(""))
			return "VlrNumber is empty";
		if (origIsdnNumber == null || origIsdnNumber.equals(""))
			return "OrigIsdnNumber is empty";
		int maxMsgLen = this.smsCodingType.getSupportesMaxMessageLength();
		if (msg.length() > maxMsgLen)
			return "Simulator does not support message length for current encoding type more than " + maxMsgLen;

		currentRequestDef = "";

		return doMtForwardSM(msg, destImsi, vlrNumber, origIsdnNumber, this.getServiceCenterAddress());
	}

	private String doMtForwardSM(String msg, String destImsi, String vlrNumber, String origIsdnNumber, String serviceCentreAddr) {

		MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();

		MAPApplicationContextVersion vers;
		MAPApplicationContextName acn = MAPApplicationContextName.shortMsgMTRelayContext;
		switch(this.mapProtocolVersion.intValue()){
		case MapProtocolVersion.VAL_MAP_V1:
			vers = MAPApplicationContextVersion.version1;
			acn = MAPApplicationContextName.shortMsgMORelayContext;
			break;
		case MapProtocolVersion.VAL_MAP_V2:
			vers = MAPApplicationContextVersion.version2;
			break;
		default:
			vers = MAPApplicationContextVersion.version3;
			break;
		}
		MAPApplicationContext mapAppContext = MAPApplicationContext.getInstance(acn, vers);

		IMSI imsi = mapProvider.getMAPParameterFactory().createIMSI(destImsi);
		SM_RP_DA da = mapProvider.getMAPParameterFactory().createSM_RP_DA(imsi);
		AddressString serviceCentreAddress = mapProvider.getMAPParameterFactory().createAddressString(this.addressNature, this.numberingPlan, serviceCentreAddr);
		SM_RP_OA oa = mapProvider.getMAPParameterFactory().createSM_RP_OA_ServiceCentreAddressOA(serviceCentreAddress);

		try {
			AddressField originatingAddress = new AddressFieldImpl(this.typeOfNumber, this.numberingPlanIdentification, origIsdnNumber);
			Calendar cld = new GregorianCalendar();
			int year = cld.get(Calendar.YEAR);
			int mon = cld.get(Calendar.MONTH);
			int day = cld.get(Calendar.DAY_OF_MONTH);
			int h = cld.get(Calendar.HOUR);
			int m = cld.get(Calendar.MINUTE);
			int s = cld.get(Calendar.SECOND);
			int tz = cld.get(Calendar.ZONE_OFFSET);
			AbsoluteTimeStamp serviceCentreTimeStamp = new AbsoluteTimeStampImpl(year - 2000, mon, day, h, m, s, tz / 1000 / 60 / 15);
			DataCodingScheme dcs = new DataCodingSchemeImpl(this.smsCodingType.intValue() == SmsCodingType.VAL_GSM7 ? 0 : 8);
			UserData userData = new UserDataImpl(msg, dcs, null, null);
			ProtocolIdentifier pi = new ProtocolIdentifierImpl(0);
			SmsDeliverTpdu tpdu = new SmsDeliverTpduImpl(false, false, false, false, originatingAddress, pi, serviceCentreTimeStamp, userData);
			SmsSignalInfo si = mapProvider.getMAPParameterFactory().createSmsSignalInfo(tpdu, null);

			MAPDialogSms curDialog = mapProvider.getMAPServiceSms().createNewDialog(mapAppContext, this.mapMan.createOrigAddress(), null,
					this.mapMan.createDestAddress(vlrNumber, this.vlrSsn), null);

			if (this.mapProtocolVersion.intValue() <= 2)
				curDialog.addForwardShortMessageRequest(da, oa, si, false);
			else
				curDialog.addMtForwardShortMessageRequest(da, oa, si, false, null);
			curDialog.send();

			String mtData = createMtData(curDialog.getDialogId(), destImsi, vlrNumber, origIsdnNumber, serviceCentreAddr);
			currentRequestDef += "Sent mtReq;";
			this.countMtFsmReq++;
			this.testerHost.sendNotif(SOURCE_NAME, "Sent: mtReq: " + msg, mtData, Level.DEBUG);

			return "MtForwardShortMessageRequest has been sent";
		} catch (MAPException ex) {
			return "Exception when sending MtForwardShortMessageRequest: " + ex.toString();
		}
	}

	private String createMtData(long dialogId, String destImsi, String vlrNumber, String origIsdnNumber, String serviceCentreAddr) {
		StringBuilder sb = new StringBuilder();
		sb.append("dialogId=");
		sb.append(dialogId);
		sb.append(", destImsi=\"");
		sb.append(destImsi);
		sb.append(", vlrNumber=\"");
		sb.append(vlrNumber);
		sb.append(", origIsdnNumber=\"");
		sb.append(origIsdnNumber);
		sb.append("\", serviceCentreAddr=\"");
		sb.append(serviceCentreAddr);
		sb.append("\"");
		return sb.toString();
	}

	@Override
	public void onForwardShortMessageRequest(ForwardShortMessageRequest ind) {
		if (!isStarted)
			return;

		MAPDialogSms curDialog = ind.getMAPDialog();
		long invokeId = ind.getInvokeId();
		SM_RP_DA da = ind.getSM_RP_DA();
		SM_RP_OA oa = ind.getSM_RP_OA();
		SmsSignalInfo si = ind.getSM_RP_UI();

		if (da.getServiceCentreAddressDA() != null) { // mo message
			this.onMoRequest(da, oa, si, curDialog);

			try {
				curDialog.addForwardShortMessageResponse(invokeId);
				this.needSendClose = true;

				this.countMoFsmResp++;
				this.testerHost.sendNotif(SOURCE_NAME, "Sent: moResp", "", Level.DEBUG);
			} catch (MAPException e) {
				this.testerHost.sendNotif(SOURCE_NAME, "Exception when invoking addMoForwardShortMessageResponse : " + e.getMessage(), e, Level.ERROR);
			}
		}
	}

	@Override
	public void onForwardShortMessageResponse(ForwardShortMessageResponse ind) {
		if (!isStarted)
			return;

		this.countMtFsmResp++;

		MAPDialogSms curDialog = ind.getMAPDialog();
		long invokeId = curDialog.getDialogId();
		currentRequestDef += "Rsvd mtResp;";
		this.testerHost.sendNotif(SOURCE_NAME, "Rcvd: mtResp", "", Level.DEBUG);
	}

	@Override
	public void onMoForwardShortMessageRequest(MoForwardShortMessageRequest ind) {
		if (!isStarted)
			return;

		MAPDialogSms curDialog = ind.getMAPDialog();
		long invokeId = ind.getInvokeId();
		SM_RP_DA da = ind.getSM_RP_DA();
		SM_RP_OA oa = ind.getSM_RP_OA();
		SmsSignalInfo si = ind.getSM_RP_UI();

		this.onMoRequest(da, oa, si, curDialog);

		try {
			curDialog.addMoForwardShortMessageResponse(invokeId, null, null);
			this.needSendClose = true;

			this.countMoFsmResp++;
			this.testerHost.sendNotif(SOURCE_NAME, "Sent: moResp", "", Level.DEBUG);
		} catch (MAPException e) {
			this.testerHost.sendNotif(SOURCE_NAME, "Exception when invoking addMoForwardShortMessageResponse : " + e.getMessage(), e, Level.ERROR);
		}
	}

	private void onMoRequest(SM_RP_DA da, SM_RP_OA oa, SmsSignalInfo si, MAPDialogSms curDialog){

		this.countMoFsmReq++;

		String serviceCentreAddr = null;
		if (da != null) {
			AddressString as = da.getServiceCentreAddressDA();
			if (as != null)
				serviceCentreAddr = as.getAddress();
		}

		String origIsdnNumber = null;
		if (oa != null) {
			ISDNAddressString isdn = oa.getMsisdn();
			if (isdn != null)
				origIsdnNumber = isdn.getAddress();
		}

		try {
			String msg = null;
			String destIsdnNumber = null;
			if (si != null) {
				SmsTpdu tpdu = si.decodeTpdu(true);
				if (tpdu instanceof SmsSubmitTpdu) {
					SmsSubmitTpdu dTpdu = (SmsSubmitTpdu) tpdu;
					AddressField af = dTpdu.getDestinationAddress();
					if (af != null)
						destIsdnNumber = af.getAddressValue();
					UserData ud = dTpdu.getUserData();
					if (ud != null) {
						ud.decode();
						msg = ud.getDecodedMessage();
					}
				}
			}
			String uData = this.createMoData(curDialog.getDialogId(), destIsdnNumber, origIsdnNumber, serviceCentreAddr);
			this.testerHost.sendNotif(SOURCE_NAME, "Rcvd: moReq: " + msg, uData, Level.DEBUG);
		} catch (MAPException e) {
			this.testerHost.sendNotif(SOURCE_NAME, "Exception when decoding MoForwardShortMessageRequest tpdu : " + e.getMessage(), e, Level.ERROR);
		}
	}

	private String createMoData(long dialogId, String destIsdnNumber, String origIsdnNumber, String serviceCentreAddr) {
		StringBuilder sb = new StringBuilder();
		sb.append("dialogId=");
		sb.append(dialogId);
		sb.append(", destIsdnNumber=\"");
		sb.append(destIsdnNumber);
		sb.append(", origIsdnNumber=\"");
		sb.append(origIsdnNumber);
		sb.append("\", serviceCentreAddr=\"");
		sb.append(serviceCentreAddr);
		sb.append("\"");
		return sb.toString();
	}

	@Override
	public void onMoForwardShortMessageResponse(MoForwardShortMessageResponse moForwSmRespInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMtForwardShortMessageRequest(MtForwardShortMessageRequest mtForwSmInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMtForwardShortMessageResponse(MtForwardShortMessageResponse ind) {
		if (!isStarted)
			return;

		this.countMtFsmResp++;

		MAPDialogSms curDialog = ind.getMAPDialog();
		long invokeId = curDialog.getDialogId();
		currentRequestDef += "Rsvd mtResp;";
		this.testerHost.sendNotif(SOURCE_NAME, "Rcvd: mtResp", "", Level.DEBUG);
	}

	@Override
	public void onSendRoutingInfoForSMRequest(SendRoutingInfoForSMRequest sendRoutingInfoForSMInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSendRoutingInfoForSMResponse(SendRoutingInfoForSMResponse ind) {
		if (!isStarted)
			return;

		this.countSriResp++;

		MAPDialogSms curDialog = ind.getMAPDialog();
		long invokeId = curDialog.getDialogId();
		LocationInfoWithLMSI li = ind.getLocationInfoWithLMSI();
		String vlrNum = "";
		if (li != null && li.getNetworkNodeNumber() != null)
			vlrNum = li.getNetworkNodeNumber().getAddress();
		currentRequestDef += "Rsvd SriResp;";
		String destImsi = "";
		if (ind.getIMSI() != null)
			destImsi = ind.getIMSI().getData();
		String uData = this.createSriRespData(invokeId, destImsi, vlrNum);
		this.testerHost.sendNotif(SOURCE_NAME, "Rcvd: sriReq", uData, Level.DEBUG);

		if (curDialog.getUserObject() != null && vlrNum != null && !vlrNum.equals("") && destImsi != null && !destImsi.equals("")) {
			// sending SMS
			MoMessageData mmd = (MoMessageData) curDialog.getUserObject();
			doMtForwardSM(mmd.msg, destImsi, vlrNum, mmd.origIsdnNumber, this.serviceCenterAddress);
		}
	}

	private String createSriRespData(long dialogId, String imsi, String vlrNumber) {
		StringBuilder sb = new StringBuilder();
		sb.append("dialogId=");
		sb.append(dialogId);
		sb.append(", imsi=\"");
		sb.append(imsi);
		sb.append("\", vlrNumber=\"");
		sb.append(vlrNumber);
		sb.append("\"");
		return sb.toString();
	}

	@Override
	public void onReportSMDeliveryStatusRequest(ReportSMDeliveryStatusRequest reportSMDeliveryStatusInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReportSMDeliveryStatusResponse(ReportSMDeliveryStatusResponse reportSMDeliveryStatusRespInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInformServiceCentreRequest(InformServiceCentreRequest informServiceCentreInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAlertServiceCentreRequest(AlertServiceCentreRequest alertServiceCentreInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAlertServiceCentreResponse(AlertServiceCentreResponse alertServiceCentreInd) {
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

	private class MoMessageData {
		public String msg;
		public String origIsdnNumber;
	}
}
