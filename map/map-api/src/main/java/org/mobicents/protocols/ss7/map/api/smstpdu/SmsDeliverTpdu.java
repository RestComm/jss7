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
 * SMS-DELIVER pdu
 * 
 * @author sergey vetyutnev
 * 
 */
public interface SmsDeliverTpdu extends SmsTpdu {

	/**
	 * @return TP-MMS field
	 */
	public boolean getMoreMessagesToSend();

	/**
	 * @return TP-LP field
	 * false: The message has not been forwarded and is not a spawned message (or the
	 * sending network entity (e.g. an SC) does not support the setting of this bit.)
	 * true:  The message has either been forwarded or is a spawned message.
	 */
	public boolean getForwardedOrSpawned();
	
	/**
	 * @return TP-RP field
	 */
	public boolean getReplyPathExists();

	/**
	 * @return TP-UDHI field
	 */
	public boolean getUserDataHeaderIndicator();

	/**
	 * @return TP-SRI field
	 */
	public boolean getStatusReportIndication();
	
	/**
	 * @return TP-OA field
	 */
	public AddressField getOriginatingAddress();

	/**
	 * @return TP-PID field
	 */
	public ProtocolIdentifier getProtocolIdentifier();
	
	/**
	 * @return TP-DCS field
	 */
	public DataCodingScheme getDataCodingScheme();

	/**
	 * @return TP-SCTS field
	 */
	public AbsoluteTimeStamp getServiceCentreTimeStamp();

	/**
	 * @return TP-UDL field
	 */
	public int getUserDataLength();
	
	/**
	 * @return TP-UD field
	 */
	public UserData getUserData();

}
