package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import org.mobicents.protocols.ss7.m3ua.parameter.HeartbeatData;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;

/**
 * 
 * @author amit bhayani
 *
 */
public class HeartbeatDataImpl extends ParameterImpl implements HeartbeatData {
    private String string;

    protected HeartbeatDataImpl(byte[] value) {
        this.tag = Parameter.INFO_String;
        this.string = new String(value);
    }

    protected HeartbeatDataImpl(String string) {
        this.tag = Parameter.INFO_String;
        this.string = string;
    }

    public String getData() {
        return this.string;
    }

    @Override
    protected byte[] getValue() {
        return this.string.getBytes();
    }

    @Override
    public String toString() {
        return String.format("HeartbeatData : data = %s ", this.string);
    }
}
