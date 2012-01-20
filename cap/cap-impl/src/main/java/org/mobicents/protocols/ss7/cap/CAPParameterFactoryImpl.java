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

package org.mobicents.protocols.ss7.cap;

import java.util.ArrayList;

import org.mobicents.protocols.ss7.cap.EsiBcsm.OAnswerSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.OCalledPartyBusySpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.ODisconnectSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.ONoAnswerSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.RouteSelectFailureSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.TAnswerSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.TBusySpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.TDisconnectSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.TNoAnswerSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParameterFactory;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.CallAcceptedSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.ChargeIndicator;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OAbandonSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OAnswerSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OCalledPartyBusySpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OChangeOfPositionSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.ODisconnectSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OMidCallSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.ONoAnswerSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OTermSeizedSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.RouteSelectFailureSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.TAnswerSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.TBusySpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.TChangeOfPositionSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.TDisconnectSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.TMidCallSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.TNoAnswerSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber;
import org.mobicents.protocols.ss7.cap.api.isup.BearerCap;
import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.CallingPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.CauseCap;
import org.mobicents.protocols.ss7.cap.api.isup.Digits;
import org.mobicents.protocols.ss7.cap.api.isup.GenericNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.LocationNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.OriginalCalledNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.RedirectingPartyIDCap;
import org.mobicents.protocols.ss7.cap.api.primitives.AChChargingAddress;
import org.mobicents.protocols.ss7.cap.api.primitives.AppendFreeFormatData;
import org.mobicents.protocols.ss7.cap.api.primitives.BCSMEvent;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.CalledPartyBCDNumber;
import org.mobicents.protocols.ss7.cap.api.primitives.CriticalityType;
import org.mobicents.protocols.ss7.cap.api.primitives.DateAndTime;
import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.cap.api.primitives.ExtensionField;
import org.mobicents.protocols.ss7.cap.api.primitives.MonitorMode;
import org.mobicents.protocols.ss7.cap.api.primitives.ReceivingSideID;
import org.mobicents.protocols.ss7.cap.api.primitives.ScfID;
import org.mobicents.protocols.ss7.cap.api.primitives.SendingSideID;
import org.mobicents.protocols.ss7.cap.api.primitives.TimeAndTimezone;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AlertingPatternCap;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AudibleIndicator;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.BackwardServiceInteractionInd;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.BearerCapability;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAMELAChBillingChargingCharacteristics;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ConnectedNumberTreatmentInd;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CwTreatmentIndicator;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.DestinationRoutingAddress;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.DpSpecificCriteria;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.DpSpecificCriteriaAlt;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.EctTreatmentIndicator;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.FCIBCCCAMELsequence1;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ForwardServiceInteractionInd;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.HoldTreatmentIndicator;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.IPSSPCapabilities;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InitialDPArgExtension;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.LowLayerCompatibility;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.MidCallControlInfo;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.NAOliInfo;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformation;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformationType;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwo;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.TimeDurationChargingResult;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.TimeIfTariffSwitch;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.TimeInformation;
import org.mobicents.protocols.ss7.cap.dialog.CAPGprsReferenceNumberImpl;
import org.mobicents.protocols.ss7.cap.isup.BearerCapImpl;
import org.mobicents.protocols.ss7.cap.isup.CalledPartyNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.CallingPartyNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.CauseCapImpl;
import org.mobicents.protocols.ss7.cap.isup.DigitsImpl;
import org.mobicents.protocols.ss7.cap.isup.GenericNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.LocationNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.OriginalCalledNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.RedirectingPartyIDCapImpl;
import org.mobicents.protocols.ss7.cap.primitives.BCSMEventImpl;
import org.mobicents.protocols.ss7.cap.primitives.CAMELAChBillingChargingCharacteristicsImpl;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsImpl;
import org.mobicents.protocols.ss7.cap.primitives.CalledPartyBCDNumberImpl;
import org.mobicents.protocols.ss7.cap.primitives.DateAndTimeImpl;
import org.mobicents.protocols.ss7.cap.primitives.ExtensionFieldImpl;
import org.mobicents.protocols.ss7.cap.primitives.ReceivingSideIDImpl;
import org.mobicents.protocols.ss7.cap.primitives.ScfIDImpl;
import org.mobicents.protocols.ss7.cap.primitives.SendingSideIDImpl;
import org.mobicents.protocols.ss7.cap.primitives.TimeAndTimezoneImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.AlertingPatternCapImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.BearerCapabilityImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.DestinationRoutingAddressImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.DpSpecificCriteriaImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSMImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.FCIBCCCAMELsequence1Impl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.IPSSPCapabilitiesImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.InitialDPArgExtensionImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.NAOliInfoImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.RequestedInformationImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwoImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.TimeDurationChargingResultImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.TimeIfTariffSwitchImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.TimeInformationImpl;
import org.mobicents.protocols.ss7.inap.api.isup.HighLayerCompatibilityInap;
import org.mobicents.protocols.ss7.inap.api.primitives.BothwayThroughConnectionInd;
import org.mobicents.protocols.ss7.inap.api.primitives.LegID;
import org.mobicents.protocols.ss7.inap.api.primitives.LegType;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericDigits;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.LocationNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.OriginalCalledNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectingNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.UserServiceInformation;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.callhandling.UUData;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.MSClassmark2;
import org.mobicents.protocols.ss7.map.api.service.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.subscriberManagement.OfferedCamel4Functionalities;
import org.mobicents.protocols.ss7.map.api.service.subscriberManagement.SupportedCamelPhases;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class CAPParameterFactoryImpl implements CAPParameterFactory {

	@Override
	public CAPGprsReferenceNumber createCAPGprsReferenceNumber(Integer destinationReference, Integer originationReference) {
		return new CAPGprsReferenceNumberImpl(destinationReference, originationReference);
	}

	@Override
	public RouteSelectFailureSpecificInfo createRouteSelectFailureSpecificInfo(CauseCap failureCause) {
		return new RouteSelectFailureSpecificInfoImpl(failureCause);
	}

	@Override
	public CauseCap createCauseCap(byte[] data) {
		return new CauseCapImpl(data);
	}

	@Override
	public CauseCap createCauseCap(CauseIndicators causeIndicators) throws CAPException {
		return new CauseCapImpl(causeIndicators);
	}

	@Override
	public DpSpecificCriteria createDpSpecificCriteria(Integer applicationTimer) {
		return new DpSpecificCriteriaImpl(applicationTimer);
	}

	@Override
	public DpSpecificCriteria createDpSpecificCriteria(MidCallControlInfo midCallControlInfo) {
		return new DpSpecificCriteriaImpl(midCallControlInfo);
	}

	@Override
	public DpSpecificCriteria createDpSpecificCriteria(DpSpecificCriteriaAlt dpSpecificCriteriaAlt) {
		return new DpSpecificCriteriaImpl(dpSpecificCriteriaAlt);
	}

	@Override
	public BCSMEvent createBCSMEvent(EventTypeBCSM eventTypeBCSM, MonitorMode monitorMode, LegID legID, DpSpecificCriteria dpSpecificCriteria,
			boolean automaticRearm) {
		return new BCSMEventImpl(eventTypeBCSM, monitorMode, legID, dpSpecificCriteria, automaticRearm);
	}

	@Override
	public CalledPartyBCDNumber createCalledPartyBCDNumber(byte[] data) {
		return new CalledPartyBCDNumberImpl(data);
	}

	@Override
	public ExtensionField createExtensionField(Integer localCode, CriticalityType criticalityType, byte[] data) {
		return new ExtensionFieldImpl(localCode, criticalityType, data);
	}

	@Override
	public ExtensionField createExtensionField(long[] globalCode, CriticalityType criticalityType, byte[] data) {
		return new ExtensionFieldImpl(globalCode, criticalityType, data);
	}

	@Override
	public CAPExtensions createCAPExtensions(ExtensionField[] fieldsList) {
		return new CAPExtensionsImpl(fieldsList);
	}

	@Override
	public CAMELAChBillingChargingCharacteristics createCAMELAChBillingChargingCharacteristics(byte[] data) {
		return new CAMELAChBillingChargingCharacteristicsImpl(data);
	}

	@Override
	public CAMELAChBillingChargingCharacteristics createCAMELAChBillingChargingCharacteristics(long maxCallPeriodDuration, boolean releaseIfdurationExceeded,
			Long tariffSwitchInterval, AudibleIndicator audibleIndicator, CAPExtensions extensions, boolean isCAPVersion3orLater) {
		return new CAMELAChBillingChargingCharacteristicsImpl(maxCallPeriodDuration, releaseIfdurationExceeded, tariffSwitchInterval, audibleIndicator,
				extensions, isCAPVersion3orLater);
	}

	@Override
	public DateAndTime createDateAndTime(int year, int month, int day, int hour, int minute, int second) {
		return new DateAndTimeImpl(year, month, day, hour, minute, second);
	}

	@Override
	public TimeAndTimezone createTimeAndTimezone(int year, int month, int day, int hour, int minute, int second, int timeZone) {
		return new TimeAndTimezoneImpl(year, month, day, hour, minute, second, timeZone);
	}

	@Override
	public SendingSideID createSendingSideID(LegType sendingSideID) {
		return new SendingSideIDImpl(sendingSideID);
	}

	@Override
	public ReceivingSideID createReceivingSideID(LegType receivingSideID) {
		return new ReceivingSideIDImpl(receivingSideID);
	}

	@Override
	public BearerCap createBearerCap(byte[] data) {
		return new BearerCapImpl(data);
	}

	@Override
	public BearerCap createBearerCap(UserServiceInformation userServiceInformation) throws CAPException {
		return new BearerCapImpl(userServiceInformation);
	}

	@Override
	public BearerCapability createBearerCapability(BearerCap bearerCap) {
		return new BearerCapabilityImpl(bearerCap);
	}

	@Override
	public Digits createDigits(byte[] data) {
		return new DigitsImpl(data);
	}

	@Override
	public Digits createDigits(GenericNumber genericNumber) throws CAPException {
		return new DigitsImpl(genericNumber);
	}

	@Override
	public Digits createDigits(GenericDigits genericDigits) throws CAPException {
		return new DigitsImpl(genericDigits);
	}

	@Override
	public CalledPartyNumberCap createCalledPartyNumberCap(byte[] data) {
		return new CalledPartyNumberCapImpl(data);
	}

	@Override
	public CalledPartyNumberCap createCalledPartyNumberCap(CalledPartyNumber calledPartyNumber) throws CAPException {
		return new CalledPartyNumberCapImpl(calledPartyNumber);
	}

	@Override
	public CallingPartyNumberCap createCallingPartyNumberCap(byte[] data) {
		return new CallingPartyNumberCapImpl(data);
	}

	@Override
	public CallingPartyNumberCap createCallingPartyNumberCap(CallingPartyNumber callingPartyNumber) throws CAPException {
		return new CallingPartyNumberCapImpl(callingPartyNumber);
	}

	@Override
	public GenericNumberCap createGenericNumberCap(byte[] data) {
		return new GenericNumberCapImpl(data);
	}

	@Override
	public GenericNumberCap createGenericNumberCap(GenericNumber genericNumber) throws CAPException {
		return new GenericNumberCapImpl(genericNumber);
	}

	@Override
	public LocationNumberCap createLocationNumberCap(byte[] data) {
		return new LocationNumberCapImpl(data);
	}

	@Override
	public LocationNumberCap createLocationNumberCap(LocationNumber locationNumber) throws CAPException {
		return new LocationNumberCapImpl(locationNumber);
	}

	@Override
	public OriginalCalledNumberCap createOriginalCalledNumberCap(byte[] data) {
		return new OriginalCalledNumberCapImpl(data);
	}

	@Override
	public OriginalCalledNumberCap createOriginalCalledNumberCap(OriginalCalledNumber originalCalledNumber) throws CAPException {
		return new OriginalCalledNumberCapImpl(originalCalledNumber);
	}

	@Override
	public RedirectingPartyIDCap createRedirectingPartyIDCap(byte[] data) {
		return new RedirectingPartyIDCapImpl(data);
	}

	@Override
	public RedirectingPartyIDCap createRedirectingPartyIDCap(RedirectingNumber redirectingNumber) throws CAPException {
		return new RedirectingPartyIDCapImpl(redirectingNumber);
	}

	@Override
	public OCalledPartyBusySpecificInfo createOCalledPartyBusySpecificInfo(CauseCap busyCause) {
		return new OCalledPartyBusySpecificInfoImpl(busyCause);
	}

	@Override
	public ONoAnswerSpecificInfo createONoAnswerSpecificInfo() {
		return new ONoAnswerSpecificInfoImpl();
	}

	@Override
	public OAnswerSpecificInfo createOAnswerSpecificInfo(CalledPartyNumberCap destinationAddress, boolean orCall, boolean forwardedCall,
			ChargeIndicator chargeIndicator, ExtBasicServiceCode extBasicServiceCode, ExtBasicServiceCode extBasicServiceCode2) {
		return new OAnswerSpecificInfoImpl(destinationAddress, orCall, forwardedCall, chargeIndicator, extBasicServiceCode, extBasicServiceCode2);
	}

	@Override
	public ODisconnectSpecificInfo createODisconnectSpecificInfo(CauseCap releaseCause) {
		return new ODisconnectSpecificInfoImpl(releaseCause);
	}

	@Override
	public TBusySpecificInfo createTBusySpecificInfo(CauseCap busyCause, boolean callForwarded, boolean routeNotPermitted,
			CalledPartyNumberCap forwardingDestinationNumber) {
		return new TBusySpecificInfoImpl(busyCause, callForwarded, routeNotPermitted, forwardingDestinationNumber);
	}

	@Override
	public TNoAnswerSpecificInfo createTNoAnswerSpecificInfo(boolean callForwarded, CalledPartyNumberCap forwardingDestinationNumber) {
		return new TNoAnswerSpecificInfoImpl(callForwarded, forwardingDestinationNumber);
	}

	@Override
	public TAnswerSpecificInfo createTAnswerSpecificInfo(CalledPartyNumberCap destinationAddress, boolean orCall, boolean forwardedCall,
			ChargeIndicator chargeIndicator, ExtBasicServiceCode extBasicServiceCode, ExtBasicServiceCode extBasicServiceCode2) {
		return new TAnswerSpecificInfoImpl(destinationAddress, orCall, forwardedCall, chargeIndicator, extBasicServiceCode, extBasicServiceCode2);
	}

	@Override
	public TDisconnectSpecificInfo createTDisconnectSpecificInfo(CauseCap releaseCause) {
		return new TDisconnectSpecificInfoImpl(releaseCause);
	}

	@Override
	public DestinationRoutingAddress createDestinationRoutingAddress(ArrayList<CalledPartyNumberCap> calledPartyNumber) {
		return new DestinationRoutingAddressImpl(calledPartyNumber);
	}

	@Override
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(RouteSelectFailureSpecificInfo routeSelectFailureSpecificInfo) {
		return new EventSpecificInformationBCSMImpl(routeSelectFailureSpecificInfo);
	}

	@Override
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(OCalledPartyBusySpecificInfo oCalledPartyBusySpecificInfo) {
		return new EventSpecificInformationBCSMImpl(oCalledPartyBusySpecificInfo);
	}

	@Override
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(ONoAnswerSpecificInfo oNoAnswerSpecificInfo) {
		return new EventSpecificInformationBCSMImpl(oNoAnswerSpecificInfo);
	}

	@Override
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(OAnswerSpecificInfo oAnswerSpecificInfo) {
		return new EventSpecificInformationBCSMImpl(oAnswerSpecificInfo);
	}

	@Override
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(OMidCallSpecificInfo oMidCallSpecificInfo) {
		return new EventSpecificInformationBCSMImpl(oMidCallSpecificInfo);
	}

	@Override
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(ODisconnectSpecificInfo oDisconnectSpecificInfo) {
		return new EventSpecificInformationBCSMImpl(oDisconnectSpecificInfo);
	}

	@Override
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(TBusySpecificInfo tBusySpecificInfo) {
		return new EventSpecificInformationBCSMImpl(tBusySpecificInfo);
	}

	@Override
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(TNoAnswerSpecificInfo tNoAnswerSpecificInfo) {
		return new EventSpecificInformationBCSMImpl(tNoAnswerSpecificInfo);
	}

	@Override
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(TAnswerSpecificInfo tAnswerSpecificInfo) {
		return new EventSpecificInformationBCSMImpl(tAnswerSpecificInfo);
	}

	@Override
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(TMidCallSpecificInfo tMidCallSpecificInfo) {
		return new EventSpecificInformationBCSMImpl(tMidCallSpecificInfo);
	}

	@Override
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(TDisconnectSpecificInfo tDisconnectSpecificInfo) {
		return new EventSpecificInformationBCSMImpl(tDisconnectSpecificInfo);
	}

	@Override
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(OTermSeizedSpecificInfo oTermSeizedSpecificInfo) {
		return new EventSpecificInformationBCSMImpl(oTermSeizedSpecificInfo);
	}

	@Override
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(CallAcceptedSpecificInfo callAcceptedSpecificInfo) {
		return new EventSpecificInformationBCSMImpl(callAcceptedSpecificInfo);
	}

	@Override
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(OAbandonSpecificInfo oAbandonSpecificInfo) {
		return new EventSpecificInformationBCSMImpl(oAbandonSpecificInfo);
	}

	@Override
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(OChangeOfPositionSpecificInfo oChangeOfPositionSpecificInfo) {
		return new EventSpecificInformationBCSMImpl(oChangeOfPositionSpecificInfo);
	}

	@Override
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(TChangeOfPositionSpecificInfo tChangeOfPositionSpecificInfo) {
		return new EventSpecificInformationBCSMImpl(tChangeOfPositionSpecificInfo);
	}

	@Override
	public RequestedInformation createRequestedInformation_CallAttemptElapsedTime(int callAttemptElapsedTimeValue) {
		return new RequestedInformationImpl(RequestedInformationType.callAttemptElapsedTime, callAttemptElapsedTimeValue);
	}

	@Override
	public RequestedInformation createRequestedInformation_CallConnectedElapsedTime(int callConnectedElapsedTimeValue) {
		return new RequestedInformationImpl(RequestedInformationType.callConnectedElapsedTime, callConnectedElapsedTimeValue);
	}

	@Override
	public RequestedInformation createRequestedInformation_CallStopTime(DateAndTime callStopTimeValue) {
		return new RequestedInformationImpl(callStopTimeValue);
	}

	@Override
	public RequestedInformation createRequestedInformation_ReleaseCause(CauseCap releaseCauseValue) {
		return new RequestedInformationImpl(releaseCauseValue);
	}

	@Override
	public TimeDurationChargingResult createTimeDurationChargingResult(ReceivingSideID partyToCharge, TimeInformation timeInformation, boolean legActive,
			boolean callLegReleasedAtTcpExpiry, CAPExtensions extensions, AChChargingAddress aChChargingAddress) {
		return new TimeDurationChargingResultImpl(partyToCharge, timeInformation, legActive, callLegReleasedAtTcpExpiry, extensions, aChChargingAddress);
	}

	@Override
	public TimeIfTariffSwitch createTimeIfTariffSwitch(int timeSinceTariffSwitch, Integer tariffSwitchInterval) {
		return new TimeIfTariffSwitchImpl(timeSinceTariffSwitch, tariffSwitchInterval);
	}

	@Override
	public TimeInformation createTimeInformation(int timeIfNoTariffSwitch) {
		return new TimeInformationImpl(timeIfNoTariffSwitch);
	}

	@Override
	public TimeInformation createTimeInformation(TimeIfTariffSwitch timeIfTariffSwitch) {
		return new TimeInformationImpl(timeIfTariffSwitch);
	}

	@Override
	public IPSSPCapabilities createIPSSPCapabilities(boolean IPRoutingAddressSupported, boolean VoiceBackSupported,
			boolean VoiceInformationSupportedViaSpeechRecognition, boolean VoiceInformationSupportedViaVoiceRecognition,
			boolean GenerationOfVoiceAnnouncementsFromTextSupported, byte[] extraData) {
		return new IPSSPCapabilitiesImpl(IPRoutingAddressSupported, VoiceBackSupported, VoiceInformationSupportedViaSpeechRecognition,
				VoiceInformationSupportedViaVoiceRecognition, GenerationOfVoiceAnnouncementsFromTextSupported, extraData);
	}

	@Override
	public InitialDPArgExtension createInitialDPArgExtension(ISDNAddressString gmscAddress, CalledPartyNumberCap forwardingDestinationNumber,
			MSClassmark2 msClassmark2, IMEI imei, SupportedCamelPhases supportedCamelPhases, OfferedCamel4Functionalities offeredCamel4Functionalities,
			BearerCapability bearerCapability2, ExtBasicServiceCode extBasicServiceCode2, HighLayerCompatibilityInap highLayerCompatibility2,
			LowLayerCompatibility lowLayerCompatibility, LowLayerCompatibility lowLayerCompatibility2, boolean enhancedDialledServicesAllowed, UUData uuData,
			boolean isCAPVersion3orLater) {
		return new InitialDPArgExtensionImpl(gmscAddress, forwardingDestinationNumber, msClassmark2, imei, supportedCamelPhases, offeredCamel4Functionalities,
				bearerCapability2, extBasicServiceCode2, highLayerCompatibility2, lowLayerCompatibility, lowLayerCompatibility2,
				enhancedDialledServicesAllowed, uuData, isCAPVersion3orLater);
	}

	@Override
	public AlertingPatternCap createAlertingPatternCap(AlertingPattern alertingPattern) {
		return new AlertingPatternCapImpl(alertingPattern);
	}

	@Override
	public AlertingPatternCap createAlertingPatternCap(byte[] data) {
		return new AlertingPatternCapImpl(data);
	}

	@Override
	public NAOliInfo createNAOliInfo(byte[] data) {
		return new NAOliInfoImpl(data);
	}

	@Override
	public NAOliInfo createNAOliInfo(int value) {
		return new NAOliInfoImpl(value);
	}

	@Override
	public ScfID createScfID(byte[] data) {
		return new ScfIDImpl(data);
	}

	@Override
	public ServiceInteractionIndicatorsTwo createServiceInteractionIndicatorsTwo(ForwardServiceInteractionInd forwardServiceInteractionInd,
			BackwardServiceInteractionInd backwardServiceInteractionInd, BothwayThroughConnectionInd bothwayThroughConnectionInd,
			ConnectedNumberTreatmentInd connectedNumberTreatmentInd, boolean nonCUGCall, HoldTreatmentIndicator holdTreatmentIndicator,
			CwTreatmentIndicator cwTreatmentIndicator, EctTreatmentIndicator ectTreatmentIndicator) {
		return new ServiceInteractionIndicatorsTwoImpl(forwardServiceInteractionInd, backwardServiceInteractionInd, bothwayThroughConnectionInd,
				connectedNumberTreatmentInd, nonCUGCall, holdTreatmentIndicator, cwTreatmentIndicator, ectTreatmentIndicator);
	}

	@Override
	public FCIBCCCAMELsequence1 createFCIBCCCAMELsequence1(byte[] freeFormatData, SendingSideID partyToCharge, AppendFreeFormatData appendFreeFormatData) {
		return new FCIBCCCAMELsequence1Impl(freeFormatData, partyToCharge, appendFreeFormatData);
	}
}
