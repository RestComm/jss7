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

import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;

/**
 * 
 * @author amit bhayani
 * @author sergey vetyutnev
 * 
 */
public interface MAPDialogSupplementary extends MAPDialog {

	/**
	 * Add's a new Process Unstructured SS Request as Component.
	 * 
	 * @param ussdDataCodingScheme
	 *            The Data Coding Scheme for this USSD String as defined in GSM
	 *            03.38
	 * @param ussdString
	 *            Ussd String
	 * @param alertingPatter
	 *            The optional alerting pattern. See {@link AlertingPattern}
	 * @param msisdn
	 *            The optional MSISDN in {@link AddressString} format.
	 * @return invokeId
	 * @throws MAPException
	 */
	public Long addProcessUnstructuredSSRequest(byte ussdDataCodingScheme, USSDString ussdString, AlertingPattern alertingPatter, AddressString msisdn)
			throws MAPException;

	/**
	 * Add's a new ProcessUnstructured SS Response as Component.
	 * 
	 * @param invokeId
	 *            The original invoke ID retrieved from
	 *            {@link ProcessUnstructuredSSResponseIndication}
	 * @param ussdDataCodingScheme
	 *            The Data Coding Scheme for this USSD String as defined in GSM
	 *            03.38
	 * @param ussdString
	 *            Ussd String {@link USSDString}
	 * @throws MAPException
	 */
	public void addProcessUnstructuredSSResponse(long invokeId, byte ussdDataCodingScheme, USSDString ussdString) throws MAPException;

	/**
	 * Add's a new Unstructured SS Request
	 * 
	 * @param ussdDataCodingScheme
	 *            The Data Coding Scheme for this USSD String as defined in GSM
	 *            03.38
	 * @param ussdString
	 *            Ussd String {@link USSDString}
	 * @param alertingPatter
	 *            The optional alerting pattern. See {@link AlertingPattern}
	 * @param msisdn
	 *            The optional MSISDN in {@link AddressString} format.
	 * @return invokeId
	 * @throws MAPException
	 */
	public Long addUnstructuredSSRequest(byte ussdDataCodingScheme, USSDString ussdString, AlertingPattern alertingPatter, AddressString msisdn)
			throws MAPException;

	/**
	 * Add's a new Unstructured SS Response
	 * 
	 * @param invokeId
	 *            The original invoke ID retrieved from
	 *            {@link UnstructuredSSResponseIndication}
	 * @param ussdDataCodingScheme
	 *            The Data Coding Scheme for this USSD String as defined in GSM
	 *            03.38
	 * @param ussdString
	 *            Ussd String {@link USSDString}
	 * @throws MAPException
	 */
	public void addUnstructuredSSResponse(long invokeId, byte ussdDataCodingScheme, USSDString ussdString) throws MAPException;

}
