/**
 * Start time:13:31:04 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyCategory;

/**
 * Start time:13:31:04 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class CallingPartyCategoryImpl extends AbstractISUPParameter implements CallingPartyCategory {

	private byte callingPartyCategory = 0;

	public CallingPartyCategoryImpl(byte callingPartyCategory) {
		super();
		this.callingPartyCategory = callingPartyCategory;
	}

	public CallingPartyCategoryImpl() {
		super();

	}

	public CallingPartyCategoryImpl(byte[] representation) throws ParameterException {
		super();
		this.decode(representation);
	}

	public int decode(byte[] b) throws ParameterException {
		if (b == null || b.length != 1) {
			throw new ParameterException("byte[] must not be null or have different size than 1");
		}
		this.callingPartyCategory = b[0];

		return 1;
	}

	public byte[] encode() throws ParameterException {

		return new byte[] { this.callingPartyCategory };
	}

	public int encode(ByteArrayOutputStream bos) throws ParameterException {
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
