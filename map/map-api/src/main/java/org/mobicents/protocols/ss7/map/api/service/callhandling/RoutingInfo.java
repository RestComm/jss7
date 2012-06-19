package org.mobicents.protocols.ss7.map.api.service.callhandling;

import java.io.Serializable;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;

/*
 * RoutingInfo ::= CHOICE {
 * roamingNumber ISDN-AddressString,
 * forwardingData ForwardingData}
 */
 
/*
 * 
 * @author cristian veliscu
 * 
 */
public interface RoutingInfo extends Serializable {
	public ISDNAddressString getRoamingNumber();
	public ForwardingData getForwardingData();
}