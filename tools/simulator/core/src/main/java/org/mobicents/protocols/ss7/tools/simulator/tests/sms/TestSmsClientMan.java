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

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.InformServiceCentreRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPServiceSmsListener;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponse;
import org.mobicents.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.mobicents.protocols.ss7.map.api.smstpdu.TypeOfNumber;
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
public class TestSmsClientMan extends TesterBase implements TestSmsClientManMBean, Stoppable, MAPDialogListener, MAPServiceSmsListener {

	public static String SOURCE_NAME = "TestSmsClient";

	private static final String ADDRESS_NATURE = "addressNature";
	private static final String NUMBERING_PLAN = "numberingPlan";
	private static final String SERVICE_CENTER_ADDRESS = "serviceCenterAddress";
	private static final String MAP_PROTOCOL_VERSION = "mapProtocolVersion";
	private static final String SRI_RESPONSE_IMSI = "sriResponseImsi";
	private static final String SRI_RESPONSE_VLR = "sriResponseVlr";
	private static final String SMSC_SSN = "smscSsn";
	private static final String TYPE_OF_NUMBER = "typeOfNumber";
	private static final String NUMBERING_PLAN_IDENTIFICATION = "numberingPlanIdentification";

	private AddressNature addressNature = AddressNature.international_number;
	private NumberingPlan numberingPlan = NumberingPlan.ISDN;
	private String serviceCenterAddress = "";
	private MapProtocolVersion mapProtocolVersion = new MapProtocolVersion(MapProtocolVersion.VAL_MAP_V3);
	private String sriResponseImsi = "";
	private String sriResponseVlr = "";
	private int smscSsn = 8;
	private TypeOfNumber typeOfNumber = TypeOfNumber.InternationalNumber;
	private NumberingPlanIdentification numberingPlanIdentification = NumberingPlanIdentification.ISDNTelephoneNumberingPlan;

	private final String name;
	private MapMan mapMan;

	private boolean isStarted = false;

	public TestSmsClientMan() {
		super(SOURCE_NAME);
		this.name = "???";
	}

	public TestSmsClientMan(String name) {
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
	public String getServiceCenterAddress() {
		return serviceCenterAddress;
	}

	@Override
	public void setServiceCenterAddress(String val) {
		serviceCenterAddress = val;
		this.testerHost.markStore();
	}

	@Override
	public MapProtocolVersion getMapProtocolVersion() {
		return mapProtocolVersion;
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
	public String getSRIResponseImsi() {
		return sriResponseImsi;
	}

	@Override
	public void setSRIResponseImsi(String val) {
		sriResponseImsi = val;
		this.testerHost.markStore();
	}

	@Override
	public String getSRIResponseVlr() {
		return sriResponseVlr;
	}

	@Override
	public void setSRIResponseVlr(String val) {
		sriResponseVlr = val;
		this.testerHost.markStore();
	}

	@Override
	public int getSmscSsn() {
		return smscSsn;
	}

	@Override
	public void setSmscSsn(int val) {
		smscSsn = val;
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

	protected static final XMLFormat<TestSmsClientMan> XML = new XMLFormat<TestSmsClientMan>(TestSmsClientMan.class) {

		public void write(TestSmsClientMan srv, OutputElement xml) throws XMLStreamException {
			xml.setAttribute(SMSC_SSN, srv.smscSsn);

			xml.add(srv.serviceCenterAddress, SERVICE_CENTER_ADDRESS);
			xml.add(srv.sriResponseImsi, SRI_RESPONSE_IMSI);
			xml.add(srv.sriResponseVlr, SRI_RESPONSE_VLR);

			xml.add(srv.addressNature.toString(), ADDRESS_NATURE);
			xml.add(srv.numberingPlan.toString(), NUMBERING_PLAN);
			xml.add(srv.mapProtocolVersion.toString(), MAP_PROTOCOL_VERSION);
			xml.add(srv.typeOfNumber.toString(), TYPE_OF_NUMBER);
			xml.add(srv.numberingPlanIdentification.toString(), NUMBERING_PLAN_IDENTIFICATION);
		}

		public void read(InputElement xml, TestSmsClientMan srv) throws XMLStreamException {
			srv.smscSsn = xml.getAttribute(SMSC_SSN).toInt();

			srv.serviceCenterAddress = (String) xml.get(SERVICE_CENTER_ADDRESS, String.class);
			srv.sriResponseImsi = (String) xml.get(SRI_RESPONSE_IMSI, String.class);
			srv.sriResponseVlr = (String) xml.get(SRI_RESPONSE_VLR, String.class);

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
		}
	};


	@Override
	public String getCurrentRequestDef() {
//		if (this.currentDialog != null)
//		return "CurDialog: " + currentRequestDef;
//	else
//		return "PrevDialog: " + currentRequestDef;
	// TODO Auto-generated method stub
	return "";
	}

	@Override
	public String getState() {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append(SOURCE_NAME);
//		sb.append(": CurDialog=");
//		MAPDialogSupplementary curDialog = currentDialog;
//		if (curDialog != null)
//			sb.append(curDialog.getDialogId());
//		else
//			sb.append("No");
//		sb.append(", Pending dialogs=");
//		sb.append(this.currentDialogQuere.size());
//		sb.append("<br>Count: processUnstructuredSSRequest-");
//		sb.append(countProcUnstReq);
//		sb.append(", processUnstructuredSSResponse-");
//		sb.append(countProcUnstResp);
//		sb.append("<br>unstructuredSSRequest-");
//		sb.append(countUnstReq);
//		sb.append(", unstructuredSSResponse-");
//		sb.append(countUnstResp);
//		sb.append(", unstructuredSSNotify-");
//		sb.append(countUnstNotifReq);
		sb.append("</html>");
		return sb.toString();

		// TODO Auto-generated method stub
	}

	public boolean start() {
		MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();
		mapProvider.getMAPServiceSms().acivate();
		mapProvider.getMAPServiceSms().addMAPServiceListener(this);
		mapProvider.addMAPDialogListener(this);
		this.testerHost.sendNotif(SOURCE_NAME, "SMS Client has been started", "", true);
		isStarted = true;

		return true;
	}

	@Override
	public void stop() {
		MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();
		isStarted = false;
		// TODO Auto-generated method stub
		//this.doRemoveDialog();
		// TODO Auto-generated method stub
		mapProvider.getMAPServiceSms().deactivate();
		mapProvider.getMAPServiceSms().removeMAPServiceListener(this);
		mapProvider.removeMAPDialogListener(this);
		this.testerHost.sendNotif(SOURCE_NAME, "SMS Client has been stopped", "", true);
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
	public String performMoForwardSM(String msg, String destIsdnNumber, String origIsdnNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
	
	@Override
	public void onForwardShortMessageRequest(ForwardShortMessageRequest forwSmInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onForwardShortMessageResponse(ForwardShortMessageResponse forwSmRespInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMoForwardShortMessageRequest(MoForwardShortMessageRequest moForwSmInd) {
		// TODO Auto-generated method stub
		
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
	public void onMtForwardShortMessageResponse(MtForwardShortMessageResponse mtForwSmRespInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSendRoutingInfoForSMRequest(SendRoutingInfoForSMRequest sendRoutingInfoForSMInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSendRoutingInfoForSMResponse(SendRoutingInfoForSMResponse sendRoutingInfoForSMRespInd) {
		// TODO Auto-generated method stub
		
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

}

