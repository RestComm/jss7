/**
 * Start time:16:14:51 2009-03-29<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.ss7.isup.impl.message.parameter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.mobicents.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.ss7.isup.message.parameter.CallingPartyNumber;

/**
 * Start time:16:14:51 2009-03-29<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author Oleg Kulikoff
 */
public class CallingPartyNumberImpl extends AbstractNAINumber implements CallingPartyNumber {

	protected int numberingPlanIndicator;

	protected int numberIncompleteIndicator;

	protected int addressRepresentationREstrictedIndicator;

	protected int screeningIndicator;

	/**
	 * 
	 * @param representation
	 * @throws ParameterRangeInvalidException
	 */
	public CallingPartyNumberImpl(byte[] representation) throws ParameterRangeInvalidException {
		super(representation);
		// TODO Auto-generated constructor stub
	}

	public CallingPartyNumberImpl() throws ParameterRangeInvalidException {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param bis
	 * @throws ParameterRangeInvalidException
	 */
	public CallingPartyNumberImpl(ByteArrayInputStream bis) throws ParameterRangeInvalidException {
		super(bis);
		// TODO Auto-generated constructor stub
	}

	public CallingPartyNumberImpl(int natureOfAddresIndicator, String address, int numberingPlanIndicator, int numberIncompleteIndicator, int addressRepresentationREstrictedIndicator,
			int screeningIndicator) {
		super(natureOfAddresIndicator, address);
		this.numberingPlanIndicator = numberingPlanIndicator;
		this.numberIncompleteIndicator = numberIncompleteIndicator;
		this.addressRepresentationREstrictedIndicator = addressRepresentationREstrictedIndicator;
		this.screeningIndicator = screeningIndicator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.isup.parameters.AbstractNumber#decodeBody(java.io.
	 * ByteArrayInputStream)
	 */
	@Override
	public int decodeBody(ByteArrayInputStream bis) throws IllegalArgumentException {
		int b = bis.read() & 0xff;

		this.numberIncompleteIndicator = (b & 0x80) >> 7;
		this.numberingPlanIndicator = (b & 0x70) >> 4;
		this.addressRepresentationREstrictedIndicator = (b & 0x0c) >> 2;
		this.screeningIndicator = (b & 0x03);

		return 1;
	}

	@Override
	public int encodeHeader(ByteArrayOutputStream bos) {
		doAddressPresentationRestricted();
		return super.encodeHeader(bos);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.isup.parameters.AbstractNumber#encodeBody(java.io.
	 * ByteArrayOutputStream)
	 */
	@Override
	public int encodeBody(ByteArrayOutputStream bos) {

		int c = this.numberingPlanIndicator << 4;
		c |= (this.numberIncompleteIndicator << 7);
		c |= (this.addressRepresentationREstrictedIndicator << 2);
		c |= (this.screeningIndicator);
		bos.write(c);
		return 1;
	}

	/**
	 * makes checks on APRI - see NOTE to APRI in Q.763, p 23
	 */
	protected void doAddressPresentationRestricted() {

		if (this.addressRepresentationREstrictedIndicator == _APRI_NOT_AVAILABLE) {

			// NOTE 1 – If the parameter is included and the address
			// presentation
			// restricted indicator indicates
			// address not available, octets 3 to n( this are digits.) are
			// omitted,
			// the subfields in items a - odd/evem, b -nai , c - ni and d -npi,
			// are
			// coded with
			//FIXME: add this filler
			// 0's, and the subfield f - filler, is coded with 11.
			this.oddFlag = 0;
			this.natureOfAddresIndicator = 0;
			this.numberIncompleteIndicator = 0;
			this.numberingPlanIndicator = 0;
			this.setAddress("");
		}
	}

	@Override
	public int encodeDigits(ByteArrayOutputStream bos) {

		if (this.addressRepresentationREstrictedIndicator == _APRI_NOT_AVAILABLE) {

			// FIXME: encode with 11(0xC0) ? or 1111(0xF0) ?
			bos.write(0xF0);

			return 1;

		} else {
			return super.encodeDigits(bos);
		}

	}

	public int getNumberingPlanIndicator() {
		return numberingPlanIndicator;
	}

	public void setNumberingPlanIndicator(int numberingPlanIndicator) {
		this.numberingPlanIndicator = numberingPlanIndicator;
	}

	public int getNumberIncompleteIndicator() {
		return numberIncompleteIndicator;
	}

	public void setNumberIncompleteIndicator(int numberIncompleteIndicator) {
		this.numberIncompleteIndicator = numberIncompleteIndicator;
	}

	public int getAddressRepresentationREstrictedIndicator() {
		return addressRepresentationREstrictedIndicator;
	}

	public void setAddressRepresentationREstrictedIndicator(int addressRepresentationREstrictedIndicator) {
		this.addressRepresentationREstrictedIndicator = addressRepresentationREstrictedIndicator;
	}

	public int getScreeningIndicator() {
		return screeningIndicator;
	}

	public void setScreeningIndicator(int screeningIndicator) {
		this.screeningIndicator = screeningIndicator;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
