package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import org.mobicents.protocols.ss7.m3ua.parameter.ConcernedDPC;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;

/**
 * 
 * @author amit bhayani
 *
 */
public class ConcernedDPCImpl extends ParameterImpl implements ConcernedDPC {

    private int pointCode;

    protected ConcernedDPCImpl(int pointCode) {
        this.pointCode = pointCode;
        this.tag = Parameter.Concerned_Destination;
    }

    protected ConcernedDPCImpl(byte[] data) {
        //data[0] is reserved
        
        this.pointCode = 0;
        this.pointCode |= data[1] & 0xFF;
        this.pointCode <<= 8;
        this.pointCode |= data[2] & 0xFF;
        this.pointCode <<= 8;
        this.pointCode |= data[3] & 0xFF;
        this.tag = Parameter.Concerned_Destination;
    }

    @Override
    protected byte[] getValue() {
        byte[] data = new byte[4];
        //reserved
        data[0] = 0;
        
        //DPC
        data[1] = (byte) (pointCode >>> 16);
        data[2] = (byte) (pointCode >>> 8);
        data[3] = (byte) (pointCode);

        return data;
    }

    @Override
    public String toString() {
        return String.format("ConcernedDPC dpc=%d", pointCode);
    }

    public int getPointCode() {
        return this.pointCode;
    }

}
