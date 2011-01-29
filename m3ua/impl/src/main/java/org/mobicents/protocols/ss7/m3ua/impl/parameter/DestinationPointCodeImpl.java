package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import org.mobicents.protocols.ss7.m3ua.parameter.DestinationPointCode;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;

/**
 * 
 * @author amit bhayani
 * 
 */
public class DestinationPointCodeImpl extends ParameterImpl implements
        DestinationPointCode {

    private int destPC = 0;
    private short mask = 0;
    private byte[] value;

    protected DestinationPointCodeImpl(byte[] value) {
        this.tag = Parameter.Destination_Point_Code;
        this.value = value;
        this.mask = value[0];

        destPC = 0;
        destPC |= value[1] & 0xFF;
        destPC <<= 8;
        destPC |= value[2] & 0xFF;
        destPC <<= 8;
        destPC |= value[3] & 0xFF;
    }

    protected DestinationPointCodeImpl(int pc, short mask) {
        this.tag = Parameter.Destination_Point_Code;
        this.destPC = pc;
        this.mask = mask;
        encode();
    }

    private void encode() {
        // create byte array taking into account data, point codes and
        // indicators;
        this.value = new byte[4];
        // encode point code with mask
        value[0] = (byte) this.mask;// Mask

        value[1] = (byte) (destPC >> 16);
        value[2] = (byte) (destPC >> 8);
        value[3] = (byte) (destPC);
    }

    public int getPointCode() {
        return destPC;
    }

    @Override
    protected byte[] getValue() {
        return value;
    }

    public short getMask() {
        return this.mask;
    }

}
