package org.mobicents.protocols.ss7.m3ua.impl.message.ssnm;

import java.nio.ByteBuffer;

import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterImpl;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.ssnm.DestinationUPUnavailable;
import org.mobicents.protocols.ss7.m3ua.parameter.AffectedPointCode;
import org.mobicents.protocols.ss7.m3ua.parameter.InfoString;
import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.UserCause;

/**
 * 
 * @author amit bhayani
 * 
 */
public class DestinationUPUnavailableImpl extends M3UAMessageImpl implements
        DestinationUPUnavailable {

    public DestinationUPUnavailableImpl() {
        super(MessageClass.SIGNALING_NETWORK_MANAGEMENT,
                MessageType.DESTINATION_USER_PART_UNAVAILABLE);
    }

    public AffectedPointCode getAffectedPointCode() {
        return (AffectedPointCode) parameters
                .get(ParameterImpl.Affected_Point_Code);
    }

    public InfoString getInfoString() {
        return (InfoString) parameters.get(ParameterImpl.INFO_String);
    }

    public NetworkAppearance getNetworkAppearance() {
        return (NetworkAppearance) parameters
                .get(ParameterImpl.Network_Appearance);
    }

    public RoutingContext getRoutingContext() {
        return (RoutingContext) parameters.get(ParameterImpl.Routing_Context);
    }

    public UserCause getUserCause() {
        return (UserCause) parameters.get(ParameterImpl.User_Cause);
    }

    public void setAffectedPointCode(AffectedPointCode afpc) {
        parameters.put(ParameterImpl.Affected_Point_Code, afpc);
    }

    public void setInfoString(InfoString str) {
        parameters.put(ParameterImpl.INFO_String, str);
    }

    public void setNetworkAppearance(NetworkAppearance p) {
        parameters.put(ParameterImpl.Network_Appearance, p);
    }

    public void setRoutingContext(RoutingContext routingCntx) {
        parameters.put(ParameterImpl.Routing_Context, routingCntx);
    }

    public void setUserCause(UserCause usrCau) {
        parameters.put(ParameterImpl.User_Cause, usrCau);
    }

    @Override
    protected void encodeParams(ByteBuffer buffer) {
        if (parameters.containsKey(Parameter.Network_Appearance)) {
            ((ParameterImpl) parameters.get(Parameter.Network_Appearance))
                    .write(buffer);
        }

        if (parameters.containsKey(Parameter.Routing_Context)) {
            ((ParameterImpl) parameters.get(Parameter.Routing_Context))
                    .write(buffer);
        }
        if (parameters.containsKey(Parameter.Affected_Point_Code)) {
            ((ParameterImpl) parameters.get(Parameter.Affected_Point_Code))
                    .write(buffer);
        }
        if (parameters.containsKey(Parameter.User_Cause)) {
            ((ParameterImpl) parameters.get(Parameter.User_Cause))
                    .write(buffer);
        }
        if (parameters.containsKey(Parameter.INFO_String)) {
            ((ParameterImpl) parameters.get(Parameter.INFO_String))
                    .write(buffer);
        }

    }
}
