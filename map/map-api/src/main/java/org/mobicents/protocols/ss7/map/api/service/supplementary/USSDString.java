package org.mobicents.protocols.ss7.map.api.service.supplementary;

import java.nio.charset.Charset;

import org.mobicents.protocols.ss7.map.api.MAPException;

/**
 * 
 * @author amit bhayani
 * 
 */
public interface USSDString {

	/**
	 * Get the decoded USSD String
	 * 
	 * @return
	 */
	public String getString();

	/**
	 * Get the byte[] that represents encoded USSD String
	 * 
	 * @return
	 */
	public byte[] getEncodedString();

	/**
	 * Get the {@link java.nio.charset.Charset}
	 * 
	 * @return
	 */
	public Charset getCharset();

	/**
	 * Encode the set USSD String. After calling this operation, call
	 * getEncodedString() to get the encoded byte[]
	 * 
	 * @throws MAPException
	 */
	public void encode() throws MAPException;

	/**
	 * Decode the byte[] that represents the USSD String. After calling this
	 * operation call getString() to get the USSD String
	 * 
	 * @throws MAPException
	 */
	public void decode() throws MAPException;

}
