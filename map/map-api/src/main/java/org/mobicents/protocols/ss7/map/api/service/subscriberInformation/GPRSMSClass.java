package org.mobicents.protocols.ss7.map.api.service.subscriberInformation;

/**
 * GPRSMSClass ::= SEQUENCE {
 * 	mSNetworkCapability [0] MSNetworkCapability,
 * 	mSRadioAccessCapability [1] MSRadioAccessCapability OPTIONAL
 * }
 * @author amit bhayani
 *
 */
public interface GPRSMSClass {
	public MSNetworkCapability getMSNetworkCapability();
	
	public MSRadioAccessCapability getMSRadioAccessCapability();
}
