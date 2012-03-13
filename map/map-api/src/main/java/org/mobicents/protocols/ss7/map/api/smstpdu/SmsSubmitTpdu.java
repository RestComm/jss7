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

package org.mobicents.protocols.ss7.map.api.smstpdu;

/**
 * SMS-SUBMIT pdu
 * 
 * @author sergey vetyutnev
 * 
 */
public interface SmsSubmitTpdu extends SmsTpdu {

	/**
	 * Mandatory. Parameter indicating whether or not the SC shall accept an
	 * SMS-SUBMIT for an SM still held in the SC, which has the same TP-MR and
	 * the same TP-DA as a previously submitted SM from the same OA.
	 * 
	 * @return TP-RD field
	 */
	public boolean getRejectDuplicates();

	/**
	 * Mandatory. Parameter indicating whether or not the TP-VP field is
	 * present.
	 * 
	 * @return TP-VPF field
	 */
	public ValidityPeriodFormat getValidityPeriodFormat();

	/**
	 * Mandatory. Parameter indicating the request for a Reply-Path.
	 * 
	 * @return TP-RP field
	 */
	public boolean getReplyPathExists();

	/**
	 * Optional. Parameter indicating that the TP-UD field contains a header.
	 * 
	 * @return TP-UDHI field
	 */
	public boolean getUserDataHeaderIndicator();

	/**
	 * Optional. Parameter indicating if the MS is requesting a status report.
	 * 
	 * @return TP-SRR field
	 */
	public boolean getStatusReportRequest();

	/**
	 * Mandatory. Parameter identifying the SMS-SUBMIT
	 * 
	 * @return TP-MR field
	 */
	public int getMessageReference();

	/**
	 * Mandatory. Address of the destination SME
	 * 
	 * @return TP-DA field
	 */
	public AddressField getDestinationAddress();

	/**
	 * Mandatory. Parameter identifying the top layer protocol, if any.
	 * 
	 * @return TP-PID field
	 */
	public ProtocolIdentifier getProtocolIdentifier();

	/**
	 * Mandatory. Parameter identifying the coding scheme within the
	 * TP–User–Data.
	 * 
	 * @return TP-DCS field
	 */
	public DataCodingScheme getDataCodingScheme();

	/**
	 * Optional. Parameter identifying the time from where the message is no
	 * longer valid.
	 * 
	 * @return TP-VP field
	 */
	public ValidityPeriod getValidityPeriod();

	/**
	 * Mandatory. Parameter indicating the length of the TP-User-Data field to
	 * follow.
	 * 
	 * @return TP-UDL field
	 */
	public int getUserDataLength();

	/**
	 * Optional. Actual Data
	 * 
	 * @return TP-UD field
	 */
	public UserData getUserData();

}
