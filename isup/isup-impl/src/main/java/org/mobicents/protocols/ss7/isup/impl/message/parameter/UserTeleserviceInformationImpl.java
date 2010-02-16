/**
 * Start time:12:43:13 2009-04-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.parameter.UserTeleserviceInformation;

/**
 * Start time:12:43:13 2009-04-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class UserTeleserviceInformationImpl extends AbstractParameter implements UserTeleserviceInformation {

	private int codingStandard;
	private int interpretation;
	private int presentationMethod;
	private int highLayerCharIdentification;

	// I hate this, its soo jsr 17 style...
	private boolean eHighLayerCharIdentificationPresent;
	private boolean eVidedoTelephonyCharIdentificationPresent;

	private int eHighLayerCharIdentification;
	private int eVidedoTelephonyCharIdentification;

	public UserTeleserviceInformationImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserTeleserviceInformationImpl(int codingStandard, int interpretation, int presentationMethod, int highLayerCharIdentification) {
		super();
		this.setCodingStandard(codingStandard);
		this.setInterpretation(interpretation);
		this.setPresentationMethod(presentationMethod);
		this.setHighLayerCharIdentification(highLayerCharIdentification);
	}

	public UserTeleserviceInformationImpl(int codingStandard, int interpretation, int presentationMethod, int highLayerCharIdentification, int eVidedoTelephonyCharIdentification,
			boolean notImportantIgnoredParameter) {
		super();
		this.setCodingStandard(codingStandard);
		this.setInterpretation(interpretation);
		this.setPresentationMethod(presentationMethod);
		this.setHighLayerCharIdentification(highLayerCharIdentification);
		setEVidedoTelephonyCharIdentification(eVidedoTelephonyCharIdentification);
	}

	public UserTeleserviceInformationImpl(int codingStandard, int interpretation, int presentationMethod, int highLayerCharIdentification, int eHighLayerCharIdentification) {
		super();
		this.setCodingStandard(codingStandard);
		this.setInterpretation(interpretation);
		this.setPresentationMethod(presentationMethod);
		this.setHighLayerCharIdentification(highLayerCharIdentification);
		this.setEHighLayerCharIdentification(eHighLayerCharIdentification);
	}

	public UserTeleserviceInformationImpl(byte[] b) throws ParameterRangeInvalidException {
		super();
		// FIXME: this is only elementID

		decodeElement(b);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */
	public int decodeElement(byte[] b) throws ParameterRangeInvalidException {
		if (b == null || b.length < 2) {
			throw new ParameterRangeInvalidException("byte[] must not be null and length must be greater than  1");
		}

		try {
			this.setPresentationMethod(b[0]);
			this.setInterpretation((b[0] >> 2));
			this.setCodingStandard((b[0] >> 5));
			this.setHighLayerCharIdentification(b[1]);
		} catch (Exception e) {
			throw new ParameterRangeInvalidException(e);
		}
		boolean ext = ((b[1] >> 7) & 0x01) == 0;
		if (ext && b.length != 3) {
			throw new ParameterRangeInvalidException("byte[] indicates extension to high layer cahracteristic indicator, however there isnt enough bytes");
		}
		if (!ext) {
			// ??
			return b.length;
		}

		// FIXME: add check for excesive byte?, it should not happen though?
		if (this.highLayerCharIdentification == _HLCI_MAINTAINENCE || this.highLayerCharIdentification == _HLCI_MANAGEMENT) {
			this.setEHighLayerCharIdentification(b[2] & 0x7F);
		} else if ((this.highLayerCharIdentification >= _HLCI_AUDIO_VID_LOW_RANGE && this.highLayerCharIdentification <= _HLCI_AUDIO_VID_HIGH_RANGE)
				|| (this.highLayerCharIdentification >= _HLCI_AUDIO_VID_LOW_RANGE2 && this.highLayerCharIdentification <= _HLCI_AUDIO_VID_HIGH_RANGE2)) {
			this.setEVidedoTelephonyCharIdentification(b[2] & 0x7F);
		} else {
			logger.warning("HLCI indicates value which does not allow for extension, but its present....");
		}
		return b.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {
		byte[] b = null;
		if (this.eHighLayerCharIdentificationPresent || this.eVidedoTelephonyCharIdentificationPresent) {
			b = new byte[3];
		} else {
			b = new byte[2];
		}

		int v = 0;
		v = this.presentationMethod & 0x03;
		v |= (this.interpretation & 0x07) << 2;
		v |= (this.codingStandard & 0x03) << 5;
		v |= 0x80;

		b[0] = (byte) v;
		b[1] = (byte) (this.highLayerCharIdentification & 0x7F);
		if (this.eHighLayerCharIdentificationPresent || this.eVidedoTelephonyCharIdentificationPresent) {

			if (this.eHighLayerCharIdentificationPresent) {
				b[2] = (byte) (0x80 | (this.eHighLayerCharIdentification & 0x7F));
			} else {
				b[2] = (byte) (0x80 | (this.eVidedoTelephonyCharIdentification & 0x7F));
			}
			return b;
		} else {
			b[1] |= 0x80;
			return b;
		}
	}

	public int getCodingStandard() {
		return codingStandard;
	}

	public void setCodingStandard(int codingStandard) {
		this.codingStandard = codingStandard & 0x03;
	}

	public int getInterpretation() {
		return interpretation;
	}

	public void setInterpretation(int interpretation) {
		this.interpretation = interpretation & 0x07;
	}

	public int getPresentationMethod() {
		return presentationMethod;
	}

	public void setPresentationMethod(int presentationMethod) {
		this.presentationMethod = presentationMethod & 0x03;
	}

	public int getHighLayerCharIdentification() {
		return highLayerCharIdentification;
	}

	public void setHighLayerCharIdentification(int highLayerCharIdentification) {
		this.highLayerCharIdentification = highLayerCharIdentification & 0x7F;
	}

	public int getEHighLayerCharIdentification() {
		return eHighLayerCharIdentification;
	}

	public void setEHighLayerCharIdentification(int highLayerCharIdentification) {

		if (this.eVidedoTelephonyCharIdentificationPresent) {
			throw new IllegalStateException("Either Extended VideoTlephony or Extended HighLayer octet is set. ExtendedVideoTlephony is already present");
		}
		if (this.highLayerCharIdentification == _HLCI_MAINTAINENCE || this.highLayerCharIdentification == _HLCI_MANAGEMENT) {
			this.eHighLayerCharIdentificationPresent = true;
			this.eHighLayerCharIdentification = highLayerCharIdentification & 0x7F;
		} else {
			throw new IllegalArgumentException("Can not set this octet - HLCI is of value: " + this.highLayerCharIdentification);
		}
	}

	public int getEVidedoTelephonyCharIdentification() {
		return eVidedoTelephonyCharIdentification;
	}

	public void setEVidedoTelephonyCharIdentification(int eVidedoTelephonyCharIdentification) {
		if (this.eHighLayerCharIdentificationPresent) {
			throw new IllegalStateException("Either Extended VideoTlephony or Extended HighLayer octet is set. ExtendedHighLayer is already present");
		}
		if ((this.highLayerCharIdentification >= _HLCI_AUDIO_VID_LOW_RANGE && this.highLayerCharIdentification <= _HLCI_AUDIO_VID_HIGH_RANGE)
				|| (this.highLayerCharIdentification >= _HLCI_AUDIO_VID_LOW_RANGE2 && this.highLayerCharIdentification <= _HLCI_AUDIO_VID_HIGH_RANGE2)) {
			this.eVidedoTelephonyCharIdentificationPresent = true;
			this.eVidedoTelephonyCharIdentification = eVidedoTelephonyCharIdentification & 0x7F;
		} else {
			throw new IllegalArgumentException("Can not set this octet - HLCI is of value: " + this.highLayerCharIdentification);
		}
	}

	public boolean isEHighLayerCharIdentificationPresent() {
		return eHighLayerCharIdentificationPresent;
	}

	public boolean isEVidedoTelephonyCharIdentificationPresent() {
		return eVidedoTelephonyCharIdentificationPresent;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
