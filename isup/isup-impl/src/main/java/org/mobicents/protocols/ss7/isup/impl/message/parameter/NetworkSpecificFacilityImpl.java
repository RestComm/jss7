/**
 * Start time:09:37:50 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.parameter.NetworkSpecificFacility;

/**
 * Start time:09:37:50 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class NetworkSpecificFacilityImpl extends AbstractParameter implements NetworkSpecificFacility {

	/**
	 * This tells us to include byte 1a - sets lengthOfNetworkIdentification to
	 * 1+networkdIdentification.length
	 */
	private boolean includeNetworkIdentification;

	private int lengthOfNetworkIdentification;
	private int typeOfNetworkIdentification;
	private int networkIdentificationPlan;
	// FIXME: ext bit: indicated as to be used as in 3.25 but on specs id
	// different...
	private byte[] networkIdentification;
	private byte[] networkSpecificaFacilityIndicator;

	public NetworkSpecificFacilityImpl(byte[] b) throws ParameterRangeInvalidException {
		super();
		decodeElement(b);
	}

	public NetworkSpecificFacilityImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NetworkSpecificFacilityImpl(boolean includeNetworkIdentification, byte typeOfNetworkIdentification, byte networkdIdentificationPlan, byte[] networkdIdentification,
			byte[] networkSpecificaFacilityIndicator) {
		super();
		this.includeNetworkIdentification = includeNetworkIdentification;
		this.typeOfNetworkIdentification = typeOfNetworkIdentification;
		this.networkIdentificationPlan = networkdIdentificationPlan;
		this.networkIdentification = networkdIdentification;
		this.networkSpecificaFacilityIndicator = networkSpecificaFacilityIndicator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */
	public int decodeElement(byte[] b) throws ParameterRangeInvalidException {
		if (b == null || b.length < 1) {
			throw new ParameterRangeInvalidException("byte[] must nto be null or have length greater than 1");
		}
		// try {
		int shift = 0;
		this.lengthOfNetworkIdentification = b[shift++];

		// FIXME: We ignore ext bit, we dont need it ? ?????
		this.typeOfNetworkIdentification = (byte) ((b[shift] >> 4) & 0x07);
		this.networkIdentificationPlan = (byte) (b[shift] & 0x0F);
		shift++;
		if (this.lengthOfNetworkIdentification > 0) {

			byte[] _networkId = new byte[this.lengthOfNetworkIdentification];
			for (int i = 0; i < this.lengthOfNetworkIdentification; i++, shift++) {

				_networkId[i] = (byte) (b[shift] | 0x80);
			}

			// now lets set it.
			if (_networkId.length > 0) {

				_networkId[_networkId.length - 1] = (byte) (_networkId[_networkId.length - 1] & 0x7F);
			}

			this.setNetworkIdentification(_networkId);
		}

		if (shift + 1 == b.length) {
			throw new ParameterRangeInvalidException("There is no facility indicator. This part is mandatory!!!");
		}
		byte[] _facility = new byte[b.length - shift - 1];
		// -1 cause shift counts from 0
		System.arraycopy(b, shift, _facility, 0, b.length - shift - 1);
		this.setNetworkSpecificaFacilityIndicator(_facility);
		return b.length;
		// } catch (ArrayIndexOutOfBoundsException aioobe) {
		// throw new IllegalArgumentException("Failed to parse due to: ",
		// aioobe);
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		bos.write(this.lengthOfNetworkIdentification);
		// This should always be set to true if there is network ID
		if (this.includeNetworkIdentification) {
			int b1 = 0;
			b1 = ((this.typeOfNetworkIdentification & 0x07) << 4);
			b1 |= (this.networkIdentificationPlan & 0x0F);

			if (this.networkIdentification != null && this.networkIdentification.length > 0) {
				b1 |= 0x80;
				bos.write(b1);
				for (int index = 0; index < this.networkIdentification.length; index++) {
					if (index == this.networkIdentification.length - 1) {

						bos.write(this.networkIdentification[index] & 0x7F);

					} else {
						bos.write(this.networkIdentification[index] | (0x01 << 7));

					}
				}
			} else {
				bos.write(b1 & 0x7F);
			}
		}

		if (this.networkSpecificaFacilityIndicator == null) {
			throw new IllegalArgumentException("Network Specific Facility must not be null");
		}
		bos.write(this.networkSpecificaFacilityIndicator);

		return bos.toByteArray();
	}

	public boolean isIncludeNetworkIdentification() {
		return includeNetworkIdentification;
	}

	public int getLengthOfNetworkIdentification() {
		return lengthOfNetworkIdentification;
	}

	public int getTypeOfNetworkIdentification() {
		return typeOfNetworkIdentification;
	}

	public void setTypeOfNetworkIdentification(byte typeOfNetworkIdentification) {
		this.typeOfNetworkIdentification = typeOfNetworkIdentification;
	}

	public int getNetworkIdentificationPlan() {
		return networkIdentificationPlan;
	}

	public void setNetworkIdentificationPlan(byte networkdIdentificationPlan) {
		this.networkIdentificationPlan = networkdIdentificationPlan;
	}

	public byte[] getNetworkIdentification() {
		return networkIdentification;
	}

	public void setNetworkIdentification(byte[] networkdIdentification) {

		if (networkdIdentification != null && networkdIdentification.length > Byte.MAX_VALUE * 2 - 1) {
			throw new IllegalArgumentException("Length of Network Identification part must not be greater than: " + (Byte.MAX_VALUE * 2 - 1));
		}

		this.networkIdentification = networkdIdentification;
		this.includeNetworkIdentification = true;

	}

	public byte[] getNetworkSpecificaFacilityIndicator() {
		return networkSpecificaFacilityIndicator;
	}

	public void setNetworkSpecificaFacilityIndicator(byte[] networkSpecificaFacilityIndicator) {
		this.networkSpecificaFacilityIndicator = networkSpecificaFacilityIndicator;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
