package org.mobicents.protocols.ss7.m3ua.impl.message.aspsm;

import java.nio.ByteBuffer;

import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterImpl;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.aspsm.ASPUp;
import org.mobicents.protocols.ss7.m3ua.parameter.ASPIdentifier;
import org.mobicents.protocols.ss7.m3ua.parameter.InfoString;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;

/**
 * 
 * @author amit bhayani
 * 
 */
public class ASPUpImpl extends M3UAMessageImpl implements ASPUp {

    public ASPUpImpl() {
        super(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP);
    }

    public ASPIdentifier getASPIdentifier() {
        return (ASPIdentifier) parameters.get(Parameter.ASP_Identifier);
    }

    public void setASPIdentifier(ASPIdentifier p) {
        if (p != null) {
            parameters.put(Parameter.ASP_Identifier, p);
        }
    }

    public InfoString getInfoString() {
        return (InfoString) parameters.get(Parameter.INFO_String);
    }

    public void setInfoString(InfoString str) {
        if (str != null) {
            parameters.put(Parameter.INFO_String, str);
        }
    }

    @Override
    protected void encodeParams(ByteBuffer buffer) {
        if (parameters.containsKey(Parameter.ASP_Identifier)) {
            ((ParameterImpl) parameters.get(Parameter.ASP_Identifier)).write(buffer);
        }

        if (parameters.containsKey(Parameter.INFO_String)) {
            ((ParameterImpl) parameters.get(Parameter.INFO_String)).write(buffer);
        }
    }

}
