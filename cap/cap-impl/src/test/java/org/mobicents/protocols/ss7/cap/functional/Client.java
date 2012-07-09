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
import org.mobicents.protocols.ss7.cap.api.CAPApplicationContext;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParameterFactory;
import org.mobicents.protocols.ss7.cap.api.CAPProvider;
import org.mobicents.protocols.ss7.cap.api.CAPStack;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPDialogCircuitSwitchedCall;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.InitialDPRequest;
import org.mobicents.protocols.ss7.cap.isup.CalledPartyNumberCapImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.InitialDPRequestImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CalledPartyNumberImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

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

	private CAPStack capStack;
	private CAPProvider capProvider;

	private CAPParameterFactory capParameterFactory;

//	private boolean _S_receivedUnstructuredSSIndication, _S_sentEnd;
	
	private CAPDialogCircuitSwitchedCall clientCscDialog;

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

		this.capProvider.addCAPDialogListener(this);

		this.capProvider.getCAPServiceCircuitSwitchedCall().addCAPServiceListener(this);

		this.capProvider.getCAPServiceCircuitSwitchedCall().acivate();
	}


	public void sendInitialDp() throws CAPException {

		CAPApplicationContext appCnt = CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF;

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

		this.observerdEvents.add(TestEvent.createSentEvent(EventType.InitialDpIndication, null, sequence++));
		clientCscDialog.send();
	}

//	public void actionA() throws CAPException {
//
//		this.capProvider.getCAPServiceCircuitSwitchedCall().acivate();
//		this.capProvider.getCAPServiceSms().acivate();
//		this.capProvider.getCAPServiceSms().acivate();
//
//		CAPApplicationContext appCnt = null;
//		switch (this.step) {
//		case Action_InitilDp:
//			appCnt = CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF;
//			break;
//		}
//
//		clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(appCnt, this.thisAddress, this.remoteAddress);
//
//		switch (this.step) {
//		case Action_InitilDp: {
//			InitialDPRequest initialDp = getTestInitialDp();
//			clientCscDialog.addInitialDPRequest(initialDp.getServiceKey(), initialDp.getCalledPartyNumber(), initialDp.getCallingPartyNumber(),
//					initialDp.getCallingPartysCategory(), initialDp.getCGEncountered(), initialDp.getIPSSPCapabilities(), initialDp.getLocationNumber(),
//					initialDp.getOriginalCalledPartyID(), initialDp.getExtensions(), initialDp.getHighLayerCompatibility(),
//					initialDp.getAdditionalCallingPartyNumber(), initialDp.getBearerCapability(), initialDp.getEventTypeBCSM(),
//					initialDp.getRedirectingPartyID(), initialDp.getRedirectionInformation(), initialDp.getCause(),
//					initialDp.getServiceInteractionIndicatorsTwo(), initialDp.getCarrier(), initialDp.getCugIndex(), initialDp.getCugInterlock(),
//					initialDp.getCugOutgoingAccess(), initialDp.getIMSI(), initialDp.getSubscriberState(), initialDp.getLocationInformation(),
//					initialDp.getExtBasicServiceCode(), initialDp.getCallReferenceNumber(), initialDp.getMscAddress(), initialDp.getCalledPartyBCDNumber(),
//					initialDp.getTimeAndTimezone(), initialDp.getCallForwardingSSPending(), initialDp.getInitialDPArgExtension());
//		}
//			break;
//		}
//
//		clientCscDialog.send();
//	}


	public static boolean checkTestInitialDp(InitialDPRequest ind) {
		
		try {
			if (ind.getServiceKey() != 321)
				return false;

			if (ind.getCalledPartyNumber() == null)
				return false;
			if (ind.getCalledPartyNumber().getCalledPartyNumber() == null)
				return false;
			if (ind.getCalledPartyNumber().getCalledPartyNumber().getNatureOfAddressIndicator() != 1)
				return false;
			if (!ind.getCalledPartyNumber().getCalledPartyNumber().getAddress().equals("11223344"))
				return false;
			if (ind.getCalledPartyNumber().getCalledPartyNumber().getNumberingPlanIndicator() != 2)
				return false;
			if (ind.getCalledPartyNumber().getCalledPartyNumber().getInternalNetworkNumberIndicator() != 1)
				return false;

			return true;

		} catch (CAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}	

	public static InitialDPRequest getTestInitialDp() {
		
		try {
			CalledPartyNumberImpl calledPartyNumber = new CalledPartyNumberImpl(1, "11223344", 2, 1);
			// int natureOfAddresIndicator, String address, int
			// numberingPlanIndicator, int internalNetworkNumberIndicator
			CalledPartyNumberCapImpl calledPartyNumberCap;
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
}

