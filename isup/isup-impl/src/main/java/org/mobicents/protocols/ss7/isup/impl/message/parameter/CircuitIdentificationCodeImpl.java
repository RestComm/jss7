/**
 * Start time:14:49:25 2009-09-18<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitIdentificationCode;

/**
 * Start time:14:49:25 2009-09-18<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class CircuitIdentificationCodeImpl extends AbstractParameter implements CircuitIdentificationCode {

	private long cic;
	/**
	 * 	
	 */
	public CircuitIdentificationCodeImpl() {
		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.message.parameter.CircuitIdentificationCode#getCIC()
	 */
	public long getCIC() {
		return this.cic;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.message.parameter.CircuitIdentificationCode#setCIC(long)
	 */
	public void setCIC(long cic) {
		this.cic = cic & 0x0FFF;

	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.message.parameter.ISUPParameter#getCode()
	 */
	public int getCode() {
		//Its not a real parameter.
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.ISUPComponent#decodeElement(byte[])
	 */
	public int decodeElement(byte[] b) throws ParameterRangeInvalidException {
		if(b == null || b.length!=2)
		{
			throw new ParameterRangeInvalidException("byte[] must not be null or has size equal to 2.");
		}
		this.cic = (b[0] & 0xFF);
		this.cic |= ((b[1] & 0x0F) << 8) ;
		return b.length;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {
		byte[] b = new byte[2];
		b[0] = (byte) this.cic;
		b[1] = (byte) ((this.cic >> 8) & 0x0F);
		return b;
	}

}
