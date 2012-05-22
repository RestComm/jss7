package org.mobicents.protocols.ss7.map.api.service.subscriberInformation;

/**
 * MSNetworkCapability ::= OCTET STRING (SIZE (1..8))
 *		-- This parameter carries the value part of the MS Network Capability IE defined in
 *		-- 3GPP TS 24.008 [35].
 * @author amit bhayani
 *
 */
public interface MSNetworkCapability {
	public byte[] getData();
}
