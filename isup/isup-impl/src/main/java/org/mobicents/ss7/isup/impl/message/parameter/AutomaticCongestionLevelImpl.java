/**
 * Start time:13:42:13 2009-03-30<br>
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
import org.mobicents.ss7.isup.message.parameter.AutomaticCongestionLevel;

/**
 * Start time:13:42:13 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 */
public class AutomaticCongestionLevelImpl extends AbstractParameter implements AutomaticCongestionLevel{
	
	private int automaticCongestionLevel = 0;

	
	
	
	public AutomaticCongestionLevelImpl() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AutomaticCongestionLevelImpl(byte[] b) throws ParameterRangeInvalidException {
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
			throw new ParameterRangeInvalidException("byte[] must not be null or have different size than 1");
		}
		this.automaticCongestionLevel = b[0] & 0x01;
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {

		return new byte[] { (byte) this.automaticCongestionLevel };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.isup.ISUPComponent#encodeElement(java.io.ByteArrayOutputStream
	 * )
	 */
	public int encodeElement(ByteArrayOutputStream bos) throws IOException {
		bos.write(this.automaticCongestionLevel);
		return 1;
	}

	public int getAutomaticCongestionLevel() {
		return automaticCongestionLevel;
	}

	public void setAutomaticCongestionLevel(int automaticCongestionLevel) {
		this.automaticCongestionLevel = automaticCongestionLevel & 0x01;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
