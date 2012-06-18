package org.mobicents.protocols.ss7.map.api.service.callhandling;

import java.io.Serializable;


/*
 * ExtendedRoutingInfo ::= CHOICE {
 * routingInfo RoutingInfo,
 * camelRoutingInfo [8] CamelRoutingInfo}
 */
 
/*
 * 
 * @author cristian veliscu
 * 
 */
public interface ExtendedRoutingInfo extends Serializable {
	public RoutingInfo getRoutingInfo();
	public byte[] getCamelRoutingInfo(); // TODO: 
}