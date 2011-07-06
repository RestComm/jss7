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

import org.mobicents.protocols.ss7.map.api.MapServiceFactory;
import org.mobicents.protocols.ss7.map.api.dialog.AddressNature;
import org.mobicents.protocols.ss7.map.api.dialog.AddressString;
import org.mobicents.protocols.ss7.map.api.dialog.IMSI;
import org.mobicents.protocols.ss7.map.api.dialog.LMSI;
import org.mobicents.protocols.ss7.map.api.dialog.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.dialog.MAPPrivateExtension;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.dialog.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.dialog.AddressStringImpl;
import org.mobicents.protocols.ss7.map.dialog.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.dialog.MAPPrivateExtensionImpl;
import org.mobicents.protocols.ss7.map.dialog.MAPUserAbortChoiceImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.ProcessUnstructuredSSRequestImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.ProcessUnstructuredSSResponseImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.USSDStringImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.UnstructuredSSRequestImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.UnstructuredSSResponseImpl;

/**
 * 
 * @author amit bhayani
 * 
 */
public class MapServiceFactoryImpl implements MapServiceFactory {

	public ProcessUnstructuredSSRequest createProcessUnstructuredSSRequest(byte ussdDataCodingScheme,
			USSDString ussdString) {

		ProcessUnstructuredSSRequest request = new ProcessUnstructuredSSRequestImpl(ussdDataCodingScheme, ussdString);
		return request;
	}

	public ProcessUnstructuredSSResponse createProcessUnstructuredSsRequestResponse(int invokeID,
			byte ussdDataCodingScheme, USSDString ussdString) {
		ProcessUnstructuredSSResponse response = new ProcessUnstructuredSSResponseImpl(ussdDataCodingScheme, ussdString);
		return response;
	}

	public UnstructuredSSRequest createUnstructuredSSRequest(byte ussdDataCodingScheme, USSDString ussdString) {
		UnstructuredSSRequest request = new UnstructuredSSRequestImpl(ussdDataCodingScheme, ussdString);
		return request;
	}

	public UnstructuredSSResponse createUnstructuredSsRequestResponse(int invokeID, byte ussdDataCodingScheme,
			USSDString ussdString) {
		UnstructuredSSResponse response = new UnstructuredSSResponseImpl(ussdDataCodingScheme, ussdString);
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
	public IMSI createIMSI(Long MCC, Long MNC, String MSIN) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LMSI createLMSI(byte[] data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SM_RP_DA createSM_RP_DA(IMSI imsi) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SM_RP_DA createSM_RP_DA(LMSI lmsi) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SM_RP_DA createSM_RP_DA(AddressString serviceCentreAddressDA) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SM_RP_DA createSM_RP_DA() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SM_RP_OA createSM_RP_OA_Msisdn(AddressString msisdn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SM_RP_OA createSM_RP_OA_ServiceCentreAddressOA(AddressString serviceCentreAddressOA) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SM_RP_OA createSM_RP_OA() {
		// TODO Auto-generated method stub
		return null;
	}

}
