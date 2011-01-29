package org.mobicents.protocols.ss7.m3ua.impl.message.rkm;

import java.nio.ByteBuffer;

import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterImpl;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.rkm.RegistrationResponse;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;
import org.mobicents.protocols.ss7.m3ua.parameter.RegistrationResult;

/**
 * 
 * @author amit bhayani
 *
 */
public class RegistrationResponseImpl extends M3UAMessageImpl implements
        RegistrationResponse {

    public RegistrationResponseImpl() {
        super(MessageClass.ROUTING_KEY_MANAGEMENT, MessageType.REG_RESPONSE);
    }

    @Override
    protected void encodeParams(ByteBuffer buffer) {
        ((ParameterImpl) parameters.get(Parameter.Registration_Result))
                .write(buffer);
    }

    public RegistrationResult getRegistrationResult() {
        return (RegistrationResult) parameters
                .get(ParameterImpl.Registration_Result);
    }

    public void setRegistrationResult(RegistrationResult rslt) {
        parameters.put(ParameterImpl.Registration_Result, rslt);
    }

}
