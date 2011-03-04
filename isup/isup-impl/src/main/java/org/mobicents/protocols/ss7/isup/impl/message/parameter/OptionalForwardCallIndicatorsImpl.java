/**
 * Start time:12:00:59 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.OptionalForwardCallIndicators;

/**
 * Start time:12:00:59 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class OptionalForwardCallIndicatorsImpl extends AbstractISUPParameter implements OptionalForwardCallIndicators {

	private static final int _TURN_ON = 1;
	private static final int _TURN_OFF = 0;

	private int closedUserGroupCallIndicator;
	private boolean simpleSegmentationIndicator;
	private boolean connectedLineIdentityRequestIndicator;

	public OptionalForwardCallIndicatorsImpl(byte[] b) throws ParameterException {
		super();
		decode(b);
	}

	public OptionalForwardCallIndicatorsImpl(int closedUserGroupCallIndicator, boolean simpleSegmentationIndicator, boolean connectedLineIdentityRequestIndicator) {
		super();
		this.closedUserGroupCallIndicator = closedUserGroupCallIndicator;
		this.simpleSegmentationIndicator = simpleSegmentationIndicator;
		this.connectedLineIdentityRequestIndicator = connectedLineIdentityRequestIndicator;
	}

	public OptionalForwardCallIndicatorsImpl() {
		super();
		
	}

	public int decode(byte[] b) throws ParameterException {
		if (b == null || b.length != 1) {
			throw new ParameterException("byte[] must  not be null and length must  be 1");
		}
		this.closedUserGroupCallIndicator = (byte) (b[0] & 0x03);
		this.simpleSegmentationIndicator = ((b[0] >> 2) & 0x01) == _TURN_ON;
		this.connectedLineIdentityRequestIndicator = ((b[0] >> 7) & 0x01) == _TURN_ON;
		return 1;
	}

	public byte[] encode() throws ParameterException {

		int b0 = 0;

		b0 = this.closedUserGroupCallIndicator & 0x03;
		b0 |= (this.simpleSegmentationIndicator ? _TURN_ON : _TURN_OFF) << 2;
		b0 |= (this.connectedLineIdentityRequestIndicator ? _TURN_ON : _TURN_OFF) << 7;

		return new byte[] { (byte) b0 };
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}

	public int getClosedUserGroupCallIndicator() {
		return closedUserGroupCallIndicator;
	}

	public void setClosedUserGroupCallIndicator(int closedUserGroupCallIndicator) {
		this.closedUserGroupCallIndicator = closedUserGroupCallIndicator;
	}

	public boolean isSimpleSegmentationIndicator() {
		return simpleSegmentationIndicator;
	}

	public void setSimpleSegmentationIndicator(boolean simpleSegmentationIndicator) {
		this.simpleSegmentationIndicator = simpleSegmentationIndicator;
	}

	public boolean isConnectedLineIdentityRequestIndicator() {
		return connectedLineIdentityRequestIndicator;
	}

	public void setConnectedLineIdentityRequestIndicator(boolean connectedLineIdentityRequestIndicator) {
		this.connectedLineIdentityRequestIndicator = connectedLineIdentityRequestIndicator;
	}

}
