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

package org.mobicents.protocols.ss7.map.functional;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.map.MAPStackImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.MAPServiceListener;
import org.mobicents.protocols.ss7.map.api.MAPStack;
import org.mobicents.protocols.ss7.map.api.MapServiceFactory;
import org.mobicents.protocols.ss7.map.api.dialog.AddressNature;
import org.mobicents.protocols.ss7.map.api.dialog.AddressString;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.mobicents.protocols.ss7.map.api.dialog.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.map.api.dialog.MAPProviderError;
import org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.dialog.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.dialog.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.dialog.ProcedureCancellationReason;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;

/**
 * 
 * @author amit bhayani
 * @author sergey vetyutnev
 * 
 */
public class Client implements MAPDialogListener, MAPServiceSupplementaryListener {

	private static Logger logger = Logger.getLogger(Client.class);

	private MAPFunctionalTest runningTestCase;
	private SccpAddress thisAddress;
	private SccpAddress remoteAddress;

	private MAPStack mapStack;
	private MAPProvider mapProvider;

	private MapServiceFactory mapServiceFactory;

//	private boolean finished = true;
	private String unexpected = "";
	private boolean _S_receivedUnstructuredSSIndication, _S_sentEnd;
	private boolean _S_receivedMAPOpenInfoExtentionContainer;
	private boolean _S_receivedAbortInfo;
	private boolean _S_receivedEndInfo;

	private MAPDialogSupplementary clientDialog;
	
	private FunctionalTestScenario step;

	Client(MAPStack mapStack, MAPFunctionalTest runningTestCase,
			SccpAddress thisAddress, SccpAddress remoteAddress) {
		super();
		this.mapStack = mapStack;
		this.runningTestCase = runningTestCase;
		this.thisAddress = thisAddress;
		this.remoteAddress = remoteAddress;
		this.mapProvider = this.mapStack.getMAPProvider();

		this.mapServiceFactory = this.mapProvider.getMapServiceFactory();

		this.mapProvider.addMAPDialogListener(this);
		this.mapProvider.getMAPServiceSupplementary().addMAPServiceListener(this);
	}

	public void start() throws MAPException {
		this.mapProvider.getMAPServiceSupplementary().acivate();
		
		MAPApplicationContext appCnt = MAPApplicationContext.networkUnstructuredSsContextV2;
		AddressString orgiReference = this.mapServiceFactory
				.createAddressString(AddressNature.international_number,
						NumberingPlan.ISDN, "31628968300");
		AddressString destReference = this.mapServiceFactory
				.createAddressString(AddressNature.international_number,
						NumberingPlan.land_mobile, "204208300008002");
		
		AddressString msisdn = this.mapServiceFactory.createAddressString(
				AddressNature.international_number, NumberingPlan.ISDN, "31628838002");		

		clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt,
				this.thisAddress, orgiReference, this.remoteAddress,
				destReference);

		
		USSDString ussdString = this.mapServiceFactory
				.createUSSDString(MAPFunctionalTest.USSD_STRING);

		clientDialog.addProcessUnstructuredSSRequest( (byte) 0x0F, ussdString, msisdn);

		logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);

		clientDialog.send();
	}

	

	public void actionA() throws MAPException {
		this.mapProvider.getMAPServiceSupplementary().acivate();
		
		MAPApplicationContext appCnt = MAPApplicationContext.networkUnstructuredSsContextV2;
		AddressString orgiReference = this.mapServiceFactory
				.createAddressString(AddressNature.international_number,
						NumberingPlan.ISDN, "31628968300");
		AddressString destReference = this.mapServiceFactory
				.createAddressString(AddressNature.international_number,
						NumberingPlan.land_mobile, "204208300008002");
		
		AddressString msisdn = this.mapServiceFactory.createAddressString(
				AddressNature.international_number, NumberingPlan.ISDN, "31628838002");		

//		clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt,
//				this.thisAddress, null, this.remoteAddress,
//				null);
		clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt,
				this.thisAddress, orgiReference, this.remoteAddress,
				destReference);
		clientDialog.setExtentionContainer(MAPFunctionalTest.GetTestExtensionContainer(this.mapServiceFactory));

		
		USSDString ussdString = this.mapServiceFactory
				.createUSSDString(MAPFunctionalTest.USSD_STRING);

		clientDialog.addProcessUnstructuredSSRequest( (byte) 0x0F, ussdString, msisdn);

		logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);

		clientDialog.send();
	}

	public void actionB() throws MAPException {
		this.mapProvider.getMAPServiceSupplementary().acivate();
		
		MAPApplicationContext appCnt = MAPApplicationContext.networkUnstructuredSsContextV2;
		AddressString orgiReference = this.mapServiceFactory
				.createAddressString(AddressNature.international_number,
						NumberingPlan.ISDN, "31628968300");
		AddressString destReference = this.mapServiceFactory
				.createAddressString(AddressNature.international_number,
						NumberingPlan.land_mobile, "204208300008002");
		
		AddressString msisdn = this.mapServiceFactory.createAddressString(
				AddressNature.international_number, NumberingPlan.ISDN, "31628838002");		

		clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt,
				this.thisAddress, orgiReference, this.remoteAddress,
				destReference);

		
		USSDString ussdString = this.mapServiceFactory
				.createUSSDString(MAPFunctionalTest.USSD_STRING);

		clientDialog.addProcessUnstructuredSSRequest( (byte) 0x0F, ussdString, msisdn);

		logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);

		clientDialog.send();
	}

	
	public boolean isFinished() {

//		return this.finished && _S_receivedUnstructuredSSIndication
//		&& _S_sentEnd && _S_recievedMAPOpenInfoExtentionContainer;
		switch( this.step ) {
		case actionA:
			return _S_receivedUnstructuredSSIndication && _S_sentEnd && _S_receivedMAPOpenInfoExtentionContainer;
		case actionB:
			return _S_receivedAbortInfo && _S_receivedMAPOpenInfoExtentionContainer;
		case actionC:
			return _S_receivedAbortInfo;
		case actionD:
			return _S_receivedUnstructuredSSIndication && _S_receivedEndInfo && _S_receivedMAPOpenInfoExtentionContainer;
		case actionE:
			return _S_receivedUnstructuredSSIndication && _S_sentEnd;
		case actionF:
			return _S_receivedAbortInfo;
		}
		
		
		return false;
	}

	public String getStatus() {
		String status = "";

		switch( this.step ) {
		case actionA:
			status += "_S_receivedUnstructuredSSIndication["
				+ _S_receivedUnstructuredSSIndication + "]" + "\n";
			status += "_S_recievedMAPOpenInfoExtentionContainer["
					+ _S_receivedMAPOpenInfoExtentionContainer + "]" + "\n";
			status += "_S_sentEnd[" + _S_sentEnd + "]" + "\n";
			break;
			
		case actionB:
			status += "_S_receivedAbortInfo["
				+ _S_receivedAbortInfo + "]" + "\n";
			status += "_S_recievedMAPOpenInfoExtentionContainer["
				+ _S_receivedMAPOpenInfoExtentionContainer + "]" + "\n";
			break;
			
		case actionC:
			status += "_S_receivedAbortInfo["
				+ _S_receivedAbortInfo + "]" + "\n";
			break;
			
		case actionD:
			status += "_S_receivedUnstructuredSSIndication["
				+ _S_receivedUnstructuredSSIndication + "]" + "\n";
			status += "_S_recievedMAPOpenInfoExtentionContainer["
					+ _S_receivedMAPOpenInfoExtentionContainer + "]" + "\n";
			status += "_S_receivedEndInfo[" + _S_receivedEndInfo + "]" + "\n";
			break;
			
		case actionE:
			status += "_S_receivedUnstructuredSSIndication["
				+ _S_receivedUnstructuredSSIndication + "]" + "\n";
			status += "_S_sentEnd[" + _S_sentEnd + "]" + "\n";
			break;
			
		case actionF:
			status += "_S_receivedAbortInfo["
				+ _S_receivedAbortInfo + "]" + "\n";
			break;
		}

		return status + unexpected;
	}
	
	public void reset() {
//		this.finished = true;
		this._S_receivedUnstructuredSSIndication = false;
		this._S_sentEnd = false;
		this._S_receivedMAPOpenInfoExtentionContainer = false;
		this._S_receivedAbortInfo = false;
		this._S_receivedEndInfo = false;
	}
	
	public void setStep (FunctionalTestScenario step) {
		this.step = step;
	}

	
	
	/**
	 * MAPDialog Listener's
	 */
	@Override
	public void onDialogDelimiter(MAPDialog mapDialog) {

		switch( this.step ) {
		case actionA:
			logger.debug("Calling Client.end()");
			try {
				mapDialog.close(true);
				_S_sentEnd = true;
			} catch (MAPException e) {
				logger.error(e);
				throw new RuntimeException(e);
			}
			break;
			
		case actionE:
			logger.debug("Sending MAPUserAbortInfo ");
			try {
				_S_sentEnd = true;
				mapDialog.setExtentionContainer(MAPFunctionalTest.GetTestExtensionContainer(this.mapServiceFactory));
				MAPUserAbortChoice choice = this.mapServiceFactory.createMAPUserAbortChoice();
				choice.setProcedureCancellationReason(ProcedureCancellationReason.handoverCancellation);
				mapDialog.abort(choice);
			} catch (MAPException e) {
				logger.error(e);
				throw new RuntimeException(e);
			}
			break;

		}
	}
	
	@Override
	public void onDialogRequest(MAPDialog mapDialog,
			AddressString destReference, AddressString origReference,
			MAPExtensionContainer extensionContainer) {
	}

	@Override
	public void onDialogAccept(MAPDialog mapDialog,
			MAPExtensionContainer extensionContainer) {
		
		switch( this.step ) {
		case actionA:
			if( MAPFunctionalTest.CheckTestExtensionContainer(extensionContainer) )
				_S_receivedMAPOpenInfoExtentionContainer = true;

			logger.debug("Received onMAPAcceptInfo ");
			break;
			
		case actionD:
			if( MAPFunctionalTest.CheckTestExtensionContainer(extensionContainer) )
				_S_receivedMAPOpenInfoExtentionContainer = true;
			
			this._S_receivedEndInfo = true;

			logger.debug("Received onMAPAcceptInfo ");
			break;

		case actionE:
			logger.debug("Received onMAPAcceptInfo ");
			break;
		}
	}

	@Override
	public void onDialogReject(MAPDialog mapDialog,
			MAPRefuseReason refuseReason, MAPProviderError providerError,
			ApplicationContextName alternativeApplicationContext,
			MAPExtensionContainer extensionContainer) {
		switch( this.step ) {
		case actionB:
			if (refuseReason == MAPRefuseReason.InvalidDestinationReference) {
				logger.debug("Received InvalidDestinationReference");
				_S_receivedAbortInfo = true;
				
				if( MAPFunctionalTest.CheckTestExtensionContainer(extensionContainer) )
					_S_receivedMAPOpenInfoExtentionContainer = true;
			}
			break;
			
		case actionC:
			if (refuseReason == MAPRefuseReason.ApplicationContextNotSupported) {
				logger.debug("Received ApplicationContextNotSupported");
				
				if (alternativeApplicationContext != null
						&& Arrays.equals(
								alternativeApplicationContext.getOid(),
								new long[] { 1, 2, 3 })) {
					_S_receivedAbortInfo = true;
				}
			}
			break;
		}
	}

	@Override
	public void onDialogUserAbort(MAPDialog mapDialog,
			MAPUserAbortChoice userReason,
			MAPExtensionContainer extensionContainer) {
	}

	@Override
	public void onDialogProviderAbort(MAPDialog mapDialog,
			MAPAbortProviderReason abortProviderReason,
			MAPAbortSource abortSource, MAPExtensionContainer extensionContainer) {
		switch( this.step ) {
		case actionF:
			logger.debug("Received DialogProviderAbort " 
					+ abortProviderReason.toString());
			if (abortProviderReason == MAPAbortProviderReason.InvalidPDU)
				_S_receivedAbortInfo = true;
			if( MAPFunctionalTest.CheckTestExtensionContainer(extensionContainer) )
				_S_receivedMAPOpenInfoExtentionContainer = true;
			break;
		}
	}

	@Override
	public void onDialogClose(MAPDialog mapDialog) {
	}

	@Override
	public void onDialogNotice(MAPDialog mapDialog,
			MAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
	}

	public void onDialogResease(MAPDialog mapDialog) {
		int i1=0;
		i1 = 1;
	}
	
	
	/**
	 * MAP Service Listeners
	 */
	public void onProcessUnstructuredSSIndication(
			ProcessUnstructuredSSIndication procUnstrInd) {
	}

	public void onUnstructuredSSIndication(UnstructuredSSIndication unstrInd) {
		switch( this.step ) {
		case actionA:
		case actionD:
		case actionE:
			logger.debug("Received UnstructuredSSIndication "
					+ unstrInd.getUSSDString().getString());
			_S_receivedUnstructuredSSIndication = true;
		}
	}

}
