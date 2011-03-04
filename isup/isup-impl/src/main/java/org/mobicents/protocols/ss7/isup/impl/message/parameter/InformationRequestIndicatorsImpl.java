/**
 * Start time:14:20:07 2009-04-01<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.InformationRequestIndicators;

/**
 * Start time:14:20:07 2009-04-01<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class InformationRequestIndicatorsImpl extends AbstractISUPParameter implements InformationRequestIndicators {

	private final static int _TURN_ON = 1;
	private final static int _TURN_OFF = 0;

	private boolean callingPartAddressRequestIndicator;
	private boolean holdingIndicator;
	private boolean callingpartysCategoryRequestIndicator;
	private boolean chargeInformationRequestIndicator;
	private boolean maliciousCallIdentificationRequestIndicator;

	// FIXME: should we carre about this?
	private int reserved;

	public InformationRequestIndicatorsImpl(byte[] b) throws ParameterException {
		super();
		decode(b);
	}

	public InformationRequestIndicatorsImpl() {
		super();
		
	}

	public InformationRequestIndicatorsImpl(boolean callingPartAddressRequestIndicator, boolean holdingIndicator, boolean callingpartysCategoryRequestIndicator,
			boolean chargeInformationRequestIndicator, boolean maliciousCallIdentificationRequestIndicator, int reserved) {
		super();
		this.callingPartAddressRequestIndicator = callingPartAddressRequestIndicator;
		this.holdingIndicator = holdingIndicator;
		this.callingpartysCategoryRequestIndicator = callingpartysCategoryRequestIndicator;
		this.chargeInformationRequestIndicator = chargeInformationRequestIndicator;
		this.maliciousCallIdentificationRequestIndicator = maliciousCallIdentificationRequestIndicator;
		this.reserved = reserved;
	}

	public int decode(byte[] b) throws ParameterException {
		if (b == null || b.length != 2) {
			throw new IllegalArgumentException("byte[] must  not be null and length must  be 2");
		}

		this.callingPartAddressRequestIndicator = (b[0] & 0x01) == _TURN_ON;
		this.holdingIndicator = ((b[0] >> 1) & 0x01) == _TURN_ON;
		this.callingpartysCategoryRequestIndicator = ((b[0] >> 3) & 0x01) == _TURN_ON;
		this.chargeInformationRequestIndicator = ((b[0] >> 4) & 0x01) == _TURN_ON;
		this.maliciousCallIdentificationRequestIndicator = ((b[0] >> 7) & 0x01) == _TURN_ON;
		this.reserved = (b[1] >> 4) & 0x0F;
		return 2;
	}

	public byte[] encode() throws ParameterException {
		int b0 = 0;
		int b1 = 0;
		b0 |= this.callingPartAddressRequestIndicator ? _TURN_ON : _TURN_OFF;
		b0 |= (this.holdingIndicator ? _TURN_ON : _TURN_OFF) << 1;
		b0 |= (this.callingpartysCategoryRequestIndicator ? _TURN_ON : _TURN_OFF) << 3;
		b0 |= (this.chargeInformationRequestIndicator ? _TURN_ON : _TURN_OFF) << 4;
		b0 |= (this.maliciousCallIdentificationRequestIndicator ? _TURN_ON : _TURN_OFF) << 7;

		b1 |= (this.reserved & 0x0F) << 4;

		return new byte[] { (byte) b0, (byte) b1 };
	}

	public boolean isCallingPartAddressRequestIndicator() {
		return callingPartAddressRequestIndicator;
	}

	public void setCallingPartAddressRequestIndicator(boolean callingPartAddressRequestIndicator) {
		this.callingPartAddressRequestIndicator = callingPartAddressRequestIndicator;
	}

	public boolean isHoldingIndicator() {
		return holdingIndicator;
	}

	public void setHoldingIndicator(boolean holdingIndicator) {
		this.holdingIndicator = holdingIndicator;
	}

	public boolean isCallingpartysCategoryRequestIndicator() {
		return callingpartysCategoryRequestIndicator;
	}

	public void setCallingpartysCategoryRequestIndicator(boolean callingpartysCategoryRequestIndicator) {
		this.callingpartysCategoryRequestIndicator = callingpartysCategoryRequestIndicator;
	}

	public boolean isChargeInformationRequestIndicator() {
		return chargeInformationRequestIndicator;
	}

	public void setChargeInformationRequestIndicator(boolean chargeInformationRequestIndicator) {
		this.chargeInformationRequestIndicator = chargeInformationRequestIndicator;
	}

	public boolean isMaliciousCallIdentificationRequestIndicator() {
		return maliciousCallIdentificationRequestIndicator;
	}

	public void setMaliciousCallIdentificationRequestIndicator(boolean maliciousCallIdentificationRequestIndicator) {
		this.maliciousCallIdentificationRequestIndicator = maliciousCallIdentificationRequestIndicator;
	}

	public int getReserved() {
		return reserved;
	}

	public void setReserved(int reserved) {
		this.reserved = reserved;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
