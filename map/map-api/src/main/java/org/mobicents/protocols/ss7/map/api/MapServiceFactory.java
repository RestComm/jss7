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

import org.mobicents.protocols.ss7.map.api.dialog.AddressNature;
import org.mobicents.protocols.ss7.map.api.dialog.AddressString;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.dialog.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.dialog.MAPPrivateExtension;
import org.mobicents.protocols.ss7.map.api.dialog.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponse;

/**
 * 
 * @author amit bhayani
 * 
 */
public interface MapServiceFactory {

	public ProcessUnstructuredSSRequest createProcessUnstructuredSSRequest(byte ussdDataCodingScheme,
			USSDString ussdString);

	public ProcessUnstructuredSSResponse createProcessUnstructuredSsRequestResponse(int invokeID,
			byte ussdDataCodingScheme, USSDString ussdString);

	public UnstructuredSSRequest createUnstructuredSSRequest(byte ussdDataCodingScheme, USSDString ussdString);

	public UnstructuredSSResponse createUnstructuredSsRequestResponse(int invokeID, byte ussdDataCodingScheme,
			USSDString ussdString);

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

	/**
	 * Creates a new instance of {@link MAPUserAbortChoice}
	 * 
	 * @return
	 */
	public MAPUserAbortChoice createMAPUserAbortChoice();

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
	public MAPExtensionContainer createMAPExtensionContainer(ArrayList<MAPPrivateExtension> privateExtensionList,
			byte[] pcsExtensions);
}
