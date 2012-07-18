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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation;

import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.APN;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristics;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext2QoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext3QoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext4QoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtPDPType;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPAddress;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPType;

/**
 * 

PDP-ContextInfo ::= SEQUENCE {
	pdp-ContextIdentifier	[0] ContextId,
	pdp-ContextActive	[1] NULL		OPTIONAL,
	pdp-Type		[2] PDP-Type,
	pdp-Address	[3] PDP-Address	OPTIONAL,
	apn-Subscribed	[4] APN		OPTIONAL,
	apn-InUse		[5] APN		OPTIONAL,
	nsapi		[6] NSAPI		OPTIONAL,
	transactionId	[7] TransactionId	OPTIONAL,
	teid-ForGnAndGp	[8] TEID		OPTIONAL,
	teid-ForIu	[9] TEID		OPTIONAL,
	ggsn-Address	[10] GSN-Address 	OPTIONAL,
	qos-Subscribed	[11] Ext-QoS-Subscribed	OPTIONAL,
	qos-Requested	[12] Ext-QoS-Subscribed	OPTIONAL,
	qos-Negotiated	[13] Ext-QoS-Subscribed	OPTIONAL,
	chargingId	[14] GPRSChargingID	OPTIONAL,
	chargingCharacteristics	[15] ChargingCharacteristics	OPTIONAL,
	rnc-Address	[16] GSN-Address	OPTIONAL,
	extensionContainer	[17] ExtensionContainer	OPTIONAL,
	...,
	qos2-Subscribed	[18] Ext2-QoS-Subscribed	OPTIONAL,
	-- qos2-Subscribed may be present only if qos-Subscribed is present.
	qos2-Requested	[19] Ext2-QoS-Subscribed	OPTIONAL,
	-- qos2-Requested may be present only if qos-Requested is present.
	qos2-Negotiated	[20] Ext2-QoS-Subscribed	OPTIONAL,
	-- qos2-Negotiated may be present only if qos-Negotiated is present.
	qos3-Subscribed	[21] Ext3-QoS-Subscribed	OPTIONAL,
	-- qos3-Subscribed may be present only if qos2-Subscribed is present.
	qos3-Requested	[22] Ext3-QoS-Subscribed	OPTIONAL,
	-- qos3-Requested may be present only if qos2-Requested is present.
	qos3-Negotiated	[23] Ext3-QoS-Subscribed	OPTIONAL,
	-- qos3-Negotiated may be present only if qos2-Negotiated is present.
	qos4-Subscribed	[25] Ext4-QoS-Subscribed	OPTIONAL,
	-- qos4-Subscribed may be present only if qos3-Subscribed is present.
	qos4-Requested	[26] Ext4-QoS-Subscribed	OPTIONAL,
	-- qos4-Requested may be present only if qos3-Requested is present.
	qos4-Negotiated	[27] Ext4-QoS-Subscribed	OPTIONAL,
	-- qos4-Negotiated may be present only if qos3-Negotiated is present. 
	ext-pdp-Type	[28] Ext-PDP-Type	OPTIONAL,
	-- contains the value IPv4v6 defined in 3GPP TS 29.060 [105], if the PDP can be
	-- accessed by dual-stack UEs.
	ext-pdp-Address	[29] PDP-Address	OPTIONAL
	-- contains an additional IP address in case of dual-stack static IP address assignment
	-- for the UE.
	-- it may contain an IPv4 or an IPv6 address/prefix, and it may be present
	-- only if pdp-Address is present; if both are present, each parameter shall
	-- contain a different type of address (IPv4 or IPv6).

}

ContextId ::= INTEGER (1..50)

NSAPI ::= INTEGER (0..15)
--	This type is used to indicate the Network layer Service Access Point

 * 
 * @author sergey vetyutnev
 * 
 */
public interface PDPContextInfo {

	public int getPdpContextIdentifier();

	public boolean getPdpContextActive();

	public PDPType getPdpType();

	public PDPAddress getPdpAddress();

	public APN getApnSubscribed();

	public APN getApnInUse();

	public Integer getNsapi();

	public TransactionId getTransactionId();

	public TEID getTeidForGnAndGp();

	public TEID getTeidForIu();

	public GSNAddress getGgsnAddress();

	public ExtQoSSubscribed getQosSubscribed();

	public ExtQoSSubscribed getQosRequested();

	public ExtQoSSubscribed getQosNegotiated();

	public GPRSChargingID getChargingId();

	public ChargingCharacteristics getChargingCharacteristics();

	public GSNAddress getRncAddress();

	public MAPExtensionContainer getExtensionContainer();

	public Ext2QoSSubscribed getQos2Subscribed();

	public Ext2QoSSubscribed getQos2Requested();

	public Ext2QoSSubscribed getQos2Negotiated();

	public Ext3QoSSubscribed getQos3Subscribed();

	public Ext3QoSSubscribed getQos3Requested();

	public Ext3QoSSubscribed getQos3Negotiated();

	public Ext4QoSSubscribed getQos4Subscribed();

	public Ext4QoSSubscribed getQos4Requested();

	public Ext4QoSSubscribed getQos4Negotiated();

	public ExtPDPType getExtPdpType();

	public PDPAddress getExtPdpAddress();

}
