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
	 * Set the USSD String that will be encoded
	 * 
	 * @param ussdString
	 */
	public void setString(String ussdString);

	/**
	 * Get the decoded USSD String
	 * 
	 * @return
	 */
	public String getString();

	/**
	 * Set the encoded byte[] that represents USSD String
	 * 
	 * @param encodedString
	 */
	public void setEncodedString(byte[] encodedString);

	/**
	 * Get the byte[] that represents encoded USSD String
	 * 
	 * @return
	 */
	public byte[] getEncodedString();

	/**
	 * Set the {@link java.nio.charset.Charset} that will be used for encoding
	 * and decoding of USSD String. If not set the default will be used; which
	 * is 7-Bit GSM Encoding as specified in GSM 03.38 Specs
	 * 
	 * @param charset
	 */
	public void setCharset(Charset charset);

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
