package org.mobicents.protocols.ss7.m3ua.impl.message.rkm;

import java.nio.ByteBuffer;

import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterImpl;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.rkm.DeregistrationRequest;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;

/**
 * 
 * @author amit bhayani
 *
 */
public class DeregistrationRequestImpl extends M3UAMessageImpl implements
        DeregistrationRequest {

    public DeregistrationRequestImpl() {
        super(MessageClass.ROUTING_KEY_MANAGEMENT, MessageType.DEREG_REQUEST);
    }

    @Override
    protected void encodeParams(ByteBuffer buffer) {
        ((ParameterImpl) parameters.get(Parameter.Routing_Context))
                .write(buffer);
    }

    public RoutingContext getRoutingContext() {
        return (RoutingContext) parameters.get(ParameterImpl.Routing_Context);
    }

    public void setRoutingContext(RoutingContext rc) {
        parameters.put(ParameterImpl.Routing_Context, rc);
    }

}
