package org.mobicents.protocols.ss7.map.api.service.subscriberInformation;

import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 * MNPInfoRes ::= SEQUENCE {
 *		routeingNumber 			[0] RouteingNumber OPTIONAL,
 *		imsi 					[1] IMSI OPTIONAL,
 *		msisdn 					[2] ISDN-AddressString OPTIONAL,
 *		numberPortabilityStatus [3] NumberPortabilityStatus OPTIONAL,
 *		extensionContainer 		[4] ExtensionContainer OPTIONAL,
 *	... }
 *	-- The IMSI parameter contains a generic IMSI, i.e. it is not tied necessarily to the
 *	-- Subscriber. MCC and MNC values in this IMSI shall point to the Subscription Network of
 *	-- the Subscriber. See 3GPP TS 23.066 [108].
 * 
 * @author amit bhayani
 *
 */
public interface MNPInfoRes {
	
	public RouteingNumber getRouteingNumber();
	
	public IMSI getIMSI();
	
	public ISDNAddressString getMSISDN();
	
	public NumberPortabilityStatus getNumberPortabilityStatus();
	
	public MAPExtensionContainer getExtensionContainer();
	

}
