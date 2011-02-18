package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;

/**
 * 
 * @author amit bhayani
 *
 */
public class TrafficModeTypeImpl extends ParameterImpl implements
        TrafficModeType {
    private int mode = 0;
    private byte[] value;

    protected TrafficModeTypeImpl(byte[] data) {
        this.tag = Parameter.Traffic_Mode_Type;
        this.value = data;
        this.mode = 0;
        this.mode |= data[0] & 0xFF;
        this.mode <<= 8;
        this.mode |= data[1] & 0xFF;
        this.mode <<= 8;
        this.mode |= data[2] & 0xFF;
        this.mode <<= 8;
        this.mode |= data[3] & 0xFF;
    }

    protected TrafficModeTypeImpl(int traffmode) {
        this.tag = Parameter.Traffic_Mode_Type;
        mode = traffmode;
        encode();
    }

    private void encode() {
        // create byte array taking into account data, point codes and
        // indicators;
        this.value = new byte[4];
        // encode routing context
        value[0] = (byte) (mode >> 24);
        value[1] = (byte) (mode >> 16);
        value[2] = (byte) (mode >> 8);
        value[3] = (byte) (mode);
    }

    public int getMode() {
        return mode;
    }

    @Override
    protected byte[] getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return String.format("TrafficModeType mode=%d", mode);
    }

}
