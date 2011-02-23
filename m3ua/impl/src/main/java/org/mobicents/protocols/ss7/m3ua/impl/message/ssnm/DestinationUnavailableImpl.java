package org.mobicents.protocols.ss7.m3ua.impl.message.ssnm;

import java.nio.ByteBuffer;

import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterImpl;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.ssnm.DestinationUnavailable;
import org.mobicents.protocols.ss7.m3ua.parameter.AffectedPointCode;
import org.mobicents.protocols.ss7.m3ua.parameter.InfoString;
import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;

/**
 * 
 * @author amit bhayani
 * 
 */
public class DestinationUnavailableImpl extends M3UAMessageImpl implements DestinationUnavailable {

    public DestinationUnavailableImpl() {
        super(MessageClass.SIGNALING_NETWORK_MANAGEMENT, MessageType.DESTINATION_UNAVAILABLE);
    }

    public AffectedPointCode getAffectedPointCodes() {
        return (AffectedPointCode) parameters.get(Parameter.Affected_Point_Code);
    }

    public InfoString getInfoString() {
        return (InfoString) parameters.get(Parameter.INFO_String);
    }

    public NetworkAppearance getNetworkAppearance() {
        return (NetworkAppearance) parameters.get(Parameter.Network_Appearance);
    }

    public RoutingContext getRoutingContexts() {
        return (RoutingContext) parameters.get(Parameter.Routing_Context);
    }

    public void setAffectedPointCodes(AffectedPointCode afpc) {
        parameters.put(Parameter.Affected_Point_Code, afpc);
    }

    public void setInfoString(InfoString str) {
        if (str != null) {
            parameters.put(Parameter.INFO_String, str);
        }
    }

    public void setNetworkAppearance(NetworkAppearance p) {
        if (p != null) {
            parameters.put(Parameter.Network_Appearance, p);
        }
    }

    public void setRoutingContexts(RoutingContext routingCntx) {
        parameters.put(Parameter.Routing_Context, routingCntx);
    }

    @Override
    protected void encodeParams(ByteBuffer buffer) {
        if (parameters.containsKey(Parameter.Network_Appearance)) {
            ((ParameterImpl) parameters.get(Parameter.Network_Appearance)).write(buffer);
        }

        if (parameters.containsKey(Parameter.Routing_Context)) {
            ((ParameterImpl) parameters.get(Parameter.Routing_Context)).write(buffer);
        }
        if (parameters.containsKey(Parameter.Affected_Point_Code)) {
            ((ParameterImpl) parameters.get(Parameter.Affected_Point_Code)).write(buffer);
        }
        if (parameters.containsKey(Parameter.INFO_String)) {
            ((ParameterImpl) parameters.get(Parameter.INFO_String)).write(buffer);
        }

    }
}
