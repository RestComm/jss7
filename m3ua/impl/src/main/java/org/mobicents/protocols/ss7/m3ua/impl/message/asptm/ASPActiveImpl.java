package org.mobicents.protocols.ss7.m3ua.impl.message.asptm;

import java.nio.ByteBuffer;

import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterImpl;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.asptm.ASPActive;
import org.mobicents.protocols.ss7.m3ua.parameter.InfoString;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;

/**
 * 
 * @author amit bhayani
 * 
 */
public class ASPActiveImpl extends M3UAMessageImpl implements ASPActive {

    public ASPActiveImpl() {
        super(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE);
    }

    @Override
    protected void encodeParams(ByteBuffer buffer) {
        if (parameters.containsKey(Parameter.Traffic_Mode_Type)) {
            ((ParameterImpl) parameters.get(Parameter.Traffic_Mode_Type)).write(buffer);
        }
        if (parameters.containsKey(Parameter.Routing_Context)) {
            ((ParameterImpl) parameters.get(Parameter.Routing_Context)).write(buffer);
        }
        if (parameters.containsKey(Parameter.INFO_String)) {
            ((ParameterImpl) parameters.get(Parameter.INFO_String)).write(buffer);
        }
    }

    public InfoString getInfoString() {
        return (InfoString) parameters.get(Parameter.INFO_String);
    }

    public RoutingContext getRoutingContext() {
        return (RoutingContext) parameters.get(Parameter.Routing_Context);
    }

    public TrafficModeType getTrafficModeType() {
        return (TrafficModeType) parameters.get(Parameter.Traffic_Mode_Type);
    }

    public void setInfoString(InfoString str) {
        if (str != null) {
            parameters.put(Parameter.INFO_String, str);
        }

    }

    public void setRoutingContext(RoutingContext rc) {
        parameters.put(Parameter.Routing_Context, rc);

    }

    public void setTrafficModeType(TrafficModeType mode) {
        if (mode != null) {
            parameters.put(Parameter.Traffic_Mode_Type, mode);
        }
    }

}
