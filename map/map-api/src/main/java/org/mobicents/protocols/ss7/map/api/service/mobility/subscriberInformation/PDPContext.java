package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 * PDP-Context ::= SEQUENCE {
 *		pdp-ContextId 					ContextId,
 *		pdp-Type 						[16] 	PDP-Type,
 *		pdp-Address 					[17] 	PDP-Address OPTIONAL,
 *		qos-Subscribed 					[18] 	QoS-Subscribed,
 *		vplmnAddressAllowed 			[19] 	NULL OPTIONAL,
 *		apn 							[20] 	APN,
 *		extensionContainer 				[21] 	ExtensionContainer OPTIONAL,
 *		... ,
 *		ext-QoS-Subscribed 				[0] 	Ext-QoS-Subscribed OPTIONAL,
 *		pdp-ChargingCharacteristics 	[1] 	ChargingCharacteristics OPTIONAL,
 *		ext2-QoS-Subscribed 			[2] 	Ext2-QoS-Subscribed OPTIONAL,
 *		-- ext2-QoS-Subscribed may be present only if ext-QoS-Subscribed is present.
 *		ext3-QoS-Subscribed 			[3] 	Ext3-QoS-Subscribed OPTIONAL,
 *		-- ext3-QoS-Subscribed may be present only if ext2-QoS-Subscribed is present.
 *		ext4-QoS-Subscribed 			[4] 	Ext4-QoS-Subscribed OPTIONAL,
 *		-- ext4-QoS-Subscribed may be present only if ext3-QoS-Subscribed is present.
 *		apn-oi-Replacement 				[5] 	APN-OI-Replacement OPTIONAL,
 *		-- this apn-oi-Replacement refers to the APN level apn-oi-Replacement and has
 *		-- higher priority than UE level apn-oi-Replacement.
 *		ext-pdp-Type 					[6] 	Ext-PDP-Type OPTIONAL,
 *		-- contains the value IPv4v6 defined in 3GPP TS 29.060 [105], if the PDP can be
 *		-- accessed by dual-stack UEs
 *		ext-pdp-Address 				[7] 	PDP-Address OPTIONAL,
 *		-- contains an additional IP address in case of dual-stack static IP address assignment
 *		-- for the UE.
 *		-- it may contain an IPv4 or an IPv6 address/prefix, and it may be present
 *		-- only if pdp-Address is present; if both are present, each parameter shall
 *		-- contain a different type of address (IPv4 or IPv6).
 *		sipto-Permission 				[8] 	SIPTO-Permission OPTIONAL,
 *		lipa-Permission 				[9] 	LIPA-Permission OPTIONAL
 *	}
 * @author amit bhayani
 *
 */
public interface PDPContext {
	
	/**
	 * ContextId ::= INTEGER (1..maxNumOfPDP-Contexts)
	 * 
	 * maxNumOfPDP-Contexts INTEGER ::= 50
     *
     * 
	 * @return
	 */
	public Integer getPDPContextId();
	
	
	/**
	 * PDP-Type ::= OCTET STRING (SIZE (2))
	 *		-- Octets are coded according to TS 3GPP TS 29.060 [105]
	 *		-- Only the values PPP, IPv4 and IPv6 are allowed for this parameter.
	 *
	 * @return
	 */
	public byte[] getPDPType();
	
	/**
	 * PDP-Address ::= OCTET STRING (SIZE (1..16))
	 *		-- Octets are coded according to TS 3GPP TS 29.060 [105]
	 *
	 *		-- The possible size values
	 *		-- 1-7 octets X.25 address
 	 *		-- 4 octets IPv4 address
 	 *		-- 16 octets Ipv6 address
	 *
	 * @return
	 */
	public byte[] getPDPAddress();
	
	/**
	 * QoS-Subscribed ::= OCTET STRING (SIZE (3))
	 *		-- Octets are coded according to TS 3GPP TS 24.008 [35] Quality of Service Octets
     *		-- 3-5.
	 *
	 * @return
	 */
	public byte[] getQoSSubscribed();
	
	public Boolean isVPLMNAddressAllowed();
	
	/**
	 * APN ::= OCTET STRING (SIZE (2..63))
	 *		-- Octets are coded according to TS 3GPP TS 23.003 [17]
     *
	 * @return
	 */
	public byte[] getAPN();
	
	/**
	 * 
	 * @return
	 */
	public MAPExtensionContainer getExtensionContainer();
	
	/**
	 * Ext-QoS-Subscribed ::= OCTET STRING (SIZE (1..9))
	 *		-- OCTET 1:
     *		-- Allocation/Retention Priority (This octet encodes each priority level defined in
	 *		-- 		23.107 as the binary value of the priority level, declaration in 29.060)
 	 *		-- Octets 2-9 are coded according to 3GPP TS 24.008 [35] Quality of Service Octets
     *		-- 6-13.
	 *
	 * @return
	 */
	public byte[] getExtQoSSubscribed();
	
	/**
	 * ChargingCharacteristics ::= OCTET STRING (SIZE (2))
	 *		-- Octets are coded according to 3GPP TS 32.215.
	 *
	 * @return
	 */
	public byte[] getChargingCharacteristics();
	
	/**
	 * Ext2-QoS-Subscribed ::= OCTET STRING (SIZE (1..3))
	 *		-- Octets 1-3 are coded according to 3GPP TS 24.008 [35] Quality of Service Octets 14-16.
	 *		-- If Quality of Service information is structured with 14 octet length, then
 	 *		-- Octet 1 is coded according to 3GPP TS 24.008 [35] Quality of Service Octet 14.
	 *
	 * @return
	 */
	public byte[] getExt2QoSSubscribed();
	
	/**
	 * Ext3-QoS-Subscribed ::= OCTET STRING (SIZE (1..2))
	 *		-- Octets 1-2 are coded according to 3GPP TS 24.008 [35] Quality of Service Octets 17-18.
	 *
	 * @return
	 */
	public byte[] getExt3QoSSubscribed();
	
	/**
	 * Ext4-QoS-Subscribed ::= OCTET STRING (SIZE (1))
	 *		-- Octet 1:
	 *		-- Evolved Allocation/Retention Priority. This octet encodes the Priority Level (PL),
	 *		-- the Preemption Capability (PCI) and Preemption Vulnerability (PVI) values, as
 	 *		-- described in 3GPP TS 29.060 [105].
	 *
	 * @return
	 */
	public byte[] getExt4QoSSubscribed();
	
	/**
	 * APN-OI-Replacement ::= OCTET STRING (SIZE (9..100))
	 *		-- Octets are coded as APN Operator Identifier according to TS 3GPP TS 23.003 [17]
	 *
	 * @return
	 */
	public byte[] getAPNOIReplacement();
	
	/**
	 * Ext-PDP-Type ::= OCTET STRING (SIZE (2))
	 *	-- Octets are coded, similarly to PDP-Type, according to TS 3GPP TS 29.060 [105].
	 *	-- Only the value IPv4v6 is allowed for this parameter.
 	 *
	 * @return
	 */
	public byte[] getExtPDPType();
	
	/**
	 * PDP-Address ::= OCTET STRING (SIZE (1..16))
	 *		-- Octets are coded according to TS 3GPP TS 29.060 [105]
	 *
	 *		-- The possible size values
	 *		-- 1-7 octets X.25 address
 	 *		-- 4 octets IPv4 address
 	 *		-- 16 octets Ipv6 address
	 *
	 * @return
	 */
	public byte[] getExtPDPAddress();	
	
	public SIPTOPermission getSIPTOPermission();

	public LIPAPermission getLIPAPermission();
}
