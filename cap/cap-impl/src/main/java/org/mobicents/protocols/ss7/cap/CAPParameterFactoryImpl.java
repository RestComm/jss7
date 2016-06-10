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

package org.mobicents.protocols.ss7.cap;

import java.util.ArrayList;

import org.mobicents.protocols.ss7.cap.EsiBcsm.CallAcceptedSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.ChargeIndicatorImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.CollectedInfoSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.DpSpecificInfoAltImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.MetDPCriterionAltImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.MetDPCriterionImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.MidCallEventsImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.OAbandonSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.OAnswerSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.OCalledPartyBusySpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.OChangeOfPositionSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.ODisconnectSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.OMidCallSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.ONoAnswerSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.OServiceChangeSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.OTermSeizedSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.RouteSelectFailureSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.TAnswerSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.TBusySpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.TChangeOfPositionSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.TDisconnectSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.TMidCallSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.TNoAnswerSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.TServiceChangeSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiGprs.DetachSpecificInformationImpl;
import org.mobicents.protocols.ss7.cap.EsiGprs.DisconnectSpecificInformationImpl;
import org.mobicents.protocols.ss7.cap.EsiGprs.PDPContextEstablishmentAcknowledgementSpecificInformationImpl;
import org.mobicents.protocols.ss7.cap.EsiGprs.PDPContextEstablishmentSpecificInformationImpl;
import org.mobicents.protocols.ss7.cap.EsiGprs.PdpContextchangeOfPositionSpecificInformationImpl;
import org.mobicents.protocols.ss7.cap.EsiSms.OSmsFailureSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiSms.OSmsSubmissionSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiSms.TSmsDeliverySpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiSms.TSmsFailureSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParameterFactory;
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
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformationType;
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
import org.mobicents.protocols.ss7.cap.primitives.AChChargingAddressImpl;
import org.mobicents.protocols.ss7.cap.primitives.BCSMEventImpl;
import org.mobicents.protocols.ss7.cap.primitives.BurstImpl;
import org.mobicents.protocols.ss7.cap.primitives.BurstListImpl;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsImpl;
import org.mobicents.protocols.ss7.cap.primitives.CalledPartyBCDNumberImpl;
import org.mobicents.protocols.ss7.cap.primitives.DateAndTimeImpl;
import org.mobicents.protocols.ss7.cap.primitives.ExtensionFieldImpl;
import org.mobicents.protocols.ss7.cap.primitives.ReceivingSideIDImpl;
import org.mobicents.protocols.ss7.cap.primitives.ScfIDImpl;
import org.mobicents.protocols.ss7.cap.primitives.SendingSideIDImpl;
import org.mobicents.protocols.ss7.cap.primitives.TimeAndTimezoneImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.AOCBeforeAnswerImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.AOCSubsequentImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.AlertingPatternCapImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.AudibleIndicatorImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.BackwardServiceInteractionIndImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.BearerCapabilityImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.CAI_GSM0224Impl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.CAMELAChBillingChargingCharacteristicsImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.CAMELSCIBillingChargingCharacteristicsAltImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.CallSegmentToCancelImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.CarrierImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.ChangeOfLocationAltImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.ChangeOfLocationImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.CollectedDigitsImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.CollectedInfoImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.DestinationRoutingAddressImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.DpSpecificCriteriaAltImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.DpSpecificCriteriaImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSMImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.FCIBCCCAMELsequence1Impl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.ForwardServiceInteractionIndImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.IPSSPCapabilitiesImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.InbandInfoImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.InformationToSendImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.InitialDPArgExtensionImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.LegOrCallSegmentImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.LowLayerCompatibilityImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.MessageIDImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.MessageIDTextImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.MidCallControlInfoImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.NAOliInfoImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.RequestedInformationImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.SCIBillingChargingCharacteristicsImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwoImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.TimeDurationChargingResultImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.TimeIfTariffSwitchImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.TimeInformationImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.ToneImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.VariableMessageImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.VariablePartDateImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.VariablePartImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.VariablePartPriceImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.VariablePartTimeImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.AOCGPRSImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.AccessPointNameImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.CAMELFCIGPRSBillingChargingCharacteristicsImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.CAMELSCIGPRSBillingChargingCharacteristicsImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.ChargingCharacteristicsImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.ChargingResultImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.ChargingRollOverImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.ElapsedTimeImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.ElapsedTimeRollOverImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.EndUserAddressImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.FreeFormatDataGprsImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.GPRSCauseImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.GPRSEventImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.GPRSEventSpecificInformationImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.GPRSQoSExtensionImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.GPRSQoSImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.PDPAddressImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.PDPIDImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.PDPTypeNumberImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.PDPTypeOrganizationImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.QualityOfServiceImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.ROTimeGPRSIfTariffSwitchImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.ROVolumeIfTariffSwitchImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.SGSNCapabilitiesImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.TimeGPRSIfTariffSwitchImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.TransferredVolumeImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.TransferredVolumeRollOverImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.VolumeIfTariffSwitchImpl;
import org.mobicents.protocols.ss7.cap.service.sms.primitive.EventSpecificInformationSMSImpl;
import org.mobicents.protocols.ss7.cap.service.sms.primitive.FreeFormatDataSMSImpl;
import org.mobicents.protocols.ss7.cap.service.sms.primitive.MTSMSCauseImpl;
import org.mobicents.protocols.ss7.cap.service.sms.primitive.RPCauseImpl;
import org.mobicents.protocols.ss7.cap.service.sms.primitive.SMSAddressStringImpl;
import org.mobicents.protocols.ss7.cap.service.sms.primitive.SMSEventImpl;
import org.mobicents.protocols.ss7.cap.service.sms.primitive.TPDataCodingSchemeImpl;
import org.mobicents.protocols.ss7.cap.service.sms.primitive.TPProtocolIdentifierImpl;
import org.mobicents.protocols.ss7.cap.service.sms.primitive.TPShortMessageSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.service.sms.primitive.TPValidityPeriodImpl;
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
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.GeneralProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.InvokeProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tcap.asn.comp.ProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnErrorProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultProblemType;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CAPParameterFactoryImpl implements CAPParameterFactory {

    public Problem createProblemGeneral(GeneralProblemType prob) {
        Problem pb = TcapFactory.createProblem(ProblemType.General);
        pb.setGeneralProblemType(prob);
        return pb;
    }

    public Problem createProblemInvoke(InvokeProblemType prob) {
        Problem pb = TcapFactory.createProblem(ProblemType.Invoke);
        pb.setInvokeProblemType(prob);
        return pb;
    }

    public Problem createProblemResult(ReturnResultProblemType prob) {
        Problem pb = TcapFactory.createProblem(ProblemType.ReturnResult);
        pb.setReturnResultProblemType(prob);
        return pb;
    }

    public Problem createProblemError(ReturnErrorProblemType prob) {
        Problem pb = TcapFactory.createProblem(ProblemType.ReturnError);
        pb.setReturnErrorProblemType(prob);
        return pb;
    }

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
    public BCSMEvent createBCSMEvent(EventTypeBCSM eventTypeBCSM, MonitorMode monitorMode, LegID legID,
            DpSpecificCriteria dpSpecificCriteria, boolean automaticRearm) {
        return new BCSMEventImpl(eventTypeBCSM, monitorMode, legID, dpSpecificCriteria, automaticRearm);
    }

    @Override
    public CalledPartyBCDNumber createCalledPartyBCDNumber(byte[] data) {
        return new CalledPartyBCDNumberImpl(data);
    }

    @Override
    public CalledPartyBCDNumber createCalledPartyBCDNumber(AddressNature addressNature, NumberingPlan numberingPlan,
            String address) throws CAPException {
        return new CalledPartyBCDNumberImpl(addressNature, numberingPlan, address);
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
    public CAPExtensions createCAPExtensions(ArrayList<ExtensionField> fieldsList) {
        return new CAPExtensionsImpl(fieldsList);
    }

    @Override
    public CAMELAChBillingChargingCharacteristics createCAMELAChBillingChargingCharacteristics(byte[] data) {
        return new CAMELAChBillingChargingCharacteristicsImpl(data);
    }

    @Override
    public CAMELAChBillingChargingCharacteristics createCAMELAChBillingChargingCharacteristics(long maxCallPeriodDuration,
            boolean releaseIfdurationExceeded, Long tariffSwitchInterval, AudibleIndicator audibleIndicator,
            CAPExtensions extensions, int capVersion) {
        return new CAMELAChBillingChargingCharacteristicsImpl(maxCallPeriodDuration, releaseIfdurationExceeded,
                tariffSwitchInterval, audibleIndicator, extensions, capVersion);
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
    public Digits createDigits_GenericNumber(byte[] data) {
        DigitsImpl res = new DigitsImpl(data);
        res.setIsGenericNumber();
        return res;
    }

    @Override
    public Digits createDigits_GenericDigits(byte[] data) {
        DigitsImpl res = new DigitsImpl(data);
        res.setIsGenericDigits();
        return res;
    }

    @Override
    public Digits createDigits_GenericNumber(GenericNumber genericNumber) throws CAPException {
        return new DigitsImpl(genericNumber);
    }

    @Override
    public Digits createDigits_GenericDigits(GenericDigits genericDigits) throws CAPException {
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
    public OAbandonSpecificInfo createOAbandonSpecificInfo(boolean routeNotPermitted) {
        return new OAbandonSpecificInfoImpl(routeNotPermitted);
    }

    @Override
    public ONoAnswerSpecificInfo createONoAnswerSpecificInfo() {
        return new ONoAnswerSpecificInfoImpl();
    }

    @Override
    public OAnswerSpecificInfo createOAnswerSpecificInfo(CalledPartyNumberCap destinationAddress, boolean orCall,
            boolean forwardedCall, ChargeIndicator chargeIndicator, ExtBasicServiceCode extBasicServiceCode,
            ExtBasicServiceCode extBasicServiceCode2) {
        return new OAnswerSpecificInfoImpl(destinationAddress, orCall, forwardedCall, chargeIndicator, extBasicServiceCode,
                extBasicServiceCode2);
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
    public TNoAnswerSpecificInfo createTNoAnswerSpecificInfo(boolean callForwarded,
            CalledPartyNumberCap forwardingDestinationNumber) {
        return new TNoAnswerSpecificInfoImpl(callForwarded, forwardingDestinationNumber);
    }

    @Override
    public TAnswerSpecificInfo createTAnswerSpecificInfo(CalledPartyNumberCap destinationAddress, boolean orCall,
            boolean forwardedCall, ChargeIndicator chargeIndicator, ExtBasicServiceCode extBasicServiceCode,
            ExtBasicServiceCode extBasicServiceCode2) {
        return new TAnswerSpecificInfoImpl(destinationAddress, orCall, forwardedCall, chargeIndicator, extBasicServiceCode,
                extBasicServiceCode2);
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
    public EventSpecificInformationBCSM createEventSpecificInformationBCSM(
            RouteSelectFailureSpecificInfo routeSelectFailureSpecificInfo) {
        return new EventSpecificInformationBCSMImpl(routeSelectFailureSpecificInfo);
    }

    @Override
    public EventSpecificInformationBCSM createEventSpecificInformationBCSM(
            OCalledPartyBusySpecificInfo oCalledPartyBusySpecificInfo) {
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
    public EventSpecificInformationBCSM createEventSpecificInformationBCSM(
            OChangeOfPositionSpecificInfo oChangeOfPositionSpecificInfo) {
        return new EventSpecificInformationBCSMImpl(oChangeOfPositionSpecificInfo);
    }

    @Override
    public EventSpecificInformationBCSM createEventSpecificInformationBCSM(
            TChangeOfPositionSpecificInfo tChangeOfPositionSpecificInfo) {
        return new EventSpecificInformationBCSMImpl(tChangeOfPositionSpecificInfo);
    }

    @Override
    public EventSpecificInformationBCSM createEventSpecificInformationBCSM(DpSpecificInfoAlt dpSpecificInfoAlt) {
        return new EventSpecificInformationBCSMImpl(dpSpecificInfoAlt);
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
    public TimeDurationChargingResult createTimeDurationChargingResult(ReceivingSideID partyToCharge,
            TimeInformation timeInformation, boolean legActive, boolean callLegReleasedAtTcpExpiry, CAPExtensions extensions,
            AChChargingAddress aChChargingAddress) {
        return new TimeDurationChargingResultImpl(partyToCharge, timeInformation, legActive, callLegReleasedAtTcpExpiry,
                extensions, aChChargingAddress);
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
        return new IPSSPCapabilitiesImpl(IPRoutingAddressSupported, VoiceBackSupported,
                VoiceInformationSupportedViaSpeechRecognition, VoiceInformationSupportedViaVoiceRecognition,
                GenerationOfVoiceAnnouncementsFromTextSupported, extraData);
    }

    @Override
    public InitialDPArgExtension createInitialDPArgExtension(ISDNAddressString gmscAddress,
            CalledPartyNumberCap forwardingDestinationNumber, MSClassmark2 msClassmark2, IMEI imei,
            SupportedCamelPhases supportedCamelPhases, OfferedCamel4Functionalities offeredCamel4Functionalities,
            BearerCapability bearerCapability2, ExtBasicServiceCode extBasicServiceCode2,
            HighLayerCompatibilityInap highLayerCompatibility2, LowLayerCompatibility lowLayerCompatibility,
            LowLayerCompatibility lowLayerCompatibility2, boolean enhancedDialledServicesAllowed, UUData uuData,
            boolean collectInformationAllowed, boolean releaseCallArgExtensionAllowed, boolean isCAPVersion3orLater) {
        return new InitialDPArgExtensionImpl(gmscAddress, forwardingDestinationNumber, msClassmark2, imei,
                supportedCamelPhases, offeredCamel4Functionalities, bearerCapability2, extBasicServiceCode2,
                highLayerCompatibility2, lowLayerCompatibility, lowLayerCompatibility2, enhancedDialledServicesAllowed, uuData,
                collectInformationAllowed, releaseCallArgExtensionAllowed, isCAPVersion3orLater);
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
    public NAOliInfo createNAOliInfo(int value) {
        return new NAOliInfoImpl(value);
    }

    @Override
    public ScfID createScfID(byte[] data) {
        return new ScfIDImpl(data);
    }

    @Override
    public ServiceInteractionIndicatorsTwo createServiceInteractionIndicatorsTwo(
            ForwardServiceInteractionInd forwardServiceInteractionInd,
            BackwardServiceInteractionInd backwardServiceInteractionInd,
            BothwayThroughConnectionInd bothwayThroughConnectionInd, ConnectedNumberTreatmentInd connectedNumberTreatmentInd,
            boolean nonCUGCall, HoldTreatmentIndicator holdTreatmentIndicator, CwTreatmentIndicator cwTreatmentIndicator,
            EctTreatmentIndicator ectTreatmentIndicator) {
        return new ServiceInteractionIndicatorsTwoImpl(forwardServiceInteractionInd, backwardServiceInteractionInd,
                bothwayThroughConnectionInd, connectedNumberTreatmentInd, nonCUGCall, holdTreatmentIndicator,
                cwTreatmentIndicator, ectTreatmentIndicator);
    }

    @Override
    public FCIBCCCAMELsequence1 createFCIBCCCAMELsequence1(FreeFormatData freeFormatData, SendingSideID partyToCharge,
            AppendFreeFormatData appendFreeFormatData) {
        return new FCIBCCCAMELsequence1Impl(freeFormatData, partyToCharge, appendFreeFormatData);
    }

    @Override
    public CAMELSCIBillingChargingCharacteristicsAlt createCAMELSCIBillingChargingCharacteristicsAlt() {
        return new CAMELSCIBillingChargingCharacteristicsAltImpl();
    }

    @Override
    public CAI_GSM0224 createCAI_GSM0224(Integer e1, Integer e2, Integer e3, Integer e4, Integer e5, Integer e6, Integer e7) {
        return new CAI_GSM0224Impl(e1, e2, e3, e4, e5, e6, e7);
    }

    @Override
    public AOCSubsequent createAOCSubsequent(CAI_GSM0224 cai_GSM0224, Integer tariffSwitchInterval) {
        return new AOCSubsequentImpl(cai_GSM0224, tariffSwitchInterval);
    }

    @Override
    public AOCBeforeAnswer createAOCBeforeAnswer(CAI_GSM0224 aocInitial, AOCSubsequent aocSubsequent) {
        return new AOCBeforeAnswerImpl(aocInitial, aocSubsequent);
    }

    @Override
    public SCIBillingChargingCharacteristics createSCIBillingChargingCharacteristics(AOCBeforeAnswer aocBeforeAnswer) {
        return new SCIBillingChargingCharacteristicsImpl(aocBeforeAnswer);
    }

    @Override
    public SCIBillingChargingCharacteristics createSCIBillingChargingCharacteristics(AOCSubsequent aocSubsequent) {
        return new SCIBillingChargingCharacteristicsImpl(aocSubsequent);
    }

    @Override
    public SCIBillingChargingCharacteristics createSCIBillingChargingCharacteristics(
            CAMELSCIBillingChargingCharacteristicsAlt aocExtension) {
        return new SCIBillingChargingCharacteristicsImpl(aocExtension);
    }

    @Override
    public VariablePartPrice createVariablePartPrice(byte[] data) {
        return new VariablePartPriceImpl(data);
    }

    @Override
    public VariablePartPrice createVariablePartPrice(double price) {
        return new VariablePartPriceImpl(price);
    }

    @Override
    public VariablePartPrice createVariablePartPrice(int integerPart, int hundredthPart) {
        return new VariablePartPriceImpl(integerPart, hundredthPart);
    }

    @Override
    public VariablePartDate createVariablePartDate(byte[] data) {
        return new VariablePartDateImpl(data);
    }

    @Override
    public VariablePartDate createVariablePartDate(int year, int month, int day) {
        return new VariablePartDateImpl(year, month, day);
    }

    @Override
    public VariablePartTime createVariablePartTime(byte[] data) {
        return new VariablePartTimeImpl(data);
    }

    @Override
    public VariablePartTime createVariablePartTime(int hour, int minute) {
        return new VariablePartTimeImpl(hour, minute);
    }

    @Override
    public VariablePart createVariablePart(Integer integer) {
        return new VariablePartImpl(integer);
    }

    @Override
    public VariablePart createVariablePart(Digits number) {
        return new VariablePartImpl(number);
    }

    @Override
    public VariablePart createVariablePart(VariablePartTime time) {
        return new VariablePartImpl(time);
    }

    @Override
    public VariablePart createVariablePart(VariablePartDate date) {
        return new VariablePartImpl(date);
    }

    @Override
    public VariablePart createVariablePart(VariablePartPrice price) {
        return new VariablePartImpl(price);
    }

    @Override
    public MessageIDText createMessageIDText(String messageContent, byte[] attributes) {
        return new MessageIDTextImpl(messageContent, attributes);
    }

    @Override
    public VariableMessage createVariableMessage(int elementaryMessageID, ArrayList<VariablePart> variableParts) {
        return new VariableMessageImpl(elementaryMessageID, variableParts);
    }

    @Override
    public MessageID createMessageID(Integer elementaryMessageID) {
        return new MessageIDImpl(elementaryMessageID);
    }

    @Override
    public MessageID createMessageID(MessageIDText text) {
        return new MessageIDImpl(text);
    }

    @Override
    public MessageID createMessageID(ArrayList<Integer> elementaryMessageIDs) {
        return new MessageIDImpl(elementaryMessageIDs);
    }

    @Override
    public MessageID createMessageID(VariableMessage variableMessage) {
        return new MessageIDImpl(variableMessage);
    }

    @Override
    public InbandInfo createInbandInfo(MessageID messageID, Integer numberOfRepetitions, Integer duration, Integer interval) {
        return new InbandInfoImpl(messageID, numberOfRepetitions, duration, interval);
    }

    @Override
    public Tone createTone(int toneID, Integer duration) {
        return new ToneImpl(toneID, duration);
    }

    @Override
    public InformationToSend createInformationToSend(InbandInfo inbandInfo) {
        return new InformationToSendImpl(inbandInfo);
    }

    @Override
    public InformationToSend createInformationToSend(Tone tone) {
        return new InformationToSendImpl(tone);
    }

    @Override
    public CollectedDigits createCollectedDigits(Integer minimumNbOfDigits, int maximumNbOfDigits, byte[] endOfReplyDigit,
            byte[] cancelDigit, byte[] startDigit, Integer firstDigitTimeOut, Integer interDigitTimeOut,
            ErrorTreatment errorTreatment, Boolean interruptableAnnInd, Boolean voiceInformation, Boolean voiceBack) {
        return new CollectedDigitsImpl(minimumNbOfDigits, maximumNbOfDigits, endOfReplyDigit, cancelDigit, startDigit,
                firstDigitTimeOut, interDigitTimeOut, errorTreatment, interruptableAnnInd, voiceInformation, voiceBack);
    }

    @Override
    public CollectedInfo createCollectedInfo(CollectedDigits collectedDigits) {
        return new CollectedInfoImpl(collectedDigits);
    }

    @Override
    public CallSegmentToCancel createCallSegmentToCancel(Integer invokeID, Integer callSegmentID) {
        return new CallSegmentToCancelImpl(invokeID, callSegmentID);
    }

    @Override
    public AccessPointName createAccessPointName(byte[] data) {
        return new AccessPointNameImpl(data);
    }

    @Override
    public AOCGPRS createAOCGPRS(CAI_GSM0224 aocInitial, AOCSubsequent aocSubsequent) {
        return new AOCGPRSImpl(aocInitial, aocSubsequent);
    }

    @Override
    public CAMELFCIGPRSBillingChargingCharacteristics createCAMELFCIGPRSBillingChargingCharacteristics(
            org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.FCIBCCCAMELsequence1Gprs fcIBCCCAMELsequence1) {

        return new CAMELFCIGPRSBillingChargingCharacteristicsImpl(fcIBCCCAMELsequence1);
    }

    @Override
    public CAMELSCIGPRSBillingChargingCharacteristics createCAMELSCIGPRSBillingChargingCharacteristics(AOCGPRS aocGPRS,
            PDPID pdpID) {

        return new CAMELSCIGPRSBillingChargingCharacteristicsImpl(aocGPRS, pdpID);
    }

    @Override
    public ChargingCharacteristics createChargingCharacteristics(long maxTransferredVolume) {
        return new ChargingCharacteristicsImpl(maxTransferredVolume);
    }

    @Override
    public ChargingCharacteristics createChargingCharacteristics(int maxElapsedTime) {
        return new ChargingCharacteristicsImpl(maxElapsedTime);
    }

    @Override
    public ChargingResult createChargingResult(TransferredVolume transferredVolume) {
        return new ChargingResultImpl(transferredVolume);
    }

    @Override
    public ChargingResult createChargingResult(ElapsedTime elapsedTime) {
        return new ChargingResultImpl(elapsedTime);
    }

    @Override
    public ChargingRollOver createChargingRollOver(ElapsedTimeRollOver elapsedTimeRollOver) {
        return new ChargingRollOverImpl(elapsedTimeRollOver);
    }

    @Override
    public ChargingRollOver createChargingRollOver(TransferredVolumeRollOver transferredVolumeRollOver) {
        return new ChargingRollOverImpl(transferredVolumeRollOver);
    }

    @Override
    public ElapsedTime createElapsedTime(Integer timeGPRSIfNoTariffSwitch) {
        return new ElapsedTimeImpl(timeGPRSIfNoTariffSwitch);
    }

    @Override
    public ElapsedTime createElapsedTime(TimeGPRSIfTariffSwitch timeGPRSIfTariffSwitch) {
        return new ElapsedTimeImpl(timeGPRSIfTariffSwitch);
    }

    @Override
    public ElapsedTimeRollOver createElapsedTimeRollOver(Integer roTimeGPRSIfNoTariffSwitch) {
        return new ElapsedTimeRollOverImpl(roTimeGPRSIfNoTariffSwitch);
    }

    @Override
    public ElapsedTimeRollOver createElapsedTimeRollOver(ROTimeGPRSIfTariffSwitch roTimeGPRSIfTariffSwitch) {
        return new ElapsedTimeRollOverImpl(roTimeGPRSIfTariffSwitch);
    }

    @Override
    public EndUserAddress createEndUserAddress(PDPTypeOrganization pdpTypeOrganization, PDPTypeNumber pdpTypeNumber,
            PDPAddress pdpAddress) {
        return new EndUserAddressImpl(pdpTypeOrganization, pdpTypeNumber, pdpAddress);
    }

    @Override
    public org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.FCIBCCCAMELsequence1Gprs createFCIBCCCAMELsequence1(
            FreeFormatDataGprs freeFormatData, PDPID pdpID, AppendFreeFormatData appendFreeFormatData) {
        return new org.mobicents.protocols.ss7.cap.service.gprs.primitive.FCIBCCCAMELsequence1GprsImpl(freeFormatData, pdpID,
                appendFreeFormatData);
    }

    @Override
    public FreeFormatDataGprs createFreeFormatDataGprs(byte[] data) {
        return new FreeFormatDataGprsImpl(data);
    }

    @Override
    public GPRSCause createGPRSCause(int data) {
        return new GPRSCauseImpl(data);
    }

    @Override
    public GPRSEvent createGPRSEvent(GPRSEventType gprsEventType, MonitorMode monitorMode) {
        return new GPRSEventImpl(gprsEventType, monitorMode);
    }

    @Override
    public GPRSEventSpecificInformation createGPRSEventSpecificInformation(LocationInformationGPRS locationInformationGPRS) {
        return new GPRSEventSpecificInformationImpl(locationInformationGPRS);
    }

    @Override
    public GPRSEventSpecificInformation createGPRSEventSpecificInformation(
            PdpContextchangeOfPositionSpecificInformation pdpContextchangeOfPositionSpecificInformation) {
        return new GPRSEventSpecificInformationImpl(pdpContextchangeOfPositionSpecificInformation);
    }

    @Override
    public GPRSEventSpecificInformation createGPRSEventSpecificInformation(DetachSpecificInformation detachSpecificInformation) {
        return new GPRSEventSpecificInformationImpl(detachSpecificInformation);
    }

    @Override
    public GPRSEventSpecificInformation createGPRSEventSpecificInformation(
            DisconnectSpecificInformation disconnectSpecificInformation) {
        return new GPRSEventSpecificInformationImpl(disconnectSpecificInformation);
    }

    @Override
    public GPRSEventSpecificInformation createGPRSEventSpecificInformation(
            PDPContextEstablishmentSpecificInformation pdpContextEstablishmentSpecificInformation) {
        return new GPRSEventSpecificInformationImpl(pdpContextEstablishmentSpecificInformation);
    }

    @Override
    public GPRSEventSpecificInformation createGPRSEventSpecificInformation(
            PDPContextEstablishmentAcknowledgementSpecificInformation pdpContextEstablishmentAcknowledgementSpecificInformation) {
        return new GPRSEventSpecificInformationImpl(pdpContextEstablishmentAcknowledgementSpecificInformation);
    }

    @Override
    public GPRSQoSExtension createGPRSQoSExtension(Ext2QoSSubscribed supplementToLongQoSFormat) {
        return new GPRSQoSExtensionImpl(supplementToLongQoSFormat);
    }

    @Override
    public GPRSQoS createGPRSQoS(QoSSubscribed shortQoSFormat) {
        return new GPRSQoSImpl(shortQoSFormat);
    }

    @Override
    public GPRSQoS createGPRSQoS(ExtQoSSubscribed longQoSFormat) {
        return new GPRSQoSImpl(longQoSFormat);
    }

    @Override
    public PDPAddress createPDPAddress(byte[] data) {
        return new PDPAddressImpl(data);
    }

    @Override
    public PDPID createPDPID(int data) {
        return new PDPIDImpl(data);
    }

    @Override
    public PDPTypeNumber createPDPTypeNumber(int data) {
        return new PDPTypeNumberImpl(data);
    }

    @Override
    public PDPTypeNumber createPDPTypeNumber(PDPTypeNumberValue value) {
        return new PDPTypeNumberImpl(value);
    }

    @Override
    public PDPTypeOrganization createPDPTypeOrganization(int data) {
        return new PDPTypeOrganizationImpl(data);
    }

    @Override
    public PDPTypeOrganization createPDPTypeOrganization(PDPTypeOrganizationValue value) {
        return new PDPTypeOrganizationImpl(value);
    }

    @Override
    public QualityOfService createQualityOfService(GPRSQoS requestedQoS, GPRSQoS subscribedQoS, GPRSQoS negotiatedQoS,
            GPRSQoSExtension requestedQoSExtension, GPRSQoSExtension subscribedQoSExtension,
            GPRSQoSExtension negotiatedQoSExtension) {
        return new QualityOfServiceImpl(requestedQoS, subscribedQoS, negotiatedQoS, requestedQoSExtension,
                subscribedQoSExtension, negotiatedQoSExtension);
    }

    @Override
    public ROTimeGPRSIfTariffSwitch createROTimeGPRSIfTariffSwitch(Integer roTimeGPRSSinceLastTariffSwitch,
            Integer roTimeGPRSTariffSwitchInterval) {
        return new ROTimeGPRSIfTariffSwitchImpl(roTimeGPRSSinceLastTariffSwitch, roTimeGPRSTariffSwitchInterval);
    }

    @Override
    public ROVolumeIfTariffSwitch createROVolumeIfTariffSwitch(Integer roVolumeSinceLastTariffSwitch,
            Integer roVolumeTariffSwitchInterval) {
        return new ROVolumeIfTariffSwitchImpl(roVolumeSinceLastTariffSwitch, roVolumeTariffSwitchInterval);
    }

    @Override
    public SGSNCapabilities createSGSNCapabilities(int data) {
        return new SGSNCapabilitiesImpl(data);
    }

    @Override
    public SGSNCapabilities createSGSNCapabilities(boolean aoCSupportedBySGSN) {
        return new SGSNCapabilitiesImpl(aoCSupportedBySGSN);
    }

    @Override
    public TimeGPRSIfTariffSwitch createTimeGPRSIfTariffSwitch(int timeGPRSSinceLastTariffSwitch,
            Integer timeGPRSTariffSwitchInterval) {
        return new TimeGPRSIfTariffSwitchImpl(timeGPRSSinceLastTariffSwitch, timeGPRSTariffSwitchInterval);
    }

    @Override
    public TransferredVolume createTransferredVolume(Long volumeIfNoTariffSwitch) {
        return new TransferredVolumeImpl(volumeIfNoTariffSwitch);
    }

    @Override
    public TransferredVolume createTransferredVolume(VolumeIfTariffSwitch volumeIfTariffSwitch) {
        return new TransferredVolumeImpl(volumeIfTariffSwitch);
    }

    @Override
    public TransferredVolumeRollOver createTransferredVolumeRollOver(Integer roVolumeIfNoTariffSwitch) {
        return new TransferredVolumeRollOverImpl(roVolumeIfNoTariffSwitch);
    }

    @Override
    public TransferredVolumeRollOver createTransferredVolumeRollOver(ROVolumeIfTariffSwitch roVolumeIfTariffSwitch) {
        return new TransferredVolumeRollOverImpl(roVolumeIfTariffSwitch);
    }

    @Override
    public VolumeIfTariffSwitch createVolumeIfTariffSwitch(long volumeSinceLastTariffSwitch, Long volumeTariffSwitchInterval) {
        return new VolumeIfTariffSwitchImpl(volumeSinceLastTariffSwitch, volumeTariffSwitchInterval);
    }

    @Override
    public DetachSpecificInformation createDetachSpecificInformation(InitiatingEntity initiatingEntity,
            boolean routeingAreaUpdate) {
        return new DetachSpecificInformationImpl(initiatingEntity, routeingAreaUpdate);
    }

    @Override
    public DisconnectSpecificInformation createDisconnectSpecificInformation(InitiatingEntity initiatingEntity,
            boolean routeingAreaUpdate) {
        return new DisconnectSpecificInformationImpl(initiatingEntity, routeingAreaUpdate);
    }

    @Override
    public PdpContextchangeOfPositionSpecificInformation createPdpContextchangeOfPositionSpecificInformation(
            AccessPointName accessPointName, GPRSChargingID chargingID, LocationInformationGPRS locationInformationGPRS,
            EndUserAddress endUserAddress, QualityOfService qualityOfService, TimeAndTimezone timeAndTimezone,
            GSNAddress gsnAddress) {
        return new PdpContextchangeOfPositionSpecificInformationImpl(accessPointName, chargingID, locationInformationGPRS,
                endUserAddress, qualityOfService, timeAndTimezone, gsnAddress);
    }

    @Override
    public PDPContextEstablishmentAcknowledgementSpecificInformation createPDPContextEstablishmentAcknowledgementSpecificInformation(
            AccessPointName accessPointName, GPRSChargingID chargingID, LocationInformationGPRS locationInformationGPRS,
            EndUserAddress endUserAddress, QualityOfService qualityOfService, TimeAndTimezone timeAndTimezone,
            GSNAddress gsnAddress) {
        return new PDPContextEstablishmentAcknowledgementSpecificInformationImpl(accessPointName, chargingID,
                locationInformationGPRS, endUserAddress, qualityOfService, timeAndTimezone, gsnAddress);
    }

    @Override
    public PDPContextEstablishmentSpecificInformation createPDPContextEstablishmentSpecificInformation(
            AccessPointName accessPointName, EndUserAddress endUserAddress, QualityOfService qualityOfService,
            LocationInformationGPRS locationInformationGPRS, TimeAndTimezone timeAndTimezone,
            PDPInitiationType pdpInitiationType, boolean secondaryPDPContext) {
        return new PDPContextEstablishmentSpecificInformationImpl(accessPointName, endUserAddress, qualityOfService,
                locationInformationGPRS, timeAndTimezone, pdpInitiationType, secondaryPDPContext);
    }

    @Override
    public TPValidityPeriod createTPValidityPeriod(byte[] data) {
        return new TPValidityPeriodImpl(data);
    }

    @Override
    public TPShortMessageSpecificInfo createTPShortMessageSpecificInfo(int data) {
        return new TPShortMessageSpecificInfoImpl(data);
    }

    @Override
    public TPProtocolIdentifier createTPProtocolIdentifier(int data) {
        return new TPProtocolIdentifierImpl(data);
    }

    @Override
    public TPDataCodingScheme createTPDataCodingScheme(int data) {
        return new TPDataCodingSchemeImpl(data);
    }

    @Override
    public SMSEvent createSMSEvent(EventTypeSMS eventTypeSMS, MonitorMode monitorMode) {
        return new SMSEventImpl(eventTypeSMS, monitorMode);
    }

    @Override
    public SMSAddressString createSMSAddressString(AddressNature addressNature, NumberingPlan numberingPlan, String address) {
        return new SMSAddressStringImpl(addressNature, numberingPlan, address);
    }

    @Override
    public RPCause createRPCause(int data) {
        return new RPCauseImpl(data);
    }

    @Override
    public MTSMSCause createMTSMSCause(int data) {
        return new MTSMSCauseImpl(data);
    }

    @Override
    public org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.FreeFormatData createFreeFormatData(byte[] data) {
        return new org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.FreeFormatDataImpl(data);
    }

    @Override
    public org.mobicents.protocols.ss7.cap.api.service.sms.primitive.FCIBCCCAMELsequence1SMS createFCIBCCCAMELsequence1(
            org.mobicents.protocols.ss7.cap.api.service.sms.primitive.FreeFormatDataSMS freeFormatData,
            AppendFreeFormatData appendFreeFormatData) {
        return new org.mobicents.protocols.ss7.cap.service.sms.primitive.FCIBCCCAMELsequence1SMSImpl(freeFormatData,
                appendFreeFormatData);
    }

    @Override
    public EventSpecificInformationSMS createEventSpecificInformationSMSImpl(
            OSmsFailureSpecificInfo oSmsFailureSpecificInfo) {
        return new EventSpecificInformationSMSImpl(oSmsFailureSpecificInfo);
    }

    @Override
    public EventSpecificInformationSMS createEventSpecificInformationSMSImpl(
            OSmsSubmissionSpecificInfo oSmsSubmissionSpecificInfo) {
        return new EventSpecificInformationSMSImpl(oSmsSubmissionSpecificInfo);
    }

    @Override
    public EventSpecificInformationSMS createEventSpecificInformationSMSImpl(
            TSmsFailureSpecificInfo tSmsFailureSpecificInfo) {
        return new EventSpecificInformationSMSImpl(tSmsFailureSpecificInfo);
    }

    @Override
    public EventSpecificInformationSMS createEventSpecificInformationSMSImpl(
            TSmsDeliverySpecificInfo tSmsDeliverySpecificInfo) {
        return new EventSpecificInformationSMSImpl(tSmsDeliverySpecificInfo);
    }

    @Override
    public FreeFormatDataSMS createFreeFormatDataSMS(byte[] data) {
        return new FreeFormatDataSMSImpl(data);
    }

    @Override
    public OSmsFailureSpecificInfo createOSmsFailureSpecificInfo(MOSMSCause failureCause) {
        return new OSmsFailureSpecificInfoImpl(failureCause);
    }

    @Override
    public OSmsSubmissionSpecificInfo createOSmsSubmissionSpecificInfo() {
        return new OSmsSubmissionSpecificInfoImpl();
    }

    @Override
    public TSmsFailureSpecificInfo createTSmsFailureSpecificInfo(MTSMSCause failureCause) {
        return new TSmsFailureSpecificInfoImpl(failureCause);
    }

    @Override
    public TSmsDeliverySpecificInfo createTSmsDeliverySpecificInfo() {
        return new TSmsDeliverySpecificInfoImpl();
    }

    @Override
    public LegOrCallSegment createLegOrCallSegment(Integer callSegmentID) {
        return new LegOrCallSegmentImpl(callSegmentID);
    }

    @Override
    public LegOrCallSegment createLegOrCallSegment(LegID legID) {
        return new LegOrCallSegmentImpl(legID);
    }

    @Override
    public ChargeIndicator createChargeIndicator(int data) {
        return new ChargeIndicatorImpl(data);
    }

    @Override
    public ChargeIndicator createChargeIndicator(ChargeIndicatorValue value) {
        return new ChargeIndicatorImpl(value);
    }

    @Override
    public BackwardServiceInteractionInd createBackwardServiceInteractionInd(ConferenceTreatmentIndicator conferenceTreatmentIndicator,
            CallCompletionTreatmentIndicator callCompletionTreatmentIndicator) {
        return new BackwardServiceInteractionIndImpl(conferenceTreatmentIndicator, callCompletionTreatmentIndicator);
    }

    @Override
    public Carrier createCarrier(byte[] data) {
        return new CarrierImpl(data);
    }

    @Override
    public ForwardServiceInteractionInd createForwardServiceInteractionInd(ConferenceTreatmentIndicator conferenceTreatmentIndicator,
            CallDiversionTreatmentIndicator callDiversionTreatmentIndicator, CallingPartyRestrictionIndicator callingPartyRestrictionIndicator) {
        return new ForwardServiceInteractionIndImpl(conferenceTreatmentIndicator, callDiversionTreatmentIndicator, callingPartyRestrictionIndicator);
    }

    @Override
    public LowLayerCompatibility createLowLayerCompatibility(byte[] data) {
        return new LowLayerCompatibilityImpl(data);
    }

    @Override
    public MidCallEvents createMidCallEvents_Completed(Digits dtmfDigits) {
        return new MidCallEventsImpl(dtmfDigits, true);
    }

    @Override
    public MidCallEvents createMidCallEvents_TimeOut(Digits dtmfDigits) {
        return new MidCallEventsImpl(dtmfDigits, false);
    }

    @Override
    public OMidCallSpecificInfo createOMidCallSpecificInfo(MidCallEvents midCallEvents) {
        return new OMidCallSpecificInfoImpl(midCallEvents);
    }

    @Override
    public TMidCallSpecificInfo createTMidCallSpecificInfo(MidCallEvents midCallEvents) {
        return new TMidCallSpecificInfoImpl(midCallEvents);
    }

    @Override
    public OTermSeizedSpecificInfo createOTermSeizedSpecificInfo(LocationInformation locationInformation) {
        return new OTermSeizedSpecificInfoImpl(locationInformation);
    }

    @Override
    public CallAcceptedSpecificInfo createCallAcceptedSpecificInfo(LocationInformation locationInformation) {
        return new CallAcceptedSpecificInfoImpl(locationInformation);
    }

    @Override
    public MetDPCriterionAlt createMetDPCriterionAlt() {
        return new MetDPCriterionAltImpl();
    }

    @Override
    public MetDPCriterion createMetDPCriterion_enteringCellGlobalId(CellGlobalIdOrServiceAreaIdFixedLength value) {
        return new MetDPCriterionImpl(value, MetDPCriterionImpl.CellGlobalIdOrServiceAreaIdFixedLength_Option.enteringCellGlobalId);
    }

    @Override
    public MetDPCriterion createMetDPCriterion_leavingCellGlobalId(CellGlobalIdOrServiceAreaIdFixedLength value) {
        return new MetDPCriterionImpl(value, MetDPCriterionImpl.CellGlobalIdOrServiceAreaIdFixedLength_Option.leavingCellGlobalId);
    }

    @Override
    public MetDPCriterion createMetDPCriterion_enteringServiceAreaId(CellGlobalIdOrServiceAreaIdFixedLength value) {
        return new MetDPCriterionImpl(value, MetDPCriterionImpl.CellGlobalIdOrServiceAreaIdFixedLength_Option.enteringServiceAreaId);
    }

    @Override
    public MetDPCriterion createMetDPCriterion_leavingServiceAreaId(CellGlobalIdOrServiceAreaIdFixedLength value) {
        return new MetDPCriterionImpl(value, MetDPCriterionImpl.CellGlobalIdOrServiceAreaIdFixedLength_Option.leavingServiceAreaId);
    }

    @Override
    public MetDPCriterion createMetDPCriterion_enteringLocationAreaId(LAIFixedLength value) {
        return new MetDPCriterionImpl(value, MetDPCriterionImpl.LAIFixedLength_Option.enteringLocationAreaId);
    }

    @Override
    public MetDPCriterion createMetDPCriterion_leavingLocationAreaId(LAIFixedLength value) {
        return new MetDPCriterionImpl(value, MetDPCriterionImpl.LAIFixedLength_Option.leavingLocationAreaId);
    }

    @Override
    public MetDPCriterion createMetDPCriterion_interSystemHandOverToUMTS() {
        return new MetDPCriterionImpl(MetDPCriterionImpl.Boolean_Option.interSystemHandOverToUMTS);
    }

    @Override
    public MetDPCriterion createMetDPCriterion_interSystemHandOverToGSM() {
        return new MetDPCriterionImpl(MetDPCriterionImpl.Boolean_Option.interSystemHandOverToGSM);
    }

    @Override
    public MetDPCriterion createMetDPCriterion_interPLMNHandOver() {
        return new MetDPCriterionImpl(MetDPCriterionImpl.Boolean_Option.interPLMNHandOver);
    }

    @Override
    public MetDPCriterion createMetDPCriterion_interMSCHandOver() {
        return new MetDPCriterionImpl(MetDPCriterionImpl.Boolean_Option.interMSCHandOver);
    }

    @Override
    public MetDPCriterion createMetDPCriterion(MetDPCriterionAlt metDPCriterionAlt) {
        return new MetDPCriterionImpl(metDPCriterionAlt);
    }

    @Override
    public OChangeOfPositionSpecificInfo createOChangeOfPositionSpecificInfo(LocationInformation locationInformation,
            ArrayList<MetDPCriterion> metDPCriteriaList) {
        return new OChangeOfPositionSpecificInfoImpl(locationInformation, metDPCriteriaList);
    }

    @Override
    public TChangeOfPositionSpecificInfo createTChangeOfPositionSpecificInfo(LocationInformation locationInformation,
            ArrayList<MetDPCriterion> metDPCriteriaList) {
        return new TChangeOfPositionSpecificInfoImpl(locationInformation, metDPCriteriaList);
    }

    @Override
    public OServiceChangeSpecificInfo createOServiceChangeSpecificInfo(ExtBasicServiceCode extBasicServiceCode) {
        return new OServiceChangeSpecificInfoImpl(extBasicServiceCode);
    }

    @Override
    public TServiceChangeSpecificInfo createTServiceChangeSpecificInfo(ExtBasicServiceCode extBasicServiceCode) {
        return new TServiceChangeSpecificInfoImpl(extBasicServiceCode);
    }

    @Override
    public CollectedInfoSpecificInfo createCollectedInfoSpecificInfo(CalledPartyNumberCap calledPartyNumber) {
        return new CollectedInfoSpecificInfoImpl(calledPartyNumber);
    }

    @Override
    public DpSpecificInfoAlt createDpSpecificInfoAlt(OServiceChangeSpecificInfo oServiceChangeSpecificInfo,
            CollectedInfoSpecificInfo collectedInfoSpecificInfo, TServiceChangeSpecificInfo tServiceChangeSpecificInfo) {
        return new DpSpecificInfoAltImpl(oServiceChangeSpecificInfo, collectedInfoSpecificInfo, tServiceChangeSpecificInfo);
    }

    @Override
    public ChangeOfLocationAlt createChangeOfLocationAlt() {
        return new ChangeOfLocationAltImpl();
    }

    @Override
    public ChangeOfLocation createChangeOfLocation_cellGlobalId(CellGlobalIdOrServiceAreaIdFixedLength value) {
        return new ChangeOfLocationImpl(value, ChangeOfLocationImpl.CellGlobalIdOrServiceAreaIdFixedLength_Option.cellGlobalId);
    }

    @Override
    public ChangeOfLocation createChangeOfLocation_serviceAreaId(CellGlobalIdOrServiceAreaIdFixedLength value) {
        return new ChangeOfLocationImpl(value, ChangeOfLocationImpl.CellGlobalIdOrServiceAreaIdFixedLength_Option.serviceAreaId);
    }

    @Override
    public ChangeOfLocation createChangeOfLocation(LAIFixedLength locationAreaId) {
        return new ChangeOfLocationImpl(locationAreaId);
    }

    @Override
    public ChangeOfLocation createChangeOfLocation_interSystemHandOver() {
        return new ChangeOfLocationImpl(ChangeOfLocationImpl.Boolean_Option.interSystemHandOver);
    }

    @Override
    public ChangeOfLocation createChangeOfLocation_interPLMNHandOver() {
        return new ChangeOfLocationImpl(ChangeOfLocationImpl.Boolean_Option.interPLMNHandOver);
    }

    @Override
    public ChangeOfLocation createChangeOfLocation_interMSCHandOver() {
        return new ChangeOfLocationImpl(ChangeOfLocationImpl.Boolean_Option.interMSCHandOver);
    }

    @Override
    public ChangeOfLocation createChangeOfLocation(ChangeOfLocationAlt changeOfLocationAlt) {
        return new ChangeOfLocationImpl(changeOfLocationAlt);
    }

    @Override
    public DpSpecificCriteriaAlt createDpSpecificCriteriaAlt(ArrayList<ChangeOfLocation> changeOfPositionControlInfo, Integer numberOfDigits) {
        return new DpSpecificCriteriaAltImpl(changeOfPositionControlInfo, numberOfDigits);
    }

    @Override
    public MidCallControlInfo createMidCallControlInfo(Integer minimumNumberOfDigits, Integer maximumNumberOfDigits, String endOfReplyDigit,
            String cancelDigit, String startDigit, Integer interDigitTimeout) {
        return new MidCallControlInfoImpl(minimumNumberOfDigits, maximumNumberOfDigits, endOfReplyDigit, cancelDigit, startDigit, interDigitTimeout);
    }

    @Override
    public Burst createBurst(Integer numberOfBursts, Integer burstInterval, Integer numberOfTonesInBurst, Integer toneDuration, Integer toneInterval) {
        return new BurstImpl(numberOfBursts, burstInterval, numberOfTonesInBurst, toneDuration, toneInterval);
    }

    @Override
    public BurstList createBurstList(Integer warningPeriod, Burst burst) {
        return new BurstListImpl(warningPeriod, burst);
    }

    @Override
    public AudibleIndicator createAudibleIndicator(Boolean tone) {
        return new AudibleIndicatorImpl(tone);
    }

    @Override
    public AudibleIndicator createAudibleIndicator(BurstList burstList) {
        return new AudibleIndicatorImpl(burstList);
    }

    @Override
    public AChChargingAddress createAChChargingAddress(LegID legID) {
        return new AChChargingAddressImpl(legID);
    }

    @Override
    public AChChargingAddress createAChChargingAddress(int srfConnection) {
        return new AChChargingAddressImpl(srfConnection);
    }
}
