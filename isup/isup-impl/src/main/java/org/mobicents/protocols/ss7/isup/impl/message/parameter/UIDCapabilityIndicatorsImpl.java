/**
 * Start time:13:49:42 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.UIDCapabilityIndicators;

/**
 * Start time:13:49:42 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * 
 */
public class UIDCapabilityIndicatorsImpl extends AbstractISUPParameter implements UIDCapabilityIndicators {

	private static final int _TURN_ON = 1;
	private static final int _TURN_OFF = 0;

	private byte[] uidCapabilityIndicators = null;

	public UIDCapabilityIndicatorsImpl(byte[] udiActionIndicators) throws ParameterException {
		super();
		decode(udiActionIndicators);
	}

	public UIDCapabilityIndicatorsImpl() {
		super();
		
	}

	public int decode(byte[] b) throws ParameterException {
		try {
			setUIDCapabilityIndicators(b);
		} catch (Exception e) {
			throw new ParameterException(e);
		}
		return b.length;
	}

	public byte[] encode() throws ParameterException {
		for (int index = 0; index < this.uidCapabilityIndicators.length; index++) {
			this.uidCapabilityIndicators[index] = (byte) (this.uidCapabilityIndicators[index] & 0x7F);
		}

		this.uidCapabilityIndicators[this.uidCapabilityIndicators.length - 1] = (byte) ((this.uidCapabilityIndicators[this.uidCapabilityIndicators.length - 1]) | (0x01 << 7));
		return this.uidCapabilityIndicators;
	}

	public byte[] getUIDCapabilityIndicators() {
		return uidCapabilityIndicators;
	}

	public void setUIDCapabilityIndicators(byte[] uidCapabilityIndicators) {
		if (uidCapabilityIndicators == null || uidCapabilityIndicators.length == 0) {
			throw new IllegalArgumentException("byte[] must not be null and length must be greater than 0");
		}
		this.uidCapabilityIndicators = uidCapabilityIndicators;
	}

	public byte createUIDAction(boolean TCI, boolean T9) {
		byte b = (byte) (TCI ? _TURN_ON : _TURN_OFF);
		b |= (T9 ? _TURN_ON : _TURN_OFF) << 1;
		return b;
	}

	public boolean getT9Indicator(byte b) {
		return ((b >> 1) & 0x01) == _TURN_ON;
	}

	public boolean getTCIndicator(byte b) {
		return ((b >> 1) & 0x01) == _TURN_ON;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
