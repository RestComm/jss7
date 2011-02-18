package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import org.mobicents.protocols.ss7.m3ua.parameter.LocalRKIdentifier;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;

/**
 * 
 * @author amit bhayani
 *
 */
public class LocalRKIdentifierImpl extends ParameterImpl implements
        LocalRKIdentifier {

    private byte[] value;
    private long id;

    protected LocalRKIdentifierImpl(byte[] data) {
        this.tag = Parameter.Local_Routing_Key_Identifier;
        this.value = data;
        
        this.id = 0;
        this.id |= data[0] & 0xFF;
        this.id <<= 8;
        this.id |= data[1] & 0xFF;
        this.id <<= 8;
        this.id |= data[2] & 0xFF;
        this.id <<= 8;
        this.id |= data[3] & 0xFF;
    }

    protected LocalRKIdentifierImpl(long id) {
        this.tag = Parameter.Local_Routing_Key_Identifier;
        this.id = id;
        this.encode();
    }

    private void encode() {
        // create byte array taking into account data, point codes and
        // indicators;
        this.value = new byte[4];
        // encode routing context
        value[0] = (byte) (this.id >> 24);
        value[1] = (byte) (this.id >> 16);
        value[2] = (byte) (this.id >> 8);
        value[3] = (byte) (this.id);
    }

    @Override
    protected byte[] getValue() {
        return this.value;
    }

    public long getId() {
        return this.id;
    }
    
    @Override
    public String toString() {
        return String.format("LocalRKIdentifier id=%d", id);
    }
}
