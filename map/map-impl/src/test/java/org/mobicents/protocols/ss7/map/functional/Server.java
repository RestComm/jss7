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

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.MAPStack;
import org.mobicents.protocols.ss7.map.api.MapServiceFactory;
import org.mobicents.protocols.ss7.map.api.dialog.AddressString;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.mobicents.protocols.ss7.map.api.dialog.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.map.api.dialog.MAPProviderError;
import org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSIndication;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;

/**
 * 
 * @author amit bhayani
 * 
 */
public class Server implements MAPDialogListener, MAPServiceSupplementaryListener {

	private static Logger logger = Logger.getLogger(Server.class);

	private MAPFunctionalTest runningTestCase;
	private SccpAddress thisAddress;
	private SccpAddress remoteAddress;

	private MAPStack mapStack;
	private MAPProvider mapProvider;

	private MapServiceFactory mapServiceFactory;

	private boolean finished = true;
	private boolean _S_recievedMAPOpenInfo, _S_recievedMAPCloseInfo;
	private String unexpected = "";

	Server(MAPStack mapStack, MAPFunctionalTest runningTestCase, SccpAddress thisAddress, SccpAddress remoteAddress) {
		super();
		this.mapStack = mapStack;
		this.runningTestCase = runningTestCase;
		this.thisAddress = thisAddress;
		this.remoteAddress = remoteAddress;
		this.mapProvider = this.mapStack.getMAPProvider();

		this.mapServiceFactory = this.mapProvider.getMapServiceFactory();

		this.mapProvider.addMAPDialogListener(this);
		this.mapProvider.getMAPServiceSupplementary().addMAPServiceListener(this);

		this.mapProvider.getMAPServiceSupplementary().acivate();
	}

	public boolean isFinished() {

		return this.finished && _S_recievedMAPOpenInfo && _S_recievedMAPCloseInfo;
	}

	public String getStatus() {
		String status = "";

		status += "_S_recievedMAPCloseInfo[" + _S_recievedMAPCloseInfo + "]" + "\n";
		status += "_S_recievedMAPOpenInfo[" + _S_recievedMAPOpenInfo + "]" + "\n";
		return status + unexpected;
	}

	/**
	 * MAPDialog Listener's
	 */
	@Override
	public void onDialogDelimiter(MAPDialog mapDialog) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
			MAPExtensionContainer extensionContainer) {

		logger.debug("Received MAPOpenInfo ");
		this._S_recievedMAPOpenInfo = true;
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
	public void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason,
			MAPExtensionContainer extensionContainer) {
		logger.debug("Received MAPUserAbortInfo");
	}

	@Override
	public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason,
			MAPAbortSource abortSource, MAPExtensionContainer extensionContainer) {
		logger.debug("Received MAPProviderAbortInfo");
	}

	@Override
	public void onDialogClose(MAPDialog mapDialog) {

		logger.debug("Received MAPCloseInfo");
		this._S_recievedMAPCloseInfo = true;
	}

	@Override
	public void onDialogNotice(MAPDialog mapDialog, MAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
		// TODO Auto-generated method stub

	}

	/**
	 * MAP Service Listeners
	 */
	public void onProcessUnstructuredSSIndication(ProcessUnstructuredSSIndication procUnstrInd) {
		String ussdString = procUnstrInd.getUSSDString().getString();
		AddressString msisdn = procUnstrInd.getMSISDNAddressString();
		logger.debug("Received ProcessUnstructuredSSIndication " + ussdString + " from MSISDN " + msisdn.getAddress());

		if (!ussdString.equals(MAPFunctionalTest.USSD_STRING)) {
			this.finished = false;
			unexpected += " Received USSDString " + ussdString + ". But was expected " + MAPFunctionalTest.USSD_STRING;
		} else {

			MAPDialogSupplementary mapDialog = procUnstrInd.getMAPDialog();
			Long invokeId = procUnstrInd.getInvokeId();

			USSDString ussdStringObj = this.mapServiceFactory.createUSSDString(MAPFunctionalTest.USSD_MENU);

			try {
				mapDialog.addUnstructuredSSRequest((byte) 0x0F, ussdStringObj);

				mapDialog.send();
			} catch (MAPException e) {
				logger.error(e);
				throw new RuntimeException(e);
			}

			logger.debug("InvokeId =  " + invokeId);
		}

	}

	public void onUnstructuredSSIndication(UnstructuredSSIndication unstrInd) {
		// TODO Auto-generated method stub

	}

}
