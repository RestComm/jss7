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

import org.mobicents.protocols.ss7.cap.EsiBcsm.RouteSelectFailureSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParameterFactory;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.RouteSelectFailureSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber;
import org.mobicents.protocols.ss7.cap.api.isup.AdditionalCallingPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.BearerCap;
import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.CallingPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.CauseCap;
import org.mobicents.protocols.ss7.cap.api.isup.GenericNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.LocationNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.OriginalCalledNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.RedirectingPartyIDCap;
import org.mobicents.protocols.ss7.cap.api.primitives.BCSMEvent;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.CalledPartyBCDNumber;
import org.mobicents.protocols.ss7.cap.api.primitives.CriticalityType;
import org.mobicents.protocols.ss7.cap.api.primitives.DateAndTime;
import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.cap.api.primitives.ExtensionField;
import org.mobicents.protocols.ss7.cap.api.primitives.MonitorMode;
import org.mobicents.protocols.ss7.cap.api.primitives.ReceivingSideID;
import org.mobicents.protocols.ss7.cap.api.primitives.SendingSideID;
import org.mobicents.protocols.ss7.cap.api.primitives.TimeAndTimezone;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AudibleIndicator;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.BearerCapability;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAMELAChBillingChargingCharacteristics;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.DpSpecificCriteria;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.DpSpecificCriteriaAlt;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.MidCallControlInfo;
import org.mobicents.protocols.ss7.cap.dialog.CAPGprsReferenceNumberImpl;
import org.mobicents.protocols.ss7.cap.isup.AdditionalCallingPartyNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.BearerCapImpl;
import org.mobicents.protocols.ss7.cap.isup.CalledPartyNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.CallingPartyNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.CauseCapImpl;
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
import org.mobicents.protocols.ss7.cap.primitives.SendingSideIDImpl;
import org.mobicents.protocols.ss7.cap.primitives.TimeAndTimezoneImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.BearerCapabilityImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.DpSpecificCriteriaImpl;
import org.mobicents.protocols.ss7.inap.api.primitives.LegID;
import org.mobicents.protocols.ss7.inap.api.primitives.LegType;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.LocationNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.OriginalCalledNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectingNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.UserServiceInformation;

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
	public AdditionalCallingPartyNumberCap createAdditionalCallingPartyNumberCap(byte[] data) {
		return new AdditionalCallingPartyNumberCapImpl(data);
	}

	@Override
	public AdditionalCallingPartyNumberCap createAdditionalCallingPartyNumberCap(GenericNumber genericNumber) throws CAPException {
		return new AdditionalCallingPartyNumberCapImpl(genericNumber);
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
}
