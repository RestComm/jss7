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

package org.mobicents.protocols.ss7.m3ua.parameter;

import org.mobicents.protocols.ss7.m3ua.parameter.CongestedIndication.CongestionLevel;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;

/**
 * Constructs parameters.
 * 
 * @author amit bhayani
 * @author kulikov
 */
public interface ParameterFactory {
    /**
     * Constructs Protocol Data parameter.
     * 
     * @param opc
     *            the origination point code
     * @param dpc
     *            the destination point code
     * @param si
     *            the service indicator
     * @param ni
     *            the network indicator
     * @param mp
     *            the message priority indicator
     * @param sls
     *            the signaling link selection
     * @param data
     *            message payload
     * @return Protocol data parameter
     */
    public ProtocolData createProtocolData(int opc, int dpc, int si, int ni,
            int mp, int sls, byte[] data);

    /**
     * Constructs Protocol Data parameter.
     * 
     * @param mp
     *            message priority
     * @param msu
     *            the message signaling unit
     * @return Protocol data parameter
     */
    public ProtocolData createProtocolData(Mtp3TransferPrimitive mtp3TransferPrimitive);
    
    public ProtocolData createProtocolData(byte[] payloadData);

    public NetworkAppearance createNetworkAppearance(long netApp);

    public RoutingContext createRoutingContext(long[] routCntx);

    public CorrelationId createCorrelationId(long corrId);

    public AffectedPointCode createAffectedPointCode(int[] pc, short[] mask);

    public DestinationPointCode createDestinationPointCode(int pc, short mask);

    public InfoString createInfoString(String string);

    public ConcernedDPC createConcernedDPC(int pointCode);

    public CongestedIndication createCongestedIndication(CongestionLevel level);

    public UserCause createUserCause(int user, int cause);

    public ASPIdentifier createASPIdentifier(long aspId);

    public LocalRKIdentifier createLocalRKIdentifier(long id);

    public OPCList createOPCList(int[] pc, short[] mask);

    public ServiceIndicators createServiceIndicators(short[] inds);

    public TrafficModeType createTrafficModeType(int mode); 

    public RegistrationStatus createRegistrationStatus(int status);

    public DiagnosticInfo createDiagnosticInfo(String info);

    public RoutingKey createRoutingKey(LocalRKIdentifier localRkId,
            RoutingContext rc, TrafficModeType trafMdTy,
            NetworkAppearance netApp, DestinationPointCode[] dpc,
            ServiceIndicators[] servInds, OPCList[] opcList);

    public RegistrationResult createRegistrationResult(
            LocalRKIdentifier localRkId, RegistrationStatus status,
            RoutingContext rc);

    public DeregistrationStatus createDeregistrationStatus(int status);

    public DeregistrationResult createDeregistrationResult(RoutingContext rc,
            DeregistrationStatus status);

    public ErrorCode createErrorCode(int code);

    public Status createStatus(int type, int info);
    
    public HeartbeatData createHeartbeatData(byte[] data);

}
