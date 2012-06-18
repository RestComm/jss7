package org.mobicents.protocols.ss7.map.api.service.callhandling;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;


/*
 *  ExternalSignalInfo ::= SEQUENCE {
 *  protocolId ProtocolId,
 *  signalInfo SignalInfo,
 *  extensionContainer ExtensionContainer OPTIONAL,
 *  -- extensionContainer must not be used in version 2
 *	...}
 */
 
/*
 * 
 * @author cristian veliscu
 * 
 */
public interface ExternalSignalInfo {
	public byte[] getSignalInfo(); // TODO:
	public ProtocolId getProtocolId();
	public MAPExtensionContainer getExtensionContainer();
}