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

package org.mobicents.protocols.ss7.cap.api;

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
public interface CAPParameterFactory {
	
	public CAPGprsReferenceNumber createCAPGprsReferenceNumber(Integer destinationReference, Integer originationReference);
	
	public RouteSelectFailureSpecificInfo createRouteSelectFailureSpecificInfo(CauseCap failureCause);
	
	public CauseCap createCauseCap(byte[] data);
	public CauseCap createCauseCap(CauseIndicators causeIndicators) throws CAPException;
	
	public DpSpecificCriteria createDpSpecificCriteria(Integer applicationTimer);
	public DpSpecificCriteria createDpSpecificCriteria(MidCallControlInfo midCallControlInfo);
	public DpSpecificCriteria createDpSpecificCriteria(DpSpecificCriteriaAlt dpSpecificCriteriaAlt);

	public BCSMEvent createBCSMEvent(EventTypeBCSM eventTypeBCSM, MonitorMode monitorMode, LegID legID, DpSpecificCriteria dpSpecificCriteria,
			boolean automaticRearm);
	
	public CalledPartyBCDNumber createCalledPartyBCDNumber(byte[] data);

	public ExtensionField createExtensionField(Integer localCode, CriticalityType criticalityType, byte[] data);
	public ExtensionField createExtensionField(long[] globalCode, CriticalityType criticalityType, byte[] data);
	public CAPExtensions createCAPExtensions(ExtensionField[] fieldsList);
	
	public CAMELAChBillingChargingCharacteristics createCAMELAChBillingChargingCharacteristics(byte[] data);
	public CAMELAChBillingChargingCharacteristics createCAMELAChBillingChargingCharacteristics(long maxCallPeriodDuration, boolean releaseIfdurationExceeded,
			Long tariffSwitchInterval, AudibleIndicator audibleIndicator, CAPExtensions extensions, boolean isCAPVersion3orLater);
	
	public DateAndTime createDateAndTime(int year, int month, int day, int hour, int minute, int second);
	public TimeAndTimezone createTimeAndTimezone(int year, int month, int day, int hour, int minute, int second, int timeZone);

	public SendingSideID createSendingSideID(LegType sendingSideID);
	public ReceivingSideID createReceivingSideID(LegType receivingSideID);

	public BearerCap createBearerCap(byte[] data);
	public BearerCap createBearerCap(UserServiceInformation userServiceInformation) throws CAPException;
	public BearerCapability createBearerCapability(BearerCap bearerCap);

	public AdditionalCallingPartyNumberCap createAdditionalCallingPartyNumberCap(byte[] data);
	public AdditionalCallingPartyNumberCap createAdditionalCallingPartyNumberCap(GenericNumber genericNumber) throws CAPException;
	public CalledPartyNumberCap createCalledPartyNumberCap(byte[] data);
	public CalledPartyNumberCap createCalledPartyNumberCap(CalledPartyNumber calledPartyNumber) throws CAPException;
	public CallingPartyNumberCap createCallingPartyNumberCap(byte[] data);
	public CallingPartyNumberCap createCallingPartyNumberCap(CallingPartyNumber callingPartyNumber) throws CAPException;
	public GenericNumberCap createGenericNumberCap(byte[] data);
	public GenericNumberCap createGenericNumberCap(GenericNumber genericNumber) throws CAPException;
	public LocationNumberCap createLocationNumberCap(byte[] data);
	public LocationNumberCap createLocationNumberCap(LocationNumber locationNumber) throws CAPException;
	public OriginalCalledNumberCap createOriginalCalledNumberCap(byte[] data);
	public OriginalCalledNumberCap createOriginalCalledNumberCap(OriginalCalledNumber originalCalledNumber) throws CAPException;
	public RedirectingPartyIDCap createRedirectingPartyIDCap(byte[] data);
	public RedirectingPartyIDCap createRedirectingPartyIDCap(RedirectingNumber redirectingNumber) throws CAPException;

}
