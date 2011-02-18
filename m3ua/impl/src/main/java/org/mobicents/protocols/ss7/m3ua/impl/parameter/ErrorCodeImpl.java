package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import org.mobicents.protocols.ss7.m3ua.parameter.ErrorCode;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;

/**
 * 
 * @author amit bhayani
 * 
 */
public class ErrorCodeImpl extends ParameterImpl implements ErrorCode {

    private int code;

    public ErrorCodeImpl(int code) {
        this.code = code;
        this.tag = Parameter.Error_Code;
    }

    public ErrorCodeImpl(byte[] data) {
        this.code = 0;
        this.code |= data[0] & 0xFF;
        this.code <<= 8;
        this.code |= data[1] & 0xFF;
        this.code <<= 8;
        this.code |= data[2] & 0xFF;
        this.code <<= 8;
        this.code |= data[3] & 0xFF;
        this.tag = Parameter.Error_Code;
    }

    @Override
    protected byte[] getValue() {
        byte[] data = new byte[4];
        data[0] = (byte) (code >>> 24);
        data[1] = (byte) (code >>> 16);
        data[2] = (byte) (code >>> 8);
        data[3] = (byte) (code);

        return data;
    }

    public int getCode() {
        return this.code;
    }

    @Override
    public String toString() {
        return String.format("ErrorCode code=%d", code);
    }

}
