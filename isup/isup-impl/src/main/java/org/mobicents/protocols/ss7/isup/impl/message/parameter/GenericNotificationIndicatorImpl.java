/**
 * Start time:13:44:22 2009-03-31<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNotificationIndicator;

/**
 * Start time:13:44:22 2009-03-31<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class GenericNotificationIndicatorImpl extends AbstractParameter implements GenericNotificationIndicator {

	private int[] notificationIndicator;

	public GenericNotificationIndicatorImpl(byte[] b) throws ParameterRangeInvalidException {
		super();
		decodeElement(b);
	}

	public GenericNotificationIndicatorImpl() {
		super();
		
	}

	public GenericNotificationIndicatorImpl(int[] notificationIndicator) {
		super();
		this.setNotificationIndicator(notificationIndicator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */
	public int decodeElement(byte[] b) throws ParameterRangeInvalidException {
		if (b == null || b.length < 2) {
			throw new ParameterRangeInvalidException("byte[] must  not be null and length must be 1 or greater");
		}

		this.notificationIndicator = new int[b.length];

		for (int i = 0; i < b.length; i++) {
			int extFlag = (b[i] >> 7) & 0x01;
			if (extFlag == 0x01 && (b.length - 1 > i)) {
				throw new ParameterRangeInvalidException("Extenstion flag idnicates end of data, however byte[] has more octets. Index: " + i);
			}
			this.notificationIndicator[i] = b[i] & 0x7F;
		}
		return this.notificationIndicator.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {
		byte[] b = new byte[this.notificationIndicator.length];

		for (int index = 0; index < b.length; index++) {
			b[index] = (byte) (this.notificationIndicator[index] & 0x7F);
		}

		// sets extension bit to show that we dont have more octets
		b[b.length - 1] |= 1 << 7;

		return b;
	}

	public int[] getNotificationIndicator() {
		return notificationIndicator;
	}

	public void setNotificationIndicator(int[] notificationIndicator) {
		if (notificationIndicator == null) {
			throw new IllegalArgumentException("Notification indicator must not be null");
		}
		this.notificationIndicator = notificationIndicator;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
