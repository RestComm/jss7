package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;
import org.mobicents.protocols.ss7.m3ua.parameter.ServiceIndicators;

/**
 * 
 * @author amit bhayani
 * 
 */
public class ServiceIndicatorsImpl extends ParameterImpl implements
        ServiceIndicators {

    private short[] indicators;
    private byte[] value = null;

    protected ServiceIndicatorsImpl(short[] inds) {
        this.tag = Parameter.Service_Indicators;
        this.indicators = inds;
        this.encode();
    }

    protected ServiceIndicatorsImpl(byte[] value) {
        this.tag = Parameter.Service_Indicators;
        this.indicators = new short[value.length];
        for (int i = 0; i < value.length; i++) {
            this.indicators[i] = value[i];
        }
        this.value = value;
    }

    private void encode() {
        // create byte array taking into account data, point codes and
        // indicators;
        this.value = new byte[indicators.length];
        int count = 0;
        // encode routing context
        while (count < value.length) {
            value[count] = (byte) indicators[count++];
        }
    }

    @Override
    protected byte[] getValue() {
        return this.value;
    }

    public short[] getIndicators() {
        return this.indicators;
    }

}
