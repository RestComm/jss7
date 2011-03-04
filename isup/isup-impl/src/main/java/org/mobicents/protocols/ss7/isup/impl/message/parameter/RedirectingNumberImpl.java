/**
 * Start time:14:54:53 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectingNumber;

/**
 * Start time:14:54:53 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class RedirectingNumberImpl extends AbstractNAINumber implements RedirectingNumber {

	protected int numberingPlanIndicator = 0;

	protected int addressRepresentationRestrictedIndicator = 0;

	public RedirectingNumberImpl() {
		super();
		
	}

	public RedirectingNumberImpl(int natureOfAddresIndicator, String address, int numberingPlanIndicator, int addressRepresentationRestrictedIndicator) {
		super(natureOfAddresIndicator, address);
		this.numberingPlanIndicator = numberingPlanIndicator;
		this.addressRepresentationRestrictedIndicator = addressRepresentationRestrictedIndicator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.isup.parameters.AbstractNumber#decodeBody(java.io.
	 * ByteArrayInputStream)
	 */
	public RedirectingNumberImpl(byte[] representation) throws ParameterException {
		super(representation);
		
	}

	public RedirectingNumberImpl(ByteArrayInputStream bis) throws ParameterException {
		super(bis);
		
	}

	
	public int decodeBody(ByteArrayInputStream bis) throws IllegalArgumentException {
		int b = bis.read() & 0xff;

		this.numberingPlanIndicator = (b & 0x70) >> 4;
		this.addressRepresentationRestrictedIndicator = (b & 0x0c) >> 2;

		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.isup.parameters.AbstractNumber#encodeBody(java.io.
	 * ByteArrayOutputStream)
	 */
	
	public int encodeBody(ByteArrayOutputStream bos) {
		int c = this.natureOfAddresIndicator << 4;
		c |= (this.addressRepresentationRestrictedIndicator << 2);

		bos.write(c);
		return 1;
	}

	public int getNumberingPlanIndicator() {
		return numberingPlanIndicator;
	}

	public void setNumberingPlanIndicator(int numberingPlanIndicator) {
		this.numberingPlanIndicator = numberingPlanIndicator;
	}

	public int getAddressRepresentationRestrictedIndicator() {
		return addressRepresentationRestrictedIndicator;
	}

	public void setAddressRepresentationRestrictedIndicator(int addressRepresentationREstrictedIndicator) {
		this.addressRepresentationRestrictedIndicator = addressRepresentationREstrictedIndicator;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
	/**
	 * <pre>
	 * a) Odd/even indicator: as for 3.9 a).
	 * 	b) Nature of address indicator: as for 3.10 b).
	 * 	c) Numbering plan indicator: as for 3.9 d).
	 * 	d) Address presentation restricted indicator: as for 3.10 e).
	 * 	e) Address signal: as for 3.10 g).
	 * 	f) Filler: as for 3.9 f).
	 * </pre>
	 */
}
