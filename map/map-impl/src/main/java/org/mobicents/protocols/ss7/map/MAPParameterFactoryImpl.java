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

import org.mobicents.protocols.ss7.isup.message.parameter.LocationNumber;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.primitives.AdditionalNumberType;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;
import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.mobicents.protocols.ss7.map.api.primitives.FTNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LAIFixedLength;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.MAPPrivateExtension;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallReferenceNumber;
import org.mobicents.protocols.ss7.map.api.service.sms.LocationInfoWithLMSI;
import org.mobicents.protocols.ss7.map.api.service.sms.MWStatus;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_SMEA;
import org.mobicents.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.GeodeticInformation;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.GeographicalInformation;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.LSAIdentity;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.LocationInformation;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.LocationInformationEPS;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.LocationNumberMap;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.NotReachableReason;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.SubscriberState;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.SubscriberStateChoice;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.UserCSGInformation;
import org.mobicents.protocols.ss7.map.api.service.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.subscriberManagement.ExtBearerServiceCode;
import org.mobicents.protocols.ss7.map.api.service.subscriberManagement.ExtTeleserviceCode;
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
import org.mobicents.protocols.ss7.map.primitives.FTNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.IMEIImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.LAIFixedLengthImpl;
import org.mobicents.protocols.ss7.map.primitives.LMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPPrivateExtensionImpl;
import org.mobicents.protocols.ss7.map.primitives.USSDStringImpl;
import org.mobicents.protocols.ss7.map.service.callhandling.CallReferenceNumberImpl;
import org.mobicents.protocols.ss7.map.service.sms.LocationInfoWithLMSIImpl;
import org.mobicents.protocols.ss7.map.service.sms.MWStatusImpl;
import org.mobicents.protocols.ss7.map.service.sms.SM_RP_DAImpl;
import org.mobicents.protocols.ss7.map.service.sms.SM_RP_OAImpl;
import org.mobicents.protocols.ss7.map.service.sms.SM_RP_SMEAImpl;
import org.mobicents.protocols.ss7.map.service.sms.SmsSignalInfoImpl;
import org.mobicents.protocols.ss7.map.service.subscriberInformation.LocationInformationImpl;
import org.mobicents.protocols.ss7.map.service.subscriberInformation.LocationNumberMapImpl;
import org.mobicents.protocols.ss7.map.service.subscriberInformation.SubscriberStateImpl;
import org.mobicents.protocols.ss7.map.service.subscriberManagement.ExtBasicServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.subscriberManagement.ExtBearerServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.subscriberManagement.ExtTeleserviceCodeImpl;
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

	public ProcessUnstructuredSSRequest createProcessUnstructuredSSRequestIndication(byte ussdDataCodingSch, USSDString ussdString,
			AlertingPattern alertingPattern, ISDNAddressString msisdnAddressString) {

		ProcessUnstructuredSSRequest request = new ProcessUnstructuredSSRequestImpl(ussdDataCodingSch, ussdString, alertingPattern,
				msisdnAddressString);
		return request;
	}

	public ProcessUnstructuredSSResponse createProcessUnstructuredSSResponseIndication(byte ussdDataCodingScheme, USSDString ussdString) {
		ProcessUnstructuredSSResponse response = new ProcessUnstructuredSSResponseImpl(ussdDataCodingScheme, ussdString);
		return response;
	}

	public UnstructuredSSRequest createUnstructuredSSRequestIndication(byte ussdDataCodingSch, USSDString ussdString,
			AlertingPattern alertingPattern, ISDNAddressString msisdnAddressString) {
		UnstructuredSSRequest request = new UnstructuredSSRequestImpl(ussdDataCodingSch, ussdString, alertingPattern, msisdnAddressString);
		return request;
	}

	public UnstructuredSSResponse createUnstructuredSSRequestIndication(byte ussdDataCodingScheme, USSDString ussdString) {
		UnstructuredSSResponse response = new UnstructuredSSResponseImpl(ussdDataCodingScheme, ussdString);
		return response;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.MAPParameterFactory#createUnstructuredSSNotifyRequestIndication(byte, org.mobicents.protocols.ss7.map.api.primitives.USSDString, org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern, org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString)
	 */
	@Override
	public UnstructuredSSNotifyRequest createUnstructuredSSNotifyRequestIndication(byte ussdDataCodingSch, USSDString ussdString,
			AlertingPattern alertingPattern, ISDNAddressString msisdnAddressString) {
		UnstructuredSSNotifyRequest request = new UnstructuredSSNotifyRequestImpl(ussdDataCodingSch, ussdString, alertingPattern, msisdnAddressString);
		return request;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.MAPParameterFactory#createUnstructuredSSNotifyResponseIndication()
	 */
	@Override
	public UnstructuredSSNotifyResponse createUnstructuredSSNotifyResponseIndication() {
		UnstructuredSSNotifyResponse response = new UnstructuredSSNotifyResponseImpl();
		return response;
	}

	public USSDString createUSSDString(String ussdString, Charset charset) {
		return new USSDStringImpl(ussdString, charset);
	}

	public USSDString createUSSDString(String ussdString) {
		return new USSDStringImpl(ussdString, null);
	}

	public USSDString createUSSDString(byte[] ussdString, Charset charset) {
		return new USSDStringImpl(ussdString, charset);
	}

	public USSDString createUSSDString(byte[] ussdString) {
		return new USSDStringImpl(ussdString, null);
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

	@Override
	public MAPPrivateExtension createMAPPrivateExtension(long[] oId, byte[] data) {
		return new MAPPrivateExtensionImpl(oId, data);
	}

	@Override
	public MAPExtensionContainer createMAPExtensionContainer(ArrayList<MAPPrivateExtension> privateExtensionList,
			byte[] pcsExtensions) {
		return new MAPExtensionContainerImpl(privateExtensionList, pcsExtensions);
	}

	@Override
	public IMSI createIMSI(String data) {
		return new IMSIImpl(data);
	}

	@Override
	public IMEI createIMEI(String imei) {
		return new IMEIImpl(imei);
	}

	@Override
	public LMSI createLMSI(byte[] data) {
		return new LMSIImpl(data);
	}

	@Override
	public SM_RP_DA createSM_RP_DA(IMSI imsi) {
		return new SM_RP_DAImpl(imsi);
	}

	@Override
	public SM_RP_DA createSM_RP_DA(LMSI lmsi) {
		return new SM_RP_DAImpl(lmsi);
	}

	@Override
	public SM_RP_DA createSM_RP_DA(AddressString serviceCentreAddressDA) {
		return new SM_RP_DAImpl(serviceCentreAddressDA);
	}

	@Override
	public SM_RP_DA createSM_RP_DA() {
		return new SM_RP_DAImpl();
	}

	@Override
	public SM_RP_OA createSM_RP_OA_Msisdn(ISDNAddressString msisdn) {
		SM_RP_OAImpl res = new SM_RP_OAImpl();
		res.setMsisdn(msisdn);
		return res;
	}

	@Override
	public SM_RP_OA createSM_RP_OA_ServiceCentreAddressOA(AddressString serviceCentreAddressOA) {
		SM_RP_OAImpl res = new SM_RP_OAImpl();
		res.setServiceCentreAddressOA(serviceCentreAddressOA);
		return res;
	}
	
	@Override
	public SM_RP_OA createSM_RP_OA() {
		return new SM_RP_OAImpl();
	}

	@Override
	public SmsSignalInfo createSmsSignalInfo(byte[] data, Charset gsm8Charset) {
		return new SmsSignalInfoImpl(data, gsm8Charset);
	}

	@Override
	public SmsSignalInfo createSmsSignalInfo(SmsTpdu data, Charset gsm8Charset) throws MAPException {
		return new SmsSignalInfoImpl(data, gsm8Charset);
	}

	@Override
	public SM_RP_SMEA createSM_RP_SMEA(byte[] data) {
		return new SM_RP_SMEAImpl(data);
	}

	@Override
	public MWStatus createMWStatus(boolean scAddressNotIncluded, boolean mnrfSet, boolean mcefSet, boolean mnrgSet) {
		return new MWStatusImpl(scAddressNotIncluded, mnrfSet, mcefSet, mnrgSet);
	}

	@Override
	public LocationInfoWithLMSI createLocationInfoWithLMSI(ISDNAddressString networkNodeNumber, LMSI lmsi, MAPExtensionContainer extensionContainer,
			AdditionalNumberType additionalNumberType, ISDNAddressString additionalNumber) {
		return new LocationInfoWithLMSIImpl(networkNodeNumber, lmsi, extensionContainer, additionalNumberType, additionalNumber);
	}

	
	@Override
	public Problem createProblemGeneral(GeneralProblemType prob) {
		Problem pb = TcapFactory.createProblem(ProblemType.General);
		pb.setGeneralProblemType(prob);
		return pb;
	}

	@Override
	public Problem createProblemInvoke(InvokeProblemType prob) {
		Problem pb = TcapFactory.createProblem(ProblemType.Invoke);
		pb.setInvokeProblemType(prob);
		return pb;
	}

	@Override
	public Problem createProblemResult(ReturnResultProblemType prob) {
		Problem pb = TcapFactory.createProblem(ProblemType.ReturnResult);
		pb.setReturnResultProblemType(prob);
		return pb;
	}

	@Override
	public Problem createProblemError(ReturnErrorProblemType prob) {
		Problem pb = TcapFactory.createProblem(ProblemType.ReturnError);
		pb.setReturnErrorProblemType(prob);
		return pb;
	}

	@Override
	public CellGlobalIdOrServiceAreaIdOrLAI createCellGlobalIdOrServiceAreaIdOrLAI(CellGlobalIdOrServiceAreaIdFixedLength cellGlobalIdOrServiceAreaIdFixedLength) {
		return new CellGlobalIdOrServiceAreaIdOrLAIImpl(cellGlobalIdOrServiceAreaIdFixedLength);
	}

	@Override
	public CellGlobalIdOrServiceAreaIdOrLAI createCellGlobalIdOrServiceAreaIdOrLAI(LAIFixedLength laiFixedLength) {
		return new CellGlobalIdOrServiceAreaIdOrLAIImpl(laiFixedLength);
	}

	@Override
	public CellGlobalIdOrServiceAreaIdFixedLength createCellGlobalIdOrServiceAreaIdFixedLength(byte[] data) {
		return new CellGlobalIdOrServiceAreaIdFixedLengthImpl(data);
	}

	@Override
	public CellGlobalIdOrServiceAreaIdFixedLength createCellGlobalIdOrServiceAreaIdFixedLength(int mcc, int mnc, int lac, int cellId) throws MAPException {
		return new CellGlobalIdOrServiceAreaIdFixedLengthImpl(mcc, mnc, lac, cellId);
	}

	@Override
	public LAIFixedLength createLAIFixedLength(byte[] data) {
		return new LAIFixedLengthImpl(data);
	}

	@Override
	public LAIFixedLength createLAIFixedLength(int mcc, int mnc, int lac) throws MAPException {
		return new LAIFixedLengthImpl(mcc, mnc, lac);
	}

	@Override
	public CallReferenceNumber createCallReferenceNumber(byte[] data) {
		return new CallReferenceNumberImpl(data);
	}

	@Override
	public LocationInformation createLocationInformation(Integer ageOfLocationInformation, GeographicalInformation geographicalInformation,
			ISDNAddressString vlrNumber, LocationNumberMap locationNumber, CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI,
			MAPExtensionContainer extensionContainer, LSAIdentity selectedLSAId, ISDNAddressString mscNumber, GeodeticInformation geodeticInformation,
			boolean currentLocationRetrieved, boolean saiPresent, LocationInformationEPS locationInformationEPS, UserCSGInformation userCSGInformation) {
		return new LocationInformationImpl(ageOfLocationInformation, geographicalInformation, vlrNumber, locationNumber, cellGlobalIdOrServiceAreaIdOrLAI,
				extensionContainer, selectedLSAId, mscNumber, geodeticInformation, currentLocationRetrieved, saiPresent, locationInformationEPS,
				userCSGInformation);
	}

	@Override
	public LocationNumberMap createLocationNumberMap(byte[] data) {
		return new LocationNumberMapImpl(data);
	}

	@Override
	public LocationNumberMap createLocationNumberMap(LocationNumber locationNumber) throws MAPException {
		return new LocationNumberMapImpl(locationNumber);
	}

	@Override
	public SubscriberState createSubscriberState(SubscriberStateChoice subscriberStateChoice, NotReachableReason notReachableReason) {
		return new SubscriberStateImpl(subscriberStateChoice, notReachableReason);
	}

	@Override
	public ExtBasicServiceCode createExtBasicServiceCode(ExtBearerServiceCode extBearerServiceCode) {
		return new ExtBasicServiceCodeImpl(extBearerServiceCode);
	}

	@Override
	public ExtBasicServiceCode createExtBasicServiceCode(ExtTeleserviceCode extTeleserviceCode) {
		return new ExtBasicServiceCodeImpl(extTeleserviceCode);
	}

	@Override
	public ExtBearerServiceCode createExtBearerServiceCode(byte[] data) {
		return new ExtBearerServiceCodeImpl(data);
	}

	@Override
	public ExtTeleserviceCode createExtTeleserviceCode(byte[] data) {
		return new ExtTeleserviceCodeImpl(data);
	}
}
