/**
 * Start time:16:16:18 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.parameter.PivotRoutingIndicators;

/**
 * Start time:16:16:18 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 */
public class PivotRoutingIndicatorsImpl extends AbstractParameter implements PivotRoutingIndicators{
	
	

	private byte[] pivotRoutingIndicators;

	public PivotRoutingIndicatorsImpl() {
		super();
		
	}

	public PivotRoutingIndicatorsImpl(byte[] pivotRoutingIndicators) throws ParameterRangeInvalidException {
		super();
		decodeElement(pivotRoutingIndicators);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {
		for (int index = 0; index < this.pivotRoutingIndicators.length; index++) {
			this.pivotRoutingIndicators[index] = (byte) (this.pivotRoutingIndicators[index] & 0x7F);
		}

		this.pivotRoutingIndicators[this.pivotRoutingIndicators.length - 1] = (byte) ((this.pivotRoutingIndicators[this.pivotRoutingIndicators.length - 1]) | (0x01 << 7));
		return this.pivotRoutingIndicators;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */
	public int decodeElement(byte[] b) throws ParameterRangeInvalidException {

		setPivotRoutingIndicators(b);
		return b.length;
	}

	public byte[] getPivotRoutingIndicators() {
		return pivotRoutingIndicators;
	}

	public void setPivotRoutingIndicators(byte[] pivotRoutingIndicators) {
		if (pivotRoutingIndicators == null || pivotRoutingIndicators.length == 0) {
			throw new IllegalArgumentException("byte[] must not be null and length must be greater than 0");
		}
		this.pivotRoutingIndicators = pivotRoutingIndicators;
	}
	public int getCode() {

		return _PARAMETER_CODE;
	}
}
