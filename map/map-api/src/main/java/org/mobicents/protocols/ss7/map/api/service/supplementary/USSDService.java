package org.mobicents.protocols.ss7.map.api.service.supplementary;

public interface USSDService {

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
	 * This parameter contains a string of unstructured information in an
	 * Unstructured Supplementary Service Data operation. The string is sent
	 * either by the mobile user or the network. The contents of a string sent
	 * by the MS are interpreted by the network as specified in GSM 02.90.
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
