/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */ 
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
public class DestinationUPUnavailableImpl extends M3UAMessageImpl implements DestinationUPUnavailable {

    public DestinationUPUnavailableImpl() {
        super(MessageClass.SIGNALING_NETWORK_MANAGEMENT, MessageType.DESTINATION_USER_PART_UNAVAILABLE);
    }

    public AffectedPointCode getAffectedPointCode() {
        return (AffectedPointCode) parameters.get(Parameter.Affected_Point_Code);
    }

    public InfoString getInfoString() {
        return (InfoString) parameters.get(Parameter.INFO_String);
    }

    public NetworkAppearance getNetworkAppearance() {
        return (NetworkAppearance) parameters.get(Parameter.Network_Appearance);
    }

    public RoutingContext getRoutingContext() {
        return (RoutingContext) parameters.get(Parameter.Routing_Context);
    }

    public UserCause getUserCause() {
        return (UserCause) parameters.get(Parameter.User_Cause);
    }

    public void setAffectedPointCode(AffectedPointCode afpc) {
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

    public void setRoutingContext(RoutingContext routingCntx) {
        parameters.put(Parameter.Routing_Context, routingCntx);
    }

    public void setUserCause(UserCause usrCau) {
        parameters.put(Parameter.User_Cause, usrCau);
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
        if (parameters.containsKey(Parameter.User_Cause)) {
            ((ParameterImpl) parameters.get(Parameter.User_Cause)).write(buffer);
        }
        if (parameters.containsKey(Parameter.INFO_String)) {
            ((ParameterImpl) parameters.get(Parameter.INFO_String)).write(buffer);
        }

    }
}
