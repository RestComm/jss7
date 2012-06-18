package org.mobicents.protocols.ss7.map.api.service.callhandling;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;


/*
 *  Ext-ExternalSignalInfo ::= SEQUENCE {
 *  protocolId Ext-ProtocolId,
 *  signalInfo SignalInfo,
 *  extensionContainer ExtensionContainer OPTIONAL,
 *	...}
 */
 
/*
 * 
 * @author cristian veliscu
 * 
 */
public interface ExtExternalSignalInfo {
	public byte[] getSignalInfo(); // TODO: 
	public ExtProtocolId getExtProtocolId();
	public MAPExtensionContainer getExtensionContainer();
}