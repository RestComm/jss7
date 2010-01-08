/**
 * Start time:16:55:01 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.ss7.isup.message.parameter.RedirectionNumberRestriction;

/**
 * Start time:16:55:01 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * 
 */
public class RedirectionNumberRestrictionImpl extends AbstractParameter implements RedirectionNumberRestriction {

	private int presentationRestrictedIndicator;

	public RedirectionNumberRestrictionImpl(int presentationRestrictedIndicator) {
		super();
		this.presentationRestrictedIndicator = presentationRestrictedIndicator;
	}

	public RedirectionNumberRestrictionImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RedirectionNumberRestrictionImpl(byte[] b) throws ParameterRangeInvalidException {
		super();
		decodeElement(b);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */
	public int decodeElement(byte[] b) throws ParameterRangeInvalidException {
		if (b == null || b.length != 1) {
			throw new ParameterRangeInvalidException("byte[] must  not be null and length must  be 1");
		}

		this.presentationRestrictedIndicator = (byte) (b[0] & 0x03);
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {
		return new byte[] { (byte) (this.presentationRestrictedIndicator & 0x03) };
	}

	public int getPresentationRestrictedIndicator() {
		return presentationRestrictedIndicator;
	}

	public void setPresentationRestrictedIndicator(int presentationRestrictedIndicator) {
		this.presentationRestrictedIndicator = presentationRestrictedIndicator;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
