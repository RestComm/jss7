/*
 * The Java Call Control API for CAMEL 2
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */

package org.mobicents.ss7.sccp.impl.parameter;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import org.mobicents.ss7.sccp.parameter.GlobalTitle;


/**
 *
 * @author Oleg Kulikov
 */
public class GT0100Impl implements GlobalTitle {
    
	//FIXME: check if there is common part for all GT
    private int translationType;
    private int numberingPlan;
    private int encodingScheme;
    private int natureOfAddress;
    private String digits = "";
    
    /** Creates a new instance of GT0100 */
    public GT0100Impl() {
    	
    }
    
    public GT0100Impl(int translationType, int numberingPlan,
            int encodingScheme, int natureOfAddress, String digits) {
        this.translationType = translationType;
        this.numberingPlan = numberingPlan;
        this.encodingScheme = encodingScheme;
        this.natureOfAddress = natureOfAddress;
        this.digits = digits;
    }
    
    public void decode(InputStream in) throws IOException {
        int b1 = in.read() & 0xff;
        int b2 = in.read() & 0xff;
        int b3 = in.read() & 0xff;
        
        translationType = b1;
        numberingPlan = (b2 & 0xf0) >> 4;
        encodingScheme = (b2 & 0x0f);
        natureOfAddress = b3 & 0x3f;
        
        while (in.available() > 1) {
            int b = in.read() & 0xff;
            
            digits += Integer.toHexString(b & 0x0f) +
                    Integer.toHexString((b & 0xf0) >> 4);
        }
        
        int b = in.read() & 0xff;
        digits += Integer.toHexString((b & 0x0f));
        
        if (encodingScheme != 1) {
            digits += Integer.toHexString((b & 0xf0) >> 4);
        }
    }
    
    public void encode(OutputStream out) throws IOException {
        out.write((byte)translationType);
        
        byte b = (byte)((numberingPlan << 4) | (encodingScheme));
        out.write(b);
        
        b = (byte)(natureOfAddress & 0x3f);
        out.write(b);
        
        int count = encodingScheme != 1 ? digits.length() : digits.length() - 1;
        for (int i = 0; i < count - 1; i += 2) {
            String ds1 = digits.substring(i, i + 1);
            String ds2 = digits.substring(i + 1, i + 2);
            
            int d1 = Integer.parseInt(ds1,16);
            int d2 = Integer.parseInt(ds2, 16);
            
            b = (byte) (d2 << 4 | d1);
            out.write(b);
        }
        
        if (encodingScheme == 1) {
            String ds1 = digits.substring(count, count + 1);
            int d = Integer.parseInt(ds1);
            
            b = (byte)(d & 0x0f);
            out.write(b);
        }
    }
    
    
    public String toString() {
        StringBuffer msg = new StringBuffer();
        msg.append("Global Title includes translation type, numbering plan, " +
                "encoding scheme, nature of address indicator\n");
        msg.append(digits);
        return msg.toString();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((digits == null) ? 0 : digits.hashCode());
		result = prime * result + encodingScheme;
		result = prime * result + natureOfAddress;
		result = prime * result + numberingPlan;
		result = prime * result + translationType;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GT0100Impl other = (GT0100Impl) obj;
		if (digits == null) {
			if (other.digits != null)
				return false;
		} else if (!digits.equals(other.digits))
			return false;
		if (encodingScheme != other.encodingScheme)
			return false;
		if (natureOfAddress != other.natureOfAddress)
			return false;
		if (numberingPlan != other.numberingPlan)
			return false;
		if (translationType != other.translationType)
			return false;
		return true;
	}

	//FIXME: add bit trims with &
	
	public int getTranslationType() {
		return translationType;
	}

	public void setTranslationType(int translationType) {
		this.translationType = translationType;
	}

	public int getNumberingPlan() {
		return numberingPlan;
	}

	public void setNumberingPlan(int numberingPlan) {
		this.numberingPlan = numberingPlan;
	}

	public int getEncodingScheme() {
		return encodingScheme;
	}

	public void setEncodingScheme(int encodingScheme) {
		this.encodingScheme = encodingScheme;
	}

	public int getNatureOfAddress() {
		return natureOfAddress;
	}

	public void setNatureOfAddress(int natureOfAddress) {
		this.natureOfAddress = natureOfAddress;
	}

	public String getDigits() {
		return digits;
	}

	public void setDigits(String digits) {
		this.digits = digits;
	}
    
}
