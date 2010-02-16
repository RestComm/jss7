/**
 * Start time:11:25:13 2009-03-31<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.parameter.EventInformation;

/**
 * Start time:11:25:13 2009-03-31<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class EventInformationImpl extends AbstractParameter implements EventInformation {

	private final static int _TURN_ON = 1;
	private final static int _TURN_OFF = 0;

	private int eventIndicator;
	private boolean eventPresentationRestrictedIndicator;

	public EventInformationImpl(byte[] b) throws ParameterRangeInvalidException {
		super();
		decodeElement(b);
	}

	public EventInformationImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EventInformationImpl(int eventIndicator) {
		super();
		this.eventIndicator = eventIndicator;
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

		this.eventIndicator = b[0] & 0x7F;
		this.eventPresentationRestrictedIndicator = ((b[0] >> 7) & 0x01) == _TURN_ON;

		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {
		byte[] b = new byte[] { (byte) (this.eventIndicator & 0x7F) };

		b[0] |= (byte) ((this.eventPresentationRestrictedIndicator ? _TURN_ON : _TURN_OFF) << 7);
		return b;
	}

	public int getEventIndicator() {
		return eventIndicator;
	}

	public void setEventIndicator(int eventIndicator) {
		this.eventIndicator = eventIndicator;
	}

	public boolean isEventPresentationRestrictedIndicator() {
		return eventPresentationRestrictedIndicator;
	}

	public void setEventPresentationRestrictedIndicator(boolean eventPresentationRestrictedIndicator) {
		this.eventPresentationRestrictedIndicator = eventPresentationRestrictedIndicator;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
