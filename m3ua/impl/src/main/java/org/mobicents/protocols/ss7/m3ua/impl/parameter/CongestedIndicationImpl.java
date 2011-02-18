package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import org.mobicents.protocols.ss7.m3ua.parameter.CongestedIndication;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;

/**
 * 
 * @author amit bhayani
 * 
 */
public class CongestedIndicationImpl extends ParameterImpl implements
        CongestedIndication {

    private CongestionLevel level;

    protected CongestedIndicationImpl(CongestionLevel level) {
        this.level = level;
        this.tag = Parameter.Congestion_Indications;
    }

    protected CongestedIndicationImpl(byte[] data) {
        //data[0], data[1] and data[2] are reserved
        this.level = CongestionLevel.getCongestionLevel(data[3]);
        this.tag = Parameter.Congestion_Indications;
    }

    @Override
    protected byte[] getValue() {
        byte[] data = new byte[4];
        data[0] = 0;// Reserved
        data[1] = 0; // Reserved
        data[2] = 0;// Reserved
        data[3] = (byte) level.getLevel();

        return data;
    }

    public CongestionLevel getCongestionLevel() {
        return this.level;
    }

    @Override
    public String toString() {
        return String.format("CongestedIndication level=%s", level);
    }

}
