package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import org.mobicents.protocols.ss7.m3ua.parameter.DiagnosticInfo;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;

/**
 * 
 * @author amit bhayani
 * 
 */
public class DiagnosticInfoImpl extends ParameterImpl implements DiagnosticInfo {

    private String info;

    public DiagnosticInfoImpl(String info) {
        this.info = info;
        this.tag = Parameter.Diagnostic_Information;
    }

    public DiagnosticInfoImpl(byte[] value) {
        this.tag = Parameter.Diagnostic_Information;
        this.info = new String(value);
    }

    @Override
    protected byte[] getValue() {
        return this.info.getBytes();
    }

    public String getInfo() {
        return this.info;
    }

}
