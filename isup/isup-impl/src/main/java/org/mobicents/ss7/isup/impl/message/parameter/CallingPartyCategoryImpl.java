/**
 * Start time:13:31:04 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.ss7.isup.impl.message.parameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.mobicents.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.ss7.isup.message.parameter.CallingPartyCategory;

/**
 * Start time:13:31:04 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class CallingPartyCategoryImpl extends AbstractParameter implements CallingPartyCategory {

	private byte callingPartyCategory = 0;

	public CallingPartyCategoryImpl(byte callingPartyCategory) {
		super();
		this.callingPartyCategory = callingPartyCategory;
	}

	public CallingPartyCategoryImpl() {
		super();

	}

	public CallingPartyCategoryImpl(byte[] representation) throws ParameterRangeInvalidException {
		super();
		this.decodeElement(representation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */
	public int decodeElement(byte[] b) throws ParameterRangeInvalidException {
		if (b == null || b.length != 1) {
			throw new ParameterRangeInvalidException("byte[] must not be null or have different size than 1");
		}
		this.callingPartyCategory = b[0];

		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {

		return new byte[] { this.callingPartyCategory };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.isup.ISUPComponent#encodeElement(java.io.ByteArrayOutputStream
	 * )
	 */
	public int encodeElement(ByteArrayOutputStream bos) throws IOException {
		bos.write(this.callingPartyCategory);
		return 1;
	}

	public byte getCallingPartyCategory() {
		return callingPartyCategory;
	}

	public void setCallingPartyCategory(byte callingPartyCategory) {
		this.callingPartyCategory = callingPartyCategory;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
