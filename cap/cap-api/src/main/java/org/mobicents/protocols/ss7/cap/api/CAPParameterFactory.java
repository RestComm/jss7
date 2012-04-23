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

import java.util.ArrayList;

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
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CallSegmentToCancel;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CollectedDigits;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CollectedInfo;
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
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InbandInfo;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InformationToSend;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InitialDPArgExtension;
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
public interface CAPParameterFactory {
	
	public CAPGprsReferenceNumber createCAPGprsReferenceNumber(Integer destinationReference, Integer originationReference);
	
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

	public Digits createDigits(byte[] data);
	public Digits createDigits(GenericNumber genericNumber) throws CAPException;
	public Digits createDigits(GenericDigits genericDigits) throws CAPException;
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
	
	public RouteSelectFailureSpecificInfo createRouteSelectFailureSpecificInfo(CauseCap failureCause);
	public OCalledPartyBusySpecificInfo createOCalledPartyBusySpecificInfo(CauseCap busyCause);
	public ONoAnswerSpecificInfo createONoAnswerSpecificInfo();
	public OAnswerSpecificInfo createOAnswerSpecificInfo(CalledPartyNumberCap destinationAddress, boolean orCall, boolean forwardedCall,
			ChargeIndicator chargeIndicator, ExtBasicServiceCode extBasicServiceCode, ExtBasicServiceCode extBasicServiceCode2);
	public ODisconnectSpecificInfo createODisconnectSpecificInfo(CauseCap releaseCause);
	public TBusySpecificInfo createTBusySpecificInfo(CauseCap busyCause, boolean callForwarded, boolean routeNotPermitted,
			CalledPartyNumberCap forwardingDestinationNumber);
	public TNoAnswerSpecificInfo createTNoAnswerSpecificInfo(boolean callForwarded, CalledPartyNumberCap forwardingDestinationNumber);
	public TAnswerSpecificInfo createTAnswerSpecificInfo(CalledPartyNumberCap destinationAddress, boolean orCall, boolean forwardedCall,
			ChargeIndicator chargeIndicator, ExtBasicServiceCode extBasicServiceCode, ExtBasicServiceCode extBasicServiceCode2);
	public TDisconnectSpecificInfo createTDisconnectSpecificInfo(CauseCap releaseCause);

	public DestinationRoutingAddress createDestinationRoutingAddress(ArrayList<CalledPartyNumberCap> calledPartyNumber);
	
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(RouteSelectFailureSpecificInfo routeSelectFailureSpecificInfo);
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(OCalledPartyBusySpecificInfo oCalledPartyBusySpecificInfo);
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(ONoAnswerSpecificInfo oNoAnswerSpecificInfo);
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(OAnswerSpecificInfo oAnswerSpecificInfo);
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(OMidCallSpecificInfo oMidCallSpecificInfo);
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(ODisconnectSpecificInfo oDisconnectSpecificInfo);
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(TBusySpecificInfo tBusySpecificInfo);
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(TNoAnswerSpecificInfo tNoAnswerSpecificInfo);
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(TAnswerSpecificInfo tAnswerSpecificInfo);
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(TMidCallSpecificInfo tMidCallSpecificInfo);
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(TDisconnectSpecificInfo tDisconnectSpecificInfo);
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(OTermSeizedSpecificInfo oTermSeizedSpecificInfo);
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(CallAcceptedSpecificInfo callAcceptedSpecificInfo);
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(OAbandonSpecificInfo oAbandonSpecificInfo);
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(OChangeOfPositionSpecificInfo oChangeOfPositionSpecificInfo);
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(TChangeOfPositionSpecificInfo tChangeOfPositionSpecificInfo);

	public RequestedInformation createRequestedInformation_CallAttemptElapsedTime(int callAttemptElapsedTimeValue);
	public RequestedInformation createRequestedInformation_CallConnectedElapsedTime(int callConnectedElapsedTimeValue);
	public RequestedInformation createRequestedInformation_CallStopTime(DateAndTime callStopTimeValue);
	public RequestedInformation createRequestedInformation_ReleaseCause(CauseCap releaseCauseValue);

	public TimeDurationChargingResult createTimeDurationChargingResult(ReceivingSideID partyToCharge, TimeInformation timeInformation, boolean legActive,
			boolean callLegReleasedAtTcpExpiry, CAPExtensions extensions, AChChargingAddress aChChargingAddress);
	public TimeIfTariffSwitch createTimeIfTariffSwitch(int timeSinceTariffSwitch, Integer tariffSwitchInterval);
	public TimeInformation createTimeInformation(int timeIfNoTariffSwitch);
	public TimeInformation createTimeInformation(TimeIfTariffSwitch timeIfTariffSwitch);

	public IPSSPCapabilities createIPSSPCapabilities(boolean IPRoutingAddressSupported, boolean VoiceBackSupported,
			boolean VoiceInformationSupportedViaSpeechRecognition, boolean VoiceInformationSupportedViaVoiceRecognition,
			boolean GenerationOfVoiceAnnouncementsFromTextSupported, byte[] extraData);

	public InitialDPArgExtension createInitialDPArgExtension(ISDNAddressString gmscAddress, CalledPartyNumberCap forwardingDestinationNumber,
			MSClassmark2 msClassmark2, IMEI imei, SupportedCamelPhases supportedCamelPhases, OfferedCamel4Functionalities offeredCamel4Functionalities,
			BearerCapability bearerCapability2, ExtBasicServiceCode extBasicServiceCode2, HighLayerCompatibilityInap highLayerCompatibility2,
			LowLayerCompatibility lowLayerCompatibility, LowLayerCompatibility lowLayerCompatibility2, boolean enhancedDialledServicesAllowed, UUData uuData,
			boolean isCAPVersion3orLater);

	public AlertingPatternCap createAlertingPatternCap(AlertingPattern alertingPattern);
	public AlertingPatternCap createAlertingPatternCap(byte[] data);
	public NAOliInfo createNAOliInfo(byte[] data);
	public NAOliInfo createNAOliInfo(int value);

	public ScfID createScfID(byte[] data);
	public ServiceInteractionIndicatorsTwo createServiceInteractionIndicatorsTwo(ForwardServiceInteractionInd forwardServiceInteractionInd,
			BackwardServiceInteractionInd backwardServiceInteractionInd, BothwayThroughConnectionInd bothwayThroughConnectionInd,
			ConnectedNumberTreatmentInd connectedNumberTreatmentInd, boolean nonCUGCall, HoldTreatmentIndicator holdTreatmentIndicator,
			CwTreatmentIndicator cwTreatmentIndicator, EctTreatmentIndicator ectTreatmentIndicator);

	public FCIBCCCAMELsequence1 createFCIBCCCAMELsequence1(byte[] freeFormatData, SendingSideID partyToCharge, AppendFreeFormatData appendFreeFormatData);

	public CAMELSCIBillingChargingCharacteristicsAlt createCAMELSCIBillingChargingCharacteristicsAlt();
	public CAI_GSM0224 createCAI_GSM0224(Integer e1, Integer e2, Integer e3, Integer e4, Integer e5, Integer e6, Integer e7);
	public AOCSubsequent createAOCSubsequent(CAI_GSM0224 cai_GSM0224, Integer tariffSwitchInterval);
	public AOCBeforeAnswer createAOCBeforeAnswer(CAI_GSM0224 aocInitial, AOCSubsequent aocSubsequent);
	public SCIBillingChargingCharacteristics createSCIBillingChargingCharacteristics(AOCBeforeAnswer aocBeforeAnswer);
	public SCIBillingChargingCharacteristics createSCIBillingChargingCharacteristics(AOCSubsequent aocSubsequent);
	public SCIBillingChargingCharacteristics createSCIBillingChargingCharacteristics(CAMELSCIBillingChargingCharacteristicsAlt aocExtension);

	public VariablePartPrice createVariablePartPrice(byte[] data);
	public VariablePartPrice createVariablePartPrice(double price);
	public VariablePartPrice createVariablePartPrice(int integerPart, int hundredthPart);
	public VariablePartDate createVariablePartDate(byte[] data);
	public VariablePartDate createVariablePartDate(int year, int month, int day);
	public VariablePartTime createVariablePartTime(byte[] data);
	public VariablePartTime createVariablePartTime(int hour, int minute);
	public VariablePart createVariablePart(Integer integer);
	public VariablePart createVariablePart(Digits number);
	public VariablePart createVariablePart(VariablePartTime time);
	public VariablePart createVariablePart(VariablePartDate date);
	public VariablePart createVariablePart(VariablePartPrice price);
	
	public MessageIDText createMessageIDText(String messageContent, byte[] attributes);
	public VariableMessage createVariableMessage(int elementaryMessageID, ArrayList<VariablePart> variableParts);
	public MessageID createMessageID(Integer elementaryMessageID);
	public MessageID createMessageID(MessageIDText text);
	public MessageID createMessageID(ArrayList<Integer> elementaryMessageIDs);
	public MessageID createMessageID(VariableMessage variableMessage);
	public InbandInfo createInbandInfo(MessageID messageID, Integer numberOfRepetitions, Integer duration, Integer interval);
	
	public Tone createTone(int toneID, Integer duration);
	public InformationToSend createInformationToSend(InbandInfo inbandInfo);
	public InformationToSend createInformationToSend(Tone tone);
	public CollectedDigits createCollectedDigits(Integer minimumNbOfDigits, int maximumNbOfDigits, byte[] endOfReplyDigit, byte[] cancelDigit,
			byte[] startDigit, Integer firstDigitTimeOut, Integer interDigitTimeOut, ErrorTreatment errorTreatment, Boolean interruptableAnnInd,
			Boolean voiceInformation, Boolean voiceBack);
	public CollectedInfo createCollectedInfo(CollectedDigits collectedDigits);
	public CallSegmentToCancel createCallSegmentToCancel(Integer invokeID, Integer callSegmentID);

}

