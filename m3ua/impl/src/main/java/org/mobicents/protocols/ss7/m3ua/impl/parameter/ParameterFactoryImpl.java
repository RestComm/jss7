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
package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import org.mobicents.protocols.ss7.m3ua.parameter.ASPIdentifier;
import org.mobicents.protocols.ss7.m3ua.parameter.AffectedPointCode;
import org.mobicents.protocols.ss7.m3ua.parameter.ConcernedDPC;
import org.mobicents.protocols.ss7.m3ua.parameter.CongestedIndication;
import org.mobicents.protocols.ss7.m3ua.parameter.CongestedIndication.CongestionLevel;
import org.mobicents.protocols.ss7.m3ua.parameter.CorrelationId;
import org.mobicents.protocols.ss7.m3ua.parameter.DeregistrationResult;
import org.mobicents.protocols.ss7.m3ua.parameter.DeregistrationStatus;
import org.mobicents.protocols.ss7.m3ua.parameter.DestinationPointCode;
import org.mobicents.protocols.ss7.m3ua.parameter.DiagnosticInfo;
import org.mobicents.protocols.ss7.m3ua.parameter.ErrorCode;
import org.mobicents.protocols.ss7.m3ua.parameter.HeartbeatData;
import org.mobicents.protocols.ss7.m3ua.parameter.InfoString;
import org.mobicents.protocols.ss7.m3ua.parameter.LocalRKIdentifier;
import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.OPCList;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;
import org.mobicents.protocols.ss7.m3ua.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.m3ua.parameter.ProtocolData;
import org.mobicents.protocols.ss7.m3ua.parameter.RegistrationResult;
import org.mobicents.protocols.ss7.m3ua.parameter.RegistrationStatus;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingKey;
import org.mobicents.protocols.ss7.m3ua.parameter.ServiceIndicators;
import org.mobicents.protocols.ss7.m3ua.parameter.Status;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.mobicents.protocols.ss7.m3ua.parameter.UserCause;

/**
 *
 * @author kulikov
 */
public class ParameterFactoryImpl implements ParameterFactory {
    public ProtocolData createProtocolData(int opc, int dpc, int si, int ni, int mp, int sls, byte[] data) {
        return new ProtocolDataImpl(opc, dpc, si, ni, mp, sls, data);
    }

    public ProtocolData createProtocolData(byte[] payloadData) {
        ProtocolDataImpl p = new ProtocolDataImpl(payloadData);
        return p;
    }

    public NetworkAppearance createNetworkAppearance(long netApp) {
        return new NetworkAppearanceImpl(netApp);
    }

    public RoutingContext createRoutingContext(long[] routCntx) {
        return new RoutingContextImpl(routCntx);
    }

    public CorrelationId createCorrelationId(long corrId) {
        return new CorrelationIdImpl(corrId);
    }

    public AffectedPointCode createAffectedPointCode(int[] pc, short[] mask) {
        return new AffectedPointCodeImpl(pc, mask);
    }

    public DestinationPointCode createDestinationPointCode(int pc, short mask) {
        return new DestinationPointCodeImpl(pc, mask);
    }

    public InfoString createInfoString(String string) {
        return new InfoStringImpl(string);
    }

    public ConcernedDPC createConcernedDPC(int pointCode) {
        return new ConcernedDPCImpl(pointCode);
    }

    public CongestedIndication createCongestedIndication(CongestionLevel level) {
        return new CongestedIndicationImpl(level);
    }

    public UserCause createUserCause(int user, int cause) {
        return new UserCauseImpl(user, cause);
    }

    public ASPIdentifier createASPIdentifier(long aspId) {
        return new ASPIdentifierImpl(aspId);
    }

    public LocalRKIdentifier createLocalRKIdentifier(long id) {
        return new LocalRKIdentifierImpl(id);
    }

    public OPCList createOPCList(int[] pc, short[] mask) {
        return new OPCListImpl(pc, mask);
    }

    public ServiceIndicators createServiceIndicators(short[] inds) {
        return new ServiceIndicatorsImpl(inds);
    }

    public TrafficModeType createTrafficModeType(int mode) {
        return new TrafficModeTypeImpl(mode);
    }

    public RegistrationStatus createRegistrationStatus(int status) {
        return new RegistrationStatusImpl(status);
    }

    public DiagnosticInfo createDiagnosticInfo(String info) {
        return new DiagnosticInfoImpl(info);
    }

    public RoutingKey createRoutingKey(LocalRKIdentifier localRkId, RoutingContext rc, TrafficModeType trafMdTy,
            NetworkAppearance netApp, DestinationPointCode[] dpc, ServiceIndicators[] servInds, OPCList[] opcList) {
        return new RoutingKeyImpl(localRkId, rc, trafMdTy, netApp, dpc, servInds, opcList);
    }

    public RegistrationResult createRegistrationResult(LocalRKIdentifier localRkId, RegistrationStatus status, RoutingContext rc) {
        return new RegistrationResultImpl(localRkId, status, rc);
    }

    public DeregistrationStatus createDeregistrationStatus(int status) {
        return new DeregistrationStatusImpl(status);
    }

    public DeregistrationResult createDeregistrationResult(RoutingContext rc, DeregistrationStatus status) {
        return new DeregistrationResultImpl(rc, status);
    }

    public ErrorCode createErrorCode(int code) {
        return new ErrorCodeImpl(code);
    }

    public Status createStatus(int type, int info) {
        return new StatusImpl(type, info);
    }

    public HeartbeatData createHeartbeatData(byte[] data) {
        return new HeartbeatDataImpl(data);
    }

    public Parameter createParameter(int tag, byte[] value) {
        ParameterImpl p = null;
        switch (tag) {
            case ParameterImpl.Protocol_Data:
                p = new ProtocolDataImpl(value);
                break;
            case ParameterImpl.Traffic_Mode_Type:
                p = new TrafficModeTypeImpl(value);
                break;
            case ParameterImpl.Network_Appearance:
                p = new NetworkAppearanceImpl(value);
                break;
            case ParameterImpl.Routing_Context:
                p = new RoutingContextImpl(value);
                break;
            case ParameterImpl.Correlation_ID:
                p = new CorrelationIdImpl(value);
                break;
            case ParameterImpl.Affected_Point_Code:
                p = new AffectedPointCodeImpl(value);
                break;
            case ParameterImpl.Originating_Point_Code_List:
                p = new OPCListImpl(value);
                break;
            case ParameterImpl.Destination_Point_Code:
                p = new DestinationPointCodeImpl(value);
                break;
            case ParameterImpl.INFO_String:
                p = new InfoStringImpl(value);
                break;
            case ParameterImpl.Concerned_Destination:
                p = new ConcernedDPCImpl(value);
                break;
            case ParameterImpl.Congestion_Indications:
                p = new CongestedIndicationImpl(value);
                break;
            case ParameterImpl.User_Cause:
                p = new UserCauseImpl(value);
                break;
            case ParameterImpl.ASP_Identifier:
                p = new ASPIdentifierImpl(value);
                break;
            case ParameterImpl.Local_Routing_Key_Identifier:
                p = new LocalRKIdentifierImpl(value);
                break;
            case ParameterImpl.Service_Indicators:
                p = new ServiceIndicatorsImpl(value);
                break;
            case ParameterImpl.Routing_Key:
                p = new RoutingKeyImpl(value);
                break;
            case ParameterImpl.Registration_Status:
                p = new RegistrationStatusImpl(value);
                break;
            case ParameterImpl.Registration_Result:
                p = new RegistrationResultImpl(value);
                break;
            case ParameterImpl.Deregistration_Status:
                p = new DeregistrationStatusImpl(value);
                break;
            case ParameterImpl.Deregistration_Result:
                p = new DeregistrationResultImpl(value);
                break;
            case ParameterImpl.Diagnostic_Information:
                p = new DiagnosticInfoImpl(value);
                break;
            case ParameterImpl.Error_Code:
                p = new ErrorCodeImpl(value);
                break;
            case ParameterImpl.Status:
                p = new StatusImpl(value);
                break;
            case ParameterImpl.Heartbeat_Data:
                p = new HeartbeatDataImpl(value);
                break;
            default:
                p = new UnknownParameterImpl(tag, value.length, value);
                break;
        }
        return p;
    }

}
