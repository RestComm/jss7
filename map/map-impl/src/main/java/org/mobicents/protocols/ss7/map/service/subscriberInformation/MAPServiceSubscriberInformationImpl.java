/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.map.service.subscriberInformation;

import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.MAPDialogImpl;
import org.mobicents.protocols.ss7.map.MAPProviderImpl;
import org.mobicents.protocols.ss7.map.MAPServiceBaseImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.MAPServiceListener;
import org.mobicents.protocols.ss7.map.api.dialog.ServingCheckData;
import org.mobicents.protocols.ss7.map.api.dialog.ServingCheckResult;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.MAPDialogSubscriberInformation;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.MAPServiceSubscriberInformation;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.MAPServiceSubscriberInformationListener;
import org.mobicents.protocols.ss7.map.dialog.ServingCheckDataImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * @author abhayani
 * 
 */
public class MAPServiceSubscriberInformationImpl extends MAPServiceBaseImpl implements MAPServiceSubscriberInformation {

	protected Logger loger = Logger.getLogger(MAPServiceSubscriberInformationImpl.class);

	public MAPServiceSubscriberInformationImpl(MAPProviderImpl mapProviderImpl) {
		super(mapProviderImpl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.MAPServiceBase#createNewDialog(org
	 * .mobicents.protocols.ss7.map.api.MAPApplicationContext,
	 * org.mobicents.protocols.ss7.sccp.parameter.SccpAddress,
	 * org.mobicents.protocols.ss7.map.api.primitives.AddressString,
	 * org.mobicents.protocols.ss7.sccp.parameter.SccpAddress,
	 * org.mobicents.protocols.ss7.map.api.primitives.AddressString)
	 */
	public MAPDialogSubscriberInformation createNewDialog(MAPApplicationContext appCntx, SccpAddress origAddress, AddressString origReference,
			SccpAddress destAddress, AddressString destReference) throws MAPException {

		// We cannot create a dialog if the service is not activated
		if (!this.isActivated())
			throw new MAPException("Cannot create MAPDialogSubscriberInformation because MAPServiceSubscriberInformation is not activated");

		Dialog tcapDialog = this.createNewTCAPDialog(origAddress, destAddress);
		MAPDialogSubscriberInformationImpl dialog = new MAPDialogSubscriberInformationImpl(appCntx, tcapDialog, this.mapProviderImpl, this, origReference,
				destReference);

		this.putMAPDialogIntoCollection(dialog);

		return dialog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
	 * MAPServiceSubscriberInformation
	 * #addMAPServiceListener(org.mobicents.protocols
	 * .ss7.map.api.service.subscriberInformation
	 * .MAPServiceSubscriberInformationListener)
	 */
	public void addMAPServiceListener(MAPServiceSubscriberInformationListener mapServiceListener) {
		super.addMAPServiceListener(mapServiceListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
	 * MAPServiceSubscriberInformation
	 * #removeMAPServiceListener(org.mobicents.protocols
	 * .ss7.map.api.service.subscriberInformation
	 * .MAPServiceSubscriberInformationListener)
	 */
	public void removeMAPServiceListener(MAPServiceSubscriberInformationListener mapServiceListener) {
		super.addMAPServiceListener(mapServiceListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.MAPServiceBase#isServingService(org
	 * .mobicents.protocols.ss7.map.api.MAPApplicationContext)
	 */
	public ServingCheckData isServingService(MAPApplicationContext dialogApplicationContext) {
		MAPApplicationContextName ctx = dialogApplicationContext.getApplicationContextName();
		int vers = dialogApplicationContext.getApplicationContextVersion().getVersion();

		switch (ctx) {
		case anyTimeEnquiryContext:
			if (vers == 3) {
				return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
			}
		}

		return new ServingCheckDataImpl(ServingCheckResult.AC_NotServing);
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
		return new MAPDialogSubscriberInformationImpl(appCntx, tcapDialog, this.mapProviderImpl, this, null, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.MAPServiceBaseImpl#processComponent(org
	 * .mobicents.protocols.ss7.tcap.asn.comp.ComponentType,
	 * org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode,
	 * org.mobicents.protocols.ss7.tcap.asn.comp.Parameter,
	 * org.mobicents.protocols.ss7.map.api.MAPDialog, java.lang.Long,
	 * java.lang.Long)
	 */
	public void processComponent(ComponentType compType, OperationCode oc, Parameter parameter, MAPDialog mapDialog, Long invokeId, Long linkedId)
			throws MAPParsingComponentException {

		if (compType == ComponentType.Invoke && this.mapProviderImpl.isCongested()) {
			// we reject all supplementary services when congestion
			return;
		}
		MAPDialogSubscriberInformationImpl mapDialogSubscriberInformationImpl = (MAPDialogSubscriberInformationImpl) mapDialog;

		Long ocValue = oc.getLocalOperationCode();
		if (ocValue == null)
			new MAPParsingComponentException("", MAPParsingComponentExceptionReason.UnrecognizedOperation);

		long ocValueInt = ocValue;
		int ocValueInt2 = (int) ocValueInt;
		switch (ocValueInt2) {
		case MAPOperationCode.anyTimeInterrogation:
			if (compType == ComponentType.Invoke)
				this.processAnyTimeInterrogationRequest(parameter, mapDialogSubscriberInformationImpl, invokeId);
			else
				this.processAnyTimeInterrogationResponse(parameter, mapDialogSubscriberInformationImpl, invokeId);
			break;
		default:
			new MAPParsingComponentException("", MAPParsingComponentExceptionReason.UnrecognizedOperation);
		}
	}

	private void processAnyTimeInterrogationRequest(Parameter parameter, MAPDialogSubscriberInformationImpl mapDialogImpl, Long invokeId)
			throws MAPParsingComponentException {

		if (parameter == null)
			throw new MAPParsingComponentException("Error while decoding AnyTimeInterrogationRequestIndication: Parameter is mandatory but not found",
					MAPParsingComponentExceptionReason.MistypedParameter);

		if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
			throw new MAPParsingComponentException(
					"Error while decoding AnyTimeInterrogationRequestIndication: Bad tag or tagClass or parameter is primitive, received tag="
							+ parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

		byte[] buf = parameter.getData();
		AsnInputStream ais = new AsnInputStream(buf);

		AnyTimeInterrogationRequestImpl ind = new AnyTimeInterrogationRequestImpl();
		ind.decodeData(ais, buf.length);
		ind.setInvokeId(invokeId);
		ind.setMAPDialog(mapDialogImpl);

		for (MAPServiceListener serLis : this.serviceListeners) {
			try {
				((MAPServiceSubscriberInformationListener) serLis).onAnyTimeInterrogationRequest(ind);
			} catch (Exception e) {
				loger.error("Error processing ProcessUnstructuredSSRequestIndication: " + e.getMessage(), e);
			}
		}

	}

	private void processAnyTimeInterrogationResponse(Parameter parameter, MAPDialogSubscriberInformationImpl mapDialogImpl, Long invokeId)
			throws MAPParsingComponentException {

		if (parameter == null)
			throw new MAPParsingComponentException("Error while decoding AnyTimeInterrogationResponseIndication: Parameter is mandatory but not found",
					MAPParsingComponentExceptionReason.MistypedParameter);

		if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
			throw new MAPParsingComponentException(
					"Error while decoding AnyTimeInterrogationResponseIndication: Bad tag or tagClass or parameter is primitive, received tag="
							+ parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

		byte[] buf = parameter.getData();
		AsnInputStream ais = new AsnInputStream(buf);

		AnyTimeInterrogationResponseImpl ind = new AnyTimeInterrogationResponseImpl();
		ind.decodeData(ais, buf.length);
		ind.setInvokeId(invokeId);
		ind.setMAPDialog(mapDialogImpl);

		for (MAPServiceListener serLis : this.serviceListeners) {
			try {
				((MAPServiceSubscriberInformationListener) serLis).onAnyTimeInterrogationResponse(ind);
			} catch (Exception e) {
				loger.error("Error processing ProcessUnstructuredSSRequestIndication: " + e.getMessage(), e);
			}
		}

	}

}
