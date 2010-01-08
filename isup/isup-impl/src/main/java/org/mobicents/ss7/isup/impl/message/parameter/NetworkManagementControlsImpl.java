/**
 * Start time:12:02:43 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.ss7.isup.message.parameter.NetworkManagementControls;

/**
 * Start time:12:02:43 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class NetworkManagementControlsImpl extends AbstractParameter implements NetworkManagementControls {

	private static final int _TURN_ON = 1;
	private static final int _TURN_OFF = 0;
	// FIXME - should we switch to boolean[] - its a slight perf loss :P
	private byte[] networkManagementControls = null;

	public NetworkManagementControlsImpl(byte[] b) throws ParameterRangeInvalidException {
		super();
		decodeElement(b);
	}

	public NetworkManagementControlsImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */
	public int decodeElement(byte[] b) throws ParameterRangeInvalidException {
		try {
			setNetworkManagementControls(b);
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

		for (int index = 0; index < this.networkManagementControls.length; index++) {
			this.networkManagementControls[index] = (byte) (this.networkManagementControls[index] & 0x01);
		}

		this.networkManagementControls[this.networkManagementControls.length - 1] = (byte) ((this.networkManagementControls[this.networkManagementControls.length - 1]) | (0x01 << 7));
		return this.networkManagementControls;
	}

	public boolean isTARControlEnabled(byte b) {
		return (b & 0x01) == _TURN_ON;
	}

	public byte createTAREnabledByte(boolean enabled) {
		return (byte) (enabled ? _TURN_ON : _TURN_OFF);
	}

	public byte[] getNetworkManagementControls() {
		return networkManagementControls;
	}

	public void setNetworkManagementControls(byte[] networkManagementControls) throws IllegalArgumentException {
		if (networkManagementControls == null || networkManagementControls.length == 0) {
			throw new IllegalArgumentException("byte[] must not be null and length must be greater than 0");
		}
		this.networkManagementControls = networkManagementControls;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
