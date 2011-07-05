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

package org.mobicents.protocols.ss7.map.service.supplementary;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.MAPDialogImpl;
import org.mobicents.protocols.ss7.map.MAPProviderImpl;
import org.mobicents.protocols.ss7.map.MAPServiceBaseImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPServiceListener;
import org.mobicents.protocols.ss7.map.api.dialog.AddressString;
import org.mobicents.protocols.ss7.map.api.dialog.ServingCheckData;
import org.mobicents.protocols.ss7.map.api.dialog.ServingCheckResult;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementary;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;
import org.mobicents.protocols.ss7.map.dialog.AddressStringImpl;
import org.mobicents.protocols.ss7.map.dialog.ServingCheckDataImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextNameImpl;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

public class MAPServiceSupplementaryImpl extends MAPServiceBaseImpl implements MAPServiceSupplementary {
	
	private int testMode = 0;

	public MAPServiceSupplementaryImpl(MAPProviderImpl mapProviderImpl) {
		super(mapProviderImpl);
	}

	/**
	 * Creating a new outgoing MAP Supplementary dialog and adding it to the
	 * MAPProvider.dialog collection
	 * 
	 */
	public MAPDialogSupplementary createNewDialog(MAPApplicationContext appCntx, SccpAddress origAddress,
			AddressString origReference, SccpAddress destAddress, AddressString destReference) throws MAPException {

		// We cannot create a dialog if the service is not activated
		if (!this.isActivated())
			throw new MAPException(
					"Cannot create MAPDialogSupplementary because MAPServiceSupplementary is not activated");

		Dialog tcapDialog = this.createNewTCAPDialog(origAddress, destAddress);
		MAPDialogSupplementaryImpl dialog = new MAPDialogSupplementaryImpl(appCntx, tcapDialog, this.mapProviderImpl,
				this, origReference, destReference);

		this.PutMADDialogIntoCollection(dialog);
		// this.mapProviderImpl.dialogs.put(dialog.getDialogId(), dialog);

		return dialog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.MAPServiceBaseImpl#createNewDialogIncoming
	 * (org.mobicents.protocols.ss7.map.api.MAPApplicationContext,
	 * org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog)
	 */
	protected MAPDialogImpl createNewDialogIncoming(MAPApplicationContext appCntx, Dialog tcapDialog) {
		return new MAPDialogSupplementaryImpl(appCntx, tcapDialog, this.mapProviderImpl, this, null, null);
	}

	public void addMAPServiceListener(MAPServiceSupplementaryListener mapServiceListener) {
		super.addMAPServiceListener(mapServiceListener);
	}

	public void removeMAPServiceListener(MAPServiceSupplementaryListener mapServiceListener) {
		super.removeMAPServiceListener(mapServiceListener);
	}

	public ServingCheckData isServingService(MAPApplicationContext dialogApplicationContext) {
		// Do not remove this !!!!
		if(this.testMode == 1) {
			// For reproducing FunctionalTestScenario.actionC MAPFunctionalTest
			//   - remove temporally this comment comment 
			ApplicationContextNameImpl ac = new ApplicationContextNameImpl();
			ac.setOid(new long[] { 1, 2, 3 });
			ServingCheckDataImpl i1 = new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect, ac);
			return i1;
		}
		
		if (dialogApplicationContext.getApplicationContext() == MAPApplicationContext.networkUnstructuredSsContextV2
				.getApplicationContext())
			return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
		else
			return new ServingCheckDataImpl(ServingCheckResult.AC_NotServing);
	}

	public Boolean processComponent(OperationCode oc, Parameter parameter, MAPDialog mapDialog, Long invokeId)
			throws MAPException {

		MAPDialogSupplementaryImpl mapDialogSupplementaryImpl = (MAPDialogSupplementaryImpl) mapDialog;

		//FIXME: add check for global code?
		if (oc.getLocalOperationCode() == MAPOperationCode.processUnstructuredSS_Request) {
			this.processUnstructuredSSRequest(parameter, mapDialogSupplementaryImpl, invokeId);
		} else if (oc.getLocalOperationCode() == MAPOperationCode.unstructuredSS_Request) {
			this.unstructuredSSRequest(parameter, mapDialogSupplementaryImpl, invokeId);
		} else {
			return false;
		}

		return true;
	}

	private void unstructuredSSRequest(Parameter parameter, MAPDialogSupplementaryImpl mapDialogImpl, Long invokeId)
			throws MAPException {
		if (parameter.getTag() == Tag.SEQUENCE) {
			Parameter[] parameters = parameter.getParameters();

			byte[] data = parameters[0].getData();

			// First Parameter is ussd-DataCodingScheme
			byte ussd_DataCodingScheme = data[0];

			// Second Parameter is ussd-String
			data = parameters[1].getData();
			USSDString ussdString = this.mapProviderImpl.getMapServiceFactory().createUSSDString(data, null);
			ussdString.decode();

			UnstructuredSSIndicationImpl unSSInd = new UnstructuredSSIndicationImpl(ussd_DataCodingScheme, ussdString);

			unSSInd.setInvokeId(invokeId);
			unSSInd.setMAPDialog(mapDialogImpl);

			for (MAPServiceListener serLis : this.serviceListeners) {
				((MAPServiceSupplementaryListener) serLis).onUnstructuredSSIndication(unSSInd);
			}
		} else {
			// TODO This is Error, what do we do next? Or should it even happen?
			// loger.error("Expected Parameter tag as SEQUENCE but received "
			// + parameter.getTag());
			throw new MAPException("Expected Parameter tag as SEQUENCE but received " + parameter.getTag());
		}
	}

	private void processUnstructuredSSRequest(Parameter parameter, MAPDialogSupplementaryImpl mapDialogImpl,
			Long invokeId) throws MAPException {
		if (parameter.getTag() == Tag.SEQUENCE) {
			Parameter[] parameters = parameter.getParameters();

			byte[] data = parameters[0].getData();

			// First Parameter is ussd-DataCodingScheme
			byte ussd_DataCodingScheme = data[0];

			// Second Parameter is ussd-String
			data = parameters[1].getData();

			USSDString ussdString = this.mapProviderImpl.getMapServiceFactory().createUSSDString(data, null);
			ussdString.decode();

			ProcessUnstructuredSSIndicationImpl procUnSSInd = new ProcessUnstructuredSSIndicationImpl(
					ussd_DataCodingScheme, ussdString);

			procUnSSInd.setInvokeId(invokeId);
			procUnSSInd.setMAPDialog(mapDialogImpl);

			// MSISDN
			if (parameters.length > 2) {
				Parameter msisdnParam = parameters[2];
				if (msisdnParam.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC && msisdnParam.getTag() == 0x00) {
					byte[] msisdnData = msisdnParam.getData();

					AsnInputStream ansIS = new AsnInputStream(new ByteArrayInputStream(msisdnData));

					AddressStringImpl msisdnAddStr = new AddressStringImpl();
					try {
						msisdnAddStr.decode(ansIS);
						procUnSSInd.setMSISDNAddressString(msisdnAddStr);
					} catch (IOException e) {
						// loger.error(
						// "Error while decoding the MSISDN AddressString ",
						// e);
						throw new MAPException("IOException when decoding AddressString", e);
					}
				}
			}

			for (MAPServiceListener serLis : this.serviceListeners) {
				((MAPServiceSupplementaryListener) serLis).onProcessUnstructuredSSIndication(procUnSSInd);
			}
		} else {
			// TODO This is Error, what do we do next? Or should it even happen?
			// loger.error("Expected Parameter tag as SEQUENCE but received "
			// + parameter.getTag());
			throw new MAPException("Expected Parameter tag as SEQUENCE but received " + parameter.getTag());
		}
	}

	public void setTestMode( int testMode ) {
		this.testMode = testMode;
	}
}
