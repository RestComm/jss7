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

package org.mobicents.protocols.ss7.map.service.lsm;

import java.util.BitSet;

import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.MAPDialogImpl;
import org.mobicents.protocols.ss7.map.MAPProviderImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPServiceBase;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaEventInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.CellGlobalIdOrServiceAreaIdOrLAI;
import org.mobicents.protocols.ss7.map.api.service.lsm.DeferredmtlrData;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSCodeword;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSQoS;
import org.mobicents.protocols.ss7.map.api.service.lsm.LocationType;
import org.mobicents.protocols.ss7.map.api.service.lsm.MAPDialogLsm;
import org.mobicents.protocols.ss7.map.api.service.lsm.SLRArgExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberIdentity;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;

/**
 * @author amit bhayani
 * 
 */
public class MAPDialogLsmImpl extends MAPDialogImpl implements MAPDialogLsm {

	/**
	 * @param appCntx
	 * @param tcapDialog
	 * @param mapProviderImpl
	 * @param mapService
	 * @param origReference
	 * @param destReference
	 */
	protected MAPDialogLsmImpl(MAPApplicationContext appCntx, Dialog tcapDialog, MAPProviderImpl mapProviderImpl, MAPServiceBase mapService,
			AddressString origReference, AddressString destReference) {
		super(appCntx, tcapDialog, mapProviderImpl, mapService, origReference, destReference);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.MAPDialogLsm#
	 * addSubscriberLocationReportRequestIndication
	 * (org.mobicents.protocols.ss7.map.api.service.lsm.LCSEvent,
	 * org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID,
	 * org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo,
	 * org.mobicents.protocols.ss7.map.api.dialog.IMSI,
	 * org.mobicents.protocols.ss7.map.api.dialog.AddressString,
	 * org.mobicents.protocols.ss7.map.api.dialog.AddressString,
	 * org.mobicents.protocols.ss7.map.api.dialog.AddressString,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * int, java.lang.String,
	 * org.mobicents.protocols.ss7.map.api.service.lsm.DeferredmtlrData,
	 * java.lang.String, org.mobicents.protocols.ss7.map.api.service.lsm.
	 * CellGlobalIdOrServiceAreaIdOrLAI, java.lang.String, int, boolean,
	 * org.mobicents
	 * .protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator)
	 */
	@Override
	public Long addSubscriberLocationReportRequestIndication(LCSEvent lcsEvent, LCSClientID lcsClientID, LCSLocationInfo lcsLocationInfo,
			ISDNAddressString msisdn, IMSI imsi, IMEI imei, ISDNAddressString naEsrd, ISDNAddressString naEsrk, byte[] locationEstimate,
			Integer ageOfLocationEstimate, SLRArgExtensionContainer slrArgExtensionContainer, byte[] addLocationEstimate, DeferredmtlrData deferredmtlrData,
			Byte lcsReferenceNumber, byte[] geranPositioningData, byte[] utranPositioningData, CellGlobalIdOrServiceAreaIdOrLAI cellIdOrSai,
			byte[] hgmlcAddress, Integer lcsServiceTypeID, Boolean saiPresent, Boolean pseudonymIndicator,
			AccuracyFulfilmentIndicator accuracyFulfilmentIndicator) throws MAPException {

		if (lcsEvent == null || lcsClientID == null || lcsLocationInfo == null) {
			throw new MAPException("Mandatroy parameters lCSEvent, lCSClientID or lCSLocationInfo cannot be null");
		}

		try {
			Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();

			// Operation Code
			OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
			oc.setLocalOperationCode((long) MAPOperationCode.provideSubscriberLocation);

			SubscriberLocationReportRequestIndicationImpl req = new SubscriberLocationReportRequestIndicationImpl(lcsEvent, lcsClientID, lcsLocationInfo,
					msisdn, imsi, imei, naEsrd, naEsrk, locationEstimate, ageOfLocationEstimate, slrArgExtensionContainer, addLocationEstimate,
					deferredmtlrData, lcsReferenceNumber, geranPositioningData, utranPositioningData, cellIdOrSai, hgmlcAddress, lcsServiceTypeID, saiPresent,
					pseudonymIndicator, accuracyFulfilmentIndicator);

			AsnOutputStream asnOs = new AsnOutputStream();
			req.encode(asnOs);

			Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
			p.setTagClass(Tag.CLASS_UNIVERSAL);
			p.setTag(Tag.SEQUENCE);
			p.setPrimitive(false);
			p.setData(asnOs.toByteArray());

			invoke.setParameter(p);

			Long invokeId = this.tcapDialog.getNewInvokeId();
			invoke.setInvokeId(invokeId);

			this.sendInvokeComponent(invoke);

			return invokeId;
		} catch (TCAPException e) {
			throw new MAPException(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.MAPDialogLsm#
	 * addSubscriberLocationReportResponseIndication(long,
	 * org.mobicents.protocols.ss7.map.api.dialog.MAPExtensionContainer,
	 * org.mobicents.protocols.ss7.map.api.dialog.AddressString,
	 * org.mobicents.protocols.ss7.map.api.dialog.AddressString)
	 */
	@Override
	public void addSubscriberLocationReportResponseIndication(long invokeId, ISDNAddressString naEsrd, ISDNAddressString naEsrk,
			MAPExtensionContainer extensionContainer) throws MAPException {

		ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCResultLastRequest();
		resultLast.setInvokeId(invokeId);

		// Operation Code
		OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
		oc.setLocalOperationCode((long) MAPOperationCode.provideSubscriberLocation);
		resultLast.setOperationCode(oc);

		SubscriberLocationReportResponseIndicationImpl resInd = new SubscriberLocationReportResponseIndicationImpl(naEsrd, naEsrk, extensionContainer);

		AsnOutputStream asnOs = new AsnOutputStream();
		resInd.encode(asnOs);

		Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
		p.setTagClass(Tag.CLASS_UNIVERSAL);
		p.setTag(Tag.SEQUENCE);
		p.setPrimitive(false);
		p.setData(asnOs.toByteArray());

		resultLast.setParameter(p);

		this.sendReturnResultLastComponent(resultLast);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.MAPDialogLsm#
	 * addSendRoutingInforForLCSRequestIndication
	 * (org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString,
	 * org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberIdentity,
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer)
	 */
	@Override
	public Long addSendRoutingInforForLCSRequestIndication(ISDNAddressString mlcNumber, SubscriberIdentity targetMS, MAPExtensionContainer extensionContainer)
			throws MAPException {

		if (mlcNumber == null || targetMS == null) {
			throw new MAPException("Mandatroy parameters mlcNumber or targetMS cannot be null");
		}

		try {
			Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();

			// Operation Code
			OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
			oc.setLocalOperationCode((long) MAPOperationCode.sendRoutingInfoForLCS);

			SendRoutingInfoForLCSRequestIndicationImpl req = new SendRoutingInfoForLCSRequestIndicationImpl(extensionContainer, targetMS, mlcNumber);

			AsnOutputStream asnOs = new AsnOutputStream();
			req.encode(asnOs);

			Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
			p.setTagClass(Tag.CLASS_UNIVERSAL);
			p.setTag(Tag.SEQUENCE);
			p.setPrimitive(false);
			p.setData(asnOs.toByteArray());

			invoke.setParameter(p);

			Long invokeId = this.tcapDialog.getNewInvokeId();
			invoke.setInvokeId(invokeId);

			this.sendInvokeComponent(invoke);

			return invokeId;
		} catch (TCAPException e) {
			throw new MAPException(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.MAPDialogLsm#
	 * addSendRoutingInforForLCSResponseIndication
	 * (org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberIdentity,
	 * org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo,
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer,
	 * byte[], byte[], byte[], byte[])
	 */
	@Override
	public void addSendRoutingInforForLCSResponseIndication(long invokeId, SubscriberIdentity targetMS, LCSLocationInfo lcsLocationInfo,
			MAPExtensionContainer extensionContainer, byte[] vgmlcAddress, byte[] hGmlcAddress, byte[] pprAddress, byte[] additionalVGmlcAddress)
			throws MAPException {

		if (targetMS == null || lcsLocationInfo == null) {
			throw new MAPException("Mandatroy parameters targetMS or lcsLocationInfo cannot be null");
		}

		ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCResultLastRequest();
		resultLast.setInvokeId(invokeId);

		// Operation Code
		OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
		oc.setLocalOperationCode((long) MAPOperationCode.sendRoutingInfoForLCS);
		resultLast.setOperationCode(oc);

		SendRoutingInfoForLCSResponseIndicationImpl resInd = new SendRoutingInfoForLCSResponseIndicationImpl(targetMS, lcsLocationInfo, extensionContainer,
				vgmlcAddress, hGmlcAddress, pprAddress, additionalVGmlcAddress);

		AsnOutputStream asnOs = new AsnOutputStream();
		resInd.encode(asnOs);

		Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
		p.setTagClass(Tag.CLASS_UNIVERSAL);
		p.setTag(Tag.SEQUENCE);
		p.setPrimitive(false);
		p.setData(asnOs.toByteArray());

		resultLast.setParameter(p);

		this.sendReturnResultLastComponent(resultLast);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.MAPDialogLsm#
	 * addProvideSubscriberLocationRequest
	 * (org.mobicents.protocols.ss7.map.api.service.lsm.LocationType,
	 * org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString,
	 * org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID,
	 * java.lang.Boolean, org.mobicents.protocols.ss7.map.api.primitives.IMSI,
	 * org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString,
	 * org.mobicents.protocols.ss7.map.api.primitives.LMSI,
	 * org.mobicents.protocols.ss7.map.api.primitives.IMEI, java.lang.Integer,
	 * org.mobicents.protocols.ss7.map.api.service.lsm.LCSQoS,
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer,
	 * java.util.BitSet, java.lang.Byte, java.lang.Integer,
	 * org.mobicents.protocols.ss7.map.api.service.lsm.LCSCodeword,
	 * org.mobicents.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck,
	 * org.mobicents.protocols.ss7.map.api.service.lsm.AreaEventInfo, byte[])
	 */
	@Override
	public Long addProvideSubscriberLocationRequest(LocationType locationType, ISDNAddressString mlcNumber, LCSClientID lcsClientID, Boolean privacyOverride,
			IMSI imsi, ISDNAddressString msisdn, LMSI lmsi, IMEI imei, Integer lcsPriority, LCSQoS lcsQoS, MAPExtensionContainer extensionContainer,
			BitSet supportedGADShapes, Byte lcsReferenceNumber, Integer lcsServiceTypeID, LCSCodeword lcsCodeword, LCSPrivacyCheck lcsPrivacyCheck,
			AreaEventInfo areaEventInfo, byte[] hgmlcAddress) throws MAPException {

		if (locationType == null || mlcNumber == null) {
			throw new MAPException("Mandatroy parameters locationType or mlcNumber cannot be null");
		}

		try {
			Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();

			// Operation Code
			OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
			oc.setLocalOperationCode((long) MAPOperationCode.provideSubscriberLocation);

			ProvideSubscriberLocationRequestIndicationImpl req = new ProvideSubscriberLocationRequestIndicationImpl(locationType, mlcNumber, lcsClientID,
					privacyOverride, imsi, msisdn, lmsi, imei, lcsPriority, lcsQoS, extensionContainer, supportedGADShapes, lcsReferenceNumber,
					lcsServiceTypeID, lcsCodeword, lcsPrivacyCheck, areaEventInfo, hgmlcAddress);

			AsnOutputStream asnOs = new AsnOutputStream();
			req.encode(asnOs);

			Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
			p.setTagClass(Tag.CLASS_UNIVERSAL);
			p.setTag(Tag.SEQUENCE);
			p.setPrimitive(false);
			p.setData(asnOs.toByteArray());

			invoke.setParameter(p);

			Long invokeId = this.tcapDialog.getNewInvokeId();
			invoke.setInvokeId(invokeId);

			this.sendInvokeComponent(invoke);

			return invokeId;
		} catch (TCAPException e) {
			throw new MAPException(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.MAPDialogLsm#
	 * addProvideSubscriberLocationResponse(long, byte[], byte[], byte[],
	 * java.lang.Integer, byte[],
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer,
	 * java.lang.Boolean, org.mobicents.protocols.ss7.map.api.service.lsm.
	 * CellGlobalIdOrServiceAreaIdOrLAI, java.lang.Boolean,
	 * org.mobicents.protocols
	 * .ss7.map.api.service.lsm.AccuracyFulfilmentIndicator)
	 */
	@Override
	public void addProvideSubscriberLocationResponse(long invokeId, byte[] locationEstimate, byte[] geranPositioningData, byte[] utranPositioningData,
			Integer ageOfLocationEstimate, byte[] additionalLocationEstimate, MAPExtensionContainer extensionContainer, Boolean deferredMTLRResponseIndicator,
			CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI, Boolean saiPresent, AccuracyFulfilmentIndicator accuracyFulfilmentIndicator)
			throws MAPException {

		if (locationEstimate == null) {
			throw new MAPException("Mandatroy parameters locationEstimate cannot be null");
		}

		ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCResultLastRequest();
		resultLast.setInvokeId(invokeId);

		// Operation Code
		OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
		oc.setLocalOperationCode((long) MAPOperationCode.provideSubscriberLocation);
		resultLast.setOperationCode(oc);

		ProvideSubscriberLocationResponseIndicationImpl resInd = new ProvideSubscriberLocationResponseIndicationImpl(locationEstimate, geranPositioningData,
				utranPositioningData, ageOfLocationEstimate, additionalLocationEstimate, extensionContainer, deferredMTLRResponseIndicator,
				cellGlobalIdOrServiceAreaIdOrLAI, saiPresent, accuracyFulfilmentIndicator);

		AsnOutputStream asnOs = new AsnOutputStream();
		resInd.encode(asnOs);

		Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
		p.setTagClass(Tag.CLASS_UNIVERSAL);
		p.setTag(Tag.SEQUENCE);
		p.setPrimitive(false);
		p.setData(asnOs.toByteArray());

		resultLast.setParameter(p);

		this.sendReturnResultLastComponent(resultLast);
	}

}
