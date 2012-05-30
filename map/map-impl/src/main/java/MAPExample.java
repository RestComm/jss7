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

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.map.MAPStackImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPMessage;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.MAPStack;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.mobicents.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.map.api.dialog.MAPProviderError;
import org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
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
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.SccpStack;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;

public class MAPExample implements MAPDialogListener, MAPServiceSupplementaryListener {

	private MAPStack mapStack;
	private MAPProvider mapProvider;

	MAPParameterFactory servFact;

	SccpAddress destAddress = null;

	// The address created by passing the AddressNature, NumberingPlan and
	// actual address
	AddressString destReference = servFact.createAddressString(AddressNature.international_number,
			NumberingPlan.land_mobile, "204208300008002");

	SccpAddress origAddress = null;

	AddressString origReference = servFact.createAddressString(AddressNature.international_number, NumberingPlan.ISDN,
			"31628968300");

	MAPExample(SccpProvider sccpPprovider, SccpAddress address, SccpAddress remoteAddress) {
		origAddress = address;
		destAddress = remoteAddress;

		mapStack = new MAPStackImpl(sccpPprovider, 8);
		mapProvider = mapStack.getMAPProvider();
		servFact = mapProvider.getMAPParameterFactory();

		mapProvider.addMAPDialogListener(this);
		mapProvider.getMAPServiceSupplementary().addMAPServiceListener(this);
	}

	private static SccpProvider getSccpProvider() throws NamingException {

		// no arg is ok, if we run in JBoss
		InitialContext ctx = new InitialContext();
		try {
			String providerJndiName = "/mobicents/ss7/sccp";
			return ((SccpStack) ctx.lookup(providerJndiName)).getSccpProvider();

		} finally {
			ctx.close();
		}
	}

	private static SccpAddress createLocalAddress() {
		return new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 1, null, 8);
	}

	private static SccpAddress createRemoteAddress() {
		return new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 2, null, 8);
	}

	public void run() throws Exception {

		// Make the supplimentary service activated
		mapProvider.getMAPServiceSupplementary().acivate();

		// First create Dialog
		MAPDialogSupplementary mapDialog = mapProvider.getMAPServiceSupplementary().createNewDialog(
				MAPApplicationContext.getInstance(MAPApplicationContextName.networkUnstructuredSsContext, MAPApplicationContextVersion.version2), destAddress,
				destReference, origAddress, origReference);

		// The dataCodingScheme is still byte, as I am not exactly getting how
		// to encode/decode this.
		byte ussdDataCodingScheme = 0x0f;

		// USSD String: *125*+31628839999#
		// The Charset is null, here we let system use default Charset (UTF-7 as
		// explained in GSM 03.38. However if MAP User wants, it can set its own
		// impl of Charset
		USSDString ussdString = servFact.createUSSDString("*125*+31628839999#", null);

		ISDNAddressString msisdn = this.servFact.createISDNAddressString(AddressNature.international_number,
				NumberingPlan.ISDN, "31628838002");

		mapDialog.addProcessUnstructuredSSRequest(ussdDataCodingScheme, ussdString,null, msisdn);

		// This will initiate the TC-BEGIN with INVOKE component
		mapDialog.send();
	}



	public static void main(String[] args) throws Exception {
		SccpProvider sccpProvider = getSccpProvider(); // JNDI lookup of SCCP

		SccpAddress localAddress = createLocalAddress();
		SccpAddress remoteAddress = createRemoteAddress();

		MAPExample example = new MAPExample(sccpProvider, localAddress, remoteAddress);

		example.run();

	}

	public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
			MAPExtensionContainer extensionContainer) {
		// TODO Auto-generated method stub

	}

	public void onDialogAccept(MAPDialog mapDialog, MAPExtensionContainer extensionContainer) {
		// TODO Auto-generated method stub

	}

	public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason, MAPProviderError providerError,
			ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
		// TODO Auto-generated method stub

	}

	public void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason,
			MAPExtensionContainer extensionContainer) {
		// TODO Auto-generated method stub

	}

	public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason,
			MAPAbortSource abortSource, MAPExtensionContainer extensionContainer) {
		// TODO Auto-generated method stub

	}

	public void onDialogClose(MAPDialog mapDialog) {
		// TODO Auto-generated method stub

	}

	public void onDialogDelimiter(MAPDialog mapDialog) {
		// TODO Auto-generated method stub

	}

	public void onDialogNotice(MAPDialog mapDialog, MAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
		// TODO Auto-generated method stub

	}

	public void onDialogRelease(MAPDialog mapDialog) {
		
	}

	public void onDialogTimeout(MAPDialog mapDialog) {
		// TODO Auto-generated method stub
		
	}

	public void onErrorComponent(MAPDialog mapDialog, Long invokeId, MAPErrorMessage mapErrorMessage) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderErrorComponent(MAPDialog mapDialog, Long invokeId, MAPProviderError providerError) {
		// TODO Auto-generated method stub
		
	}

	public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem) {
		// TODO Auto-generated method stub
		
	}

	public void onInvokeTimeout(MAPDialog mapDialog, Long invoke) {
		// TODO Auto-generated method stub
		
	}

	//USSD Listener callback
	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener#onProcessUnstructuredSSRequestIndication(org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequestIndication)
	 */
	public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener#onProcessUnstructuredSSResponseIndication(org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponseIndication)
	 */
	public void onProcessUnstructuredSSResponse(ProcessUnstructuredSSResponse procUnstrResInd) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener#onUnstructuredSSRequestIndication(org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequestIndication)
	 */
	public void onUnstructuredSSRequest(UnstructuredSSRequest unstrReqInd) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener#onUnstructuredSSResponseIndication(org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponseIndication)
	 */
	public void onUnstructuredSSResponse(UnstructuredSSResponse unstrResInd) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener#onUnstructuredSSNotifyIndication(org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyIndication)
	 */
	public void onUnstructuredSSNotifyRequest(UnstructuredSSNotifyRequest unstrNotifyInd) {
		// TODO Auto-generated method stub
		
	}

	public void onDialogRequestEricsson(MAPDialog mapDialog, AddressString destReference, AddressString origReference, IMSI eriImsi, AddressString eriVlrNo) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener#onUnstructuredSSNotifyResponseIndication(org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyResponseIndication)
	 */
	public void onUnstructuredSSNotifyResponse(UnstructuredSSNotifyResponse unstrNotifyInd) {
		// TODO Auto-generated method stub
		
	}

	public void onMAPMessage(MAPMessage mapMessage) {
		// TODO Auto-generated method stub
		
	}

}
