package org.mobicents.protocols.ss7.m3ua.impl.message.aspsm;

import java.nio.ByteBuffer;

import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterImpl;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.aspsm.ASPDownAck;
import org.mobicents.protocols.ss7.m3ua.parameter.InfoString;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;

/**
 * 
 * @author amit bhayani
 * 
 */
public class ASPDownAckImpl extends M3UAMessageImpl implements ASPDownAck {

    public ASPDownAckImpl() {
        super(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN_ACK);
    }

    public InfoString getInfoString() {
        return (InfoString) parameters.get(ParameterImpl.INFO_String);
    }

    public void setInfoString(InfoString str) {
        if (str != null) {
            parameters.put(ParameterImpl.INFO_String, str);
        }
    }

    @Override
    protected void encodeParams(ByteBuffer buffer) {

        if (parameters.containsKey(Parameter.INFO_String)) {
            ((ParameterImpl) parameters.get(Parameter.INFO_String)).write(buffer);
        }
    }

}
