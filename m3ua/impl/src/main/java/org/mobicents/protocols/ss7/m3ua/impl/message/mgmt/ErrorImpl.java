/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.m3ua.impl.message.mgmt;

import java.nio.ByteBuffer;

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
    protected void encodeParams(ByteBuffer buffer) {
        ((ParameterImpl) parameters.get(Parameter.Error_Code)).write(buffer);

        if (parameters.containsKey(Parameter.Routing_Context)) {
            ((ParameterImpl) parameters.get(Parameter.Routing_Context)).write(buffer);
        }

        if (parameters.containsKey(Parameter.Affected_Point_Code)) {
            ((ParameterImpl) parameters.get(Parameter.Affected_Point_Code)).write(buffer);
        }

        if (parameters.containsKey(Parameter.Network_Appearance)) {
            ((ParameterImpl) parameters.get(Parameter.Network_Appearance)).write(buffer);
        }

        if (parameters.containsKey(Parameter.Diagnostic_Information)) {
            ((ParameterImpl) parameters.get(Parameter.Diagnostic_Information)).write(buffer);
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
