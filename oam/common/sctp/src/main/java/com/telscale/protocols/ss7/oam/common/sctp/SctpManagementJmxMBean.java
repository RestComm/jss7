/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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
package com.telscale.protocols.ss7.oam.common.sctp;

import org.mobicents.protocols.api.Association;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.api.Management;
import org.mobicents.protocols.api.Server;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 *
 */
public interface SctpManagementJmxMBean extends Management {
    Server addSctpServer(String serverName, String hostAddress, int port, IpChannelType ipChannelType,
            boolean acceptAnonymousConnections, int maxConcurrentConnectionsCount, String extraHostAddresses) throws Exception;

    Association addSctpAssociation(String hostAddress, int hostPort, String peerAddress, int peerPort, String assocName,
            IpChannelType ipChannelType, String extraHostAddresses) throws Exception;

    Association addSctpServerAssociation(String peerAddress, int peerPort, String serverName, String assocName,
            IpChannelType ipChannelType) throws Exception;

}
