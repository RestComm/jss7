/**
 * Start time:12:34:12 2009-09-07<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.parameter.CallTransferNumber;

/**
 * Start time:12:34:12 2009-09-07<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class CallTransferNumberImpl extends AbstractNAINumber implements CallTransferNumber {

	protected int numberingPlanIndicator;

	protected int addressRepresentationREstrictedIndicator;

	protected int screeningIndicator;
	/** 
	 * @param representation
	 * @throws ParameterRangeInvalidException
	 */
	public CallTransferNumberImpl(byte[] representation) throws ParameterRangeInvalidException {
		super(representation);
		// TODO Auto-generated constructor stub
	}

	public CallTransferNumberImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * tttttt
	 * 
	 * @param bis
	 * @throws ParameterRangeInvalidException
	 */
	public CallTransferNumberImpl(ByteArrayInputStream bis) throws ParameterRangeInvalidException {
		super(bis);
		// TODO Auto-generated constructor stub
	}

	public CallTransferNumberImpl(int natureOfAddresIndicator, String address, int numberingPlanIndicator, int addressRepresentationREstrictedIndicator, int screeningIndicator) {
		super(natureOfAddresIndicator, address);
		this.numberingPlanIndicator = numberingPlanIndicator;
		this.addressRepresentationREstrictedIndicator = addressRepresentationREstrictedIndicator;
		this.screeningIndicator = screeningIndicator;
	}

	@Override
	public int encodeHeader(ByteArrayOutputStream bos) {
		doAddressPresentationRestricted();
		return super.encodeHeader(bos);
	}

	/**
	 * makes checks on APRI - see NOTE to APRI in Q.763, p 23
	 */
	protected void doAddressPresentationRestricted() {

		
		//FIXME: Oleg?
//		
//		if (this.addressRepresentationREstrictedIndicator == _)
//			return;
//
//		// NOTE 1 – If the parameter is included and the address presentation
//		// restricted indicator indicates
//		// address not available, octets 3 to n( this are digits.) are omitted,
//		// the subfields in items a - odd/evem, b -nai , c - ni and d -npi, are
//		// coded with
//		// 0's, and the subfield f - filler, is coded with 11.
//		this.oddFlag = 0;
//		this.natureOfAddresIndicator = 0;
//		this.numberingPlanIndicator = 0;
//		// 11
//		this.screeningIndicator = 3;
	}
	/* (non-Javadoc)
	 * @see org.mobicents.ss7.isup.message.parameter.AbstractNumber#decodeBody(java.io.ByteArrayInputStream)
	 */
	@Override
	public int decodeBody(ByteArrayInputStream bis) throws IllegalArgumentException {
		int b = bis.read() & 0xff;

		this.numberingPlanIndicator = (b & 0x70) >> 4;
		this.addressRepresentationREstrictedIndicator = (b & 0x0c) >> 2;
		this.screeningIndicator = (b & 0x03);
		return 1;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.ss7.isup.message.parameter.AbstractNumber#encodeBody(java.io.ByteArrayOutputStream)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.isup.parameters.AbstractNumber#encodeBody(java.io.
	 * ByteArrayOutputStream)
	 */
	@Override
	public int encodeBody(ByteArrayOutputStream bos) {
		int c = this.numberingPlanIndicator << 4;

		c |= (this.addressRepresentationREstrictedIndicator << 2);
		c |= (this.screeningIndicator);
		bos.write(c & 0x7F);
		return 1;
	}

	public int getNumberingPlanIndicator() {
		return numberingPlanIndicator;
	}

	public void setNumberingPlanIndicator(int numberingPlanIndicator) {
		this.numberingPlanIndicator = numberingPlanIndicator;
	}

	public int getAddressRepresentationRestrictedIndicator() {
		return addressRepresentationREstrictedIndicator;
	}

	public void setAddressRepresentationRestrictedIndicator(int addressRepresentationREstrictedIndicator) {
		this.addressRepresentationREstrictedIndicator = addressRepresentationREstrictedIndicator;
	}

	public int getScreeningIndicator() {
		return screeningIndicator;
	}

	public void setScreeningIndicator(int screeningIndicator) {
		this.screeningIndicator = screeningIndicator;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.ss7.isup.message.parameter.ISUPParameter#getCode()
	 */
	public int getCode() {
		return this._PARAMETER_CODE;
	}

}
