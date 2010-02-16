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

package org.mobicents.protocols.ss7.sccp.impl.parameter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;



/**
 *
 * @author Oleg Kulikov
 */
public class SccpAddressImpl implements SccpAddress {
    
    protected int pointCodeIndicator;
    protected int ssnIndicator;
    protected int globalTitleIndicator;
    protected int routingIndicator;
    
    protected int signalingPointCode;
    protected int ssn;
    protected GlobalTitle globalTitle;
    
    /** Creates a new instance of UnitDataMandatoryVariablePart */
    public SccpAddressImpl() {
    }
    
    public SccpAddressImpl(int pointCodeIndicator,
            int ssnIndicator, int gtIndicator, int routingIndicator,
            int signalingPointCode, int ssn, GlobalTitle globalTitle) {
        
        this.pointCodeIndicator = pointCodeIndicator;
        this.ssnIndicator = ssnIndicator;
        this.globalTitleIndicator = gtIndicator;
        this.routingIndicator = routingIndicator;
        this.signalingPointCode = signalingPointCode;
        this.ssn = ssn;
        this.globalTitle = globalTitle;
    }
    
    
    public void decode(byte[] buffer) throws IOException {
        DataInputStream in = new DataInputStream(
                new ByteArrayInputStream(buffer));
        
        int i = in.readUnsignedByte();
        
        pointCodeIndicator = i & 0x01;
        ssnIndicator = (i & 0x02) >> 1;
        globalTitleIndicator = (i & 0x3c) >> 2;
        routingIndicator = (i & 0x40) >> 6;
        
        if (pointCodeIndicator == 1) {
            int b1 = in.readUnsignedByte();
            int b2 = in.readUnsignedByte();
            
            signalingPointCode = ((b2 & 0x3f) << 8) | b1;
        }
        
        if (ssnIndicator == 1) {
            ssn = in.readUnsignedByte();
        }
        
        switch (globalTitleIndicator) {
            case 4 :
                globalTitle = new GT0100Impl();
                break;
        }
        if(globalTitle!=null)
        	globalTitle.decode(in);
    }
    
    public byte[] encode() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        byte b = (byte)(pointCodeIndicator | ssnIndicator << 1 |
                globalTitleIndicator << 2 | routingIndicator << 6);
        out.write(b);
        
        if (pointCodeIndicator == 1) {
            byte b1 = (byte) signalingPointCode;
            byte b2 = (byte) ((signalingPointCode >> 8) & 0x3f);
            
            out.write(b1);
            out.write(b2);
        }
        
        if (ssnIndicator == 1) {
            out.write((byte) ssn);
        }
        
        globalTitle.encode(out);
        return out.toByteArray();
    }
    
    
    public String toString() {
        StringBuffer msg = new StringBuffer();
        
        if (pointCodeIndicator == 1) {
            msg.append("Address contains a signaling point code\n");
        } else {
            msg.append("Address contains no signaling point code\n");
        }
        
        if (ssnIndicator == 1) {
            msg.append("Address contains a subsystem number\n");
        } else {
            msg.append("Address contains no subsystem number\n");
        }
        if(globalTitle!=null)
        	msg.append(globalTitle.toString());
        return msg.toString();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((globalTitle == null) ? 0 : globalTitle.hashCode());
		result = prime * result + globalTitleIndicator;
		result = prime * result + pointCodeIndicator;
		result = prime * result + routingIndicator;
		result = prime * result + signalingPointCode;
		result = prime * result + ssn;
		result = prime * result + ssnIndicator;
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
		SccpAddressImpl other = (SccpAddressImpl) obj;
		if (globalTitle == null) {
			if (other.globalTitle != null)
				return false;
		} else if (!globalTitle.equals(other.globalTitle))
			return false;
		if (globalTitleIndicator != other.globalTitleIndicator)
			return false;
		if (pointCodeIndicator != other.pointCodeIndicator)
			return false;
		if (routingIndicator != other.routingIndicator)
			return false;
		if (signalingPointCode != other.signalingPointCode)
			return false;
		if (ssn != other.ssn)
			return false;
		if (ssnIndicator != other.ssnIndicator)
			return false;
		return true;
	}

	public int getPointCodeIndicator() {
		return pointCodeIndicator;
	}

	public void setPointCodeIndicator(int pointCodeIndicator) {
		this.pointCodeIndicator = pointCodeIndicator;
	}

	public int getSsnIndicator() {
		return ssnIndicator;
	}

	public void setSsnIndicator(int ssnIndicator) {
		this.ssnIndicator = ssnIndicator;
	}

	public int getGlobalTitleIndicator() {
		return globalTitleIndicator;
	}

	public void setGlobalTitleIndicator(int globalTitleIndicator) {
		this.globalTitleIndicator = globalTitleIndicator;
	}

	public int getRoutingIndicator() {
		return routingIndicator;
	}

	public void setRoutingIndicator(int routingIndicator) {
		this.routingIndicator = routingIndicator;
	}

	public int getSignalingPointCode() {
		return signalingPointCode;
	}

	public void setSignalingPointCode(int signalingPointCode) {
		this.signalingPointCode = signalingPointCode;
	}

	public int getSsn() {
		return ssn;
	}

	public void setSsn(int ssn) {
		this.ssn = ssn;
	}

	public GlobalTitle getGlobalTitle() {
		return globalTitle;
	}

	public void setGlobalTitle(GlobalTitle globalTitle) {
		this.globalTitle = globalTitle;
	}
    

}
