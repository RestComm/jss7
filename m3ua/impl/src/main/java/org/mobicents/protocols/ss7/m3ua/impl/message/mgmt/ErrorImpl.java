/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.protocols.ss7.m3ua.impl.message.mgmt;

import io.netty.buffer.ByteBuf;

import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterImpl;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.mgmt.Error;
import org.mobicents.protocols.ss7.m3ua.parameter.AffectedPointCode;
import org.mobicents.protocols.ss7.m3ua.parameter.DiagnosticInfo;
import org.mobicents.protocols.ss7.m3ua.parameter.ErrorCode;
import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;

/**
 *
 * @author amit bhayani
 *
 */
public class ErrorImpl extends M3UAMessageImpl implements Error {

    public ErrorImpl() {
        super(MessageClass.MANAGEMENT, MessageType.ERROR, MessageType.S_ERROR);
    }

    @Override
    protected void encodeParams(ByteBuf buf) {
        ((ParameterImpl) parameters.get(Parameter.Error_Code)).write(buf);

        if (parameters.containsKey(Parameter.Routing_Context)) {
            ((ParameterImpl) parameters.get(Parameter.Routing_Context)).write(buf);
        }

        if (parameters.containsKey(Parameter.Affected_Point_Code)) {
            ((ParameterImpl) parameters.get(Parameter.Affected_Point_Code)).write(buf);
        }

        if (parameters.containsKey(Parameter.Network_Appearance)) {
            ((ParameterImpl) parameters.get(Parameter.Network_Appearance)).write(buf);
        }

        if (parameters.containsKey(Parameter.Diagnostic_Information)) {
            ((ParameterImpl) parameters.get(Parameter.Diagnostic_Information)).write(buf);
        }
    }

    public AffectedPointCode getAffectedPointCode() {
        return ((AffectedPointCode) parameters.get(Parameter.Affected_Point_Code));
    }

    public DiagnosticInfo getDiagnosticInfo() {
        return ((DiagnosticInfo) parameters.get(Parameter.Diagnostic_Information));
    }

    public ErrorCode getErrorCode() {
        return ((ErrorCode) parameters.get(Parameter.Error_Code));
    }

    public NetworkAppearance getNetworkAppearance() {
        return ((NetworkAppearance) parameters.get(Parameter.Network_Appearance));
    }

    public RoutingContext getRoutingContext() {
        return ((RoutingContext) parameters.get(Parameter.Routing_Context));
    }

    public void setAffectedPointCode(AffectedPointCode affPc) {
        parameters.put(Parameter.Affected_Point_Code, affPc);
    }

    public void setDiagnosticInfo(DiagnosticInfo diagInfo) {
        parameters.put(Parameter.Diagnostic_Information, diagInfo);
    }

    public void setErrorCode(ErrorCode code) {
        parameters.put(Parameter.Error_Code, code);
    }

    public void setNetworkAppearance(NetworkAppearance netApp) {
        parameters.put(Parameter.Network_Appearance, netApp);
    }

    public void setRoutingContext(RoutingContext rc) {
        if (rc != null) {
            parameters.put(Parameter.Routing_Context, rc);
        }
    }

}
