/**
 * Start time:11:50:01 2009-03-31<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.parameter.FacilityIndicator;

/**
 * Start time:11:50:01 2009-03-31<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class FacilityIndicatorImpl extends AbstractParameter implements FacilityIndicator {

	private int facilityIndicator = 0;

	public FacilityIndicatorImpl(byte[] b) throws ParameterRangeInvalidException {
		super();
		decodeElement(b);
	}

	public FacilityIndicatorImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FacilityIndicatorImpl(int facilityIndicator) {
		super();
		this.facilityIndicator = facilityIndicator;
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

		this.facilityIndicator = b[0];
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {
		byte[] b = { (byte) this.facilityIndicator };
		return b;
	}

	public int getFacilityIndicator() {
		return facilityIndicator;
	}

	public void setFacilityIndicator(int facilityIndicator) {
		this.facilityIndicator = facilityIndicator;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
