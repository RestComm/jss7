/**
 * Start time:14:44:16 2009-04-02<br>
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
import org.mobicents.protocols.ss7.isup.message.parameter.RangeAndStatus;

/**
 * Start time:14:44:16 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class RangeAndStatusImpl extends AbstractParameter implements RangeAndStatus {

	private byte range;
	private byte[] status;

	// FIXME:
	// private Status[] status = null;

	public RangeAndStatusImpl(byte[] b) throws ParameterRangeInvalidException {
		super();
		if(b.length<1)
		{
			throw new ParameterRangeInvalidException("RangeAndStatus requires atleast 1 byte.");
		}
		decodeElement(b);
		
	}

	public RangeAndStatusImpl() {
		super();

	}

	public RangeAndStatusImpl(byte range, byte[] status) {
		super();
		this.range = range;
		setStatus(status);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */
	public int decodeElement(byte[] b) throws ParameterRangeInvalidException {

		this.range = b[0];
		if (b.length == 1)
			return 1;
		this.status = new byte[b.length - 1];
		System.arraycopy(b, 1, this.status, 0, this.status.length);

		return b.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {
		try {
			checkData(range,status);
		} catch (ParameterRangeInvalidException e) {
			//FIXME: akward
			e.printStackTrace();
			throw new IOException(e.toString());
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bos.write(this.range);
		if (this.status != null)
			bos.write(this.status);
		return bos.toByteArray();
	}

	public byte getRange() {
		return range;
	}

	public void setRange(byte range) {
		this.setRange(range, false);
	}

	public void setRange(byte range, boolean addStatus) {
		this.range = range;
		// range tells how much cics are affected, or potentially affected.
		// statys field contains bits(1|0) to indicate
		if (addStatus) {
			// check len of byte, +1, for cic in message.
			int len = (range + 1) / 8;
			if ((range + 1) % 8 != 0) {
				len++;
			}
			this.status = new byte[len];
		}
	}

	public byte[] getStatus() {
		return status;
	}

	public void setStatus(byte[] status) {
		this.status = status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.isup.message.parameter.RangeAndStatus#isAffected
	 * (byte)
	 */
	public boolean isAffected(byte b) throws IllegalArgumentException {
		if (this.status.length < (b / 8)) {
			throw new IllegalArgumentException("Argument exceeds status!");
		}
		int index_l = (b / 8) ;
		
		int index = b % 8; // number of bit to lit... ech
		int n2Pattern = (int) Math.pow(2, index); // hmm no int pows... sucks
		return (this.status[index_l] & n2Pattern) > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.isup.message.parameter.RangeAndStatus#setAffected
	 * (byte, boolean)
	 */
	public void setAffected(byte subrange, boolean v) throws IllegalArgumentException {
		// ceck
		if (this.status.length < (subrange / 8)) {
			throw new IllegalArgumentException("Argument exceeds status!");
		}
		int index_l = (subrange / 8) ; 
		int index = subrange % 8; // number of bit to lit... ech
		int n2Pattern = (int) Math.pow(2, index); // hmm no int pows... sucks

		if (v) {
			this.status[index_l] |= n2Pattern;
		} else {
			// not, we have to inverse pattern...
			n2Pattern = 0xFF ^ n2Pattern; // this will create bits with zeros in place of n2Pattern ones!
			this.status[index_l] &= n2Pattern; // do logical and, this will kill proper bit and leave rest unchanged
		}
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}

	
	
	private static void checkData(byte range, byte[] status) throws ParameterRangeInvalidException
	{
		//FIXME: add checks specific to messages~!
		if(status!=null)
		{
	
			int len = (range + 1) / 8;
			if ((range + 1) % 8 != 0) {
				len++;
			}
			if(status.length!=len)
			{
				throw new ParameterRangeInvalidException("Wrong length of status part: "+status.length+", range: "+range);
			}
		}else
		{
			//there are cases when this can be null;
		}
	}
	
	public static void main(String[] args) {
		System.err.println(0xFF ^ 0x80);

	}
	
	
}
