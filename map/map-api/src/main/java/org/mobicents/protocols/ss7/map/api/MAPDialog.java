package org.mobicents.protocols.ss7.map.api;

import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;

/**
 * 
 * @author amit bhayani
 * 
 */
public interface MAPDialog {

	public Long getDialogId();

	/**
	 * Adds the {@code ServiceRequest} to this MAPDialog however the
	 * ServiceRequest is not sent to remote end till send() is called
	 * 
	 * @param serviceRequest
	 */
	// public void addServiceRequest(ServiceRequest serviceRequest);
	/**
	 * Adds the {@code serviceResponse} to this MAPDialog however the
	 * serviceResponse is not sent to remote end till send() is called
	 * 
	 * @param serviceResponse
	 */
	// public void addServiceResponse(ServiceResponse serviceResponse);
	/**
	 * This is equivalent of MAP User issuing the MAP-DELIMITER Service Request.
	 * send() is called to explicitly request the transfer of the MAP protocol
	 * data units to the peer entities.
	 */
	public void send() throws MAPException;

	/**
	 * This is equvalent of MAP User issuing the MAP-CLOSE Service Request. This
	 * service is used for releasing a previously established MAP dialogue. The
	 * service may be invoked by either MAP service-user depending on rules
	 * defined within the service-user.
	 * 
	 * <br/>
	 * 
	 * If prearrangedEnd is false, all the Service Primitive added to MAPDialog
	 * and not sent yet, will be sent to peer.
	 * 
	 * <br/>
	 * 
	 * If prearrangedEnd is true, all the Service Primitive added to MAPDialog
	 * and not sent yet, will not be sent to peer.
	 * 
	 * @param prearrangedEnd
	 */
	public void close(boolean prearrangedEnd);

	/**
	 * This is equivalent to MAP User issuing the MAP-U-ABORT Service Request.
	 * 
	 * @param userReason
	 */
	// TODO : add DiagnosticInformation and SpecificInformation?
	public void abort(int userReason);

	public void addProcessUnstructuredSSRequest(byte ussdDataCodingScheme,
			USSDString ussdString) throws MAPException;

	public void addProcessUnstructuredSSResponse(byte ussdDataCodingScheme,
			USSDString ussdString) throws MAPException;

	public void addUnstructuredSSRequest(byte ussdDataCodingScheme,
			USSDString ussdString) throws MAPException;

	public void addUnstructuredSSResponse(byte ussdDataCodingScheme,
			USSDString ussdString) throws MAPException;

}
