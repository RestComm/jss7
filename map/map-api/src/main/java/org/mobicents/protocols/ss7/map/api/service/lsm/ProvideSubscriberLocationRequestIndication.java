/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.map.api.service.lsm;

import java.util.BitSet;

import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.MAPPrimitive;

/**
 * ProvideSubscriberLocation-Arg ::= SEQUENCE {
 *     locationType LocationType,
 *     mlc-Number ISDN-AddressString,
 *     lcs-ClientID [0] LCS-ClientID OPTIONAL,
 *     privacyOverride [1] NULL OPTIONAL,
 *     imsi [2] IMSI OPTIONAL,
 *     msisdn [3] ISDN-AddressString OPTIONAL,
 *     lmsi [4] LMSI OPTIONAL,
 *     imei [5] IMEI OPTIONAL,
 *     lcs-Priority [6] LCS-Priority OPTIONAL,
 *     lcs-QoS [7] LCS-QoS OPTIONAL,
 *     extensionContainer [8] ExtensionContainer OPTIONAL,
 *     ... ,
 *     supportedGADShapes [9] SupportedGADShapes OPTIONAL,
 *     lcs-ReferenceNumber [10] LCS-ReferenceNumber OPTIONAL,
 *     lcsServiceTypeID [11] LCSServiceTypeID OPTIONAL,
 *     lcsCodeword [12] LCSCodeword OPTIONAL,
 *     lcs-PrivacyCheck [13] LCS-PrivacyCheck OPTIONAL,
 *     areaEventInfo [14] AreaEventInfo OPTIONAL,
 *     h-gmlc-Address [15] GSN-Address OPTIONAL }
 *     -- one of imsi or msisdn is mandatory
 *     -- If a location estimate type indicates activate deferred location or cancel deferred
 *     -- location, a lcs-Reference number shall be included
 * 
 * @author amit bhayani
 *
 */
public interface ProvideSubscriberLocationRequestIndication extends LsmMessage {
	
	/**
	 * 
	 * @return
	 */
	public LocationType getLocationType();
	
	public ISDNAddressString getMlcNumber();
	
	public LCSClientID getLCSClientID();
	
	public Boolean getPrivacyOverride();
	
	public IMSI getIMSI();
	
	public ISDNAddressString getMSISDN();
	
	public LMSI getLMSI();
	
	/**
	 * LCS-Priority ::= OCTET STRING (SIZE (1))
     *		-- 0 = highest priority
     *		-- 1 = normal priority
     *		-- all other values treated as 1
	 * @return
	 */
	public Integer getLCSPriority();
	
	public LCSQoS getLCSQoS();	
	
	public IMEI getIMEI();
	
	public MAPExtensionContainer getExtensionContainer();
	
	/**
	 * SupportedGADShapes ::= BIT STRING {
     *      ellipsoidPoint (0),
     *      ellipsoidPointWithUncertaintyCircle (1),
     *      ellipsoidPointWithUncertaintyEllipse (2),
     *      polygon (3),
     *      ellipsoidPointWithAltitude (4),
     *      ellipsoidPointWithAltitudeAndUncertaintyElipsoid (5),
     *      ellipsoidArc (6) } (SIZE (7..16))
     *   -- A node shall mark in the BIT STRING all Shapes defined in 3GPP TS 23.032 it supports.
     *   -- exception handling: bits 7 to 15 shall be ignored if received.
	 * 
	 * @return
	 */
	public BitSet getSupportedGADShapes();
	
	/**
	 * LCS-ReferenceNumber::= OCTET STRING (SIZE(1))
	 * 
	 * @return
	 */
	public Byte getLCSReferenceNumber();
	
	public LCSCodeword getLCSCodeword();

	/**
	 * LCSServiceTypeID ::= INTEGER (0..127)
     *    -- the integer values 0-63 are reserved for Standard LCS service types
     *    -- the integer values 64-127 are reserved for Non Standard LCS service types
     *
	 * @return
	 */
	public Integer getLCSServiceTypeID();
	

	
	public LCSPrivacyCheck getLCSPrivacyCheck();
	
	public AreaEventInfo getAreaEventInfo();
	
	/**
	 * GSN-Address ::= OCTET STRING (SIZE (5..17))
     *     -- Octets are coded according to TS 3GPP TS 23.003 [17]
     *     
	 * @return
	 */
	public byte[] getHGMLCAddress();
}
