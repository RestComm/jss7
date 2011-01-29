package org.mobicents.protocols.ss7.m3ua.impl.message.rkm;

import java.nio.ByteBuffer;

import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterImpl;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.rkm.RegistrationRequest;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingKey;

/**
 * 
 * @author amit bhayani
 *
 */
public class RegistrationRequestImpl extends M3UAMessageImpl implements
        RegistrationRequest {

    public RegistrationRequestImpl() {
        super(MessageClass.ROUTING_KEY_MANAGEMENT, MessageType.REG_REQUEST);
    }

    @Override
    protected void encodeParams(ByteBuffer buffer) {
        if (parameters.containsKey(Parameter.Routing_Key)) {
            ((ParameterImpl) parameters.get(Parameter.Routing_Key))
                    .write(buffer);
        }
    }

    public RoutingKey getRoutingKey() {
        return (RoutingKey) parameters.get(ParameterImpl.Routing_Key);
    }

    public void setRoutingKey(RoutingKey key) {
        parameters.put(ParameterImpl.Routing_Key, key);
    }

}
