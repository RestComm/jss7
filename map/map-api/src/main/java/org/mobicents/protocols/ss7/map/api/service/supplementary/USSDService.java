package org.mobicents.protocols.ss7.map.api.service.supplementary;

import org.mobicents.protocols.ss7.map.api.MAPMessage;

public interface USSDService extends MAPMessage {

	/**
	 * This parameter contains the information of the alphabet and the language
	 * used for the unstructured information in an Unstructured Supplementary
	 * Service Data operation. The coding of this parameter is according to the
	 * Cell Broadcast Data Coding Scheme as specified in GSM 03.38.
	 * 
	 * @return
	 */
	public byte getUSSDDataCodingScheme();

	/**
	 * 
	 * @param ussdDataCodingSch
	 */
	public void setUSSDDataCodingScheme(byte ussdDataCodingSch);

	/**
	 * <p>
	 * This parameter contains a string of unstructured information in an
	 * Unstructured Supplementary Service Data operation. The string is sent
	 * either by the mobile user or the network. The contents of a string sent
	 * by the MS are interpreted by the network as specified in GSM 02.90.
	 * </p>
	 * <br/>
	 * <p>
	 * USSD String is OCTET STRING (SIZE (1..160))
	 * </p>
	 * 
	 * <br/>
	 * 
	 * <p>
	 * The structure of the contents of the USSD-String is dependent -- on the
	 * USSD-DataCodingScheme as described in TS GSM 03.38.
	 * </p>
	 * 
	 * 
	 * 
	 * @return
	 */
	public byte[] getUSSDString();

	/**
	 * 
	 * @param ussdString
	 */
	public void setUSSDString(byte[] ussdString);

}
