package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import org.mobicents.protocols.ss7.m3ua.parameter.InfoString;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;

/**
 * 
 * @author amit bhayani
 *
 */
public class InfoStringImpl extends ParameterImpl implements InfoString {

    private String string;

    protected InfoStringImpl(byte[] value) {
        this.tag = Parameter.INFO_String;
        this.string = new String(value);
    }

    protected InfoStringImpl(String string) {
        this.tag = Parameter.INFO_String;
        this.string = string;
    }

    public String getString() {
        return this.string;
    }

    @Override
    protected byte[] getValue() {
        return this.string.getBytes();
    }

    @Override
    public String toString() {
        return String.format("InfoString : string = %s ", this.string);
    }

}
