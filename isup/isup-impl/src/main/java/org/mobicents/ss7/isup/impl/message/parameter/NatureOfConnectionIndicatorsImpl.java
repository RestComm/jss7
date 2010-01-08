/**
 * Start time:09:12:26 2009-04-02<br>
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
import org.mobicents.ss7.isup.message.parameter.NatureOfConnectionIndicators;

/**
 * Start time:09:12:26 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class NatureOfConnectionIndicatorsImpl extends AbstractParameter implements NatureOfConnectionIndicators {

	private static final int _TURN_ON = 1;
	private static final int _TURN_OFF = 0;

	private int satelliteIndicator = 0;
	private int continuityCheckIndicator = 0;
	private boolean echoControlDeviceIndicator = false;

	public NatureOfConnectionIndicatorsImpl(byte[] b) throws ParameterRangeInvalidException {
		super();
		decodeElement(b);
	}

	public NatureOfConnectionIndicatorsImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NatureOfConnectionIndicatorsImpl(byte satelliteIndicator, byte continuityCheckIndicator, boolean echoControlDeviceIndicator) {
		super();
		this.satelliteIndicator = satelliteIndicator;
		this.continuityCheckIndicator = continuityCheckIndicator;
		this.echoControlDeviceIndicator = echoControlDeviceIndicator;
	}

	public int decodeElement(byte[] b) throws ParameterRangeInvalidException {
		if (b == null || b.length != 1) {
			throw new ParameterRangeInvalidException("byte[] must not be null and must have length of 1");
		}
		this.satelliteIndicator = (byte) (b[0] & 0x03);
		this.continuityCheckIndicator = (byte) ((b[0] >> 2) & 0x03);
		this.echoControlDeviceIndicator = ((b[0] >> 4) == _TURN_ON);

		return 1;
	}

	public byte[] encodeElement() throws IOException {

		int b0 = 0;
		b0 = this.satelliteIndicator & 0x03;
		b0 |= (this.continuityCheckIndicator & 0x03) << 2;
		b0 |= (this.echoControlDeviceIndicator ? _TURN_ON : _TURN_OFF) << 4;
		return new byte[] { (byte) b0 };
	}

	@Override
	public int encodeElement(ByteArrayOutputStream bos) throws IOException {
		byte[] b = this.encodeElement();
		bos.write(b);
		return b.length;

	}

	public int getSatelliteIndicator() {
		return satelliteIndicator;
	}

	public void setSatelliteIndicator(int satelliteIndicator) {
		this.satelliteIndicator = satelliteIndicator & 0x03;
	}

	public int getContinuityCheckIndicator() {
		return continuityCheckIndicator;
	}

	public void setContinuityCheckIndicator(int continuityCheckIndicator) {
		this.continuityCheckIndicator = continuityCheckIndicator & 0x03;
	}

	public boolean isEchoControlDeviceIndicator() {
		return echoControlDeviceIndicator;
	}

	public void setEchoControlDeviceIndicator(boolean echoControlDeviceIndicator) {
		this.echoControlDeviceIndicator = echoControlDeviceIndicator;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
