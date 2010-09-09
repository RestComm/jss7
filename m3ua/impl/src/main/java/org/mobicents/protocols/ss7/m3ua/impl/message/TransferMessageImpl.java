/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, encode to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */

package org.mobicents.protocols.ss7.m3ua.impl.message;

import java.nio.ByteBuffer;
import org.mobicents.protocols.ss7.m3ua.message.parm.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.message.*;
import org.mobicents.protocols.ss7.m3ua.impl.message.parms.ProtocolDataImpl;
import org.mobicents.protocols.ss7.m3ua.message.parm.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.message.parm.ProtocolData;

/**
 *
 * @author kulikov
 */
public class TransferMessageImpl extends M3UAMessageImpl implements TransferMessage {
    
    protected TransferMessageImpl() {
        super(MessageClass.TRANSFER_MESSAGES, MessageType.PAYLOAD);
    }
    
    public NetworkAppearance getNetworkAppearance() {
        return (NetworkAppearance) parameters.get(ParameterImpl.Network_Appearance);
    }
    
    public void setNetworkAppearance(NetworkAppearance p) {
        //parameters.put(ParameterImpl.Network_Appearance, p);
    }

    public RoutingContext getRoutingContext() {
        return (RoutingContext) parameters.get(ParameterImpl.Routing_Context);
    }
    
    public void setRoutingContext(RoutingContext p) {
        //parameters.put(ParameterImpl.Routing_Context, p);
    }

    public ProtocolData getData() {
        return (ProtocolDataImpl) parameters.get(ParameterImpl.Protocol_Data);
    }
    
    public void setData(ProtocolData p) {
        parameters.put(ParameterImpl.Protocol_Data, p);
    }
    
    @Override
    public String toString() {
        return "TransferMessage: " + parameters;
    }

    @Override
    protected void encodeParams(ByteBuffer buffer) {
        if (parameters.containsKey(Parameter.Network_Appearance)) {
            ((ParameterImpl)parameters.get(Parameter.Network_Appearance)).write(buffer);
        }
        if (parameters.containsKey(Parameter.Routing_Context)) {
            ((ParameterImpl)parameters.get(Parameter.Routing_Context)).write(buffer);
        }
        if (parameters.containsKey(Parameter.Protocol_Data)) {
            ((ParameterImpl)parameters.get(Parameter.Protocol_Data)).write(buffer);
        }
        if (parameters.containsKey(Parameter.Correlation_ID)) {
            ((ParameterImpl)parameters.get(Parameter.Correlation_ID)).write(buffer);
        }
    }

}
