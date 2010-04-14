package org.mobicents.protocols.ss7.map.api;

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

public interface MAPProvider {
	
	public static final int NETWORK_UNSTRUCTURED_SS_CONTEXT_V2 = 1;

	/**
	 * Creates a new Dialog. This is equivalent to issuing MAP_OPEN Service
	 * Request to MAP Provider.
	 * 
	 * @param applicationCntx
	 *            This parameter identifies the type of application context
	 *            being established. If the dialogue is accepted the received
	 *            application context name shall be echoed. In case of refusal
	 *            of dialogue this parameter shall indicate the highest version
	 *            supported.
	 * 
	 * @param destAddress
	 *            A valid SCCP address identifying the destination peer entity.
	 *            As an implementation option, this parameter may also, in the
	 *            indication, be implicitly associated with the service access
	 *            point at which the primitive is issued.
	 * 
	 * @param destReference
	 *            This parameter is a reference which refines the identification
	 *            of the called process. It may be identical to Destination
	 *            address but its value is to be carried at MAP level.
	 * 
	 * @param origAddress
	 *            A valid SCCP address identifying the requestor of a MAP
	 *            dialogue. As an implementation option, this parameter may
	 *            also, in the request, be implicitly associated with the
	 *            service access point at which the primitive is issued.
	 * 
	 * @param origReference
	 *            This parameter is a reference which refines the identification
	 *            of the calling process. It may be identical to the Originating
	 *            address but its value is to be carried at MAP level.
	 *            Processing of the Originating-reference shall be performed
	 *            according to the supplementary service descriptions and other
	 *            service descriptions, e.g. operator determined barring.
	 * @return
	 */
	public MAPDialog createNewDialog(int applicationCntx,
			SccpAddress destAddress, byte[] destReference,
			SccpAddress origAddress, byte[] origReference);

	/**
	 * 
	 * @param mapDialogListener
	 */
	public void addMAPDialogListener(MAPDialogListener mapDialogListener);

	/**
	 * 
	 * @param mapDialogListener
	 */
	public void removeMAPDialogListener(MAPDialogListener mapDialogListener);

	/**
	 * 
	 * @param mapServiceListener
	 */
	public void addMAPServiceListener(MAPServiceListener mapServiceListener);

	/**
	 * 
	 * @param mapServiceListener
	 */
	public void removeMAPServiceListener(MAPServiceListener mapServiceListener);
	
	
	public MapServiceFactory getMapServiceFactory();

}
