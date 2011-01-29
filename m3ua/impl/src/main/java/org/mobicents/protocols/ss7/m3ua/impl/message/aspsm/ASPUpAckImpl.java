package org.mobicents.protocols.ss7.m3ua.impl.message.aspsm;

import java.nio.ByteBuffer;

import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterImpl;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.aspsm.ASPUpAck;
import org.mobicents.protocols.ss7.m3ua.parameter.ASPIdentifier;
import org.mobicents.protocols.ss7.m3ua.parameter.InfoString;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;

/**
 * 
 * @author amit bhayani
 *
 */
public class ASPUpAckImpl extends M3UAMessageImpl implements ASPUpAck {

    public ASPUpAckImpl() {
        super(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK);
    }

    public ASPIdentifier getASPIdentifier() {
        return (ASPIdentifier) parameters.get(ParameterImpl.ASP_Identifier);
    }

    public void setASPIdentifier(ASPIdentifier p) {
        parameters.put(ParameterImpl.ASP_Identifier, p);
    }

    public InfoString getInfoString() {
        return (InfoString) parameters.get(ParameterImpl.INFO_String);
    }

    public void setInfoString(InfoString str) {
        parameters.put(ParameterImpl.INFO_String, str);
    }

    @Override
    protected void encodeParams(ByteBuffer buffer) {
        if (parameters.containsKey(Parameter.ASP_Identifier)) {
            ((ParameterImpl) parameters.get(Parameter.ASP_Identifier))
                    .write(buffer);
        }

        if (parameters.containsKey(Parameter.INFO_String)) {
            ((ParameterImpl) parameters.get(Parameter.INFO_String))
                    .write(buffer);
        }
    }

}
