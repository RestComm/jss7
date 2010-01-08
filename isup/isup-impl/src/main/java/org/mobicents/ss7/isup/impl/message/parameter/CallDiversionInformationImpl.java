/**
 * Start time:14:32:32 2009-03-30<br>
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
import org.mobicents.ss7.isup.message.parameter.CallDiversionInformation;

/**
 * Start time:14:32:32 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * 
 */
public class CallDiversionInformationImpl extends AbstractParameter implements CallDiversionInformation {

	private int redirectingReason = 0;

	private int notificationSubscriptionOptions = 0;

	public CallDiversionInformationImpl(int notificationSubscriptionOptions, int redirectingReason) {
		super();
		this.notificationSubscriptionOptions = notificationSubscriptionOptions;
		this.redirectingReason = redirectingReason;
	}

	public CallDiversionInformationImpl() throws ParameterRangeInvalidException {

	}

	public CallDiversionInformationImpl(byte[] b) throws ParameterRangeInvalidException {
		decodeElement(b);
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

		int v = b[0];
		this.notificationSubscriptionOptions = v & 0x07;
		this.redirectingReason = (v >> 3) & 0x0F;

		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {
		byte[] b = new byte[1];

		int v = 0;
		v |= this.notificationSubscriptionOptions & 0x07;
		v |= (this.redirectingReason & 0x0F) << 3;
		b[0] = (byte) v;
		return b;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.isup.ISUPComponent#encodeElement(java.io.ByteArrayOutputStream
	 * )
	 */
	public int encodeElement(ByteArrayOutputStream bos) throws IOException {
		byte[] b = this.encodeElement();
		bos.write(b);
		return b.length;
	}

	public int getNotificationSubscriptionOptions() {
		return notificationSubscriptionOptions;
	}

	public void setNotificationSubscriptionOptions(int notificationSubscriptionOptions) {
		this.notificationSubscriptionOptions = notificationSubscriptionOptions;
	}

	public int getRedirectingReason() {
		return redirectingReason;
	}

	public void setRedirectingReason(int redirectingReason) {
		this.redirectingReason = redirectingReason;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
