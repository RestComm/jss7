package org.mobicents.protocols.ss7.map.api;

import org.mobicents.protocols.ss7.map.api.dialog.AddressString;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;

/**
 * 
 * @author amit bhayani
 * 
 */
public interface MAPDialog {

	/**
	 * Returns this Dialog's ID. Thiis ID is actually TCAP's Dialog ID.
	 * {@link org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog}
	 * 
	 * @return
	 */
	public Long getDialogId();

	/**
	 * This is equivalent of MAP User issuing the MAP_DELIMITER Service Request.
	 * send() is called to explicitly request the transfer of the MAP protocol
	 * data units to the peer entities.
	 */
	public void send() throws MAPException;

	/**
	 * This is equivalent of MAP User issuing the MAP_CLOSE Service Request.
	 * This service is used for releasing a previously established MAP dialogue.
	 * The service may be invoked by either MAP service-user depending on rules
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
	public void close(boolean prearrangedEnd) throws MAPException;

	/**
	 * This is equivalent to MAP User issuing the MAP_U_ABORT Service Request.
	 * 
	 * @param userReason
	 */
	public void abort(MAPUserAbortChoice mapUserAbortChoice)
			throws MAPException;

	/**
	 * Add's a new Process Unstructured SS Request as Component.
	 * 
	 * @param ussdDataCodingScheme
	 *            The Data Coding Scheme for this USSD String as defined in GSM
	 *            03.38
	 * @param ussdString
	 *            Ussd String
	 * @param msisdn
	 *            The MSISDN in {@link AddressString} format. This is optional
	 * @throws MAPException
	 */
	public void addProcessUnstructuredSSRequest(byte ussdDataCodingScheme,
			USSDString ussdString, AddressString msisdn) throws MAPException;

	/**
	 * Add's a new ProcessUnstructured SS Response as Component.
	 * 
	 * @param invokeId
	 *            The original invoke ID retrieved from
	 *            {@link ProcessUnstructuredSSIndication}
	 * @param lastResult
	 *            Specify if this Result is last - true, or there would be
	 *            follow-up results - false
	 * @param ussdDataCodingScheme
	 *            The Data Coding Scheme for this USSD String as defined in GSM
	 *            03.38
	 * @param ussdString
	 *            Ussd String {@link USSDString}
	 * @throws MAPException
	 */
	public void addProcessUnstructuredSSResponse(long invokeId,
			boolean lastResult, byte ussdDataCodingScheme, USSDString ussdString)
			throws MAPException;

	/**
	 * Add's a new Unstructured SS Request
	 * 
	 * @param ussdDataCodingScheme
	 *            The Data Coding Scheme for this USSD String as defined in GSM
	 *            03.38
	 * @param ussdString
	 *            Ussd String {@link USSDString}
	 * @throws MAPException
	 */
	public void addUnstructuredSSRequest(byte ussdDataCodingScheme,
			USSDString ussdString) throws MAPException;

	/**
	 * Add's a new Unstructured SS Response
	 * 
	 * @param invokeId
	 *            The original invoke ID retrieved from
	 *            {@link UnstructuredSSIndication}
	 * @param lastResult
	 *            Specify if this Result is last - true, or there would be
	 *            follow-up results - false
	 * @param ussdDataCodingScheme
	 *            The Data Coding Scheme for this USSD String as defined in GSM
	 *            03.38
	 * @param ussdString
	 *            Ussd String {@link USSDString}
	 * @throws MAPException
	 */
	public void addUnstructuredSSResponse(long invokeId, boolean lastResult,
			byte ussdDataCodingScheme, USSDString ussdString)
			throws MAPException;

}
