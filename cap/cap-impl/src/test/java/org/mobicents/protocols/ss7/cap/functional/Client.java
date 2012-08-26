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

import java.util.ArrayList;

import org.apache.log4j.Logger;
import static org.testng.Assert.*;

import org.mobicents.protocols.ss7.cap.CAPDialogImpl;
import org.mobicents.protocols.ss7.cap.CAPProviderImpl;
import org.mobicents.protocols.ss7.cap.api.CAPApplicationContext;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParameterFactory;
import org.mobicents.protocols.ss7.cap.api.CAPProvider;
import org.mobicents.protocols.ss7.cap.api.CAPStack;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.ODisconnectSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.CallingPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.CauseCap;
import org.mobicents.protocols.ss7.cap.api.isup.Digits;
import org.mobicents.protocols.ss7.cap.api.isup.LocationNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.OriginalCalledNumberCap;
import org.mobicents.protocols.ss7.cap.api.primitives.BCSMEvent;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.cap.api.primitives.MonitorMode;
import org.mobicents.protocols.ss7.cap.api.primitives.ReceivingSideID;
import org.mobicents.protocols.ss7.cap.api.primitives.ScfID;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPDialogCircuitSwitchedCall;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.InitialDPRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.RequestReportBCSMEventRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.Carrier;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.IPSSPCapabilities;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.NAOliInfo;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformationType;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwo;
import org.mobicents.protocols.ss7.cap.isup.CalledPartyNumberCapImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.InitialDPRequestImpl;
import org.mobicents.protocols.ss7.inap.api.INAPParameterFactory;
import org.mobicents.protocols.ss7.inap.api.primitives.LegType;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfo;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfoMessageType;
import org.mobicents.protocols.ss7.isup.ISUPParameterFactory;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.NAINumber;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.TCAPSendException;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class Client extends EventTestHarness  {

	private static Logger logger = Logger.getLogger(Client.class);

//	private CAPFunctionalTest runningTestCase;
	private SccpAddress thisAddress;
	private SccpAddress remoteAddress;

	protected CAPStack capStack;
	protected CAPProvider capProvider;

	protected CAPParameterFactory capParameterFactory;
	protected MAPParameterFactory mapParameterFactory;
	protected INAPParameterFactory inapParameterFactory;
	protected ISUPParameterFactory isupParameterFactory;

//	private boolean _S_receivedUnstructuredSSIndication, _S_sentEnd;
	
	protected CAPDialogCircuitSwitchedCall clientCscDialog;

//	private FunctionalTestScenario step;
	
	private long savedInvokeId;
//	private int dialogStep;
//	private String unexpected = "";


	Client(CAPStack capStack, CAPFunctionalTest runningTestCase, SccpAddress thisAddress, SccpAddress remoteAddress) {
		super(logger);
		
		this.capStack = capStack;
//		this.runningTestCase = runningTestCase;
		this.thisAddress = thisAddress;
		this.remoteAddress = remoteAddress;
		this.capProvider = this.capStack.getCAPProvider();

		this.capParameterFactory = this.capProvider.getCAPParameterFactory();
		this.mapParameterFactory = this.capProvider.getMAPParameterFactory();
		this.inapParameterFactory = this.capProvider.getINAPParameterFactory();
		this.isupParameterFactory = this.capProvider.getISUPParameterFactory();

		this.capProvider.addCAPDialogListener(this);

		this.capProvider.getCAPServiceCircuitSwitchedCall().addCAPServiceListener(this);

		this.capProvider.getCAPServiceCircuitSwitchedCall().acivate();
	}


	public void sendInitialDp(CAPApplicationContext appCnt) throws CAPException {

		clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(appCnt, this.thisAddress, this.remoteAddress);

		InitialDPRequest initialDp = getTestInitialDp();
		clientCscDialog.addInitialDPRequest(initialDp.getServiceKey(), initialDp.getCalledPartyNumber(), initialDp.getCallingPartyNumber(),
				initialDp.getCallingPartysCategory(), initialDp.getCGEncountered(), initialDp.getIPSSPCapabilities(), initialDp.getLocationNumber(),
				initialDp.getOriginalCalledPartyID(), initialDp.getExtensions(), initialDp.getHighLayerCompatibility(),
				initialDp.getAdditionalCallingPartyNumber(), initialDp.getBearerCapability(), initialDp.getEventTypeBCSM(), initialDp.getRedirectingPartyID(),
				initialDp.getRedirectionInformation(), initialDp.getCause(), initialDp.getServiceInteractionIndicatorsTwo(), initialDp.getCarrier(),
				initialDp.getCugIndex(), initialDp.getCugInterlock(), initialDp.getCugOutgoingAccess(), initialDp.getIMSI(), initialDp.getSubscriberState(),
				initialDp.getLocationInformation(), initialDp.getExtBasicServiceCode(), initialDp.getCallReferenceNumber(), initialDp.getMscAddress(),
				initialDp.getCalledPartyBCDNumber(), initialDp.getTimeAndTimezone(), initialDp.getCallForwardingSSPending(),
				initialDp.getInitialDPArgExtension());

		this.observerdEvents.add(TestEvent.createSentEvent(EventType.InitialDpRequest, null, sequence++));
		clientCscDialog.send();
	}

	public void sendAssistRequestInstructionsRequest() throws CAPException {

		clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff, this.thisAddress, this.remoteAddress);

		GenericNumber genericNumber = this.isupParameterFactory.createGenericNumber();
		genericNumber.setAddress("333111222");
		genericNumber.setAddressRepresentationRestrictedIndicator(GenericNumber._APRI_ALLOWED);
		genericNumber.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
//		genericNumber.setNumberIncompleter(GenericNumber._NI_COMPLETE);
		genericNumber.setNumberingPlanIndicator(GenericNumber._NPI_ISDN);
		genericNumber.setNumberQualifierIndicator(GenericNumber._NQIA_CALLED_NUMBER);
		genericNumber.setScreeningIndicator(GenericNumber._SI_NETWORK_PROVIDED);
		Digits correlationID = this.capParameterFactory.createDigits(genericNumber);
		IPSSPCapabilities ipSSPCapabilities = this.capParameterFactory.createIPSSPCapabilities(true, false, true, false, false, null);
		clientCscDialog.addAssistRequestInstructionsRequest(correlationID, ipSSPCapabilities, null);
		this.observerdEvents.add(TestEvent.createSentEvent(EventType.AssistRequestInstructionsRequest, null, sequence++));
		clientCscDialog.send();
	}

	public void sendEstablishTemporaryConnectionRequest_CallInformationRequest() throws CAPException {

		clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(CAPApplicationContext.CapV4_scf_gsmSSFGeneric, this.thisAddress,
				this.remoteAddress);

		GenericNumber genericNumber = this.isupParameterFactory.createGenericNumber();
		genericNumber.setAddress("333111222");
		genericNumber.setAddressRepresentationRestrictedIndicator(GenericNumber._APRI_ALLOWED);
		genericNumber.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
//		genericNumber.setNumberIncompleter(GenericNumber._NI_COMPLETE);
		genericNumber.setNumberingPlanIndicator(GenericNumber._NPI_ISDN);
		genericNumber.setNumberQualifierIndicator(GenericNumber._NQIA_CALLED_NUMBER);
		genericNumber.setScreeningIndicator(GenericNumber._SI_NETWORK_PROVIDED);
		Digits assistingSSPIPRoutingAddress = this.capParameterFactory.createDigits(genericNumber);
		clientCscDialog.addEstablishTemporaryConnectionRequest(assistingSSPIPRoutingAddress, null, null, null, null, null, null, null, null, null, null);
		this.observerdEvents.add(TestEvent.createSentEvent(EventType.EstablishTemporaryConnectionRequest, null, sequence++));

		ArrayList<RequestedInformationType> requestedInformationTypeList = new ArrayList<RequestedInformationType>();
		requestedInformationTypeList.add(RequestedInformationType.callStopTime);
		clientCscDialog.addCallInformationRequestRequest(requestedInformationTypeList, null, null);
		this.observerdEvents.add(TestEvent.createSentEvent(EventType.CallInformationRequestRequest, null, sequence++));
		clientCscDialog.send();
	}

	public void sendActivityTestRequest(int invokeTimeout) throws CAPException {

		clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF,
				this.thisAddress, this.remoteAddress);

		clientCscDialog.addActivityTestRequest(invokeTimeout);
		this.observerdEvents.add(TestEvent.createSentEvent(EventType.ActivityTestRequest, null, sequence++));
		clientCscDialog.send();
	}

	public void sendEventReportBCSMRequest_1() throws CAPException {

		CauseIndicators causeIndicators = this.isupParameterFactory.createCauseIndicators();
		causeIndicators.setLocation(CauseIndicators._LOCATION_USER);
		causeIndicators.setCodingStandard(CauseIndicators._CODING_STANDARD_ITUT);
		causeIndicators.setCauseValue(CauseIndicators._CV_ALL_CLEAR);
		CauseCap releaseCause = this.capParameterFactory.createCauseCap(causeIndicators);
		ODisconnectSpecificInfo oDisconnectSpecificInfo = this.capParameterFactory.createODisconnectSpecificInfo(releaseCause);
		ReceivingSideID legID = this.capParameterFactory.createReceivingSideID(LegType.leg1);
		MiscCallInfo miscCallInfo = this.inapParameterFactory.createMiscCallInfo(MiscCallInfoMessageType.notification, null);
		EventSpecificInformationBCSM eventSpecificInformationBCSM = this.capParameterFactory.createEventSpecificInformationBCSM(oDisconnectSpecificInfo);
		clientCscDialog.addEventReportBCSMRequest(EventTypeBCSM.oDisconnect, eventSpecificInformationBCSM, legID, miscCallInfo, null);

		this.observerdEvents.add(TestEvent.createSentEvent(EventType.EventReportBCSMRequest, null, sequence++));
		clientCscDialog.send();
	}

	public void sendBadData() throws CAPException {

		clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(null, this.thisAddress, this.remoteAddress);

		try {
			Dialog tcapDialog = ((CAPDialogImpl)clientCscDialog).getTcapDialog();
			TCBeginRequest tcBeginReq = ((CAPProviderImpl)this.capProvider).getTCAPProvider().getDialogPrimitiveFactory().createBegin(tcapDialog);
			tcapDialog.send(tcBeginReq);
		} catch (TCAPSendException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean checkTestInitialDp(InitialDPRequest ind) {
		
		try {
			if (ind.getServiceKey() != 321)
				return false;

			if (ind.getCalledPartyNumber() == null)
				return false;
			if (ind.getCalledPartyNumber().getCalledPartyNumber() == null)
				return false;
			if (ind.getCalledPartyNumber().getCalledPartyNumber().getNatureOfAddressIndicator() != NAINumber._NAI_INTERNATIONAL_NUMBER)
				return false;
			if (!ind.getCalledPartyNumber().getCalledPartyNumber().getAddress().equals("11223344"))
				return false;
			if (ind.getCalledPartyNumber().getCalledPartyNumber().getNumberingPlanIndicator() != CalledPartyNumber._NPI_ISDN)
				return false;
			if (ind.getCalledPartyNumber().getCalledPartyNumber().getInternalNetworkNumberIndicator() != CalledPartyNumber._INN_ROUTING_NOT_ALLOWED)
				return false;

			return true;

		} catch (CAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}	

	public InitialDPRequest getTestInitialDp() {
		
		try {
			CalledPartyNumber calledPartyNumber = this.isupParameterFactory.createCalledPartyNumber(); 
			calledPartyNumber.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
			calledPartyNumber.setAddress("11223344");
			calledPartyNumber.setNumberingPlanIndicator(CalledPartyNumber._NPI_ISDN);
			calledPartyNumber.setInternalNetworkNumberIndicator(CalledPartyNumber._INN_ROUTING_NOT_ALLOWED);
			CalledPartyNumberCap calledPartyNumberCap = this.capParameterFactory.createCalledPartyNumberCap(calledPartyNumber);
			calledPartyNumberCap = new CalledPartyNumberCapImpl(calledPartyNumber);

			InitialDPRequestImpl res = new InitialDPRequestImpl(321, calledPartyNumberCap, null, null, null, null, null, null, null, null,
					null, null, null, null, null, null, null, null, null, null, false, null, null, null, null, null, null, null, null, false, null, false);

			return res;
		} catch (CAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
//		int serviceKey, CalledPartyNumberCap calledPartyNumber, CallingPartyNumberCap callingPartyNumber,
//		CallingPartysCategoryInap callingPartysCategory, CGEncountered CGEncountered, IPSSPCapabilities IPSSPCapabilities,
//		LocationNumberCap locationNumber, OriginalCalledNumberCap originalCalledPartyID, CAPExtensions extensions,
//		HighLayerCompatibilityInap highLayerCompatibility, AdditionalCallingPartyNumberCap additionalCallingPartyNumber, BearerCapability bearerCapability,
//		EventTypeBCSM eventTypeBCSM, RedirectingPartyIDCap redirectingPartyID, RedirectionInformationInap redirectionInformation, CauseCap cause,
//		ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Carrier carrier, CUGIndex cugIndex, CUGInterlock cugInterlock,
//		boolean cugOutgoingAccess, IMSI imsi, SubscriberState subscriberState, LocationInformation locationInformation,
//		ExtBasicServiceCode extBasicServiceCode, CallReferenceNumber callReferenceNumber, ISDNAddressString mscAddress,
//		CalledPartyBCDNumber calledPartyBCDNumber, TimeAndTimezone timeAndTimezone, boolean callForwardingSSPending,
//		InitialDPArgExtension initialDPArgExtension, boolean isCAPVersion3orLater
	}

	public void checkRequestReportBCSMEventRequest(RequestReportBCSMEventRequest ind){
		assertEquals(ind.getBCSMEventList().size(), 7);

		BCSMEvent ev = ind.getBCSMEventList().get(0);
		assertEquals(ev.getEventTypeBCSM(), EventTypeBCSM.routeSelectFailure);
		assertEquals(ev.getMonitorMode(), MonitorMode.notifyAndContinue);
		assertNull(ev.getLegID());
		ev = ind.getBCSMEventList().get(1);
		assertEquals(ev.getEventTypeBCSM(), EventTypeBCSM.oCalledPartyBusy);
		assertEquals(ev.getMonitorMode(), MonitorMode.interrupted);
		assertNull(ev.getLegID());
		ev = ind.getBCSMEventList().get(2);
		assertEquals(ev.getEventTypeBCSM(), EventTypeBCSM.oNoAnswer);
		assertEquals(ev.getMonitorMode(), MonitorMode.interrupted);
		assertNull(ev.getLegID());
		ev = ind.getBCSMEventList().get(3);
		assertEquals(ev.getEventTypeBCSM(), EventTypeBCSM.oAnswer);
		assertEquals(ev.getMonitorMode(), MonitorMode.notifyAndContinue);
		assertNull(ev.getLegID());
		ev = ind.getBCSMEventList().get(4);
		assertEquals(ev.getEventTypeBCSM(), EventTypeBCSM.oDisconnect);
		assertEquals(ev.getMonitorMode(), MonitorMode.notifyAndContinue);
		assertEquals(ev.getLegID().getSendingSideID(), LegType.leg1);
		ev = ind.getBCSMEventList().get(5);
		assertEquals(ev.getEventTypeBCSM(), EventTypeBCSM.oDisconnect);
		assertEquals(ev.getMonitorMode(), MonitorMode.interrupted);
		assertEquals(ev.getLegID().getSendingSideID(), LegType.leg2);
		ev = ind.getBCSMEventList().get(6);
		assertEquals(ev.getEventTypeBCSM(), EventTypeBCSM.oAbandon);
		assertEquals(ev.getMonitorMode(), MonitorMode.notifyAndContinue);
		assertNull(ev.getLegID());
	}

	public void debug(String message) {
		this.logger.debug(message);
	}
	
	public void error(String message, Exception e){
		this.logger.error(message, e);
	}
}

