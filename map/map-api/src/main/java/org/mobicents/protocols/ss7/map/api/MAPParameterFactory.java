/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012. 
 * and individual contributors
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

package org.mobicents.protocols.ss7.map.api;

import java.nio.charset.Charset;
import java.util.ArrayList;

import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.ss7.isup.message.parameter.LocationNumber;
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
import org.mobicents.protocols.ss7.map.api.service.callhandling.CUGCheckInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallReferenceNumber;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CamelInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CamelRoutingInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ForwardingData;
import org.mobicents.protocols.ss7.map.api.service.callhandling.GmscCamelSubscriptionInfo;
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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LIPAPermission;
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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.PDPContext;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.PDPContextInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.PSSubscriberState;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.PSSubscriberStateChoice;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RAIdentity;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RouteingNumber;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SIPTOPermission;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberState;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberStateChoice;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.TAId;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.TEID;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.TransactionId;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.UserCSGInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.APN;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.APNOIReplacement;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGId;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInterlock;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristics;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultCallHandling;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext2QoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext3QoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext4QoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtPDPType;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAIdentity;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTDPData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTdpCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmTriggerDetectionPoint;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPAddress;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPType;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTDPData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTdpCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmTriggerDetectionPoint;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.sms.LocationInfoWithLMSI;
import org.mobicents.protocols.ss7.map.api.service.sms.MWStatus;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_SMEA;
import org.mobicents.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSStatus;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.api.smstpdu.AddressField;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsTpdu;
import org.mobicents.protocols.ss7.tcap.asn.comp.GeneralProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.InvokeProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnErrorProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultProblemType;

/**
 * 
 * @author amit bhayani
 * @author sergey vetyutnev
 * 
 */
public interface MAPParameterFactory {

	public ProcessUnstructuredSSRequest createProcessUnstructuredSSRequestIndication(CBSDataCodingScheme ussdDataCodingSch, USSDString ussdString,
			AlertingPattern alertingPattern, ISDNAddressString msisdnAddressString);

	public ProcessUnstructuredSSResponse createProcessUnstructuredSSResponseIndication(CBSDataCodingScheme ussdDataCodingScheme, USSDString ussdString);

	public UnstructuredSSRequest createUnstructuredSSRequestIndication(CBSDataCodingScheme ussdDataCodingSch, USSDString ussdString,
			AlertingPattern alertingPattern, ISDNAddressString msisdnAddressString);

	public UnstructuredSSResponse createUnstructuredSSRequestIndication(CBSDataCodingScheme ussdDataCodingScheme, USSDString ussdString);
	
	public UnstructuredSSNotifyRequest createUnstructuredSSNotifyRequestIndication(CBSDataCodingScheme ussdDataCodingSch, USSDString ussdString,
			AlertingPattern alertingPattern, ISDNAddressString msisdnAddressString);

	public UnstructuredSSNotifyResponse createUnstructuredSSNotifyResponseIndication();

	/**
	 * Creates a new instance of {@link USSDString}. The passed USSD String is
	 * encoded by using the default Charset defined in GSM 03.38 Specs
	 * 
	 * @param ussdString
	 *            The USSD String
	 * @return new instance of {@link USSDString}
	 */
	public USSDString createUSSDString(String ussdString) throws MAPException;

	/**
	 * Creates a new instance of {@link USSDString} using the passed
	 * {@link java.nio.charset.Charset} for encoding the passed ussdString
	 * String
	 * 
	 * @param ussdString
	 *            The USSD String
	 * @param charSet
	 *            The Charset used for encoding the passed USSD String
	 * @return new instance of {@link USSDString}
	 */
	public USSDString createUSSDString(String ussdString, CBSDataCodingScheme dataCodingScheme, Charset gsm8Charset) throws MAPException;

	/**
	 * Creates a new instance of {@link USSDString}. The passed USSD String
	 * byte[] is encoded by using the default Charset defined in GSM 03.38 Specs
	 * 
	 * @param ussdString
	 *            The USSD String
	 * @return new instance of {@link USSDString}
	 */
	public USSDString createUSSDString(byte[] ussdString);

	/**
	 * Creates a new instance of {@link USSDString} using the passed
	 * {@link java.nio.charset.Charset} for encoding the passed ussdString
	 * byte[]
	 * 
	 * @param ussdString
	 *            The byte[] of the USSD String
	 * @param charSet
	 *            The Charset used for encoding the passed USSD String byte[]
	 * @return new instance of {@link USSDString}
	 */
	public USSDString createUSSDString(byte[] ussdString, CBSDataCodingScheme dataCodingScheme, Charset gsm8Charset);

	/**
	 * Creates a new instance of {@link AddressString}
	 * 
	 * @param addNature
	 *            The nature of this AddressString. See {@link AddressNature}.
	 * @param numPlan
	 *            The {@link NumberingPlan} of this AddressString
	 * @param address
	 *            The actual address (number)
	 * @return new instance of {@link AddressString}
	 */
	public AddressString createAddressString(AddressNature addNature, NumberingPlan numPlan, String address);

	public ISDNAddressString createISDNAddressString(AddressNature addNature, NumberingPlan numPlan, String address);

	public FTNAddressString createFTNAddressString(AddressNature addNature, NumberingPlan numPlan, String address);

	/**
	 * Creates a new instance of {@link IMSI}
	 * 
	 * @param data
	 *            whole data string 
	 * @return new instance of {@link IMSI}
	 */
	public IMSI createIMSI(String data);

	public IMEI createIMEI(String imei);

	/**
	 * Creates a new instance of {@link LMSI}
	 * 
	 * @param data
	 * 
	 * @return new instance of {@link LMSI}
	 */
	public LMSI createLMSI(byte[] data);

	/**
	 * Creates a new instance of {@link SM_RP_DA} with imsi parameter
	 * 
	 * @param imsi
	 * @return
	 */
	public SM_RP_DA createSM_RP_DA(IMSI imsi);

	/**
	 * Creates a new instance of {@link SM_RP_DA} with lmsi parameter
	 * 
	 * @param lmsi
	 * @return
	 */
	public SM_RP_DA createSM_RP_DA(LMSI lmsi);

	/**
	 * Creates a new instance of {@link SM_RP_DA} with serviceCentreAddressDA
	 * parameter
	 * 
	 * @param serviceCentreAddressDA
	 * @return
	 */
	public SM_RP_DA createSM_RP_DA(AddressString serviceCentreAddressDA);

	/**
	 * Creates a new instance of {@link SM_RP_DA} with noSM_RP_DA parameter
	 * 
	 * @return
	 */
	public SM_RP_DA createSM_RP_DA();

	/**
	 * Creates a new instance of {@link SM_RP_OA} with msisdn parameter
	 * 
	 * @param msisdn
	 * @return
	 */
	public SM_RP_OA createSM_RP_OA_Msisdn(ISDNAddressString msisdn);

	/**
	 * Creates a new instance of {@link SM_RP_OA} with serviceCentreAddressOA
	 * parameter
	 * 
	 * @param serviceCentreAddressOA
	 * @return
	 */
	public SM_RP_OA createSM_RP_OA_ServiceCentreAddressOA(AddressString serviceCentreAddressOA);

	/**
	 * Creates a new instance of {@link SM_RP_OA} with noSM_RP_OA parameter
	 * 
	 * @return
	 */
	public SM_RP_OA createSM_RP_OA();

	public SmsSignalInfo createSmsSignalInfo(byte[] data, Charset gsm8Charset);

	public SmsSignalInfo createSmsSignalInfo(SmsTpdu data, Charset gsm8Charset) throws MAPException;

	public SM_RP_SMEA createSM_RP_SMEA(byte[] data);
	public SM_RP_SMEA createSM_RP_SMEA(AddressField addressField) throws MAPException;
	
	/**
	 * Creates a new instance of {@link MAPUserAbortChoice}
	 * 
	 * @return
	 */
	public MAPUserAbortChoice createMAPUserAbortChoice();

	public MWStatus createMWStatus(boolean scAddressNotIncluded, boolean mnrfSet, boolean mcefSet, boolean mnrgSet);

	public LocationInfoWithLMSI createLocationInfoWithLMSI(ISDNAddressString networkNodeNumber, LMSI lmsi, MAPExtensionContainer extensionContainer,
			AdditionalNumberType additionalNumberType, ISDNAddressString additionalNumber);

	/**
	 * Creates a new instance of {@link MAPPrivateExtension} for
	 * {@link MAPExtensionContainer}
	 * 
	 * @param oId
	 *            PrivateExtension ObjectIdentifier
	 * @param data
	 *            PrivateExtension data (ASN.1 encoded byte array with tag
	 *            bytes)
	 * @return
	 */
	public MAPPrivateExtension createMAPPrivateExtension(long[] oId, byte[] data);

	/**
	 * @param privateExtensionList
	 *            List of PrivateExtensions
	 * @param pcsExtensions
	 *            pcsExtensions value (ASN.1 encoded byte array without tag
	 *            byte)
	 * @return
	 */
	public MAPExtensionContainer createMAPExtensionContainer(ArrayList<MAPPrivateExtension> privateExtensionList, byte[] pcsExtensions);
	
	public CellGlobalIdOrServiceAreaIdOrLAI createCellGlobalIdOrServiceAreaIdOrLAI(CellGlobalIdOrServiceAreaIdFixedLength cellGlobalIdOrServiceAreaIdFixedLength);

	public CellGlobalIdOrServiceAreaIdOrLAI createCellGlobalIdOrServiceAreaIdOrLAI(LAIFixedLength laiFixedLength);
	
	public CellGlobalIdOrServiceAreaIdFixedLength createCellGlobalIdOrServiceAreaIdFixedLength(byte[] data);

	public CellGlobalIdOrServiceAreaIdFixedLength createCellGlobalIdOrServiceAreaIdFixedLength(int mcc, int mnc, int lac, int cellId) throws MAPException;

	public LAIFixedLength createLAIFixedLength(byte[] data);
	public LAIFixedLength createLAIFixedLength(int mcc, int mnc, int lac) throws MAPException;

	public CallReferenceNumber createCallReferenceNumber(byte[] data);

	public LocationInformation createLocationInformation(Integer ageOfLocationInformation, GeographicalInformation geographicalInformation,
			ISDNAddressString vlrNumber, LocationNumberMap locationNumber, CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI,
			MAPExtensionContainer extensionContainer, LSAIdentity selectedLSAId, ISDNAddressString mscNumber, GeodeticInformation geodeticInformation,
			boolean currentLocationRetrieved, boolean saiPresent, LocationInformationEPS locationInformationEPS, UserCSGInformation userCSGInformation);
	public LocationNumberMap createLocationNumberMap(byte[] data);
	public LocationNumberMap createLocationNumberMap(LocationNumber locationNumber) throws MAPException;
	public SubscriberState createSubscriberState(SubscriberStateChoice subscriberStateChoice, NotReachableReason notReachableReason);

	public PlmnId createPlmnId(byte[] data);
	public PlmnId createPlmnId(int mcc, int mnc);
	public GSNAddress createGSNAddress(byte[] data);

	public AuthenticationTriplet createAuthenticationTriplet(byte[] rand, byte[] sres, byte[] kc);
	public AuthenticationQuintuplet createAuthenticationQuintuplet(byte[] rand, byte[] xres, byte[] ck, byte[] ik, byte[] autn);
	public TripletList createTripletList(ArrayList<AuthenticationTriplet> authenticationTriplets);
	public QuintupletList createQuintupletList(ArrayList<AuthenticationQuintuplet> quintupletList);
	public AuthenticationSetList createAuthenticationSetList(TripletList tripletList);
	public AuthenticationSetList createAuthenticationSetList(QuintupletList quintupletList);
	public ReSynchronisationInfo createReSynchronisationInfo(byte[] rand, byte[] auts);
	public EpsAuthenticationSetList createEpsAuthenticationSetList(ArrayList<EpcAv> epcAv);
	public EpcAv createEpcAv(byte[] rand, byte[] xres, byte[] autn, byte[] kasme, MAPExtensionContainer extensionContainer);

	public VLRCapability createVlrCapability(SupportedCamelPhases supportedCamelPhases, MAPExtensionContainer extensionContainer,
			boolean solsaSupportIndicator, ISTSupportIndicator istSupportIndicator, SuperChargerInfo superChargerSupportedInServingNetworkEntity,
			boolean longFtnSupported, SupportedLCSCapabilitySets supportedLCSCapabilitySets, OfferedCamel4CSIs offeredCamel4CSIs,
			SupportedRATTypes supportedRATTypesIndicator, boolean longGroupIDSupported, boolean mtRoamingForwardingSupported);
	public SupportedCamelPhases createSupportedCamelPhases(boolean phase1, boolean phase2, boolean phase3, boolean phase4);
	public SuperChargerInfo createSuperChargerInfo(Boolean sendSubscriberData);
	public SuperChargerInfo createSuperChargerInfo(byte[] subscriberDataStored);
	public SupportedLCSCapabilitySets createSupportedLCSCapabilitySets(boolean lcsCapabilitySetRelease98_99, boolean lcsCapabilitySetRelease4,
			boolean lcsCapabilitySetRelease5, boolean lcsCapabilitySetRelease6, boolean lcsCapabilitySetRelease7);
	public OfferedCamel4CSIs createOfferedCamel4CSIs(boolean oCsi, boolean dCsi, boolean vtCsi, boolean tCsi, boolean mtSMSCsi, boolean mgCsi,
			boolean psiEnhancements);
	public SupportedRATTypes createSupportedRATTypes(boolean utran, boolean geran, boolean gan, boolean i_hspa_evolution, boolean e_utran);
	public ADDInfo createADDInfo(IMEI imeisv, boolean skipSubscriberDataUpdate);
	public PagingArea createPagingArea(ArrayList<LocationArea> locationAreas);
	public LAC createLAC(byte[] data);
	public LAC createLAC(int lac) throws MAPException;
	public LocationArea createLocationArea(LAIFixedLength laiFixedLength);
	public LocationArea createLocationArea(LAC lac);

	
	public AnyTimeInterrogationRequest createAnyTimeInterrogationRequest(SubscriberIdentity subscriberIdentity, RequestedInfo requestedInfo,
			ISDNAddressString gsmSCFAddress, MAPExtensionContainer extensionContainer);
	public AnyTimeInterrogationResponse createAnyTimeInterrogationResponse(SubscriberInfo subscriberInfo, MAPExtensionContainer extensionContainer);
	public DiameterIdentity createDiameterIdentity(byte[] data);
	public SubscriberIdentity createSubscriberIdentity(IMSI imsi);
	public SubscriberIdentity createSubscriberIdentity(ISDNAddressString msisdn);
	public APN createAPN(byte[] data);
	public PDPAddress createPDPAddress(byte[] data);
	public PDPType createPDPType(byte[] data);
	public PDPContextInfo createPDPContextInfo(int pdpContextIdentifier, boolean pdpContextActive, PDPType pdpType, PDPAddress pdpAddress, APN apnSubscribed,
			APN apnInUse, Integer asapi, TransactionId transactionId, TEID teidForGnAndGp, TEID teidForIu, GSNAddress ggsnAddress,
			ExtQoSSubscribed qosSubscribed, ExtQoSSubscribed qosRequested, ExtQoSSubscribed qosNegotiated, GPRSChargingID chargingId,
			ChargingCharacteristics chargingCharacteristics, GSNAddress rncAddress, MAPExtensionContainer extensionContainer, Ext2QoSSubscribed qos2Subscribed,
			Ext2QoSSubscribed qos2Requested, Ext2QoSSubscribed qos2Negotiated, Ext3QoSSubscribed qos3Subscribed, Ext3QoSSubscribed qos3Requested,
			Ext3QoSSubscribed qos3Negotiated, Ext4QoSSubscribed qos4Subscribed, Ext4QoSSubscribed qos4Requested, Ext4QoSSubscribed qos4Negotiated,
			ExtPDPType extPdpType, PDPAddress extPdpAddress);
	public PDPContext createPDPContext(int pdpContextId, PDPType pdpType, PDPAddress pdpAddress, QoSSubscribed qosSubscribed, boolean vplmnAddressAllowed,
			APN apn, MAPExtensionContainer extensionContainer, ExtQoSSubscribed extQoSSubscribed, ChargingCharacteristics chargingCharacteristics,
			Ext2QoSSubscribed ext2QoSSubscribed, Ext3QoSSubscribed ext3QoSSubscribed, Ext4QoSSubscribed ext4QoSSubscribed, APNOIReplacement apnoiReplacement,
			ExtPDPType extpdpType, PDPAddress extpdpAddress, SIPTOPermission sipToPermission, LIPAPermission lipaPermission);
	public APNOIReplacement createAPNOIReplacement(byte[] data);
	public QoSSubscribed createQoSSubscribed(byte[] data);
	public CSGId createCSGId(BitSetStrictLength data);
	public LSAIdentity createLSAIdentity(byte[] data);
	public GPRSChargingID createGPRSChargingID(byte[] data);
	public ChargingCharacteristics createChargingCharacteristics(byte[] data);
	public ExtQoSSubscribed createExtQoSSubscribed(byte[] data);
	public Ext2QoSSubscribed createExt2QoSSubscribed(byte[] data);
	public Ext3QoSSubscribed createExt3QoSSubscribed(byte[] data);
	public Ext4QoSSubscribed createExt4QoSSubscribed(int data);
	public ExtPDPType createExtPDPType(byte[] data);
	public TransactionId createTransactionId(byte[] data);
	public TAId createTAId(byte[] data);
	public RAIdentity createRAIdentity(byte[] data);
	public EUtranCgi createEUtranCgi(byte[] data);
	public TEID createTEID(byte[] data);
	public GPRSMSClass createGPRSMSClass(MSNetworkCapability mSNetworkCapability, MSRadioAccessCapability mSRadioAccessCapability);
	public GeographicalInformation createGeographicalInformation(byte[] data);
	public GeographicalInformation createGeographicalInformation(double latitude, double longitude, double uncertainty) throws MAPException;
	public GeodeticInformation createGeodeticInformation(byte[] data);
	public GeodeticInformation createGeodeticInformation(int screeningAndPresentationIndicators, double latitude, double longitude, double uncertainty,
			int confidence) throws MAPException;
	public LocationInformationEPS createLocationInformationEPS(EUtranCgi eUtranCellGlobalIdentity, TAId trackingAreaIdentity,
			MAPExtensionContainer extensionContainer, GeographicalInformation geographicalInformation, GeodeticInformation geodeticInformation,
			boolean currentLocationRetrieved, Integer ageOfLocationInformation, DiameterIdentity mmeName);
	public LocationInformationGPRS createLocationInformationGPRS(CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI,
			RAIdentity routeingAreaIdentity, GeographicalInformation geographicalInformation, ISDNAddressString sgsnNumber, LSAIdentity selectedLSAIdentity,
			MAPExtensionContainer extensionContainer, boolean saiPresent, GeodeticInformation geodeticInformation, boolean currentLocationRetrieved,
			Integer ageOfLocationInformation);
	public MSNetworkCapability createMSNetworkCapability(byte[] data);
	public MSRadioAccessCapability createMSRadioAccessCapability(byte[] data);
	public MSClassmark2 createMSClassmark2(byte[] data);
	public MNPInfoRes createMNPInfoRes(RouteingNumber routeingNumber, IMSI imsi, ISDNAddressString msisdn, NumberPortabilityStatus numberPortabilityStatus,
			MAPExtensionContainer extensionContainer);
	public RequestedInfo createRequestedInfo(boolean locationInformation, boolean subscriberState, MAPExtensionContainer extensionContainer,
			boolean currentLocation, DomainType requestedDomain, boolean imei, boolean msClassmark, boolean mnpRequestedInfo);
	public RouteingNumber createRouteingNumber(String data);
	public SubscriberInfo createSubscriberInfo(LocationInformation locationInformation, SubscriberState subscriberState,
			MAPExtensionContainer extensionContainer, LocationInformationGPRS locationInformationGPRS, PSSubscriberState psSubscriberState, IMEI imei,
			MSClassmark2 msClassmark2, GPRSMSClass gprsMSClass, MNPInfoRes mnpInfoRes);
	public UserCSGInformation createUserCSGInformation(CSGId csgId, MAPExtensionContainer extensionContainer, Integer accessMode, Integer cmi);
	public PSSubscriberState createPSSubscriberState(PSSubscriberStateChoice choice, NotReachableReason netDetNotReachable,
			ArrayList<PDPContextInfo> pdpContextInfoList);	

	public AddGeographicalInformation createAddGeographicalInformation(byte[] data);
	public AddGeographicalInformation createAddGeographicalInformation_EllipsoidPointWithUncertaintyCircle(double latitude, double longitude, double uncertainty)
			throws MAPException;
	public AddGeographicalInformation createAddGeographicalInformation_EllipsoidPointWithUncertaintyEllipse(double latitude, double longitude,
			double uncertaintySemiMajorAxis, double uncertaintySemiMinorAxis, double angleOfMajorAxis, int confidence) throws MAPException;
	public AddGeographicalInformation createAddGeographicalInformation_EllipsoidPointWithAltitudeAndUncertaintyEllipsoid(double latitude, double longitude,
			double uncertaintySemiMajorAxis, double uncertaintySemiMinorAxis, double angleOfMajorAxis, int confidence, int altitude, double uncertaintyAltitude)
			throws MAPException;
	public AddGeographicalInformation createAddGeographicalInformation_EllipsoidArc(double latitude, double longitude, int innerRadius,
			double uncertaintyRadius, double offsetAngle, double includedAngle, int confidence) throws MAPException;
	public AddGeographicalInformation createAddGeographicalInformation_EllipsoidPoint(double latitude, double longitude) throws MAPException;

	public AdditionalNumber createAdditionalNumberMscNumber(ISDNAddressString mSCNumber);
	public AdditionalNumber createAdditionalNumberSgsnNumber(ISDNAddressString sGSNNumber);
	public AreaDefinition createAreaDefinition(ArrayList<Area> areaList);
	public AreaEventInfo createAreaEventInfo(AreaDefinition areaDefinition, OccurrenceInfo occurrenceInfo, Integer intervalTime);
	public AreaIdentification createAreaIdentification(byte[] data);
	public AreaIdentification createAreaIdentification(AreaType type, int mcc, int mnc, int lac, int Rac_CellId_UtranCellId) throws MAPException ;
	public Area createArea(AreaType areaType, AreaIdentification areaIdentification);
	public DeferredLocationEventType createDeferredLocationEventType(boolean msAvailable, boolean enteringIntoArea, boolean leavingFromArea,
			boolean beingInsideArea);
	public DeferredmtlrData createDeferredmtlrData(DeferredLocationEventType deferredLocationEventType, TerminationCause terminationCause,
			LCSLocationInfo lcsLocationInfo);
	
	public ExtGeographicalInformation createExtGeographicalInformation(byte[] data);
	public ExtGeographicalInformation createExtGeographicalInformation_EllipsoidPointWithUncertaintyCircle(double latitude, double longitude, double uncertainty)
			throws MAPException;
	public ExtGeographicalInformation createExtGeographicalInformation_EllipsoidPointWithUncertaintyEllipse(double latitude, double longitude,
			double uncertaintySemiMajorAxis, double uncertaintySemiMinorAxis, double angleOfMajorAxis, int confidence) throws MAPException;
	public ExtGeographicalInformation createExtGeographicalInformation_EllipsoidPointWithAltitudeAndUncertaintyEllipsoid(double latitude, double longitude,
			double uncertaintySemiMajorAxis, double uncertaintySemiMinorAxis, double angleOfMajorAxis, int confidence, int altitude, double uncertaintyAltitude)
			throws MAPException;
	public ExtGeographicalInformation createExtGeographicalInformation_EllipsoidArc(double latitude, double longitude, int innerRadius,
			double uncertaintyRadius, double offsetAngle, double includedAngle, int confidence) throws MAPException;
	public ExtGeographicalInformation createExtGeographicalInformation_EllipsoidPoint(double latitude, double longitude) throws MAPException;

	public GeranGANSSpositioningData createGeranGANSSpositioningData(byte[] data);
	public LCSClientID createLCSClientID(LCSClientType lcsClientType, LCSClientExternalID lcsClientExternalID, LCSClientInternalID lcsClientInternalID,
			LCSClientName lcsClientName, AddressString lcsClientDialedByMS, APN lcsAPN, LCSRequestorID lcsRequestorID);
	public LCSClientExternalID createLCSClientExternalID(final ISDNAddressString externalAddress, final MAPExtensionContainer extensionContainer);
	public LCSClientName createLCSClientName(CBSDataCodingScheme dataCodingScheme, USSDString nameString, LCSFormatIndicator lcsFormatIndicator);
	public LCSCodeword createLCSCodeword(CBSDataCodingScheme dataCodingScheme, USSDString lcsCodewordString);
	public LCSLocationInfo createLCSLocationInfo(ISDNAddressString networkNodeNumber, LMSI lmsi, MAPExtensionContainer extensionContainer,
			boolean gprsNodeIndicator, AdditionalNumber additionalNumber, SupportedLCSCapabilitySets supportedLCSCapabilitySets,
			SupportedLCSCapabilitySets additionalLCSCapabilitySets, DiameterIdentity mmeName, DiameterIdentity aaaServerName);
	public LCSPrivacyCheck createLCSPrivacyCheck(PrivacyCheckRelatedAction callSessionUnrelated, PrivacyCheckRelatedAction callSessionRelated);
	public LCSQoS createLCSQoS(Integer horizontalAccuracy, Integer verticalAccuracy, boolean verticalCoordinateRequest, ResponseTime responseTime,
			MAPExtensionContainer extensionContainer);
	public LCSRequestorID createLCSRequestorID(CBSDataCodingScheme dataCodingScheme, USSDString requestorIDString, LCSFormatIndicator lcsFormatIndicator);
	public LocationType createLocationType(final LocationEstimateType locationEstimateType, final DeferredLocationEventType deferredLocationEventType);
	public PeriodicLDRInfo createPeriodicLDRInfo(int reportingAmount, int reportingInterval);
	public PositioningDataInformation createPositioningDataInformation(byte[] data);
	public ReportingPLMN createReportingPLMN(PlmnId plmnId, RANTechnology ranTechnology, boolean ranPeriodicLocationSupport);
	public ReportingPLMNList createReportingPLMNList(boolean plmnListPrioritized, ArrayList<ReportingPLMN> plmnList);
	public ResponseTime createResponseTime(ResponseTimeCategory responseTimeCategory);
	public ServingNodeAddress createServingNodeAddressMscNumber(ISDNAddressString mscNumber);
	public ServingNodeAddress createServingNodeAddressSgsnNumber(ISDNAddressString sgsnNumber);
	public ServingNodeAddress createServingNodeAddressMmeNumber(DiameterIdentity mmeNumber);
	public SLRArgExtensionContainer createSLRArgExtensionContainer(ArrayList<MAPPrivateExtension> privateExtensionList, SLRArgPCSExtensions slrArgPcsExtensions);
	public SLRArgPCSExtensions createSLRArgPCSExtensions(boolean naEsrkRequest);
	public SupportedGADShapes createSupportedGADShapes(boolean ellipsoidPoint, boolean ellipsoidPointWithUncertaintyCircle,
			boolean ellipsoidPointWithUncertaintyEllipse, boolean polygon, boolean ellipsoidPointWithAltitude,
			boolean ellipsoidPointWithAltitudeAndUncertaintyElipsoid, boolean ellipsoidArc);
	public UtranGANSSpositioningData createUtranGANSSpositioningData(byte[] data);
	public UtranPositioningDataInfo createUtranPositioningDataInfo(byte[] data);

	public VelocityEstimate createVelocityEstimate(byte[] data);
	public VelocityEstimate createVelocityEstimate_HorizontalVelocity(int horizontalSpeed, int bearing) throws MAPException;
	public VelocityEstimate createVelocityEstimate_HorizontalWithVerticalVelocity(int horizontalSpeed, int bearing, int verticalSpeed) throws MAPException;
	public VelocityEstimate createVelocityEstimate_HorizontalVelocityWithUncertainty(int horizontalSpeed, int bearing, int uncertaintyHorizontalSpeed)
			throws MAPException;
	public VelocityEstimate createVelocityEstimate_HorizontalWithVerticalVelocityAndUncertainty(int horizontalSpeed, int bearing, int verticalSpeed,
			int uncertaintyHorizontalSpeed, int uncertaintyVerticalSpeed) throws MAPException;

	public ExtBasicServiceCode createExtBasicServiceCode(ExtBearerServiceCode extBearerServiceCode);
	public ExtBasicServiceCode createExtBasicServiceCode(ExtTeleserviceCode extTeleserviceCode);
	public ExtBearerServiceCode createExtBearerServiceCode(byte[] data);
	public ExtBearerServiceCode createExtBearerServiceCode(BearerServiceCodeValue value);
	public BearerServiceCode createBearerServiceCode(int data);
	public BearerServiceCode createBearerServiceCode(BearerServiceCodeValue value);
	public ExtTeleserviceCode createExtTeleserviceCode(byte[] data);
	public ExtTeleserviceCode createExtTeleserviceCode(TeleserviceCodeValue value);
	public TeleserviceCode createTeleserviceCode(int data);
	public TeleserviceCode createTeleserviceCode(TeleserviceCodeValue value);

	public CamelRoutingInfo createCamelRoutingInfo(ForwardingData forwardingData, GmscCamelSubscriptionInfo gmscCamelSubscriptionInfo,
			MAPExtensionContainer extensionContainer);
	public GmscCamelSubscriptionInfo createGmscCamelSubscriptionInfo(TCSI tCsi, OCSI oCsi, MAPExtensionContainer extensionContainer,
			ArrayList<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList, ArrayList<TBcsmCamelTdpCriteria> tBcsmCamelTdpCriteriaList, DCSI dCsi);
	public TCSI createTCSI(ArrayList<TBcsmCamelTDPData> tBcsmCamelTDPDataList, MAPExtensionContainer extensionContainer, Integer camelCapabilityHandling,
			boolean notificationToCSE, boolean csiActive);
	public OCSI createOCSI(ArrayList<OBcsmCamelTDPData> oBcsmCamelTDPDataList, MAPExtensionContainer extensionContainer, Integer camelCapabilityHandling,
			boolean notificationToCSE, boolean csiActive);
	public TBcsmCamelTDPData createTBcsmCamelTDPData(TBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint, long serviceKey, ISDNAddressString gsmSCFAddress,
			DefaultCallHandling defaultCallHandling, MAPExtensionContainer extensionContainer);
	public OBcsmCamelTDPData createOBcsmCamelTDPData(OBcsmTriggerDetectionPoint oBcsmTriggerDetectionPoint, long serviceKey, ISDNAddressString gsmSCFAddress,
			DefaultCallHandling defaultCallHandling, MAPExtensionContainer extensionContainer);

	public CamelInfo createCamelInfo(SupportedCamelPhases supportedCamelPhases, boolean suppressTCSI, MAPExtensionContainer extensionContainer,
			OfferedCamel4CSIs offeredCamel4CSIs);
	public CUGInterlock createCUGInterlock(byte[] data);
	public CUGCheckInfo createCUGCheckInfo(CUGInterlock cugInterlock, boolean cugOutgoingAccess, MAPExtensionContainer extensionContainer);

	public SSCode createSSCode(SupplementaryCodeValue value);
	public SSCode createSSCode(int data);
	public SSStatus createSSStatus(boolean qBit, boolean pBit, boolean rBit, boolean aBit);
	public BasicServiceCode createBasicServiceCode(TeleserviceCode teleservice);
	public BasicServiceCode createBasicServiceCode(BearerServiceCode bearerService);
	
	public Problem createProblemGeneral(GeneralProblemType prob);
	public Problem createProblemInvoke(InvokeProblemType prob);
	public Problem createProblemResult(ReturnResultProblemType prob);
	public Problem createProblemError(ReturnErrorProblemType prob);
	
	public RequestedEquipmentInfo createRequestedEquipmentInfo(boolean equipmentStatus, boolean bmuef);
	public UESBIIuA createUESBIIuA(BitSetStrictLength data);
	public UESBIIuB createUESBIIuB(BitSetStrictLength data);
	public UESBIIu createUESBIIu(UESBIIuA uesbiIuA, UESBIIuB uesbiIuB);
}
