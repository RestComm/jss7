package org.mobicents.protocols.ss7.m3ua.impl.message.rkm;

import java.nio.ByteBuffer;

import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterImpl;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.rkm.DeregistrationResponse;
import org.mobicents.protocols.ss7.m3ua.parameter.DeregistrationResult;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;

/**
 * 
 * @author amit bhayani
 *
 */
public class DeregistrationResponseImpl extends M3UAMessageImpl implements
        DeregistrationResponse {

    public DeregistrationResponseImpl() {
        super(MessageClass.ROUTING_KEY_MANAGEMENT, MessageType.DEREG_RESPONSE);
    }

    @Override
    protected void encodeParams(ByteBuffer buffer) {
        ((ParameterImpl) parameters.get(Parameter.Deregistration_Result))
                .write(buffer);
    }

    public DeregistrationResult getDeregistrationResult() {
        return (DeregistrationResult) parameters
                .get(Parameter.Deregistration_Result);
    }

    public void setDeregistrationResult(DeregistrationResult result) {
        parameters.put(ParameterImpl.Deregistration_Result, result);

    }

}
