/**
 * Start time:09:11:07 2009-04-06<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectCapability;

/**
 * Start time:09:11:07 2009-04-06<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 */
public class RedirectCapabilityImpl extends AbstractParameter implements RedirectCapability{

	

	private byte[] capabilities;
	public RedirectCapabilityImpl()  {
		super();
		
	}
	public RedirectCapabilityImpl(byte[] capabilities) throws ParameterRangeInvalidException {
		super();
		decodeElement(capabilities);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */
	public int decodeElement(byte[] b) throws ParameterRangeInvalidException {
		try
		{
			this.setCapabilities(b);
		}catch(Exception e)
		{
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
		for (int index = 0; index < this.capabilities.length; index++) {
			this.capabilities[index] = (byte) (this.capabilities[index] & 0x07);
		}

		this.capabilities[this.capabilities.length - 1] = (byte) ((this.capabilities[this.capabilities.length - 1]) | (0x01 << 7));
		return this.capabilities;
	}

	public byte[] getCapabilities() {
		return capabilities;
	}

	public void setCapabilities(byte[] capabilities) {
		if (capabilities == null || capabilities.length == 0) {
			throw new IllegalArgumentException("byte[] must not be null and length must be greater than 0");
		}
		this.capabilities = capabilities;
	}

	public int getCapability(byte b) {
		return b & 0x7F;
	}
	public int getCode() {

		return _PARAMETER_CODE;
	}
}
