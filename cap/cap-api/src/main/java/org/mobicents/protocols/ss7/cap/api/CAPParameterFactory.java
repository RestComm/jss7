/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

import java.util.ArrayList;

import org.mobicents.protocols.ss7.cap.api.EsiBcsm.CallAcceptedSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.ChargeIndicator;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.ChargeIndicatorValue;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.CollectedInfoSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.DpSpecificInfoAlt;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.MetDPCriterion;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.MetDPCriterionAlt;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.MidCallEvents;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OAbandonSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OAnswerSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OCalledPartyBusySpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OChangeOfPositionSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.ODisconnectSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OMidCallSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.ONoAnswerSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OServiceChangeSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OTermSeizedSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.RouteSelectFailureSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.TAnswerSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.TBusySpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.TChangeOfPositionSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.TDisconnectSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.TMidCallSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.TNoAnswerSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.TServiceChangeSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiGprs.DetachSpecificInformation;
import org.mobicents.protocols.ss7.cap.api.EsiGprs.DisconnectSpecificInformation;
import org.mobicents.protocols.ss7.cap.api.EsiGprs.PDPContextEstablishmentAcknowledgementSpecificInformation;
import org.mobicents.protocols.ss7.cap.api.EsiGprs.PDPContextEstablishmentSpecificInformation;
import org.mobicents.protocols.ss7.cap.api.EsiGprs.PdpContextchangeOfPositionSpecificInformation;
import org.mobicents.protocols.ss7.cap.api.EsiSms.OSmsFailureSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiSms.OSmsSubmissionSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiSms.TSmsDeliverySpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiSms.TSmsFailureSpecificInfo;
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
import org.mobicents.protocols.ss7.cap.api.primitives.Burst;
import org.mobicents.protocols.ss7.cap.api.primitives.BurstList;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.CalledPartyBCDNumber;
import org.mobicents.protocols.ss7.cap.api.primitives.CriticalityType;
import org.mobicents.protocols.ss7.cap.api.primitives.DateAndTime;
import org.mobicents.protocols.ss7.cap.api.primitives.ErrorTreatment;
import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.cap.api.primitives.ExtensionField;
import org.mobicents.protocols.ss7.cap.api.primitives.MonitorMode;
import org.mobicents.protocols.ss7.cap.api.primitives.ReceivingSideID;
import org.mobicents.protocols.ss7.cap.api.primitives.ScfID;
import org.mobicents.protocols.ss7.cap.api.primitives.SendingSideID;
import org.mobicents.protocols.ss7.cap.api.primitives.TimeAndTimezone;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AOCBeforeAnswer;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AOCSubsequent;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AlertingPatternCap;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AudibleIndicator;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.BackwardServiceInteractionInd;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.BearerCapability;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAI_GSM0224;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAMELAChBillingChargingCharacteristics;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAMELSCIBillingChargingCharacteristicsAlt;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CallCompletionTreatmentIndicator;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CallDiversionTreatmentIndicator;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CallSegmentToCancel;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CallingPartyRestrictionIndicator;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.Carrier;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ChangeOfLocation;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ChangeOfLocationAlt;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CollectedDigits;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CollectedInfo;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ConferenceTreatmentIndicator;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ConnectedNumberTreatmentInd;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CwTreatmentIndicator;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.DestinationRoutingAddress;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.DpSpecificCriteria;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.DpSpecificCriteriaAlt;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.EctTreatmentIndicator;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.FCIBCCCAMELsequence1;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ForwardServiceInteractionInd;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.FreeFormatData;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.HoldTreatmentIndicator;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.IPSSPCapabilities;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InbandInfo;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InformationToSend;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InitialDPArgExtension;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.LegOrCallSegment;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.LowLayerCompatibility;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.MessageID;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.MessageIDText;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.MidCallControlInfo;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.NAOliInfo;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformation;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.SCIBillingChargingCharacteristics;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwo;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.TimeDurationChargingResult;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.TimeIfTariffSwitch;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.TimeInformation;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.Tone;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.VariableMessage;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.VariablePart;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.VariablePartDate;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.VariablePartPrice;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.VariablePartTime;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.AOCGPRS;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.AccessPointName;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.CAMELFCIGPRSBillingChargingCharacteristics;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.CAMELSCIGPRSBillingChargingCharacteristics;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.ChargingCharacteristics;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.ChargingResult;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.ChargingRollOver;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.ElapsedTime;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.ElapsedTimeRollOver;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.EndUserAddress;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.FreeFormatDataGprs;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSCause;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSEvent;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventSpecificInformation;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventType;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSQoS;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSQoSExtension;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.InitiatingEntity;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPAddress;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPID;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPInitiationType;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeNumber;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeNumberValue;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeOrganization;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeOrganizationValue;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.QualityOfService;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.ROTimeGPRSIfTariffSwitch;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.ROVolumeIfTariffSwitch;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.SGSNCapabilities;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.TimeGPRSIfTariffSwitch;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.TransferredVolume;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.TransferredVolumeRollOver;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.VolumeIfTariffSwitch;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.EventSpecificInformationSMS;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.EventTypeSMS;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.FCIBCCCAMELsequence1SMS;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.FreeFormatDataSMS;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.MOSMSCause;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.MTSMSCause;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.RPCause;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.SMSAddressString;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.SMSEvent;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.TPDataCodingScheme;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.TPProtocolIdentifier;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.TPShortMessageSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.TPValidityPeriod;
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
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;
import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LAIFixedLength;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.callhandling.UUData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSChargingID;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.MSClassmark2;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext2QoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4Functionalities;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.tcap.asn.comp.GeneralProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.InvokeProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnErrorProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultProblemType;

/**
 *
 * @author sergey vetyutnev
 *
 */
public interface CAPParameterFactory {

    CAPGprsReferenceNumber createCAPGprsReferenceNumber(Integer destinationReference, Integer originationReference);

    CauseCap createCauseCap(byte[] data);

    CauseCap createCauseCap(CauseIndicators causeIndicators) throws CAPException;

    DpSpecificCriteria createDpSpecificCriteria(Integer applicationTimer);

    DpSpecificCriteria createDpSpecificCriteria(MidCallControlInfo midCallControlInfo);

    DpSpecificCriteria createDpSpecificCriteria(DpSpecificCriteriaAlt dpSpecificCriteriaAlt);

    BCSMEvent createBCSMEvent(EventTypeBCSM eventTypeBCSM, MonitorMode monitorMode, LegID legID,
            DpSpecificCriteria dpSpecificCriteria, boolean automaticRearm);

    CalledPartyBCDNumber createCalledPartyBCDNumber(byte[] data);

    CalledPartyBCDNumber createCalledPartyBCDNumber(AddressNature addressNature, NumberingPlan numberingPlan,
            String address) throws CAPException;

    ExtensionField createExtensionField(Integer localCode, CriticalityType criticalityType, byte[] data);

    ExtensionField createExtensionField(long[] globalCode, CriticalityType criticalityType, byte[] data);

    CAPExtensions createCAPExtensions(ArrayList<ExtensionField> fieldsList);

    CAMELAChBillingChargingCharacteristics createCAMELAChBillingChargingCharacteristics(byte[] data);

    CAMELAChBillingChargingCharacteristics createCAMELAChBillingChargingCharacteristics(long maxCallPeriodDuration,
            boolean releaseIfdurationExceeded, Long tariffSwitchInterval, AudibleIndicator audibleIndicator,
            CAPExtensions extensions, int capVersion);

    DateAndTime createDateAndTime(int year, int month, int day, int hour, int minute, int second);

    TimeAndTimezone createTimeAndTimezone(int year, int month, int day, int hour, int minute, int second, int timeZone);

    SendingSideID createSendingSideID(LegType sendingSideID);

    ReceivingSideID createReceivingSideID(LegType receivingSideID);

    BearerCap createBearerCap(byte[] data);

    BearerCap createBearerCap(UserServiceInformation userServiceInformation) throws CAPException;

    BearerCapability createBearerCapability(BearerCap bearerCap);

    Digits createDigits_GenericNumber(byte[] data);

    Digits createDigits_GenericDigits(byte[] data);

    Digits createDigits_GenericNumber(GenericNumber genericNumber) throws CAPException;

    Digits createDigits_GenericDigits(GenericDigits genericDigits) throws CAPException;

    CalledPartyNumberCap createCalledPartyNumberCap(byte[] data);

    CalledPartyNumberCap createCalledPartyNumberCap(CalledPartyNumber calledPartyNumber) throws CAPException;

    CallingPartyNumberCap createCallingPartyNumberCap(byte[] data);

    CallingPartyNumberCap createCallingPartyNumberCap(CallingPartyNumber callingPartyNumber) throws CAPException;

    GenericNumberCap createGenericNumberCap(byte[] data);

    GenericNumberCap createGenericNumberCap(GenericNumber genericNumber) throws CAPException;

    LocationNumberCap createLocationNumberCap(byte[] data);

    LocationNumberCap createLocationNumberCap(LocationNumber locationNumber) throws CAPException;

    OriginalCalledNumberCap createOriginalCalledNumberCap(byte[] data);

    OriginalCalledNumberCap createOriginalCalledNumberCap(OriginalCalledNumber originalCalledNumber) throws CAPException;

    RedirectingPartyIDCap createRedirectingPartyIDCap(byte[] data);

    RedirectingPartyIDCap createRedirectingPartyIDCap(RedirectingNumber redirectingNumber) throws CAPException;

    RouteSelectFailureSpecificInfo createRouteSelectFailureSpecificInfo(CauseCap failureCause);

    OCalledPartyBusySpecificInfo createOCalledPartyBusySpecificInfo(CauseCap busyCause);

    OAbandonSpecificInfo createOAbandonSpecificInfo(boolean routeNotPermitted);

    ONoAnswerSpecificInfo createONoAnswerSpecificInfo();

    OAnswerSpecificInfo createOAnswerSpecificInfo(CalledPartyNumberCap destinationAddress, boolean orCall,
            boolean forwardedCall, ChargeIndicator chargeIndicator, ExtBasicServiceCode extBasicServiceCode,
            ExtBasicServiceCode extBasicServiceCode2);

    ODisconnectSpecificInfo createODisconnectSpecificInfo(CauseCap releaseCause);

    TBusySpecificInfo createTBusySpecificInfo(CauseCap busyCause, boolean callForwarded, boolean routeNotPermitted,
            CalledPartyNumberCap forwardingDestinationNumber);

    TNoAnswerSpecificInfo createTNoAnswerSpecificInfo(boolean callForwarded,
            CalledPartyNumberCap forwardingDestinationNumber);

    TAnswerSpecificInfo createTAnswerSpecificInfo(CalledPartyNumberCap destinationAddress, boolean orCall,
            boolean forwardedCall, ChargeIndicator chargeIndicator, ExtBasicServiceCode extBasicServiceCode,
            ExtBasicServiceCode extBasicServiceCode2);

    TDisconnectSpecificInfo createTDisconnectSpecificInfo(CauseCap releaseCause);

    DestinationRoutingAddress createDestinationRoutingAddress(ArrayList<CalledPartyNumberCap> calledPartyNumber);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(
            RouteSelectFailureSpecificInfo routeSelectFailureSpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(
            OCalledPartyBusySpecificInfo oCalledPartyBusySpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(ONoAnswerSpecificInfo oNoAnswerSpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(OAnswerSpecificInfo oAnswerSpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(OMidCallSpecificInfo oMidCallSpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(ODisconnectSpecificInfo oDisconnectSpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(TBusySpecificInfo tBusySpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(TNoAnswerSpecificInfo tNoAnswerSpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(TAnswerSpecificInfo tAnswerSpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(TMidCallSpecificInfo tMidCallSpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(TDisconnectSpecificInfo tDisconnectSpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(OTermSeizedSpecificInfo oTermSeizedSpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(CallAcceptedSpecificInfo callAcceptedSpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(OAbandonSpecificInfo oAbandonSpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(OChangeOfPositionSpecificInfo oChangeOfPositionSpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(TChangeOfPositionSpecificInfo tChangeOfPositionSpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(DpSpecificInfoAlt dpSpecificInfoAlt);

    RequestedInformation createRequestedInformation_CallAttemptElapsedTime(int callAttemptElapsedTimeValue);

    RequestedInformation createRequestedInformation_CallConnectedElapsedTime(int callConnectedElapsedTimeValue);

    RequestedInformation createRequestedInformation_CallStopTime(DateAndTime callStopTimeValue);

    RequestedInformation createRequestedInformation_ReleaseCause(CauseCap releaseCauseValue);

    TimeDurationChargingResult createTimeDurationChargingResult(ReceivingSideID partyToCharge,
            TimeInformation timeInformation, boolean legActive, boolean callLegReleasedAtTcpExpiry, CAPExtensions extensions,
            AChChargingAddress aChChargingAddress);

    TimeIfTariffSwitch createTimeIfTariffSwitch(int timeSinceTariffSwitch, Integer tariffSwitchInterval);

    TimeInformation createTimeInformation(int timeIfNoTariffSwitch);

    TimeInformation createTimeInformation(TimeIfTariffSwitch timeIfTariffSwitch);

    IPSSPCapabilities createIPSSPCapabilities(boolean IPRoutingAddressSupported, boolean VoiceBackSupported,
            boolean VoiceInformationSupportedViaSpeechRecognition, boolean VoiceInformationSupportedViaVoiceRecognition,
            boolean GenerationOfVoiceAnnouncementsFromTextSupported, byte[] extraData);

    InitialDPArgExtension createInitialDPArgExtension(ISDNAddressString gmscAddress,
            CalledPartyNumberCap forwardingDestinationNumber, MSClassmark2 msClassmark2, IMEI imei,
            SupportedCamelPhases supportedCamelPhases, OfferedCamel4Functionalities offeredCamel4Functionalities,
            BearerCapability bearerCapability2, ExtBasicServiceCode extBasicServiceCode2,
            HighLayerCompatibilityInap highLayerCompatibility2, LowLayerCompatibility lowLayerCompatibility,
            LowLayerCompatibility lowLayerCompatibility2, boolean enhancedDialledServicesAllowed, UUData uuData,
            boolean collectInformationAllowed, boolean releaseCallArgExtensionAllowed, boolean isCAPVersion3orLater);

    AlertingPatternCap createAlertingPatternCap(AlertingPattern alertingPattern);

    AlertingPatternCap createAlertingPatternCap(byte[] data);

    NAOliInfo createNAOliInfo(int value);

    ScfID createScfID(byte[] data);

    ServiceInteractionIndicatorsTwo createServiceInteractionIndicatorsTwo(
            ForwardServiceInteractionInd forwardServiceInteractionInd,
            BackwardServiceInteractionInd backwardServiceInteractionInd,
            BothwayThroughConnectionInd bothwayThroughConnectionInd, ConnectedNumberTreatmentInd connectedNumberTreatmentInd,
            boolean nonCUGCall, HoldTreatmentIndicator holdTreatmentIndicator, CwTreatmentIndicator cwTreatmentIndicator,
            EctTreatmentIndicator ectTreatmentIndicator);

    FCIBCCCAMELsequence1 createFCIBCCCAMELsequence1(FreeFormatData freeFormatData, SendingSideID partyToCharge,
            AppendFreeFormatData appendFreeFormatData);

    CAMELSCIBillingChargingCharacteristicsAlt createCAMELSCIBillingChargingCharacteristicsAlt();

    CAI_GSM0224 createCAI_GSM0224(Integer e1, Integer e2, Integer e3, Integer e4, Integer e5, Integer e6, Integer e7);

    AOCSubsequent createAOCSubsequent(CAI_GSM0224 cai_GSM0224, Integer tariffSwitchInterval);

    AOCBeforeAnswer createAOCBeforeAnswer(CAI_GSM0224 aocInitial, AOCSubsequent aocSubsequent);

    SCIBillingChargingCharacteristics createSCIBillingChargingCharacteristics(AOCBeforeAnswer aocBeforeAnswer);

    SCIBillingChargingCharacteristics createSCIBillingChargingCharacteristics(AOCSubsequent aocSubsequent);

    SCIBillingChargingCharacteristics createSCIBillingChargingCharacteristics(
            CAMELSCIBillingChargingCharacteristicsAlt aocExtension);

    VariablePartPrice createVariablePartPrice(byte[] data);

    VariablePartPrice createVariablePartPrice(double price);

    VariablePartPrice createVariablePartPrice(int integerPart, int hundredthPart);

    VariablePartDate createVariablePartDate(byte[] data);

    VariablePartDate createVariablePartDate(int year, int month, int day);

    VariablePartTime createVariablePartTime(byte[] data);

    VariablePartTime createVariablePartTime(int hour, int minute);

    VariablePart createVariablePart(Integer integer);

    VariablePart createVariablePart(Digits number);

    VariablePart createVariablePart(VariablePartTime time);

    VariablePart createVariablePart(VariablePartDate date);

    VariablePart createVariablePart(VariablePartPrice price);

    MessageIDText createMessageIDText(String messageContent, byte[] attributes);

    VariableMessage createVariableMessage(int elementaryMessageID, ArrayList<VariablePart> variableParts);

    MessageID createMessageID(Integer elementaryMessageID);

    MessageID createMessageID(MessageIDText text);

    MessageID createMessageID(ArrayList<Integer> elementaryMessageIDs);

    MessageID createMessageID(VariableMessage variableMessage);

    InbandInfo createInbandInfo(MessageID messageID, Integer numberOfRepetitions, Integer duration, Integer interval);

    Tone createTone(int toneID, Integer duration);

    InformationToSend createInformationToSend(InbandInfo inbandInfo);

    InformationToSend createInformationToSend(Tone tone);

    CollectedDigits createCollectedDigits(Integer minimumNbOfDigits, int maximumNbOfDigits, byte[] endOfReplyDigit,
            byte[] cancelDigit, byte[] startDigit, Integer firstDigitTimeOut, Integer interDigitTimeOut,
            ErrorTreatment errorTreatment, Boolean interruptableAnnInd, Boolean voiceInformation, Boolean voiceBack);

    CollectedInfo createCollectedInfo(CollectedDigits collectedDigits);

    CallSegmentToCancel createCallSegmentToCancel(Integer invokeID, Integer callSegmentID);

    Problem createProblemGeneral(GeneralProblemType prob);

    Problem createProblemInvoke(InvokeProblemType prob);

    Problem createProblemResult(ReturnResultProblemType prob);

    Problem createProblemError(ReturnErrorProblemType prob);

    AccessPointName createAccessPointName(byte[] data);

    AOCGPRS createAOCGPRS(CAI_GSM0224 aocInitial, AOCSubsequent aocSubsequent);

    CAMELFCIGPRSBillingChargingCharacteristics createCAMELFCIGPRSBillingChargingCharacteristics(
            org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.FCIBCCCAMELsequence1Gprs fcIBCCCAMELsequence1);

    CAMELSCIGPRSBillingChargingCharacteristics createCAMELSCIGPRSBillingChargingCharacteristics(AOCGPRS aocGPRS,
            PDPID pdpID);

    ChargingCharacteristics createChargingCharacteristics(long maxTransferredVolume);

    ChargingCharacteristics createChargingCharacteristics(int maxElapsedTime);

    ChargingResult createChargingResult(TransferredVolume transferredVolume);

    ChargingResult createChargingResult(ElapsedTime elapsedTime);

    ChargingRollOver createChargingRollOver(ElapsedTimeRollOver elapsedTimeRollOver);

    ChargingRollOver createChargingRollOver(TransferredVolumeRollOver transferredVolumeRollOver);

    ElapsedTime createElapsedTime(Integer timeGPRSIfNoTariffSwitch);

    ElapsedTime createElapsedTime(TimeGPRSIfTariffSwitch timeGPRSIfTariffSwitch);

    ElapsedTimeRollOver createElapsedTimeRollOver(Integer roTimeGPRSIfNoTariffSwitch);

    ElapsedTimeRollOver createElapsedTimeRollOver(ROTimeGPRSIfTariffSwitch roTimeGPRSIfTariffSwitch);

    EndUserAddress createEndUserAddress(PDPTypeOrganization pdpTypeOrganization, PDPTypeNumber pdpTypeNumber,
            PDPAddress pdpAddress);

    org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.FCIBCCCAMELsequence1Gprs createFCIBCCCAMELsequence1(
            FreeFormatDataGprs freeFormatData, PDPID pdpID, AppendFreeFormatData appendFreeFormatData);

    FreeFormatData createFreeFormatData(byte[] data);

    FreeFormatDataGprs createFreeFormatDataGprs(byte[] data);

    GPRSCause createGPRSCause(int data);

    GPRSEvent createGPRSEvent(GPRSEventType gprsEventType, MonitorMode monitorMode);

    GPRSEventSpecificInformation createGPRSEventSpecificInformation(LocationInformationGPRS locationInformationGPRS);

    GPRSEventSpecificInformation createGPRSEventSpecificInformation(
            PdpContextchangeOfPositionSpecificInformation pdpContextchangeOfPositionSpecificInformation);

    GPRSEventSpecificInformation createGPRSEventSpecificInformation(DetachSpecificInformation detachSpecificInformation);

    GPRSEventSpecificInformation createGPRSEventSpecificInformation(
            DisconnectSpecificInformation disconnectSpecificInformation);

    GPRSEventSpecificInformation createGPRSEventSpecificInformation(
            PDPContextEstablishmentSpecificInformation pdpContextEstablishmentSpecificInformation);

    GPRSEventSpecificInformation createGPRSEventSpecificInformation(
            PDPContextEstablishmentAcknowledgementSpecificInformation pdpContextEstablishmentAcknowledgementSpecificInformation);

    GPRSQoSExtension createGPRSQoSExtension(Ext2QoSSubscribed supplementToLongQoSFormat);

    GPRSQoS createGPRSQoS(QoSSubscribed shortQoSFormat);

    GPRSQoS createGPRSQoS(ExtQoSSubscribed longQoSFormat);

    PDPAddress createPDPAddress(byte[] data);

    PDPID createPDPID(int data);

    PDPTypeNumber createPDPTypeNumber(int data);

    PDPTypeNumber createPDPTypeNumber(PDPTypeNumberValue value);

    PDPTypeOrganization createPDPTypeOrganization(int data);

    PDPTypeOrganization createPDPTypeOrganization(PDPTypeOrganizationValue value);

    QualityOfService createQualityOfService(GPRSQoS requestedQoS, GPRSQoS subscribedQoS, GPRSQoS negotiatedQoS,
            GPRSQoSExtension requestedQoSExtension, GPRSQoSExtension subscribedQoSExtension,
            GPRSQoSExtension negotiatedQoSExtension);

    ROTimeGPRSIfTariffSwitch createROTimeGPRSIfTariffSwitch(Integer roTimeGPRSSinceLastTariffSwitch,
            Integer roTimeGPRSTariffSwitchInterval);

    ROVolumeIfTariffSwitch createROVolumeIfTariffSwitch(Integer roVolumeSinceLastTariffSwitch,
            Integer roVolumeTariffSwitchInterval);

    SGSNCapabilities createSGSNCapabilities(int data);

    SGSNCapabilities createSGSNCapabilities(boolean aoCSupportedBySGSN);

    TimeGPRSIfTariffSwitch createTimeGPRSIfTariffSwitch(int timeGPRSSinceLastTariffSwitch,
            Integer timeGPRSTariffSwitchInterval);

    TransferredVolume createTransferredVolume(Long volumeIfNoTariffSwitch);

    TransferredVolume createTransferredVolume(VolumeIfTariffSwitch volumeIfTariffSwitch);

    TransferredVolumeRollOver createTransferredVolumeRollOver(Integer roVolumeIfNoTariffSwitch);

    TransferredVolumeRollOver createTransferredVolumeRollOver(ROVolumeIfTariffSwitch roVolumeIfTariffSwitch);

    VolumeIfTariffSwitch createVolumeIfTariffSwitch(long volumeSinceLastTariffSwitch, Long volumeTariffSwitchInterval);

    DetachSpecificInformation createDetachSpecificInformation(InitiatingEntity initiatingEntity,
            boolean routeingAreaUpdate);

    DisconnectSpecificInformation createDisconnectSpecificInformation(InitiatingEntity initiatingEntity,
            boolean routeingAreaUpdate);

    PdpContextchangeOfPositionSpecificInformation createPdpContextchangeOfPositionSpecificInformation(
            AccessPointName accessPointName, GPRSChargingID chargingID, LocationInformationGPRS locationInformationGPRS,
            EndUserAddress endUserAddress, QualityOfService qualityOfService, TimeAndTimezone timeAndTimezone,
            GSNAddress gsnAddress);

    PDPContextEstablishmentAcknowledgementSpecificInformation createPDPContextEstablishmentAcknowledgementSpecificInformation(
            AccessPointName accessPointName, GPRSChargingID chargingID, LocationInformationGPRS locationInformationGPRS,
            EndUserAddress endUserAddress, QualityOfService qualityOfService, TimeAndTimezone timeAndTimezone,
            GSNAddress gsnAddress);

    PDPContextEstablishmentSpecificInformation createPDPContextEstablishmentSpecificInformation(AccessPointName accessPointName, EndUserAddress endUserAddress,
            QualityOfService qualityOfService, LocationInformationGPRS locationInformationGPRS, TimeAndTimezone timeAndTimezone,
            PDPInitiationType pdpInitiationType, boolean secondaryPDPContext);

    TPValidityPeriod createTPValidityPeriod(byte[] data);

    TPShortMessageSpecificInfo createTPShortMessageSpecificInfo(int data);

    TPProtocolIdentifier createTPProtocolIdentifier(int data);

    TPDataCodingScheme createTPDataCodingScheme(int data);

    SMSEvent createSMSEvent(EventTypeSMS eventTypeSMS, MonitorMode monitorMode);

    SMSAddressString createSMSAddressString(AddressNature addressNature, NumberingPlan numberingPlan, String address);

    RPCause createRPCause(int data);

    MTSMSCause createMTSMSCause(int data);

    FreeFormatDataSMS createFreeFormatDataSMS(byte[] data);

    FCIBCCCAMELsequence1SMS createFCIBCCCAMELsequence1(FreeFormatDataSMS freeFormatData, AppendFreeFormatData appendFreeFormatData);

    EventSpecificInformationSMS createEventSpecificInformationSMSImpl(OSmsFailureSpecificInfo oSmsFailureSpecificInfo);

    EventSpecificInformationSMS createEventSpecificInformationSMSImpl(OSmsSubmissionSpecificInfo oSmsSubmissionSpecificInfo);

    EventSpecificInformationSMS createEventSpecificInformationSMSImpl(TSmsFailureSpecificInfo tSmsFailureSpecificInfo);

    EventSpecificInformationSMS createEventSpecificInformationSMSImpl(TSmsDeliverySpecificInfo tSmsDeliverySpecificInfo);

    OSmsFailureSpecificInfo createOSmsFailureSpecificInfo(MOSMSCause failureCause);

    OSmsSubmissionSpecificInfo createOSmsSubmissionSpecificInfo();

    TSmsFailureSpecificInfo createTSmsFailureSpecificInfo(MTSMSCause failureCause);

    TSmsDeliverySpecificInfo createTSmsDeliverySpecificInfo();

    LegOrCallSegment createLegOrCallSegment(Integer callSegmentID);

    LegOrCallSegment createLegOrCallSegment(LegID legID);

    ChargeIndicator createChargeIndicator(int data);

    ChargeIndicator createChargeIndicator(ChargeIndicatorValue value);

    BackwardServiceInteractionInd createBackwardServiceInteractionInd(ConferenceTreatmentIndicator conferenceTreatmentIndicator,
            CallCompletionTreatmentIndicator callCompletionTreatmentIndicator);

    Carrier createCarrier(byte[] data);

    ForwardServiceInteractionInd createForwardServiceInteractionInd(ConferenceTreatmentIndicator conferenceTreatmentIndicator,
            CallDiversionTreatmentIndicator callDiversionTreatmentIndicator, CallingPartyRestrictionIndicator callingPartyRestrictionIndicator);

    LowLayerCompatibility createLowLayerCompatibility(byte[] data);

    MidCallEvents createMidCallEvents_Completed(Digits dtmfDigits);

    MidCallEvents createMidCallEvents_TimeOut(Digits dtmfDigits);

    OMidCallSpecificInfo createOMidCallSpecificInfo(MidCallEvents midCallEvents);

    TMidCallSpecificInfo createTMidCallSpecificInfo(MidCallEvents midCallEvents);

    OTermSeizedSpecificInfo createOTermSeizedSpecificInfo(LocationInformation locationInformation);

    CallAcceptedSpecificInfo createCallAcceptedSpecificInfo(LocationInformation locationInformation);

    MetDPCriterionAlt createMetDPCriterionAlt();

    MetDPCriterion createMetDPCriterion_enteringCellGlobalId(CellGlobalIdOrServiceAreaIdFixedLength value);

    MetDPCriterion createMetDPCriterion_leavingCellGlobalId(CellGlobalIdOrServiceAreaIdFixedLength value);

    MetDPCriterion createMetDPCriterion_enteringServiceAreaId(CellGlobalIdOrServiceAreaIdFixedLength value);

    MetDPCriterion createMetDPCriterion_leavingServiceAreaId(CellGlobalIdOrServiceAreaIdFixedLength value);

    MetDPCriterion createMetDPCriterion_enteringLocationAreaId(LAIFixedLength value);

    MetDPCriterion createMetDPCriterion_leavingLocationAreaId(LAIFixedLength value);

    MetDPCriterion createMetDPCriterion_interSystemHandOverToUMTS();

    MetDPCriterion createMetDPCriterion_interSystemHandOverToGSM();

    MetDPCriterion createMetDPCriterion_interPLMNHandOver();

    MetDPCriterion createMetDPCriterion_interMSCHandOver();

    MetDPCriterion createMetDPCriterion(MetDPCriterionAlt metDPCriterionAlt);

    OChangeOfPositionSpecificInfo createOChangeOfPositionSpecificInfo(LocationInformation locationInformation, ArrayList<MetDPCriterion> metDPCriteriaList);

    TChangeOfPositionSpecificInfo createTChangeOfPositionSpecificInfo(LocationInformation locationInformation, ArrayList<MetDPCriterion> metDPCriteriaList);

    OServiceChangeSpecificInfo createOServiceChangeSpecificInfo(ExtBasicServiceCode extBasicServiceCode);

    TServiceChangeSpecificInfo createTServiceChangeSpecificInfo(ExtBasicServiceCode extBasicServiceCode);

    CollectedInfoSpecificInfo createCollectedInfoSpecificInfo(CalledPartyNumberCap calledPartyNumber);

    DpSpecificInfoAlt createDpSpecificInfoAlt(OServiceChangeSpecificInfo oServiceChangeSpecificInfo, CollectedInfoSpecificInfo collectedInfoSpecificInfo,
            TServiceChangeSpecificInfo tServiceChangeSpecificInfo);

    ChangeOfLocationAlt createChangeOfLocationAlt();

    ChangeOfLocation createChangeOfLocation_cellGlobalId(CellGlobalIdOrServiceAreaIdFixedLength value);

    ChangeOfLocation createChangeOfLocation_serviceAreaId(CellGlobalIdOrServiceAreaIdFixedLength value);

    ChangeOfLocation createChangeOfLocation(LAIFixedLength locationAreaId);

    ChangeOfLocation createChangeOfLocation_interSystemHandOver();

    ChangeOfLocation createChangeOfLocation_interPLMNHandOver();

    ChangeOfLocation createChangeOfLocation_interMSCHandOver();

    ChangeOfLocation createChangeOfLocation(ChangeOfLocationAlt changeOfLocationAlt);

    DpSpecificCriteriaAlt createDpSpecificCriteriaAlt(ArrayList<ChangeOfLocation> changeOfPositionControlInfo, Integer numberOfDigits);

    MidCallControlInfo createMidCallControlInfo(Integer minimumNumberOfDigits, Integer maximumNumberOfDigits, String endOfReplyDigit, String cancelDigit,
            String startDigit, Integer interDigitTimeout);

    Burst createBurst(Integer numberOfBursts, Integer burstInterval, Integer numberOfTonesInBurst, Integer toneDuration, Integer toneInterval);

    BurstList createBurstList(Integer warningPeriod, Burst burst);

    AudibleIndicator createAudibleIndicator(Boolean tone);

    AudibleIndicator createAudibleIndicator(BurstList burstList);

    AChChargingAddress createAChChargingAddress(LegID legID);

    AChChargingAddress createAChChargingAddress(int srfConnection);

}
