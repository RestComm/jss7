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

package org.mobicents.protocols.ss7.map.api.service.supplementary;

import org.mobicents.protocols.ss7.map.api.MAPMessage;

/**
 * 
 * @author amit bhayani
 * 
 */
public interface USSDService extends MAPMessage {

	public MAPDialogSupplementary getMAPDialog();

	/**
	 * This parameter contains the information of the alphabet and the language
	 * used for the unstructured information in an Unstructured Supplementary
	 * Service Data operation. The coding of this parameter is according to the
	 * Cell Broadcast Data Coding Scheme as specified in GSM 03.38.
	 * 
	 * @return
	 */
	public byte getUSSDDataCodingScheme();

	/**
	 * 
	 * @param ussdDataCodingSch
	 */
	public void setUSSDDataCodingScheme(byte ussdDataCodingSch);

	/**
	 * <p>
	 * This parameter contains a string of unstructured information in an
	 * Unstructured Supplementary Service Data operation. The string is sent
	 * either by the mobile user or the network. The contents of a string sent
	 * by the MS are interpreted by the network as specified in GSM 02.90.
	 * </p>
	 * <br/>
	 * <p>
	 * USSD String is OCTET STRING (SIZE (1..160))
	 * </p>
	 * 
	 * <br/>
	 * 
	 * <p>
	 * The structure of the contents of the USSD-String is dependent -- on the
	 * USSD-DataCodingScheme as described in TS GSM 03.38.
	 * </p>
	 * 
	 * 
	 * 
	 * @return
	 */
	public USSDString getUSSDString();

	/**
	 * 
	 * @param ussdString
	 */
	public void setUSSDString(USSDString ussdString);

}
