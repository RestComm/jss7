/**
 * Start time:17:36:23 2009-03-29<br>
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
import org.mobicents.ss7.isup.message.parameter.GenericNumber;

/**
 * Start time:17:36:23 2009-03-29<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author Oleg Kulikoff
 */
public class GenericNumberImpl extends AbstractNAINumber implements GenericNumber {

	private static final int _TURN_ON = 1;
	private static final int _TURN_OFF = 0;

	protected int numberQualifierIndicator;
	protected int numberingPlanIndicator;

	protected int addressRepresentationRestrictedIndicator;
	protected boolean numberIncomplete;
	protected int screeningIndicator;

	public GenericNumberImpl(int natureOfAddresIndicator, String address, int numberQualifierIndicator, int numberingPlanIndicator, int addressRepresentationREstrictedIndicator,
			boolean numberIncomplete, int screeningIndicator) {
		super(natureOfAddresIndicator, address);
		this.numberQualifierIndicator = numberQualifierIndicator;
		this.numberingPlanIndicator = numberingPlanIndicator;
		this.addressRepresentationRestrictedIndicator = addressRepresentationREstrictedIndicator;
		this.numberIncomplete = numberIncomplete;
		this.screeningIndicator = screeningIndicator;
	}

	public GenericNumberImpl(byte[] representation) throws ParameterRangeInvalidException {
		super(representation);

	}

	public GenericNumberImpl(ByteArrayInputStream bis) throws ParameterRangeInvalidException {
		super(bis);
		// TODO Auto-generated constructor stub
	}

	public GenericNumberImpl() {
		super();
		// TODO Auto-generated constructor stub
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

		this.numberIncomplete = ((b & 0x80) >> 7) == _TURN_ON;
		this.numberingPlanIndicator = (b & 0x70) >> 4;
		this.addressRepresentationRestrictedIndicator = (b & 0x0c) >> 2;
		this.screeningIndicator = (b & 0x03);
		return 1;
	}

	/**
	 * makes checks on APRI - see NOTE to APRI in Q.763, p 23
	 */
	protected void doAddressPresentationRestricted() {

		if (this.addressRepresentationRestrictedIndicator == _APRI_NOT_AVAILABLE)
			return;
		// NOTE 1 – If the parameter is included and the address presentation
		// restricted indicator indicates
		// address not available, octets 3 to n( this are digits.) are omitted,
		// the subfields in items a - odd/evem, b -nai , c - ni and d -npi, are
		// coded with
		// 0's, and the subfield f - filler, is coded with 11.
		this.oddFlag = 0;
		this.natureOfAddresIndicator = 0;
		this.numberingPlanIndicator = 0;
		this.numberIncomplete = _NI_COMPLETE;
		// 11
		this.screeningIndicator = 3;
		this.setAddress("");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.isup.parameters.AbstractNumber#encodeBody(java.io.
	 * ByteArrayOutputStream)
	 */
	@Override
	public int encodeBody(ByteArrayOutputStream bos) {

		int c = this.screeningIndicator;
		c |= (this.addressRepresentationRestrictedIndicator << 2);
		c |= (this.numberingPlanIndicator << 4);
		c |= ((this.numberIncomplete ? _TURN_ON : _TURN_OFF) << 7);

		bos.write(c);

		return 1;
	}

	@Override
	public int decodeHeader(ByteArrayInputStream bis) throws ParameterRangeInvalidException {
		this.numberQualifierIndicator = bis.read() & 0xff;
		return super.decodeHeader(bis) + 1;
	}

	@Override
	public int encodeHeader(ByteArrayOutputStream bos) {
		doAddressPresentationRestricted();
		bos.write(this.numberQualifierIndicator);
		return super.encodeHeader(bos) + 1;
	}

	public int getNumberQualifierIndicator() {
		return numberQualifierIndicator;
	}

	public void setNumberQualifierIndicator(int numberQualifierIndicator) {
		this.numberQualifierIndicator = numberQualifierIndicator;
	}

	public int getNumberingPlanIndicator() {
		return numberingPlanIndicator;
	}

	public void setNumberingPlanIndicator(int numberingPlanIndicator) {
		this.numberingPlanIndicator = numberingPlanIndicator & 0x07;
	}

	public int getAddressRepresentationRestrictedIndicator() {
		return addressRepresentationRestrictedIndicator;
	}

	public void setAddressRepresentationRestrictedIndicator(int addressRepresentationREstrictedIndicator) {
		this.addressRepresentationRestrictedIndicator = addressRepresentationREstrictedIndicator & 0x03;
	}

	public boolean isNumberIncomplete() {
		return numberIncomplete;
	}

	public void setNumberIncompleter(boolean numberIncomplete) {
		this.numberIncomplete = numberIncomplete;
	}

	public int getScreeningIndicator() {
		return screeningIndicator;
	}

	public void setScreeningIndicator(int screeningIndicator) {
		this.screeningIndicator = screeningIndicator & 0x03;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
