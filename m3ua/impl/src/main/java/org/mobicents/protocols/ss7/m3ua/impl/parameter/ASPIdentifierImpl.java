package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import org.mobicents.protocols.ss7.m3ua.parameter.ASPIdentifier;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;

public class ASPIdentifierImpl extends ParameterImpl implements ASPIdentifier {

    private long aspID = 0;
    private byte[] value;

    protected ASPIdentifierImpl(byte[] value) {
        this.tag = Parameter.ASP_Identifier;

        this.aspID = 0;
        this.aspID |= value[0] & 0xFF;
        this.aspID <<= 8;
        this.aspID |= value[1] & 0xFF;
        this.aspID <<= 8;
        this.aspID |= value[2] & 0xFF;
        this.aspID <<= 8;
        this.aspID |= value[3] & 0xFF;

        this.value = value;
    }

    protected ASPIdentifierImpl(long id) {
        this.tag = Parameter.ASP_Identifier;
        aspID = id;
        encode();
    }

    private void encode() {
        // create byte array taking into account data, point codes and
        // indicators;
        this.value = new byte[4];

        // encode asp identifier
        value[0] = (byte) (aspID >> 24);
        value[1] = (byte) (aspID >> 16);
        value[2] = (byte) (aspID >> 8);
        value[3] = (byte) (aspID);
    }

    public long getAspId() {
        return aspID;
    }

    @Override
    protected byte[] getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("ASPIdentifier id=%d", aspID);
    }
}
