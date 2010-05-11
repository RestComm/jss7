/**
 * Start time:13:47:48 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.parameter.DisplayInformation;

/**
 * Start time:13:47:48 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class DisplayInformationImpl extends AbstractParameter implements DisplayInformation {

	// FIXME: Q.931 4.5.16 Display - Oleg is this correct?

	private byte[] info;

	public DisplayInformationImpl(byte[] info) throws ParameterRangeInvalidException {
		super();
		// FIXME: this is only elementID
		//super.tag = new byte[] { 0x28 };
		decodeElement(info);
	}

	public DisplayInformationImpl() {
		super();
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */
	public int decodeElement(byte[] b) throws ParameterRangeInvalidException {
		try {
			setInfo(b);
		} catch (Exception e) {
			throw new ParameterRangeInvalidException(e);
		}
		return b.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {
		for (int index = 0; index < this.info.length; index++) {
			this.info[index] = (byte) (this.info[index] & 0x7F);
		}

		this.info[this.info.length - 1] = (byte) ((this.info[this.info.length - 1]) | (0x01 << 7));
		return this.info;
	}

	public byte[] getInfo() {
		return info;
	}

	public void setInfo(byte[] info) throws IllegalArgumentException {
		if (info == null || info.length == 0) {
			throw new IllegalArgumentException("byte[] must not be null and length must be greater than 0");
		}
		this.info = info;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
