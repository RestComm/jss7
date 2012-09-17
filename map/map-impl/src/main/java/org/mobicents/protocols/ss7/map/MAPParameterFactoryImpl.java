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

package org.mobicents.protocols.ss7.map;

import java.nio.charset.Charset;
import java.util.ArrayList;

import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.ss7.isup.message.parameter.LocationNumber;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.primitives.AdditionalNumberType;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;
import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.mobicents.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.mobicents.protocols.ss7.map.api.primitives.FTNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LAIFixedLength;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.MAPPrivateExtension;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.PlmnId;
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallReferenceNumber;
import org.mobicents.protocols.ss7.map.api.service.lsm.AddGeographicalInformation;
import org.mobicents.protocols.ss7.map.api.service.lsm.AdditionalNumber;
import org.mobicents.protocols.ss7.map.api.service.lsm.Area;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaDefinition;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaEventInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaIdentification;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaType;
import org.mobicents.protocols.ss7.map.api.service.lsm.DeferredLocationEventType;
import org.mobicents.protocols.ss7.map.api.service.lsm.DeferredmtlrData;
import org.mobicents.protocols.ss7.map.api.service.lsm.ExtGeographicalInformation;
import org.mobicents.protocols.ss7.map.api.service.lsm.GeranGANSSpositioningData;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientExternalID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientInternalID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientName;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSCodeword;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSFormatIndicator;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSQoS;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSRequestorID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LocationEstimateType;
import org.mobicents.protocols.ss7.map.api.service.lsm.LocationType;
import org.mobicents.protocols.ss7.map.api.service.lsm.OccurrenceInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.PeriodicLDRInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.PositioningDataInformation;
import org.mobicents.protocols.ss7.map.api.service.lsm.PrivacyCheckRelatedAction;
import org.mobicents.protocols.ss7.map.api.service.lsm.RANTechnology;
import org.mobicents.protocols.ss7.map.api.service.lsm.ReportingPLMN;
import org.mobicents.protocols.ss7.map.api.service.lsm.ReportingPLMNList;
import org.mobicents.protocols.ss7.map.api.service.lsm.ResponseTime;
import org.mobicents.protocols.ss7.map.api.service.lsm.ResponseTimeCategory;
import org.mobicents.protocols.ss7.map.api.service.lsm.SLRArgExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.lsm.SLRArgPCSExtensions;
import org.mobicents.protocols.ss7.map.api.service.lsm.ServingNodeAddress;
import org.mobicents.protocols.ss7.map.api.service.lsm.SupportedGADShapes;
import org.mobicents.protocols.ss7.map.api.service.lsm.TerminationCause;
import org.mobicents.protocols.ss7.map.api.service.lsm.UtranGANSSpositioningData;
import org.mobicents.protocols.ss7.map.api.service.lsm.UtranPositioningDataInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.VelocityEstimate;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationQuintuplet;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetList;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationTriplet;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.EpcAv;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.EpsAuthenticationSetList;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.QuintupletList;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.ReSynchronisationInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.TripletList;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.RequestedEquipmentInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.UESBIIu;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.UESBIIuA;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.UESBIIuB;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.ADDInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.IMSIWithLMSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.ISTSupportIndicator;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.LAC;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.LocationArea;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.PagingArea;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SuperChargerInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySets;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SupportedRATTypes;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.VLRCapability;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.DomainType;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.EUtranCgi;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSChargingID;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSMSClass;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GeodeticInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GeographicalInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationEPS;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationNumberMap;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.MNPInfoRes;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.MSClassmark2;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.MSNetworkCapability;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.MSRadioAccessCapability;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.NotReachableReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.NumberPortabilityStatus;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.PDPContextInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.PSSubscriberState;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.PSSubscriberStateChoice;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RAIdentity;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RouteingNumber;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberState;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberStateChoice;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.TAId;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.TEID;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.TransactionId;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.UserCSGInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.APN;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGId;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristics;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext2QoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext3QoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext4QoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtPDPType;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAIdentity;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPAddress;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPType;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.map.api.service.sms.LocationInfoWithLMSI;
import org.mobicents.protocols.ss7.map.api.service.sms.MWStatus;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_SMEA;
import org.mobicents.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsTpdu;
import org.mobicents.protocols.ss7.map.dialog.MAPUserAbortChoiceImpl;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.mobicents.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.mobicents.protocols.ss7.map.primitives.DiameterIdentityImpl;
import org.mobicents.protocols.ss7.map.primitives.FTNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.GSNAddressImpl;
import org.mobicents.protocols.ss7.map.primitives.IMEIImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.LAIFixedLengthImpl;
import org.mobicents.protocols.ss7.map.primitives.LMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPPrivateExtensionImpl;
import org.mobicents.protocols.ss7.map.primitives.PlmnIdImpl;
import org.mobicents.protocols.ss7.map.primitives.SubscriberIdentityImpl;
import org.mobicents.protocols.ss7.map.primitives.USSDStringImpl;
import org.mobicents.protocols.ss7.map.service.callhandling.CallReferenceNumberImpl;
import org.mobicents.protocols.ss7.map.service.lsm.AddGeographicalInformationImpl;
import org.mobicents.protocols.ss7.map.service.lsm.AdditionalNumberImpl;
import org.mobicents.protocols.ss7.map.service.lsm.AreaDefinitionImpl;
import org.mobicents.protocols.ss7.map.service.lsm.AreaEventInfoImpl;
import org.mobicents.protocols.ss7.map.service.lsm.AreaIdentificationImpl;
import org.mobicents.protocols.ss7.map.service.lsm.AreaImpl;
import org.mobicents.protocols.ss7.map.service.lsm.DeferredLocationEventTypeImpl;
import org.mobicents.protocols.ss7.map.service.lsm.DeferredmtlrDataImpl;
import org.mobicents.protocols.ss7.map.service.lsm.ExtGeographicalInformationImpl;
import org.mobicents.protocols.ss7.map.service.lsm.GeranGANSSpositioningDataImpl;
import org.mobicents.protocols.ss7.map.service.lsm.LCSClientExternalIDImpl;
import org.mobicents.protocols.ss7.map.service.lsm.LCSClientIDImpl;
import org.mobicents.protocols.ss7.map.service.lsm.LCSClientNameImpl;
import org.mobicents.protocols.ss7.map.service.lsm.LCSCodewordImpl;
import org.mobicents.protocols.ss7.map.service.lsm.LCSLocationInfoImpl;
import org.mobicents.protocols.ss7.map.service.lsm.LCSPrivacyCheckImpl;
import org.mobicents.protocols.ss7.map.service.lsm.LCSQoSImpl;
import org.mobicents.protocols.ss7.map.service.lsm.LCSRequestorIDImpl;
import org.mobicents.protocols.ss7.map.service.lsm.LocationTypeImpl;
import org.mobicents.protocols.ss7.map.service.lsm.PeriodicLDRInfoImpl;
import org.mobicents.protocols.ss7.map.service.lsm.PositioningDataInformationImpl;
import org.mobicents.protocols.ss7.map.service.lsm.ReportingPLMNImpl;
import org.mobicents.protocols.ss7.map.service.lsm.ReportingPLMNListImpl;
import org.mobicents.protocols.ss7.map.service.lsm.ResponseTimeImpl;
import org.mobicents.protocols.ss7.map.service.lsm.SLRArgExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.lsm.SLRArgPCSExtensionsImpl;
import org.mobicents.protocols.ss7.map.service.lsm.ServingNodeAddressImpl;
import org.mobicents.protocols.ss7.map.service.lsm.SupportedGADShapesImpl;
import org.mobicents.protocols.ss7.map.service.lsm.UtranGANSSpositioningDataImpl;
import org.mobicents.protocols.ss7.map.service.lsm.UtranPositioningDataInfoImpl;
import org.mobicents.protocols.ss7.map.service.lsm.VelocityEstimateImpl;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.AuthenticationQuintupletImpl;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.AuthenticationSetListImpl;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.AuthenticationTripletImpl;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.EpcAvImpl;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.EpsAuthenticationSetListImpl;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.QuintupletListImpl;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.ReSynchronisationInfoImpl;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.TripletListImpl;
import org.mobicents.protocols.ss7.map.service.mobility.imei.RequestedEquipmentInfoImpl;
import org.mobicents.protocols.ss7.map.service.mobility.imei.UESBIIuAImpl;
import org.mobicents.protocols.ss7.map.service.mobility.imei.UESBIIuBImpl;
import org.mobicents.protocols.ss7.map.service.mobility.imei.UESBIIuImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.ADDInfoImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.IMSIWithLMSIImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.LACImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.LocationAreaImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.PagingAreaImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.SuperChargerInfoImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.SupportedLCSCapabilitySetsImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.SupportedRATTypesImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.VLRCapabilityImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.AnyTimeInterrogationRequestImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.AnyTimeInterrogationResponseImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.EUtranCgiImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.GPRSChargingIDImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.GPRSMSClassImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.GeodeticInformationImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.GeographicalInformationImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.LocationInformationEPSImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.LocationInformationGPRSImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.LocationInformationImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.LocationNumberMapImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.MNPInfoResImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.MSClassmark2Impl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.MSNetworkCapabilityImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.MSRadioAccessCapabilityImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.PDPContextInfoImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.PSSubscriberStateImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.RAIdentityImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.RequestedInfoImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.RouteingNumberImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.SubscriberInfoImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.SubscriberStateImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.TAIdImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.TEIDImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.TransactionIdImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.UserCSGInformationImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.APNImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.CSGIdImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ChargingCharacteristicsImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.Ext2QoSSubscribedImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.Ext3QoSSubscribedImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.Ext4QoSSubscribedImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBearerServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtPDPTypeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtQoSSubscribedImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtTeleserviceCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.LSAIdentityImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.PDPAddressImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.PDPTypeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;
import org.mobicents.protocols.ss7.map.service.sms.LocationInfoWithLMSIImpl;
import org.mobicents.protocols.ss7.map.service.sms.MWStatusImpl;
import org.mobicents.protocols.ss7.map.service.sms.SM_RP_DAImpl;
import org.mobicents.protocols.ss7.map.service.sms.SM_RP_OAImpl;
import org.mobicents.protocols.ss7.map.service.sms.SM_RP_SMEAImpl;
import org.mobicents.protocols.ss7.map.service.sms.SmsSignalInfoImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.ProcessUnstructuredSSRequestImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.ProcessUnstructuredSSResponseImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.UnstructuredSSNotifyRequestImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.UnstructuredSSNotifyResponseImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.UnstructuredSSRequestImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.UnstructuredSSResponseImpl;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.GeneralProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.InvokeProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tcap.asn.comp.ProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnErrorProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultProblemType;

/**
 * 
 * @author amit bhayani
 * 
 */
public class MAPParameterFactoryImpl implements MAPParameterFactory {

	public ProcessUnstructuredSSRequest createProcessUnstructuredSSRequestIndication(CBSDataCodingScheme ussdDataCodingSch, USSDString ussdString,
			AlertingPattern alertingPattern, ISDNAddressString msisdnAddressString) {

		ProcessUnstructuredSSRequest request = new ProcessUnstructuredSSRequestImpl(ussdDataCodingSch, ussdString, alertingPattern,
				msisdnAddressString);
		return request;
	}

	public ProcessUnstructuredSSResponse createProcessUnstructuredSSResponseIndication(CBSDataCodingScheme ussdDataCodingScheme, USSDString ussdString) {
		ProcessUnstructuredSSResponse response = new ProcessUnstructuredSSResponseImpl(ussdDataCodingScheme, ussdString);
		return response;
	}

	public UnstructuredSSRequest createUnstructuredSSRequestIndication(CBSDataCodingScheme ussdDataCodingSch, USSDString ussdString,
			AlertingPattern alertingPattern, ISDNAddressString msisdnAddressString) {
		UnstructuredSSRequest request = new UnstructuredSSRequestImpl(ussdDataCodingSch, ussdString, alertingPattern, msisdnAddressString);
		return request;
	}

	public UnstructuredSSResponse createUnstructuredSSRequestIndication(CBSDataCodingScheme ussdDataCodingScheme, USSDString ussdString) {
		UnstructuredSSResponse response = new UnstructuredSSResponseImpl(ussdDataCodingScheme, ussdString);
		return response;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.MAPParameterFactory#createUnstructuredSSNotifyRequestIndication(byte, org.mobicents.protocols.ss7.map.api.primitives.USSDString, org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern, org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString)
	 */
	public UnstructuredSSNotifyRequest createUnstructuredSSNotifyRequestIndication(CBSDataCodingScheme ussdDataCodingSch, USSDString ussdString,
			AlertingPattern alertingPattern, ISDNAddressString msisdnAddressString) {
		UnstructuredSSNotifyRequest request = new UnstructuredSSNotifyRequestImpl(ussdDataCodingSch, ussdString, alertingPattern, msisdnAddressString);
		return request;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.MAPParameterFactory#createUnstructuredSSNotifyResponseIndication()
	 */
	public UnstructuredSSNotifyResponse createUnstructuredSSNotifyResponseIndication() {
		UnstructuredSSNotifyResponse response = new UnstructuredSSNotifyResponseImpl();
		return response;
	}

	public USSDString createUSSDString(String ussdString, CBSDataCodingScheme dataCodingScheme, Charset gsm8Charset) {
		return new USSDStringImpl(ussdString, dataCodingScheme, gsm8Charset);
	}

	public USSDString createUSSDString(String ussdString) {
		return new USSDStringImpl(ussdString, null, null);
	}

	public USSDString createUSSDString(byte[] ussdString, CBSDataCodingScheme dataCodingScheme, Charset gsm8Charset) {
		return new USSDStringImpl(ussdString, dataCodingScheme, gsm8Charset);
	}

	public USSDString createUSSDString(byte[] ussdString) {
		return new USSDStringImpl(ussdString, null, null);
	}

	public AddressString createAddressString(AddressNature addNature, NumberingPlan numPlan, String address) {
		return new AddressStringImpl(addNature, numPlan, address);
	}

	public ISDNAddressString createISDNAddressString(AddressNature addNature, NumberingPlan numPlan, String address) {
		return new ISDNAddressStringImpl(addNature, numPlan, address);
	} 

	public FTNAddressString createFTNAddressString(AddressNature addNature, NumberingPlan numPlan, String address) {
		return new FTNAddressStringImpl(addNature, numPlan, address);
	}

	public MAPUserAbortChoice createMAPUserAbortChoice() {
		MAPUserAbortChoiceImpl mapUserAbortChoice = new MAPUserAbortChoiceImpl();
		return mapUserAbortChoice;
	}

	public MAPPrivateExtension createMAPPrivateExtension(long[] oId, byte[] data) {
		return new MAPPrivateExtensionImpl(oId, data);
	}

	public MAPExtensionContainer createMAPExtensionContainer(ArrayList<MAPPrivateExtension> privateExtensionList,
			byte[] pcsExtensions) {
		return new MAPExtensionContainerImpl(privateExtensionList, pcsExtensions);
	}

	public IMSI createIMSI(String data) {
		return new IMSIImpl(data);
	}

	public IMEI createIMEI(String imei) {
		return new IMEIImpl(imei);
	}

	public LMSI createLMSI(byte[] data) {
		return new LMSIImpl(data);
	}

	public SM_RP_DA createSM_RP_DA(IMSI imsi) {
		return new SM_RP_DAImpl(imsi);
	}

	public SM_RP_DA createSM_RP_DA(LMSI lmsi) {
		return new SM_RP_DAImpl(lmsi);
	}

	public SM_RP_DA createSM_RP_DA(AddressString serviceCentreAddressDA) {
		return new SM_RP_DAImpl(serviceCentreAddressDA);
	}

	public SM_RP_DA createSM_RP_DA() {
		return new SM_RP_DAImpl();
	}

	public SM_RP_OA createSM_RP_OA_Msisdn(ISDNAddressString msisdn) {
		SM_RP_OAImpl res = new SM_RP_OAImpl();
		res.setMsisdn(msisdn);
		return res;
	}

	public SM_RP_OA createSM_RP_OA_ServiceCentreAddressOA(AddressString serviceCentreAddressOA) {
		SM_RP_OAImpl res = new SM_RP_OAImpl();
		res.setServiceCentreAddressOA(serviceCentreAddressOA);
		return res;
	}
	
	public SM_RP_OA createSM_RP_OA() {
		return new SM_RP_OAImpl();
	}

	public SmsSignalInfo createSmsSignalInfo(byte[] data, Charset gsm8Charset) {
		return new SmsSignalInfoImpl(data, gsm8Charset);
	}

	public SmsSignalInfo createSmsSignalInfo(SmsTpdu data, Charset gsm8Charset) throws MAPException {
		return new SmsSignalInfoImpl(data, gsm8Charset);
	}

	public SM_RP_SMEA createSM_RP_SMEA(byte[] data) {
		return new SM_RP_SMEAImpl(data);
	}

	public MWStatus createMWStatus(boolean scAddressNotIncluded, boolean mnrfSet, boolean mcefSet, boolean mnrgSet) {
		return new MWStatusImpl(scAddressNotIncluded, mnrfSet, mcefSet, mnrgSet);
	}

	public LocationInfoWithLMSI createLocationInfoWithLMSI(ISDNAddressString networkNodeNumber, LMSI lmsi, MAPExtensionContainer extensionContainer,
			AdditionalNumberType additionalNumberType, ISDNAddressString additionalNumber) {
		return new LocationInfoWithLMSIImpl(networkNodeNumber, lmsi, extensionContainer, additionalNumberType, additionalNumber);
	}

	
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

	public CellGlobalIdOrServiceAreaIdOrLAI createCellGlobalIdOrServiceAreaIdOrLAI(CellGlobalIdOrServiceAreaIdFixedLength cellGlobalIdOrServiceAreaIdFixedLength) {
		return new CellGlobalIdOrServiceAreaIdOrLAIImpl(cellGlobalIdOrServiceAreaIdFixedLength);
	}

	public CellGlobalIdOrServiceAreaIdOrLAI createCellGlobalIdOrServiceAreaIdOrLAI(LAIFixedLength laiFixedLength) {
		return new CellGlobalIdOrServiceAreaIdOrLAIImpl(laiFixedLength);
	}

	public CellGlobalIdOrServiceAreaIdFixedLength createCellGlobalIdOrServiceAreaIdFixedLength(byte[] data) {
		return new CellGlobalIdOrServiceAreaIdFixedLengthImpl(data);
	}

	public CellGlobalIdOrServiceAreaIdFixedLength createCellGlobalIdOrServiceAreaIdFixedLength(int mcc, int mnc, int lac, int cellId) throws MAPException {
		return new CellGlobalIdOrServiceAreaIdFixedLengthImpl(mcc, mnc, lac, cellId);
	}

	public LAIFixedLength createLAIFixedLength(byte[] data) {
		return new LAIFixedLengthImpl(data);
	}

	public LAIFixedLength createLAIFixedLength(int mcc, int mnc, int lac) throws MAPException {
		return new LAIFixedLengthImpl(mcc, mnc, lac);
	}

	public CallReferenceNumber createCallReferenceNumber(byte[] data) {
		return new CallReferenceNumberImpl(data);
	}

	public LocationInformation createLocationInformation(Integer ageOfLocationInformation, GeographicalInformation geographicalInformation,
			ISDNAddressString vlrNumber, LocationNumberMap locationNumber, CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI,
			MAPExtensionContainer extensionContainer, LSAIdentity selectedLSAId, ISDNAddressString mscNumber, GeodeticInformation geodeticInformation,
			boolean currentLocationRetrieved, boolean saiPresent, LocationInformationEPS locationInformationEPS, UserCSGInformation userCSGInformation) {
		return new LocationInformationImpl(ageOfLocationInformation, geographicalInformation, vlrNumber, locationNumber, cellGlobalIdOrServiceAreaIdOrLAI,
				extensionContainer, selectedLSAId, mscNumber, geodeticInformation, currentLocationRetrieved, saiPresent, locationInformationEPS,
				userCSGInformation);
	}

	public LocationNumberMap createLocationNumberMap(byte[] data) {
		return new LocationNumberMapImpl(data);
	}

	public LocationNumberMap createLocationNumberMap(LocationNumber locationNumber) throws MAPException {
		return new LocationNumberMapImpl(locationNumber);
	}

	public SubscriberState createSubscriberState(SubscriberStateChoice subscriberStateChoice, NotReachableReason notReachableReason) {
		return new SubscriberStateImpl(subscriberStateChoice, notReachableReason);
	}

	public ExtBasicServiceCode createExtBasicServiceCode(ExtBearerServiceCode extBearerServiceCode) {
		return new ExtBasicServiceCodeImpl(extBearerServiceCode);
	}

	public ExtBasicServiceCode createExtBasicServiceCode(ExtTeleserviceCode extTeleserviceCode) {
		return new ExtBasicServiceCodeImpl(extTeleserviceCode);
	}

	public ExtBearerServiceCode createExtBearerServiceCode(byte[] data) {
		return new ExtBearerServiceCodeImpl(data);
	}

	public ExtTeleserviceCode createExtTeleserviceCode(byte[] data) {
		return new ExtTeleserviceCodeImpl(data);
	}

	public AuthenticationTriplet createAuthenticationTriplet(byte[] rand, byte[] sres, byte[] kc) {
		return new AuthenticationTripletImpl(rand, sres, kc);
	}

	public AuthenticationQuintuplet createAuthenticationQuintuplet(byte[] rand, byte[] xres, byte[] ck, byte[] ik, byte[] autn) {
		return new AuthenticationQuintupletImpl(rand, xres, ck, ik, autn);
	}

	public TripletList createTripletList(ArrayList<AuthenticationTriplet> authenticationTriplets) {
		return new TripletListImpl(authenticationTriplets);
	}

	public QuintupletList createQuintupletList(ArrayList<AuthenticationQuintuplet> quintupletList) {
		return new QuintupletListImpl(quintupletList);
	}

	public AuthenticationSetList createAuthenticationSetList(TripletList tripletList) {
		return new AuthenticationSetListImpl(tripletList);
	}

	public AuthenticationSetList createAuthenticationSetList(QuintupletList quintupletList) {
		return new AuthenticationSetListImpl(quintupletList);
	}

	public PlmnId createPlmnId(byte[] data) {
		return new PlmnIdImpl(data);
	}

	public GSNAddress createGSNAddress(byte[] data) {
		return new GSNAddressImpl(data);
	}

	public ReSynchronisationInfo createReSynchronisationInfo(byte[] rand, byte[] auts) {
		return new ReSynchronisationInfoImpl(rand, auts);
	}

	public EpsAuthenticationSetList createEpsAuthenticationSetList(ArrayList<EpcAv> epcAv) {
		return new EpsAuthenticationSetListImpl(epcAv);
	}

	public EpcAv createEpcAv(byte[] rand, byte[] xres, byte[] autn, byte[] kasme, MAPExtensionContainer extensionContainer) {
		return new EpcAvImpl(rand, xres, autn, kasme, extensionContainer);
	}

	public VLRCapability createVlrCapability(SupportedCamelPhases supportedCamelPhases, MAPExtensionContainer extensionContainer,
			boolean solsaSupportIndicator, ISTSupportIndicator istSupportIndicator, SuperChargerInfo superChargerSupportedInServingNetworkEntity,
			boolean longFtnSupported, SupportedLCSCapabilitySets supportedLCSCapabilitySets, OfferedCamel4CSIs offeredCamel4CSIs,
			SupportedRATTypes supportedRATTypesIndicator, boolean longGroupIDSupported, boolean mtRoamingForwardingSupported) {
		return new VLRCapabilityImpl(supportedCamelPhases, extensionContainer, solsaSupportIndicator, istSupportIndicator,
				superChargerSupportedInServingNetworkEntity, longFtnSupported, supportedLCSCapabilitySets, offeredCamel4CSIs, supportedRATTypesIndicator,
				longGroupIDSupported, mtRoamingForwardingSupported);
	}

	public SupportedCamelPhases createSupportedCamelPhases(boolean phase1, boolean phase2, boolean phase3, boolean phase4) {
		return new SupportedCamelPhasesImpl(phase1, phase2, phase3, phase4);
	}

	public SuperChargerInfo createSuperChargerInfo(Boolean sendSubscriberData) {
		return new SuperChargerInfoImpl(sendSubscriberData);
	}

	public SuperChargerInfo createSuperChargerInfo(byte[] subscriberDataStored) {
		return new SuperChargerInfoImpl(subscriberDataStored);
	}

	public SupportedLCSCapabilitySets createSupportedLCSCapabilitySets(boolean lcsCapabilitySetRelease98_99, boolean lcsCapabilitySetRelease4,
			boolean lcsCapabilitySetRelease5, boolean lcsCapabilitySetRelease6, boolean lcsCapabilitySetRelease7) {
		return new SupportedLCSCapabilitySetsImpl(lcsCapabilitySetRelease98_99, lcsCapabilitySetRelease4, lcsCapabilitySetRelease5, lcsCapabilitySetRelease6,
				lcsCapabilitySetRelease7);
	}

	public OfferedCamel4CSIs createOfferedCamel4CSIs(boolean oCsi, boolean dCsi, boolean vtCsi, boolean tCsi, boolean mtSMSCsi, boolean mgCsi,
			boolean psiEnhancements) {
		return new OfferedCamel4CSIsImpl(oCsi, dCsi, vtCsi, tCsi, mtSMSCsi, mgCsi, psiEnhancements);
	}

	public SupportedRATTypes createSupportedRATTypes(boolean utran, boolean geran, boolean gan, boolean i_hspa_evolution, boolean e_utran) {
		return new SupportedRATTypesImpl(utran, geran, gan, i_hspa_evolution, e_utran);
	}

	public ADDInfo createADDInfo(IMEI imeisv, boolean skipSubscriberDataUpdate) {
		return new ADDInfoImpl(imeisv, skipSubscriberDataUpdate);
	}

	public PagingArea createPagingArea(ArrayList<LocationArea> locationAreas) {
		return new PagingAreaImpl(locationAreas);
	}

	public LAC createLAC(byte[] data) {
		return new LACImpl(data);
	}

	public LAC createLAC(int lac) throws MAPException {
		return new LACImpl(lac);
	}

	public LocationArea createLocationArea(LAIFixedLength laiFixedLength) {
		return new LocationAreaImpl(laiFixedLength);
	}

	public LocationArea createLocationArea(LAC lac) {
		return new LocationAreaImpl(lac);
	}

	public AnyTimeInterrogationRequest createAnyTimeInterrogationRequest(SubscriberIdentity subscriberIdentity, RequestedInfo requestedInfo,
			ISDNAddressString gsmSCFAddress, MAPExtensionContainer extensionContainer) {
		return new AnyTimeInterrogationRequestImpl(subscriberIdentity, requestedInfo, gsmSCFAddress, extensionContainer);
	}

	public AnyTimeInterrogationResponse createAnyTimeInterrogationResponse(SubscriberInfo subscriberInfo, MAPExtensionContainer extensionContainer) {
		return new AnyTimeInterrogationResponseImpl(subscriberInfo, extensionContainer);
	}

	public DiameterIdentity createDiameterIdentity(byte[] data) {
		return new DiameterIdentityImpl(data);
	}

	public SubscriberIdentity createSubscriberIdentity(IMSI imsi) {
		return new SubscriberIdentityImpl(imsi);
	}

	public SubscriberIdentity createSubscriberIdentity(ISDNAddressString msisdn) {
		return new SubscriberIdentityImpl(msisdn);
	}

	public APN createAPN(byte[] data) {
		return new APNImpl(data);
	}

	public PDPAddress createPDPAddress(byte[] data) {
		return new PDPAddressImpl(data);
	}

	public PDPType createPDPType(byte[] data) {
		return new PDPTypeImpl(data);
	}

	public PDPContextInfo createPDPContextInfo(int pdpContextIdentifier, boolean pdpContextActive, PDPType pdpType, PDPAddress pdpAddress, APN apnSubscribed,
			APN apnInUse, Integer asapi, TransactionId transactionId, TEID teidForGnAndGp, TEID teidForIu, GSNAddress ggsnAddress,
			ExtQoSSubscribed qosSubscribed, ExtQoSSubscribed qosRequested, ExtQoSSubscribed qosNegotiated, GPRSChargingID chargingId,
			ChargingCharacteristics chargingCharacteristics, GSNAddress rncAddress, MAPExtensionContainer extensionContainer, Ext2QoSSubscribed qos2Subscribed,
			Ext2QoSSubscribed qos2Requested, Ext2QoSSubscribed qos2Negotiated, Ext3QoSSubscribed qos3Subscribed, Ext3QoSSubscribed qos3Requested,
			Ext3QoSSubscribed qos3Negotiated, Ext4QoSSubscribed qos4Subscribed, Ext4QoSSubscribed qos4Requested, Ext4QoSSubscribed qos4Negotiated,
			ExtPDPType extPdpType, PDPAddress extPdpAddress) {
		return new PDPContextInfoImpl(pdpContextIdentifier, pdpContextActive, pdpType, pdpAddress, apnSubscribed, apnInUse, asapi, transactionId,
				teidForGnAndGp, teidForIu, ggsnAddress, qosSubscribed, qosRequested, qosNegotiated, chargingId, chargingCharacteristics, rncAddress,
				extensionContainer, qos2Subscribed, qos2Requested, qos2Negotiated, qos3Subscribed, qos3Requested, qos3Negotiated, qos4Subscribed,
				qos4Requested, qos4Negotiated, extPdpType, extPdpAddress);
	}

	public CSGId createCSGId(BitSetStrictLength data) {
		return new CSGIdImpl(data);
	}

	public LSAIdentity createLSAIdentity(byte[] data) {
		return new LSAIdentityImpl(data);
	}

	public GPRSChargingID createGPRSChargingID(byte[] data) {
		return new GPRSChargingIDImpl(data);
	}

	public ChargingCharacteristics createChargingCharacteristics(byte[] data) {
		return new ChargingCharacteristicsImpl(data);
	}

	public ExtQoSSubscribed createExtQoSSubscribed(byte[] data) {
		return new ExtQoSSubscribedImpl(data);
	}

	public Ext2QoSSubscribed createExt2QoSSubscribed(byte[] data) {
		return new Ext2QoSSubscribedImpl(data);
	}

	public Ext3QoSSubscribed createExt3QoSSubscribed(byte[] data) {
		return new Ext3QoSSubscribedImpl(data);
	}

	public Ext4QoSSubscribed createExt4QoSSubscribed(int data) {
		return new Ext4QoSSubscribedImpl(data);
	}

	public ExtPDPType createExtPDPType(byte[] data) {
		return new ExtPDPTypeImpl(data);
	}

	public TransactionId createTransactionId(byte[] data) {
		return new TransactionIdImpl(data);
	}

	public TAId createTAId(byte[] data) {
		return new TAIdImpl(data);
	}

	public RAIdentity createRAIdentity(byte[] data) {
		return new RAIdentityImpl(data);
	}

	public EUtranCgi createEUtranCgi(byte[] data) {
		return new EUtranCgiImpl(data);
	}

	public TEID createTEID(byte[] data) {
		return new TEIDImpl(data);
	}

	public GPRSMSClass createGPRSMSClass(MSNetworkCapability mSNetworkCapability, MSRadioAccessCapability mSRadioAccessCapability) {
		return new GPRSMSClassImpl(mSNetworkCapability, mSRadioAccessCapability);
	}

	public GeodeticInformation createGeodeticInformation(byte[] data) {
		return new GeodeticInformationImpl(data);
	}

	public GeographicalInformation createGeographicalInformation(byte[] data) {
		return new GeographicalInformationImpl(data);
	}

	public LocationInformationEPS createLocationInformationEPS(EUtranCgi eUtranCellGlobalIdentity, TAId trackingAreaIdentity,
			MAPExtensionContainer extensionContainer, GeographicalInformation geographicalInformation, GeodeticInformation geodeticInformation,
			boolean currentLocationRetrieved, Integer ageOfLocationInformation, DiameterIdentity mmeName) {
		return new LocationInformationEPSImpl(eUtranCellGlobalIdentity, trackingAreaIdentity, extensionContainer, geographicalInformation, geodeticInformation,
				currentLocationRetrieved, ageOfLocationInformation, mmeName);
	}

	public LocationInformationGPRS createLocationInformationGPRS(CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI,
			RAIdentity routeingAreaIdentity, GeographicalInformation geographicalInformation, ISDNAddressString sgsnNumber, LSAIdentity selectedLSAIdentity,
			MAPExtensionContainer extensionContainer, boolean saiPresent, GeodeticInformation geodeticInformation, boolean currentLocationRetrieved,
			Integer ageOfLocationInformation) {
		return new LocationInformationGPRSImpl(cellGlobalIdOrServiceAreaIdOrLAI, routeingAreaIdentity, geographicalInformation, sgsnNumber,
				selectedLSAIdentity, extensionContainer, saiPresent, geodeticInformation, currentLocationRetrieved, ageOfLocationInformation);
	}

	public MSNetworkCapability createMSNetworkCapability(byte[] data) {
		return new MSNetworkCapabilityImpl(data);
	}

	public MSRadioAccessCapability createMSRadioAccessCapability(byte[] data) {
		return new MSRadioAccessCapabilityImpl(data);
	}

	public MSClassmark2 createMSClassmark2(byte[] data) {
		return new MSClassmark2Impl(data);
	}

	public MNPInfoRes createMNPInfoRes(RouteingNumber routeingNumber, IMSI imsi, ISDNAddressString msisdn, NumberPortabilityStatus numberPortabilityStatus,
			MAPExtensionContainer extensionContainer) {
		return new MNPInfoResImpl(routeingNumber, imsi, msisdn, numberPortabilityStatus, extensionContainer);
	}

	public RequestedInfo createRequestedInfo(boolean locationInformation, boolean subscriberState, MAPExtensionContainer extensionContainer,
			boolean currentLocation, DomainType requestedDomain, boolean imei, boolean msClassmark, boolean mnpRequestedInfo) {
		return new RequestedInfoImpl(locationInformation, subscriberState, extensionContainer, currentLocation, requestedDomain, imei, msClassmark,
				mnpRequestedInfo);
	}

	public RouteingNumber createRouteingNumber(String data) {
		return new RouteingNumberImpl(data);
	}

	public SubscriberInfo createSubscriberInfo(LocationInformation locationInformation, SubscriberState subscriberState,
			MAPExtensionContainer extensionContainer, LocationInformationGPRS locationInformationGPRS, PSSubscriberState psSubscriberState, IMEI imei,
			MSClassmark2 msClassmark2, GPRSMSClass gprsMSClass, MNPInfoRes mnpInfoRes) {
		return new SubscriberInfoImpl(locationInformation, subscriberState, extensionContainer, locationInformationGPRS, psSubscriberState, imei, msClassmark2,
				gprsMSClass, mnpInfoRes);
	}

	public UserCSGInformation createUserCSGInformation(CSGId csgId, MAPExtensionContainer extensionContainer, Integer accessMode, Integer cmi) {
		return new UserCSGInformationImpl(csgId, extensionContainer, accessMode, cmi);
	}

	public PSSubscriberState createPSSubscriberState(PSSubscriberStateChoice choice, NotReachableReason netDetNotReachable,
			ArrayList<PDPContextInfo> pdpContextInfoList) {
		return new PSSubscriberStateImpl(choice, netDetNotReachable, pdpContextInfoList);
	}

	public AddGeographicalInformation createAddGeographicalInformation(byte[] data) {
		return new AddGeographicalInformationImpl(data);
	}

	public AdditionalNumber createAdditionalNumberMscNumber(ISDNAddressString mSCNumber) {
		return new AdditionalNumberImpl(mSCNumber, null);
	}

	public AdditionalNumber createAdditionalNumberSgsnNumber(ISDNAddressString sGSNNumber) {
		return new AdditionalNumberImpl(null, sGSNNumber);
	}

	public AreaDefinition createAreaDefinition(ArrayList<Area> areaList) {
		return new AreaDefinitionImpl(areaList);
	}

	public AreaEventInfo createAreaEventInfo(AreaDefinition areaDefinition, OccurrenceInfo occurrenceInfo, Integer intervalTime) {
		return new AreaEventInfoImpl(areaDefinition, occurrenceInfo, intervalTime);
	}

	public AreaIdentification createAreaIdentification(byte[] data) {
		return new AreaIdentificationImpl(data);
	}

	public AreaIdentification createAreaIdentification(AreaType type, int mcc, int mnc, int lac, int Rac_CellId_UtranCellId) throws MAPException {
		return new AreaIdentificationImpl(type, mcc, mnc, lac, Rac_CellId_UtranCellId);
	}

	public Area createArea(AreaType areaType, AreaIdentification areaIdentification) {
		return new AreaImpl(areaType, areaIdentification);
	}

	public DeferredLocationEventType createDeferredLocationEventType(boolean msAvailable, boolean enteringIntoArea, boolean leavingFromArea,
			boolean beingInsideArea) {
		return new DeferredLocationEventTypeImpl(msAvailable, enteringIntoArea, leavingFromArea, beingInsideArea);
	}

	public DeferredmtlrData createDeferredmtlrData(DeferredLocationEventType deferredLocationEventType, TerminationCause terminationCause,
			LCSLocationInfo lcsLocationInfo) {
		return new DeferredmtlrDataImpl(deferredLocationEventType, terminationCause, lcsLocationInfo);
	}

	public ExtGeographicalInformation createExtGeographicalInformation(byte[] data) {
		return new ExtGeographicalInformationImpl(data);
	}

	public GeranGANSSpositioningData createGeranGANSSpositioningData(byte[] data) {
		return new GeranGANSSpositioningDataImpl(data);
	}

	public LCSClientID createLCSClientID(LCSClientType lcsClientType, LCSClientExternalID lcsClientExternalID, LCSClientInternalID lcsClientInternalID,
			LCSClientName lcsClientName, AddressString lcsClientDialedByMS, APN lcsAPN, LCSRequestorID lcsRequestorID) {
		return new LCSClientIDImpl(lcsClientType, lcsClientExternalID, lcsClientInternalID, lcsClientName, lcsClientDialedByMS, lcsAPN, lcsRequestorID);
	}

	public LCSClientExternalID createLCSClientExternalID(ISDNAddressString externalAddress, MAPExtensionContainer extensionContainer) {
		return new LCSClientExternalIDImpl(externalAddress, extensionContainer);
	}

	public LCSClientName createLCSClientName(byte dataCodingScheme, USSDString nameString, LCSFormatIndicator lcsFormatIndicator) {
		return new LCSClientNameImpl(dataCodingScheme, nameString, lcsFormatIndicator);
	}

	public LCSCodeword createLCSCodeword(byte dataCodingScheme, USSDString lcsCodewordString) {
		return new LCSCodewordImpl(dataCodingScheme, lcsCodewordString);
	}

	public LCSLocationInfo createLCSLocationInfo(ISDNAddressString networkNodeNumber, LMSI lmsi, MAPExtensionContainer extensionContainer,
			boolean gprsNodeIndicator, AdditionalNumber additionalNumber, SupportedLCSCapabilitySets supportedLCSCapabilitySets,
			SupportedLCSCapabilitySets additionalLCSCapabilitySets, DiameterIdentity mmeName, DiameterIdentity aaaServerName) {
		return new LCSLocationInfoImpl(networkNodeNumber, lmsi, extensionContainer, gprsNodeIndicator, additionalNumber, supportedLCSCapabilitySets,
				additionalLCSCapabilitySets, mmeName, aaaServerName);
	}

	public LCSPrivacyCheck createLCSPrivacyCheck(PrivacyCheckRelatedAction callSessionUnrelated, PrivacyCheckRelatedAction callSessionRelated) {
		return new LCSPrivacyCheckImpl(callSessionUnrelated, callSessionRelated);
	}

	public LCSQoS createLCSQoS(Integer horizontalAccuracy, Integer verticalAccuracy, boolean verticalCoordinateRequest, ResponseTime responseTime,
			MAPExtensionContainer extensionContainer) {
		return new LCSQoSImpl(horizontalAccuracy, verticalAccuracy, verticalCoordinateRequest, responseTime, extensionContainer);
	}

	public LCSRequestorID createLCSRequestorID(byte dataCodingScheme, USSDString requestorIDString, LCSFormatIndicator lcsFormatIndicator) {
		return new LCSRequestorIDImpl(dataCodingScheme, requestorIDString, lcsFormatIndicator);
	}

	public LocationType createLocationType(LocationEstimateType locationEstimateType, DeferredLocationEventType deferredLocationEventType) {
		return new LocationTypeImpl(locationEstimateType, deferredLocationEventType);
	}

	public PeriodicLDRInfo createPeriodicLDRInfo(int reportingAmount, int reportingInterval) {
		return new PeriodicLDRInfoImpl(reportingAmount, reportingInterval);
	}

	public PositioningDataInformation createPositioningDataInformation(byte[] data) {
		return new PositioningDataInformationImpl(data);
	}

	public ReportingPLMN createReportingPLMN(PlmnId plmnId, RANTechnology ranTechnology, boolean ranPeriodicLocationSupport) {
		return new ReportingPLMNImpl(plmnId, ranTechnology, ranPeriodicLocationSupport);
	}

	public ReportingPLMNList createReportingPLMNList(boolean plmnListPrioritized, ArrayList<ReportingPLMN> plmnList) {
		return new ReportingPLMNListImpl(plmnListPrioritized, plmnList);
	}

	public ResponseTime createResponseTime(ResponseTimeCategory responseTimeCategory) {
		return new ResponseTimeImpl(responseTimeCategory);
	}

	public ServingNodeAddress createServingNodeAddressMscNumber(ISDNAddressString mscNumber) {
		return new ServingNodeAddressImpl(mscNumber, true);
	}

	public ServingNodeAddress createServingNodeAddressSgsnNumber(ISDNAddressString sgsnNumber) {
		return new ServingNodeAddressImpl(sgsnNumber, false);
	}

	public ServingNodeAddress createServingNodeAddressMmeNumber(DiameterIdentity mmeNumber) {
		return new ServingNodeAddressImpl(mmeNumber);
	}

	public SLRArgExtensionContainer createSLRArgExtensionContainer(ArrayList<MAPPrivateExtension> privateExtensionList, SLRArgPCSExtensions slrArgPcsExtensions) {
		return new SLRArgExtensionContainerImpl(privateExtensionList, slrArgPcsExtensions);
	}

	public SLRArgPCSExtensions createSLRArgPCSExtensions(boolean naEsrkRequest) {
		return new SLRArgPCSExtensionsImpl(naEsrkRequest);
	}

	public SupportedGADShapes createSupportedGADShapes(boolean ellipsoidPoint, boolean ellipsoidPointWithUncertaintyCircle,
			boolean ellipsoidPointWithUncertaintyEllipse, boolean polygon, boolean ellipsoidPointWithAltitude,
			boolean ellipsoidPointWithAltitudeAndUncertaintyElipsoid, boolean ellipsoidArc) {
		return new SupportedGADShapesImpl(ellipsoidPoint, ellipsoidPointWithUncertaintyCircle, ellipsoidPointWithUncertaintyEllipse, polygon,
				ellipsoidPointWithAltitude, ellipsoidPointWithAltitudeAndUncertaintyElipsoid, ellipsoidArc);
	}

	public UtranGANSSpositioningData createUtranGANSSpositioningData(byte[] data) {
		return new UtranGANSSpositioningDataImpl(data);
	}

	public UtranPositioningDataInfo createUtranPositioningDataInfo(byte[] data) {
		return new UtranPositioningDataInfoImpl(data);
	}

	public VelocityEstimate createVelocityEstimate(byte[] data) {
		return new VelocityEstimateImpl(data);
	}

	@Override
	public RequestedEquipmentInfo createRequestedEquipmentInfo(boolean equipmentStatus, boolean bmuef) {
		return new RequestedEquipmentInfoImpl(equipmentStatus, bmuef);
	}

	@Override
	public UESBIIuA createUESBIIuA(BitSetStrictLength data) {
		return new UESBIIuAImpl(data);
	}

	@Override
	public UESBIIuB createUESBIIuB(BitSetStrictLength data) {
		return new UESBIIuBImpl(data);
	}

	@Override
	public UESBIIu createUESBIIu(UESBIIuA uesbiIuA, UESBIIuB uesbiIuB) {
		return new UESBIIuImpl(uesbiIuA, uesbiIuB);
	}

	public IMSIWithLMSI createServingNodeAddressMmeNumber(IMSI imsi, LMSI lmsi) {
		return new IMSIWithLMSIImpl(imsi, lmsi);
	}
}

