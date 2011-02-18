package org.mobicents.protocols.ss7.m3ua.impl.message.mgmt;

import java.nio.ByteBuffer;

import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterImpl;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.mgmt.Notify;
import org.mobicents.protocols.ss7.m3ua.parameter.ASPIdentifier;
import org.mobicents.protocols.ss7.m3ua.parameter.InfoString;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.Status;

/**
 * 
 * @author amit bhayani
 * 
 */
public class NotifyImpl extends M3UAMessageImpl implements Notify {

    public NotifyImpl() {
        super(MessageClass.MANAGEMENT, MessageType.NOTIFY);
    }

    @Override
    protected void encodeParams(ByteBuffer buffer) {
        ((ParameterImpl) parameters.get(Parameter.Status)).write(buffer);

        if (parameters.containsKey(Parameter.ASP_Identifier)) {
            ((ParameterImpl) parameters.get(Parameter.ASP_Identifier)).write(buffer);
        }

        if (parameters.containsKey(Parameter.Routing_Context)) {
            ((ParameterImpl) parameters.get(Parameter.Routing_Context)).write(buffer);
        }

        if (parameters.containsKey(Parameter.INFO_String)) {
            ((ParameterImpl) parameters.get(Parameter.INFO_String)).write(buffer);
        }
    }

    public ASPIdentifier getASPIdentifier() {
        return ((ASPIdentifier) parameters.get(Parameter.ASP_Identifier));
    }

    public InfoString getInfoString() {
        return ((InfoString) parameters.get(Parameter.INFO_String));
    }

    public RoutingContext getRoutingContext() {
        return ((RoutingContext) parameters.get(Parameter.Routing_Context));
    }

    public Status getStatus() {
        return ((Status) parameters.get(Parameter.Status));
    }

    public void setASPIdentifier(ASPIdentifier id) {
        parameters.put(Parameter.ASP_Identifier, id);
    }

    public void setInfoString(InfoString str) {
        parameters.put(Parameter.INFO_String, str);
    }

    public void setRoutingContext(RoutingContext rc) {
        parameters.put(Parameter.Routing_Context, rc);
    }

    public void setStatus(Status status) {
        parameters.put(Parameter.Status, status);
    }
}
