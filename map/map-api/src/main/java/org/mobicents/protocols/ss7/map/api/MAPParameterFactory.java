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

package org.mobicents.protocols.ss7.map.api;

import java.nio.charset.Charset;
import java.util.ArrayList;

import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.ss7.isup.message.parameter.LocationNumber;
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

	public ProcessUnstructuredSSRequest createProcessUnstructuredSSRequestIndication(byte ussdDataCodingSch, USSDString ussdString,
			AlertingPattern alertingPattern, ISDNAddressString msisdnAddressString);

	public ProcessUnstructuredSSResponse createProcessUnstructuredSSResponseIndication(byte ussdDataCodingScheme, USSDString ussdString);

	public UnstructuredSSRequest createUnstructuredSSRequestIndication(byte ussdDataCodingSch, USSDString ussdString,
			AlertingPattern alertingPattern, ISDNAddressString msisdnAddressString);

	public UnstructuredSSResponse createUnstructuredSSRequestIndication(byte ussdDataCodingScheme, USSDString ussdString);
	
	public UnstructuredSSNotifyRequest createUnstructuredSSNotifyRequestIndication(byte ussdDataCodingSch, USSDString ussdString,
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
	public USSDString createUSSDString(String ussdString);

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
	public USSDString createUSSDString(String ussdString, Charset charSet);

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
	public USSDString createUSSDString(byte[] ussdString, Charset charSet);

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
	public GeodeticInformation createGeodeticInformation(byte[] data);
	public GeographicalInformation createGeographicalInformation(byte[] data);
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
	public GeranGANSSpositioningData createGeranGANSSpositioningData(byte[] data);
	public LCSClientID createLCSClientID(LCSClientType lcsClientType, LCSClientExternalID lcsClientExternalID, LCSClientInternalID lcsClientInternalID,
			LCSClientName lcsClientName, AddressString lcsClientDialedByMS, APN lcsAPN, LCSRequestorID lcsRequestorID);
	public LCSClientExternalID createLCSClientExternalID(final ISDNAddressString externalAddress, final MAPExtensionContainer extensionContainer);
	public LCSClientName createLCSClientName(byte dataCodingScheme, USSDString nameString, LCSFormatIndicator lcsFormatIndicator);
	public LCSCodeword createLCSCodeword(byte dataCodingScheme, USSDString lcsCodewordString);
	public LCSLocationInfo createLCSLocationInfo(ISDNAddressString networkNodeNumber, LMSI lmsi, MAPExtensionContainer extensionContainer,
			boolean gprsNodeIndicator, AdditionalNumber additionalNumber, SupportedLCSCapabilitySets supportedLCSCapabilitySets,
			SupportedLCSCapabilitySets additionalLCSCapabilitySets, DiameterIdentity mmeName, DiameterIdentity aaaServerName);
	public LCSPrivacyCheck createLCSPrivacyCheck(PrivacyCheckRelatedAction callSessionUnrelated, PrivacyCheckRelatedAction callSessionRelated);
	public LCSQoS createLCSQoS(Integer horizontalAccuracy, Integer verticalAccuracy, boolean verticalCoordinateRequest, ResponseTime responseTime,
			MAPExtensionContainer extensionContainer);
	public LCSRequestorID createLCSRequestorID(byte dataCodingScheme, USSDString requestorIDString, LCSFormatIndicator lcsFormatIndicator);
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

	public ExtBasicServiceCode createExtBasicServiceCode(ExtBearerServiceCode extBearerServiceCode);
	public ExtBasicServiceCode createExtBasicServiceCode(ExtTeleserviceCode extTeleserviceCode);
	public ExtBearerServiceCode createExtBearerServiceCode(byte[] data);
	public ExtTeleserviceCode createExtTeleserviceCode(byte[] data);

	public Problem createProblemGeneral(GeneralProblemType prob);
	public Problem createProblemInvoke(InvokeProblemType prob);
	public Problem createProblemResult(ReturnResultProblemType prob);
	public Problem createProblemError(ReturnErrorProblemType prob);
	
	public RequestedEquipmentInfo createRequestedEquipmentInfo(boolean equipmentStatus, boolean bmuef);
	public UESBIIuA createUESBIIuA(BitSetStrictLength data);
	public UESBIIuB createUESBIIuB(BitSetStrictLength data);
	public UESBIIu createUESBIIu(UESBIIuA uesbiIuA, UESBIIuB uesbiIuB);
}
