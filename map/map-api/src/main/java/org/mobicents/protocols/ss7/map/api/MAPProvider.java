package org.mobicents.protocols.ss7.map.api;

public interface MAPProvider {

	/**
	 * 
	 * @param dialogRequest
	 */
	public void sendDialogRequest(DialogRequest dialogRequest);

	
	/**
	 * 
	 * @param dialogResponse
	 */
	public void sendDialogResponse(DialogResponse dialogResponse);

	
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
	 * @param serviceRequest
	 */
	public void sendServiceRequest(ServiceRequest serviceRequest);

	
	/**
	 * 
	 * @param serviceResponse
	 */
	public void sendServiceResponse(ServiceResponse serviceResponse);

	
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

}
