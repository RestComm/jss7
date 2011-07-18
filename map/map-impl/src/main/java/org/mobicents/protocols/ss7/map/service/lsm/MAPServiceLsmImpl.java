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
package org.mobicents.protocols.ss7.map.service.lsm;

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
import org.mobicents.protocols.ss7.map.api.service.lsm.MAPDialogLsm;
import org.mobicents.protocols.ss7.map.api.service.lsm.MAPServiceLsm;
import org.mobicents.protocols.ss7.map.api.service.lsm.MAPServiceLsmListener;
import org.mobicents.protocols.ss7.map.dialog.ServingCheckDataImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * @author amit bhayani
 * 
 */
public class MAPServiceLsmImpl extends MAPServiceBaseImpl implements MAPServiceLsm {

	/**
	 * @param mapProviderImpl
	 */
	protected MAPServiceLsmImpl(MAPProviderImpl mapProviderImpl) {
		super(mapProviderImpl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.MAPServiceBase#isServingService(org
	 * .mobicents.protocols.ss7.map.api.MAPApplicationContext)
	 */
	@Override
	public ServingCheckData isServingService(MAPApplicationContext dialogApplicationContext) {
		if (dialogApplicationContext.getApplicationContextName() == MAPApplicationContextName.locationSvcEnquiryContext) {
			if (dialogApplicationContext.getApplicationContextVersion().getVersion() == 3) {
				return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
			} else {
				return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect);
			}
		} else if (dialogApplicationContext.getApplicationContextName() == MAPApplicationContextName.locationSvcGatewayContext) {
			if (dialogApplicationContext.getApplicationContextVersion().getVersion() == 3) {
				return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
			} else {
				return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect);
			}
		}

		return new ServingCheckDataImpl(ServingCheckResult.AC_NotServing);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.MAPServiceBase#processComponent(org
	 * .mobicents.protocols.ss7.tcap.asn.comp.ComponentType,
	 * org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode,
	 * org.mobicents.protocols.ss7.tcap.asn.comp.Parameter,
	 * org.mobicents.protocols.ss7.map.api.MAPDialog, java.lang.Long,
	 * java.lang.Long)
	 */
	@Override
	public void processComponent(ComponentType compType, OperationCode oc, Parameter parameter, MAPDialog mapDialog, Long invokeId, Long linkedId)
			throws MAPParsingComponentException {
		MAPDialogLsmImpl mAPDialogLsmImpl = (MAPDialogLsmImpl) mapDialog;

		Long ocValue = oc.getLocalOperationCode();
		if (ocValue == null)
			new MAPParsingComponentException("", MAPParsingComponentExceptionReason.UnrecognizedOperation);

		long ocValueInt = ocValue;
		int ocValueInt2 = (int) ocValueInt;
		switch (ocValueInt2) {
		case MAPOperationCode.provideSubscriberLocation:
			if (compType == ComponentType.Invoke) {
				this.provideSubscriberLocationReq(parameter, mAPDialogLsmImpl, invokeId);
			} else {
				this.provideSubscriberLocationRes(parameter, mAPDialogLsmImpl, invokeId);
			}
			break;
		case MAPOperationCode.subscriberLocationReport:
			if (compType == ComponentType.Invoke) {
				this.subscriberLocationReportReq(parameter, mAPDialogLsmImpl, invokeId);
			} else {
				this.subscriberLocationReportRes(parameter, mAPDialogLsmImpl, invokeId);
			}
			break;
		case MAPOperationCode.sendRoutingInfoForLCS:
			if (compType == ComponentType.Invoke) {
				this.sendRoutingInfoForLCSReq(parameter, mAPDialogLsmImpl, invokeId);
			} else {
				this.sendRoutingInfoForLCSRes(parameter, mAPDialogLsmImpl, invokeId);
			}
			break;
		default:
			new MAPParsingComponentException("", MAPParsingComponentExceptionReason.UnrecognizedOperation);
		}
	}

	private void provideSubscriberLocationReq(Parameter param, MAPDialogLsmImpl mapDialogImpl, Long invokeId) throws MAPParsingComponentException {
		if (param == null) {
			throw new MAPParsingComponentException("Error while decoding ProvideSubscriberLocationRequest: Parameter is mandatory but not found",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

		ProvideSubscriberLocationRequestIndicationImpl provideSubsLoctReqInd = new ProvideSubscriberLocationRequestIndicationImpl();
		provideSubsLoctReqInd.decode(param);
		
		provideSubsLoctReqInd.setInvokeId(invokeId);
		provideSubsLoctReqInd.setMAPDialog(mapDialogImpl);

		for (MAPServiceListener serLis : this.serviceListeners) {
			((MAPServiceLsmListener) serLis).onProvideSubscriberLocationRequestIndication(provideSubsLoctReqInd);
		}

	}

	private void provideSubscriberLocationRes(Parameter param, MAPDialogLsmImpl mapDialogImpl, Long invokeId) throws MAPParsingComponentException {
		if (param == null) {
			throw new MAPParsingComponentException("Error while decoding ProvideSubscriberLocationRequest: Parameter is mandatory but not found",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

		ProvideSubscriberLocationResponseIndicationImpl provideSubsLoctResInd = new ProvideSubscriberLocationResponseIndicationImpl();
		provideSubsLoctResInd.decode(param);
		
		provideSubsLoctResInd.setInvokeId(invokeId);
		provideSubsLoctResInd.setMAPDialog(mapDialogImpl);

		for (MAPServiceListener serLis : this.serviceListeners) {
			((MAPServiceLsmListener) serLis).onProvideSubscriberLocationResponseIndication(provideSubsLoctResInd);
		}

	}

	private void subscriberLocationReportReq(Parameter parameter, MAPDialogLsmImpl mapDialogImpl, Long invokeId) throws MAPParsingComponentException {
		if (parameter == null) {
			throw new MAPParsingComponentException("Error while decoding subscriberLocationReport: Parameter is mandatory but not found",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

		SubscriberLocationReportRequestIndicationImpl reqInd = new SubscriberLocationReportRequestIndicationImpl();
		reqInd.decode(parameter);
		
		reqInd.setInvokeId(invokeId);
		reqInd.setMAPDialog(mapDialogImpl);

		for (MAPServiceListener serLis : this.serviceListeners) {
			((MAPServiceLsmListener) serLis).onSubscriberLocationReportRequestIndication(reqInd);
		}
	}

	private void subscriberLocationReportRes(Parameter parameter, MAPDialogLsmImpl mapDialogImpl, Long invokeId) throws MAPParsingComponentException {
		if (parameter == null) {
			throw new MAPParsingComponentException("Error while decoding subscriberLocationReport: Parameter is mandatory but not found",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

		SubscriberLocationReportResponseIndicationImpl resInd = new SubscriberLocationReportResponseIndicationImpl();
		resInd.decode(parameter);
		
		resInd.setInvokeId(invokeId);
		resInd.setMAPDialog(mapDialogImpl);

		for (MAPServiceListener serLis : this.serviceListeners) {
			((MAPServiceLsmListener) serLis).onSubscriberLocationReportResponseIndication(resInd);
		}
	}

	private void sendRoutingInfoForLCSReq(Parameter parameter, MAPDialogLsmImpl mapDialogImpl, Long invokeId) throws MAPParsingComponentException {
		if (parameter == null) {
			throw new MAPParsingComponentException("Error while decoding sendRoutingInfoForLCS: Parameter is mandatory but not found",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

		SendRoutingInfoForLCSRequestIndicationImpl reqInd = new SendRoutingInfoForLCSRequestIndicationImpl();
		reqInd.decode(parameter);
		
		reqInd.setInvokeId(invokeId);
		reqInd.setMAPDialog(mapDialogImpl);

		for (MAPServiceListener serLis : this.serviceListeners) {
			((MAPServiceLsmListener) serLis).onSendRoutingInforForLCSRequestIndication(reqInd);
		}
	}

	private void sendRoutingInfoForLCSRes(Parameter parameter, MAPDialogLsmImpl mapDialogImpl, Long invokeId) throws MAPParsingComponentException {
		if (parameter == null) {
			throw new MAPParsingComponentException("Error while decoding sendRoutingInfoForLCS: Parameter is mandatory but not found",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

		SendRoutingInfoForLCSResponseIndicationImpl resInd = new SendRoutingInfoForLCSResponseIndicationImpl();
		resInd.decode(parameter);
		
		resInd.setInvokeId(invokeId);
		resInd.setMAPDialog(mapDialogImpl);

		for (MAPServiceListener serLis : this.serviceListeners) {
			((MAPServiceLsmListener) serLis).onSendRoutingInforForLCSResponseIndication(resInd);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.MAPServiceBase#checkInvokeTimeOut
	 * (org.mobicents.protocols.ss7.map.api.MAPDialog,
	 * org.mobicents.protocols.ss7.tcap.asn.comp.Invoke)
	 */
	@Override
	public Boolean checkInvokeTimeOut(MAPDialog dialog, Invoke invoke) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.lsm.MAPServiceLsm#createNewDialog
	 * (org.mobicents.protocols.ss7.map.api.MAPApplicationContext,
	 * org.mobicents.protocols.ss7.sccp.parameter.SccpAddress,
	 * org.mobicents.protocols.ss7.map.api.dialog.AddressString,
	 * org.mobicents.protocols.ss7.sccp.parameter.SccpAddress,
	 * org.mobicents.protocols.ss7.map.api.dialog.AddressString)
	 */
	@Override
	public MAPDialogLsm createNewDialog(MAPApplicationContext appCntx, SccpAddress origAddress, AddressString origReference, SccpAddress destAddress,
			AddressString destReference) throws MAPException {
		// We cannot create a dialog if the service is not activated
		if (!this.isActivated())
			throw new MAPException("Cannot create MAPDialogLsm because MAPServiceLsm is not activated");

		Dialog tcapDialog = this.createNewTCAPDialog(origAddress, destAddress);

		MAPDialogLsmImpl dialog = new MAPDialogLsmImpl(appCntx, tcapDialog, this.mapProviderImpl, this, origReference, destReference);

		this.PutMADDialogIntoCollection(dialog);

		return dialog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.MAPServiceLsm#
	 * addMAPServiceListener
	 * (org.mobicents.protocols.ss7.map.api.service.lsm.MAPServiceLsmListener)
	 */
	@Override
	public void addMAPServiceListener(MAPServiceLsmListener mapServiceListener) {
		super.addMAPServiceListener(mapServiceListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.MAPServiceLsm#
	 * removeMAPServiceListener
	 * (org.mobicents.protocols.ss7.map.api.service.lsm.MAPServiceLsmListener)
	 */
	@Override
	public void removeMAPServiceListener(MAPServiceLsmListener mapServiceListener) {
		super.removeMAPServiceListener(mapServiceListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.MAPServiceBaseImpl#createNewDialogIncoming
	 * (org.mobicents.protocols.ss7.map.api.MAPApplicationContext,
	 * org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog)
	 */
	@Override
	protected MAPDialogImpl createNewDialogIncoming(MAPApplicationContext appCntx, Dialog tcapDialog) {
		return new MAPDialogLsmImpl(appCntx, tcapDialog, this.mapProviderImpl, this, null, null);
	}

}
