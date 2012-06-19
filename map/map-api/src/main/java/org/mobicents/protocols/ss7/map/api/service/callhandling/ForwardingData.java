package org.mobicents.protocols.ss7.map.api.service.callhandling;

import org.mobicents.protocols.ss7.map.api.primitives.FTNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;


/*
 *	ForwardingData ::= SEQUENCE {
 *  forwardedToNumber [5] ISDN-AddressString OPTIONAL,
 *  forwardedToSubaddress [4] ISDN-SubaddressString OPTIONAL,
 *  forwardingOptions [6] ForwardingOptions OPTIONAL,
 *  extensionContainer [7] ExtensionContainer OPTIONAL,
 *  ...,
 *  longForwardedToNumber [8] FTN-AddressString OPTIONAL}
 */
 
/*
 * 
 * @author cristian veliscu
 * 
 */
public interface ForwardingData {	
	public ISDNAddressString getForwardedToNumber();
	public byte[] getForwardedToSubaddress(); // TODO: ISDNSubaddressString
	public ForwardingOptions getForwardingOptions();
	public MAPExtensionContainer getExtensionContainer();
	public FTNAddressString getLongForwardedToNumber();
}