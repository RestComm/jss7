/**
 * Start time:08:42:25 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.MLPPPrecedence;

/**
 * Start time:08:42:25 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MLPPPrecedenceImpl extends AbstractISUPParameter implements MLPPPrecedence {

	private int lfb;
	private int precedenceLevel;
	private int mllpServiceDomain;
	// FIXME: ensure zero in first digit.?
	private byte[] niDigits;

	public MLPPPrecedenceImpl() {
		super();
		
	}

	public MLPPPrecedenceImpl(byte[] b) throws ParameterException {
		super();
		decode(b);
	}

	public MLPPPrecedenceImpl(byte lfb, byte precedenceLevel, int mllpServiceDomain, byte[] niDigits) {
		super();
		this.lfb = lfb;
		this.precedenceLevel = precedenceLevel;
		this.mllpServiceDomain = mllpServiceDomain;
		setNiDigits(niDigits);
	}

	public int decode(byte[] b) throws ParameterException {
		if (b == null || b.length != 6) {
			throw new ParameterException("byte[] must  not be null and length must  be 6");
		}

		this.precedenceLevel = (byte) (b[0] & 0x0F);
		this.lfb = (byte) ((b[0] >> 5) & 0x03);
		byte v = 0;
		this.niDigits = new byte[4];
		for (int i = 0; i < 2; i++) {
			v = 0;
			v = b[i + 1];
			this.niDigits[i * 2] = (byte) (v & 0x0F);
			this.niDigits[i * 2 + 1] = (byte) ((v >> 4) & 0x0F);
		}

		this.mllpServiceDomain = b[3] << 16;
		this.mllpServiceDomain |= b[4] << 8;
		this.mllpServiceDomain |= b[5];
		return 6;
	}

	public byte[] encode() throws ParameterException {
		byte[] b = new byte[6];
		b[0] = (byte) ((this.lfb & 0x03) << 5);
		b[0] |= this.precedenceLevel & 0x0F;
		byte v = 0;
		for (int i = 0; i < 2; i++) {
			v = 0;

			v |= (this.niDigits[i * 2] & 0x0F) << 4;
			v |= (this.niDigits[i * 2 + 1] & 0x0F);

			b[i + 1] = v;
		}

		b[3] = (byte) (this.mllpServiceDomain >> 16);
		b[4] = (byte) (this.mllpServiceDomain >> 8);
		b[5] = (byte) this.mllpServiceDomain;
		return b;
	}

	public byte getLfb() {
		return (byte) lfb;
	}

	public void setLfb(byte lfb) {
		this.lfb = lfb;
	}

	public byte getPrecedenceLevel() {
		return (byte) precedenceLevel;
	}

	public void setPrecedenceLevel(byte precedenceLevel) {
		this.precedenceLevel = precedenceLevel;
	}

	public int getMllpServiceDomain() {
		return mllpServiceDomain;
	}

	public void setMllpServiceDomain(int mllpServiceDomain) {
		this.mllpServiceDomain = mllpServiceDomain;
	}

	public byte[] getNiDigits() {
		return niDigits;
	}

	public void setNiDigits(byte[] niDigits) {
		if (niDigits == null || niDigits.length != 4) {
			throw new IllegalArgumentException();
		}
		this.niDigits = niDigits;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
