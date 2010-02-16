/**
 * Start time:14:48:06 2009-03-30<br>
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
import org.mobicents.protocols.ss7.isup.message.parameter.CallReference;

/**
 * Start time:14:48:06 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 */
public class CallReferenceImpl extends AbstractParameter implements CallReference{

	
	private int callIdentity = 0;
	//Should we use here SignalingPointCode class?
	private int signalingPointCode = 0;

	public CallReferenceImpl(byte[] b) throws ParameterRangeInvalidException {
		super();
		decodeElement(b);
	}

	public CallReferenceImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CallReferenceImpl(int callIdentity, int signalingPointCode) {
		super();
		this.callIdentity = callIdentity;
		this.signalingPointCode = signalingPointCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */
	public int decodeElement(byte[] b) throws ParameterRangeInvalidException {
		if (b == null || b.length != 5) {
			throw new ParameterRangeInvalidException("byte[] must not be null or have length of 5");
		}
//		for (int i = 0; i < 3; i++) {
//			this.callIdentity |= (b[i] << (i * 8));
//		}
		this.callIdentity = ( (b[0] & 0xFF)  << 16 )  | ((b[1]& 0xFF) << 8) | (b[2] & 0xFF) ;
//		this.signalingPointCode |= b[3];
//		this.signalingPointCode |= (b[4] << 8);
		this.signalingPointCode = ((b[3]& 0xFF) | ((b[4] & 0x3F) << 8));
		// This kills first two bits.
		//this.signalingPointCode &= 0x3FFF;
		return 5;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {
		byte[] b = new byte[5];

//		for (int i = 0; i < 3; i++) {
//			b[i] = (byte) (this.callIdentity >> (i * 8));
//
//		}
		
		b[0]=(byte) (this.callIdentity>>16);
		b[1]=(byte) (this.callIdentity>>8);
		b[2]=(byte) this.callIdentity;
		
		b[3] = (byte) this.signalingPointCode;
		b[4] = (byte) ((this.signalingPointCode >> 8) & 0x3F);

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

	public int getCallIdentity() {
		return callIdentity;
	}

	public void setCallIdentity(int callIdentity) {
		this.callIdentity = callIdentity & 0xFFFFFF;
	}

	public int getSignalingPointCode() {
		return signalingPointCode;
	}

	public void setSignalingPointCode(int signalingPointCode) {
		this.signalingPointCode = signalingPointCode;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
