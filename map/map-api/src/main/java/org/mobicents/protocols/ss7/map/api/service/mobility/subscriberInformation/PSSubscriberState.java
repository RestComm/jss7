package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation;


/**
 * PS-SubscriberState ::= CHOICE {
 *		notProvidedFromSGSNorMME [0] NULL,
 *		ps-Detached [1] NULL,
 *		ps-AttachedNotReachableForPaging [2] NULL,
 *		ps-AttachedReachableForPaging [3] NULL,
 *		ps-PDP-ActiveNotReachableForPaging [4] PDP-ContextInfoList,
 *		ps-PDP-ActiveReachableForPaging [5] PDP-ContextInfoList,
 *		netDetNotReachable NotReachableReason }
 *
 * @author amit bhayani
 *
 */
public interface PSSubscriberState {
	
	public boolean isNotProvidedFromSGSNorMME();
	
	public boolean isPsDetached();
	
	public boolean isPsAttachedNotReachableForPaging();
	
	public boolean isPsAttachedReachableForPaging();
	
	public boolean isPsPDPActiveNotReachableForPaging();
	
	public boolean isPsPDPActiveReachableForPaging();
	
	public void setNetDetNotReachable(NotReachableReason notReachableReason);
	
	public NotReachableReason getNetDetNotReachable();
	
	//TODO : Implement PDP-ContextInfoList 

}
