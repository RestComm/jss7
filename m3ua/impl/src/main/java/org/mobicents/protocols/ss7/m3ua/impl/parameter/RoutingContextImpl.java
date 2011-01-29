package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import java.util.Arrays;

import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;

public class RoutingContextImpl extends ParameterImpl implements RoutingContext {

    private long[] rcs = null;
    private byte[] value;

    protected RoutingContextImpl(byte[] value) {
        this.tag = Parameter.Routing_Context;

        int count = 0;
        int arrSize = 0;
        rcs = new long[(value.length / 4)];

        while (count < value.length) {
            rcs[arrSize] = 0;
            rcs[arrSize] |= value[count++] & 0xFF;
            rcs[arrSize] <<= 8;
            rcs[arrSize] |= value[count++] & 0xFF;
            rcs[arrSize] <<= 8;
            rcs[arrSize] |= value[count++] & 0xFF;
            rcs[arrSize] <<= 8;
            rcs[arrSize++] |= value[count++] & 0xFF;
        }

        this.value = value;
    }

    protected RoutingContextImpl(long[] routingcontexts) {
        this.tag = Parameter.Routing_Context;
        rcs = routingcontexts;
        encode();
    }

    private void encode() {
        // create byte array taking into account data, point codes and
        // indicators;
        this.value = new byte[(rcs.length * 4)];
        int count = 0;
        int arrSize = 0;
        // encode routing context
        while (count < value.length) {
            value[count++] = (byte) (rcs[arrSize] >>> 24);
            value[count++] = (byte) (rcs[arrSize] >>> 16);
            value[count++] = (byte) (rcs[arrSize] >>> 8);
            value[count++] = (byte) (rcs[arrSize++]);
        }
    }

    public long[] getRoutingContexts() {
        return this.rcs;
    }

    @Override
    protected byte[] getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("RoutingContext data: rc = %s", Arrays
                .toString(rcs));
    }
}
